package com.kevin.ods.domain;

import java.util.List;

/**
 * Excel批量上传结果
 *
 * @author kevin
 */
public class ExcelUploadBatchResult
{
    /** 批次号 */
    private String batchId;

    /** 原始文件名 */
    private String fileName;

    /** 总sheet数 */
    private Integer totalSheets;

    /** 成功sheet数 */
    private Integer successSheets;

    /** 失败sheet数 */
    private Integer failedSheets;

    /** 新建表数量 */
    private Integer newTables;

    /** 命中已有表数量 */
    private Integer existingTables;

    /** 总导入行数 */
    private Integer totalRows;

    /** 每个sheet处理明细 */
    private List<ExcelUploadSheetResult> sheetResults;

    public String getBatchId()
    {
        return batchId;
    }

    public void setBatchId(String batchId)
    {
        this.batchId = batchId;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Integer getTotalSheets()
    {
        return totalSheets;
    }

    public void setTotalSheets(Integer totalSheets)
    {
        this.totalSheets = totalSheets;
    }

    public Integer getSuccessSheets()
    {
        return successSheets;
    }

    public void setSuccessSheets(Integer successSheets)
    {
        this.successSheets = successSheets;
    }

    public Integer getFailedSheets()
    {
        return failedSheets;
    }

    public void setFailedSheets(Integer failedSheets)
    {
        this.failedSheets = failedSheets;
    }

    public Integer getNewTables()
    {
        return newTables;
    }

    public void setNewTables(Integer newTables)
    {
        this.newTables = newTables;
    }

    public Integer getExistingTables()
    {
        return existingTables;
    }

    public void setExistingTables(Integer existingTables)
    {
        this.existingTables = existingTables;
    }

    public Integer getTotalRows()
    {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows)
    {
        this.totalRows = totalRows;
    }

    public List<ExcelUploadSheetResult> getSheetResults()
    {
        return sheetResults;
    }

    public void setSheetResults(List<ExcelUploadSheetResult> sheetResults)
    {
        this.sheetResults = sheetResults;
    }
}
