/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service;

import com.cryptotrade.miningpool.dto.request.HashrateRentalRequest;
import com.cryptotrade.miningpool.entity.HashrateRental;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 算力租赁Service接口
 */
public interface HashrateRentalService {
    
    /**
     * 用户租赁算力
     */
    HashrateRental rentHashrate(Long userId, HashrateRentalRequest request);
    
    /**
     * 根据ID获取租赁记录
     */
    HashrateRental getRentalById(Long rentalId);
    
    /**
     * 根据订单号获取租赁记录
     */
    HashrateRental getRentalByOrderNo(String orderNo);
    
    /**
     * 获取用户的所有租赁记录
     */
    List<HashrateRental> getUserRentals(Long userId);
    
    /**
     * 获取用户的分页租赁记录
     */
    Page<HashrateRental> getUserRentals(Long userId, Integer page, Integer size);
    
    /**
     * 根据矿池ID获取租赁记录
     */
    List<HashrateRental> getPoolRentals(Long poolId);
    
    /**
     * 获取用户的活跃租赁记录
     */
    List<HashrateRental> getActiveRentals(Long userId);
    
    /**
     * 计算租赁费用
     */
    java.math.BigDecimal calculateRentalCost(Long poolId, java.math.BigDecimal hashrateAmount, Integer rentalPeriod);
}














