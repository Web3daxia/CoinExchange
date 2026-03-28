/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.CurrencyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 币种多语言名称Repository
 */
@Repository
public interface CurrencyNameRepository extends JpaRepository<CurrencyName, Long> {
    
    List<CurrencyName> findByCurrencyId(Long currencyId);
    
    Optional<CurrencyName> findByCurrencyIdAndLanguageCode(Long currencyId, String languageCode);
    
    void deleteByCurrencyId(Long currencyId);
}














