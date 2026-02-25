package com.kevin.ods.mapper;

import java.util.List;
import com.kevin.ods.domain.ExcelTableColumn;

/**
 * Excel表列信息Mapper接口
 * 
     * @author kevin
 * @date 2024-01-08
 */
public interface ExcelTableColumnMapper 
{
    /**
     * 查询Excel表列信息
     * 
     * @param columnId Excel表列信息主键
     * @return Excel表列信息
     */
    public ExcelTableColumn selectExcelTableColumnById(Long columnId);

    /**
     * 根据表ID查询列列表
     * 
     * @param tableId 表ID
     * @return Excel表列信息集合
     */
    public List<ExcelTableColumn> selectExcelTableColumnByTableId(Long tableId);

    /**
     * 查询Excel表列信息列表
     * 
     * @param excelTableColumn Excel表列信息
     * @return Excel表列信息集合
     */
    public List<ExcelTableColumn> selectExcelTableColumnList(ExcelTableColumn excelTableColumn);

    /**
     * 新增Excel表列信息
     * 
     * @param excelTableColumn Excel表列信息
     * @return 结果
     */
    public int insertExcelTableColumn(ExcelTableColumn excelTableColumn);

    /**
     * 批量新增Excel表列信息
     * 
     * @param excelTableColumnList Excel表列信息列表
     * @return 结果
     */
    public int batchInsertExcelTableColumn(List<ExcelTableColumn> excelTableColumnList);

    /**
     * 修改Excel表列信息
     * 
     * @param excelTableColumn Excel表列信息
     * @return 结果
     */
    public int updateExcelTableColumn(ExcelTableColumn excelTableColumn);

    /**
     * 删除Excel表列信息
     * 
     * @param columnId Excel表列信息主键
     * @return 结果
     */
    public int deleteExcelTableColumnById(Long columnId);

    /**
     * 根据表ID删除列信息
     * 
     * @param tableId 表ID
     * @return 结果
     */
    public int deleteExcelTableColumnByTableId(Long tableId);

    /**
     * 批量删除Excel表列信息
     * 
     * @param columnIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExcelTableColumnByIds(Long[] columnIds);
}
