/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.SystemAdminLoginRequest;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminLoginResponse;
import com.cryptotrade.systemmanagement.service.SystemAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理员认证控制器
 * 提供管理员登录接口
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员认证")
public class AdminAuthController {
    
    @Autowired
    private SystemAdminService systemAdminService;
    
    @PostMapping("/login")
    @ApiOperation(value = "管理员登录", notes = "系统管理员登录接口")
    public Result<SystemAdminLoginResponse> login(
            @Valid @RequestBody SystemAdminLoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            // 获取客户端IP和设备信息
            String clientIp = getClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            
            if (request.getLoginIp() == null) {
                request.setLoginIp(clientIp);
            }
            if (request.getLoginDevice() == null) {
                request.setLoginDevice(userAgent);
            }
            
            SystemAdminLoginResponse response = systemAdminService.login(request);
            return Result.success("登录成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}


