/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.controller;

import com.cryptotrade.basicinfo.dto.request.SetPreferenceRequest;
import com.cryptotrade.basicinfo.entity.*;
import com.cryptotrade.basicinfo.repository.UserPreferenceRepository;
import com.cryptotrade.basicinfo.service.*;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 基础信息控制器
 */
@RestController
@RequestMapping("/basic-info")
@Api(tags = "基础信息模块")
public class BasicInfoController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ApiEndpointService apiEndpointService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    // ==================== 汇率 ====================

    @GetMapping("/exchange-rates")
    @ApiOperation(value = "查询汇率", notes = "查询所有支持的汇率")
    public Result<List<Map<String, Object>>> getExchangeRates(
            @RequestParam(required = false) String baseCurrency) {
        try {
            List<Map<String, Object>> rates = exchangeRateService.getAllExchangeRates(baseCurrency);
            return Result.success(rates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/exchange-rate")
    @ApiOperation(value = "获取汇率", notes = "获取指定货币对的汇率")
    public Result<BigDecimal> getExchangeRate(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        try {
            BigDecimal rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
            return Result.success(rate);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/convert")
    @ApiOperation(value = "货币转换", notes = "转换货币金额")
    public Result<BigDecimal> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        try {
            BigDecimal converted = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);
            return Result.success(converted);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 语言 ====================

    @GetMapping("/languages")
    @ApiOperation(value = "查询支持的语言", notes = "获取所有支持的语言列表")
    public Result<List<LanguagePack>> getSupportedLanguages() {
        try {
            List<LanguagePack> languages = languageService.getSupportedLanguages();
            return Result.success(languages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/language/detect")
    @ApiOperation(value = "自动识别语言", notes = "根据IP地址自动识别语言")
    public Result<String> detectLanguage(
            @RequestParam(required = false) String ipAddress) {
        try {
            String languageCode = languageService.detectLanguageByIp(ipAddress);
            return Result.success(languageCode);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/language/translations")
    @ApiOperation(value = "获取语言包", notes = "获取指定语言的翻译数据")
    public Result<Map<String, String>> getTranslations(
            @RequestParam String languageCode) {
        try {
            Map<String, String> translations = languageService.getTranslationData(languageCode);
            return Result.success(translations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/language/set")
    @ApiOperation(value = "设置用户语言", notes = "设置用户的语言偏好")
    public Result<UserPreference> setUserLanguage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String languageCode) {
        try {
            UserPreference preference = languageService.setUserLanguage(userId, languageCode);
            return Result.success("语言设置成功", preference);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/preference")
    @ApiOperation(value = "查询用户偏好", notes = "查询用户的语言、货币等偏好设置")
    public Result<UserPreference> getUserPreference(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            UserPreference preference = languageService.getUserPreference(userId);
            return Result.success(preference);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/preference")
    @ApiOperation(value = "设置用户偏好", notes = "设置用户的语言、货币、API端点等偏好")
    public Result<UserPreference> setUserPreference(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SetPreferenceRequest request) {
        try {
            UserPreference preference = languageService.getUserPreference(userId);
            
            if (request.getLanguageCode() != null) {
                preference.setLanguageCode(request.getLanguageCode());
            }
            if (request.getCurrency() != null) {
                preference.setCurrency(request.getCurrency());
            }
            if (request.getApiEndpointId() != null) {
                preference.setApiEndpointId(request.getApiEndpointId());
            }
            if (request.getTimezone() != null) {
                preference.setTimezone(request.getTimezone());
            }
            if (request.getDateFormat() != null) {
                preference.setDateFormat(request.getDateFormat());
            }
            if (request.getNumberFormat() != null) {
                preference.setNumberFormat(request.getNumberFormat());
            }

            if (request.getApiEndpointId() != null) {
                apiEndpointService.setUserEndpoint(userId, request.getApiEndpointId());
            }

            preference = userPreferenceRepository.save(preference);
            
            return Result.success("偏好设置成功", preference);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== API端点 ====================

    @GetMapping("/api-endpoints")
    @ApiOperation(value = "查询API端点", notes = "查询所有可用的API端点（行情线路）")
    public Result<List<ApiEndpoint>> getApiEndpoints(
            @RequestParam(required = false) String endpointType) {
        try {
            List<ApiEndpoint> endpoints = apiEndpointService.getAvailableEndpoints(endpointType);
            return Result.success(endpoints);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/api-endpoint/default")
    @ApiOperation(value = "获取默认API端点", notes = "获取默认的API端点")
    public Result<ApiEndpoint> getDefaultEndpoint(
            @RequestParam String endpointType) {
        try {
            ApiEndpoint endpoint = apiEndpointService.getDefaultEndpoint(endpointType);
            return Result.success(endpoint);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/api-endpoint/set")
    @ApiOperation(value = "设置用户API端点", notes = "设置用户选择的API端点（行情线路）")
    public Result<Void> setUserEndpoint(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long endpointId) {
        try {
            apiEndpointService.setUserEndpoint(userId, endpointId);
            return Result.success("API端点设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/api-endpoint/user")
    @ApiOperation(value = "获取用户API端点", notes = "获取用户选择的API端点")
    public Result<ApiEndpoint> getUserEndpoint(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String endpointType) {
        try {
            ApiEndpoint endpoint = apiEndpointService.getUserEndpoint(userId, endpointType);
            return Result.success(endpoint);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 通知 ====================

    @GetMapping("/notifications")
    @ApiOperation(value = "查询通知", notes = "查询用户的通知列表")
    public Result<List<Notification>> getNotifications(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String type) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId, type);
            return Result.success(notifications);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/notifications/unread")
    @ApiOperation(value = "查询未读通知", notes = "查询用户的未读通知")
    public Result<List<Notification>> getUnreadNotifications(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(userId);
            return Result.success(notifications);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/notifications/{notificationId}/read")
    @ApiOperation(value = "标记通知为已读", notes = "标记指定通知为已读")
    public Result<Void> markAsRead(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(userId, notificationId);
            return Result.success("标记成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/notifications/read-all")
    @ApiOperation(value = "标记所有通知为已读", notes = "标记用户的所有通知为已读")
    public Result<Void> markAllAsRead(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            notificationService.markAllAsRead(userId);
            return Result.success("全部标记成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

