package com.kevin.ods.domain;

import java.util.List;

/**
 * 单个sheet上传结果
 *
 * @author kevin
 */
public class ExcelUploadSheetResult
{
    /** sheet序号（从0开始） */
    private Integer sheetNo;

    /** sheet名称 */
    private String sheetName;

    /** 是否成功 */
    private boolean success;

    /** 结果消息 */
    private String message;

    /** 是否新建表 */
    private boolean newTable;

    /** 表ID */
    private Long tableId;

    /** 表名 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据行数 */
    private Integer rowCount;

    /** 列信息 */
    private List<ExcelTableColumn> columns;

    public Integer getSheetNo()
    {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo)
    {
        this.sheetNo = sheetNo;
    }

    public String getSheetName()
    {
        return sheetName;
    }

    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isNewTable()
    {
        return newTable;
    }

    public void setNewTable(boolean newTable)
    {
        this.newTable = newTable;
    }

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableComment()
    {
        return tableComment;
    }

    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    public Integer getRowCount()
    {
        return rowCount;
    }

    public void setRowCount(Integer rowCount)
    {
        this.rowCount = rowCount;
    }

    public List<ExcelTableColumn> getColumns()
    {
        return columns;
    }

    public void setColumns(List<ExcelTableColumn> columns)
    {
        this.columns = columns;
    }
}
