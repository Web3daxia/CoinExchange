/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.SpotOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.SpotOrderManagement;
import com.cryptotrade.systemmanagement.repository.SpotOrderManagementRepository;
import com.cryptotrade.systemmanagement.service.SpotOrderManagementService;
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
 * 现货订单管理Service实现
 */
@Service
public class SpotOrderManagementServiceImpl implements SpotOrderManagementService {

    @Autowired
    private SpotOrderManagementRepository repository;

    @Override
    public Page<SpotOrderManagement> searchCurrentOrders(SpotOrderSearchRequest request) {
        Specification<SpotOrderManagement> spec = buildSearchSpecification(request);
        // 当前委托：状态为PENDING的订单
        Specification<SpotOrderManagement> currentSpec = spec.and((root, query, cb) -> 
            cb.equal(root.get("status"), "PENDING"));
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.findAll(currentSpec, pageable);
    }

    @Override
    public Page<SpotOrderManagement> searchHistoryOrders(SpotOrderSearchRequest request) {
        Specification<SpotOrderManagement> spec = buildSearchSpecification(request);
        // 历史委托：状态不为PENDING的订单
        Specification<SpotOrderManagement> historySpec = spec.and((root, query, cb) -> 
            cb.notEqual(root.get("status"), "PENDING"));
        
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.findAll(historySpec, pageable);
    }

    /**
     * 构建搜索条件
     */
    private Specification<SpotOrderManagement> buildSearchSpecification(SpotOrderSearchRequest request) {
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

            // 交易币种
            if (StringUtils.hasText(request.getBaseCurrency())) {
                predicates.add(cb.equal(root.get("baseCurrency"), request.getBaseCurrency()));
            }

            // 结算币种
            if (StringUtils.hasText(request.getQuoteCurrency())) {
                predicates.add(cb.equal(root.get("quoteCurrency"), request.getQuoteCurrency()));
            }

            // 委托单号
            if (StringUtils.hasText(request.getOrderNo())) {
                predicates.add(cb.like(root.get("orderNo"), "%" + request.getOrderNo() + "%"));
            }

            // 成交时间范围
            if (request.getFilledAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("filledAt"), request.getFilledAtStart()));
            }
            if (request.getFilledAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("filledAt"), request.getFilledAtEnd()));
            }

            // 交易对
            if (StringUtils.hasText(request.getPairName())) {
                predicates.add(cb.equal(root.get("pairName"), request.getPairName()));
            }

            // 订单状态
            if (StringUtils.hasText(request.getStatus()) && !"ALL".equals(request.getStatus())) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            // 订单方向
            if (StringUtils.hasText(request.getSide()) && !"ALL".equals(request.getSide())) {
                predicates.add(cb.equal(root.get("side"), request.getSide()));
            }

            // 挂单类型
            if (StringUtils.hasText(request.getOrderType()) && !"ALL".equals(request.getOrderType())) {
                predicates.add(cb.equal(root.get("orderType"), request.getOrderType()));
            }

            // 订单来源
            if (StringUtils.hasText(request.getOrderSource()) && !"ALL".equals(request.getOrderSource())) {
                if ("USER".equals(request.getOrderSource())) {
                    predicates.add(cb.equal(root.get("orderSource"), "USER"));
                } else if ("NOT_USER".equals(request.getOrderSource())) {
                    predicates.add(cb.notEqual(root.get("orderSource"), "USER"));
                } else if ("ROBOT".equals(request.getOrderSource())) {
                    predicates.add(cb.equal(root.get("orderSource"), "ROBOT"));
                } else if ("NOT_ROBOT".equals(request.getOrderSource())) {
                    predicates.add(cb.notEqual(root.get("orderSource"), "ROBOT"));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SpotOrderManagement getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        SpotOrderManagement order = repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("只能撤销交易中的订单");
        }
        
        // 这里应该调用实际的订单服务来撤销订单
        // 然后更新管理表的状态
        order.setStatus("CANCELLED");
        order.setCancelledAt(java.time.LocalDateTime.now());
        repository.save(order);
    }

    @Override
    public SpotOrderManagement getOrderDetail(Long id) {
        return getOrderById(id);
    }
}














