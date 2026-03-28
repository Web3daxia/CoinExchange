/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.service.impl;

import com.cryptotrade.academy.entity.AcademyContent;
import com.cryptotrade.academy.entity.AcademyComment;
import com.cryptotrade.academy.entity.AcademyFeedback;
import com.cryptotrade.academy.repository.AcademyCommentRepository;
import com.cryptotrade.academy.repository.AcademyContentRepository;
import com.cryptotrade.academy.repository.AcademyFeedbackRepository;
import com.cryptotrade.academy.service.AcademyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 学院服务实现
 */
@Service
public class AcademyServiceImpl implements AcademyService {
    @Autowired
    private AcademyContentRepository contentRepository;

    @Autowired
    private AcademyCommentRepository commentRepository;

    @Autowired
    private AcademyFeedbackRepository feedbackRepository;

    @Override
    public List<AcademyContent> getContentList(String contentType, String category, String languageCode) {
        List<AcademyContent> contents = contentRepository.findByLanguageCodeAndStatus(
                languageCode != null ? languageCode : "en", "PUBLISHED");

        if (contentType != null) {
            contents = contents.stream()
                    .filter(c -> contentType.equals(c.getContentType()))
                    .collect(Collectors.toList());
        }

        if (category != null) {
            contents = contents.stream()
                    .filter(c -> category.equals(c.getCategory()))
                    .collect(Collectors.toList());
        }

        return contents;
    }

    @Override
    @Transactional
    public AcademyContent getContentDetail(Long contentId) {
        Objects.requireNonNull(contentId, "内容ID不能为空");
        AcademyContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        // 增加浏览量
        content.setViewCount(content.getViewCount() + 1);
        contentRepository.save(content);

        return content;
    }

    @Override
    @Transactional
    public AcademyContent createContent(AcademyContent content) {
        content.setViewCount(0L);
        content.setLikeCount(0L);
        content.setCommentCount(0L);
        content.setStatus("DRAFT");
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Override
    @Transactional
    public AcademyContent updateContent(Long contentId, AcademyContent content) {
        Objects.requireNonNull(contentId, "内容ID不能为空");
        AcademyContent existing = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        if (content.getTitle() != null) {
            existing.setTitle(content.getTitle());
        }
        if (content.getSummary() != null) {
            existing.setSummary(content.getSummary());
        }
        if (content.getContent() != null) {
            existing.setContent(content.getContent());
        }
        if (content.getIconUrl() != null) {
            existing.setIconUrl(content.getIconUrl());
        }
        if (content.getCoverUrl() != null) {
            existing.setCoverUrl(content.getCoverUrl());
        }
        if (content.getVideoUrl() != null) {
            existing.setVideoUrl(content.getVideoUrl());
        }
        if (content.getCategory() != null) {
            existing.setCategory(content.getCategory());
        }
        if (content.getTags() != null) {
            existing.setTags(content.getTags());
        }
        if (content.getStatus() != null) {
            existing.setStatus(content.getStatus());
            if ("PUBLISHED".equals(content.getStatus()) && existing.getPublishTime() == null) {
                existing.setPublishTime(LocalDateTime.now());
            }
        }
        existing.setUpdatedAt(LocalDateTime.now());
        return contentRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteContent(Long contentId) {
        Objects.requireNonNull(contentId, "内容ID不能为空");
        AcademyContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));
        content.setStatus("ARCHIVED");
        contentRepository.save(content);
    }

    @Override
    @Transactional
    public AcademyComment addComment(Long contentId, Long userId, String commentText, Long parentId) {
        Objects.requireNonNull(contentId, "内容ID不能为空");
        Objects.requireNonNull(userId, "用户ID不能为空");
        AcademyContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        AcademyComment comment = new AcademyComment();
        comment.setContentId(contentId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setCommentText(commentText);
        comment.setStatus("ACTIVE");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);

        // 更新评论数
        content.setCommentCount(content.getCommentCount() + 1);
        contentRepository.save(content);

        return comment;
    }

    @Override
    public List<AcademyComment> getComments(Long contentId) {
        return commentRepository.findByContentIdAndStatus(contentId, "ACTIVE");
    }

    @Override
    @Transactional
    public AcademyFeedback submitFeedback(Long contentId, Long userId, Integer rating, String feedbackText) {
        AcademyFeedback existing = feedbackRepository.findByContentIdAndUserId(contentId, userId).orElse(null);
        
        if (existing != null) {
            existing.setRating(rating);
            existing.setFeedbackText(feedbackText);
            existing.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(existing);
        } else {
            AcademyFeedback feedback = new AcademyFeedback();
            feedback.setContentId(contentId);
            feedback.setUserId(userId);
            feedback.setRating(rating);
            feedback.setFeedbackText(feedbackText);
            feedback.setCreatedAt(LocalDateTime.now());
            feedback.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        }
    }
}














