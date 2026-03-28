/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网格机器人参数调整历史Repository
 */
@Repository
public interface GridRobotAdjustmentHistoryRepository extends JpaRepository<GridRobotAdjustmentHistory, Long> {
    /**
     * 根据机器人ID查询调整历史
     */
    List<GridRobotAdjustmentHistory> findByRobotIdOrderByAdjustedAtDesc(Long robotId);

    /**
     * 根据调整类型查询
     */
    List<GridRobotAdjustmentHistory> findByAdjustType(String adjustType);
}













