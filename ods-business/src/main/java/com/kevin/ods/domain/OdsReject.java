package com.kevin.ods.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * ods拦截对象 ods_reject
 *
 * @author kevin
 * @date 2024-06-06
 */
public class OdsReject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String businessno;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String productcode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actualid;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String auditProgName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String auditTypeName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date intDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String businesstype;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String businessstatus;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tsflag;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long topid;

    public void setBusinessno(String businessno)
    {
        this.businessno = businessno;
    }

    public String getBusinessno()
    {
        return businessno;
    }
    public void setProductcode(String productcode)
    {
        this.productcode = productcode;
    }

    public String getProductcode()
    {
        return productcode;
    }
    public void setActualid(String actualid)
    {
        this.actualid = actualid;
    }

    public String getActualid()
    {
        return actualid;
    }
    public void setAuditProgName(String auditProgName)
    {
        this.auditProgName = auditProgName;
    }

    public String getAuditProgName()
    {
        return auditProgName;
    }
    public void setAuditTypeName(String auditTypeName)
    {
        this.auditTypeName = auditTypeName;
    }

    public String getAuditTypeName()
    {
        return auditTypeName;
    }
    public void setIntDate(Date intDate)
    {
        this.intDate = intDate;
    }

    public Date getIntDate()
    {
        return intDate;
    }
    public void setBusinesstype(String businesstype)
    {
        this.businesstype = businesstype;
    }

    public String getBusinesstype()
    {
        return businesstype;
    }
    public void setBusinessstatus(String businessstatus)
    {
        this.businessstatus = businessstatus;
    }

    public String getBusinessstatus()
    {
        return businessstatus;
    }
    public void setTsflag(String tsflag)
    {
        this.tsflag = tsflag;
    }

    public String getTsflag()
    {
        return tsflag;
    }
    public void setTopid(Long topid)
    {
        this.topid = topid;
    }

    public Long getTopid()
    {
        return topid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("businessno", getBusinessno())
            .append("productcode", getProductcode())
            .append("actualid", getActualid())
            .append("auditProgName", getAuditProgName())
            .append("auditTypeName", getAuditTypeName())
            .append("intDate", getIntDate())
            .append("businesstype", getBusinesstype())
            .append("businessstatus", getBusinessstatus())
            .append("tsflag", getTsflag())
            .append("remark", getRemark())
            .append("topid", getTopid())
            .toString();
    }
}
