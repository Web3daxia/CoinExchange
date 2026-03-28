/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;
import com.cryptotrade.pledgeloan.repository.LoanCurrencyConfigRepository;
import com.cryptotrade.pledgeloan.service.LoanCurrencyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 借款币种配置Service实现
 */
@Service
public class LoanCurrencyConfigServiceImpl implements LoanCurrencyConfigService {

    @Autowired
    private LoanCurrencyConfigRepository configRepository;

    @Override
    public List<LoanCurrencyConfig> getAllConfigs() {
        return configRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public LoanCurrencyConfig getConfigById(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在: " + id));
    }

    @Override
    public LoanCurrencyConfig getConfigByCurrencyCode(String currencyCode) {
        return configRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("配置不存在: " + currencyCode));
    }

    @Override
    @Transactional
    public LoanCurrencyConfig createConfig(LoanCurrencyConfig config) {
        if (configRepository.findByCurrencyCode(config.getCurrencyCode()).isPresent()) {
            throw new RuntimeException("币种配置已存在: " + config.getCurrencyCode());
        }
        config.setStatus("ACTIVE");
        return configRepository.save(config);
    }

    @Override
    @Transactional
    public LoanCurrencyConfig updateConfig(Long id, LoanCurrencyConfig config) {
        LoanCurrencyConfig existing = getConfigById(id);
        
        existing.setCurrencyName(config.getCurrencyName());
        existing.setInterestRate(config.getInterestRate());
        existing.setMinLoanAmount(config.getMinLoanAmount());
        existing.setMaxLoanAmount(config.getMaxLoanAmount());
        existing.setLoanTermOptions(config.getLoanTermOptions());
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
    public LoanCurrencyConfig updateStatus(Long id, String status) {
        LoanCurrencyConfig config = getConfigById(id);
        config.setStatus(status);
        return configRepository.save(config);
    }
}

