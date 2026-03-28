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
@Table(name = "kyc_advanced")
@Data
@EntityListeners(AuditingEntityListener.class)
public class KycAdvanced {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "kyc_basic_id", nullable = false)
    private Long kycBasicId;

    @Column(name = "handheld_image_url")
    private String handheldImageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PROCESSING, APPROVED, REJECTED

    @Column(name = "face_match_score")
    private Double faceMatchScore; // 人脸匹配分数

    @Column(name = "face_match_result")
    private Boolean faceMatchResult; // 人脸匹配结果

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "verification_statement")
    private String verificationStatement; // 用户说的验证语句

    @Column(name = "reviewer_id")
    private Long reviewerId; // 审核人ID

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















