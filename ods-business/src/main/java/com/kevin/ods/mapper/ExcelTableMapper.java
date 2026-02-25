package com.kevin.ods.mapper;

import java.util.List;
import java.util.Map;
import com.kevin.ods.domain.ExcelTable;
import org.apache.ibatis.annotations.Param;

/**
 * Excel上传表Mapper接口
 * 
 * @author kevin
 * @date 2024-01-08
 */
public interface ExcelTableMapper 
{
    /**
     * 查询Excel上传表
     * 
     * @param tableId Excel上传表主键
     * @return Excel上传表
     */
    public ExcelTable selectExcelTableById(Long tableId);

    /**
     * 根据结构Hash查询表
     * 
     * @param structureHash 结构MD5
     * @return Excel上传表
     */
    public ExcelTable selectExcelTableByHash(String structureHash);

    /**
     * 查询Excel上传表列表
     * 
     * @param excelTable Excel上传表
     * @return Excel上传表集合
     */
    public List<ExcelTable> selectExcelTableList(ExcelTable excelTable);

    /**
     * 新增Excel上传表
     * 
     * @param excelTable Excel上传表
     * @return 结果
     */
    public int insertExcelTable(ExcelTable excelTable);

    /**
     * 修改Excel上传表
     * 
     * @param excelTable Excel上传表
     * @return 结果
     */
    public int updateExcelTable(ExcelTable excelTable);

    /**
     * 删除Excel上传表
     * 
     * @param tableId Excel上传表主键
     * @return 结果
     */
    public int deleteExcelTableById(Long tableId);

    /**
     * 批量删除Excel上传表
     * 
     * @param tableIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExcelTableByIds(Long[] tableIds);

    /**
     * 动态创建表
     * 
     * @param sql 建表SQL
     * @return 结果
     */
    public int createTable(String sql);

    /**
     * 动态插入数据
     * 
     * @param sql 插入SQL
     * @return 结果
     */
    public int insertData(String sql);

    /**
     * 查询动态表数据（分页）
     * 
     * @param tableName 表名
     * @param startRow 起始行
     * @param pageSize 每页大小
     * @return 数据列表
     */
    public List<Map<String, Object>> selectTableData(@Param("tableName") String tableName, 
                                                      @Param("startRow") int startRow, 
                                                      @Param("pageSize") int pageSize);

    /**
     * 查询动态表数据总数
     * 
     * @param tableName 表名
     * @return 总数
     */
    public Long selectTableDataCount(@Param("tableName") String tableName);
}
