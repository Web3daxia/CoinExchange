/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.entity.GridRobotSettlement;

import java.util.List;

/**
 * 网格机器人结算服务接口
 */
public interface GridRobotSettlementService {
    /**
     * 结算机器人
     */
    GridRobotSettlement settleRobot(Long userId, Long robotId);

    /**
     * 查询结算记录
     */
    GridRobotSettlement getSettlement(String settlementId);

    /**
     * 查询用户结算记录列表
     */
    List<GridRobotSettlement> getUserSettlements(Long userId, Long robotId);
}













