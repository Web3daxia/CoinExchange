/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.repository;

import com.cryptotrade.proofofreserves.entity.UserDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 用户存款Repository
 */
@Repository
public interface UserDepositRepository extends JpaRepository<UserDeposit, Long> {
    /**
     * 根据用户ID查询存款
     */
    List<UserDeposit> findByUserId(Long userId);

    /**
     * 根据存款哈希查询
     */
    Optional<UserDeposit> findByDepositHash(String depositHash);

    /**
     * 统计总存款
     */
    @Query("SELECT COALESCE(SUM(ud.balance), 0) FROM UserDeposit ud WHERE ud.currency = :currency")
    BigDecimal sumBalanceByCurrency(@Param("currency") String currency);
}















