/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统操作日志实体
 */
@Entity
@Table(name = "system_logs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 操作用户ID（关联system_admins表）

    @Column(name = "username", length = 100)
    private String username; // 操作用户名

    @Column(name = "operation", nullable = false, length = 200)
    private String operation; // 操作事务（操作描述）

    @Column(name = "module", length = 100)
    private String module; // 操作模块

    @Column(name = "path", length = 500)
    private String path; // 操作路径（请求URL）

    @Column(name = "method", length = 20)
    private String method; // 请求方法: GET, POST, PUT, DELETE等

    @Column(name = "ip_address", length = 50)
    private String ipAddress; // IP地址

    @Column(name = "device_id", length = 200)
    private String deviceId; // 设备号/设备标识

    @Column(name = "region", length = 100)
    private String region; // 地区（根据IP地址解析）

    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams; // 请求参数（JSON格式）

    @Column(name = "response_status")
    private Integer responseStatus; // 响应状态码

    @Column(name = "execution_time")
    private Long executionTime; // 执行时间（毫秒）

    @Column(name = "status", length = 20)
    private String status = "SUCCESS"; // 操作状态: SUCCESS, FAILED, ERROR

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage; // 错误信息

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 创建时间（操作时间）
}














