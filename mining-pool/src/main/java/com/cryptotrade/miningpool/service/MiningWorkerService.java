/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service;

import com.cryptotrade.miningpool.entity.MiningWorker;

import java.util.List;

/**
 * 矿工管理Service接口
 */
public interface MiningWorkerService {
    
    /**
     * 用户加入矿池（分配算力到矿池）
     */
    MiningWorker joinPool(Long userId, Long poolId, Long rentalId, java.math.BigDecimal hashrateAmount, String unit);
    
    /**
     * 用户退出矿池
     */
    void exitPool(Long userId, Long poolId);
    
    /**
     * 根据ID获取矿工记录
     */
    MiningWorker getWorkerById(Long workerId);
    
    /**
     * 获取用户在指定矿池的矿工记录
     */
    MiningWorker getWorkerByUserAndPool(Long userId, Long poolId);
    
    /**
     * 获取用户的所有矿工记录
     */
    List<MiningWorker> getUserWorkers(Long userId);
    
    /**
     * 获取矿池的所有矿工记录
     */
    List<MiningWorker> getPoolWorkers(Long poolId);
    
    /**
     * 获取矿池的活跃矿工记录
     */
    List<MiningWorker> getActivePoolWorkers(Long poolId);
    
    /**
     * 获取矿池的矿工排名（按算力贡献）
     */
    List<MiningWorker> getPoolWorkerRanking(Long poolId, Integer limit);
    
    /**
     * 更新矿工算力贡献
     */
    void updateWorkerHashrate(Long workerId, java.math.BigDecimal hashrateContribution);
}














