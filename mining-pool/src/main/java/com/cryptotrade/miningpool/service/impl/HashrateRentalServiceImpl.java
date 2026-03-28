/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service.impl;

import com.cryptotrade.miningpool.dto.request.HashrateRentalRequest;
import com.cryptotrade.miningpool.entity.HashrateRental;
import com.cryptotrade.miningpool.entity.HashrateType;
import com.cryptotrade.miningpool.entity.MiningPool;
import com.cryptotrade.miningpool.repository.HashrateRentalRepository;
import com.cryptotrade.miningpool.repository.HashrateTypeRepository;
import com.cryptotrade.miningpool.repository.MiningPoolRepository;
import com.cryptotrade.miningpool.service.HashrateRentalService;
import com.cryptotrade.miningpool.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 算力租赁Service实现
 */
@Service
public class HashrateRentalServiceImpl implements HashrateRentalService {

    @Autowired
    private HashrateRentalRepository rentalRepository;

    @Autowired
    private MiningPoolRepository poolRepository;

    @Autowired
    private HashrateTypeRepository hashrateTypeRepository;

    @Override
    @Transactional
    public HashrateRental rentHashrate(Long userId, HashrateRentalRequest request) {
        // 获取矿池
        MiningPool pool = poolRepository.findById(request.getPoolId())
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + request.getPoolId()));

        // 检查矿池状态
        if (!"ACTIVE".equals(pool.getStatus())) {
            throw new RuntimeException("矿池不可用，当前状态: " + pool.getStatus());
        }

        // 检查最低算力门槛
        if (pool.getMinHashrate() != null && 
            request.getHashrateAmount().compareTo(pool.getMinHashrate()) < 0) {
            throw new RuntimeException("算力不能小于最低算力门槛: " + pool.getMinHashrate());
        }

        // 检查最大参与用户数
        if (pool.getMaxParticipants() != null && 
            pool.getCurrentParticipants() >= pool.getMaxParticipants()) {
            throw new RuntimeException("矿池已达到最大参与用户数");
        }

        // 获取算力类型
        HashrateType hashrateType = hashrateTypeRepository.findById(request.getHashrateTypeId())
                .orElseThrow(() -> new RuntimeException("算力类型不存在: " + request.getHashrateTypeId()));

        // 计算租赁费用
        BigDecimal totalCost = calculateRentalCost(request.getPoolId(), request.getHashrateAmount(), request.getRentalPeriod());

        // TODO: 检查用户余额并冻结资金

        // 创建租赁记录
        HashrateRental rental = new HashrateRental();
        rental.setUserId(userId);
        rental.setPoolId(request.getPoolId());
        rental.setHashrateTypeId(request.getHashrateTypeId());
        rental.setHashrateAmount(request.getHashrateAmount());
        rental.setUnit(hashrateType.getUnit());
        rental.setRentalPeriod(request.getRentalPeriod());
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(request.getRentalPeriod()));
        rental.setTotalCost(totalCost);
        rental.setPaidAmount(totalCost); // 假设已支付
        rental.setCurrency(request.getCurrency());
        rental.setStatus("ACTIVE");
        rental.setRentalOrderNo(OrderNoGenerator.generateRentalOrderNo());

        HashrateRental savedRental = rentalRepository.save(rental);

        // 更新矿池参与用户数
        pool.setCurrentParticipants(pool.getCurrentParticipants() + 1);
        poolRepository.save(pool);

        // TODO: 调用矿工服务，将算力分配到矿池

        return savedRental;
    }

    @Override
    public HashrateRental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("租赁记录不存在: " + rentalId));
    }

    @Override
    public HashrateRental getRentalByOrderNo(String orderNo) {
        return rentalRepository.findByRentalOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("租赁记录不存在: " + orderNo));
    }

    @Override
    public List<HashrateRental> getUserRentals(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    @Override
    public Page<HashrateRental> getUserRentals(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return rentalRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<HashrateRental> getPoolRentals(Long poolId) {
        return rentalRepository.findByPoolId(poolId);
    }

    @Override
    public List<HashrateRental> getActiveRentals(Long userId) {
        return rentalRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    @Override
    public BigDecimal calculateRentalCost(Long poolId, BigDecimal hashrateAmount, Integer rentalPeriod) {
        MiningPool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));

        // 计算费用：算力数量 * 算力价格 * 租赁天数
        return hashrateAmount.multiply(pool.getHashratePrice()).multiply(BigDecimal.valueOf(rentalPeriod));
    }
}














