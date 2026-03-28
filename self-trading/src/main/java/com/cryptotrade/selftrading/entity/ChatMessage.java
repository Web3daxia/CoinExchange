/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 聊天消息实体
 */
@Entity
@Table(name = "chat_messages")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 关联的订单ID

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 发送者ID

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId; // 接收者ID

    @Column(name = "message_type", nullable = false)
    private String messageType; // TEXT（文字）、IMAGE（图片）、VIDEO（视频）、FILE（文件）、EMOJI（表情）

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 消息内容

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl; // 文件URL（图片、视频、文件）

    @Column(name = "file_name")
    private String fileName; // 文件名

    @Column(name = "file_size")
    private Long fileSize; // 文件大小（字节）

    @Column(name = "is_read")
    private Boolean isRead; // 是否已读

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















