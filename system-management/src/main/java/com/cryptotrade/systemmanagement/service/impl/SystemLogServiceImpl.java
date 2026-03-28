/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.SystemLogSearchRequest;
import com.cryptotrade.systemmanagement.entity.SystemLog;
import com.cryptotrade.systemmanagement.repository.SystemLogRepository;
import com.cryptotrade.systemmanagement.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统日志Service实现
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    @Transactional
    public void saveLog(SystemLog log) {
        systemLogRepository.save(log);
    }

    @Override
    public Page<SystemLog> searchLogs(SystemLogSearchRequest request) {
        Specification<SystemLog> spec = buildSearchSpecification(request);
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return systemLogRepository.findAll(spec, pageable);
    }

    /**
     * 构建搜索条件
     */
    private Specification<SystemLog> buildSearchSpecification(SystemLogSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 操作用户ID
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), request.getUserId()));
            }

            // 操作用户名
            if (StringUtils.hasText(request.getUsername())) {
                predicates.add(cb.like(root.get("username"), "%" + request.getUsername() + "%"));
            }

            // 操作事务
            if (StringUtils.hasText(request.getOperation())) {
                predicates.add(cb.like(root.get("operation"), "%" + request.getOperation() + "%"));
            }

            // 操作模块
            if (StringUtils.hasText(request.getModule())) {
                predicates.add(cb.equal(root.get("module"), request.getModule()));
            }

            // 请求方法
            if (StringUtils.hasText(request.getMethod())) {
                predicates.add(cb.equal(root.get("method"), request.getMethod()));
            }

            // IP地址
            if (StringUtils.hasText(request.getIpAddress())) {
                predicates.add(cb.like(root.get("ipAddress"), "%" + request.getIpAddress() + "%"));
            }

            // 设备号
            if (StringUtils.hasText(request.getDeviceId())) {
                predicates.add(cb.like(root.get("deviceId"), "%" + request.getDeviceId() + "%"));
            }

            // 地区
            if (StringUtils.hasText(request.getRegion())) {
                predicates.add(cb.like(root.get("region"), "%" + request.getRegion() + "%"));
            }

            // 操作状态
            if (StringUtils.hasText(request.getStatus()) && !"ALL".equals(request.getStatus())) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            // 时间范围
            if (request.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getEndTime()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SystemLog getLogById(Long id) {
        return systemLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("日志不存在: " + id));
    }

    @Override
    public Page<SystemLog> getLogsByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return systemLogRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("userId"), userId),
                pageable
        );
    }

    @Override
    public Page<SystemLog> getLogsByModule(String module, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return systemLogRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("module"), module),
                pageable
        );
    }

    @Override
    @Transactional
    public void deleteOldLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        List<SystemLog> oldLogs = systemLogRepository.findAll(
                (root, query, cb) -> cb.lessThan(root.get("createdAt"), cutoffDate)
        );
        systemLogRepository.deleteAll(oldLogs);
    }
}














