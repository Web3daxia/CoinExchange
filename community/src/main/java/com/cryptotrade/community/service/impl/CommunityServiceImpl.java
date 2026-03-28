/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.service.impl;

import com.cryptotrade.community.entity.*;
import com.cryptotrade.community.repository.*;
import com.cryptotrade.community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 社区服务实现
 */
@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityContentRepository contentRepository;

    @Autowired
    private CommunityCommentRepository commentRepository;

    @Autowired
    private CommunityLikeRepository likeRepository;

    @Autowired
    private CommunityFollowRepository followRepository;

    @Autowired
    private CommunityRewardRepository rewardRepository;

    @Override
    @Transactional
    public CommunityContent createContent(Long userId, CommunityContent content) {
        content.setUserId(userId);
        content.setLikeCount(0L);
        content.setCommentCount(0L);
        content.setShareCount(0L);
        content.setRewardCount(0L);
        content.setViewCount(0L);
        content.setStatus("PUBLISHED");
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Override
    public List<CommunityContent> getContentList(String category, String contentType) {
        List<CommunityContent> contents = contentRepository.findByStatus("PUBLISHED");

        if (category != null) {
            contents = contents.stream()
                    .filter(c -> category.equals(c.getCategory()))
                    .collect(Collectors.toList());
        }

        if (contentType != null) {
            contents = contents.stream()
                    .filter(c -> contentType.equals(c.getContentType()))
                    .collect(Collectors.toList());
        }

        return contents;
    }

    @Override
    @Transactional
    public CommunityContent getContentDetail(Long contentId) {
        CommunityContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        content.setViewCount(content.getViewCount() + 1);
        contentRepository.save(content);

        return content;
    }

    @Override
    @Transactional
    public CommunityComment addComment(Long contentId, Long userId, String commentText, Long parentId) {
        CommunityContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        CommunityComment comment = new CommunityComment();
        comment.setContentId(contentId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setCommentText(commentText);
        comment.setLikeCount(0L);
        comment.setStatus("ACTIVE");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);

        content.setCommentCount(content.getCommentCount() + 1);
        contentRepository.save(content);

        return comment;
    }

    @Override
    @Transactional
    public void likeContent(Long contentId, Long userId) {
        Optional<CommunityLike> existing = likeRepository.findByContentIdAndUserId(contentId, userId);
        if (existing.isPresent()) {
            return; // 已点赞
        }

        CommunityLike like = new CommunityLike();
        like.setContentId(contentId);
        like.setUserId(userId);
        like.setCreatedAt(LocalDateTime.now());
        likeRepository.save(like);

        CommunityContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));
        content.setLikeCount(content.getLikeCount() + 1);
        contentRepository.save(content);
    }

    @Override
    @Transactional
    public void unlikeContent(Long contentId, Long userId) {
        Optional<CommunityLike> likeOpt = likeRepository.findByContentIdAndUserId(contentId, userId);
        if (likeOpt.isPresent()) {
            likeRepository.delete(likeOpt.get());

            CommunityContent content = contentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("内容不存在"));
            content.setLikeCount(Math.max(0, content.getLikeCount() - 1));
            contentRepository.save(content);
        }
    }

    @Override
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }

        Optional<CommunityFollow> existing = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (existing.isPresent()) {
            return; // 已关注
        }

        CommunityFollow follow = new CommunityFollow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedAt(LocalDateTime.now());
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        Optional<CommunityFollow> followOpt = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (followOpt.isPresent()) {
            followRepository.delete(followOpt.get());
        }
    }

    @Override
    @Transactional
    public CommunityReward rewardContent(Long contentId, Long fromUserId, String currency, BigDecimal amount) {
        CommunityContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("内容不存在"));

        if (fromUserId.equals(content.getUserId())) {
            throw new RuntimeException("不能赞赏自己的内容");
        }

        // 扣除赞赏者余额（调用钱包服务）
        // walletService.deductBalance(fromUserId, currency, amount);

        // 增加被赞赏者余额（调用钱包服务）
        // walletService.addBalance(content.getUserId(), currency, amount);

        CommunityReward reward = new CommunityReward();
        reward.setContentId(contentId);
        reward.setFromUserId(fromUserId);
        reward.setToUserId(content.getUserId());
        reward.setRewardCurrency(currency);
        reward.setRewardAmount(amount);
        reward.setCreatedAt(LocalDateTime.now());
        reward = rewardRepository.save(reward);

        content.setRewardCount(content.getRewardCount() + 1);
        contentRepository.save(content);

        return reward;
    }

    @Override
    public List<Map<String, Object>> getLeaderboard(String rankType, Integer limit) {
        List<Map<String, Object>> leaderboard = new ArrayList<>();

        if ("reward".equals(rankType)) {
            // 按赞赏金额排行
            List<CommunityReward> rewards = rewardRepository.findAll();
            Map<Long, BigDecimal> userRewards = rewards.stream()
                    .collect(Collectors.groupingBy(
                            CommunityReward::getToUserId,
                            Collectors.reducing(BigDecimal.ZERO, CommunityReward::getRewardAmount, BigDecimal::add)
                    ));

            leaderboard = userRewards.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(limit != null ? limit : 10)
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("userId", entry.getKey());
                        item.put("totalReward", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());
        } else if ("content".equals(rankType)) {
            // 按内容数排行
            List<CommunityContent> contents = contentRepository.findAll();
            Map<Long, Long> userContentCount = contents.stream()
                    .filter(c -> "PUBLISHED".equals(c.getStatus()))
                    .collect(Collectors.groupingBy(
                            CommunityContent::getUserId,
                            Collectors.counting()
                    ));

            leaderboard = userContentCount.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(limit != null ? limit : 10)
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("userId", entry.getKey());
                        item.put("contentCount", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return leaderboard;
    }
}















