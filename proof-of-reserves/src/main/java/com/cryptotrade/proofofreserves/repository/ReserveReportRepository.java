/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.repository;

import com.cryptotrade.proofofreserves.entity.ReserveReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 储备金证明报告Repository
 */
@Repository
public interface ReserveReportRepository extends JpaRepository<ReserveReport, Long> {
    /**
     * 根据审计状态查询
     */
    List<ReserveReport> findByAuditStatus(String auditStatus);

    /**
     * 根据报告编号查询
     */
    ReserveReport findByReportNo(String reportNo);
}















