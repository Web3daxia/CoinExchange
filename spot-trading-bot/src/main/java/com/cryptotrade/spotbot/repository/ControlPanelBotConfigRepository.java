/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.repository;

import com.cryptotrade.spotbot.entity.ControlPanelBotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 控盘机器人配置Repository
 */
@Repository
public interface ControlPanelBotConfigRepository extends JpaRepository<ControlPanelBotConfig, Long> {
    
    /**
     * 根据交易对名称查找配置
     */
    Optional<ControlPanelBotConfig> findByPairName(String pairName);

    /**
     * 根据交易对名称删除配置
     */
    void deleteByPairName(String pairName);

    /**
     * 检查交易对名称是否存在
     */
    boolean existsByPairName(String pairName);
}














