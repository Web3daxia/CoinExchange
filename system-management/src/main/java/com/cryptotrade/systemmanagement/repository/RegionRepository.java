/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCountryCode(String countryCode);
    
    boolean existsByCountryCode(String countryCode);
    
    List<Region> findByApiAccessEnabledFalse();
    
    List<Region> findByFrontendAccessEnabledFalse();
    
    List<Region> findByStatus(String status);
}














