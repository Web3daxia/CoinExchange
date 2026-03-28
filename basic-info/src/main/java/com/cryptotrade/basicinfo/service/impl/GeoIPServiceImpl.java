/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.service.GeoIPService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * GeoIP服务实现类
 * 使用 ip-api.com 免费API获取IP地理位置信息
 */
@Service
public class GeoIPServiceImpl implements GeoIPService {

    private static final Logger logger = LoggerFactory.getLogger(GeoIPServiceImpl.class);

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ip-api.com 免费API端点（JSON格式）
    private static final String IP_API_URL = "http://ip-api.com/json/{ip}?fields=status,message,country,countryCode";

    // Redis缓存键前缀
    private static final String CACHE_PREFIX_COUNTRY_CODE = "geoip:country_code:";
    private static final String CACHE_PREFIX_COUNTRY_NAME = "geoip:country_name:";
    
    // 缓存过期时间（24小时）
    private static final long CACHE_EXPIRE_HOURS = 24;

    @Override
    public String getCountryCode(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            return null;
        }

        // 处理本地IP地址
        if (isLocalIp(ipAddress)) {
            return null;
        }

        // 从缓存获取
        if (redisTemplate != null) {
            String cacheKey = CACHE_PREFIX_COUNTRY_CODE + ipAddress;
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return cached;
            }
        }

        try {
            // 调用GeoIP API
            String countryCode = fetchCountryCodeFromAPI(ipAddress);
            
            // 缓存结果
            if (countryCode != null && redisTemplate != null) {
                String cacheKey = CACHE_PREFIX_COUNTRY_CODE + ipAddress;
                redisTemplate.opsForValue().set(cacheKey, countryCode, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
            
            return countryCode;
        } catch (Exception e) {
            logger.warn("获取IP地址 {} 的国家代码失败: {}", ipAddress, e.getMessage());
            return null;
        }
    }

    @Override
    public String getCountryName(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            return null;
        }

        // 处理本地IP地址
        if (isLocalIp(ipAddress)) {
            return null;
        }

        // 从缓存获取
        if (redisTemplate != null) {
            String cacheKey = CACHE_PREFIX_COUNTRY_NAME + ipAddress;
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return cached;
            }
        }

        try {
            // 调用GeoIP API
            String countryName = fetchCountryNameFromAPI(ipAddress);
            
            // 缓存结果
            if (countryName != null && redisTemplate != null) {
                String cacheKey = CACHE_PREFIX_COUNTRY_NAME + ipAddress;
                redisTemplate.opsForValue().set(cacheKey, countryName, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
            
            return countryName;
        } catch (Exception e) {
            logger.warn("获取IP地址 {} 的国家名称失败: {}", ipAddress, e.getMessage());
            return null;
        }
    }

    /**
     * 从API获取国家代码
     */
    private String fetchCountryCodeFromAPI(String ipAddress) {
        if (restTemplate == null) {
            logger.warn("RestTemplate未配置，无法调用GeoIP API");
            return null;
        }

        try {
            String response = restTemplate.getForObject(IP_API_URL, String.class, ipAddress);
            if (response == null) {
                return null;
            }

            JsonNode jsonNode = objectMapper.readTree(response);
            
            // 检查API响应状态
            if (jsonNode.has("status") && "success".equals(jsonNode.get("status").asText())) {
                if (jsonNode.has("countryCode")) {
                    return jsonNode.get("countryCode").asText();
                }
            } else {
                String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "未知错误";
                logger.warn("GeoIP API返回错误: {}", message);
            }
        } catch (Exception e) {
            logger.error("调用GeoIP API失败", e);
        }

        return null;
    }

    /**
     * 从API获取国家名称
     */
    private String fetchCountryNameFromAPI(String ipAddress) {
        if (restTemplate == null) {
            logger.warn("RestTemplate未配置，无法调用GeoIP API");
            return null;
        }

        try {
            String response = restTemplate.getForObject(IP_API_URL, String.class, ipAddress);
            if (response == null) {
                return null;
            }

            JsonNode jsonNode = objectMapper.readTree(response);
            
            // 检查API响应状态
            if (jsonNode.has("status") && "success".equals(jsonNode.get("status").asText())) {
                if (jsonNode.has("country")) {
                    return jsonNode.get("country").asText();
                }
            } else {
                String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "未知错误";
                logger.warn("GeoIP API返回错误: {}", message);
            }
        } catch (Exception e) {
            logger.error("调用GeoIP API失败", e);
        }

        return null;
    }

    /**
     * 判断是否为本地IP地址
     */
    private boolean isLocalIp(String ipAddress) {
        if (ipAddress == null) {
            return true;
        }
        
        // 本地回环地址
        if (ipAddress.equals("127.0.0.1") || ipAddress.equals("localhost") || 
            ipAddress.equals("::1") || ipAddress.startsWith("192.168.") ||
            ipAddress.startsWith("10.") || ipAddress.startsWith("172.16.") ||
            ipAddress.startsWith("172.17.") || ipAddress.startsWith("172.18.") ||
            ipAddress.startsWith("172.19.") || ipAddress.startsWith("172.20.") ||
            ipAddress.startsWith("172.21.") || ipAddress.startsWith("172.22.") ||
            ipAddress.startsWith("172.23.") || ipAddress.startsWith("172.24.") ||
            ipAddress.startsWith("172.25.") || ipAddress.startsWith("172.26.") ||
            ipAddress.startsWith("172.27.") || ipAddress.startsWith("172.28.") ||
            ipAddress.startsWith("172.29.") || ipAddress.startsWith("172.30.") ||
            ipAddress.startsWith("172.31.")) {
            return true;
        }
        
        return false;
    }
}


