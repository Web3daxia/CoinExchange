/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;
import com.cryptotrade.robot.repository.GridRobotAdjustmentHistoryRepository;
import com.cryptotrade.robot.service.GridRobotAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 网格机器人参数调整服务实现类
 */
@Service
public class GridRobotAdjustmentServiceImpl implements GridRobotAdjustmentService {

    @Autowired
    private GridRobotAdjustmentHistoryRepository adjustmentHistoryRepository;

    @Override
    @Transactional
    public GridRobotAdjustmentHistory createAdjustment(Long robotId, String adjustType,
                                                       GridRobotAdjustmentHistory adjustment) {
        adjustment.setRobotId(robotId);
        adjustment.setAdjustType(adjustType);
        return adjustmentHistoryRepository.save(adjustment);
    }

    @Override
    public List<GridRobotAdjustmentHistory> getAdjustmentHistory(Long robotId) {
        return adjustmentHistoryRepository.findByRobotIdOrderByAdjustedAtDesc(robotId);
    }
}













