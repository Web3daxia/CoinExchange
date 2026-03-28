/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsSource;
import com.cryptotrade.newsfeed.repository.NewsSourceRepository;
import com.cryptotrade.newsfeed.service.NewsFetchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 后台管理 - 新闻源管理Controller
 */
@RestController
@RequestMapping("/api/admin/news-source")
@Api(tags = "后台管理-新闻源管理")
public class AdminNewsSourceController {

    @Autowired
    private NewsSourceRepository sourceRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取新闻源列表", notes = "获取所有新闻源")
    public Result<List<NewsSource>> getNewsSourceList() {
        try {
            List<NewsSource> sources = sourceRepository.findAll();
            return Result.success(sources);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取新闻源详情", notes = "根据ID获取新闻源详情")
    public Result<NewsSource> getNewsSourceById(
            @ApiParam(value = "新闻源ID", required = true) @PathVariable Long id) {
        try {
            NewsSource source = sourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("新闻源不存在: " + id));
            return Result.success(source);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建新闻源", notes = "创建新的新闻源配置")
    public Result<NewsSource> createNewsSource(
            @ApiParam(value = "新闻源信息", required = true) @RequestBody NewsSource source) {
        try {
            // 检查源代码是否已存在
            if (sourceRepository.findBySourceCode(source.getSourceCode()).isPresent()) {
                return Result.error("新闻源代码已存在: " + source.getSourceCode());
            }

            source.setStatus("ACTIVE");
            NewsSource saved = sourceRepository.save(source);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新新闻源", notes = "更新新闻源配置")
    public Result<NewsSource> updateNewsSource(
            @ApiParam(value = "新闻源ID", required = true) @PathVariable Long id,
            @ApiParam(value = "新闻源信息", required = true) @RequestBody NewsSource source) {
        try {
            NewsSource existing = sourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("新闻源不存在: " + id));

            // 更新字段
            existing.setSourceName(source.getSourceName());
            existing.setApiUrl(source.getApiUrl());
            existing.setApiKey(source.getApiKey());
            existing.setApiSecret(source.getApiSecret());
            existing.setFetchInterval(source.getFetchInterval());
            existing.setPriority(source.getPriority());
            existing.setDescription(source.getDescription());

            NewsSource saved = sourceRepository.save(existing);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation(value = "更新新闻源状态", notes = "启用或停用新闻源")
    public Result<NewsSource> updateNewsSourceStatus(
            @ApiParam(value = "新闻源ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        try {
            NewsSource source = sourceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("新闻源不存在: " + id));

            source.setStatus(status);
            NewsSource saved = sourceRepository.save(source);
            return Result.success("状态更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除新闻源", notes = "删除新闻源配置")
    public Result<Void> deleteNewsSource(
            @ApiParam(value = "新闻源ID", required = true) @PathVariable Long id) {
        try {
            if (!sourceRepository.existsById(id)) {
                return Result.error("新闻源不存在: " + id);
            }
            sourceRepository.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Autowired
    private com.cryptotrade.newsfeed.service.NewsFetchService newsFetchService;

    @PostMapping("/fetch/{id}")
    @ApiOperation(value = "手动采集新闻", notes = "手动触发指定新闻源的采集任务")
    public Result<Integer> fetchNewsFromSource(
            @ApiParam(value = "新闻源ID", required = true) @PathVariable Long id) {
        try {
            int count = newsFetchService.fetchNewsFromSource(id);
            return Result.success("采集完成，共采集 " + count + " 条新闻", count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

