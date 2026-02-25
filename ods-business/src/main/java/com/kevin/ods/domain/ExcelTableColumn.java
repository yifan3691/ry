package com.kevin.ods.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Excel表列信息对象 excel_table_column
 * 
 * @author kevin
 * @date 2024-01-08
 */
public class ExcelTableColumn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long columnId;

    /** 关联excel_table */
    private Long tableId;

    /** 列名(英文) */
    @Excel(name = "列名")
    private String columnName;

    /** 列描述(Excel表头) */
    @Excel(name = "列描述")
    private String columnComment;

    /** MySQL类型 */
    @Excel(name = "MySQL类型")
    private String columnType;

    /** Java类型 */
    private String javaType;

    /** 排序 */
    private Integer sort;

    /** 是否主键 */
    private String isPk;

    /** 是否必填 */
    private String isRequired;

    public void setColumnId(Long columnId) 
    {
        this.columnId = columnId;
    }

    public Long getColumnId() 
    {
        return columnId;
    }

    public void setTableId(Long tableId) 
    {
        this.tableId = tableId;
    }

    public Long getTableId() 
    {
        return tableId;
    }

    public void setColumnName(String columnName) 
    {
        this.columnName = columnName;
    }

    public String getColumnName() 
    {
        return columnName;
    }

    public void setColumnComment(String columnComment) 
    {
        this.columnComment = columnComment;
    }

    public String getColumnComment() 
    {
        return columnComment;
    }

    public void setColumnType(String columnType) 
    {
        this.columnType = columnType;
    }

    public String getColumnType() 
    {
        return columnType;
    }

    public void setJavaType(String javaType) 
    {
        this.javaType = javaType;
    }

    public String getJavaType() 
    {
        return javaType;
    }

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public void setIsPk(String isPk) 
    {
        this.isPk = isPk;
    }

    public String getIsPk() 
    {
        return isPk;
    }

    public void setIsRequired(String isRequired) 
    {
        this.isRequired = isRequired;
    }

    public String getIsRequired() 
    {
        return isRequired;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("columnId", getColumnId())
            .append("tableId", getTableId())
            .append("columnName", getColumnName())
            .append("columnComment", getColumnComment())
            .append("columnType", getColumnType())
            .append("javaType", getJavaType())
            .append("sort", getSort())
            .append("isPk", getIsPk())
            .append("isRequired", getIsRequired())
            .append("createTime", getCreateTime())
            .toString();
    }
}
