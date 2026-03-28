/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.repository;

import com.cryptotrade.miningpool.entity.HashrateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 算力类型Repository
 */
@Repository
public interface HashrateTypeRepository extends JpaRepository<HashrateType, Long> {
    
    Optional<HashrateType> findByTypeCode(String typeCode);
    
    List<HashrateType> findByStatus(String status);
    
    List<HashrateType> findByAlgorithm(String algorithm);
}














