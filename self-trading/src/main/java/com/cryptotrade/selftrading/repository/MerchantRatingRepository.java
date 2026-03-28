/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.repository;

import com.cryptotrade.selftrading.entity.MerchantRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRatingRepository extends JpaRepository<MerchantRating, Long> {
    List<MerchantRating> findByMerchantIdAndStatusOrderByCreatedAtDesc(Long merchantId, String status);

    List<MerchantRating> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}




