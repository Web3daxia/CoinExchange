/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.CurrencyCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 币种分类关联Repository
 */
@Repository
public interface CurrencyCategoryRelationRepository extends JpaRepository<CurrencyCategoryRelation, Long> {
    
    List<CurrencyCategoryRelation> findByCurrencyId(Long currencyId);
    
    List<CurrencyCategoryRelation> findByCategoryId(Long categoryId);
    
    void deleteByCurrencyId(Long currencyId);
    
    void deleteByCategoryId(Long categoryId);
    
    boolean existsByCurrencyIdAndCategoryId(Long currencyId, Long categoryId);
}














