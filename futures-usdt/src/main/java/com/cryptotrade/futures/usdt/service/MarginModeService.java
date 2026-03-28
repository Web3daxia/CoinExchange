/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.dto.request.CreateSegmentRequest;
import com.cryptotrade.futures.usdt.dto.request.SetMarginModeRequest;
import com.cryptotrade.futures.usdt.dto.request.UpdateSegmentRequest;
import com.cryptotrade.futures.usdt.entity.MarginMode;
import com.cryptotrade.futures.usdt.entity.MarginSegment;

import java.math.BigDecimal;
import java.util.List;

public interface MarginModeService {
    /**
     * 设置保证金模式
     */
    MarginMode setMarginMode(Long userId, SetMarginModeRequest request);

    /**
     * 查询用户保证金模式
     */
    MarginMode getMarginMode(Long userId);

    /**
     * 创建分仓（分仓模式）
     */
    MarginSegment createSegment(Long userId, CreateSegmentRequest request);

    /**
     * 查询分仓列表
     */
    List<MarginSegment> getSegments(Long userId);

    /**
     * 更新分仓
     */
    MarginSegment updateSegment(Long userId, Long segmentId, UpdateSegmentRequest request);

    /**
     * 删除分仓
     */
    void deleteSegment(Long userId, Long segmentId);

    /**
     * 计算保证金（根据保证金模式）
     * @param userId 用户ID
     * @param positionQuantity 持仓数量
     * @param price 价格
     * @param leverage 杠杆倍数
     * @param marginMode 保证金模式
     * @param segmentId 分仓ID（分仓模式使用）
     * @return 可用保证金
     */
    BigDecimal calculateMargin(Long userId, BigDecimal positionQuantity, BigDecimal price, 
                               Integer leverage, String marginMode, Long segmentId);
}

