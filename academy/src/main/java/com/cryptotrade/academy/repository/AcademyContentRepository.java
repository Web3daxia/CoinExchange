/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.repository;

import com.cryptotrade.academy.entity.AcademyContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学院内容Repository
 */
@Repository
public interface AcademyContentRepository extends JpaRepository<AcademyContent, Long> {
    /**
     * 根据内容类型查询
     */
    List<AcademyContent> findByContentTypeAndStatus(String contentType, String status);

    /**
     * 根据语言查询
     */
    List<AcademyContent> findByLanguageCodeAndStatus(String languageCode, String status);

    /**
     * 根据分类查询
     */
    List<AcademyContent> findByCategoryAndStatus(String category, String status);
}















