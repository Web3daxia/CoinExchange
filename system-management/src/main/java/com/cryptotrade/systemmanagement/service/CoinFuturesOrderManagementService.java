/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.CoinFuturesOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.CoinFuturesOrderManagement;
import org.springframework.data.domain.Page;

/**
 * 币本位永续合约委托管理Service接口
 */
public interface CoinFuturesOrderManagementService {
    
    /**
     * 搜索当前委托
     */
    Page<CoinFuturesOrderManagement> searchCurrentOrders(CoinFuturesOrderSearchRequest request);

    /**
     * 搜索历史委托
     */
    Page<CoinFuturesOrderManagement> searchHistoryOrders(CoinFuturesOrderSearchRequest request);

    /**
     * 根据ID获取委托详情
     */
    CoinFuturesOrderManagement getOrderById(Long id);

    /**
     * 强制撤销订单
     */
    void forceCancelOrder(Long orderId);
}














