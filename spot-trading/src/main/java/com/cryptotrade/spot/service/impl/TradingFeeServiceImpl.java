/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.service.TradingFeeService;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TradingFeeServiceImpl implements TradingFeeService {

    @Autowired
    private UserRepository userRepository;

    @Value("${trading.fee.default-rate:0.001}")
    private BigDecimal defaultFeeRate; // 默认费率 0.1%

    @Value("${trading.fee.platform-token-discount:0.5}")
    private BigDecimal platformTokenDiscount; // 平台代币折扣 50%

    @Override
    public BigDecimal calculateFee(Long userId, String pairName, BigDecimal quantity, 
                                   BigDecimal price, String side) {
        BigDecimal feeRate = getFeeRate(userId);
        BigDecimal tradeAmount = quantity.multiply(price);
        return tradeAmount.multiply(feeRate);
    }

    @Override
    public BigDecimal getFeeRate(Long userId) {
        // TODO: 根据用户VIP等级获取费率
        // 这里先返回默认费率，后续可以根据用户等级调整
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 可以根据用户的VIP等级或交易量返回不同的费率
            // 例如：VIP0: 0.1%, VIP1: 0.08%, VIP2: 0.05%等
        }
        
        return defaultFeeRate;
    }

    @Override
    public BigDecimal calculateFeeWithDiscount(Long userId, BigDecimal fee, boolean usePlatformToken) {
        if (!usePlatformToken) {
            return fee;
        }
        
        // 使用平台代币支付，享受折扣
        return fee.multiply(platformTokenDiscount);
    }
}


