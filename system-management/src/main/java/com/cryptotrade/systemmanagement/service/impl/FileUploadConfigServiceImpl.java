/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.FileUploadConfig;
import com.cryptotrade.systemmanagement.repository.FileUploadConfigRepository;
import com.cryptotrade.systemmanagement.service.FileUploadConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class FileUploadConfigServiceImpl implements FileUploadConfigService {
    
    @Autowired
    private FileUploadConfigRepository repository;
    
    @Override
    @Transactional
    public FileUploadConfig createConfig(FileUploadConfig config) {
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public FileUploadConfig updateConfig(Long id, FileUploadConfig config) {
        FileUploadConfig existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public FileUploadConfig getConfigById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("配置不存在"));
    }
    
    @Override
    public FileUploadConfig getConfig(String uploadType, String fileCategory) {
        return repository.findByUploadTypeAndFileCategory(uploadType, fileCategory).orElse(null);
    }
    
    @Override
    public List<FileUploadConfig> getConfigsByUploadType(String uploadType) {
        return repository.findByUploadTypeAndEnabledTrue(uploadType);
    }
    
    @Override
    public List<FileUploadConfig> getAllConfigs() {
        return repository.findAll();
    }
    
    @Override
    public boolean isFileAllowed(String uploadType, String fileCategory, String fileName, long fileSize) {
        FileUploadConfig config = getConfig(uploadType, fileCategory);
        if (config == null || !config.getEnabled()) {
            return false;
        }
        
        // 检查文件大小
        if (fileSize > config.getMaxFileSize()) {
            return false;
        }
        
        // 检查文件扩展名
        String extension = getFileExtension(fileName);
        if (extension == null) {
            return false;
        }
        
        String[] allowedExtensions = config.getAllowedExtensions().toLowerCase().split(",");
        return Arrays.asList(allowedExtensions).contains(extension.toLowerCase());
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(lastDot + 1);
    }
}

