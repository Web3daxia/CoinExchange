/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OptionOrderRepository extends JpaRepository<OptionOrder, Long> {
    List<OptionOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<OptionOrder> findByUserIdAndStatus(Long userId, String status);

    List<OptionOrder> findByContractIdAndStatus(Long contractId, String status);

    List<OptionOrder> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}















