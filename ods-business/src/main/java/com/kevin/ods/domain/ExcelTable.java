package com.kevin.ods.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Excel上传表对象 excel_table
 * 
 * @author kevin
 * @date 2024-01-08
 */
public class ExcelTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long tableId;

    /** 生成的表名(upload_xxx) */
    @Excel(name = "表名")
    private String tableName;

    /** 表描述 */
    @Excel(name = "表描述")
    private String tableComment;

    /** 结构MD5(列名+类型+顺序) */
    private String structureHash;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    private String fileName;

    /** 数据行数 */
    @Excel(name = "数据行数")
    private Integer rowCount;

    /** 状态(0正常 1停用) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 列信息列表 */
    private List<ExcelTableColumn> columns;

    /** 是否新建表（非数据库字段） */
    private boolean isNewTable;

    public void setTableId(Long tableId) 
    {
        this.tableId = tableId;
    }

    public Long getTableId() 
    {
        return tableId;
    }

    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }

    public String getTableName() 
    {
        return tableName;
    }

    public void setTableComment(String tableComment) 
    {
        this.tableComment = tableComment;
    }

    public String getTableComment() 
    {
        return tableComment;
    }

    public void setStructureHash(String structureHash) 
    {
        this.structureHash = structureHash;
    }

    public String getStructureHash() 
    {
        return structureHash;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setRowCount(Integer rowCount) 
    {
        this.rowCount = rowCount;
    }

    public Integer getRowCount() 
    {
        return rowCount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public List<ExcelTableColumn> getColumns()
    {
        return columns;
    }

    public void setColumns(List<ExcelTableColumn> columns)
    {
        this.columns = columns;
    }

    public boolean isNewTable()
    {
        return isNewTable;
    }

    public void setNewTable(boolean newTable)
    {
        isNewTable = newTable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("tableComment", getTableComment())
            .append("structureHash", getStructureHash())
            .append("fileName", getFileName())
            .append("rowCount", getRowCount())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
