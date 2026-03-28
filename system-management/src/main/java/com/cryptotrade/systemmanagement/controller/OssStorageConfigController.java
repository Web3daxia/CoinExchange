/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.OssStorageConfig;
import com.cryptotrade.systemmanagement.service.OssStorageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/oss-config")
@Api(tags = "OSS存储配置管理")
public class OssStorageConfigController {
    
    @Autowired
    private OssStorageConfigService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建OSS存储配置")
    public Result<OssStorageConfig> create(@Valid @RequestBody OssStorageConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新OSS存储配置")
    public Result<OssStorageConfig> update(@PathVariable Long id, @Valid @RequestBody OssStorageConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除OSS存储配置")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询所有OSS存储配置")
    public Result<List<OssStorageConfig>> list() {
        try {
            return Result.success(service.getAllConfigs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/default")
    @ApiOperation(value = "获取默认OSS存储配置")
    public Result<OssStorageConfig> getDefault() {
        try {
            return Result.success(service.getDefaultConfig());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/set-default/{id}")
    @ApiOperation(value = "设置默认OSS存储配置")
    public Result<Void> setDefault(@PathVariable Long id) {
        try {
            service.setDefault(id);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














