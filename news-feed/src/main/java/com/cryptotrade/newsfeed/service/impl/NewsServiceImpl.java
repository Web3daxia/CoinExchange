/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsTagRelation;
import com.cryptotrade.newsfeed.repository.NewsRepository;
import com.cryptotrade.newsfeed.repository.NewsTagRelationRepository;
import com.cryptotrade.newsfeed.service.NewsService;
import com.cryptotrade.newsfeed.service.NewsTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻Service实现
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsTagRelationRepository tagRelationRepository;

    @Autowired(required = false)
    private NewsTranslationService translationService;

    @Override
    public Page<News> getNewsList(String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (status != null && !status.isEmpty()) {
            return newsRepository.findByStatusOrderByPublishTimeDesc(status, pageable);
        }
        return newsRepository.findByStatusOrderByPublishTimeDesc("PUBLISHED", pageable);
    }

    @Override
    public Page<News> getHotNews(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByIsHotTrueAndStatus("PUBLISHED", pageable);
    }

    @Override
    public Page<News> getRecommendedNews(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByIsRecommendedTrueAndStatus("PUBLISHED", pageable);
    }

    @Override
    public Page<News> getNewsByCategory(Long categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<News> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("categoryId"), categoryId));
            predicates.add(cb.equal(root.get("status"), "PUBLISHED"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return newsRepository.findAll(spec, pageable);
    }

    @Override
    public Page<News> getNewsByTag(Long tagId, Integer page, Integer size) {
        // 通过标签关联表获取新闻ID列表
        List<NewsTagRelation> relations = tagRelationRepository.findByTagId(tagId);
        List<Long> newsIds = new ArrayList<>();
        for (NewsTagRelation relation : relations) {
            newsIds.add(relation.getNewsId());
        }
        
        if (newsIds.isEmpty()) {
            return Page.empty(PageRequest.of(page, size));
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Specification<News> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), "PUBLISHED"));
            predicates.add(root.get("id").in(newsIds));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return newsRepository.findAll(spec, pageable);
    }

    @Override
    public Page<News> searchNews(String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<News> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), "PUBLISHED"));
            // 使用全文搜索
            predicates.add(cb.or(
                    cb.like(root.get("title"), "%" + keyword + "%"),
                    cb.like(root.get("summary"), "%" + keyword + "%"),
                    cb.like(root.get("content"), "%" + keyword + "%")
            ));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return newsRepository.findAll(spec, pageable);
    }

    @Override
    public News getNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));
    }

    @Override
    public News getNewsById(Long newsId, String languageCode) {
        if (translationService != null && languageCode != null) {
            return translationService.getNewsByLanguage(newsId, languageCode);
        }
        return getNewsById(newsId);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long newsId) {
        News news = getNewsById(newsId);
        news.setViewCount(news.getViewCount() + 1);
        newsRepository.save(news);
    }

    @Override
    @Transactional
    public News reviewNews(Long newsId, Long reviewerId, String status, String remark) {
        News news = getNewsById(newsId);
        news.setStatus(status);
        news.setReviewerId(reviewerId);
        news.setReviewTime(java.time.LocalDateTime.now());
        news.setReviewRemark(remark);
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public void setHotNews(Long newsId, boolean isHot) {
        News news = getNewsById(newsId);
        news.setIsHot(isHot);
        newsRepository.save(news);
    }

    @Override
    @Transactional
    public void setRecommendedNews(Long newsId, boolean isRecommended) {
        News news = getNewsById(newsId);
        news.setIsRecommended(isRecommended);
        newsRepository.save(news);
    }
}

