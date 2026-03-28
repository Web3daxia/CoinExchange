/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.RebateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 返佣配置Repository
 */
@Repository
public interface RebateConfigRepository extends JpaRepository<RebateConfig, Long> {
    /**
     * 根据用户ID查询返佣配置
     */
    Optional<RebateConfig> findByUserId(Long userId);
}















