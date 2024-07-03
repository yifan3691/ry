package com.kevin.ods.domain;

import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kevin
 * @version 1.0
 * @project ruoyi
 * @description 业财
 * @date 2024/6/18 14:45:06
 */
public class OdsBusinessfee extends BaseEntity {


    /** 指标代码 */
    @Excel(name = "指标代码")
    private String indicatorCode;

    /** 核保、核批时间 */
    @Excel(name = "核保、核批时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date underwritingTime;

    /** 数据源系统代码 */
    @Excel(name = "数据源系统代码")
    private String dataSourceSystemCode;

    /** 业务单号 */
    @Excel(name = "业务单号")
    private String businessno;

    /** 不含税金额差异 */
    @Excel(name = "不含税金额差异")
    private BigDecimal taxExclusiveAmountDifference;

    /** 含税金额差异 */
    @Excel(name = "含税金额差异")
    private BigDecimal taxInclusiveAmountDifference;

    /** 税额差异 */
    @Excel(name = "税额差异")
    private BigDecimal taxAmountDifference;

    /** 承保不含税金额 */
    @Excel(name = "承保不含税金额")
    private BigDecimal underwrittenTaxExclusiveAmount;

    /** 承保含税金额 */
    @Excel(name = "承保含税金额")
    private BigDecimal underwrittenTaxInclusiveAmount;

    /** 承保税额 */
    @Excel(name = "承保税额")
    private BigDecimal underwrittenTaxAmount;

    /** 收付不含税金额 */
    @Excel(name = "收付不含税金额")
    private BigDecimal paymentTaxExclusiveAmount;

    /** 收付含税金额 */
    @Excel(name = "收付含税金额")
    private BigDecimal paymentTaxInclusiveAmount;

    /** 收付税额 */
    @Excel(name = "收付税额")
    private BigDecimal paymentTaxAmount;

    /** 创建时间 */
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    /** 一级指标金额差异容忍度 */
    @Excel(name = "一级指标金额差异容忍度")
    private BigDecimal primaryIndicatorTolerance;

    /** 明细金额差异容忍度 */
    @Excel(name = "明细金额差异容忍度")
    private BigDecimal detailAmountTolerance;

    private String isCompleted;


    private String mark;

    public Date getUnderwritingTime() {
        return underwritingTime;
    }

    public void setUnderwritingTime(Date underwritingTime) {
        this.underwritingTime = underwritingTime;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public String getDataSourceSystemCode() {
        return dataSourceSystemCode;
    }

    public void setDataSourceSystemCode(String dataSourceSystemCode) {
        this.dataSourceSystemCode = dataSourceSystemCode;
    }

    public String getBusinessno() {
        return businessno;
    }

    public void setBusinessno(String businessno) {
        this.businessno = businessno;
    }

    public BigDecimal getTaxExclusiveAmountDifference() {
        return taxExclusiveAmountDifference;
    }

    public void setTaxExclusiveAmountDifference(BigDecimal taxExclusiveAmountDifference) {
        this.taxExclusiveAmountDifference = taxExclusiveAmountDifference;
    }

    public BigDecimal getTaxInclusiveAmountDifference() {
        return taxInclusiveAmountDifference;
    }

    public void setTaxInclusiveAmountDifference(BigDecimal taxInclusiveAmountDifference) {
        this.taxInclusiveAmountDifference = taxInclusiveAmountDifference;
    }

    public BigDecimal getTaxAmountDifference() {
        return taxAmountDifference;
    }

    public void setTaxAmountDifference(BigDecimal taxAmountDifference) {
        this.taxAmountDifference = taxAmountDifference;
    }

    public BigDecimal getUnderwrittenTaxExclusiveAmount() {
        return underwrittenTaxExclusiveAmount;
    }

    public void setUnderwrittenTaxExclusiveAmount(BigDecimal underwrittenTaxExclusiveAmount) {
        this.underwrittenTaxExclusiveAmount = underwrittenTaxExclusiveAmount;
    }

    public BigDecimal getUnderwrittenTaxInclusiveAmount() {
        return underwrittenTaxInclusiveAmount;
    }

    public void setUnderwrittenTaxInclusiveAmount(BigDecimal underwrittenTaxInclusiveAmount) {
        this.underwrittenTaxInclusiveAmount = underwrittenTaxInclusiveAmount;
    }

    public BigDecimal getUnderwrittenTaxAmount() {
        return underwrittenTaxAmount;
    }

    public void setUnderwrittenTaxAmount(BigDecimal underwrittenTaxAmount) {
        this.underwrittenTaxAmount = underwrittenTaxAmount;
    }

    public BigDecimal getPaymentTaxExclusiveAmount() {
        return paymentTaxExclusiveAmount;
    }

    public void setPaymentTaxExclusiveAmount(BigDecimal paymentTaxExclusiveAmount) {
        this.paymentTaxExclusiveAmount = paymentTaxExclusiveAmount;
    }

    public BigDecimal getPaymentTaxInclusiveAmount() {
        return paymentTaxInclusiveAmount;
    }

    public void setPaymentTaxInclusiveAmount(BigDecimal paymentTaxInclusiveAmount) {
        this.paymentTaxInclusiveAmount = paymentTaxInclusiveAmount;
    }

    public BigDecimal getPaymentTaxAmount() {
        return paymentTaxAmount;
    }

    public void setPaymentTaxAmount(BigDecimal paymentTaxAmount) {
        this.paymentTaxAmount = paymentTaxAmount;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public BigDecimal getPrimaryIndicatorTolerance() {
        return primaryIndicatorTolerance;
    }

    public void setPrimaryIndicatorTolerance(BigDecimal primaryIndicatorTolerance) {
        this.primaryIndicatorTolerance = primaryIndicatorTolerance;
    }

    public BigDecimal getDetailAmountTolerance() {
        return detailAmountTolerance;
    }

    public void setDetailAmountTolerance(BigDecimal detailAmountTolerance) {
        this.detailAmountTolerance = detailAmountTolerance;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
