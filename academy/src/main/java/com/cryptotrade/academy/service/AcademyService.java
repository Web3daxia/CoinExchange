/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.service;

import com.cryptotrade.academy.entity.AcademyContent;
import com.cryptotrade.academy.entity.AcademyComment;
import com.cryptotrade.academy.entity.AcademyFeedback;

import java.util.List;

/**
 * 学院服务接口
 */
public interface AcademyService {
    /**
     * 获取内容列表
     */
    List<AcademyContent> getContentList(String contentType, String category, String languageCode);

    /**
     * 获取内容详情
     */
    AcademyContent getContentDetail(Long contentId);

    /**
     * 创建内容
     */
    AcademyContent createContent(AcademyContent content);

    /**
     * 更新内容
     */
    AcademyContent updateContent(Long contentId, AcademyContent content);

    /**
     * 删除内容
     */
    void deleteContent(Long contentId);

    /**
     * 添加评论
     */
    AcademyComment addComment(Long contentId, Long userId, String commentText, Long parentId);

    /**
     * 获取评论列表
     */
    List<AcademyComment> getComments(Long contentId);

    /**
     * 提交反馈
     */
    AcademyFeedback submitFeedback(Long contentId, Long userId, Integer rating, String feedbackText);
}















