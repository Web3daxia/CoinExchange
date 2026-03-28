/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.FileUploadConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadConfigRepository extends JpaRepository<FileUploadConfig, Long> {
    Optional<FileUploadConfig> findByUploadTypeAndFileCategory(String uploadType, String fileCategory);
    
    List<FileUploadConfig> findByUploadType(String uploadType);
    
    List<FileUploadConfig> findByUploadTypeAndEnabledTrue(String uploadType);
    
    List<FileUploadConfig> findByFileCategory(String fileCategory);
}














