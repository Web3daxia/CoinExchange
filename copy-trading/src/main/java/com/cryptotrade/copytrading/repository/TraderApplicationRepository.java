/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.repository;

import com.cryptotrade.copytrading.entity.TraderApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraderApplicationRepository extends JpaRepository<TraderApplication, Long> {
    Optional<TraderApplication> findByUserIdAndStatus(Long userId, String status);

    List<TraderApplication> findByUserId(Long userId);

    List<TraderApplication> findByStatus(String status);

    List<TraderApplication> findByTraderTypeAndStatus(String traderType, String status);
}















