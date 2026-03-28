/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.repository;

import com.cryptotrade.miningpool.entity.HashrateRental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户算力租赁记录Repository
 */
@Repository
public interface HashrateRentalRepository extends JpaRepository<HashrateRental, Long>, JpaSpecificationExecutor<HashrateRental> {
    
    Optional<HashrateRental> findByRentalOrderNo(String rentalOrderNo);
    
    List<HashrateRental> findByUserId(Long userId);
    
    List<HashrateRental> findByPoolId(Long poolId);
    
    List<HashrateRental> findByUserIdAndStatus(Long userId, String status);
    
    Page<HashrateRental> findByUserId(Long userId, Pageable pageable);
}














