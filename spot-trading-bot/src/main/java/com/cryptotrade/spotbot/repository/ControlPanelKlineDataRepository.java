/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.repository;

import com.cryptotrade.spotbot.document.ControlPanelKlineData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 控盘机器人K线数据Repository（MongoDB）
 */
@Repository
public interface ControlPanelKlineDataRepository extends MongoRepository<ControlPanelKlineData, String> {
    
    /**
     * 根据交易对名称和K线周期查询
     */
    List<ControlPanelKlineData> findByPairNameAndInterval(String pairName, String interval);

    /**
     * 根据交易对名称、K线周期和时间范围查询
     */
    List<ControlPanelKlineData> findByPairNameAndIntervalAndOpenTimeMillisBetween(
            String pairName, String interval, Long startTime, Long endTime);

    /**
     * 根据交易对名称查询所有K线数据
     */
    List<ControlPanelKlineData> findByPairName(String pairName);

    /**
     * 根据交易对名称和开盘时间查询
     */
    ControlPanelKlineData findByPairNameAndIntervalAndOpenTimeMillis(
            String pairName, String interval, Long openTimeMillis);

    /**
     * 根据交易对名称删除所有K线数据
     */
    void deleteByPairName(String pairName);

    /**
     * 根据交易对名称、K线周期和时间范围删除
     */
    void deleteByPairNameAndIntervalAndOpenTimeMillisBetween(
            String pairName, String interval, Long startTime, Long endTime);
}














