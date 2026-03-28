/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * API端点配置实体（行情线路）
 */
@Entity
@Table(name = "api_endpoints")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ApiEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endpoint_name", nullable = false)
    private String endpointName; // 端点名称，如 主线路、备用线路1、备用线路2

    @Column(name = "endpoint_type", nullable = false)
    private String endpointType; // 端点类型，如 MARKET_DATA（行情数据）、TRADING（交易）、WEBSOCKET（WebSocket）

    @Column(name = "base_url", nullable = false)
    private String baseUrl; // 基础URL

    @Column(name = "region")
    private String region; // 区域，如 ASIA, EUROPE, AMERICA

    @Column(name = "priority", nullable = false)
    private Integer priority; // 优先级（数字越小优先级越高）

    @Column(name = "is_default")
    private Boolean isDefault; // 是否默认线路

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）、MAINTENANCE（维护中）

    @Column(name = "response_time")
    private Long responseTime; // 响应时间（毫秒）

    @Column(name = "last_check_time")
    private LocalDateTime lastCheckTime; // 最后检查时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















