/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.GridRobotSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 网格机器人结算记录Repository
 */
@Repository
public interface GridRobotSettlementRepository extends JpaRepository<GridRobotSettlement, Long> {
    /**
     * 根据结算ID查询
     */
    Optional<GridRobotSettlement> findBySettlementId(String settlementId);

    /**
     * 根据机器人ID查询结算记录
     */
    List<GridRobotSettlement> findByRobotId(Long robotId);

    /**
     * 根据用户ID查询结算记录
     */
    List<GridRobotSettlement> findByUserId(Long userId);

    /**
     * 根据状态查询结算记录
     */
    List<GridRobotSettlement> findByStatus(String status);
}













