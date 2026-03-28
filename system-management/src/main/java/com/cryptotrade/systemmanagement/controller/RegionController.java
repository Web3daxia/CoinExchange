/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.Region;
import com.cryptotrade.systemmanagement.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/region")
@Api(tags = "地区管理")
public class RegionController {
    
    @Autowired
    private RegionService regionService;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加地区", notes = "添加新的地区配置")
    public Result<Region> createRegion(
            @ApiParam(value = "国家代码", required = true) @RequestParam String countryCode,
            @ApiParam(value = "国家名称", required = true) @RequestParam String countryName,
            @ApiParam(value = "地区代码") @RequestParam(required = false) String regionCode,
            @ApiParam(value = "地区名称") @RequestParam(required = false) String regionName,
            @ApiParam(value = "API访问启用", defaultValue = "true") @RequestParam(required = false, defaultValue = "true") Boolean apiAccessEnabled,
            @ApiParam(value = "前端访问启用", defaultValue = "true") @RequestParam(required = false, defaultValue = "true") Boolean frontendAccessEnabled,
            @ApiParam(value = "限制原因") @RequestParam(required = false) String blockReason) {
        try {
            // 限制消息使用默认消息
            Map<String, String> blockMessage = new HashMap<>();
            blockMessage.put("zh-CN", "您所在的地区无法访问该服务");
            blockMessage.put("en-US", "Service is not available in your region");
            
            Region region = regionService.createRegion(countryCode, countryName, regionCode, regionName,
                    apiAccessEnabled, frontendAccessEnabled, blockReason, blockMessage);
            return Result.success("地区添加成功", region);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{regionId}")
    @ApiOperation(value = "更新地区", notes = "更新地区配置")
    public Result<Region> updateRegion(
            @ApiParam(value = "地区ID", required = true) @PathVariable Long regionId,
            @ApiParam(value = "国家名称") @RequestParam(required = false) String countryName,
            @ApiParam(value = "地区代码") @RequestParam(required = false) String regionCode,
            @ApiParam(value = "地区名称") @RequestParam(required = false) String regionName,
            @ApiParam(value = "API访问启用") @RequestParam(required = false) Boolean apiAccessEnabled,
            @ApiParam(value = "前端访问启用") @RequestParam(required = false) Boolean frontendAccessEnabled,
            @ApiParam(value = "限制原因") @RequestParam(required = false) String blockReason,
            @ApiParam(value = "状态") @RequestParam(required = false) String status) {
        try {
            Region region = regionService.updateRegion(regionId, countryName, regionCode, regionName,
                    apiAccessEnabled, frontendAccessEnabled, blockReason, null, status);
            return Result.success("地区更新成功", region);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{regionId}")
    @ApiOperation(value = "删除地区", notes = "删除地区配置")
    public Result<Void> deleteRegion(
            @ApiParam(value = "地区ID", required = true) @PathVariable Long regionId) {
        try {
            regionService.deleteRegion(regionId);
            return Result.success("地区删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询地区列表", notes = "查询所有地区配置")
    public Result<List<Region>> getAllRegions() {
        try {
            List<Region> regions = regionService.getAllRegions();
            return Result.success(regions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/blocked")
    @ApiOperation(value = "查询被限制地区", notes = "查询API访问被限制的地区")
    public Result<List<Region>> getBlockedRegions() {
        try {
            List<Region> regions = regionService.getBlockedRegions();
            return Result.success(regions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{regionId}")
    @ApiOperation(value = "查询地区详情", notes = "根据ID查询地区详情")
    public Result<Region> getRegionById(
            @ApiParam(value = "地区ID", required = true) @PathVariable Long regionId) {
        try {
            Region region = regionService.getRegionById(regionId);
            return Result.success(region);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/check/{countryCode}")
    @ApiOperation(value = "检查地区访问权限", notes = "检查指定国家代码的访问权限")
    public Result<Map<String, Object>> checkAccess(
            @ApiParam(value = "国家代码", required = true) @PathVariable String countryCode) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("apiAccessEnabled", regionService.isApiAccessAllowed(countryCode));
            result.put("frontendAccessEnabled", regionService.isFrontendAccessAllowed(countryCode));
            result.put("blockMessage", regionService.getBlockMessage(countryCode, "zh-CN"));
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














