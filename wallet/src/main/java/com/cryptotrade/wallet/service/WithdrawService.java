/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import com.cryptotrade.wallet.entity.WithdrawAddress;
import com.cryptotrade.wallet.entity.WithdrawLimit;
import com.cryptotrade.wallet.entity.WithdrawRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 提现服务接口
 */
public interface WithdrawService {
    /**
     * 提交提现请求
     * @param userId 用户ID
     * @param currency 币种
     * @param chain 链类型
     * @param address 提现地址
     * @param amount 提现金额
     * @param addressId 地址簿ID（可选）
     * @return 提现记录
     */
    WithdrawRecord submitWithdraw(Long userId, String currency, String chain,
                                  String address, BigDecimal amount, Long addressId);

    /**
     * 验证提现（邮箱/手机验证码、谷歌验证码）
     * @param userId 用户ID
     * @param withdrawId 提现ID
     * @param emailCode 邮箱验证码
     * @param phoneCode 手机验证码
     * @param googleCode 谷歌验证码
     */
    void verifyWithdraw(Long userId, Long withdrawId, String emailCode,
                       String phoneCode, String googleCode);

    /**
     * 查询提现状态
     * @param withdrawNo 提现单号
     * @return 提现记录
     */
    WithdrawRecord getWithdrawStatus(String withdrawNo);

    /**
     * 查询用户提现历史
     * @param userId 用户ID
     * @return 提现记录列表
     */
    List<WithdrawRecord> getWithdrawHistory(Long userId);

    /**
     * 查询提现限制和手续费
     * @param currency 币种
     * @param chain 链类型
     * @param amount 提现金额
     * @return 限制和手续费信息
     */
    Map<String, Object> getWithdrawLimit(String currency, String chain, BigDecimal amount);

    /**
     * 查询用户地址簿
     * @param userId 用户ID
     * @param currency 币种（可选）
     * @param chain 链类型（可选）
     * @return 地址列表
     */
    List<WithdrawAddress> getWithdrawAddresses(Long userId, String currency, String chain);

    /**
     * 添加提现地址到地址簿
     * @param userId 用户ID
     * @param currency 币种
     * @param chain 链类型
     * @param address 地址
     * @param addressLabel 地址标签
     * @return 地址记录
     */
    WithdrawAddress addWithdrawAddress(Long userId, String currency, String chain,
                                      String address, String addressLabel);
}















