/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service.impl;

import com.cryptotrade.spotbot.dto.request.ControlPanelBotConfigRequest;
import com.cryptotrade.spotbot.entity.ControlPanelBotConfig;
import com.cryptotrade.spotbot.repository.ControlPanelBotConfigRepository;
import com.cryptotrade.spotbot.service.ControlPanelBotConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 控盘机器人配置Service实现
 */
@Service
public class ControlPanelBotConfigServiceImpl implements ControlPanelBotConfigService {

    @Autowired
    private ControlPanelBotConfigRepository configRepository;

    @Override
    @Transactional
    public ControlPanelBotConfig createConfig(ControlPanelBotConfigRequest request) {
        // 检查交易对是否已存在配置
        if (configRepository.existsByPairName(request.getPairName())) {
            throw new IllegalArgumentException("交易对 " + request.getPairName() + " 的配置已存在");
        }

        ControlPanelBotConfig config = new ControlPanelBotConfig();
        BeanUtils.copyProperties(request, config);
        config.setCurrentPrice(request.getInitialPrice());
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());
        
        return configRepository.save(config);
    }

    @Override
    @Transactional
    public ControlPanelBotConfig updateConfig(Long id, ControlPanelBotConfigRequest request) {
        ControlPanelBotConfig config = configRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("配置不存在，ID: " + id));

        // 如果交易对名称改变，检查新名称是否已存在
        if (!config.getPairName().equals(request.getPairName())) {
            if (configRepository.existsByPairName(request.getPairName())) {
                throw new IllegalArgumentException("交易对 " + request.getPairName() + " 的配置已存在");
            }
        }

        BeanUtils.copyProperties(request, config, "id", "createdAt");
        config.setUpdatedAt(LocalDateTime.now());
        
        return configRepository.save(config);
    }

    @Override
    public Optional<ControlPanelBotConfig> getConfigById(Long id) {
        return configRepository.findById(id);
    }

    @Override
    public Optional<ControlPanelBotConfig> getConfigByPairName(String pairName) {
        return configRepository.findByPairName(pairName);
    }

    @Override
    public List<ControlPanelBotConfig> getAllConfigs() {
        return configRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        configRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteConfigByPairName(String pairName) {
        configRepository.deleteByPairName(pairName);
    }

    @Override
    @Transactional
    public ControlPanelBotConfig updateStatus(Long id, String status) {
        ControlPanelBotConfig config = configRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("配置不存在，ID: " + id));
        config.setStatus(status);
        config.setUpdatedAt(LocalDateTime.now());
        return configRepository.save(config);
    }
}














