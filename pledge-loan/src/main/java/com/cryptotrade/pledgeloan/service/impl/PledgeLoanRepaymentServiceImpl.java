/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.entity.PledgeLoanRepayment;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanRepaymentRepository;
import com.cryptotrade.pledgeloan.service.AssetService;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import com.cryptotrade.pledgeloan.service.PledgeLoanRepaymentService;
import com.cryptotrade.pledgeloan.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质押借币还款Service实现
 */
@Service
public class PledgeLoanRepaymentServiceImpl implements PledgeLoanRepaymentService {

    @Autowired
    private PledgeLoanRepaymentRepository repaymentRepository;

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @Autowired
    private PledgeLoanOrderService orderService;

    @Autowired
    private AssetService assetService;

    @Override
    @Transactional
    public PledgeLoanRepayment repayFull(Long orderId, Long userId) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        // 检查用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许还款: " + order.getStatus());
        }

        // 计算应还利息
        BigDecimal interest = calculateInterest(orderId);
        BigDecimal totalAmount = order.getRemainingPrincipal().add(interest);

        // 创建还款记录
        PledgeLoanRepayment repayment = new PledgeLoanRepayment();
        repayment.setOrderId(orderId);
        repayment.setOrderNo(order.getOrderNo());
        repayment.setUserId(userId);
        repayment.setRepaymentNo(OrderNoGenerator.generateRepaymentNo());
        repayment.setRepaymentType("FULL");
        repayment.setPrincipalAmount(order.getRemainingPrincipal());
        repayment.setInterestAmount(interest);
        repayment.setTotalAmount(totalAmount);
        repayment.setRepaymentCurrency(order.getLoanCurrency());
        repayment.setRepaymentTime(LocalDateTime.now());
        repayment.setIsEarlyRepayment(order.getEndTime().isAfter(LocalDateTime.now()));

        repaymentRepository.save(repayment);

        // 扣除用户账户资金
        boolean deductSuccess = assetService.deductAsset(userId, order.getLoanCurrency(), totalAmount, order.getOrderNo());
        if (!deductSuccess) {
            throw new RuntimeException("扣除用户资金失败，余额不足");
        }

        // 更新订单状态
        order.setStatus("REPAID");
        order.setActualEndTime(LocalDateTime.now());
        order.setRemainingPrincipal(BigDecimal.ZERO);
        order.setPaidInterest(order.getPaidInterest().add(interest));
        orderRepository.save(order);

        // 解冻质押资产
        assetService.unfreezeAsset(userId, order.getPledgeCurrency(), order.getPledgeAmount(), order.getOrderNo());
        
        // 返还质押资产到用户账户（解冻后自动可用，无需额外操作）

        return repayment;
    }

    @Override
    @Transactional
    public PledgeLoanRepayment repayPartial(Long orderId, Long userId, BigDecimal principalAmount) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        // 检查用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许还款: " + order.getStatus());
        }

        // 检查还款金额
        if (principalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("还款金额必须大于0");
        }

        if (principalAmount.compareTo(order.getRemainingPrincipal()) > 0) {
            throw new RuntimeException("还款金额不能超过剩余本金");
        }

        // 计算利息（按比例计算）
        BigDecimal interestRatio = principalAmount.divide(order.getRemainingPrincipal(), 6, RoundingMode.HALF_UP);
        BigDecimal interest = calculateInterest(orderId).multiply(interestRatio);
        BigDecimal totalAmount = principalAmount.add(interest);

        // 创建还款记录
        PledgeLoanRepayment repayment = new PledgeLoanRepayment();
        repayment.setOrderId(orderId);
        repayment.setOrderNo(order.getOrderNo());
        repayment.setUserId(userId);
        repayment.setRepaymentNo(OrderNoGenerator.generateRepaymentNo());
        repayment.setRepaymentType("PARTIAL");
        repayment.setPrincipalAmount(principalAmount);
        repayment.setInterestAmount(interest);
        repayment.setTotalAmount(totalAmount);
        repayment.setRepaymentCurrency(order.getLoanCurrency());
        repayment.setRepaymentTime(LocalDateTime.now());
        repayment.setIsEarlyRepayment(false);

        repaymentRepository.save(repayment);

        // 更新订单
        order.setRemainingPrincipal(order.getRemainingPrincipal().subtract(principalAmount));
        order.setPaidInterest(order.getPaidInterest().add(interest));
        
        // 如果本金已还清，更新订单状态
        if (order.getRemainingPrincipal().compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("REPAID");
            order.setActualEndTime(LocalDateTime.now());
        }
        
        orderRepository.save(order);

        // 扣除用户账户资金
        boolean deductSuccess = assetService.deductAsset(userId, order.getLoanCurrency(), totalAmount, order.getOrderNo());
        if (!deductSuccess) {
            throw new RuntimeException("扣除用户资金失败，余额不足");
        }

        // 部分还款后，根据新的本金重新计算质押比例和解冻部分质押资产
        // 简化处理：全部还清后才解冻质押资产
        // 如果本金已还清，解冻全部质押资产
        if (order.getRemainingPrincipal().compareTo(BigDecimal.ZERO) <= 0) {
            assetService.unfreezeAsset(userId, order.getPledgeCurrency(), order.getPledgeAmount(), order.getOrderNo());
        }
        // 注意：如果需要根据还款比例解冻部分质押资产，可以按以下逻辑实现：
        // 1. 计算还款比例：principalAmount / 原始本金
        // 2. 计算应解冻的质押资产：order.getPledgeAmount() * 还款比例
        // 3. 解冻相应数量的质押资产
        // 4. 更新订单的质押金额：order.setPledgeAmount(order.getPledgeAmount() - 解冻金额)

        return repayment;
    }

    @Override
    @Transactional
    public PledgeLoanRepayment repayInterest(Long orderId, Long userId, BigDecimal interestAmount) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        // 检查用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许还款: " + order.getStatus());
        }

        // 计算应还利息
        BigDecimal totalInterest = calculateInterest(orderId);
        BigDecimal unpaidInterest = totalInterest.subtract(order.getPaidInterest());

        // 检查还款金额
        if (interestAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("还款金额必须大于0");
        }

        if (interestAmount.compareTo(unpaidInterest) > 0) {
            interestAmount = unpaidInterest; // 不能超过未付利息
        }

        // 创建还款记录
        PledgeLoanRepayment repayment = new PledgeLoanRepayment();
        repayment.setOrderId(orderId);
        repayment.setOrderNo(order.getOrderNo());
        repayment.setUserId(userId);
        repayment.setRepaymentNo(OrderNoGenerator.generateRepaymentNo());
        repayment.setRepaymentType("INTEREST");
        repayment.setPrincipalAmount(BigDecimal.ZERO);
        repayment.setInterestAmount(interestAmount);
        repayment.setTotalAmount(interestAmount);
        repayment.setRepaymentCurrency(order.getLoanCurrency());
        repayment.setRepaymentTime(LocalDateTime.now());
        repayment.setIsEarlyRepayment(false);

        repaymentRepository.save(repayment);

        // 更新订单
        order.setPaidInterest(order.getPaidInterest().add(interestAmount));
        orderRepository.save(order);

        // 扣除用户账户资金
        boolean deductSuccess = assetService.deductAsset(userId, order.getLoanCurrency(), interestAmount, order.getOrderNo());
        if (!deductSuccess) {
            throw new RuntimeException("扣除用户资金失败，余额不足");
        }

        return repayment;
    }

    @Override
    public BigDecimal calculateInterest(Long orderId) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        if (!"ACTIVE".equals(order.getStatus()) && !"REPAID".equals(order.getStatus())) {
            return BigDecimal.ZERO;
        }

        // 计算已过天数
        LocalDateTime startTime = order.getStartTime();
        LocalDateTime endTime = order.getEndTime();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime calculateEndTime = now.isBefore(endTime) ? now : endTime;

        long days = java.time.Duration.between(startTime, calculateEndTime).toDays();
        if (days < 0) {
            days = 0;
        }

        // 计算利息：本金 * 年化利率 * 天数 / 365
        BigDecimal dailyRate = order.getInterestRate().divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
        BigDecimal interest = order.getLoanAmount()
                .multiply(dailyRate)
                .multiply(BigDecimal.valueOf(days))
                .setScale(8, RoundingMode.HALF_UP);

        return interest;
    }

    @Override
    public List<PledgeLoanRepayment> getOrderRepayments(Long orderId) {
        return repaymentRepository.findByOrderId(orderId);
    }

    @Override
    public List<PledgeLoanRepayment> getUserRepayments(Long userId) {
        return repaymentRepository.findByUserId(userId);
    }
}

