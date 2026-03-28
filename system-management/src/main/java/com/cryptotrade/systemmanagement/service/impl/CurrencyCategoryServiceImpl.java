/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.CurrencyCategory;
import com.cryptotrade.systemmanagement.repository.CurrencyCategoryRepository;
import com.cryptotrade.systemmanagement.repository.CurrencyCategoryRelationRepository;
import com.cryptotrade.systemmanagement.service.CurrencyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 币种分类Service实现
 */
@Service
public class CurrencyCategoryServiceImpl implements CurrencyCategoryService {

    @Autowired
    private CurrencyCategoryRepository categoryRepository;

    @Autowired
    private CurrencyCategoryRelationRepository relationRepository;

    @Override
    @Transactional
    public CurrencyCategory createCategory(String categoryCode, String categoryName, String categoryNameEn,
                                           Integer sortOrder, String description) {
        if (categoryRepository.findByCategoryCode(categoryCode).isPresent()) {
            throw new RuntimeException("分类代码已存在: " + categoryCode);
        }

        CurrencyCategory category = new CurrencyCategory();
        category.setCategoryCode(categoryCode);
        category.setCategoryName(categoryName);
        category.setCategoryNameEn(categoryNameEn);
        category.setSortOrder(sortOrder != null ? sortOrder : 0);
        category.setStatus("ACTIVE");
        category.setDescription(description);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CurrencyCategory updateCategory(Long categoryId, String categoryName, String categoryNameEn,
                                           Integer sortOrder, String status, String description) {
        CurrencyCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + categoryId));

        if (categoryName != null) {
            category.setCategoryName(categoryName);
        }
        if (categoryNameEn != null) {
            category.setCategoryNameEn(categoryNameEn);
        }
        if (sortOrder != null) {
            category.setSortOrder(sortOrder);
        }
        if (status != null) {
            category.setStatus(status);
        }
        if (description != null) {
            category.setDescription(description);
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("分类不存在: " + categoryId);
        }

        // 删除分类关联
        relationRepository.deleteByCategoryId(categoryId);
        // 删除分类
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CurrencyCategory getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + categoryId));
    }

    @Override
    public CurrencyCategory getCategoryByCode(String categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + categoryCode));
    }

    @Override
    public List<CurrencyCategory> getAllCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public List<CurrencyCategory> getActiveCategories() {
        return categoryRepository.findByStatus("ACTIVE");
    }
}














