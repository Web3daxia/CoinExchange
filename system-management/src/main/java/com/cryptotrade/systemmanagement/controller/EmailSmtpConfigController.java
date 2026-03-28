/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.EmailSmtpConfig;
import com.cryptotrade.systemmanagement.entity.EmailTemplate;
import com.cryptotrade.systemmanagement.service.EmailSmtpConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/email-config")
@Api(tags = "邮件SMTP配置管理")
public class EmailSmtpConfigController {
    
    @Autowired
    private EmailSmtpConfigService service;
    
    @PostMapping("/smtp/create")
    @ApiOperation(value = "创建邮件SMTP配置")
    public Result<EmailSmtpConfig> createSmtpConfig(@Valid @RequestBody EmailSmtpConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/smtp/update/{id}")
    @ApiOperation(value = "更新邮件SMTP配置")
    public Result<EmailSmtpConfig> updateSmtpConfig(@PathVariable Long id, @Valid @RequestBody EmailSmtpConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/smtp/active")
    @ApiOperation(value = "获取启用的SMTP配置")
    public Result<EmailSmtpConfig> getActiveSmtpConfig() {
        try {
            return Result.success(service.getActiveConfig());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/template/create")
    @ApiOperation(value = "创建邮件模板")
    public Result<EmailTemplate> createTemplate(@Valid @RequestBody EmailTemplate template) {
        try {
            return Result.success("创建成功", service.createTemplate(template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/template/update/{id}")
    @ApiOperation(value = "更新邮件模板")
    public Result<EmailTemplate> updateTemplate(@PathVariable Long id, @Valid @RequestBody EmailTemplate template) {
        try {
            return Result.success("更新成功", service.updateTemplate(id, template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/template/{id}")
    @ApiOperation(value = "删除邮件模板")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        try {
            service.deleteTemplate(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/template/{templateCode}")
    @ApiOperation(value = "查询邮件模板列表")
    public Result<List<EmailTemplate>> getTemplates(@PathVariable String templateCode) {
        try {
            return Result.success(service.getTemplatesByCode(templateCode));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














