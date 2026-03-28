/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.CoinFuturesPositionSearchRequest;
import com.cryptotrade.systemmanagement.entity.CoinFuturesPositionManagement;
import com.cryptotrade.systemmanagement.repository.CoinFuturesPositionManagementRepository;
import com.cryptotrade.systemmanagement.service.CoinFuturesPositionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 币本位永续合约仓位管理Service实现
 */
@Service
public class CoinFuturesPositionManagementServiceImpl implements CoinFuturesPositionManagementService {

    @Autowired
    private CoinFuturesPositionManagementRepository repository;

    @Override
    public Page<CoinFuturesPositionManagement> searchPositions(CoinFuturesPositionSearchRequest request) {
        Specification<CoinFuturesPositionManagement> spec = buildSearchSpecification(request);
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.findAll(spec, pageable);
    }

    /**
     * 构建搜索条件
     */
    private Specification<CoinFuturesPositionManagement> buildSearchSpecification(CoinFuturesPositionSearchRequest request) {
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

            // 可用余额大于
            if (request.getAvailableBalanceGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("availableBalance"), request.getAvailableBalanceGreaterThan()));
            }

            // 冻结余额大于
            if (request.getFrozenBalanceGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("frozenBalance"), request.getFrozenBalanceGreaterThan()));
            }

            // 多单杠杆大于
            if (request.getLongLeverageGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("longLeverage"), request.getLongLeverageGreaterThan()));
            }

            // 多单仓位大于
            if (request.getLongPositionGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("longPosition"), request.getLongPositionGreaterThan()));
            }

            // 冻结多仓大于
            if (request.getLongFrozenGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("longFrozen"), request.getLongFrozenGreaterThan()));
            }

            // 多单保证金大于
            if (request.getLongMarginGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("longMargin"), request.getLongMarginGreaterThan()));
            }

            // 空单杠杆大于
            if (request.getShortLeverageGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("shortLeverage"), request.getShortLeverageGreaterThan()));
            }

            // 空单仓位大于
            if (request.getShortPositionGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("shortPosition"), request.getShortPositionGreaterThan()));
            }

            // 冻结空仓大于
            if (request.getShortFrozenGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("shortFrozen"), request.getShortFrozenGreaterThan()));
            }

            // 空单保证金大于
            if (request.getShortMarginGreaterThan() != null) {
                predicates.add(cb.greaterThan(root.get("shortMargin"), request.getShortMarginGreaterThan()));
            }

            // 合约交易对
            if (StringUtils.hasText(request.getPairName())) {
                predicates.add(cb.equal(root.get("pairName"), request.getPairName()));
            }

            // 仓位模式
            if (StringUtils.hasText(request.getMarginMode()) && !"ALL".equals(request.getMarginMode())) {
                predicates.add(cb.equal(root.get("marginMode"), request.getMarginMode()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public CoinFuturesPositionManagement getPositionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));
    }
}














