package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusinessCustomer;

/**
 * 业务客户信息 服务层
 * 
 * @author ruoyi
 */
public interface IBusinessCustomerService
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
    public int deleteCustomerById(Long customerId, String updateBy);

    /**
     * 批量删除客户信息
     * 
     * @param customerIds 需要删除的客户ID
     * @param updateBy 操作人
     * @return 结果
     */
    public int deleteCustomerByIds(Long[] customerIds, String updateBy);

    /**
     * 校验客户姓名是否唯一
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    public boolean checkCustomerNameUnique(BusinessCustomer businessCustomer);
}
