/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.repository;

import com.cryptotrade.experiencefund.entity.ContractExperienceAccountChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 合约体验金账户变更记录Repository
 */
@Repository
public interface ContractExperienceAccountChangeRepository extends JpaRepository<ContractExperienceAccountChange, Long> {
    
    List<ContractExperienceAccountChange> findByAccountId(Long accountId);
    
    List<ContractExperienceAccountChange> findByUserId(Long userId);
    
    List<ContractExperienceAccountChange> findByChangeType(String changeType);
}














