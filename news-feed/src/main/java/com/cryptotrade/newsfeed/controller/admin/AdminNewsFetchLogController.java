/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsFetchLog;
import com.cryptotrade.newsfeed.repository.NewsFetchLogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 新闻采集日志Controller
 */
@RestController
@RequestMapping("/api/admin/news-fetch-log")
@Api(tags = "后台管理-新闻采集日志")
public class AdminNewsFetchLogController {

    @Autowired
    private NewsFetchLogRepository fetchLogRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取采集日志列表", notes = "获取新闻采集日志列表（分页）")
    public Result<Page<NewsFetchLog>> getFetchLogList(
            @ApiParam(value = "新闻源ID") @RequestParam(required = false) Long sourceId,
            @ApiParam(value = "采集状态") @RequestParam(required = false) String fetchStatus,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<NewsFetchLog> logs;
            if (sourceId != null) {
                logs = fetchLogRepository.findBySourceId(sourceId, pageable);
            } else if (fetchStatus != null && !fetchStatus.isEmpty()) {
                logs = fetchLogRepository.findByFetchStatus(fetchStatus, pageable);
            } else {
                logs = fetchLogRepository.findAll(pageable);
            }
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取采集日志详情", notes = "根据ID获取采集日志详情")
    public Result<NewsFetchLog> getFetchLogById(
            @ApiParam(value = "日志ID", required = true) @PathVariable Long id) {
        try {
            NewsFetchLog log = fetchLogRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("采集日志不存在: " + id));
            return Result.success(log);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














