/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.dto.request.ContractCalculatorRequest;
import com.cryptotrade.futures.usdt.dto.response.ContractCalculatorResponse;

/**
 * USDT本位合约计算器服务接口
 */
public interface ContractCalculatorService {
    
    /**
     * 计算合约盈亏、平仓价格、强平价格
     * @param request 计算请求
     * @return 计算结果
     */
    ContractCalculatorResponse calculate(ContractCalculatorRequest request);
}














