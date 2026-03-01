package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.BusinessCustomer;

/**
 * 业务客户信息表 数据层
 * 
 * @author ruoyi
 */
public interface BusinessCustomerMapper
{
    /**
     * 查询客户信息
     * 
     * @param customerId 客户ID
     * @return 客户信息
     */
    public BusinessCustomer selectCustomerById(Long customerId);

    /**
     * 查询客户列表
     * 
     * @param businessCustomer 客户信息
     * @return 客户集合
     */
    public List<BusinessCustomer> selectCustomerList(BusinessCustomer businessCustomer);

    /**
     * 根据客户姓名查询客户信息
     * 
     * @param customerName 客户姓名
     * @return 客户信息
     */
    public BusinessCustomer selectCustomerByName(String customerName);

    /**
     * 新增客户信息
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    public int insertCustomer(BusinessCustomer businessCustomer);

    /**
     * 修改客户信息
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    public int updateCustomer(BusinessCustomer businessCustomer);

    /**
     * 删除客户信息
     * 
     * @param customerId 客户ID
     * @param updateBy 操作人
     * @return 结果
     */
    public int deleteCustomerById(@Param("customerId") Long customerId, @Param("updateBy") String updateBy);

    /**
     * 批量删除客户信息
     * 
     * @param customerIds 需要删除的数据ID
     * @param updateBy 操作人
     * @return 结果
     */
    public int deleteCustomerByIds(@Param("customerIds") Long[] customerIds, @Param("updateBy") String updateBy);
}
