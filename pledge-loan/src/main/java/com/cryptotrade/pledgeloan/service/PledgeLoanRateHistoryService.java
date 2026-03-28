/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRateHistory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币利率调整历史Service接口
 */
public interface PledgeLoanRateHistoryService {
    
    /**
     * 记录利率调整历史
     */
    PledgeLoanRateHistory recordRateChange(String configType, String currencyCode, 
                                           String rateType, BigDecimal oldValue, BigDecimal newValue,
                                           Long operatorId, String operatorName, String remark);
    
    /**
     * 获取币种的利率调整历史
     */
    List<PledgeLoanRateHistory> getRateHistory(String currencyCode);
    
    /**
     * 获取配置类型的利率调整历史
     */
    List<PledgeLoanRateHistory> getRateHistoryByConfigType(String configType);
}














