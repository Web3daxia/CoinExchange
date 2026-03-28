/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.controller;

import com.cryptotrade.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查接口
 */
@RestController
@RequestMapping
public class HealthController {

    @GetMapping("/")
    public Result<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "API is running");
        return Result.success(response);
    }

    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Service is healthy");
        return Result.success(response);
    }
}




