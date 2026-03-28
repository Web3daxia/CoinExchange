/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsTag;
import com.cryptotrade.newsfeed.repository.NewsTagRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 新闻标签管理Controller
 */
@RestController
@RequestMapping("/api/admin/news-tag")
@Api(tags = "后台管理-新闻标签管理")
public class AdminNewsTagController {

    @Autowired
    private NewsTagRepository tagRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取标签列表", notes = "获取所有新闻标签")
    public Result<List<NewsTag>> getTagList(
            @ApiParam(value = "状态") @RequestParam(required = false) String status) {
        try {
            List<NewsTag> tags;
            if (status != null && !status.isEmpty()) {
                tags = tagRepository.findByStatus(status);
            } else {
                tags = tagRepository.findAllByOrderBySortOrderAsc();
            }
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取标签详情", notes = "根据ID获取标签详情")
    public Result<NewsTag> getTagById(
            @ApiParam(value = "标签ID", required = true) @PathVariable Long id) {
        try {
            NewsTag tag = tagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在: " + id));
            return Result.success(tag);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建标签", notes = "创建新的新闻标签")
    public Result<NewsTag> createTag(
            @ApiParam(value = "标签信息", required = true) @RequestBody NewsTag tag) {
        try {
            // 检查标签代码是否已存在
            if (tagRepository.findByTagCode(tag.getTagCode()).isPresent()) {
                return Result.error("标签代码已存在: " + tag.getTagCode());
            }

            tag.setStatus("ACTIVE");
            NewsTag saved = tagRepository.save(tag);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新标签", notes = "更新标签信息")
    public Result<NewsTag> updateTag(
            @ApiParam(value = "标签ID", required = true) @PathVariable Long id,
            @ApiParam(value = "标签信息", required = true) @RequestBody NewsTag tag) {
        try {
            NewsTag existing = tagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在: " + id));

            existing.setTagName(tag.getTagName());
            existing.setSortOrder(tag.getSortOrder());

            NewsTag saved = tagRepository.save(existing);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation(value = "更新标签状态", notes = "启用或停用标签")
    public Result<NewsTag> updateTagStatus(
            @ApiParam(value = "标签ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        try {
            NewsTag tag = tagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在: " + id));

            tag.setStatus(status);
            NewsTag saved = tagRepository.save(tag);
            return Result.success("状态更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除标签", notes = "删除标签")
    public Result<Void> deleteTag(
            @ApiParam(value = "标签ID", required = true) @PathVariable Long id) {
        try {
            if (!tagRepository.existsById(id)) {
                return Result.error("标签不存在: " + id);
            }
            tagRepository.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

