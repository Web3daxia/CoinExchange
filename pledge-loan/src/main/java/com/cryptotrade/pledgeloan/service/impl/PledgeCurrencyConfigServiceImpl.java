/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeCurrencyConfig;
import com.cryptotrade.pledgeloan.repository.PledgeCurrencyConfigRepository;
import com.cryptotrade.pledgeloan.service.PledgeCurrencyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 质押币种配置Service实现
 */
@Service
public class PledgeCurrencyConfigServiceImpl implements PledgeCurrencyConfigService {

    @Autowired
    private PledgeCurrencyConfigRepository configRepository;

    @Override
    public List<PledgeCurrencyConfig> getAllConfigs() {
        return configRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public PledgeCurrencyConfig getConfigById(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在: " + id));
    }

    @Override
    public PledgeCurrencyConfig getConfigByCurrencyCode(String currencyCode) {
        return configRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("配置不存在: " + currencyCode));
    }

    @Override
    @Transactional
    public PledgeCurrencyConfig createConfig(PledgeCurrencyConfig config) {
        if (configRepository.findByCurrencyCode(config.getCurrencyCode()).isPresent()) {
            throw new RuntimeException("币种配置已存在: " + config.getCurrencyCode());
        }
        config.setStatus("ACTIVE");
        return configRepository.save(config);
    }

    @Override
    @Transactional
    public PledgeCurrencyConfig updateConfig(Long id, PledgeCurrencyConfig config) {
        PledgeCurrencyConfig existing = getConfigById(id);
        
        existing.setCurrencyName(config.getCurrencyName());
        existing.setLoanRatio(config.getLoanRatio());
        existing.setInterestRate(config.getInterestRate());
        existing.setMinPledgeAmount(config.getMinPledgeAmount());
        existing.setMaxLoanAmount(config.getMaxLoanAmount());
        existing.setRiskRate(config.getRiskRate());
        existing.setMaintenanceRate(config.getMaintenanceRate());
        existing.setSortOrder(config.getSortOrder());
        existing.setDescription(config.getDescription());
        
        return configRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        if (!configRepository.existsById(id)) {
            throw new RuntimeException("配置不存在: " + id);
        }
        configRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PledgeCurrencyConfig updateStatus(Long id, String status) {
        PledgeCurrencyConfig config = getConfigById(id);
        config.setStatus(status);
        return configRepository.save(config);
    }
}














