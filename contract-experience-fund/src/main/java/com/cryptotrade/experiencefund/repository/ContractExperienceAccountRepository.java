/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.repository;

import com.cryptotrade.experiencefund.entity.ContractExperienceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 合约体验金账户Repository
 */
@Repository
public interface ContractExperienceAccountRepository extends JpaRepository<ContractExperienceAccount, Long> {
    
    Optional<ContractExperienceAccount> findByUserId(Long userId);
    
    Optional<ContractExperienceAccount> findByAccountNo(String accountNo);
}














