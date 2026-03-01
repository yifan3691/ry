package com.kevin.ods.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.kevin.ods.domain.ExcelUploadBatchResult;
import com.kevin.ods.domain.ExcelTable;
import com.kevin.ods.service.IExcelTableService;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel上传表Controller
 * 
 * @author kevin
 * @date 2024-01-08
 */
@RestController
@RequestMapping("/ods/excel")
public class ExcelTableController extends BaseController
{
    @Autowired
    private IExcelTableService excelTableService;

    /**
     * 查询Excel上传表列表
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExcelTable excelTable)
    {
        startPage();
        List<ExcelTable> list = excelTableService.selectExcelTableList(excelTable);
        return getDataTable(list);
    }

    /**
     * 获取Excel上传表详细信息
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:query')")
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable("tableId") Long tableId)
    {
        return success(excelTableService.selectExcelTableById(tableId));
    }

    /**
     * 上传Excel文件并创建表
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:upload')")
    @Log(title = "Excel上传建表", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult uploadExcel(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "tableComment", required = false) String tableComment,
                                   @RequestParam(value = "importMode", required = false, defaultValue = "ALL_SHEETS") String importMode,
                                   @RequestParam(value = "sheetNames", required = false) String sheetNames)
    {
        try
        {
            ExcelUploadBatchResult batchResult = excelTableService.uploadExcelBatch(file, tableComment, importMode, sheetNames);
            String msg = String.format("上传完成：共%s个sheet，成功%s个，失败%s个", batchResult.getTotalSheets(),
                    batchResult.getSuccessSheets(), batchResult.getFailedSheets());
            return AjaxResult.success(msg, batchResult);
        }
        catch (Exception e)
        {
            return AjaxResult.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 修改Excel上传表
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:edit')")
    @Log(title = "Excel上传表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExcelTable excelTable)
    {
        return toAjax(excelTableService.updateExcelTable(excelTable));
    }

    /**
     * 删除Excel上传表
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:remove')")
    @Log(title = "Excel上传表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds)
    {
        return toAjax(excelTableService.deleteExcelTableByIds(tableIds));
    }

    /**
     * 获取表数据（分页）
     */
    @PreAuthorize("@ss.hasPermi('ods:excel:query')")
    @GetMapping("/data/{tableId}")
    public TableDataInfo getTableData(@PathVariable Long tableId,
                                       @RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "20") Integer pageSize)
    {
        return excelTableService.getTableData(tableId, pageNum, pageSize);
    }
}
