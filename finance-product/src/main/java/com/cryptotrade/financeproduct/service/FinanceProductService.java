/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service;

import com.cryptotrade.financeproduct.dto.request.FinanceProductCreateRequest;
import com.cryptotrade.financeproduct.entity.FinanceProduct;

import java.util.List;

/**
 * 理财产品管理Service接口
 */
public interface FinanceProductService {
    
    /**
     * 创建理财产品
     */
    FinanceProduct createProduct(FinanceProductCreateRequest request);
    
    /**
     * 更新理财产品
     */
    FinanceProduct updateProduct(Long productId, FinanceProductCreateRequest request);
    
    /**
     * 删除理财产品
     */
    void deleteProduct(Long productId);
    
    /**
     * 根据ID获取理财产品
     */
    FinanceProduct getProductById(Long productId);
    
    /**
     * 根据产品代码获取理财产品
     */
    FinanceProduct getProductByCode(String productCode);
    
    /**
     * 获取所有理财产品
     */
    List<FinanceProduct> getAllProducts();
    
    /**
     * 获取可用（可购买）的理财产品
     */
    List<FinanceProduct> getAvailableProducts();
    
    /**
     * 根据产品类型获取理财产品
     */
    List<FinanceProduct> getProductsByType(String productType);
    
    /**
     * 根据风险等级获取理财产品
     */
    List<FinanceProduct> getProductsByRiskLevel(String riskLevel);
    
    /**
     * 根据币种获取理财产品
     */
    List<FinanceProduct> getProductsByCurrency(String currency);
}














