/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service.impl;

import com.cryptotrade.wallet.entity.Wallet;
import com.cryptotrade.wallet.entity.WalletBalanceSnapshot;
import com.cryptotrade.wallet.repository.WalletBalanceSnapshotRepository;
import com.cryptotrade.wallet.repository.WalletRepository;
import com.cryptotrade.wallet.service.WalletAssetSummaryService;
import com.cryptotrade.basicinfo.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 钱包资产汇总服务实现类
 */
@Service
public class WalletAssetSummaryServiceImpl implements WalletAssetSummaryService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletBalanceSnapshotRepository walletBalanceSnapshotRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> getAssetSummary(Long userId, String period, String currencyType) {
        Map<String, Object> result = new HashMap<>();

        // 计算时间范围
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = calculateStartTime(period, endTime);

        // 获取快照数据
        List<WalletBalanceSnapshot> snapshots = walletBalanceSnapshotRepository
                .findByUserIdAndSnapshotTimeBetweenOrderBySnapshotTimeAsc(userId, startTime, endTime);

        // 如果没有快照数据，使用实时数据
        if (snapshots.isEmpty() || "REAL_TIME".equals(period)) {
            return getRealTimeSummary(userId, currencyType);
        }

        // 构建图表数据
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (WalletBalanceSnapshot snapshot : snapshots) {
            Map<String, Object> point = new HashMap<>();
            point.put("time", snapshot.getSnapshotTime());
            point.put("totalBalance", snapshot.getTotalBalanceUsd());
            point.put("spotBalance", snapshot.getSpotBalanceUsd());
            point.put("leverageBalance", snapshot.getLeverageBalanceUsd());
            point.put("futuresUsdtBalance", snapshot.getFuturesUsdtBalanceUsd());
            point.put("futuresCoinBalance", snapshot.getFuturesCoinBalanceUsd());
            point.put("optionsBalance", snapshot.getOptionsBalanceUsd());
            point.put("copyTradingBalance", snapshot.getCopyTradingBalanceUsd());
            chartData.add(point);
        }

        // 获取当前总资产
        Map<String, Object> currentSummary = getRealTimeSummary(userId, currencyType);

        result.put("chartData", chartData);
        result.put("currentSummary", currentSummary);
        result.put("period", period);
        result.put("currencyType", currencyType);

        return result;
    }

    @Override
    @Transactional
    public void createBalanceSnapshot(Long userId) {
        // 获取所有账户余额
        List<Wallet> wallets = walletRepository.findByUserId(userId);

        // 按账户类型分组
        Map<String, Map<String, BigDecimal>> accountBalances = new HashMap<>();
        for (Wallet wallet : wallets) {
            accountBalances.computeIfAbsent(wallet.getAccountType(), k -> new HashMap<>())
                    .put(wallet.getCurrency(), wallet.getBalance());
        }

        // 计算各账户类型的总资产（USD）
        BigDecimal spotBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("SPOT", new HashMap<>()));
        BigDecimal leverageBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("LEVERAGE", new HashMap<>()));
        BigDecimal futuresUsdtBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("FUTURES_USDT", new HashMap<>()));
        BigDecimal futuresCoinBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("FUTURES_COIN", new HashMap<>()));
        BigDecimal optionsBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("OPTIONS", new HashMap<>()));
        BigDecimal copyTradingBalanceUsd = calculateAccountBalance(accountBalances.getOrDefault("COPY_TRADING", new HashMap<>()));

        BigDecimal totalBalanceUsd = spotBalanceUsd.add(leverageBalanceUsd)
                .add(futuresUsdtBalanceUsd).add(futuresCoinBalanceUsd)
                .add(optionsBalanceUsd).add(copyTradingBalanceUsd);

        // 创建快照
        WalletBalanceSnapshot snapshot = new WalletBalanceSnapshot();
        snapshot.setUserId(userId);
        snapshot.setSnapshotTime(LocalDateTime.now());
        snapshot.setTotalBalanceUsd(totalBalanceUsd);
        snapshot.setSpotBalanceUsd(spotBalanceUsd);
        snapshot.setLeverageBalanceUsd(leverageBalanceUsd);
        snapshot.setFuturesUsdtBalanceUsd(futuresUsdtBalanceUsd);
        snapshot.setFuturesCoinBalanceUsd(futuresCoinBalanceUsd);
        snapshot.setOptionsBalanceUsd(optionsBalanceUsd);
        snapshot.setCopyTradingBalanceUsd(copyTradingBalanceUsd);

        try {
            snapshot.setSnapshotData(objectMapper.writeValueAsString(accountBalances));
        } catch (Exception e) {
            snapshot.setSnapshotData("{}");
        }

        walletBalanceSnapshotRepository.save(snapshot);
    }

    /**
     * 获取实时汇总数据
     */
    private Map<String, Object> getRealTimeSummary(Long userId, String currencyType) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        Map<String, Object> summary = new HashMap<>();

        Map<String, BigDecimal> accountTotals = new HashMap<>();
        BigDecimal totalBalance = BigDecimal.ZERO;
        
        for (Wallet wallet : wallets) {
            BigDecimal balance = wallet.getBalance();
            
            // 根据currencyType转换为对应货币
            BigDecimal convertedBalance;
            if (currencyType != null && !currencyType.isEmpty() && !"USDT".equals(currencyType)) {
                // 先转换为USDT，再转换为目标货币
                BigDecimal usdValue = exchangeRateService.convertCurrency(balance, wallet.getCurrency(), "USDT");
                convertedBalance = exchangeRateService.convertCurrency(usdValue, "USDT", currencyType);
            } else {
                // 默认转换为USDT
                if ("USDT".equals(wallet.getCurrency())) {
                    convertedBalance = balance;
                } else {
                    convertedBalance = exchangeRateService.convertCurrency(balance, wallet.getCurrency(), "USDT");
                }
            }
            
            accountTotals.merge(wallet.getAccountType(), convertedBalance, BigDecimal::add);
            totalBalance = totalBalance.add(convertedBalance);
        }

        summary.put("accounts", accountTotals);
        summary.put("totalBalance", totalBalance);

        return summary;
    }

    /**
     * 计算账户余额（转换为USD）
     */
    private BigDecimal calculateAccountBalance(Map<String, BigDecimal> balances) {
        BigDecimal totalUsd = BigDecimal.ZERO;
        
        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            String currency = entry.getKey();
            BigDecimal amount = entry.getValue();
            
            // 如果已经是USDT，直接累加
            if ("USDT".equals(currency)) {
                totalUsd = totalUsd.add(amount);
            } else {
                // 转换为USDT（USD等值）
                BigDecimal usdValue = exchangeRateService.convertCurrency(amount, currency, "USDT");
                totalUsd = totalUsd.add(usdValue);
            }
        }
        
        return totalUsd;
    }

    /**
     * 计算开始时间
     */
    private LocalDateTime calculateStartTime(String period, LocalDateTime endTime) {
        switch (period) {
            case "TODAY":
                return endTime.toLocalDate().atStartOfDay();
            case "TWO_DAYS":
                return endTime.minus(2, ChronoUnit.DAYS);
            case "THREE_DAYS":
                return endTime.minus(3, ChronoUnit.DAYS);
            case "WEEK":
                return endTime.minus(7, ChronoUnit.DAYS);
            case "MONTH":
                return endTime.minus(30, ChronoUnit.DAYS);
            case "THREE_MONTHS":
                return endTime.minus(90, ChronoUnit.DAYS);
            case "HALF_YEAR":
                return endTime.minus(180, ChronoUnit.DAYS);
            case "YEAR":
                return endTime.minus(365, ChronoUnit.DAYS);
            default:
                return endTime.minus(1, ChronoUnit.HOURS);
        }
    }
}














