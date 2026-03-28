/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.AppVersion;

import java.util.List;

/**
 * APP版本管理Service接口
 */
public interface AppVersionService {
    
    /**
     * 创建APP版本
     */
    AppVersion createVersion(AppVersion appVersion);
    
    /**
     * 更新APP版本
     */
    AppVersion updateVersion(Long id, AppVersion appVersion);
    
    /**
     * 删除APP版本
     */
    void deleteVersion(Long id);
    
    /**
     * 根据ID获取版本
     */
    AppVersion getVersionById(Long id);
    
    /**
     * 根据平台和版本号获取版本
     */
    AppVersion getVersionByPlatformAndVersion(String platform, String version);
    
    /**
     * 获取所有版本
     */
    List<AppVersion> getAllVersions();
    
    /**
     * 根据平台获取版本列表
     */
    List<AppVersion> getVersionsByPlatform(String platform);
    
    /**
     * 根据平台和状态获取版本列表
     */
    List<AppVersion> getVersionsByPlatformAndStatus(String platform, String status);
    
    /**
     * 获取最新版本（按平台）
     */
    AppVersion getLatestVersion(String platform);
}














