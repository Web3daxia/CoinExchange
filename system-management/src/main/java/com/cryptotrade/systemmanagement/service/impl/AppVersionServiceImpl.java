/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.AppVersion;
import com.cryptotrade.systemmanagement.repository.AppVersionRepository;
import com.cryptotrade.systemmanagement.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * APP版本管理Service实现
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Override
    @Transactional
    public AppVersion createVersion(AppVersion appVersion) {
        // 检查平台和版本号是否已存在
        Optional<AppVersion> existing = appVersionRepository.findByPlatformAndVersion(
                appVersion.getPlatform(), appVersion.getVersion());
        if (existing.isPresent()) {
            throw new RuntimeException("该平台和版本的记录已存在: " + appVersion.getPlatform() + " " + appVersion.getVersion());
        }
        
        if (appVersion.getStatus() == null) {
            appVersion.setStatus("ACTIVE");
        }
        if (appVersion.getSortOrder() == null) {
            appVersion.setSortOrder(0);
        }
        if (appVersion.getIsForceUpdate() == null) {
            appVersion.setIsForceUpdate(false);
        }
        
        return appVersionRepository.save(appVersion);
    }

    @Override
    @Transactional
    public AppVersion updateVersion(Long id, AppVersion appVersion) {
        AppVersion existing = appVersionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("APP版本不存在: " + id));
        
        // 如果更新了平台和版本号，需要检查是否冲突
        if (appVersion.getPlatform() != null && appVersion.getVersion() != null) {
            if (!existing.getPlatform().equals(appVersion.getPlatform()) || 
                !existing.getVersion().equals(appVersion.getVersion())) {
                Optional<AppVersion> conflict = appVersionRepository.findByPlatformAndVersion(
                        appVersion.getPlatform(), appVersion.getVersion());
                if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
                    throw new RuntimeException("该平台和版本的记录已存在: " + appVersion.getPlatform() + " " + appVersion.getVersion());
                }
            }
        }
        
        if (appVersion.getPlatform() != null) {
            existing.setPlatform(appVersion.getPlatform());
        }
        if (appVersion.getVersion() != null) {
            existing.setVersion(appVersion.getVersion());
        }
        if (appVersion.getMinVersion() != null) {
            existing.setMinVersion(appVersion.getMinVersion());
        }
        if (appVersion.getDownloadUrl() != null) {
            existing.setDownloadUrl(appVersion.getDownloadUrl());
        }
        if (appVersion.getReleaseNotes() != null) {
            existing.setReleaseNotes(appVersion.getReleaseNotes());
        }
        if (appVersion.getFileSize() != null) {
            existing.setFileSize(appVersion.getFileSize());
        }
        if (appVersion.getFileHash() != null) {
            existing.setFileHash(appVersion.getFileHash());
        }
        if (appVersion.getSortOrder() != null) {
            existing.setSortOrder(appVersion.getSortOrder());
        }
        if (appVersion.getStatus() != null) {
            existing.setStatus(appVersion.getStatus());
        }
        if (appVersion.getIsForceUpdate() != null) {
            existing.setIsForceUpdate(appVersion.getIsForceUpdate());
        }
        
        return appVersionRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteVersion(Long id) {
        if (!appVersionRepository.existsById(id)) {
            throw new RuntimeException("APP版本不存在: " + id);
        }
        appVersionRepository.deleteById(id);
    }

    @Override
    public AppVersion getVersionById(Long id) {
        return appVersionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("APP版本不存在: " + id));
    }

    @Override
    public AppVersion getVersionByPlatformAndVersion(String platform, String version) {
        return appVersionRepository.findByPlatformAndVersion(platform, version)
                .orElseThrow(() -> new RuntimeException("APP版本不存在: " + platform + " " + version));
    }

    @Override
    public List<AppVersion> getAllVersions() {
        return appVersionRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public List<AppVersion> getVersionsByPlatform(String platform) {
        return appVersionRepository.findByPlatform(platform);
    }

    @Override
    public List<AppVersion> getVersionsByPlatformAndStatus(String platform, String status) {
        return appVersionRepository.findByPlatformAndStatus(platform, status);
    }

    @Override
    public AppVersion getLatestVersion(String platform) {
        List<AppVersion> versions = appVersionRepository.findByPlatformAndStatus(platform, "ACTIVE");
        if (versions.isEmpty()) {
            return null;
        }
        // 返回排序最靠前（sort_order最小）的版本，如果相同则返回版本号最大的
        return versions.stream()
                .sorted((v1, v2) -> {
                    int sortCompare = Integer.compare(v1.getSortOrder(), v2.getSortOrder());
                    if (sortCompare != 0) {
                        return sortCompare;
                    }
                    return compareVersions(v2.getVersion(), v1.getVersion()); // 版本号降序
                })
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 比较版本号
     */
    private int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");
        int maxLength = Math.max(parts1.length, parts2.length);
        
        for (int i = 0; i < maxLength; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (part1 != part2) {
                return Integer.compare(part1, part2);
            }
        }
        return 0;
    }
}














