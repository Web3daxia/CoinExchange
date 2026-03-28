/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.SensitiveWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 敏感词Repository
 */
@Repository
public interface SensitiveWordRepository extends JpaRepository<SensitiveWord, Long> {
    
    Optional<SensitiveWord> findByWord(String word);
    
    List<SensitiveWord> findByStatus(String status);
    
    List<SensitiveWord> findByWordType(String wordType);
}














