/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 新闻采集日志实体
 */
@Entity
@Table(name = "news_fetch_logs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsFetchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    private Long sourceId; // 新闻源ID

    @Column(name = "fetch_time", nullable = false)
    private LocalDateTime fetchTime; // 采集时间

    @Column(name = "fetch_status", nullable = false, length = 20)
    private String fetchStatus; // 采集状态: SUCCESS, FAILED, PARTIAL

    @Column(name = "fetched_count", nullable = false)
    private Integer fetchedCount = 0; // 采集到的新闻数量

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage; // 错误信息

    @Column(name = "execution_time")
    private Long executionTime; // 执行时间（毫秒）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














