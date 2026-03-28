/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionContractRepository extends JpaRepository<OptionContract, Long> {
    Optional<OptionContract> findByPairNameAndOptionTypeAndStrikePriceAndExpiryDate(
            String pairName, String optionType, java.math.BigDecimal strikePrice, LocalDateTime expiryDate);

    List<OptionContract> findByPairNameAndStatus(String pairName, String status);

    List<OptionContract> findByStatus(String status);

    List<OptionContract> findByExpiryDateBeforeAndStatus(LocalDateTime expiryDate, String status);

    List<OptionContract> findByOptionTypeAndExerciseType(String optionType, String exerciseType);
}















