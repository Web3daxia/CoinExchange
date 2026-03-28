/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.repository;

import com.cryptotrade.selftrading.entity.MerchantFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantFollowRepository extends JpaRepository<MerchantFollow, Long> {
    Optional<MerchantFollow> findByMerchantIdAndUserId(Long merchantId, Long userId);

    List<MerchantFollow> findByUserId(Long userId);

    List<MerchantFollow> findByMerchantId(Long merchantId);
}















