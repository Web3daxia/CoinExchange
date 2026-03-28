/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.UsdtFuturesOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.UsdtFuturesOrderManagement;
import org.springframework.data.domain.Page;

/**
 * U本位永续合约委托管理Service接口
 */
public interface UsdtFuturesOrderManagementService {
    
    /**
     * 搜索当前委托
     */
    Page<UsdtFuturesOrderManagement> searchCurrentOrders(UsdtFuturesOrderSearchRequest request);

    /**
     * 搜索历史委托
     */
    Page<UsdtFuturesOrderManagement> searchHistoryOrders(UsdtFuturesOrderSearchRequest request);

    /**
     * 根据ID获取委托详情
     */
    UsdtFuturesOrderManagement getOrderById(Long id);

    /**
     * 强制撤销订单
     */
    void forceCancelOrder(Long orderId);
}














