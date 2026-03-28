/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.entity.CoinFuturesContract;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.repository.CoinFuturesContractRepository;
import com.cryptotrade.futures.coin.repository.CoinFuturesPositionRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesFundingRateService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinFuturesFundingRateServiceImpl implements CoinFuturesFundingRateService {

    @Autowired
    private CoinFuturesContractRepository coinFuturesContractRepository;

    @Autowired
    private CoinFuturesPositionRepository coinFuturesPositionRepository;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES";

    @Override
    public BigDecimal getFundingRate(String pairName) {
        Optional<CoinFuturesContract> contractOpt = coinFuturesContractRepository
                .findByPairNameAndContractType(pairName, "COIN_MARGINED");
        
        if (contractOpt.isPresent() && contractOpt.get().getFundingRate() != null) {
            return contractOpt.get().getFundingRate();
        }
        
        return BigDecimal.ZERO; // 默认资金费率为0
    }

    @Override
    public BigDecimal calculateFundingFee(BigDecimal positionValue, BigDecimal fundingRate) {
        if (positionValue == null || fundingRate == null) {
            return BigDecimal.ZERO;
        }
        
        // 资金费用 = 持仓价值 × 资金费率
        return positionValue.multiply(fundingRate);
    }

    @Override
    @Transactional
    public void settleFundingFee(String pairName) {
        // 获取资金费率
        BigDecimal fundingRate = getFundingRate(pairName);
        
        if (fundingRate.compareTo(BigDecimal.ZERO) == 0) {
            return; // 资金费率为0，无需结算
        }

        // 获取基础资产
        String baseCurrency = pairName.split("/")[0];

        // 查询该交易对的所有OPEN状态仓位
        List<CoinFuturesPosition> positions = coinFuturesPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()) && pairName.equals(p.getPairName()))
                .collect(Collectors.toList());

        for (CoinFuturesPosition position : positions) {
            try {
                // 计算持仓价值 = 持仓数量 × 标记价格（或开仓价格）
                BigDecimal markPrice = position.getMarkPrice() != null ? 
                        position.getMarkPrice() : position.getEntryPrice();
                BigDecimal positionValue = position.getQuantity().multiply(markPrice);

                // 计算资金费用
                BigDecimal fundingFee = calculateFundingFee(positionValue, fundingRate);

                // 根据仓位方向决定支付或收取资金费用
                // 多头：如果资金费率为正，支付费用；如果为负，收取费用
                // 空头：如果资金费率为正，收取费用；如果为负，支付费用
                BigDecimal actualFee;
                if ("LONG".equals(position.getPositionSide())) {
                    // 多头：支付资金费用（如果费率为正）
                    actualFee = fundingFee; // 正数表示支付，负数表示收取
                } else {
                    // 空头：收取资金费用（如果费率为正），即支付负费用
                    actualFee = fundingFee.negate(); // 负数表示收取，正数表示支付
                }

                // 从保证金中扣除资金费用（如果是支付）或增加（如果是收取）
                BigDecimal currentMargin = position.getMargin();
                BigDecimal newMargin = currentMargin.subtract(actualFee);
                
                if (newMargin.compareTo(BigDecimal.ZERO) < 0) {
                    // 如果保证金不足，从账户余额扣除（币本位使用基础资产）
                    BigDecimal shortage = newMargin.abs();
                    walletService.deductBalance(position.getUserId(), ACCOUNT_TYPE, baseCurrency, shortage);
                    newMargin = BigDecimal.ZERO;
                } else {
                    // 如果收取费用（actualFee为负），增加到账户余额
                    if (actualFee.compareTo(BigDecimal.ZERO) < 0) {
                        walletService.addBalance(position.getUserId(), ACCOUNT_TYPE, baseCurrency, actualFee.abs());
                    }
                }

                // 更新仓位
                position.setMargin(newMargin);
                position.setFundingFee(position.getFundingFee().add(actualFee));
                
                // 更新已实现盈亏（资金费用计入已实现盈亏）
                position.setRealizedPnl(position.getRealizedPnl().subtract(actualFee));

                coinFuturesPositionRepository.save(position);
            } catch (Exception e) {
                // 记录错误，继续处理下一个仓位
                System.err.println("结算资金费用失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void settleAllFundingFees() {
        // 查询所有币本位合约
        List<CoinFuturesContract> contracts = coinFuturesContractRepository
                .findByContractType("COIN_MARGINED");

        for (CoinFuturesContract contract : contracts) {
            try {
                settleFundingFee(contract.getPairName());
            } catch (Exception e) {
                // 记录错误，继续处理下一个合约
                System.err.println("结算资金费用失败: " + contract.getPairName() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void scheduleFundingSettlement() {
        // 定时结算所有交易对的资金费用（每8小时执行一次）
        settleAllFundingFees();
    }
}















