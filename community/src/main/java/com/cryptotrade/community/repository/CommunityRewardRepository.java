/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.repository;

import com.cryptotrade.community.entity.CommunityReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 社区赞赏Repository
 */
@Repository
public interface CommunityRewardRepository extends JpaRepository<CommunityReward, Long> {
    /**
     * 根据内容ID查询赞赏
     */
    List<CommunityReward> findByContentId(Long contentId);

    /**
     * 统计用户收到的赞赏总额
     */
    @Query("SELECT COALESCE(SUM(cr.rewardAmount), 0) FROM CommunityReward cr WHERE cr.toUserId = :userId")
    BigDecimal sumRewardByUserId(@Param("userId") Long userId);
}















