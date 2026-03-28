/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRateHistory;
import com.cryptotrade.pledgeloan.repository.PledgeLoanRateHistoryRepository;
import com.cryptotrade.pledgeloan.service.PledgeLoanRateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币利率调整历史Service实现
 */
@Service
public class PledgeLoanRateHistoryServiceImpl implements PledgeLoanRateHistoryService {

    @Autowired
    private PledgeLoanRateHistoryRepository rateHistoryRepository;

    @Override
    @Transactional
    public PledgeLoanRateHistory recordRateChange(String configType, String currencyCode, 
                                                   String rateType, BigDecimal oldValue, BigDecimal newValue,
                                                   Long operatorId, String operatorName, String remark) {
        PledgeLoanRateHistory history = new PledgeLoanRateHistory();
        history.setConfigType(configType);
        history.setCurrencyCode(currencyCode);
        history.setRateType(rateType);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setOperatorId(operatorId);
        history.setOperatorName(operatorName);
        history.setRemark(remark);

        return rateHistoryRepository.save(history);
    }

    @Override
    public List<PledgeLoanRateHistory> getRateHistory(String currencyCode) {
        return rateHistoryRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public List<PledgeLoanRateHistory> getRateHistoryByConfigType(String configType) {
        return rateHistoryRepository.findByConfigType(configType);
    }
}

