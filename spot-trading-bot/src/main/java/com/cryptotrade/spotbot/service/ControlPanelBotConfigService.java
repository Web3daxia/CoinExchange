/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service;

import com.cryptotrade.spotbot.dto.request.ControlPanelBotConfigRequest;
import com.cryptotrade.spotbot.entity.ControlPanelBotConfig;

import java.util.List;
import java.util.Optional;

/**
 * 控盘机器人配置Service接口
 */
public interface ControlPanelBotConfigService {
    
    /**
     * 创建控盘机器人配置
     */
    ControlPanelBotConfig createConfig(ControlPanelBotConfigRequest request);

    /**
     * 更新控盘机器人配置
     */
    ControlPanelBotConfig updateConfig(Long id, ControlPanelBotConfigRequest request);

    /**
     * 根据ID获取配置
     */
    Optional<ControlPanelBotConfig> getConfigById(Long id);

    /**
     * 根据交易对名称获取配置
     */
    Optional<ControlPanelBotConfig> getConfigByPairName(String pairName);

    /**
     * 获取所有配置
     */
    List<ControlPanelBotConfig> getAllConfigs();

    /**
     * 删除配置
     */
    void deleteConfig(Long id);

    /**
     * 删除配置（根据交易对名称）
     */
    void deleteConfigByPairName(String pairName);

    /**
     * 启用/禁用配置
     */
    ControlPanelBotConfig updateStatus(Long id, String status);
}














