/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.SensitiveWord;
import com.cryptotrade.newsfeed.repository.SensitiveWordRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 后台管理 - 敏感词管理Controller
 */
@RestController
@RequestMapping("/api/admin/sensitive-word")
@Api(tags = "后台管理-敏感词管理")
public class AdminSensitiveWordController {

    @Autowired
    private SensitiveWordRepository sensitiveWordRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取敏感词列表", notes = "获取所有敏感词")
    public Result<List<SensitiveWord>> getSensitiveWordList(
            @ApiParam(value = "状态") @RequestParam(required = false) String status,
            @ApiParam(value = "类型") @RequestParam(required = false) String wordType) {
        try {
            List<SensitiveWord> words;
            if (status != null && !status.isEmpty()) {
                words = sensitiveWordRepository.findByStatus(status);
            } else if (wordType != null && !wordType.isEmpty()) {
                words = sensitiveWordRepository.findByWordType(wordType);
            } else {
                words = sensitiveWordRepository.findAll();
            }
            return Result.success(words);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取敏感词详情", notes = "根据ID获取敏感词详情")
    public Result<SensitiveWord> getSensitiveWordById(
            @ApiParam(value = "敏感词ID", required = true) @PathVariable Long id) {
        try {
            SensitiveWord word = sensitiveWordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("敏感词不存在: " + id));
            return Result.success(word);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建敏感词", notes = "添加新的敏感词")
    public Result<SensitiveWord> createSensitiveWord(
            @ApiParam(value = "敏感词信息", required = true) @RequestBody SensitiveWord word) {
        try {
            // 检查敏感词是否已存在
            if (sensitiveWordRepository.findByWord(word.getWord()).isPresent()) {
                return Result.error("敏感词已存在: " + word.getWord());
            }

            word.setStatus("ACTIVE");
            SensitiveWord saved = sensitiveWordRepository.save(word);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch-create")
    @ApiOperation(value = "批量创建敏感词", notes = "批量添加敏感词")
    public Result<Integer> batchCreateSensitiveWords(
            @ApiParam(value = "敏感词列表", required = true) @RequestBody List<SensitiveWord> words) {
        try {
            int savedCount = 0;
            for (SensitiveWord word : words) {
                if (!sensitiveWordRepository.findByWord(word.getWord()).isPresent()) {
                    word.setStatus("ACTIVE");
                    sensitiveWordRepository.save(word);
                    savedCount++;
                }
            }
            return Result.success("批量创建成功，共添加 " + savedCount + " 个敏感词", savedCount);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新敏感词", notes = "更新敏感词信息")
    public Result<SensitiveWord> updateSensitiveWord(
            @ApiParam(value = "敏感词ID", required = true) @PathVariable Long id,
            @ApiParam(value = "敏感词信息", required = true) @RequestBody SensitiveWord word) {
        try {
            SensitiveWord existing = sensitiveWordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("敏感词不存在: " + id));

            existing.setWordType(word.getWordType());
            existing.setStatus(word.getStatus());

            SensitiveWord saved = sensitiveWordRepository.save(existing);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation(value = "更新敏感词状态", notes = "启用或停用敏感词")
    public Result<SensitiveWord> updateSensitiveWordStatus(
            @ApiParam(value = "敏感词ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        try {
            SensitiveWord word = sensitiveWordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("敏感词不存在: " + id));

            word.setStatus(status);
            SensitiveWord saved = sensitiveWordRepository.save(word);
            return Result.success("状态更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除敏感词", notes = "删除敏感词")
    public Result<Void> deleteSensitiveWord(
            @ApiParam(value = "敏感词ID", required = true) @PathVariable Long id) {
        try {
            if (!sensitiveWordRepository.existsById(id)) {
                return Result.error("敏感词不存在: " + id);
            }
            sensitiveWordRepository.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














