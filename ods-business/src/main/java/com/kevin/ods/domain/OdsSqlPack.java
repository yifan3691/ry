package com.kevin.ods.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * ods对象 ods_sql_pack
 * 
 * @author kevin
 * @date 2024-05-29
 */
public class OdsSqlPack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long Id;

    /** 名字 */
    @Excel(name = "名字")
    private String name;

    /** sql内容 */
    @Excel(name = "sql内容")
    private String sqlVarchar;

    /** sql_priority */
    @Excel(name = "sql_priority")
    private Long sqlPriority;

    /** sql运行组 */
    @Excel(name = "sql运行组")
    private String sqlGroup;

    /** 各组运行优先级 */
    @Excel(name = "各组运行优先级")
    private String groupPriority;

    /** 程序运行组 */
    @Excel(name = "程序运行组")
    private String procedureGroup;

    /** sql出参(sql组范围，逗号分隔) */
    private String groupOutValue;

    /** sql入参(sql组范围，逗号分隔) */
    private String groupInValue;

    /** sql出参(最大范围，整个运行程序可以使用，逗号分隔) */
    private String procedureOutValue;

    /** sql入参(最大范围，整个运行程序可以使用，逗号分隔) */
    private String procedureInValue;

    /** 主键 */
    private Long actualid;

    /** 创建时间 */
    private Date createdate;

    /** 创建人 */
    private String createuserid;

    /** 1 */
    private String owneruserid;

    /** 1 */
    private String partitionid;

    /** 1 */
    private String schemaname;

    /** 1 */
    private String setid;

    /** 1 */
    private String structureid;

    /** 1 */
    private String tenantid;

    /** 更新时间 */
    private Date updatedate;

    /** 1 */
    private String updateuserid;

    /** sql运行组code */
    private String sqlGroupCode;

    /** 状态EFFECTIVE:有效 */
    private String status;

    public void setId(Long Id) 
    {
        this.Id = Id;
    }

    public Long getId() 
    {
        return Id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setSqlVarchar(String sqlVarchar) 
    {
        this.sqlVarchar = sqlVarchar;
    }

    public String getSqlVarchar() 
    {
        return sqlVarchar;
    }
    public void setSqlPriority(Long sqlPriority) 
    {
        this.sqlPriority = sqlPriority;
    }

    public Long getSqlPriority() 
    {
        return sqlPriority;
    }
    public void setSqlGroup(String sqlGroup) 
    {
        this.sqlGroup = sqlGroup;
    }

    public String getSqlGroup() 
    {
        return sqlGroup;
    }
    public void setGroupPriority(String groupPriority) 
    {
        this.groupPriority = groupPriority;
    }

    public String getGroupPriority() 
    {
        return groupPriority;
    }
    public void setProcedureGroup(String procedureGroup) 
    {
        this.procedureGroup = procedureGroup;
    }

    public String getProcedureGroup() 
    {
        return procedureGroup;
    }
    public void setGroupOutValue(String groupOutValue) 
    {
        this.groupOutValue = groupOutValue;
    }

    public String getGroupOutValue() 
    {
        return groupOutValue;
    }
    public void setGroupInValue(String groupInValue) 
    {
        this.groupInValue = groupInValue;
    }

    public String getGroupInValue() 
    {
        return groupInValue;
    }
    public void setProcedureOutValue(String procedureOutValue) 
    {
        this.procedureOutValue = procedureOutValue;
    }

    public String getProcedureOutValue() 
    {
        return procedureOutValue;
    }
    public void setProcedureInValue(String procedureInValue) 
    {
        this.procedureInValue = procedureInValue;
    }

    public String getProcedureInValue() 
    {
        return procedureInValue;
    }
    public void setActualid(Long actualid) 
    {
        this.actualid = actualid;
    }

    public Long getActualid() 
    {
        return actualid;
    }
    public void setCreatedate(Date createdate) 
    {
        this.createdate = createdate;
    }

    public Date getCreatedate() 
    {
        return createdate;
    }
    public void setCreateuserid(String createuserid) 
    {
        this.createuserid = createuserid;
    }

    public String getCreateuserid() 
    {
        return createuserid;
    }
    public void setOwneruserid(String owneruserid) 
    {
        this.owneruserid = owneruserid;
    }

    public String getOwneruserid() 
    {
        return owneruserid;
    }
    public void setPartitionid(String partitionid) 
    {
        this.partitionid = partitionid;
    }

    public String getPartitionid() 
    {
        return partitionid;
    }
    public void setSchemaname(String schemaname) 
    {
        this.schemaname = schemaname;
    }

    public String getSchemaname() 
    {
        return schemaname;
    }
    public void setSetid(String setid) 
    {
        this.setid = setid;
    }

    public String getSetid() 
    {
        return setid;
    }
    public void setStructureid(String structureid) 
    {
        this.structureid = structureid;
    }

    public String getStructureid() 
    {
        return structureid;
    }
    public void setTenantid(String tenantid) 
    {
        this.tenantid = tenantid;
    }

    public String getTenantid() 
    {
        return tenantid;
    }
    public void setUpdatedate(Date updatedate) 
    {
        this.updatedate = updatedate;
    }

    public Date getUpdatedate() 
    {
        return updatedate;
    }
    public void setUpdateuserid(String updateuserid) 
    {
        this.updateuserid = updateuserid;
    }

    public String getUpdateuserid() 
    {
        return updateuserid;
    }
    public void setSqlGroupCode(String sqlGroupCode) 
    {
        this.sqlGroupCode = sqlGroupCode;
    }

    public String getSqlGroupCode() 
    {
        return sqlGroupCode;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("Id", getId())
            .append("name", getName())
            .append("sqlVarchar", getSqlVarchar())
            .append("sqlPriority", getSqlPriority())
            .append("sqlGroup", getSqlGroup())
            .append("groupPriority", getGroupPriority())
            .append("procedureGroup", getProcedureGroup())
            .append("groupOutValue", getGroupOutValue())
            .append("groupInValue", getGroupInValue())
            .append("procedureOutValue", getProcedureOutValue())
            .append("procedureInValue", getProcedureInValue())
            .append("actualid", getActualid())
            .append("createdate", getCreatedate())
            .append("createuserid", getCreateuserid())
            .append("owneruserid", getOwneruserid())
            .append("partitionid", getPartitionid())
            .append("schemaname", getSchemaname())
            .append("setid", getSetid())
            .append("structureid", getStructureid())
            .append("tenantid", getTenantid())
            .append("updatedate", getUpdatedate())
            .append("updateuserid", getUpdateuserid())
            .append("sqlGroupCode", getSqlGroupCode())
            .append("status", getStatus())
            .toString();
    }
}
