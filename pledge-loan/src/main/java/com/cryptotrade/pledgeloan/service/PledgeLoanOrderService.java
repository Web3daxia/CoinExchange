/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币订单Service接口
 */
public interface PledgeLoanOrderService {
    
    /**
     * 创建质押借币订单（申请）
     */
    PledgeLoanOrder createOrder(Long userId, String pledgeCurrency, BigDecimal pledgeAmount,
                                String loanCurrency, BigDecimal loanAmount, Integer loanTermDays);
    
    /**
     * 计算可借额度
     */
    BigDecimal calculateLoanAmount(String pledgeCurrency, BigDecimal pledgeAmount, String loanCurrency);
    
    /**
     * 审批订单
     */
    PledgeLoanOrder approveOrder(Long orderId, Long approverId, String approvalStatus, String remark);
    
    /**
     * 拒绝订单
     */
    PledgeLoanOrder rejectOrder(Long orderId, Long approverId, String remark);
    
    /**
     * 根据ID获取订单
     */
    PledgeLoanOrder getOrderById(Long orderId);
    
    /**
     * 根据订单号获取订单
     */
    PledgeLoanOrder getOrderByOrderNo(String orderNo);
    
    /**
     * 获取用户的订单列表
     */
    List<PledgeLoanOrder> getUserOrders(Long userId);
    
    /**
     * 获取用户的分页订单列表
     */
    Page<PledgeLoanOrder> getUserOrders(Long userId, Integer page, Integer size);
    
    /**
     * 获取生效中的订单列表
     */
    List<PledgeLoanOrder> getActiveOrders();
    
    /**
     * 计算订单健康度
     */
    BigDecimal calculateHealthRate(Long orderId);
    
    /**
     * 更新订单健康度
     */
    void updateHealthRate(Long orderId);
    
    /**
     * 检查并处理风险订单
     */
    void checkAndProcessRiskOrders();
}














