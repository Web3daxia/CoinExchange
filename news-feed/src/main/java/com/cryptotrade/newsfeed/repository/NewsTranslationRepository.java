/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 新闻多语言翻译Repository
 */
@Repository
public interface NewsTranslationRepository extends JpaRepository<NewsTranslation, Long> {
    
    Optional<NewsTranslation> findByNewsIdAndLanguageCode(Long newsId, String languageCode);
    
    List<NewsTranslation> findByNewsId(Long newsId);
    
    List<NewsTranslation> findByLanguageCode(String languageCode);
    
    List<NewsTranslation> findByTranslationStatus(String translationStatus);
    
    List<NewsTranslation> findByNewsIdAndTranslationStatus(Long newsId, String translationStatus);
}














