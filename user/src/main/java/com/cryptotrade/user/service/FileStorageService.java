/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    /**
     * 存储文件并返回URL
     */
    String storeFile(MultipartFile file, String directory) throws IOException;

    /**
     * 删除文件
     */
    void deleteFile(String fileUrl) throws IOException;
}















