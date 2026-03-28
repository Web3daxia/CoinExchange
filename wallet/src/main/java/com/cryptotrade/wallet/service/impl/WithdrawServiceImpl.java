/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service.impl;

import com.cryptotrade.wallet.entity.WithdrawAddress;
import com.cryptotrade.wallet.entity.WithdrawLimit;
import com.cryptotrade.wallet.entity.WithdrawRecord;
import com.cryptotrade.wallet.repository.WithdrawAddressRepository;
import com.cryptotrade.wallet.repository.WithdrawLimitRepository;
import com.cryptotrade.wallet.repository.WithdrawRecordRepository;
import com.cryptotrade.wallet.service.WalletService;
import com.cryptotrade.wallet.service.WithdrawService;
import com.cryptotrade.user.service.VerificationService;
import com.cryptotrade.user.util.TOTPUtil;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 提现服务实现类
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawRecordRepository withdrawRecordRepository;

    @Autowired
    private WithdrawLimitRepository withdrawLimitRepository;

    @Autowired
    private WithdrawAddressRepository withdrawAddressRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private TOTPUtil totpUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public WithdrawRecord submitWithdraw(Long userId, String currency, String chain,
                                        String address, BigDecimal amount, Long addressId) {
        // 查询提现限制
        WithdrawLimit limit = withdrawLimitRepository
                .findByCurrencyAndChainAndStatus(currency, chain, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("该币种和链暂不支持提现"));

        // 检查最小金额
        if (amount.compareTo(limit.getMinAmount()) < 0) {
            throw new RuntimeException("提现金额低于最小限额: " + limit.getMinAmount());
        }

        // 检查最大金额
        if (amount.compareTo(limit.getMaxAmount()) > 0) {
            throw new RuntimeException("提现金额超过单笔最大限额: " + limit.getMaxAmount());
        }

        // 检查每日限额
        BigDecimal dailyWithdrawn = getDailyWithdrawnAmount(userId, currency, chain);
        if (limit.getDailyMaxAmount() != null &&
            dailyWithdrawn.add(amount).compareTo(limit.getDailyMaxAmount()) > 0) {
            throw new RuntimeException("提现金额超过每日最大限额");
        }

        // 检查余额
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, "SPOT", currency);
        if (availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 计算手续费
        BigDecimal fee = calculateFee(amount, limit);
        BigDecimal actualAmount = amount.subtract(fee);

        // 生成提现单号
        String withdrawNo = "WD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建提现记录
        WithdrawRecord record = new WithdrawRecord();
        record.setWithdrawNo(withdrawNo);
        record.setUserId(userId);
        record.setCurrency(currency);
        record.setChain(chain);
        record.setWithdrawAddress(address);
        record.setAddressId(addressId);
        record.setAmount(amount);
        record.setFee(fee);
        record.setActualAmount(actualAmount);
        record.setStatus("PENDING");
        record.setIsVerified(false);

        // 冻结余额
        walletService.freezeBalance(userId, "SPOT", currency, amount);

        return withdrawRecordRepository.save(record);
    }

    @Override
    @Transactional
    public void verifyWithdraw(Long userId, Long withdrawId, String emailCode,
                              String phoneCode, String googleCode) {
        WithdrawRecord record = withdrawRecordRepository.findById(withdrawId)
                .orElseThrow(() -> new RuntimeException("提现记录不存在"));

        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此提现记录");
        }

        if (!"PENDING".equals(record.getStatus())) {
            throw new RuntimeException("提现状态不允许验证");
        }

        // 验证邮箱验证码、手机验证码、谷歌验证码
        verifyWithdrawCodes(userId, emailCode, phoneCode, googleCode);

        // 保存验证码
        record.setEmailVerificationCode(emailCode);
        record.setPhoneVerificationCode(phoneCode);
        record.setGoogleVerificationCode(googleCode);
        record.setIsVerified(true);
        record.setStatus("PROCESSING"); // 等待后台审核

        withdrawRecordRepository.save(record);
    }

    @Override
    public WithdrawRecord getWithdrawStatus(String withdrawNo) {
        return withdrawRecordRepository.findByWithdrawNo(withdrawNo)
                .orElseThrow(() -> new RuntimeException("提现记录不存在"));
    }

    @Override
    public List<WithdrawRecord> getWithdrawHistory(Long userId) {
        return withdrawRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Map<String, Object> getWithdrawLimit(String currency, String chain, BigDecimal amount) {
        WithdrawLimit limit = withdrawLimitRepository
                .findByCurrencyAndChainAndStatus(currency, chain, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("该币种和链暂不支持提现"));

        // 计算手续费
        BigDecimal fee = calculateFee(amount, limit);
        BigDecimal actualAmount = amount.subtract(fee);

        Map<String, Object> result = new HashMap<>();
        result.put("currency", currency);
        result.put("chain", chain);
        result.put("minAmount", limit.getMinAmount());
        result.put("maxAmount", limit.getMaxAmount());
        result.put("dailyMaxAmount", limit.getDailyMaxAmount());
        result.put("fee", fee);
        result.put("feeRate", limit.getFeeRate());
        result.put("fixedFee", limit.getFixedFee());
        result.put("amount", amount);
        result.put("actualAmount", actualAmount);

        return result;
    }

    @Override
    public List<WithdrawAddress> getWithdrawAddresses(Long userId, String currency, String chain) {
        if (currency != null && chain != null) {
            return withdrawAddressRepository.findByUserIdAndCurrencyAndChainAndStatus(
                    userId, currency, chain, "ACTIVE");
        }
        return withdrawAddressRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, "ACTIVE");
    }

    @Override
    @Transactional
    public WithdrawAddress addWithdrawAddress(Long userId, String currency, String chain,
                                             String address, String addressLabel) {
        WithdrawAddress withdrawAddress = new WithdrawAddress();
        withdrawAddress.setUserId(userId);
        withdrawAddress.setCurrency(currency);
        withdrawAddress.setChain(chain);
        withdrawAddress.setAddress(address);
        withdrawAddress.setAddressLabel(addressLabel);
        withdrawAddress.setIsVerified(false);
        withdrawAddress.setStatus("ACTIVE");

        return withdrawAddressRepository.save(withdrawAddress);
    }

    /**
     * 计算手续费
     */
    private BigDecimal calculateFee(BigDecimal amount, WithdrawLimit limit) {
        BigDecimal fee = BigDecimal.ZERO;
        
        // 固定手续费
        if (limit.getFixedFee() != null) {
            fee = fee.add(limit.getFixedFee());
        }
        
        // 手续费率
        if (limit.getFeeRate() != null) {
            fee = fee.add(amount.multiply(limit.getFeeRate()));
        }

        return fee;
    }

    /**
     * 获取当日已提现金额
     */
    private BigDecimal getDailyWithdrawnAmount(Long userId, String currency, String chain) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<WithdrawRecord> records = withdrawRecordRepository.findByUserIdAndCreatedAtBetween(
                userId, startOfDay, endOfDay);

        return records.stream()
                .filter(r -> currency.equals(r.getCurrency()) && chain.equals(r.getChain()))
                .filter(r -> "COMPLETED".equals(r.getStatus()) || "PROCESSING".equals(r.getStatus()))
                .map(WithdrawRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 验证提现验证码（邮箱验证码、手机验证码、谷歌验证码）
     * 根据用户绑定的验证方式，验证相应的验证码
     * 
     * @param userId 用户ID
     * @param emailCode 邮箱验证码
     * @param phoneCode 手机验证码
     * @param googleCode 谷歌验证码（2FA）
     */
    private void verifyWithdrawCodes(Long userId, String emailCode, String phoneCode, String googleCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        boolean hasEmail = user.getEmail() != null && !user.getEmail().isEmpty();
        boolean hasPhone = user.getPhone() != null && !user.getPhone().isEmpty();
        boolean has2FA = Boolean.TRUE.equals(user.getTwoFaEnabled()) && user.getTwoFaSecret() != null;

        // 检查用户是否至少绑定了一种验证方式
        if (!hasEmail && !hasPhone && !has2FA) {
            throw new RuntimeException("用户未绑定任何验证方式，无法进行提现验证，请联系客服");
        }

        // 验证邮箱验证码
        if (hasEmail) {
            if (emailCode == null || emailCode.trim().isEmpty()) {
                throw new RuntimeException("已绑定邮箱，必须提供邮箱验证码");
            }
            if (!verificationService.verifyCode(user.getEmail(), emailCode)) {
                throw new RuntimeException("邮箱验证码错误或已过期");
            }
        }

        // 验证手机验证码
        if (hasPhone) {
            if (phoneCode == null || phoneCode.trim().isEmpty()) {
                throw new RuntimeException("已绑定手机号，必须提供手机验证码");
            }
            if (!verificationService.verifyCode(user.getPhone(), phoneCode)) {
                throw new RuntimeException("手机验证码错误或已过期");
            }
        }

        // 验证谷歌验证码（2FA）
        if (has2FA) {
            if (googleCode == null || googleCode.trim().isEmpty()) {
                throw new RuntimeException("已启用2FA，必须提供谷歌验证码");
            }
            if (!totpUtil.verifyCode(user.getTwoFaSecret(), googleCode)) {
                throw new RuntimeException("谷歌验证码错误");
            }
        }

        // 至少需要一种验证方式通过（上面的逻辑已经确保所有绑定的验证方式都通过）
        // 如果用户只绑定了一种验证方式，只需要提供该验证方式的验证码
        // 如果用户绑定了多种验证方式，需要提供所有已绑定验证方式的验证码
    }
}














