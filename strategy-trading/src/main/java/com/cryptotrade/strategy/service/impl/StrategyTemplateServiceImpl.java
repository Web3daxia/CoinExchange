/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.impl;

import com.cryptotrade.strategy.entity.StrategyTemplate;
import com.cryptotrade.strategy.repository.StrategyTemplateRepository;
import com.cryptotrade.strategy.service.StrategyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 策略模板服务实现类
 */
@Service
public class StrategyTemplateServiceImpl implements StrategyTemplateService {

    @Autowired
    private StrategyTemplateRepository templateRepository;

    @Override
    public List<StrategyTemplate> getTemplates(String marketType, String strategyCategory) {
        List<StrategyTemplate> templates;
        
        if (marketType != null && strategyCategory != null) {
            templates = templateRepository.findAll().stream()
                    .filter(t -> marketType.equals(t.getMarketType()) && 
                               strategyCategory.equals(t.getStrategyCategory()) &&
                               "ACTIVE".equals(t.getStatus()))
                    .collect(Collectors.toList());
        } else if (marketType != null) {
            templates = templateRepository.findByMarketTypeAndStatus(marketType, "ACTIVE");
        } else if (strategyCategory != null) {
            templates = templateRepository.findByStrategyCategory(strategyCategory).stream()
                    .filter(t -> "ACTIVE".equals(t.getStatus()))
                    .collect(Collectors.toList());
        } else {
            templates = templateRepository.findByStatus("ACTIVE");
        }
        
        return templates;
    }

    @Override
    public StrategyTemplate getTemplate(Long templateId) {
        Optional<StrategyTemplate> templateOpt = templateRepository.findById(templateId);
        return templateOpt.orElseThrow(() -> new RuntimeException("策略模板不存在"));
    }

    @Override
    @Transactional
    public StrategyTemplate createTemplate(StrategyTemplate template) {
        if (template.getStatus() == null) {
            template.setStatus("ACTIVE");
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public StrategyTemplate updateTemplate(Long templateId, StrategyTemplate template) {
        StrategyTemplate existingTemplate = getTemplate(templateId);
        
        if (template.getTemplateName() != null) {
            existingTemplate.setTemplateName(template.getTemplateName());
        }
        if (template.getDescription() != null) {
            existingTemplate.setDescription(template.getDescription());
        }
        if (template.getDefaultParams() != null) {
            existingTemplate.setDefaultParams(template.getDefaultParams());
        }
        if (template.getStatus() != null) {
            existingTemplate.setStatus(template.getStatus());
        }
        
        return templateRepository.save(existingTemplate);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        StrategyTemplate template = getTemplate(templateId);
        templateRepository.delete(template);
    }
}













