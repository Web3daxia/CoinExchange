/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_basic")
@Data
@EntityListeners(AuditingEntityListener.class)
public class KycBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @Column(name = "id_type", nullable = false)
    private String idType; // ID_CARD, PASSPORT, DRIVER_LICENSE

    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @Column(name = "front_image_url")
    private String frontImageUrl;

    @Column(name = "back_image_url")
    private String backImageUrl;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PROCESSING, APPROVED, REJECTED

    @Column(name = "review_type")
    private String reviewType; // AUTO, MANUAL

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "ocr_result", length = 2000)
    private String ocrResult; // OCR识别结果（JSON格式）

    @Column(name = "reviewer_id")
    private Long reviewerId; // 审核人ID（人工审核时）

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















