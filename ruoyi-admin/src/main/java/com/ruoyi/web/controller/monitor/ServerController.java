package com.ruoyi.web.controller.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.domain.Server;
import com.ruoyi.web.core.monitor.CpuStressService;
import com.ruoyi.web.core.monitor.CpuStressService.CpuStressRequest;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @Autowired
    private CpuStressService cpuStressService;

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }

    /**
     * 查询CPU压测状态
     */
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping("/stress")
    public AjaxResult getStressStatus()
    {
        return AjaxResult.success(cpuStressService.getStatus());
    }

    /**
     * 启动CPU压测
     */
    @PreAuthorize("@ss.hasPermi('monitor:server:stress')")
    @Log(title = "服务监控", businessType = BusinessType.OTHER)
    @PostMapping("/stress")
    public AjaxResult startStress(@RequestBody(required = false) CpuStressRequest request)
    {
        try
        {
            return AjaxResult.success("CPU压测任务已启动", cpuStressService.start(request));
        }
        catch (IllegalStateException e)
        {
            return AjaxResult.warn(e.getMessage(), cpuStressService.getStatus());
        }
    }

    /**
     * 停止CPU压测
     */
    @PreAuthorize("@ss.hasPermi('monitor:server:stress')")
    @Log(title = "服务监控", businessType = BusinessType.OTHER)
    @DeleteMapping("/stress")
    public AjaxResult stopStress()
    {
        return AjaxResult.success("CPU压测任务已停止", cpuStressService.stop());
    }
}
