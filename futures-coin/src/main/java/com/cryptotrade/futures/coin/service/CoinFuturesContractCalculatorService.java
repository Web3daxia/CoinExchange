/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.request.CoinFuturesCalculatorRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesCalculatorResponse;

/**
 * 币本位合约计算器服务接口
 */
public interface CoinFuturesContractCalculatorService {
    
    /**
     * 计算合约盈亏、平仓价格、强平价格
     * @param request 计算请求
     * @return 计算结果
     */
    CoinFuturesCalculatorResponse calculate(CoinFuturesCalculatorRequest request);
}














