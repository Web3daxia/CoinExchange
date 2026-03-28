/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service.impl;

import com.cryptotrade.wallet.entity.Wallet;
import com.cryptotrade.wallet.entity.WalletTransfer;
import com.cryptotrade.wallet.repository.WalletRepository;
import com.cryptotrade.wallet.repository.WalletTransferRepository;
import com.cryptotrade.wallet.service.WalletService;
import com.cryptotrade.wallet.service.WalletTransferService;
import com.cryptotrade.basicinfo.service.ExchangeRateService;
import com.cryptotrade.user.service.VerificationService;
import com.cryptotrade.user.util.TOTPUtil;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 钱包资产划转服务实现类
 */
@Service
public class WalletTransferServiceImpl implements WalletTransferService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransferRepository walletTransferRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private TOTPUtil totpUtil;

    @Autowired
    private UserRepository userRepository;

    @Value("${wallet.transfer.fee-rate:0}")
    private BigDecimal transferFeeRate; // 划转手续费率

    // 支持的账户类型
    private static final List<String> SUPPORTED_ACCOUNT_TYPES = Arrays.asList(
            "SPOT", "LEVERAGE", "FUTURES_USDT", "FUTURES_COIN", "OPTIONS", "COPY_TRADING"
    );

    @Override
    public Map<String, Object> getAllAccountBalances(Long userId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Map<String, BigDecimal>> accountBalances = new HashMap<>();

        // 查询所有账户类型的余额
        for (String accountType : SUPPORTED_ACCOUNT_TYPES) {
            List<Wallet> wallets = walletRepository.findByUserIdAndAccountType(userId, accountType);
            Map<String, BigDecimal> balances = new HashMap<>();
            BigDecimal totalBalance = BigDecimal.ZERO;

            for (Wallet wallet : wallets) {
                BigDecimal available = wallet.getBalance().subtract(wallet.getFrozenBalance());
                balances.put(wallet.getCurrency(), available);
                
                // 转换为USD计算总资产
                BigDecimal usdValue = exchangeRateService.convertCurrency(available, wallet.getCurrency(), "USDT");
                totalBalance = totalBalance.add(usdValue);
            }

            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("balances", balances);
            accountInfo.put("totalBalance", totalBalance);
            accountBalances.put(accountType, balances);
        }

        result.put("accounts", accountBalances);
        return result;
    }

    @Override
    @Transactional
    public WalletTransfer transferBetweenAccounts(Long userId, String fromAccountType, String toAccountType,
                                                  String currency, BigDecimal amount, String remark) {
        // 验证账户类型
        if (!SUPPORTED_ACCOUNT_TYPES.contains(fromAccountType) || 
            !SUPPORTED_ACCOUNT_TYPES.contains(toAccountType)) {
            throw new RuntimeException("不支持的账户类型");
        }

        if (fromAccountType.equals(toAccountType)) {
            throw new RuntimeException("源账户和目标账户不能相同");
        }

        // 检查余额
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, fromAccountType, currency);
        if (availableBalance == null) {
            availableBalance = BigDecimal.ZERO;
        }
        if (availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 计算手续费
        BigDecimal fee = amount.multiply(transferFeeRate);
        BigDecimal transferAmount = amount.subtract(fee);

        // 生成划转单号
        String transferNo = "WT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建划转记录
        WalletTransfer transfer = new WalletTransfer();
        transfer.setTransferNo(transferNo);
        transfer.setUserId(userId);
        transfer.setFromAccountType(fromAccountType);
        transfer.setToAccountType(toAccountType);
        transfer.setCurrency(currency);
        transfer.setAmount(amount);
        transfer.setFee(fee);
        transfer.setStatus("PENDING");
        transfer.setRemark(remark);

        transfer = walletTransferRepository.save(transfer);

        try {
            // 扣除源账户余额
            walletService.deductBalance(userId, fromAccountType, currency, amount);

            // 添加目标账户余额
            walletService.addBalance(userId, toAccountType, currency, transferAmount);

            // 更新划转状态
            transfer.setStatus("COMPLETED");
            transfer.setCompletedAt(LocalDateTime.now());
            walletTransferRepository.save(transfer);
        } catch (Exception e) {
            transfer.setStatus("FAILED");
            walletTransferRepository.save(transfer);
            throw new RuntimeException("划转失败: " + e.getMessage());
        }

        return transfer;
    }

    @Override
    public List<WalletTransfer> getTransferHistory(Long userId) {
        return walletTransferRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void confirmTransfer(Long userId, Long transferId, String verificationCode) {
        WalletTransfer transfer = walletTransferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("划转记录不存在"));

        if (!transfer.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此划转记录");
        }

        if (!"PENDING".equals(transfer.getStatus())) {
            throw new RuntimeException("划转状态不允许确认");
        }

        // 验证验证码（2FA、短信、邮箱等）
        if (!verifyVerificationCode(userId, verificationCode)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 执行划转（如果还未执行）
        if ("PENDING".equals(transfer.getStatus())) {
            walletService.deductBalance(userId, transfer.getFromAccountType(), 
                    transfer.getCurrency(), transfer.getAmount());
            BigDecimal transferAmount = transfer.getAmount().subtract(transfer.getFee());
            walletService.addBalance(userId, transfer.getToAccountType(), 
                    transfer.getCurrency(), transferAmount);

            transfer.setStatus("COMPLETED");
            transfer.setCompletedAt(LocalDateTime.now());
            walletTransferRepository.save(transfer);
        }
    }

    /**
     * 验证验证码（2FA、短信、邮箱等）
     * 按优先级验证：2FA > 邮箱验证码 > 手机验证码
     * 
     * @param userId 用户ID
     * @param verificationCode 验证码
     * @return 验证是否通过
     */
    private boolean verifyVerificationCode(Long userId, String verificationCode) {
        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            throw new RuntimeException("验证码不能为空");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 优先级1: 如果用户启用了2FA，优先验证2FA
        if (Boolean.TRUE.equals(user.getTwoFaEnabled()) && user.getTwoFaSecret() != null) {
            if (totpUtil.verifyCode(user.getTwoFaSecret(), verificationCode)) {
                return true;
            }
            // 如果2FA验证失败，继续尝试其他验证方式（允许降级）
        }

        // 优先级2: 验证邮箱验证码
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (verificationService.verifyCode(user.getEmail(), verificationCode)) {
                return true;
            }
        }

        // 优先级3: 验证手机验证码
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (verificationService.verifyCode(user.getPhone(), verificationCode)) {
                return true;
            }
        }

        // 如果所有验证方式都失败，检查是否有可用的验证方式
        boolean hasAnyVerificationMethod = (Boolean.TRUE.equals(user.getTwoFaEnabled()) && user.getTwoFaSecret() != null)
                || (user.getEmail() != null && !user.getEmail().isEmpty())
                || (user.getPhone() != null && !user.getPhone().isEmpty());

        if (!hasAnyVerificationMethod) {
            throw new RuntimeException("用户未绑定任何验证方式，请联系客服");
        }

        return false;
    }
}

