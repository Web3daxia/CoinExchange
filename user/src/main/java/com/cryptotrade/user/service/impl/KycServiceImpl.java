/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service.impl;

import com.cryptotrade.user.dto.request.KycAdvancedSubmitRequest;
import com.cryptotrade.user.dto.request.KycBasicSubmitRequest;
import com.cryptotrade.user.dto.response.KycAdvancedStatusResponse;
import com.cryptotrade.user.dto.response.KycBasicStatusResponse;
import com.cryptotrade.user.entity.KycAdvanced;
import com.cryptotrade.user.entity.KycBasic;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.repository.KycAdvancedRepository;
import com.cryptotrade.user.repository.KycBasicRepository;
import com.cryptotrade.user.repository.UserRepository;
import com.cryptotrade.user.service.FaceRecognitionService;
import com.cryptotrade.user.service.FileStorageService;
import com.cryptotrade.user.service.KycService;
import com.cryptotrade.user.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class KycServiceImpl implements KycService {

    @Autowired
    private KycBasicRepository kycBasicRepository;

    @Autowired
    private KycAdvancedRepository kycAdvancedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private OcrService ocrService;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    private static final double FACE_MATCH_THRESHOLD = 0.8; // 人脸匹配阈值

    @Override
    @Transactional
    public void submitBasicKyc(Long userId, KycBasicSubmitRequest request) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查是否已提交
        Optional<KycBasic> existingKyc = kycBasicRepository.findByUserId(userId);
        if (existingKyc.isPresent()) {
            KycBasic existing = existingKyc.get();
            if ("PENDING".equals(existing.getStatus()) || "PROCESSING".equals(existing.getStatus())) {
                throw new RuntimeException("KYC认证正在审核中，请勿重复提交");
            }
            if ("APPROVED".equals(existing.getStatus())) {
                throw new RuntimeException("基础KYC认证已通过，无需重复提交");
            }
        }

        try {
            // 保存文件
            String frontImageUrl = fileStorageService.storeFile(request.getFrontImage(), "kyc/basic/" + userId);
            String backImageUrl = fileStorageService.storeFile(request.getBackImage(), "kyc/basic/" + userId);

            KycBasic kycBasic;
            if (existingKyc.isPresent()) {
                kycBasic = existingKyc.get();
                // 删除旧文件
                if (kycBasic.getFrontImageUrl() != null) {
                    fileStorageService.deleteFile(kycBasic.getFrontImageUrl());
                }
                if (kycBasic.getBackImageUrl() != null) {
                    fileStorageService.deleteFile(kycBasic.getBackImageUrl());
                }
            } else {
                kycBasic = new KycBasic();
                kycBasic.setUserId(userId);
            }

            kycBasic.setRealName(request.getRealName());
            kycBasic.setIdType(request.getIdType());
            kycBasic.setIdNumber(request.getIdNumber());
            kycBasic.setFrontImageUrl(frontImageUrl);
            kycBasic.setBackImageUrl(backImageUrl);
            kycBasic.setStatus("PROCESSING");
            kycBasic.setReviewType("AUTO");

            // 保存初始状态
            kycBasic = kycBasicRepository.save(kycBasic);

            // 异步处理OCR识别（实际应该使用异步任务）
            processOcrRecognition(kycBasic);

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理OCR识别
     */
    private void processOcrRecognition(KycBasic kycBasic) {
        try {
            // 调用OCR服务识别身份证信息
            Map<String, String> ocrResult = ocrService.recognizeIdCard(
                    kycBasic.getFrontImageUrl(),
                    kycBasic.getIdType()
            );

            if (ocrResult != null && !ocrResult.isEmpty()) {
                // OCR识别成功，进行信息匹配
                boolean matched = matchOcrResult(kycBasic, ocrResult);
                
                if (matched) {
                    // 信息匹配，自动通过
                    kycBasic.setStatus("APPROVED");
                    kycBasic.setReviewType("AUTO");
                    kycBasic.setReviewedAt(LocalDateTime.now());
                    
                    // 更新用户KYC级别
                    User user = userRepository.findById(kycBasic.getUserId()).orElse(null);
                    if (user != null) {
                        user.setKycLevel(1);
                        user.setKycStatus("APPROVED");
                        userRepository.save(user);
                    }
                } else {
                    // 信息不匹配，转为人工审核
                    kycBasic.setStatus("PENDING");
                    kycBasic.setReviewType("MANUAL");
                }
                
                // 保存OCR结果（JSON格式）
                // kycBasic.setOcrResult(JsonUtils.toJson(ocrResult));
            } else {
                // OCR识别失败，转为人工审核
                kycBasic.setStatus("PENDING");
                kycBasic.setReviewType("MANUAL");
            }
            
            kycBasicRepository.save(kycBasic);
            
        } catch (Exception e) {
            // OCR处理失败，转为人工审核
            kycBasic.setStatus("PENDING");
            kycBasic.setReviewType("MANUAL");
            kycBasicRepository.save(kycBasic);
        }
    }

    /**
     * 匹配OCR识别结果与用户提交的信息
     */
    private boolean matchOcrResult(KycBasic kycBasic, Map<String, String> ocrResult) {
        // 比较姓名
        String ocrName = ocrResult.get("name");
        if (ocrName == null || !ocrName.equals(kycBasic.getRealName())) {
            return false;
        }

        // 比较证件号（可能需要脱敏处理）
        String ocrIdNumber = ocrResult.get("idNumber");
        if (ocrIdNumber == null || !ocrIdNumber.equals(kycBasic.getIdNumber())) {
            return false;
        }

        return true;
    }

    @Override
    public KycBasicStatusResponse getBasicKycStatus(Long userId) {
        KycBasic kycBasic = kycBasicRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("未找到KYC认证记录"));

        KycBasicStatusResponse response = new KycBasicStatusResponse();
        response.setId(kycBasic.getId());
        response.setUserId(kycBasic.getUserId());
        response.setRealName(kycBasic.getRealName());
        response.setIdType(kycBasic.getIdType());
        // 证件号脱敏显示
        response.setIdNumber(maskIdNumber(kycBasic.getIdNumber()));
        response.setStatus(kycBasic.getStatus());
        response.setReviewType(kycBasic.getReviewType());
        response.setRejectReason(kycBasic.getRejectReason());
        response.setCreatedAt(kycBasic.getCreatedAt());
        response.setReviewedAt(kycBasic.getReviewedAt());

        return response;
    }

    @Override
    @Transactional
    public void submitAdvancedKyc(Long userId, KycAdvancedSubmitRequest request) {
        // 检查基础KYC是否通过
        KycBasic kycBasic = kycBasicRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("请先完成基础KYC认证"));

        if (!"APPROVED".equals(kycBasic.getStatus())) {
            throw new RuntimeException("基础KYC认证未通过，无法进行高级认证");
        }

        // 检查是否已提交
        Optional<KycAdvanced> existingKyc = kycAdvancedRepository.findByUserId(userId);
        if (existingKyc.isPresent()) {
            KycAdvanced existing = existingKyc.get();
            if ("PENDING".equals(existing.getStatus()) || "PROCESSING".equals(existing.getStatus())) {
                throw new RuntimeException("高级KYC认证正在审核中，请勿重复提交");
            }
            if ("APPROVED".equals(existing.getStatus())) {
                throw new RuntimeException("高级KYC认证已通过，无需重复提交");
            }
        }

        try {
            // 保存文件
            String handheldImageUrl = fileStorageService.storeFile(
                    request.getHandheldImage(), "kyc/advanced/" + userId);
            String videoUrl = fileStorageService.storeFile(
                    request.getVideo(), "kyc/advanced/" + userId);

            KycAdvanced kycAdvanced;
            if (existingKyc.isPresent()) {
                kycAdvanced = existingKyc.get();
                // 删除旧文件
                if (kycAdvanced.getHandheldImageUrl() != null) {
                    fileStorageService.deleteFile(kycAdvanced.getHandheldImageUrl());
                }
                if (kycAdvanced.getVideoUrl() != null) {
                    fileStorageService.deleteFile(kycAdvanced.getVideoUrl());
                }
            } else {
                kycAdvanced = new KycAdvanced();
                kycAdvanced.setUserId(userId);
                kycAdvanced.setKycBasicId(kycBasic.getId());
            }

            kycAdvanced.setHandheldImageUrl(handheldImageUrl);
            kycAdvanced.setVideoUrl(videoUrl);
            kycAdvanced.setVerificationStatement(request.getVerificationStatement());
            kycAdvanced.setStatus("PROCESSING");

            // 保存初始状态
            kycAdvanced = kycAdvancedRepository.save(kycAdvanced);

            // 异步处理人脸识别（实际应该使用异步任务）
            processFaceRecognition(kycAdvanced, kycBasic);

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理人脸识别
     */
    private void processFaceRecognition(KycAdvanced kycAdvanced, KycBasic kycBasic) {
        try {
            // 从视频中提取人脸特征
            boolean faceExtracted = faceRecognitionService.extractFaceFromVideo(kycAdvanced.getVideoUrl());
            if (!faceExtracted) {
                kycAdvanced.setStatus("REJECTED");
                kycAdvanced.setRejectReason("无法从视频中提取人脸特征");
                kycAdvancedRepository.save(kycAdvanced);
                return;
            }

            // 验证语音语句
            boolean statementVerified = faceRecognitionService.verifyStatement(
                    kycAdvanced.getVideoUrl(),
                    kycAdvanced.getVerificationStatement()
            );
            if (!statementVerified) {
                kycAdvanced.setStatus("REJECTED");
                kycAdvanced.setRejectReason("验证语句不匹配");
                kycAdvancedRepository.save(kycAdvanced);
                return;
            }

            // 人脸比对
            Double matchScore = faceRecognitionService.compareFace(
                    kycBasic.getFrontImageUrl(),
                    kycAdvanced.getVideoUrl()
            );

            kycAdvanced.setFaceMatchScore(matchScore);
            kycAdvanced.setFaceMatchResult(matchScore >= FACE_MATCH_THRESHOLD);

            if (matchScore >= FACE_MATCH_THRESHOLD) {
                // 人脸匹配成功，认证通过
                kycAdvanced.setStatus("APPROVED");
                kycAdvanced.setReviewedAt(LocalDateTime.now());

                // 更新用户KYC级别
                User user = userRepository.findById(kycAdvanced.getUserId()).orElse(null);
                if (user != null) {
                    user.setKycLevel(2);
                    user.setKycStatus("APPROVED");
                    userRepository.save(user);
                }
            } else {
                // 人脸匹配失败，拒绝
                kycAdvanced.setStatus("REJECTED");
                kycAdvanced.setRejectReason("人脸匹配失败，匹配分数: " + matchScore);
            }

            kycAdvancedRepository.save(kycAdvanced);

        } catch (Exception e) {
            // 人脸识别处理失败，转为人工审核
            kycAdvanced.setStatus("PENDING");
            kycAdvanced.setRejectReason("人脸识别处理失败: " + e.getMessage());
            kycAdvancedRepository.save(kycAdvanced);
        }
    }

    @Override
    public KycAdvancedStatusResponse getAdvancedKycStatus(Long userId) {
        KycAdvanced kycAdvanced = kycAdvancedRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("未找到高级KYC认证记录"));

        KycAdvancedStatusResponse response = new KycAdvancedStatusResponse();
        response.setId(kycAdvanced.getId());
        response.setUserId(kycAdvanced.getUserId());
        response.setStatus(kycAdvanced.getStatus());
        response.setFaceMatchScore(kycAdvanced.getFaceMatchScore());
        response.setFaceMatchResult(kycAdvanced.getFaceMatchResult());
        response.setRejectReason(kycAdvanced.getRejectReason());
        response.setCreatedAt(kycAdvanced.getCreatedAt());
        response.setReviewedAt(kycAdvanced.getReviewedAt());

        return response;
    }

    /**
     * 证件号脱敏
     */
    private String maskIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() < 8) {
            return idNumber;
        }
        // 保留前4位和后4位，中间用*代替
        int length = idNumber.length();
        String prefix = idNumber.substring(0, 4);
        String suffix = idNumber.substring(length - 4);
        return prefix + "****" + suffix;
    }
}















