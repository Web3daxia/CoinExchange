/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedPosition;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 杠杆仓位管理服务接口
 */
public interface LeveragedPositionService {
    /**
     * 查询用户当前仓位信息
     * @param userId 用户ID
     * @return 仓位列表
     */
    List<LeveragedPosition> getUserPositions(Long userId);

    /**
     * 查询账户风险信息
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @return 风险信息
     */
    Map<String, Object> getAccountRisk(Long userId, String pairName);

    /**
     * 补充保证金操作
     * @param userId 用户ID
     * @param positionId 仓位ID
     * @param amount 补充金额
     * @return 仓位
     */
    LeveragedPosition topUpMargin(Long userId, Long positionId, BigDecimal amount);

    /**
     * 强制平仓操作
     * @param userId 用户ID
     * @param positionId 仓位ID
     * @return 是否成功
     */
    boolean liquidatePosition(Long userId, Long positionId);

    /**
     * 更新仓位盈亏
     * @param positionId 仓位ID
     */
    void updatePositionPnl(Long positionId);

    /**
     * 检查并更新所有仓位的盈亏
     */
    void updateAllPositionsPnl();

    /**
     * 计算强平价格
     * @param position 仓位
     * @return 强平价格
     */
    BigDecimal calculateLiquidationPrice(LeveragedPosition position);

    /**
     * 计算保证金率
     * @param position 仓位
     * @return 保证金率
     */
    BigDecimal calculateMarginRatio(LeveragedPosition position);
}















