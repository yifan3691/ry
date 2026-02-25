package com.kevin.ods.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.page.TableDataInfo;

import com.kevin.ods.domain.ExcelTable;
import com.kevin.ods.domain.ExcelTableColumn;
import com.kevin.ods.mapper.ExcelTableColumnMapper;
import com.kevin.ods.mapper.ExcelTableMapper;
import com.kevin.ods.service.IExcelTableService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Excel上传表Service业务层处理
 * 
 * @author kevin
 * @date 2024-01-08
 */
@Service
public class ExcelTableServiceImpl implements IExcelTableService
{
    @Autowired
    private ExcelTableMapper excelTableMapper;
    
    @Autowired
    private ExcelTableColumnMapper excelTableColumnMapper;

    /** 批量插入大小 */
    private static final int BATCH_SIZE = 500;

    /**
     * 查询Excel上传表
     * 
     * @param tableId Excel上传表主键
     * @return Excel上传表
     */
    @Override
    public ExcelTable selectExcelTableById(Long tableId)
    {
        ExcelTable excelTable = excelTableMapper.selectExcelTableById(tableId);
        if (excelTable != null)
        {
            List<ExcelTableColumn> columns = excelTableColumnMapper.selectExcelTableColumnByTableId(tableId);
            excelTable.setColumns(columns);
        }
        return excelTable;
    }

    /**
     * 查询Excel上传表列表
     * 
     * @param excelTable Excel上传表
     * @return Excel上传表
     */
    @Override
    public List<ExcelTable> selectExcelTableList(ExcelTable excelTable)
    {
        return excelTableMapper.selectExcelTableList(excelTable);
    }

    /**
     * 上传Excel并创建表
     * 
     * @param file Excel文件
     * @param tableComment 表描述
     * @return Excel上传表信息
     */
    @Override
    @Transactional
    public ExcelTable uploadExcel(MultipartFile file, String tableComment)
    {
        if (file == null || file.isEmpty())
        {
            throw new ServiceException("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName))
        {
            throw new ServiceException("文件名不能为空");
        }

        // 从文件名提取表描述
        if (StringUtils.isEmpty(tableComment))
        {
            tableComment = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        // 检查空文件
        try (InputStream is = file.getInputStream())
        {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getPhysicalNumberOfRows() == 0)
            {
                throw new ServiceException("Excel文件为空，无法创建表");
            }

            // 获取表头行
            Row headerRow = sheet.getRow(0);
            if (headerRow == null)
            {
                throw new ServiceException("Excel表头为空，无法创建表");
            }

            // 解析列信息
            List<ExcelTableColumn> columns = parseColumns(sheet, headerRow);
            if (columns.isEmpty())
            {
                throw new ServiceException("未检测到有效的表头列");
            }

            // 生成结构Hash
            String structureHash = generateStructureHash(columns);

            // 检查表是否已存在
            ExcelTable existingTable = excelTableMapper.selectExcelTableByHash(structureHash);
            if (existingTable != null)
            {
                existingTable.setNewTable(false);
                List<ExcelTableColumn> existingColumns = excelTableColumnMapper.selectExcelTableColumnByTableId(existingTable.getTableId());
                existingTable.setColumns(existingColumns);
                return existingTable;
            }

            // 生成表名
            String tableName = generateTableName();

            // 创建新表对象
            ExcelTable excelTable = new ExcelTable();
            excelTable.setTableName(tableName);
            excelTable.setTableComment(tableComment);
            excelTable.setStructureHash(structureHash);
            excelTable.setFileName(fileName);
            excelTable.setStatus("0");
            excelTable.setCreateBy(SecurityUtils.getUsername());
            excelTable.setNewTable(true);

            // 保存表信息
            excelTableMapper.insertExcelTable(excelTable);

            // 设置列的表ID和排序
            for (int i = 0; i < columns.size(); i++)
            {
                ExcelTableColumn column = columns.get(i);
                column.setTableId(excelTable.getTableId());
                column.setSort(i);
                column.setIsPk("0");
                column.setIsRequired("0");
            }

            // 批量保存列信息
            excelTableColumnMapper.batchInsertExcelTableColumn(columns);
            excelTable.setColumns(columns);

            // 创建数据库表
            createDatabaseTable(tableName, tableComment, columns);

            // 插入数据
            int rowCount = insertExcelData(sheet, tableName, columns);
            excelTable.setRowCount(rowCount);

            // 更新行数
            ExcelTable updateTable = new ExcelTable();
            updateTable.setTableId(excelTable.getTableId());
            updateTable.setRowCount(rowCount);
            excelTableMapper.updateExcelTable(updateTable);

            return excelTable;
        }
        catch (IOException e)
        {
            throw new ServiceException("读取Excel文件失败: " + e.getMessage());
        }
    }

    /**
     * 解析Excel列信息
     */
    private List<ExcelTableColumn> parseColumns(Sheet sheet, Row headerRow)
    {
        List<ExcelTableColumn> columns = new ArrayList<>();
        
        // 获取数据行用于推断类型（第二行或第三行）
        Row dataRow = null;
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if (row != null && row.getPhysicalNumberOfCells() > 0)
            {
                dataRow = row;
                break;
            }
        }

        for (int i = 0; i < headerRow.getLastCellNum(); i++)
        {
            Cell headerCell = headerRow.getCell(i);
            if (headerCell == null)
            {
                continue;
            }

            String columnComment = getCellValueAsString(headerCell).trim();
            if (StringUtils.isEmpty(columnComment))
            {
                continue;
            }

            // 转换为英文列名（拼音）
            String columnName = convertToPinyin(columnComment);
            if (StringUtils.isEmpty(columnName))
            {
                columnName = "col_" + i;
            }

            // 推断数据类型
            Cell dataCell = dataRow != null ? dataRow.getCell(i) : null;
            String[] typeInfo = inferDataType(dataCell);

            ExcelTableColumn column = new ExcelTableColumn();
            column.setColumnName(columnName);
            column.setColumnComment(columnComment);
            column.setColumnType(typeInfo[0]);
            column.setJavaType(typeInfo[1]);
            columns.add(column);
        }

        return columns;
    }

    /**
     * 推断数据类型
     */
    private String[] inferDataType(Cell cell)
    {
        String columnType = "VARCHAR(255)";
        String javaType = "String";

        if (cell == null)
        {
            return new String[]{columnType, javaType};
        }

        CellType cellType = cell.getCellType();

        if (cellType == CellType.NUMERIC)
        {
            if (DateUtil.isCellDateFormatted(cell))
            {
                columnType = "DATETIME";
                javaType = "Date";
            }
            else
            {
                double value = cell.getNumericCellValue();
                if (value != Math.floor(value))
                {
                    // 有小数
                    columnType = "DECIMAL(19,4)";
                    javaType = "BigDecimal";
                }
                else if (value > Integer.MAX_VALUE)
                {
                    columnType = "BIGINT";
                    javaType = "Long";
                }
                else
                {
                    columnType = "INT";
                    javaType = "Integer";
                }
            }
        }
        else if (cellType == CellType.STRING)
        {
            String value = cell.getStringCellValue();
            if (value != null)
            {
                int length = value.length();
                if (length > 2000)
                {
                    columnType = "TEXT";
                    javaType = "String";
                }
                else if (length > 500)
                {
                    columnType = "VARCHAR(2000)";
                    javaType = "String";
                }
                else if (length > 100)
                {
                    columnType = "VARCHAR(500)";
                    javaType = "String";
                }
                else
                {
                    columnType = "VARCHAR(255)";
                    javaType = "String";
                }
            }
        }

        return new String[]{columnType, javaType};
    }

    /**
     * 生成结构Hash
     */
    private String generateStructureHash(List<ExcelTableColumn> columns)
    {
        StringBuilder sb = new StringBuilder();
        for (ExcelTableColumn column : columns)
        {
            sb.append(column.getColumnName()).append(":")
              .append(column.getColumnType()).append("|");
        }
        
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(sb.toString().getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            // 如果MD5不可用，使用简单的字符串哈希
            return String.valueOf(sb.toString().hashCode());
        }
    }

    /**
     * 生成表名
     */
    private String generateTableName()
    {
        return "upload_" + System.currentTimeMillis();
    }

    /**
     * 转换为拼音
     */
    private String convertToPinyin(String chinese)
    {
        if (StringUtils.isEmpty(chinese))
        {
            return "";
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder result = new StringBuilder();
        for (char c : chinese.toCharArray())
        {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+"))
            {
                try
                {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0)
                    {
                        result.append(pinyinArray[0]);
                    }
                }
                catch (BadHanyuPinyinOutputFormatCombination e)
                {
                    result.append(c);
                }
            }
            else if (Character.isLetterOrDigit(c))
            {
                result.append(Character.toLowerCase(c));
            }
            else
            {
                result.append("_");
            }
        }

        String pinyin = result.toString();
        // 去除连续的下划线
        pinyin = pinyin.replaceAll("_+", "_");
        // 去除首尾下划线
        pinyin = pinyin.replaceAll("^_+|_+$", "");
        
        return pinyin;
    }

    /**
     * 创建数据库表
     */
    private void createDatabaseTable(String tableName, String tableComment, List<ExcelTableColumn> columns)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");
        sql.append("    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',\n");

        for (int i = 0; i < columns.size(); i++)
        {
            ExcelTableColumn column = columns.get(i);
            sql.append("    ").append(column.getColumnName())
               .append(" ").append(column.getColumnType());
            
            if (StringUtils.isNotEmpty(column.getColumnComment()))
            {
                sql.append(" COMMENT '").append(column.getColumnComment().replace("'", "\\'")).append("'");
            }
            
            if (i < columns.size() - 1)
            {
                sql.append(",");
            }
            sql.append("\n");
        }

        sql.append(") COMMENT='").append(tableComment.replace("'", "\\'")).append("'");

        excelTableMapper.createTable(sql.toString());
    }

    /**
     * 插入Excel数据
     */
    private int insertExcelData(Sheet sheet, String tableName, List<ExcelTableColumn> columns)
    {
        int rowCount = 0;
        List<String> valueList = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if (row == null)
            {
                continue;
            }

            StringBuilder values = new StringBuilder("(");
            for (int j = 0; j < columns.size(); j++)
            {
                ExcelTableColumn column = columns.get(j);
                Cell cell = row.getCell(j);
                String value = formatCellValue(cell, column.getColumnType());
                values.append(value);
                
                if (j < columns.size() - 1)
                {
                    values.append(", ");
                }
            }
            values.append(")");
            valueList.add(values.toString());
            rowCount++;

            // 批量插入
            if (valueList.size() >= BATCH_SIZE)
            {
                batchInsert(tableName, columns, valueList);
                valueList.clear();
            }
        }

        // 插入剩余数据
        if (!valueList.isEmpty())
        {
            batchInsert(tableName, columns, valueList);
        }

        return rowCount;
    }

    /**
     * 批量插入数据
     */
    private void batchInsert(String tableName, List<ExcelTableColumn> columns, List<String> valueList)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");
        
        for (int i = 0; i < columns.size(); i++)
        {
            sql.append(columns.get(i).getColumnName());
            if (i < columns.size() - 1)
            {
                sql.append(", ");
            }
        }
        sql.append(") VALUES ");
        
        for (int i = 0; i < valueList.size(); i++)
        {
            sql.append(valueList.get(i));
            if (i < valueList.size() - 1)
            {
                sql.append(", ");
            }
        }

        excelTableMapper.insertData(sql.toString());
    }

    /**
     * 格式化单元格值为SQL值
     */
    private String formatCellValue(Cell cell, String columnType)
    {
        if (cell == null)
        {
            return "NULL";
        }

        CellType cellType = cell.getCellType();

        if (cellType == CellType.BLANK)
        {
            return "NULL";
        }
        else if (cellType == CellType.NUMERIC)
        {
            if (DateUtil.isCellDateFormatted(cell))
            {
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return "'" + sdf.format(date) + "'";
            }
            else
            {
                double value = cell.getNumericCellValue();
                if (columnType.startsWith("DECIMAL"))
                {
                    return new BigDecimal(value).toPlainString();
                }
                else if (columnType.startsWith("BIGINT"))
                {
                    return String.valueOf((long) value);
                }
                else
                {
                    return String.valueOf((int) value);
                }
            }
        }
        else if (cellType == CellType.BOOLEAN)
        {
            return cell.getBooleanCellValue() ? "1" : "0";
        }
        else if (cellType == CellType.FORMULA)
        {
            try
            {
                return "'" + cell.getStringCellValue().replace("'", "\\'") + "'";
            }
            catch (Exception e)
            {
                try
                {
                    return String.valueOf(cell.getNumericCellValue());
                }
                catch (Exception e2)
                {
                    return "NULL";
                }
            }
        }
        else
        {
            String value = cell.getStringCellValue();
            if (StringUtils.isEmpty(value))
            {
                return "NULL";
            }
            return "'" + value.replace("'", "\\'") + "'";
        }
    }

    /**
     * 获取单元格值作为字符串
     */
    private String getCellValueAsString(Cell cell)
    {
        if (cell == null)
        {
            return "";
        }

        CellType cellType = cell.getCellType();
        
        if (cellType == CellType.STRING)
        {
            return cell.getStringCellValue();
        }
        else if (cellType == CellType.NUMERIC)
        {
            if (DateUtil.isCellDateFormatted(cell))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.format(cell.getDateCellValue());
            }
            return String.valueOf(cell.getNumericCellValue());
        }
        else if (cellType == CellType.BOOLEAN)
        {
            return String.valueOf(cell.getBooleanCellValue());
        }
        else if (cellType == CellType.FORMULA)
        {
            try
            {
                return cell.getStringCellValue();
            }
            catch (Exception e)
            {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        
        return "";
    }

    /**
     * 修改Excel上传表
     * 
     * @param excelTable Excel上传表
     * @return 结果
     */
    @Override
    public int updateExcelTable(ExcelTable excelTable)
    {
        excelTable.setUpdateBy(SecurityUtils.getUsername());
        return excelTableMapper.updateExcelTable(excelTable);
    }

    /**
     * 批量删除Excel上传表
     * 
     * @param tableIds 需要删除的Excel上传表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteExcelTableByIds(Long[] tableIds)
    {
        // 删除列信息
        for (Long tableId : tableIds)
        {
            excelTableColumnMapper.deleteExcelTableColumnByTableId(tableId);
        }
        // 删除表信息
        return excelTableMapper.deleteExcelTableByIds(tableIds);
    }

    /**
     * 删除Excel上传表信息
     * 
     * @param tableId Excel上传表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteExcelTableById(Long tableId)
    {
        // 删除列信息
        excelTableColumnMapper.deleteExcelTableColumnByTableId(tableId);
        // 删除表信息
        return excelTableMapper.deleteExcelTableById(tableId);
    }

    /**
     * 获取表数据（分页）
     * 
     * @param tableId 表ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页数据
     */
    @Override
    public TableDataInfo getTableData(Long tableId, Integer pageNum, Integer pageSize)
    {
        // 获取表信息
        ExcelTable excelTable = excelTableMapper.selectExcelTableById(tableId);
        if (excelTable == null)
        {
            throw new ServiceException("表不存在");
        }

        String tableName = excelTable.getTableName();

        // 校验表名合法性（必须以 upload_ 开头，防止 SQL 注入）
        if (!tableName.startsWith("upload_"))
        {
            throw new ServiceException("非法表名");
        }

        // 计算起始行
        int startRow = (pageNum - 1) * pageSize;

        // 查询总数
        Long total = excelTableMapper.selectTableDataCount(tableName);

        // 查询数据
        List<Map<String, Object>> dataList = excelTableMapper.selectTableData(tableName, startRow, pageSize);

        // 封装返回结果
        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setCode(200);
        dataInfo.setMsg("查询成功");
        dataInfo.setTotal(total);
        dataInfo.setRows(dataList);

        return dataInfo;
    }
}
