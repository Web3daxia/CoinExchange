/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.SpotOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.SpotOrderManagement;
import org.springframework.data.domain.Page;

/**
 * 现货订单管理Service接口
 */
public interface SpotOrderManagementService {
    
    /**
     * 搜索现货订单（当前委托）
     */
    Page<SpotOrderManagement> searchCurrentOrders(SpotOrderSearchRequest request);

    /**
     * 搜索现货订单（历史委托）
     */
    Page<SpotOrderManagement> searchHistoryOrders(SpotOrderSearchRequest request);

    /**
     * 根据订单ID获取订单详情
     */
    SpotOrderManagement getOrderById(Long id);

    /**
     * 撤销订单
     */
    void cancelOrder(Long orderId);

    /**
     * 查看订单详情
     */
    SpotOrderManagement getOrderDetail(Long id);
}














