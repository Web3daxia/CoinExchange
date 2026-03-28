/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service.impl;

import com.cryptotrade.spotbot.dto.request.KlineDataRequest;
import com.cryptotrade.spotbot.dto.response.KlineDataResponse;
import com.cryptotrade.spotbot.document.ControlPanelKlineData;
import com.cryptotrade.spotbot.repository.ControlPanelKlineDataRepository;
import com.cryptotrade.spotbot.service.ControlPanelKlineDataService;
import com.cryptotrade.spotbot.util.KlineIntervalValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 控盘机器人K线数据Service实现
 */
@Service
public class ControlPanelKlineDataServiceImpl implements ControlPanelKlineDataService {

    @Autowired
    private ControlPanelKlineDataRepository klineDataRepository;

    @Override
    public ControlPanelKlineData createKlineData(KlineDataRequest request) {
        // 验证K线周期
        KlineIntervalValidator.validate(request.getInterval());
        
        ControlPanelKlineData klineData = new ControlPanelKlineData();
        BeanUtils.copyProperties(request, klineData);
        
        // 设置时间戳（毫秒）
        if (request.getOpenTime() != null) {
            klineData.setOpenTimeMillis(request.getOpenTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (request.getCloseTime() != null) {
            klineData.setCloseTimeMillis(request.getCloseTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        
        klineData.setCreatedAt(LocalDateTime.now());
        klineData.setUpdatedAt(LocalDateTime.now());
        
        return klineDataRepository.save(klineData);
    }

    @Override
    public List<ControlPanelKlineData> batchCreateKlineData(List<KlineDataRequest> requests) {
        // 验证所有K线周期
        requests.forEach(request -> KlineIntervalValidator.validate(request.getInterval()));
        
        List<ControlPanelKlineData> klineDataList = requests.stream().map(request -> {
            ControlPanelKlineData klineData = new ControlPanelKlineData();
            BeanUtils.copyProperties(request, klineData);
            
            if (request.getOpenTime() != null) {
                klineData.setOpenTimeMillis(request.getOpenTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }
            if (request.getCloseTime() != null) {
                klineData.setCloseTimeMillis(request.getCloseTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }
            
            klineData.setCreatedAt(LocalDateTime.now());
            klineData.setUpdatedAt(LocalDateTime.now());
            
            return klineData;
        }).collect(Collectors.toList());
        
        return klineDataRepository.saveAll(klineDataList);
    }

    @Override
    public ControlPanelKlineData updateKlineData(String id, KlineDataRequest request) {
        // 验证K线周期
        KlineIntervalValidator.validate(request.getInterval());
        
        ControlPanelKlineData klineData = klineDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("K线数据不存在，ID: " + id));

        BeanUtils.copyProperties(request, klineData, "id", "createdAt");
        
        if (request.getOpenTime() != null) {
            klineData.setOpenTimeMillis(request.getOpenTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (request.getCloseTime() != null) {
            klineData.setCloseTimeMillis(request.getCloseTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        
        klineData.setUpdatedAt(LocalDateTime.now());
        
        return klineDataRepository.save(klineData);
    }

    @Override
    public ControlPanelKlineData getKlineDataById(String id) {
        return klineDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("K线数据不存在，ID: " + id));
    }

    @Override
    public List<KlineDataResponse> getKlineDataByPairAndInterval(String pairName, String interval) {
        List<ControlPanelKlineData> klineDataList = klineDataRepository.findByPairNameAndInterval(pairName, interval);
        return klineDataList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<KlineDataResponse> getKlineDataByPairAndIntervalAndTimeRange(
            String pairName, String interval, LocalDateTime startTime, LocalDateTime endTime) {
        Long startTimeMillis = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long endTimeMillis = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        List<ControlPanelKlineData> klineDataList = klineDataRepository
                .findByPairNameAndIntervalAndOpenTimeMillisBetween(pairName, interval, startTimeMillis, endTimeMillis);
        
        return klineDataList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteKlineData(String id) {
        klineDataRepository.deleteById(id);
    }

    @Override
    public void deleteKlineDataByPairName(String pairName) {
        klineDataRepository.deleteByPairName(pairName);
    }

    @Override
    public void deleteKlineDataByTimeRange(String pairName, String interval, LocalDateTime startTime, LocalDateTime endTime) {
        Long startTimeMillis = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long endTimeMillis = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        klineDataRepository.deleteByPairNameAndIntervalAndOpenTimeMillisBetween(pairName, interval, startTimeMillis, endTimeMillis);
    }

    /**
     * 转换为响应DTO
     */
    private KlineDataResponse convertToResponse(ControlPanelKlineData klineData) {
        KlineDataResponse response = new KlineDataResponse();
        BeanUtils.copyProperties(klineData, response);
        return response;
    }
}

