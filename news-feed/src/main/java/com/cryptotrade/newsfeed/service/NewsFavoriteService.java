/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.NewsFavorite;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 新闻收藏Service接口
 */
public interface NewsFavoriteService {
    
    /**
     * 收藏新闻
     */
    NewsFavorite favoriteNews(Long userId, Long newsId);
    
    /**
     * 取消收藏
     */
    void unfavoriteNews(Long userId, Long newsId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long newsId);
    
    /**
     * 获取用户的收藏列表
     */
    List<NewsFavorite> getUserFavorites(Long userId);
    
    /**
     * 获取用户的分页收藏列表
     */
    Page<NewsFavorite> getUserFavorites(Long userId, Integer page, Integer size);
}














