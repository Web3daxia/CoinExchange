/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.FileUploadConfig;
import java.util.List;

public interface FileUploadConfigService {
    FileUploadConfig createConfig(FileUploadConfig config);
    FileUploadConfig updateConfig(Long id, FileUploadConfig config);
    void deleteConfig(Long id);
    FileUploadConfig getConfigById(Long id);
    FileUploadConfig getConfig(String uploadType, String fileCategory);
    List<FileUploadConfig> getConfigsByUploadType(String uploadType);
    boolean isFileAllowed(String uploadType, String fileCategory, String fileName, long fileSize);
    List<FileUploadConfig> getAllConfigs();
}

