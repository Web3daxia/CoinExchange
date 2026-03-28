/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.entity.NewsFavorite;
import com.cryptotrade.newsfeed.repository.NewsFavoriteRepository;
import com.cryptotrade.newsfeed.service.NewsFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 新闻收藏Service实现
 */
@Service
public class NewsFavoriteServiceImpl implements NewsFavoriteService {

    @Autowired
    private NewsFavoriteRepository favoriteRepository;

    @Override
    @Transactional
    public NewsFavorite favoriteNews(Long userId, Long newsId) {
        // 检查是否已收藏
        if (favoriteRepository.existsByUserIdAndNewsId(userId, newsId)) {
            throw new RuntimeException("新闻已收藏");
        }

        NewsFavorite favorite = new NewsFavorite();
        favorite.setUserId(userId);
        favorite.setNewsId(newsId);
        return favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void unfavoriteNews(Long userId, Long newsId) {
        NewsFavorite favorite = favoriteRepository.findByUserIdAndNewsId(userId, newsId)
                .orElseThrow(() -> new RuntimeException("未收藏此新闻"));
        favoriteRepository.delete(favorite);
    }

    @Override
    public boolean isFavorited(Long userId, Long newsId) {
        return favoriteRepository.existsByUserIdAndNewsId(userId, newsId);
    }

    @Override
    public List<NewsFavorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public Page<NewsFavorite> getUserFavorites(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return favoriteRepository.findByUserId(userId, pageable);
    }
}














