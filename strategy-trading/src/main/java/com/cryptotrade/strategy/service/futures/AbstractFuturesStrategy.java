/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.futures;

import com.cryptotrade.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 合约策略抽象基类
 */
public abstract class AbstractFuturesStrategy implements StrategyService {
    
    @Override
    public abstract void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception;

    /**
     * 解析策略参数中的BigDecimal值
     */
    protected BigDecimal getBigDecimalParam(Map<String, Object> params, String key, BigDecimal defaultValue) {
        if (params.containsKey(key) && params.get(key) != null) {
            return new BigDecimal(params.get(key).toString());
        }
        return defaultValue;
    }

    /**
     * 解析策略参数中的Integer值
     */
    protected Integer getIntegerParam(Map<String, Object> params, String key, Integer defaultValue) {
        if (params.containsKey(key) && params.get(key) != null) {
            return Integer.parseInt(params.get(key).toString());
        }
        return defaultValue;
    }

    /**
     * 解析策略参数中的String值
     */
    protected String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        if (params.containsKey(key) && params.get(key) != null) {
            return params.get(key).toString();
        }
        return defaultValue;
    }

    /**
     * 计算订单数量
     */
    protected BigDecimal calculateQuantity(BigDecimal orderAmount, BigDecimal price) {
        if (orderAmount == null || price == null) {
            return BigDecimal.ZERO;
        }
        return orderAmount.divide(price, 8, RoundingMode.DOWN);
    }
}

