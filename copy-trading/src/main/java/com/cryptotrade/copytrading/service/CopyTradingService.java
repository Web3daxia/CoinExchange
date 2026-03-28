/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service;

import com.cryptotrade.copytrading.entity.CopyTradingRelation;
import com.cryptotrade.copytrading.entity.CopyOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 跟单服务接口
 */
public interface CopyTradingService {
    /**
     * 跟随带单员（公域或私域）
     * @param followerId 跟单员ID
     * @param traderId 带单员ID
     * @param marketType 市场类型
     * @param copyType 跟单类型（PUBLIC/PRIVATE）
     * @param allocationAmount 分配金额
     * @param copyRatio 跟单比例
     * @param settings 跟单设置（止损、止盈、杠杆等）
     * @return 跟单关系
     */
    CopyTradingRelation followTrader(Long followerId, Long traderId, String marketType, String copyType,
                                    BigDecimal allocationAmount, BigDecimal copyRatio, Map<String, Object> settings);

    /**
     * 停止跟单
     * @param followerId 跟单员ID
     * @param relationId 跟单关系ID
     */
    void stopCopyTrading(Long followerId, Long relationId);

    /**
     * 暂停跟单
     * @param followerId 跟单员ID
     * @param relationId 跟单关系ID
     */
    void pauseCopyTrading(Long followerId, Long relationId);

    /**
     * 恢复跟单
     * @param followerId 跟单员ID
     * @param relationId 跟单关系ID
     */
    void resumeCopyTrading(Long followerId, Long relationId);

    /**
     * 更新跟单设置
     * @param followerId 跟单员ID
     * @param relationId 跟单关系ID
     * @param settings 设置参数
     * @return 跟单关系
     */
    CopyTradingRelation updateCopyTradingSettings(Long followerId, Long relationId, Map<String, Object> settings);

    /**
     * 查询跟单状态
     * @param followerId 跟单员ID
     * @return 跟单关系列表
     */
    List<CopyTradingRelation> getCopyTradingStatus(Long followerId);

    /**
     * 复制带单员的订单
     * @param traderId 带单员ID
     * @param traderOrderId 带单员订单ID
     * @param marketType 市场类型
     */
    void copyTraderOrder(Long traderId, Long traderOrderId, String marketType);

    /**
     * 处理带单员的订单（当带单员下单时调用）
     * @param traderId 带单员ID
     * @param orderId 订单ID
     * @param marketType 市场类型
     * @param pairName 交易对
     * @param side 方向
     * @param action 操作
     * @param quantity 数量
     * @param price 价格
     */
    void handleTraderOrder(Long traderId, Long orderId, String marketType, String pairName,
                          String side, String action, BigDecimal quantity, BigDecimal price);
}















