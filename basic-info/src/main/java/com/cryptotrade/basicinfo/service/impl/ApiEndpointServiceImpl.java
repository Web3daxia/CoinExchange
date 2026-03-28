/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.entity.ApiEndpoint;
import com.cryptotrade.basicinfo.entity.UserPreference;
import com.cryptotrade.basicinfo.repository.ApiEndpointRepository;
import com.cryptotrade.basicinfo.repository.UserPreferenceRepository;
import com.cryptotrade.basicinfo.service.ApiEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * API端点服务实现类
 */
@Service
public class ApiEndpointServiceImpl implements ApiEndpointService {

    @Autowired
    private ApiEndpointRepository apiEndpointRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Override
    public List<ApiEndpoint> getAvailableEndpoints(String endpointType) {
        if (endpointType != null && !endpointType.isEmpty()) {
            return apiEndpointRepository.findByEndpointTypeAndStatusOrderByPriorityAsc(endpointType, "ACTIVE");
        }
        return apiEndpointRepository.findByStatusOrderByPriorityAsc("ACTIVE");
    }

    @Override
    public ApiEndpoint getDefaultEndpoint(String endpointType) {
        Optional<ApiEndpoint> defaultEndpoint = apiEndpointRepository
                .findByIsDefaultTrueAndEndpointTypeAndStatus(endpointType, "ACTIVE");
        
        if (defaultEndpoint.isPresent()) {
            return defaultEndpoint.get();
        }

        // 如果没有默认端点，返回优先级最高的
        List<ApiEndpoint> endpoints = apiEndpointRepository
                .findByEndpointTypeAndStatusOrderByPriorityAsc(endpointType, "ACTIVE");
        return endpoints.isEmpty() ? null : endpoints.get(0);
    }

    @Override
    @Transactional
    public void setUserEndpoint(Long userId, Long endpointId) {
        Optional<UserPreference> existing = userPreferenceRepository.findByUserId(userId);
        
        UserPreference preference;
        if (existing.isPresent()) {
            preference = existing.get();
        } else {
            preference = new UserPreference();
            preference.setUserId(userId);
            preference.setLanguageCode("en");
            preference.setCurrency("USD");
        }
        
        preference.setApiEndpointId(endpointId);
        userPreferenceRepository.save(preference);
    }

    @Override
    public ApiEndpoint getUserEndpoint(Long userId, String endpointType) {
        Optional<UserPreference> preference = userPreferenceRepository.findByUserId(userId);
        
        if (preference.isPresent()) {
            Long endpointId = preference.get().getApiEndpointId();
            if (endpointId != null) {
                Optional<ApiEndpoint> endpoint = apiEndpointRepository.findById(endpointId);
                if (endpoint.isPresent() && "ACTIVE".equals(endpoint.get().getStatus())) {
                    return endpoint.get();
                }
            }
        }

        // 返回默认端点
        return getDefaultEndpoint(endpointType);
    }

    @Override
    @Transactional
    public boolean checkEndpointHealth(Long endpointId) {
        if (endpointId == null) {
            return false;
        }
        
        Optional<ApiEndpoint> endpointOpt = apiEndpointRepository.findById(endpointId);
        if (!endpointOpt.isPresent()) {
            return false;
        }

        ApiEndpoint endpoint = endpointOpt.get();
        
        // TODO: 实际应该发送HTTP请求检查端点健康状态
        // 这里简化处理
        long startTime = System.currentTimeMillis();
        // 模拟检查
        long responseTime = System.currentTimeMillis() - startTime;
        
        endpoint.setResponseTime(responseTime);
        endpoint.setLastCheckTime(LocalDateTime.now());
        apiEndpointRepository.save(endpoint);

        return responseTime < 5000; // 响应时间小于5秒认为健康
    }
}














