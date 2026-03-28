/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.UsdtFuturesOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.UsdtFuturesOrderManagement;
import com.cryptotrade.systemmanagement.repository.UsdtFuturesOrderManagementRepository;
import com.cryptotrade.systemmanagement.service.UsdtFuturesOrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * U本位永续合约委托管理Service实现
 */
@Service
public class UsdtFuturesOrderManagementServiceImpl implements UsdtFuturesOrderManagementService {

    @Autowired
    private UsdtFuturesOrderManagementRepository repository;

    @Override
    public Page<UsdtFuturesOrderManagement> searchCurrentOrders(UsdtFuturesOrderSearchRequest request) {
        Specification<UsdtFuturesOrderManagement> spec = buildSearchSpecification(request);
        // 当前委托：状态为PENDING的订单
        Specification<UsdtFuturesOrderManagement> currentSpec = spec.and((root, query, cb) -> 
            cb.equal(root.get("status"), "PENDING"));
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.findAll(currentSpec, pageable);
    }

    @Override
    public Page<UsdtFuturesOrderManagement> searchHistoryOrders(UsdtFuturesOrderSearchRequest request) {
        Specification<UsdtFuturesOrderManagement> spec = buildSearchSpecification(request);
        // 历史委托：状态不为PENDING的订单
        Specification<UsdtFuturesOrderManagement> historySpec = spec.and((root, query, cb) -> 
            cb.notEqual(root.get("status"), "PENDING"));
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.findAll(historySpec, pageable);
    }

    /**
     * 构建搜索条件
     */
    private Specification<UsdtFuturesOrderManagement> buildSearchSpecification(UsdtFuturesOrderSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 会员邮箱
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(cb.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            // 用户ID
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), request.getUserId()));
            }

            // 会员UID
            if (StringUtils.hasText(request.getMemberUid())) {
                predicates.add(cb.like(root.get("memberUid"), "%" + request.getMemberUid() + "%"));
            }

            // 手机号
            if (StringUtils.hasText(request.getPhone())) {
                predicates.add(cb.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            // 时间范围
            if (request.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getEndTime()));
            }

            // 合约交易对
            if (StringUtils.hasText(request.getPairName())) {
                predicates.add(cb.equal(root.get("pairName"), request.getPairName()));
            }

            // 委托分类
            if (StringUtils.hasText(request.getOrderCategory()) && !"ALL".equals(request.getOrderCategory())) {
                predicates.add(cb.equal(root.get("orderCategory"), request.getOrderCategory()));
            }

            // 委托方向
            if (StringUtils.hasText(request.getOrderDirection()) && !"ALL".equals(request.getOrderDirection())) {
                predicates.add(cb.equal(root.get("orderDirection"), request.getOrderDirection()));
            }

            // 委托类型
            if (StringUtils.hasText(request.getOrderType()) && !"ALL".equals(request.getOrderType())) {
                predicates.add(cb.equal(root.get("orderType"), request.getOrderType()));
            }

            // 是否爆仓单
            if (request.getIsLiquidation() != null) {
                predicates.add(cb.equal(root.get("isLiquidation"), request.getIsLiquidation()));
            }

            // 是否计划委托
            if (request.getIsPlannedOrder() != null) {
                predicates.add(cb.equal(root.get("isPlannedOrder"), request.getIsPlannedOrder()));
            }

            // 委托状态
            if (StringUtils.hasText(request.getStatus()) && !"ALL".equals(request.getStatus())) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            // 所属的代理商
            if (request.getAgentId() != null) {
                predicates.add(cb.equal(root.get("agentId"), request.getAgentId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public UsdtFuturesOrderManagement getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    @Transactional
    public void forceCancelOrder(Long orderId) {
        UsdtFuturesOrderManagement order = repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("只能撤销委托中的订单");
        }
        
        // 这里应该调用实际的合约订单服务来强制撤销订单
        // 然后更新管理表的状态
        order.setStatus("CANCELLED");
        order.setCancelledAt(java.time.LocalDateTime.now());
        repository.save(order);
    }
}














