/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service;

import com.cryptotrade.spotbot.dto.request.KlineDataRequest;
import com.cryptotrade.spotbot.dto.response.KlineDataResponse;
import com.cryptotrade.spotbot.document.ControlPanelKlineData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 控盘机器人K线数据Service接口
 */
public interface ControlPanelKlineDataService {
    
    /**
     * 创建K线数据
     */
    ControlPanelKlineData createKlineData(KlineDataRequest request);

    /**
     * 批量创建K线数据
     */
    List<ControlPanelKlineData> batchCreateKlineData(List<KlineDataRequest> requests);

    /**
     * 更新K线数据
     */
    ControlPanelKlineData updateKlineData(String id, KlineDataRequest request);

    /**
     * 根据ID获取K线数据
     */
    ControlPanelKlineData getKlineDataById(String id);

    /**
     * 根据交易对名称和K线周期查询
     */
    List<KlineDataResponse> getKlineDataByPairAndInterval(String pairName, String interval);

    /**
     * 根据交易对名称、K线周期和时间范围查询
     */
    List<KlineDataResponse> getKlineDataByPairAndIntervalAndTimeRange(
            String pairName, String interval, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除K线数据
     */
    void deleteKlineData(String id);

    /**
     * 删除交易对的所有K线数据
     */
    void deleteKlineDataByPairName(String pairName);

    /**
     * 删除指定时间范围的K线数据
     */
    void deleteKlineDataByTimeRange(String pairName, String interval, LocalDateTime startTime, LocalDateTime endTime);
}














