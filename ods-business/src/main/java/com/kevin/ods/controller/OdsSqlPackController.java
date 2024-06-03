package com.kevin.ods.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.kevin.ods.domain.OdsSqlPack;
import com.kevin.ods.service.IOdsSqlPackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * odsController
 *
 * @author kevin
 * @date 2024-05-29
 */
@RestController
@RequestMapping("/ods/ods")
public class OdsSqlPackController extends BaseController
{
    @Autowired
    private IOdsSqlPackService odsSqlPackService;

    /**
     * 查询ods列表
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:list')")
    @GetMapping("/list")
    public TableDataInfo list(OdsSqlPack odsSqlPack)
    {
        startPage();
        List<OdsSqlPack> list = odsSqlPackService.selectOdsSqlPackList(odsSqlPack);
        return getDataTable(list);
    }

    /**
     * 导出ods列表
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:export')")
    @Log(title = "ods", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OdsSqlPack odsSqlPack)
    {
        List<OdsSqlPack> list = odsSqlPackService.selectOdsSqlPackList(odsSqlPack);
        ExcelUtil<OdsSqlPack> util = new ExcelUtil<OdsSqlPack>(OdsSqlPack.class);
        util.exportExcel(response, list, "ods数据");
    }

    /**
     * 获取ods详细信息
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:query')")
    @GetMapping(value = "/{Id}")
    public AjaxResult getInfo(@PathVariable("Id") Long Id)
    {
        return success(odsSqlPackService.selectOdsSqlPackById(Id));
    }

    /**
     * 新增ods
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:add')")
    @Log(title = "ods", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OdsSqlPack odsSqlPack)
    {
        return toAjax(odsSqlPackService.insertOdsSqlPack(odsSqlPack));
    }

    /**
     * 修改ods
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:edit')")
    @Log(title = "ods", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OdsSqlPack odsSqlPack)
    {
        return toAjax(odsSqlPackService.updateOdsSqlPack(odsSqlPack));
    }

    /**
     * 删除ods
     */
    @PreAuthorize("@ss.hasPermi('ods:ods:remove')")
    @Log(title = "ods", businessType = BusinessType.DELETE)
	@PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable String ids)
    {
        String[] arrayId = ids.split(",");
        if (arrayId.length>1) {
            return toAjax(odsSqlPackService.updateOdsSqlStatusByIds(arrayId));
        } else {
            return toAjax(odsSqlPackService.updateOdsSqlStatusById(arrayId[0]));
        }
    }


    /**
     * 查询sqlGrop列表
     */

    @GetMapping("/sqlGropList")
    public TableDataInfo sqlGropList()
    {
        return getDataTable(odsSqlPackService.selectSqlGropList());
    }

    /**
     * 查询procedureGroup列表
     */

    @GetMapping("/procedureGroupList")
    public TableDataInfo procedureGroupList()
    {
        return getDataTable(odsSqlPackService.selectprocedureGroupList());
    }


    /**
     * 查询sqlGroupCode列表
     */

    @GetMapping("/sqlGroupCodeList")
    public TableDataInfo sqlGroupCodeList()
    {
        return getDataTable(odsSqlPackService.selectsqlGroupCodeList());
    }
}
