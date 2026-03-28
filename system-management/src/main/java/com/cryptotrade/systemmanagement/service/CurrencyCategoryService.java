/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.CurrencyCategory;

import java.util.List;

/**
 * 币种分类Service接口
 */
public interface CurrencyCategoryService {
    
    /**
     * 创建分类
     */
    CurrencyCategory createCategory(String categoryCode, String categoryName, String categoryNameEn, 
                                    Integer sortOrder, String description);
    
    /**
     * 更新分类
     */
    CurrencyCategory updateCategory(Long categoryId, String categoryName, String categoryNameEn,
                                    Integer sortOrder, String status, String description);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long categoryId);
    
    /**
     * 根据ID获取分类
     */
    CurrencyCategory getCategoryById(Long categoryId);
    
    /**
     * 根据代码获取分类
     */
    CurrencyCategory getCategoryByCode(String categoryCode);
    
    /**
     * 获取所有分类
     */
    List<CurrencyCategory> getAllCategories();
    
    /**
     * 获取激活的分类列表
     */
    List<CurrencyCategory> getActiveCategories();
}














