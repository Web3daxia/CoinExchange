/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.DeliveryContractAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 交割合约账户Repository
 */
@Repository
public interface DeliveryContractAccountRepository extends JpaRepository<DeliveryContractAccount, Long> {
    
    /**
     * 根据用户ID和币种查询账户
     */
    Optional<DeliveryContractAccount> findByUserIdAndCurrency(Long userId, String currency);
    
    /**
     * 根据用户ID查询所有账户
     */
    List<DeliveryContractAccount> findByUserId(Long userId);
    
    /**
     * 检查用户和币种的账户是否存在
     */
    boolean existsByUserIdAndCurrency(Long userId, String currency);
}














