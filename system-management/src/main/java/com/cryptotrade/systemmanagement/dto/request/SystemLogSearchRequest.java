/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志搜索请求DTO
 */
@Data
public class SystemLogSearchRequest {
    private Long userId; // 操作用户ID
    private String username; // 操作用户名
    private String operation; // 操作事务
    private String module; // 操作模块
    private String method; // 请求方法
    private String ipAddress; // IP地址
    private String deviceId; // 设备号
    private String region; // 地区
    private String status; // 操作状态: SUCCESS, FAILED, ERROR, ALL
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    
    // 分页参数
    private Integer page = 0;
    private Integer size = 20;
}














