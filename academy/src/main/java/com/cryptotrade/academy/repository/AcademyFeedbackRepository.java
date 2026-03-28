/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.repository;

import com.cryptotrade.academy.entity.AcademyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 学院反馈Repository
 */
@Repository
public interface AcademyFeedbackRepository extends JpaRepository<AcademyFeedback, Long> {
    /**
     * 根据内容ID和用户ID查询
     */
    Optional<AcademyFeedback> findByContentIdAndUserId(Long contentId, Long userId);

    /**
     * 计算内容平均评分
     */
    @Query("SELECT AVG(af.rating) FROM AcademyFeedback af WHERE af.contentId = :contentId")
    BigDecimal calculateAverageRating(@Param("contentId") Long contentId);
}















