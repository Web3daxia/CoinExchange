/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.CoinFuturesPositionSearchRequest;
import com.cryptotrade.systemmanagement.entity.CoinFuturesPositionManagement;
import org.springframework.data.domain.Page;

/**
 * 币本位永续合约仓位管理Service接口
 */
public interface CoinFuturesPositionManagementService {
    
    /**
     * 搜索仓位（持仓管理/合约资产管理）
     */
    Page<CoinFuturesPositionManagement> searchPositions(CoinFuturesPositionSearchRequest request);

    /**
     * 根据ID获取仓位详情
     */
    CoinFuturesPositionManagement getPositionById(Long id);
}














