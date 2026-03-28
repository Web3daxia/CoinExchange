/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.FileUploadConfig;
import com.cryptotrade.systemmanagement.service.FileUploadConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/file-upload-config")
@Api(tags = "文件上传配置管理")
public class FileUploadConfigController {
    
    @Autowired
    private FileUploadConfigService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建文件上传配置")
    public Result<FileUploadConfig> create(@Valid @RequestBody FileUploadConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新文件上传配置")
    public Result<FileUploadConfig> update(@PathVariable Long id, @Valid @RequestBody FileUploadConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文件上传配置")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询所有文件上传配置")
    public Result<List<FileUploadConfig>> list() {
        try {
            return Result.success(service.getAllConfigs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/type/{uploadType}")
    @ApiOperation(value = "根据上传类型查询配置")
    public Result<List<FileUploadConfig>> getByUploadType(@PathVariable String uploadType) {
        try {
            return Result.success(service.getConfigsByUploadType(uploadType));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/check")
    @ApiOperation(value = "检查文件是否允许上传")
    public Result<Boolean> check(@RequestParam String uploadType, @RequestParam String fileCategory,
                                  @RequestParam String fileName, @RequestParam long fileSize) {
        try {
            return Result.success(service.isFileAllowed(uploadType, fileCategory, fileName, fileSize));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














