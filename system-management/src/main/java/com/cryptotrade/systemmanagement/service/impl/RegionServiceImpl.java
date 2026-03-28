/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.Region;
import com.cryptotrade.systemmanagement.repository.RegionRepository;
import com.cryptotrade.systemmanagement.service.RegionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RegionServiceImpl implements RegionService {
    
    @Autowired
    private RegionRepository regionRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional
    public Region createRegion(String countryCode, String countryName, String regionCode, String regionName,
                               Boolean apiAccessEnabled, Boolean frontendAccessEnabled, String blockReason,
                               Map<String, String> blockMessage) {
        if (regionRepository.existsByCountryCode(countryCode)) {
            throw new RuntimeException("国家代码已存在: " + countryCode);
        }
        
        Region region = new Region();
        region.setCountryCode(countryCode);
        region.setCountryName(countryName);
        region.setRegionCode(regionCode);
        region.setRegionName(regionName);
        region.setApiAccessEnabled(apiAccessEnabled != null ? apiAccessEnabled : true);
        region.setFrontendAccessEnabled(frontendAccessEnabled != null ? frontendAccessEnabled : true);
        region.setBlockReason(blockReason);
        
        if (blockMessage != null) {
            try {
                region.setBlockMessage(objectMapper.writeValueAsString(blockMessage));
            } catch (Exception e) {
                throw new RuntimeException("限制消息格式错误", e);
            }
        }
        
        region.setStatus("ACTIVE");
        
        return regionRepository.save(region);
    }
    
    @Override
    @Transactional
    public Region updateRegion(Long regionId, String countryName, String regionCode, String regionName,
                               Boolean apiAccessEnabled, Boolean frontendAccessEnabled, String blockReason,
                               Map<String, String> blockMessage, String status) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("地区不存在"));
        
        if (countryName != null) region.setCountryName(countryName);
        if (regionCode != null) region.setRegionCode(regionCode);
        if (regionName != null) region.setRegionName(regionName);
        if (apiAccessEnabled != null) region.setApiAccessEnabled(apiAccessEnabled);
        if (frontendAccessEnabled != null) region.setFrontendAccessEnabled(frontendAccessEnabled);
        if (blockReason != null) region.setBlockReason(blockReason);
        if (status != null) region.setStatus(status);
        
        if (blockMessage != null) {
            try {
                region.setBlockMessage(objectMapper.writeValueAsString(blockMessage));
            } catch (Exception e) {
                throw new RuntimeException("限制消息格式错误", e);
            }
        }
        
        return regionRepository.save(region);
    }
    
    @Override
    @Transactional
    public void deleteRegion(Long regionId) {
        regionRepository.deleteById(regionId);
    }
    
    @Override
    public Region getRegionById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("地区不存在"));
    }
    
    @Override
    public Region getRegionByCountryCode(String countryCode) {
        return regionRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new RuntimeException("地区不存在"));
    }
    
    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
    
    @Override
    public List<Region> getBlockedRegions() {
        return regionRepository.findByApiAccessEnabledFalse();
    }
    
    @Override
    public boolean isApiAccessAllowed(String countryCode) {
        return regionRepository.findByCountryCode(countryCode)
                .map(Region::getApiAccessEnabled)
                .orElse(true); // 默认允许
    }
    
    @Override
    public boolean isFrontendAccessAllowed(String countryCode) {
        return regionRepository.findByCountryCode(countryCode)
                .map(Region::getFrontendAccessEnabled)
                .orElse(true); // 默认允许
    }
    
    @Override
    public String getBlockMessage(String countryCode, String languageCode) {
        Region region = regionRepository.findByCountryCode(countryCode).orElse(null);
        if (region == null || region.getBlockMessage() == null) {
            return "Access denied in your region"; // 默认消息
        }
        
        try {
            Map<String, String> messages = objectMapper.readValue(
                    region.getBlockMessage(), 
                    new TypeReference<Map<String, String>>() {});
            return messages.getOrDefault(languageCode, messages.getOrDefault("en-US", "Access denied in your region"));
        } catch (Exception e) {
            return "Access denied in your region";
        }
    }
}














