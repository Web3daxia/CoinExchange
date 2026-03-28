/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsCategory;
import com.cryptotrade.newsfeed.repository.NewsCategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 后台管理 - 新闻分类管理Controller
 */
@RestController
@RequestMapping("/api/admin/news-category")
@Api(tags = "后台管理-新闻分类管理")
public class AdminNewsCategoryController {

    @Autowired
    private NewsCategoryRepository categoryRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取分类列表", notes = "获取所有新闻分类")
    public Result<List<NewsCategory>> getCategoryList(
            @ApiParam(value = "状态") @RequestParam(required = false) String status) {
        try {
            List<NewsCategory> categories;
            if (status != null && !status.isEmpty()) {
                categories = categoryRepository.findByStatus(status);
            } else {
                categories = categoryRepository.findAllByOrderBySortOrderAsc();
            }
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取分类详情", notes = "根据ID获取分类详情")
    public Result<NewsCategory> getCategoryById(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long id) {
        try {
            NewsCategory category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("分类不存在: " + id));
            return Result.success(category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建分类", notes = "创建新的新闻分类")
    public Result<NewsCategory> createCategory(
            @ApiParam(value = "分类信息", required = true) @RequestBody NewsCategory category) {
        try {
            // 检查分类代码是否已存在
            if (categoryRepository.findByCategoryCode(category.getCategoryCode()).isPresent()) {
                return Result.error("分类代码已存在: " + category.getCategoryCode());
            }

            category.setStatus("ACTIVE");
            NewsCategory saved = categoryRepository.save(category);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新分类", notes = "更新分类信息")
    public Result<NewsCategory> updateCategory(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long id,
            @ApiParam(value = "分类信息", required = true) @RequestBody NewsCategory category) {
        try {
            NewsCategory existing = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

            existing.setCategoryName(category.getCategoryName());
            existing.setParentId(category.getParentId());
            existing.setSortOrder(category.getSortOrder());
            existing.setDescription(category.getDescription());

            NewsCategory saved = categoryRepository.save(existing);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation(value = "更新分类状态", notes = "启用或停用分类")
    public Result<NewsCategory> updateCategoryStatus(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        try {
            NewsCategory category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

            category.setStatus(status);
            NewsCategory saved = categoryRepository.save(category);
            return Result.success("状态更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    public Result<Void> deleteCategory(
            @ApiParam(value = "分类ID", required = true) @PathVariable Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                return Result.error("分类不存在: " + id);
            }
            categoryRepository.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














