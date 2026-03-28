/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import com.cryptotrade.user.dto.request.KycAdvancedSubmitRequest;
import com.cryptotrade.user.dto.request.KycBasicSubmitRequest;
import com.cryptotrade.user.dto.response.KycAdvancedStatusResponse;
import com.cryptotrade.user.dto.response.KycBasicStatusResponse;

public interface KycService {
    /**
     * 提交基础KYC认证
     */
    void submitBasicKyc(Long userId, KycBasicSubmitRequest request);

    /**
     * 查询基础KYC状态
     */
    KycBasicStatusResponse getBasicKycStatus(Long userId);

    /**
     * 提交高级KYC认证
     */
    void submitAdvancedKyc(Long userId, KycAdvancedSubmitRequest request);

    /**
     * 查询高级KYC状态
     */
    KycAdvancedStatusResponse getAdvancedKycStatus(Long userId);
}















