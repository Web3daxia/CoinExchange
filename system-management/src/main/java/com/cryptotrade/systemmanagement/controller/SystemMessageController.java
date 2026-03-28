/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.SystemMessage;
import com.cryptotrade.systemmanagement.service.SystemMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/message")
@Api(tags = "系统消息管理（多语言）")
public class SystemMessageController {
    
    @Autowired
    private SystemMessageService systemMessageService;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建消息", notes = "创建多语言消息")
    public Result<SystemMessage> createMessage(
            @ApiParam(value = "消息键", required = true) @RequestParam String messageKey,
            @ApiParam(value = "语言代码", required = true) @RequestParam String languageCode,
            @ApiParam(value = "消息值", required = true) @RequestParam String messageValue,
            @ApiParam(value = "模块") @RequestParam(required = false) String module) {
        try {
            SystemMessage message = systemMessageService.createMessage(messageKey, languageCode, messageValue, module);
            return Result.success("消息创建成功", message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{messageId}")
    @ApiOperation(value = "更新消息", notes = "更新消息内容")
    public Result<SystemMessage> updateMessage(
            @ApiParam(value = "消息ID", required = true) @PathVariable Long messageId,
            @ApiParam(value = "消息值", required = true) @RequestParam String messageValue) {
        try {
            SystemMessage message = systemMessageService.updateMessage(messageId, messageValue);
            return Result.success("消息更新成功", message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{messageId}")
    @ApiOperation(value = "删除消息", notes = "删除消息")
    public Result<Void> deleteMessage(
            @ApiParam(value = "消息ID", required = true) @PathVariable Long messageId) {
        try {
            systemMessageService.deleteMessage(messageId);
            return Result.success("消息删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/language/{languageCode}")
    @ApiOperation(value = "查询语言包", notes = "查询指定语言的所有消息")
    public Result<Map<String, String>> getMessagesByLanguage(
            @ApiParam(value = "语言代码", required = true) @PathVariable String languageCode) {
        try {
            Map<String, String> messages = systemMessageService.getAllMessagesByLanguage(languageCode);
            return Result.success(messages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/key/{messageKey}")
    @ApiOperation(value = "查询消息", notes = "根据消息键查询消息（支持语言代码）")
    public Result<String> getMessage(
            @ApiParam(value = "消息键", required = true) @PathVariable String messageKey,
            @ApiParam(value = "语言代码", defaultValue = "zh-CN") @RequestParam(required = false, defaultValue = "zh-CN") String languageCode) {
        try {
            String messageValue = systemMessageService.getMessageValue(messageKey, languageCode);
            return Result.success(messageValue);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














