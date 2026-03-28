/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 新闻源配置实体
 */
@Entity
@Table(name = "news_sources")
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_name", nullable = false, length = 200)
    private String sourceName; // 新闻源名称

    @Column(name = "source_code", unique = true, nullable = false, length = 50)
    private String sourceCode; // 新闻源代码（唯一标识）

    @Column(name = "api_url", nullable = false, length = 500)
    private String apiUrl; // API地址

    @Column(name = "api_key", length = 200)
    private String apiKey; // API密钥

    @Column(name = "api_secret", length = 200)
    private String apiSecret; // API密钥（如果需要）

    @Column(name = "fetch_interval", nullable = false)
    private Integer fetchInterval = 3600; // 采集间隔（秒）

    @Column(name = "last_fetch_time")
    private LocalDateTime lastFetchTime; // 上次采集时间

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE, ERROR

    @Column(name = "priority", nullable = false)
    private Integer priority = 0; // 优先级

    @Column(name = "description", length = 500)
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














