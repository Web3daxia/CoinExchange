/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service.impl;

import com.cryptotrade.financeproduct.dto.request.FinanceProductCreateRequest;
import com.cryptotrade.financeproduct.entity.FinanceProduct;
import com.cryptotrade.financeproduct.repository.FinanceProductRepository;
import com.cryptotrade.financeproduct.service.FinanceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 理财产品管理Service实现
 */
@Service
public class FinanceProductServiceImpl implements FinanceProductService {

    @Autowired
    private FinanceProductRepository productRepository;

    @Override
    @Transactional
    public FinanceProduct createProduct(FinanceProductCreateRequest request) {
        // 检查产品代码是否已存在
        if (productRepository.findByProductCode(request.getProductCode()).isPresent()) {
            throw new RuntimeException("产品代码已存在: " + request.getProductCode());
        }

        FinanceProduct product = new FinanceProduct();
        product.setProductName(request.getProductName());
        product.setProductCode(request.getProductCode());
        product.setProductType(request.getProductType());
        product.setRiskLevel(request.getRiskLevel());
        product.setAnnualRate(request.getAnnualRate());
        product.setInvestmentCycle(request.getInvestmentCycle());
        product.setMinInvestmentAmount(request.getMinInvestmentAmount());
        product.setMaxInvestmentAmount(request.getMaxInvestmentAmount());
        product.setTotalRaiseAmount(request.getTotalRaiseAmount());
        product.setSupportedCurrency(request.getSupportedCurrency());
        product.setLockPeriod(request.getLockPeriod());
        product.setStatus(request.getStatus() != null ? request.getStatus() : "AVAILABLE");
        product.setStartTime(request.getStartTime());
        product.setEndTime(request.getEndTime());
        product.setDescription(request.getDescription());
        product.setRiskWarning(request.getRiskWarning());
        product.setSettlementMethod(request.getSettlementMethod() != null ? request.getSettlementMethod() : "AUTO");
        product.setSettlementCycle(request.getSettlementCycle());
        product.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        // 初始化已投资金额和剩余可投资金额
        product.setInvestedAmount(java.math.BigDecimal.ZERO);
        if (request.getTotalRaiseAmount() != null) {
            product.setAvailableAmount(request.getTotalRaiseAmount());
        } else {
            product.setAvailableAmount(java.math.BigDecimal.valueOf(Long.MAX_VALUE)); // 无限制
        }

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public FinanceProduct updateProduct(Long productId, FinanceProductCreateRequest request) {
        FinanceProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("理财产品不存在: " + productId));

        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        if (request.getProductCode() != null && !product.getProductCode().equals(request.getProductCode())) {
            // 检查新代码是否冲突
            if (productRepository.findByProductCode(request.getProductCode()).isPresent()) {
                throw new RuntimeException("产品代码已存在: " + request.getProductCode());
            }
            product.setProductCode(request.getProductCode());
        }
        if (request.getProductType() != null) {
            product.setProductType(request.getProductType());
        }
        if (request.getRiskLevel() != null) {
            product.setRiskLevel(request.getRiskLevel());
        }
        if (request.getAnnualRate() != null) {
            product.setAnnualRate(request.getAnnualRate());
        }
        if (request.getInvestmentCycle() != null) {
            product.setInvestmentCycle(request.getInvestmentCycle());
        }
        if (request.getMinInvestmentAmount() != null) {
            product.setMinInvestmentAmount(request.getMinInvestmentAmount());
        }
        if (request.getMaxInvestmentAmount() != null) {
            product.setMaxInvestmentAmount(request.getMaxInvestmentAmount());
        }
        if (request.getTotalRaiseAmount() != null) {
            product.setTotalRaiseAmount(request.getTotalRaiseAmount());
            // 更新剩余可投资金额
            product.setAvailableAmount(request.getTotalRaiseAmount().subtract(product.getInvestedAmount()));
        }
        if (request.getSupportedCurrency() != null) {
            product.setSupportedCurrency(request.getSupportedCurrency());
        }
        if (request.getLockPeriod() != null) {
            product.setLockPeriod(request.getLockPeriod());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        if (request.getStartTime() != null) {
            product.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            product.setEndTime(request.getEndTime());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getRiskWarning() != null) {
            product.setRiskWarning(request.getRiskWarning());
        }
        if (request.getSettlementMethod() != null) {
            product.setSettlementMethod(request.getSettlementMethod());
        }
        if (request.getSettlementCycle() != null) {
            product.setSettlementCycle(request.getSettlementCycle());
        }
        if (request.getSortOrder() != null) {
            product.setSortOrder(request.getSortOrder());
        }

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("理财产品不存在: " + productId);
        }
        // TODO: 检查是否有用户投资，如果有则不允许删除
        productRepository.deleteById(productId);
    }

    @Override
    public FinanceProduct getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("理财产品不存在: " + productId));
    }

    @Override
    public FinanceProduct getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("理财产品不存在: " + productCode));
    }

    @Override
    public List<FinanceProduct> getAllProducts() {
        return productRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public List<FinanceProduct> getAvailableProducts() {
        return productRepository.findByStatus("AVAILABLE");
    }

    @Override
    public List<FinanceProduct> getProductsByType(String productType) {
        return productRepository.findByProductType(productType);
    }

    @Override
    public List<FinanceProduct> getProductsByRiskLevel(String riskLevel) {
        return productRepository.findByRiskLevel(riskLevel);
    }

    @Override
    public List<FinanceProduct> getProductsByCurrency(String currency) {
        return productRepository.findBySupportedCurrency(currency);
    }
}














