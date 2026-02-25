package com.kevin.ods.service;

import java.util.List;
import java.util.Map;
import com.kevin.ods.domain.ExcelTable;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel上传表Service接口
 * 
 * @author kevin
 * @date 2024-01-08
 */
public interface IExcelTableService 
{
    /**
     * 查询Excel上传表
     * 
     * @param tableId Excel上传表主键
     * @return Excel上传表
     */
    public ExcelTable selectExcelTableById(Long tableId);

    /**
     * 查询Excel上传表列表
     * 
     * @param excelTable Excel上传表
     * @return Excel上传表集合
     */
    public List<ExcelTable> selectExcelTableList(ExcelTable excelTable);

    /**
     * 上传Excel并创建表
     * 
     * @param file Excel文件
     * @param tableComment 表描述
     * @return Excel上传表信息
     */
    public ExcelTable uploadExcel(MultipartFile file, String tableComment);

    /**
     * 修改Excel上传表
     * 
     * @param excelTable Excel上传表
     * @return 结果
     */
    public int updateExcelTable(ExcelTable excelTable);

    /**
     * 批量删除Excel上传表
     * 
     * @param tableIds 需要删除的Excel上传表主键集合
     * @return 结果
     */
    public int deleteExcelTableByIds(Long[] tableIds);

    /**
     * 删除Excel上传表信息
     * 
     * @param tableId Excel上传表主键
     * @return 结果
     */
    public int deleteExcelTableById(Long tableId);

    /**
     * 获取表数据（分页）
     * 
     * @param tableId 表ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页数据
     */
    public TableDataInfo getTableData(Long tableId, Integer pageNum, Integer pageSize);
}
