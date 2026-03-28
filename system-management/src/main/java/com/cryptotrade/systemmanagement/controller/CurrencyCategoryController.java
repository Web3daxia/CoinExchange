/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.CurrencyCategory;
import com.cryptotrade.systemmanagement.service.CurrencyCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 币种分类管理Controller
 */
@RestController
@RequestMapping("/admin/system/currency-category")
@Api(tags = "币种分类管理")
public class CurrencyCategoryController {

    @Autowired
    private CurrencyCategoryService categoryService;

    @PostMapping("/create")
    @ApiOperation(value = "创建分类", notes = "创建新的币种分类")
    public Result<CurrencyCategory> createCategory(
            @ApiParam(value = "分类代码", required = true) @RequestParam String categoryCode,
            @ApiParam(value = "分类名称", required = true) @RequestParam String categoryName,
            @ApiParam(value = "分类名称（英文）") @RequestParam(required = false) String categoryNameEn,
            @ApiParam(value = "排序顺序") @RequestParam(required = false, defaultValue = "0") Integer sortOrder,
            @ApiParam(value = "分类描述") @RequestParam(required = false) String description) {
        try {
            CurrencyCategory category = categoryService.createCategory(categoryCode, categoryName, categoryNameEn,
                    sortOrder, description);
            return Result.success("分类创建成功", category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update/{categoryId}")
    @ApiOperation(value = "更新分类", notes = "更新币种分类信息")
    public Result<CurrencyCategory> updateCategory(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long categoryId,
            @ApiParam(value = "分类名称") @RequestParam(required = false) String categoryName,
            @ApiParam(value = "分类名称（英文）") @RequestParam(required = false) String categoryNameEn,
            @ApiParam(value = "排序顺序") @RequestParam(required = false) Integer sortOrder,
            @ApiParam(value = "状态") @RequestParam(required = false) String status,
            @ApiParam(value = "分类描述") @RequestParam(required = false) String description) {
        try {
            CurrencyCategory category = categoryService.updateCategory(categoryId, categoryName, categoryNameEn,
                    sortOrder, status, description);
            return Result.success("分类更新成功", category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}")
    @ApiOperation(value = "删除分类", notes = "删除币种分类")
    public Result<Void> deleteCategory(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return Result.success("分类删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有分类", notes = "获取所有币种分类列表")
    public Result<List<CurrencyCategory>> getAllCategories() {
        try {
            List<CurrencyCategory> categories = categoryService.getAllCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation(value = "获取激活的分类", notes = "获取所有激活的币种分类列表")
    public Result<List<CurrencyCategory>> getActiveCategories() {
        try {
            List<CurrencyCategory> categories = categoryService.getActiveCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{categoryId}")
    @ApiOperation(value = "获取分类详情", notes = "根据ID获取分类详情")
    public Result<CurrencyCategory> getCategoryById(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long categoryId) {
        try {
            CurrencyCategory category = categoryService.getCategoryById(categoryId);
            return Result.success(category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














