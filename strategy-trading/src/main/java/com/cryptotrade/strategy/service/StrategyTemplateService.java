/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import com.cryptotrade.strategy.entity.StrategyTemplate;

import java.util.List;

/**
 * 策略模板服务接口
 */
public interface StrategyTemplateService {
    /**
     * 获取策略模板列表
     */
    List<StrategyTemplate> getTemplates(String marketType, String strategyCategory);

    /**
     * 获取策略模板详情
     */
    StrategyTemplate getTemplate(Long templateId);

    /**
     * 创建策略模板（管理员）
     */
    StrategyTemplate createTemplate(StrategyTemplate template);

    /**
     * 更新策略模板（管理员）
     */
    StrategyTemplate updateTemplate(Long templateId, StrategyTemplate template);

    /**
     * 删除策略模板（管理员）
     */
    void deleteTemplate(Long templateId);
}













