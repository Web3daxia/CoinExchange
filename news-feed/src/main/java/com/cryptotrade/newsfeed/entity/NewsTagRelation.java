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
 * 新闻标签关联实体
 */
@Entity
@Table(name = "news_tag_relations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"news_id", "tag_id"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsTagRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "news_id", nullable = false)
    private Long newsId; // 新闻ID

    @Column(name = "tag_id", nullable = false)
    private Long tagId; // 标签ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














