package com.ruoyi.system.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务客户信息表 business_customer
 * 
 * @author ruoyi
 */
public class BusinessCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客户ID */
    @Excel(name = "客户序号", cellType = ColumnType.NUMERIC, prompt = "客户编号")
    private Long customerId;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    @NotBlank(message = "客户姓名不能为空")
    @Size(max = 50, message = "客户姓名长度不能超过50个字符")
    private String customerName;

    /** 手机号码 */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    @Pattern(regexp = "^$|^1\\d{10}$", message = "手机号码格式不正确")
    private String phone;

    /** 电子邮箱 */
    @Excel(name = "电子邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "电子邮箱长度不能超过50个字符")
    private String email;

    /** 联系地址 */
    @Excel(name = "联系地址")
    @Size(max = 255, message = "联系地址长度不能超过255个字符")
    private String address;

    /** 身份证号 */
    @Excel(name = "身份证号", cellType = ColumnType.TEXT)
    @Size(max = 18, message = "身份证号长度不能超过18个字符")
    @Pattern(regexp = "^$|^\\d{15}$|^\\d{17}[0-9Xx]$", message = "身份证号格式不正确")
    private String idCard;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @NotBlank(message = "客户状态不能为空")
    @Pattern(regexp = "^[01]$", message = "客户状态值只能为0或1")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("customerId", getCustomerId())
            .append("customerName", getCustomerName())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("address", getAddress())
            .append("idCard", getIdCard())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
