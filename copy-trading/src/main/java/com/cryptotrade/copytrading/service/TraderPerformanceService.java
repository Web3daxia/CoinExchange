/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service;

import com.cryptotrade.copytrading.entity.TraderPerformance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 带单员表现数据服务接口
 */
public interface TraderPerformanceService {
    /**
     * 计算并保存带单员表现数据
     * @param traderId 带单员ID
     * @param periodType 周期类型
     * @param periodStart 周期开始时间
     * @param periodEnd 周期结束时间
     * @return 表现数据
     */
    TraderPerformance calculatePerformance(Long traderId, String periodType,
                                          LocalDateTime periodStart, LocalDateTime periodEnd);

    /**
     * 查询带单员表现数据
     * @param traderId 带单员ID
     * @param periodType 周期类型（可选）
     * @return 表现数据列表
     */
    List<TraderPerformance> getTraderPerformance(Long traderId, String periodType);

    /**
     * 查询带单员详细表现数据
     * @param traderId 带单员ID
     * @return 表现数据详情
     */
    Map<String, Object> getTraderPerformanceDetails(Long traderId);

    /**
     * 获取带单员排行榜
     * @param sortBy 排序方式（AUM、PROFIT、WIN_RATE等）
     * @param limit 返回数量
     * @return 带单员列表
     */
    List<Map<String, Object>> getTraderLeaderboard(String sortBy, Integer limit);

    /**
     * 更新带单员实时统计数据
     * @param traderId 带单员ID
     */
    void updateTraderStatistics(Long traderId);
}















