package com.kevin.ods.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.kevin.ods.domain.ExcelTable;
import com.kevin.ods.domain.ExcelTableColumn;
import com.kevin.ods.domain.ExcelUploadBatchResult;
import com.kevin.ods.domain.ExcelUploadSheetResult;
import com.kevin.ods.mapper.ExcelTableColumnMapper;
import com.kevin.ods.mapper.ExcelTableMapper;
import com.kevin.ods.service.IExcelTableService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private TransactionTemplate transactionTemplate;

    /** 批量插入大小 */
    private static final int BATCH_SIZE = 500;

    /** 导入模式：全部sheet */
    private static final String IMPORT_MODE_ALL_SHEETS = "ALL_SHEETS";

    /** 导入模式：指定sheet */
    private static final String IMPORT_MODE_SELECTED_SHEETS = "SELECTED_SHEETS";

    /** 日期格式列表 */
    private static final String[] DATE_PATTERNS = new String[] {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "yyyyMMdd"
    };

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
     * 上传Excel并创建表（兼容旧方法，默认导入全部sheet并返回第一个成功sheet结果）
     *
     * @param file Excel文件
     * @param tableComment 表描述
     * @return Excel上传表信息
     */
    @Override
    public ExcelTable uploadExcel(MultipartFile file, String tableComment)
    {
        ExcelUploadBatchResult batchResult = uploadExcelBatch(file, tableComment, IMPORT_MODE_ALL_SHEETS, null);
        if (batchResult.getSheetResults() == null || batchResult.getSheetResults().isEmpty())
        {
            throw new ServiceException("上传失败：未检测到可处理的sheet");
        }

        for (ExcelUploadSheetResult sheetResult : batchResult.getSheetResults())
        {
            if (sheetResult.isSuccess())
            {
                ExcelTable excelTable = new ExcelTable();
                excelTable.setTableId(sheetResult.getTableId());
                excelTable.setTableName(sheetResult.getTableName());
                excelTable.setTableComment(sheetResult.getTableComment());
                excelTable.setFileName(batchResult.getFileName());
                excelTable.setRowCount(sheetResult.getRowCount());
                excelTable.setColumns(sheetResult.getColumns());
                excelTable.setNewTable(sheetResult.isNewTable());
                return excelTable;
            }
        }

        throw new ServiceException("上传失败：" + batchResult.getSheetResults().get(0).getMessage());
    }

    /**
     * 上传Excel并按sheet批量建表
     *
     * @param file Excel文件
     * @param tableComment 表描述（基础描述）
     * @param importMode 导入模式：ALL_SHEETS 或 SELECTED_SHEETS
     * @param sheetNames 指定sheet名称（逗号分隔，仅在SELECTED_SHEETS时生效）
     * @return 批量上传结果
     */
    @Override
    public ExcelUploadBatchResult uploadExcelBatch(MultipartFile file, String tableComment, String importMode, String sheetNames)
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

        if (StringUtils.isEmpty(tableComment))
        {
            int suffixIndex = fileName.lastIndexOf('.');
            tableComment = suffixIndex > 0 ? fileName.substring(0, suffixIndex) : fileName;
        }

        List<SheetMeta> sheetMetas = resolveTargetSheets(file, importMode, sheetNames);
        if (sheetMetas.isEmpty())
        {
            throw new ServiceException("未找到可导入的sheet");
        }

        String batchId = String.valueOf(System.currentTimeMillis());
        boolean multiSheet = sheetMetas.size() > 1;

        ExcelUploadBatchResult batchResult = new ExcelUploadBatchResult();
        batchResult.setBatchId(batchId);
        batchResult.setFileName(fileName);
        batchResult.setTotalSheets(sheetMetas.size());

        List<ExcelUploadSheetResult> sheetResults = new ArrayList<>();
        int successSheets = 0;
        int failedSheets = 0;
        int newTables = 0;
        int existingTables = 0;
        int totalRows = 0;

        for (SheetMeta sheetMeta : sheetMetas)
        {
            ExcelUploadSheetResult sheetResult = new ExcelUploadSheetResult();
            sheetResult.setSheetNo(sheetMeta.getSheetNo());
            sheetResult.setSheetName(sheetMeta.getSheetName());

            try
            {
                final String finalTableComment = tableComment;
                ExcelUploadSheetResult txResult = transactionTemplate.execute(status -> {
                    try
                    {
                        return processSingleSheet(file, finalTableComment, fileName, batchId, sheetMeta, multiSheet);
                    }
                    catch (RuntimeException e)
                    {
                        status.setRollbackOnly();
                        throw e;
                    }
                });

                if (txResult == null)
                {
                    throw new ServiceException("sheet处理结果为空");
                }

                sheetResult = txResult;
            }
            catch (Exception e)
            {
                sheetResult.setSuccess(false);
                String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "未知异常";
                sheetResult.setMessage(message);
            }

            if (sheetResult.isSuccess())
            {
                successSheets++;
                totalRows += sheetResult.getRowCount() == null ? 0 : sheetResult.getRowCount();
                if (sheetResult.isNewTable())
                {
                    newTables++;
                }
                else
                {
                    existingTables++;
                }
            }
            else
            {
                failedSheets++;
            }

            sheetResults.add(sheetResult);
        }

        batchResult.setSuccessSheets(successSheets);
        batchResult.setFailedSheets(failedSheets);
        batchResult.setNewTables(newTables);
        batchResult.setExistingTables(existingTables);
        batchResult.setTotalRows(totalRows);
        batchResult.setSheetResults(sheetResults);

        return batchResult;
    }

    /**
     * 处理单个sheet
     */
    private ExcelUploadSheetResult processSingleSheet(MultipartFile file, String tableComment, String fileName,
                                                      String batchId, SheetMeta sheetMeta, boolean multiSheet)
    {
        ExcelParseResult parseResult;
        try
        {
            parseResult = parseExcelStructure(file, sheetMeta.getSheetNo());
        }
        catch (IOException e)
        {
            throw new ServiceException("读取sheet失败(" + sheetMeta.getSheetName() + "): " + e.getMessage());
        }

        if (parseResult.getColumnMetas().isEmpty())
        {
            throw new ServiceException("sheet[" + sheetMeta.getSheetName() + "]未检测到有效表头");
        }

        List<ExcelTableColumn> columns = toColumns(parseResult.getColumnMetas());
        String structureHash = generateStructureHash(columns, sheetMeta.getSheetName());

        ExcelUploadSheetResult sheetResult = new ExcelUploadSheetResult();
        sheetResult.setSheetNo(sheetMeta.getSheetNo());
        sheetResult.setSheetName(sheetMeta.getSheetName());

        ExcelTable existingTable = excelTableMapper.selectExcelTableByHash(structureHash);
        if (existingTable != null)
        {
            List<ExcelTableColumn> existingColumns = excelTableColumnMapper.selectExcelTableColumnByTableId(existingTable.getTableId());
            sheetResult.setSuccess(true);
            sheetResult.setNewTable(false);
            sheetResult.setMessage("表结构已存在，复用已有表");
            sheetResult.setTableId(existingTable.getTableId());
            sheetResult.setTableName(existingTable.getTableName());
            sheetResult.setTableComment(existingTable.getTableComment());
            sheetResult.setRowCount(existingTable.getRowCount());
            sheetResult.setColumns(existingColumns);
            return sheetResult;
        }

        String tableName = generateTableName(batchId, sheetMeta.getSheetNo());
        String sheetTableComment = buildSheetTableComment(tableComment, sheetMeta.getSheetName(), multiSheet);

        ExcelTable excelTable = new ExcelTable();
        excelTable.setTableName(tableName);
        excelTable.setTableComment(sheetTableComment);
        excelTable.setStructureHash(structureHash);
        excelTable.setFileName(fileName);
        excelTable.setStatus("0");
        excelTable.setCreateBy(SecurityUtils.getUsername());
        excelTable.setNewTable(true);

        excelTableMapper.insertExcelTable(excelTable);

        for (int i = 0; i < columns.size(); i++)
        {
            ExcelTableColumn column = columns.get(i);
            column.setTableId(excelTable.getTableId());
            column.setSort(i);
            column.setIsPk("0");
            column.setIsRequired("0");
        }

        excelTableColumnMapper.batchInsertExcelTableColumn(columns);

        createDatabaseTable(tableName, sheetTableComment, columns);

        int rowCount;
        try
        {
            rowCount = insertExcelData(file, tableName, parseResult.getColumnMetas(), sheetMeta.getSheetNo());
        }
        catch (IOException e)
        {
            throw new ServiceException("写入sheet数据失败(" + sheetMeta.getSheetName() + "): " + e.getMessage());
        }

        ExcelTable updateTable = new ExcelTable();
        updateTable.setTableId(excelTable.getTableId());
        updateTable.setRowCount(rowCount);
        excelTableMapper.updateExcelTable(updateTable);

        sheetResult.setSuccess(true);
        sheetResult.setNewTable(true);
        sheetResult.setMessage("上传成功，已创建新表");
        sheetResult.setTableId(excelTable.getTableId());
        sheetResult.setTableName(tableName);
        sheetResult.setTableComment(sheetTableComment);
        sheetResult.setRowCount(rowCount);
        sheetResult.setColumns(columns);
        return sheetResult;
    }

    /**
     * 解析需要导入的sheet列表
     */
    private List<SheetMeta> resolveTargetSheets(MultipartFile file, String importMode, String sheetNames)
    {
        String finalImportMode = StringUtils.upperCase(StringUtils.trimToEmpty(importMode));
        if (StringUtils.isEmpty(finalImportMode))
        {
            finalImportMode = IMPORT_MODE_ALL_SHEETS;
        }

        Set<String> selectedSheetNames = new LinkedHashSet<>();
        if (IMPORT_MODE_SELECTED_SHEETS.equals(finalImportMode))
        {
            if (StringUtils.isEmpty(sheetNames))
            {
                throw new ServiceException("导入模式为SELECTED_SHEETS时，sheetNames不能为空");
            }

            String[] nameArray = sheetNames.split(",");
            for (String name : nameArray)
            {
                String trimName = StringUtils.trim(name);
                if (StringUtils.isNotEmpty(trimName))
                {
                    selectedSheetNames.add(trimName);
                }
            }

            if (selectedSheetNames.isEmpty())
            {
                throw new ServiceException("sheetNames未包含有效sheet名称");
            }
        }

        List<SheetMeta> sheetMetas = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream))
        {
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++)
            {
                String sheetName = workbook.getSheetName(i);
                if (StringUtils.isEmpty(sheetName))
                {
                    sheetName = "Sheet" + (i + 1);
                }

                if (IMPORT_MODE_SELECTED_SHEETS.equals(finalImportMode) && !selectedSheetNames.contains(sheetName))
                {
                    continue;
                }

                sheetMetas.add(new SheetMeta(i, sheetName));
            }
        }
        catch (Exception e)
        {
            throw new ServiceException("解析sheet列表失败: " + e.getMessage());
        }

        if (IMPORT_MODE_SELECTED_SHEETS.equals(finalImportMode) && sheetMetas.isEmpty())
        {
            throw new ServiceException("未匹配到指定sheet，请检查sheetNames是否正确");
        }

        return sheetMetas;
    }

    /**
     * 解析Excel结构（表头 + 样例数据行）
     */
    private ExcelParseResult parseExcelStructure(MultipartFile file, int sheetNo) throws IOException
    {
        final Map<Integer, String> headerMap = new LinkedHashMap<>();
        final List<Map<Integer, String>> firstDataRows = new ArrayList<>(1);

        try (InputStream inputStream = file.getInputStream())
        {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>()
            {
                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context)
                {
                    if (firstDataRows.isEmpty() && !isEmptyRow(data))
                    {
                        firstDataRows.add(new HashMap<>(data));
                    }
                }

                @Override
                public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context)
                {
                    if (headMap != null)
                    {
                        headerMap.putAll(headMap);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context)
                {
                }
            }).sheet(sheetNo).headRowNumber(1).doRead();
        }

        if (headerMap.isEmpty())
        {
            throw new ServiceException("Excel表头为空，无法创建表");
        }

        Map<Integer, String> sampleRow = firstDataRows.isEmpty() ? Collections.emptyMap() : firstDataRows.get(0);
        List<Integer> columnIndexes = new ArrayList<>(headerMap.keySet());
        Collections.sort(columnIndexes);

        List<ColumnMeta> columnMetas = new ArrayList<>();
        Set<String> usedColumnNames = new HashSet<>();
        for (Integer columnIndex : columnIndexes)
        {
            String columnComment = StringUtils.trimToEmpty(headerMap.get(columnIndex));
            if (StringUtils.isEmpty(columnComment))
            {
                continue;
            }

            String rawColumnName = convertToPinyin(columnComment);
            String columnName = buildUniqueColumnName(rawColumnName, columnIndex, usedColumnNames);

            String sampleValue = sampleRow.get(columnIndex);
            String[] typeInfo = inferDataType(sampleValue);

            ExcelTableColumn column = new ExcelTableColumn();
            column.setColumnName(columnName);
            column.setColumnComment(columnComment);
            column.setColumnType(typeInfo[0]);
            column.setJavaType(typeInfo[1]);

            columnMetas.add(new ColumnMeta(columnIndex, column));
        }

        return new ExcelParseResult(columnMetas);
    }

    /**
     * 构建唯一列名，避免重复表头生成同名列导致建表失败
     */
    private String buildUniqueColumnName(String rawColumnName, Integer columnIndex, Set<String> usedColumnNames)
    {
        String baseColumnName = StringUtils.isNotEmpty(rawColumnName) ? rawColumnName : "col_" + columnIndex;
        String uniqueColumnName = baseColumnName;
        int suffix = 2;
        while (usedColumnNames.contains(uniqueColumnName))
        {
            uniqueColumnName = baseColumnName + "_" + suffix;
            suffix++;
        }
        usedColumnNames.add(uniqueColumnName);
        return uniqueColumnName;
    }

    /**
     * 推断数据类型
     */
    private String[] inferDataType(String value)
    {
        String columnType = "VARCHAR(255)";
        String javaType = "String";

        if (StringUtils.isEmpty(value))
        {
            return new String[] { columnType, javaType };
        }

        String trimValue = value.trim();
        if (StringUtils.isEmpty(trimValue))
        {
            return new String[] { columnType, javaType };
        }

        if (parseDate(trimValue) != null)
        {
            return new String[] { "DATETIME", "Date" };
        }

        String normalizeValue = trimValue.replace(",", "");
        try
        {
            BigDecimal decimalValue = new BigDecimal(normalizeValue);
            if (decimalValue.scale() > 0)
            {
                return new String[] { "DECIMAL(19,4)", "BigDecimal" };
            }

            if (decimalValue.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0
                || decimalValue.compareTo(new BigDecimal(Integer.MIN_VALUE)) < 0)
            {
                return new String[] { "BIGINT", "Long" };
            }
            return new String[] { "INT", "Integer" };
        }
        catch (Exception e)
        {
        }

        int length = trimValue.length();
        if (length > 2000)
        {
            columnType = "TEXT";
        }
        else if (length > 500)
        {
            columnType = "VARCHAR(2000)";
        }
        else if (length > 100)
        {
            columnType = "VARCHAR(500)";
        }

        return new String[] { columnType, javaType };
    }

    /**
     * 生成结构Hash
     */
    private String generateStructureHash(List<ExcelTableColumn> columns, String sheetName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("sheet:").append(sheetName).append("|");
        for (ExcelTableColumn column : columns)
        {
            sb.append(column.getColumnName()).append(":").append(column.getColumnType()).append("|");
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
            return String.valueOf(sb.toString().hashCode());
        }
    }

    /**
     * 生成表名
     */
    private String generateTableName(String batchId, Integer sheetNo)
    {
        return "upload_" + batchId + "_" + (sheetNo + 1);
    }

    /**
     * 生成表描述
     */
    private String buildSheetTableComment(String tableComment, String sheetName, boolean multiSheet)
    {
        String result = multiSheet ? tableComment + "-" + sheetName : tableComment;
        return limitLength(result, 200);
    }

    /**
     * 字符串长度截断
     */
    private String limitLength(String value, int maxLength)
    {
        if (StringUtils.isEmpty(value) || value.length() <= maxLength)
        {
            return value;
        }
        return value.substring(0, maxLength);
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
        pinyin = pinyin.replaceAll("_+", "_");
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
            sql.append("    ").append(column.getColumnName()).append(" ").append(column.getColumnType());

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
    private int insertExcelData(MultipartFile file, String tableName, List<ColumnMeta> columnMetas, int sheetNo) throws IOException
    {
        final List<ExcelTableColumn> columns = toColumns(columnMetas);
        final List<Integer> columnIndexes = new ArrayList<>();
        for (ColumnMeta columnMeta : columnMetas)
        {
            columnIndexes.add(columnMeta.getColumnIndex());
        }

        final List<String> valueList = new ArrayList<>();
        final int[] rowCount = new int[] { 0 };

        try (InputStream inputStream = file.getInputStream())
        {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, String>>()
            {
                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context)
                {
                    if (isEmptyRow(data))
                    {
                        return;
                    }

                    StringBuilder values = new StringBuilder("(");
                    for (int i = 0; i < columnIndexes.size(); i++)
                    {
                        String cellValue = data.get(columnIndexes.get(i));
                        values.append(formatCellValue(cellValue, columns.get(i).getColumnType()));

                        if (i < columnIndexes.size() - 1)
                        {
                            values.append(", ");
                        }
                    }
                    values.append(")");
                    valueList.add(values.toString());
                    rowCount[0]++;

                    if (valueList.size() >= BATCH_SIZE)
                    {
                        batchInsert(tableName, columns, valueList);
                        valueList.clear();
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context)
                {
                }
            }).sheet(sheetNo).headRowNumber(1).doRead();
        }

        if (!valueList.isEmpty())
        {
            batchInsert(tableName, columns, valueList);
        }

        return rowCount[0];
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
    private String formatCellValue(String value, String columnType)
    {
        if (StringUtils.isEmpty(value))
        {
            return "NULL";
        }

        String trimValue = value.trim();
        if (StringUtils.isEmpty(trimValue))
        {
            return "NULL";
        }

        if (columnType.startsWith("DATETIME"))
        {
            Date date = parseDate(trimValue);
            if (date == null)
            {
                return "NULL";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + sdf.format(date) + "'";
        }

        if (columnType.startsWith("DECIMAL") || columnType.startsWith("BIGINT") || columnType.startsWith("INT"))
        {
            String normalizeValue = trimValue.replace(",", "");
            if (StringUtils.equalsAnyIgnoreCase(normalizeValue, "true", "yes"))
            {
                return "1";
            }
            if (StringUtils.equalsAnyIgnoreCase(normalizeValue, "false", "no"))
            {
                return "0";
            }
            try
            {
                BigDecimal decimal = new BigDecimal(normalizeValue);
                if (columnType.startsWith("INT") || columnType.startsWith("BIGINT"))
                {
                    return decimal.toBigInteger().toString();
                }
                return decimal.toPlainString();
            }
            catch (Exception e)
            {
                return "NULL";
            }
        }

        return "'" + trimValue.replace("'", "\\'") + "'";
    }

    /**
     * 解析日期
     */
    private Date parseDate(String value)
    {
        for (String pattern : DATE_PATTERNS)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(false);
                return sdf.parse(value);
            }
            catch (ParseException e)
            {
            }
        }
        return null;
    }

    /**
     * 判断是否空行
     */
    private boolean isEmptyRow(Map<Integer, String> rowData)
    {
        if (rowData == null || rowData.isEmpty())
        {
            return true;
        }

        for (String cellValue : rowData.values())
        {
            if (StringUtils.isNotBlank(cellValue))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换ColumnMeta列表为Column列表
     */
    private List<ExcelTableColumn> toColumns(List<ColumnMeta> columnMetas)
    {
        List<ExcelTableColumn> columns = new ArrayList<>(columnMetas.size());
        for (ColumnMeta columnMeta : columnMetas)
        {
            columns.add(columnMeta.getColumn());
        }
        return columns;
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
    public int deleteExcelTableByIds(Long[] tableIds)
    {
        for (Long tableId : tableIds)
        {
            excelTableColumnMapper.deleteExcelTableColumnByTableId(tableId);
        }
        return excelTableMapper.deleteExcelTableByIds(tableIds);
    }

    /**
     * 删除Excel上传表信息
     *
     * @param tableId Excel上传表主键
     * @return 结果
     */
    @Override
    public int deleteExcelTableById(Long tableId)
    {
        excelTableColumnMapper.deleteExcelTableColumnByTableId(tableId);
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
        ExcelTable excelTable = excelTableMapper.selectExcelTableById(tableId);
        if (excelTable == null)
        {
            throw new ServiceException("表不存在");
        }

        String tableName = excelTable.getTableName();
        if (!tableName.startsWith("upload_"))
        {
            throw new ServiceException("非法表名");
        }

        int startRow = (pageNum - 1) * pageSize;
        Long total = excelTableMapper.selectTableDataCount(tableName);
        List<Map<String, Object>> dataList = excelTableMapper.selectTableData(tableName, startRow, pageSize);

        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setCode(200);
        dataInfo.setMsg("查询成功");
        dataInfo.setTotal(total);
        dataInfo.setRows(dataList);

        return dataInfo;
    }

    /**
     * Excel列元数据
     */
    private static class ColumnMeta
    {
        private final Integer columnIndex;
        private final ExcelTableColumn column;

        private ColumnMeta(Integer columnIndex, ExcelTableColumn column)
        {
            this.columnIndex = columnIndex;
            this.column = column;
        }

        public Integer getColumnIndex()
        {
            return columnIndex;
        }

        public ExcelTableColumn getColumn()
        {
            return column;
        }
    }

    /**
     * Excel解析结果
     */
    private static class ExcelParseResult
    {
        private final List<ColumnMeta> columnMetas;

        private ExcelParseResult(List<ColumnMeta> columnMetas)
        {
            this.columnMetas = columnMetas;
        }

        public List<ColumnMeta> getColumnMetas()
        {
            return columnMetas;
        }
    }

    /**
     * sheet元信息
     */
    private static class SheetMeta
    {
        private final Integer sheetNo;
        private final String sheetName;

        private SheetMeta(Integer sheetNo, String sheetName)
        {
            this.sheetNo = sheetNo;
            this.sheetName = sheetName;
        }

        public Integer getSheetNo()
        {
            return sheetNo;
        }

        public String getSheetName()
        {
            return sheetName;
        }
    }
}
