/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import com.cryptotrade.options.entity.OptionExercise;
import com.cryptotrade.options.entity.OptionPosition;

import java.util.List;

/**
 * 期权行使服务接口
 */
public interface OptionExerciseService {
    /**
     * 手动行使期权
     * @param userId 用户ID
     * @param positionId 持仓ID
     * @param quantity 行使数量
     * @return 行使记录
     */
    OptionExercise exerciseOption(Long userId, Long positionId, java.math.BigDecimal quantity);

    /**
     * 自动行使期权（到期时如果处于实值状态）
     * @param positionId 持仓ID
     * @return 行使记录
     */
    OptionExercise autoExerciseOption(Long positionId);

    /**
     * 查询期权行使状态
     * @param userId 用户ID
     * @param positionId 持仓ID
     * @return 行使记录列表
     */
    List<OptionExercise> getExerciseStatus(Long userId, Long positionId);

    /**
     * 检查并执行自动行使（到期检查）
     */
    void checkAndAutoExerciseExpiredOptions();

    /**
     * 判断期权是否处于实值状态（in-the-money）
     * @param position 持仓
     * @return 是否实值
     */
    boolean isInTheMoney(OptionPosition position);
}















