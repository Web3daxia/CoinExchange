/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.SmsServiceConfig;
import com.cryptotrade.systemmanagement.entity.SmsTemplate;
import com.cryptotrade.systemmanagement.service.SmsServiceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/sms-config")
@Api(tags = "短信服务配置管理")
public class SmsServiceConfigController {
    
    @Autowired
    private SmsServiceConfigService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建短信服务配置")
    public Result<SmsServiceConfig> create(@Valid @RequestBody SmsServiceConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新短信服务配置")
    public Result<SmsServiceConfig> update(@PathVariable Long id, @Valid @RequestBody SmsServiceConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/default")
    @ApiOperation(value = "获取默认短信服务配置")
    public Result<SmsServiceConfig> getDefault() {
        try {
            return Result.success(service.getDefaultConfig());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询所有短信服务配置")
    public Result<List<SmsServiceConfig>> list() {
        try {
            return Result.success(service.getAllConfigs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/set-default/{id}")
    @ApiOperation(value = "设置默认短信服务配置")
    public Result<Void> setDefault(@PathVariable Long id) {
        try {
            service.setDefault(id);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/template/create")
    @ApiOperation(value = "创建短信模板")
    public Result<SmsTemplate> createTemplate(@Valid @RequestBody SmsTemplate template) {
        try {
            return Result.success("创建成功", service.createTemplate(template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/template/update/{id}")
    @ApiOperation(value = "更新短信模板")
    public Result<SmsTemplate> updateTemplate(@PathVariable Long id, @Valid @RequestBody SmsTemplate template) {
        try {
            return Result.success("更新成功", service.updateTemplate(id, template));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/template/{id}")
    @ApiOperation(value = "删除短信模板")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        try {
            service.deleteTemplate(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/template/{templateCode}")
    @ApiOperation(value = "查询短信模板列表")
    public Result<List<SmsTemplate>> getTemplates(@PathVariable String templateCode) {
        try {
            return Result.success(service.getTemplatesByCode(templateCode));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














