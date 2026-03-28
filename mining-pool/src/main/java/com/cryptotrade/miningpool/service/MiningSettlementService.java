/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service;

import com.cryptotrade.miningpool.entity.MiningSettlement;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 挖矿收益结算Service接口
 */
public interface MiningSettlementService {
    
    /**
     * 结算矿池收益（按分配方式分配收益给所有矿工）
     */
    List<MiningSettlement> settlePoolRevenue(Long poolId, java.math.BigDecimal poolRevenue, String currency);
    
    /**
     * 批量结算矿池收益（按结算周期）
     */
    List<MiningSettlement> batchSettlePoolRevenue(String settlementCycle);
    
    /**
     * 获取矿工的结算记录
     */
    List<MiningSettlement> getWorkerSettlements(Long workerId);
    
    /**
     * 获取用户的分页结算记录
     */
    Page<MiningSettlement> getUserSettlements(Long userId, Integer page, Integer size);
    
    /**
     * 获取矿池的结算记录
     */
    List<MiningSettlement> getPoolSettlements(Long poolId);
    
    /**
     * 获取待结算的记录
     */
    List<MiningSettlement> getPendingSettlements();
    
    /**
     * 计算用户应得收益（根据分配方式）
     */
    java.math.BigDecimal calculateUserRevenue(Long workerId, java.math.BigDecimal poolRevenue);
}














