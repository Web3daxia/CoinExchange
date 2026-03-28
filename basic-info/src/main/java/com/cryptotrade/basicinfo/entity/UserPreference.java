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
 * 用户偏好设置实体
 */
@Entity
@Table(name = "user_preferences")
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "language_code", nullable = false)
    private String languageCode; // 用户选择的语言代码

    @Column(name = "currency", nullable = false)
    private String currency; // 用户选择的法币，如 USD, EUR, CNY

    @Column(name = "api_endpoint_id")
    private Long apiEndpointId; // 用户选择的API端点ID

    @Column(name = "timezone")
    private String timezone; // 时区

    @Column(name = "date_format")
    private String dateFormat; // 日期格式

    @Column(name = "number_format")
    private String numberFormat; // 数字格式

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















