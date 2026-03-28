/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.repository;

import com.cryptotrade.user.entity.KycAdvanced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycAdvancedRepository extends JpaRepository<KycAdvanced, Long> {
    Optional<KycAdvanced> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}















