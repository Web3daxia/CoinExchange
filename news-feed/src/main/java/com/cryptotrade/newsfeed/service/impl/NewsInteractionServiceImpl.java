/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsInteraction;
import com.cryptotrade.newsfeed.repository.NewsInteractionRepository;
import com.cryptotrade.newsfeed.repository.NewsRepository;
import com.cryptotrade.newsfeed.service.NewsInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 新闻互动Service实现
 */
@Service
public class NewsInteractionServiceImpl implements NewsInteractionService {

    @Autowired
    private NewsInteractionRepository interactionRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Override
    @Transactional
    public NewsInteraction likeNews(Long userId, Long newsId) {
        // 检查是否已点赞
        NewsInteraction existing = interactionRepository.findByUserIdAndNewsIdAndInteractionType(userId, newsId, "LIKE")
                .orElse(null);

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));

        if (existing != null) {
            // 取消点赞
            interactionRepository.delete(existing);
            news.setLikeCount(Math.max(0, news.getLikeCount() - 1));
            newsRepository.save(news);
            return null; // 或者返回一个标记表示已取消
        } else {
            // 点赞
            NewsInteraction interaction = new NewsInteraction();
            interaction.setUserId(userId);
            interaction.setNewsId(newsId);
            interaction.setInteractionType("LIKE");
            NewsInteraction saved = interactionRepository.save(interaction);

            news.setLikeCount(news.getLikeCount() + 1);
            newsRepository.save(news);

            return saved;
        }
    }

    @Override
    @Transactional
    public NewsInteraction shareNews(Long userId, Long newsId, String platform) {
        NewsInteraction interaction = new NewsInteraction();
        interaction.setUserId(userId);
        interaction.setNewsId(newsId);
        interaction.setInteractionType("SHARE");
        interaction.setSharePlatform(platform);
        NewsInteraction saved = interactionRepository.save(interaction);

        // 更新新闻分享数
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));
        news.setShareCount(news.getShareCount() + 1);
        newsRepository.save(news);

        return saved;
    }

    @Override
    @Transactional
    public void viewNews(Long userId, Long newsId) {
        // 检查是否已记录浏览（可以设置只记录一次，或每次浏览都记录）
        boolean exists = interactionRepository.existsByUserIdAndNewsIdAndInteractionType(userId, newsId, "VIEW");
        if (!exists) {
            NewsInteraction interaction = new NewsInteraction();
            interaction.setUserId(userId);
            interaction.setNewsId(newsId);
            interaction.setInteractionType("VIEW");
            interactionRepository.save(interaction);
        }
    }

    @Override
    public boolean isLiked(Long userId, Long newsId) {
        return interactionRepository.existsByUserIdAndNewsIdAndInteractionType(userId, newsId, "LIKE");
    }

    @Override
    public Long getLikeCount(Long newsId) {
        return interactionRepository.countByNewsIdAndInteractionType(newsId, "LIKE");
    }

    @Override
    public Long getShareCount(Long newsId) {
        return interactionRepository.countByNewsIdAndInteractionType(newsId, "SHARE");
    }
}














