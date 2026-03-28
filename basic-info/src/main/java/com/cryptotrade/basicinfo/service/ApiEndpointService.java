/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

import com.cryptotrade.basicinfo.entity.ApiEndpoint;

import java.util.List;

/**
 * API端点服务接口
 */
public interface ApiEndpointService {
    /**
     * 获取所有可用的API端点
     * @param endpointType 端点类型（可选）
     * @return API端点列表
     */
    List<ApiEndpoint> getAvailableEndpoints(String endpointType);

    /**
     * 获取默认API端点
     * @param endpointType 端点类型
     * @return API端点
     */
    ApiEndpoint getDefaultEndpoint(String endpointType);

    /**
     * 设置用户选择的API端点
     * @param userId 用户ID
     * @param endpointId 端点ID
     */
    void setUserEndpoint(Long userId, Long endpointId);

    /**
     * 获取用户选择的API端点
     * @param userId 用户ID
     * @param endpointType 端点类型
     * @return API端点
     */
    ApiEndpoint getUserEndpoint(Long userId, String endpointType);

    /**
     * 检查API端点健康状态
     * @param endpointId 端点ID
     * @return 是否健康
     */
    boolean checkEndpointHealth(Long endpointId);
}















