package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BusinessCustomer;
import com.ruoyi.system.service.IBusinessCustomerService;

/**
 * 业务客户信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/customer")
public class BusinessCustomerController extends BaseController
{
    @Autowired
    private IBusinessCustomerService customerService;

    /**
     * 获取客户列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessCustomer businessCustomer)
    {
        startPage();
        List<BusinessCustomer> list = customerService.selectCustomerList(businessCustomer);
        return getDataTable(list);
    }

    /**
     * 导出客户列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:export')")
    @Log(title = "客户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusinessCustomer businessCustomer)
    {
        List<BusinessCustomer> list = customerService.selectCustomerList(businessCustomer);
        ExcelUtil<BusinessCustomer> util = new ExcelUtil<BusinessCustomer>(BusinessCustomer.class);
        util.exportExcel(response, list, "客户数据");
    }

    /**
     * 根据客户ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:customer:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable Long customerId)
    {
        return success(customerService.selectCustomerById(customerId));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    @Log(title = "客户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BusinessCustomer businessCustomer)
    {
        if (!customerService.checkCustomerNameUnique(businessCustomer))
        {
            return error("新增客户'" + businessCustomer.getCustomerName() + "'失败，客户姓名已存在");
        }
        businessCustomer.setCreateBy(getUsername());
        return toAjax(customerService.insertCustomer(businessCustomer));
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:edit')")
    @Log(title = "客户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BusinessCustomer businessCustomer)
    {
        if (!customerService.checkCustomerNameUnique(businessCustomer))
        {
            return error("修改客户'" + businessCustomer.getCustomerName() + "'失败，客户姓名已存在");
        }
        businessCustomer.setUpdateBy(getUsername());
        return toAjax(customerService.updateCustomer(businessCustomer));
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:remove')")
    @Log(title = "客户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds)
    {
        return toAjax(customerService.deleteCustomerByIds(customerIds, getUsername()));
    }
}
