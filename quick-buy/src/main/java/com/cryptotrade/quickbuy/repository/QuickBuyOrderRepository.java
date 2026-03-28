/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.repository;

import com.cryptotrade.quickbuy.entity.QuickBuyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuickBuyOrderRepository extends JpaRepository<QuickBuyOrder, Long> {
    List<QuickBuyOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<QuickBuyOrder> findByUserIdAndStatus(Long userId, String status);
}















