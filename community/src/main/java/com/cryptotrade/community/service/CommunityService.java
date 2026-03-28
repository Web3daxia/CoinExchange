/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.service;

import com.cryptotrade.community.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 社区服务接口
 */
public interface CommunityService {
    /**
     * 创建内容
     */
    CommunityContent createContent(Long userId, CommunityContent content);

    /**
     * 获取内容列表
     */
    List<CommunityContent> getContentList(String category, String contentType);

    /**
     * 获取内容详情
     */
    CommunityContent getContentDetail(Long contentId);

    /**
     * 添加评论
     */
    CommunityComment addComment(Long contentId, Long userId, String commentText, Long parentId);

    /**
     * 点赞内容
     */
    void likeContent(Long contentId, Long userId);

    /**
     * 取消点赞
     */
    void unlikeContent(Long contentId, Long userId);

    /**
     * 关注用户
     */
    void followUser(Long followerId, Long followingId);

    /**
     * 取消关注
     */
    void unfollowUser(Long followerId, Long followingId);

    /**
     * 赞赏内容
     */
    CommunityReward rewardContent(Long contentId, Long fromUserId, String currency, BigDecimal amount);

    /**
     * 获取用户排行榜
     */
    List<Map<String, Object>> getLeaderboard(String rankType, Integer limit);
}















