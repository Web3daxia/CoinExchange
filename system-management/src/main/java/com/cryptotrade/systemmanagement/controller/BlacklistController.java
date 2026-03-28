/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.Blacklist;
import com.cryptotrade.systemmanagement.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/blacklist")
@Api(tags = "黑名单管理")
public class BlacklistController {
    
    @Autowired
    private BlacklistService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加黑名单")
    public Result<Blacklist> create(@Valid @RequestBody Blacklist blacklist) {
        try {
            return Result.success("添加成功", service.createBlacklist(blacklist));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新黑名单")
    public Result<Blacklist> update(@PathVariable Long id, @Valid @RequestBody Blacklist blacklist) {
        try {
            return Result.success("更新成功", service.updateBlacklist(id, blacklist));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除黑名单")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteBlacklist(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询所有黑名单")
    public Result<List<Blacklist>> list() {
        try {
            return Result.success(service.getAllBlacklists());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/type/{type}")
    @ApiOperation(value = "根据类型查询黑名单")
    public Result<List<Blacklist>> getByType(@PathVariable String type) {
        try {
            return Result.success(service.getBlacklistsByType(type));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/check")
    @ApiOperation(value = "检查是否在黑名单中")
    public Result<Boolean> check(@RequestParam String blacklistType, @RequestParam String blacklistValue) {
        try {
            return Result.success(service.isBlacklisted(blacklistType, blacklistValue));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/clean-expired")
    @ApiOperation(value = "清理过期黑名单")
    public Result<Void> cleanExpired() {
        try {
            service.removeExpiredBlacklists();
            return Result.success("清理成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














