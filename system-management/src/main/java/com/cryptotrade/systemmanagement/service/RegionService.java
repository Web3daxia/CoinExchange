/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.Region;
import java.util.List;
import java.util.Map;

public interface RegionService {
    Region createRegion(String countryCode, String countryName, String regionCode, String regionName,
                       Boolean apiAccessEnabled, Boolean frontendAccessEnabled, String blockReason,
                       Map<String, String> blockMessage);
    
    Region updateRegion(Long regionId, String countryName, String regionCode, String regionName,
                       Boolean apiAccessEnabled, Boolean frontendAccessEnabled, String blockReason,
                       Map<String, String> blockMessage, String status);
    
    void deleteRegion(Long regionId);
    
    Region getRegionById(Long regionId);
    
    Region getRegionByCountryCode(String countryCode);
    
    List<Region> getAllRegions();
    
    List<Region> getBlockedRegions();
    
    boolean isApiAccessAllowed(String countryCode);
    
    boolean isFrontendAccessAllowed(String countryCode);
    
    String getBlockMessage(String countryCode, String languageCode);
}














