package com.kevin.ods.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.kevin.ods.domain.OdsBusinessfee;
import com.kevin.ods.service.IOdsBusinessfeeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 业财Controller
 *
 * @author kevin
 * @date 2024-07-03
 */
@RestController
@RequestMapping("/kevin/businessfee")
public class OdsBusinessfeeController extends BaseController
{
    @Autowired
    private IOdsBusinessfeeService odsBusinessfeeService;

    /**
     * 查询业财列表
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:list')")
    @GetMapping("/list")
    public TableDataInfo list(OdsBusinessfee odsBusinessfee)
    {
        startPage();
        List<OdsBusinessfee> list = odsBusinessfeeService.selectOdsBusinessfeeList(odsBusinessfee);
        return getDataTable(list);
    }

    /**
     * 导出业财列表
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:export')")
    @Log(title = "业财", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OdsBusinessfee odsBusinessfee)
    {
        List<OdsBusinessfee> list = odsBusinessfeeService.selectOdsBusinessfeeList(odsBusinessfee);
        ExcelUtil<OdsBusinessfee> util = new ExcelUtil<OdsBusinessfee>(OdsBusinessfee.class);
        util.exportExcel(response, list, "业财数据");
    }

    /**
     * 获取业财详细信息
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:query')")
    @GetMapping(value = "/{indicatorCode}")
    public AjaxResult getInfo(@PathVariable("indicatorCode") String indicatorCode)
    {
        return success(odsBusinessfeeService.selectOdsBusinessfeeByIndicatorCode(indicatorCode));
    }

    /**
     * 新增业财
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:add')")
    @Log(title = "业财", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OdsBusinessfee odsBusinessfee)
    {
        return toAjax(odsBusinessfeeService.insertOdsBusinessfee(odsBusinessfee));
    }

    /**
     * 修改业财
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:edit')")
    @Log(title = "业财", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OdsBusinessfee odsBusinessfee)
    {
        return toAjax(odsBusinessfeeService.updateOdsBusinessfee(odsBusinessfee));
    }

    /**
     * 删除业财
     */
    @PreAuthorize("@ss.hasPermi('kevin:businessfee:remove')")
    @Log(title = "业财", businessType = BusinessType.DELETE)
	@DeleteMapping("/{indicatorCodes}")
    public AjaxResult remove(@PathVariable String[] indicatorCodes)
    {
        return toAjax(odsBusinessfeeService.deleteOdsBusinessfeeByIndicatorCodes(indicatorCodes));
    }

    /**
     * 导入
     */

    @Anonymous
    @PostMapping("/importData")
    public void importData(MultipartFile file) throws Exception {
        ExcelUtil<OdsBusinessfee> util = new ExcelUtil<OdsBusinessfee>(OdsBusinessfee.class);
        List<OdsBusinessfee> odsBusinessfeeList = util.importExcel(file.getInputStream());
        int num = 0 ;
        for (OdsBusinessfee odsBusinessfee : odsBusinessfeeList) {
            num = odsBusinessfeeService.insertOdsBusinessfee(odsBusinessfee);
        }
        System.out.println("完成");

    }

}
