/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service.impl;

import com.cryptotrade.financeproduct.dto.request.FinanceInvestmentRequest;
import com.cryptotrade.financeproduct.entity.FinanceInvestment;
import com.cryptotrade.financeproduct.entity.FinanceProduct;
import com.cryptotrade.financeproduct.repository.FinanceInvestmentRepository;
import com.cryptotrade.financeproduct.repository.FinanceProductRepository;
import com.cryptotrade.financeproduct.service.FinanceInvestmentService;
import com.cryptotrade.financeproduct.util.OrderNoGenerator;
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
 * 用户投资管理Service实现
 */
@Service
public class FinanceInvestmentServiceImpl implements FinanceInvestmentService {

    @Autowired
    private FinanceInvestmentRepository investmentRepository;

    @Autowired
    private FinanceProductRepository productRepository;

    @Override
    @Transactional
    public FinanceInvestment invest(Long userId, FinanceInvestmentRequest request) {
        // 获取理财产品
        FinanceProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在: " + request.getProductId()));

        // 检查产品状态
        if (!"AVAILABLE".equals(product.getStatus())) {
            throw new RuntimeException("产品不可购买，当前状态: " + product.getStatus());
        }

        // 检查投资金额
        if (request.getInvestmentAmount().compareTo(product.getMinInvestmentAmount()) < 0) {
            throw new RuntimeException("投资金额不能小于最小投资金额: " + product.getMinInvestmentAmount());
        }
        if (product.getMaxInvestmentAmount() != null && 
            request.getInvestmentAmount().compareTo(product.getMaxInvestmentAmount()) > 0) {
            throw new RuntimeException("投资金额不能大于最大投资金额: " + product.getMaxInvestmentAmount());
        }

        // 检查剩余可投资金额
        if (product.getAvailableAmount() != null && 
            request.getInvestmentAmount().compareTo(product.getAvailableAmount()) > 0) {
            throw new RuntimeException("剩余可投资金额不足");
        }

        // TODO: 检查用户余额（需要调用钱包服务）
        // TODO: 冻结用户资金

        // 创建投资记录
        FinanceInvestment investment = new FinanceInvestment();
        investment.setUserId(userId);
        investment.setProductId(product.getId());
        investment.setProductCode(product.getProductCode());
        investment.setInvestmentAmount(request.getInvestmentAmount());
        investment.setCurrency(request.getCurrency());
        investment.setStartDate(LocalDateTime.now());
        investment.setRemainingPrincipal(request.getInvestmentAmount());
        investment.setInvestmentOrderNo(OrderNoGenerator.generateInvestmentOrderNo());
        investment.setStatus("ACTIVE");

        // 计算预期收益和结束时间
        if ("FIXED".equals(product.getProductType()) && product.getInvestmentCycle() != null) {
            investment.setEndDate(LocalDateTime.now().plusDays(product.getInvestmentCycle()));
            // 计算预期收益：本金 * 年化收益率 * 投资周期 / 365
            BigDecimal expectedProfit = request.getInvestmentAmount()
                    .multiply(product.getAnnualRate())
                    .multiply(BigDecimal.valueOf(product.getInvestmentCycle()))
                    .divide(BigDecimal.valueOf(365), 8, RoundingMode.HALF_UP);
            investment.setExpectedProfit(expectedProfit);
            
            // 设置锁仓到期时间
            if (product.getLockPeriod() != null) {
                investment.setLockUntil(LocalDateTime.now().plusDays(product.getLockPeriod()));
            }
        }

        // 计算下次结算时间
        if (product.getSettlementCycle() != null) {
            switch (product.getSettlementCycle()) {
                case "DAILY":
                    investment.setNextSettlementTime(LocalDateTime.now().plusDays(1));
                    break;
                case "WEEKLY":
                    investment.setNextSettlementTime(LocalDateTime.now().plusWeeks(1));
                    break;
                case "MONTHLY":
                    investment.setNextSettlementTime(LocalDateTime.now().plusMonths(1));
                    break;
            }
        }

        FinanceInvestment savedInvestment = investmentRepository.save(investment);

        // 更新产品的已投资金额和剩余可投资金额
        product.setInvestedAmount(product.getInvestedAmount().add(request.getInvestmentAmount()));
        if (product.getAvailableAmount() != null) {
            product.setAvailableAmount(product.getAvailableAmount().subtract(request.getInvestmentAmount()));
            // 如果剩余金额为0，更新产品状态为售罄
            if (product.getAvailableAmount().compareTo(BigDecimal.ZERO) <= 0) {
                product.setStatus("SOLD_OUT");
            }
        }
        productRepository.save(product);

        return savedInvestment;
    }

    @Override
    public FinanceInvestment getInvestmentById(Long investmentId) {
        return investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + investmentId));
    }

    @Override
    public FinanceInvestment getInvestmentByOrderNo(String orderNo) {
        return investmentRepository.findByInvestmentOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + orderNo));
    }

    @Override
    public List<FinanceInvestment> getUserInvestments(Long userId) {
        return investmentRepository.findByUserId(userId);
    }

    @Override
    public Page<FinanceInvestment> getUserInvestments(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return investmentRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<FinanceInvestment> getProductInvestments(Long productId) {
        return investmentRepository.findByProductId(productId);
    }

    @Override
    public List<FinanceInvestment> getActiveInvestments(Long userId) {
        return investmentRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    @Override
    public BigDecimal calculateExpectedProfit(Long investmentId) {
        FinanceInvestment investment = getInvestmentById(investmentId);
        FinanceProduct product = productRepository.findById(investment.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在"));

        if ("FIXED".equals(product.getProductType()) && product.getInvestmentCycle() != null) {
            return investment.getInvestmentAmount()
                    .multiply(product.getAnnualRate())
                    .multiply(BigDecimal.valueOf(product.getInvestmentCycle()))
                    .divide(BigDecimal.valueOf(365), 8, RoundingMode.HALF_UP);
        }
        // 灵活型产品的收益需要实时计算
        return BigDecimal.ZERO;
    }
}














