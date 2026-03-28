/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service;

import com.cryptotrade.miningpool.dto.request.MiningPoolCreateRequest;
import com.cryptotrade.miningpool.entity.MiningPool;

import java.util.List;

/**
 * 矿池管理Service接口
 */
public interface MiningPoolService {
    
    /**
     * 创建矿池
     */
    MiningPool createPool(MiningPoolCreateRequest request);
    
    /**
     * 更新矿池
     */
    MiningPool updatePool(Long poolId, MiningPoolCreateRequest request);
    
    /**
     * 删除矿池
     */
    void deletePool(Long poolId);
    
    /**
     * 根据ID获取矿池
     */
    MiningPool getPoolById(Long poolId);
    
    /**
     * 根据矿池代码获取矿池
     */
    MiningPool getPoolByCode(String poolCode);
    
    /**
     * 获取所有矿池
     */
    List<MiningPool> getAllPools();
    
    /**
     * 获取活跃的矿池
     */
    List<MiningPool> getActivePools();
    
    /**
     * 根据币种获取矿池
     */
    List<MiningPool> getPoolsByCurrency(String currency);
    
    /**
     * 根据算法获取矿池
     */
    List<MiningPool> getPoolsByAlgorithm(String algorithm);
    
    /**
     * 更新矿池算力
     */
    void updatePoolHashrate(Long poolId, BigDecimal totalHashrate, BigDecimal activeHashrate);
    
    /**
     * 更新矿池收益
     */
    void updatePoolRevenue(Long poolId, BigDecimal revenue);
}














