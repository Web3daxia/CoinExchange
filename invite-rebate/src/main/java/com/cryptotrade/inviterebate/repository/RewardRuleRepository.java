/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.RewardRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 奖励规则Repository
 */
@Repository
public interface RewardRuleRepository extends JpaRepository<RewardRule, Long> {
    /**
     * 根据奖励类型查询规则
     */
    Optional<RewardRule> findByRewardTypeAndStatus(String rewardType, String status);

    /**
     * 查询所有启用的奖励规则
     */
    List<RewardRule> findByStatus(String status);
}















