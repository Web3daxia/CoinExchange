/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import com.cryptotrade.wallet.dto.request.AssetTransferRequest;
import com.cryptotrade.wallet.dto.response.AssetTransferResponse;
import com.cryptotrade.wallet.entity.AssetTransferRecord;
import org.springframework.data.domain.Page;

/**
 * 资产划转服务接口
 */
public interface AssetTransferService {
    
    /**
     * 执行资产划转
     * @param userId 用户ID
     * @param request 划转请求
     * @return 划转结果
     */
    AssetTransferResponse transfer(Long userId, AssetTransferRequest request);
    
    /**
     * 查询划转记录
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 划转记录列表
     */
    Page<AssetTransferRecord> getTransferHistory(Long userId, int page, int size);
    
    /**
     * 查询划转记录详情
     * @param transferId 划转记录ID
     * @return 划转记录
     */
    AssetTransferRecord getTransferDetail(Long transferId);
}














