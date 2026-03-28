/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.repository;

import com.cryptotrade.selftrading.entity.MerchantApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantApplicationRepository extends JpaRepository<MerchantApplication, Long> {
    Optional<MerchantApplication> findByUserIdAndStatus(Long userId, String status);

    List<MerchantApplication> findByUserId(Long userId);

    List<MerchantApplication> findByStatus(String status);
}















