/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

/**
 * GeoIP服务接口
 * 用于根据IP地址获取地理位置信息
 */
public interface GeoIPService {
    /**
     * 根据IP地址获取国家代码
     * @param ipAddress IP地址
     * @return 国家代码（ISO 3166-1 alpha-2，如 CN, US, GB）
     */
    String getCountryCode(String ipAddress);

    /**
     * 根据IP地址获取国家名称
     * @param ipAddress IP地址
     * @return 国家名称
     */
    String getCountryName(String ipAddress);
}


