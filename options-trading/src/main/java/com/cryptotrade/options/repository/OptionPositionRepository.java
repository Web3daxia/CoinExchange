/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionPositionRepository extends JpaRepository<OptionPosition, Long> {
    List<OptionPosition> findByUserIdAndStatus(Long userId, String status);

    List<OptionPosition> findByUserId(Long userId);

    Optional<OptionPosition> findByUserIdAndContractIdAndSide(Long userId, Long contractId, String side);

    List<OptionPosition> findByStatusAndExpiryDateBefore(String status, LocalDateTime expiryDate);

    List<OptionPosition> findByContractId(Long contractId);

    List<OptionPosition> findByStatus(String status);
}

