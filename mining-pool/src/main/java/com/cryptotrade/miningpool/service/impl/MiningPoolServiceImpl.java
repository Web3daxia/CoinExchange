/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service.impl;

import com.cryptotrade.miningpool.dto.request.MiningPoolCreateRequest;
import com.cryptotrade.miningpool.entity.MiningPool;
import com.cryptotrade.miningpool.repository.MiningPoolRepository;
import com.cryptotrade.miningpool.service.MiningPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 矿池管理Service实现
 */
@Service
public class MiningPoolServiceImpl implements MiningPoolService {

    @Autowired
    private MiningPoolRepository poolRepository;

    @Override
    @Transactional
    public MiningPool createPool(MiningPoolCreateRequest request) {
        // 检查矿池代码是否已存在
        if (poolRepository.findByPoolCode(request.getPoolCode()).isPresent()) {
            throw new RuntimeException("矿池代码已存在: " + request.getPoolCode());
        }

        MiningPool pool = new MiningPool();
        pool.setPoolName(request.getPoolName());
        pool.setPoolCode(request.getPoolCode());
        pool.setMiningCurrency(request.getMiningCurrency());
        pool.setAlgorithm(request.getAlgorithm());
        pool.setDistributionMethod(request.getDistributionMethod() != null ? request.getDistributionMethod() : "PPS");
        pool.setHashratePrice(request.getHashratePrice());
        pool.setMinHashrate(request.getMinHashrate());
        pool.setMaxParticipants(request.getMaxParticipants());
        pool.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        pool.setRiskLevel(request.getRiskLevel() != null ? request.getRiskLevel() : "MEDIUM");
        pool.setSettlementCycle(request.getSettlementCycle() != null ? request.getSettlementCycle() : "DAILY");
        pool.setDescription(request.getDescription());
        pool.setRiskWarning(request.getRiskWarning());
        pool.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        // 初始化
        pool.setTotalHashrate(BigDecimal.ZERO);
        pool.setActiveHashrate(BigDecimal.ZERO);
        pool.setPoolRevenue(BigDecimal.ZERO);
        pool.setCurrentParticipants(0);

        return poolRepository.save(pool);
    }

    @Override
    @Transactional
    public MiningPool updatePool(Long poolId, MiningPoolCreateRequest request) {
        MiningPool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));

        if (request.getPoolName() != null) {
            pool.setPoolName(request.getPoolName());
        }
        if (request.getPoolCode() != null && !pool.getPoolCode().equals(request.getPoolCode())) {
            if (poolRepository.findByPoolCode(request.getPoolCode()).isPresent()) {
                throw new RuntimeException("矿池代码已存在: " + request.getPoolCode());
            }
            pool.setPoolCode(request.getPoolCode());
        }
        if (request.getMiningCurrency() != null) {
            pool.setMiningCurrency(request.getMiningCurrency());
        }
        if (request.getAlgorithm() != null) {
            pool.setAlgorithm(request.getAlgorithm());
        }
        if (request.getDistributionMethod() != null) {
            pool.setDistributionMethod(request.getDistributionMethod());
        }
        if (request.getHashratePrice() != null) {
            pool.setHashratePrice(request.getHashratePrice());
        }
        if (request.getMinHashrate() != null) {
            pool.setMinHashrate(request.getMinHashrate());
        }
        if (request.getMaxParticipants() != null) {
            pool.setMaxParticipants(request.getMaxParticipants());
        }
        if (request.getStatus() != null) {
            pool.setStatus(request.getStatus());
        }
        if (request.getRiskLevel() != null) {
            pool.setRiskLevel(request.getRiskLevel());
        }
        if (request.getSettlementCycle() != null) {
            pool.setSettlementCycle(request.getSettlementCycle());
        }
        if (request.getDescription() != null) {
            pool.setDescription(request.getDescription());
        }
        if (request.getRiskWarning() != null) {
            pool.setRiskWarning(request.getRiskWarning());
        }
        if (request.getSortOrder() != null) {
            pool.setSortOrder(request.getSortOrder());
        }

        return poolRepository.save(pool);
    }

    @Override
    @Transactional
    public void deletePool(Long poolId) {
        if (!poolRepository.existsById(poolId)) {
            throw new RuntimeException("矿池不存在: " + poolId);
        }
        // TODO: 检查是否有用户参与，如果有则不允许删除
        poolRepository.deleteById(poolId);
    }

    @Override
    public MiningPool getPoolById(Long poolId) {
        return poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));
    }

    @Override
    public MiningPool getPoolByCode(String poolCode) {
        return poolRepository.findByPoolCode(poolCode)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolCode));
    }

    @Override
    public List<MiningPool> getAllPools() {
        return poolRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public List<MiningPool> getActivePools() {
        return poolRepository.findByStatus("ACTIVE");
    }

    @Override
    public List<MiningPool> getPoolsByCurrency(String currency) {
        return poolRepository.findByMiningCurrency(currency);
    }

    @Override
    public List<MiningPool> getPoolsByAlgorithm(String algorithm) {
        return poolRepository.findByAlgorithm(algorithm);
    }

    @Override
    @Transactional
    public void updatePoolHashrate(Long poolId, BigDecimal totalHashrate, BigDecimal activeHashrate) {
        MiningPool pool = getPoolById(poolId);
        pool.setTotalHashrate(totalHashrate);
        pool.setActiveHashrate(activeHashrate);
        poolRepository.save(pool);
    }

    @Override
    @Transactional
    public void updatePoolRevenue(Long poolId, BigDecimal revenue) {
        MiningPool pool = getPoolById(poolId);
        pool.setPoolRevenue(pool.getPoolRevenue().add(revenue));
        poolRepository.save(pool);
    }
}














