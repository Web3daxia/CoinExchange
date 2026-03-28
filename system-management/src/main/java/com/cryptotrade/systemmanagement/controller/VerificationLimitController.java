/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.systemmanagement.entity.VerificationLimitConfig;
import com.cryptotrade.systemmanagement.service.VerificationLimitService;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/verification-limit")
@Api(tags = "验证码频率限制配置管理")
public class VerificationLimitController {
    
    @Autowired
    private VerificationLimitService service;
    
    @PostMapping("/config/create")
    @ApiOperation(value = "创建验证码频率限制配置")
    public Result<VerificationLimitConfig> createConfig(@Valid @RequestBody VerificationLimitConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/config/update/{id}")
    @ApiOperation(value = "更新验证码频率限制配置")
    public Result<VerificationLimitConfig> updateConfig(@PathVariable Long id, @Valid @RequestBody VerificationLimitConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/config/list")
    @ApiOperation(value = "查询所有验证码频率限制配置")
    public Result<List<VerificationLimitConfig>> listConfigs() {
        try {
            return Result.success(service.getAllConfigs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/clean-expired")
    @ApiOperation(value = "清理过期记录")
    public Result<Void> cleanExpired() {
        try {
            service.cleanExpiredRecords();
            return Result.success("清理成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














