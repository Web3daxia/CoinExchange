/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;

import java.util.List;

/**
 * 网格机器人参数调整服务接口
 */
public interface GridRobotAdjustmentService {
    /**
     * 创建参数调整记录
     */
    GridRobotAdjustmentHistory createAdjustment(Long robotId, String adjustType,
                                                 GridRobotAdjustmentHistory adjustment);

    /**
     * 查询参数调整历史
     */
    List<GridRobotAdjustmentHistory> getAdjustmentHistory(Long robotId);
}













