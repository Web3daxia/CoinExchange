/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;
import com.cryptotrade.pledgeloan.entity.PledgeCurrencyConfig;
import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.repository.LoanCurrencyConfigRepository;
import com.cryptotrade.pledgeloan.repository.PledgeCurrencyConfigRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.service.AssetService;
import com.cryptotrade.pledgeloan.service.MarketPriceService;
import com.cryptotrade.pledgeloan.service.PledgeLoanLiquidationService;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import com.cryptotrade.pledgeloan.service.PledgeLoanRiskService;
import com.cryptotrade.pledgeloan.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质押借币订单Service实现
 */
@Service
public class PledgeLoanOrderServiceImpl implements PledgeLoanOrderService {

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @Autowired
    private PledgeCurrencyConfigRepository pledgeConfigRepository;

    @Autowired
    private LoanCurrencyConfigRepository loanConfigRepository;

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private AssetService assetService;

    @Autowired(required = false)
    private PledgeLoanRiskService riskService;

    @Autowired(required = false)
    private PledgeLoanLiquidationService liquidationService;

    @Override
    @Transactional
    public PledgeLoanOrder createOrder(Long userId, String pledgeCurrency, BigDecimal pledgeAmount,
                                       String loanCurrency, BigDecimal loanAmount, Integer loanTermDays) {
        // 获取质押币种配置
        PledgeCurrencyConfig pledgeConfig = pledgeConfigRepository.findByCurrencyCode(pledgeCurrency)
                .orElseThrow(() -> new RuntimeException("质押币种配置不存在: " + pledgeCurrency));

        if (!"ACTIVE".equals(pledgeConfig.getStatus())) {
            throw new RuntimeException("质押币种已停用: " + pledgeCurrency);
        }

        // 检查最小质押金额
        if (pledgeAmount.compareTo(pledgeConfig.getMinPledgeAmount()) < 0) {
            throw new RuntimeException("质押金额低于最小值: " + pledgeConfig.getMinPledgeAmount());
        }

        // 获取借款币种配置
        LoanCurrencyConfig loanConfig = loanConfigRepository.findByCurrencyCode(loanCurrency)
                .orElseThrow(() -> new RuntimeException("借款币种配置不存在: " + loanCurrency));

        if (!"ACTIVE".equals(loanConfig.getStatus())) {
            throw new RuntimeException("借款币种已停用: " + loanCurrency);
        }

        // 检查最小借款金额
        if (loanAmount.compareTo(loanConfig.getMinLoanAmount()) < 0) {
            throw new RuntimeException("借款金额低于最小值: " + loanConfig.getMinLoanAmount());
        }

        // 获取市场价格
        BigDecimal pledgePrice = marketPriceService.getCurrentPrice(pledgeCurrency); // USDT价格
        BigDecimal loanPrice = marketPriceService.getCurrentPrice(loanCurrency); // USDT价格

        // 计算质押价值（USDT）
        BigDecimal pledgeValue = pledgeAmount.multiply(pledgePrice);

        // 计算借款价值（USDT）
        BigDecimal loanValue = loanAmount.multiply(loanPrice);

        // 验证借款额度（根据质押比例计算最大可借额度）
        BigDecimal maxLoanValue = pledgeValue.multiply(pledgeConfig.getLoanRatio());
        if (loanValue.compareTo(maxLoanValue) > 0) {
            throw new RuntimeException("借款金额超过最大可借额度: " + maxLoanValue + " USDT");
        }

        // 检查最大借款额度限制
        if (loanConfig.getMaxLoanAmount() != null && loanAmount.compareTo(loanConfig.getMaxLoanAmount()) > 0) {
            throw new RuntimeException("借款金额超过最大限制: " + loanConfig.getMaxLoanAmount());
        }

        // 计算质押比例
        BigDecimal pledgeRatio = pledgeValue.divide(loanValue, 4, RoundingMode.HALF_UP);

        // 计算平仓价格
        BigDecimal riskRate = pledgeConfig.getRiskRate();
        BigDecimal liquidationPrice = loanValue.multiply(riskRate).divide(pledgeAmount, 8, RoundingMode.HALF_UP);

        // 计算健康度（初始为质押比例）
        BigDecimal healthRate = pledgeRatio;

        // 创建订单
        PledgeLoanOrder order = new PledgeLoanOrder();
        order.setOrderNo(OrderNoGenerator.generateOrderNo());
        order.setUserId(userId);
        order.setPledgeCurrency(pledgeCurrency);
        order.setPledgeAmount(pledgeAmount);
        order.setPledgeValue(pledgeValue);
        order.setLoanCurrency(loanCurrency);
        order.setLoanAmount(loanAmount);
        order.setLoanValue(loanValue);
        order.setInterestRate(loanConfig.getInterestRate());
        order.setLoanTermDays(loanTermDays);
        order.setPledgeRatio(pledgeRatio);
        order.setStatus("PENDING");
        order.setApprovalStatus("PENDING");
        order.setRemainingPrincipal(loanAmount);
        order.setLiquidationPrice(liquidationPrice);
        order.setHealthRate(healthRate);
        order.setLastHealthCheckTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public BigDecimal calculateLoanAmount(String pledgeCurrency, BigDecimal pledgeAmount, String loanCurrency) {
        PledgeCurrencyConfig pledgeConfig = pledgeConfigRepository.findByCurrencyCode(pledgeCurrency)
                .orElseThrow(() -> new RuntimeException("质押币种配置不存在: " + pledgeCurrency));

        LoanCurrencyConfig loanConfig = loanConfigRepository.findByCurrencyCode(loanCurrency)
                .orElseThrow(() -> new RuntimeException("借款币种配置不存在: " + loanCurrency));

        // 获取市场价格
        BigDecimal pledgePrice = marketPriceService.getCurrentPrice(pledgeCurrency);
        BigDecimal loanPrice = marketPriceService.getCurrentPrice(loanCurrency);

        // 计算质押价值（USDT）
        BigDecimal pledgeValue = pledgeAmount.multiply(pledgePrice);

        // 计算最大可借价值（USDT）
        BigDecimal maxLoanValue = pledgeValue.multiply(pledgeConfig.getLoanRatio());

        // 转换为借款币种数量
        BigDecimal maxLoanAmount = maxLoanValue.divide(loanPrice, 8, RoundingMode.DOWN);

        // 检查最大借款额度限制
        if (loanConfig.getMaxLoanAmount() != null && maxLoanAmount.compareTo(loanConfig.getMaxLoanAmount()) > 0) {
            maxLoanAmount = loanConfig.getMaxLoanAmount();
        }

        return maxLoanAmount;
    }

    @Override
    @Transactional
    public PledgeLoanOrder approveOrder(Long orderId, Long approverId, String approvalStatus, String remark) {
        PledgeLoanOrder order = getOrderById(orderId);

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许审批: " + order.getStatus());
        }

        order.setStatus("APPROVED");
        order.setApprovalStatus(approvalStatus);
        order.setApproverId(approverId);
        order.setApprovalTime(LocalDateTime.now());
        order.setApprovalRemark(remark);
        order.setStartTime(LocalDateTime.now());
        order.setEndTime(LocalDateTime.now().plusDays(order.getLoanTermDays()));
        order.setStatus("ACTIVE"); // 审批通过后直接生效

        // 冻结用户的质押资产
        boolean freezeSuccess = assetService.freezeAsset(order.getUserId(), order.getPledgeCurrency(), 
                order.getPledgeAmount(), order.getOrderNo());
        if (!freezeSuccess) {
            throw new RuntimeException("冻结质押资产失败");
        }

        // 发放借款到用户账户
        boolean addSuccess = assetService.addAsset(order.getUserId(), order.getLoanCurrency(), 
                order.getLoanAmount(), order.getOrderNo());
        if (!addSuccess) {
            // 如果发放失败，需要解冻已冻结的资产
            assetService.unfreezeAsset(order.getUserId(), order.getPledgeCurrency(), 
                    order.getPledgeAmount(), order.getOrderNo());
            throw new RuntimeException("发放借款失败");
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public PledgeLoanOrder rejectOrder(Long orderId, Long approverId, String remark) {
        PledgeLoanOrder order = getOrderById(orderId);

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许拒绝: " + order.getStatus());
        }

        order.setStatus("REJECTED");
        order.setApprovalStatus("MANUAL");
        order.setApproverId(approverId);
        order.setApprovalTime(LocalDateTime.now());
        order.setApprovalRemark(remark);

        return orderRepository.save(order);
    }

    @Override
    public PledgeLoanOrder getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    @Override
    public PledgeLoanOrder getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在: " + orderNo));
    }

    @Override
    public List<PledgeLoanOrder> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<PledgeLoanOrder> getUserOrders(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<PledgeLoanOrder> getActiveOrders() {
        return orderRepository.findByStatus("ACTIVE");
    }

    @Override
    public BigDecimal calculateHealthRate(Long orderId) {
        PledgeLoanOrder order = getOrderById(orderId);

        // 获取当前市场价格
        BigDecimal currentPledgePrice = marketPriceService.getCurrentPrice(order.getPledgeCurrency());

        // 计算当前质押价值（USDT）
        BigDecimal currentPledgeValue = order.getPledgeAmount().multiply(currentPledgePrice);

        // 计算健康度（当前质押价值/借款价值）
        BigDecimal healthRate = currentPledgeValue.divide(order.getLoanValue(), 6, RoundingMode.HALF_UP);

        return healthRate;
    }

    @Override
    @Transactional
    public void updateHealthRate(Long orderId) {
        PledgeLoanOrder order = getOrderById(orderId);
        BigDecimal healthRate = calculateHealthRate(orderId);

        order.setHealthRate(healthRate);
        order.setLastHealthCheckTime(LocalDateTime.now());

        // 更新质押价值（当前价值）
        BigDecimal currentPledgePrice = marketPriceService.getCurrentPrice(order.getPledgeCurrency());
        BigDecimal currentPledgeValue = order.getPledgeAmount().multiply(currentPledgePrice);
        order.setPledgeValue(currentPledgeValue);

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void checkAndProcessRiskOrders() {
        List<PledgeLoanOrder> activeOrders = getActiveOrders();

        for (PledgeLoanOrder order : activeOrders) {
            try {
                updateHealthRate(order.getId());

                // 获取质押币种配置
                PledgeCurrencyConfig config = pledgeConfigRepository.findByCurrencyCode(order.getPledgeCurrency())
                        .orElse(null);

                if (config == null) {
                    continue;
                }

                // 检查健康度
                if (order.getHealthRate().compareTo(config.getRiskRate()) < 0) {
                    // 触发平仓（严重风险）
                    if (riskService != null) {
                        riskService.createRiskRecord(order.getId(), "CRITICAL", 
                                "健康度低于平仓线，将触发自动平仓");
                    }
                    // 调用平仓服务
                    if (liquidationService != null) {
                        try {
                            liquidationService.autoLiquidation(order.getId(), "健康度低于平仓线");
                        } catch (Exception e) {
                            System.err.println("自动平仓失败: " + order.getOrderNo() + ", 错误: " + e.getMessage());
                        }
                    }
                } else if (order.getHealthRate().compareTo(config.getMaintenanceRate()) < 0) {
                    // 风险预警（高风险）
                    if (riskService != null) {
                        riskService.createRiskRecord(order.getId(), "HIGH", 
                                "健康度低于预警线，请及时补仓或还款");
                        // 通知用户
                        List<com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord> riskRecords = 
                                riskService.getOrderRiskRecords(order.getId());
                        if (!riskRecords.isEmpty()) {
                            com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord latest = riskRecords.get(riskRecords.size() - 1);
                            if (!latest.getIsNotified()) {
                                riskService.notifyUserRisk(latest.getId());
                            }
                        }
                    }
                } else if (order.getHealthRate().compareTo(BigDecimal.valueOf(1.2)) < 0) {
                    // 中等风险预警
                    if (riskService != null) {
                        riskService.createRiskRecord(order.getId(), "MEDIUM", 
                                "健康度较低，建议关注市场波动");
                    }
                }
            } catch (Exception e) {
                // 记录错误但继续处理其他订单
                System.err.println("检查订单风险失败: " + order.getOrderNo() + ", 错误: " + e.getMessage());
            }
        }
    }

}

