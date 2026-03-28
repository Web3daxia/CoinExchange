/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.service;

import com.cryptotrade.inviterebate.entity.RebateRecord;
import com.cryptotrade.inviterebate.entity.RebateSettlement;
import com.cryptotrade.inviterebate.entity.SystemRebateConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 后台返佣管理服务接口
 */
public interface AdminRebateService {
    /**
     * 设置系统返佣配置
     */
    SystemRebateConfig setSystemRebateConfig(SystemRebateConfig config);

    /**
     * 获取系统返佣配置列表
     */
    List<SystemRebateConfig> getSystemRebateConfigs();

    /**
     * 审核返佣结算
     */
    RebateSettlement auditSettlement(Long settlementId, Long auditUserId, String status, String remark);

    /**
     * 获取返佣统计报表
     */
    Map<String, Object> getRebateStatistics(String startDate, String endDate);

    /**
     * 查询大额返佣记录（需要审核）
     */
    List<RebateRecord> getLargeRebateRecords(BigDecimal minAmount);
}















