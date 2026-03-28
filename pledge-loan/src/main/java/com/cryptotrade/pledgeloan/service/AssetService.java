/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import java.math.BigDecimal;

/**
 * 资产服务接口
 * 用于冻结、解冻、转账等资产操作
 */
public interface AssetService {
    
    /**
     * 冻结资产
     * @param userId 用户ID
     * @param currency 币种
     * @param amount 数量
     * @param orderNo 关联订单号
     * @return 是否成功
     */
    boolean freezeAsset(Long userId, String currency, BigDecimal amount, String orderNo);
    
    /**
     * 解冻资产
     * @param userId 用户ID
     * @param currency 币种
     * @param amount 数量
     * @param orderNo 关联订单号
     * @return 是否成功
     */
    boolean unfreezeAsset(Long userId, String currency, BigDecimal amount, String orderNo);
    
    /**
     * 转账（扣除资产）
     * @param userId 用户ID
     * @param currency 币种
     * @param amount 数量
     * @param orderNo 关联订单号
     * @return 是否成功
     */
    boolean deductAsset(Long userId, String currency, BigDecimal amount, String orderNo);
    
    /**
     * 增加资产
     * @param userId 用户ID
     * @param currency 币种
     * @param amount 数量
     * @param orderNo 关联订单号
     * @return 是否成功
     */
    boolean addAsset(Long userId, String currency, BigDecimal amount, String orderNo);
    
    /**
     * 检查资产余额
     * @param userId 用户ID
     * @param currency 币种
     * @return 可用余额
     */
    BigDecimal getAvailableBalance(Long userId, String currency);
}














