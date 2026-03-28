/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质押借币订单Repository
 */
@Repository
public interface PledgeLoanOrderRepository extends JpaRepository<PledgeLoanOrder, Long>, JpaSpecificationExecutor<PledgeLoanOrder> {
    
    Optional<PledgeLoanOrder> findByOrderNo(String orderNo);
    
    List<PledgeLoanOrder> findByUserId(Long userId);
    
    List<PledgeLoanOrder> findByUserIdAndStatus(Long userId, String status);
    
    Page<PledgeLoanOrder> findByUserId(Long userId, Pageable pageable);
    
    Page<PledgeLoanOrder> findByStatus(String status, Pageable pageable);
    
    List<PledgeLoanOrder> findByStatus(String status);
    
    List<PledgeLoanOrder> findByStatusIn(List<String> statuses);
}














