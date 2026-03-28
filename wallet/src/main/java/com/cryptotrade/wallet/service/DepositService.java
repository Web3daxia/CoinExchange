/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import com.cryptotrade.wallet.entity.DepositAddress;
import com.cryptotrade.wallet.entity.DepositRecord;

import java.util.List;
import java.util.Map;

/**
 * 充值服务接口
 */
public interface DepositService {
    /**
     * 查询所有支持的充值地址和二维码
     * @param currency 币种（可选）
     * @return 充值地址列表
     */
    List<Map<String, Object>> getDepositAddresses(String currency);

    /**
     * 获取第三方充值二维码（如优盾钱包）
     * @param currency 币种
     * @param chain 链类型
     * @param amount 充值金额（可选）
     * @return 第三方充值信息
     */
    Map<String, Object> getThirdPartyDeposit(String currency, String chain, java.math.BigDecimal amount);

    /**
     * 查询用户充值历史
     * @param userId 用户ID
     * @return 充值记录列表
     */
    List<DepositRecord> getDepositHistory(Long userId);

    /**
     * 查询充值记录详情
     * @param depositNo 充值单号
     * @return 充值记录
     */
    DepositRecord getDepositRecord(String depositNo);

    /**
     * 处理充值到账（定时任务或回调）
     * @param transactionHash 交易哈希
     * @param amount 充值金额
     * @param address 充值地址
     */
    void processDeposit(String transactionHash, java.math.BigDecimal amount, String address);
}















