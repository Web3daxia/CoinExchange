/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.repository;

import com.cryptotrade.spot.entity.SpotOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotOrderRepository extends JpaRepository<SpotOrder, Long> {
    List<SpotOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<SpotOrder> findByUserIdAndStatus(Long userId, String status);
    List<SpotOrder> findByPairNameAndStatus(String pairName, String status);
    List<SpotOrder> findByPairNameAndStatusAndPriceLessThanEqualOrderByPriceAsc(String pairName, String status, java.math.BigDecimal price);
    List<SpotOrder> findByPairNameAndStatusAndPriceGreaterThanEqualOrderByPriceDesc(String pairName, String status, java.math.BigDecimal price);
}















