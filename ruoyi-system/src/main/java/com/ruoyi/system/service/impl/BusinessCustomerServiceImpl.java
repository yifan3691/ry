package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BusinessCustomer;
import com.ruoyi.system.mapper.BusinessCustomerMapper;
import com.ruoyi.system.service.IBusinessCustomerService;

/**
 * 业务客户信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class BusinessCustomerServiceImpl implements IBusinessCustomerService
{
    @Autowired
    private BusinessCustomerMapper customerMapper;

    /**
     * 查询客户信息
     * 
     * @param customerId 客户ID
     * @return 客户信息
     */
    @Override
    public BusinessCustomer selectCustomerById(Long customerId)
    {
        return customerMapper.selectCustomerById(customerId);
    }

    /**
     * 查询客户列表
     * 
     * @param businessCustomer 客户信息
     * @return 客户集合
     */
    @Override
    public List<BusinessCustomer> selectCustomerList(BusinessCustomer businessCustomer)
    {
        return customerMapper.selectCustomerList(businessCustomer);
    }

    /**
     * 新增客户信息
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    @Override
    public int insertCustomer(BusinessCustomer businessCustomer)
    {
        businessCustomer.setCustomerName(StringUtils.trim(businessCustomer.getCustomerName()));
        if (StringUtils.isEmpty(businessCustomer.getStatus()))
        {
            businessCustomer.setStatus("0");
        }
        if (!checkCustomerNameUnique(businessCustomer))
        {
            throw new ServiceException("新增客户'" + businessCustomer.getCustomerName() + "'失败，客户姓名已存在");
        }
        return customerMapper.insertCustomer(businessCustomer);
    }

    /**
     * 修改客户信息
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    @Override
    public int updateCustomer(BusinessCustomer businessCustomer)
    {
        businessCustomer.setCustomerName(StringUtils.trim(businessCustomer.getCustomerName()));
        if (!checkCustomerNameUnique(businessCustomer))
        {
            throw new ServiceException("修改客户'" + businessCustomer.getCustomerName() + "'失败，客户姓名已存在");
        }
        return customerMapper.updateCustomer(businessCustomer);
    }

    /**
     * 删除客户信息
     * 
     * @param customerId 客户ID
     * @param updateBy 操作人
     * @return 结果
     */
    @Override
    public int deleteCustomerById(Long customerId, String updateBy)
    {
        return customerMapper.deleteCustomerById(customerId, updateBy);
    }

    /**
     * 批量删除客户信息
     * 
     * @param customerIds 需要删除的客户ID
     * @param updateBy 操作人
     * @return 结果
     */
    @Override
    public int deleteCustomerByIds(Long[] customerIds, String updateBy)
    {
        return customerMapper.deleteCustomerByIds(customerIds, updateBy);
    }

    /**
     * 校验客户姓名是否唯一
     * 
     * @param businessCustomer 客户信息
     * @return 结果
     */
    @Override
    public boolean checkCustomerNameUnique(BusinessCustomer businessCustomer)
    {
        Long customerId = StringUtils.isNull(businessCustomer.getCustomerId()) ? -1L : businessCustomer.getCustomerId();
        BusinessCustomer info = customerMapper.selectCustomerByName(StringUtils.trim(businessCustomer.getCustomerName()));
        if (StringUtils.isNotNull(info) && info.getCustomerId().longValue() != customerId.longValue())
        {
            return false;
        }
        return true;
    }
}
