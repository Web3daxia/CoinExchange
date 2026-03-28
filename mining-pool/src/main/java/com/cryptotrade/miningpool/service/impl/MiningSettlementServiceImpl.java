/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service.impl;

import com.cryptotrade.miningpool.entity.MiningPool;
import com.cryptotrade.miningpool.entity.MiningSettlement;
import com.cryptotrade.miningpool.entity.MiningWorker;
import com.cryptotrade.miningpool.repository.MiningPoolRepository;
import com.cryptotrade.miningpool.repository.MiningSettlementRepository;
import com.cryptotrade.miningpool.repository.MiningWorkerRepository;
import com.cryptotrade.miningpool.service.MiningSettlementService;
import com.cryptotrade.miningpool.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 挖矿收益结算Service实现
 */
@Service
public class MiningSettlementServiceImpl implements MiningSettlementService {

    @Autowired
    private MiningSettlementRepository settlementRepository;

    @Autowired
    private MiningWorkerRepository workerRepository;

    @Autowired
    private MiningPoolRepository poolRepository;

    @Override
    @Transactional
    public List<MiningSettlement> settlePoolRevenue(Long poolId, BigDecimal poolRevenue, String currency) {
        MiningPool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));

        // 获取矿池的所有活跃矿工
        List<MiningWorker> workers = workerRepository.findByPoolIdAndStatus(poolId, "ACTIVE");

        if (workers.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算总算力
        BigDecimal totalHashrate = pool.getTotalHashrate();
        if (totalHashrate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("矿池总算力为0，无法分配收益");
        }

        LocalDateTime now = LocalDateTime.now();
        List<MiningSettlement> settlements = new ArrayList<>();

        // 根据分配方式分配收益
        String distributionMethod = pool.getDistributionMethod();
        for (MiningWorker worker : workers) {
            BigDecimal userRevenue;
            BigDecimal distributionRate;

            if ("PPS".equals(distributionMethod)) {
                // PPS: 按股份支付，根据算力比例分配
                distributionRate = worker.getHashrateContribution().divide(totalHashrate, 6, BigDecimal.ROUND_HALF_UP);
                userRevenue = poolRevenue.multiply(distributionRate);
            } else if ("PPLNS".equals(distributionMethod)) {
                // PPLNS: 按最后N股份，这里简化处理为按算力比例
                distributionRate = worker.getHashrateContribution().divide(totalHashrate, 6, BigDecimal.ROUND_HALF_UP);
                userRevenue = poolRevenue.multiply(distributionRate);
            } else {
                // PROP: 按比例分配
                distributionRate = worker.getHashrateContribution().divide(totalHashrate, 6, BigDecimal.ROUND_HALF_UP);
                userRevenue = poolRevenue.multiply(distributionRate);
            }

            // 创建结算记录
            MiningSettlement settlement = new MiningSettlement();
            settlement.setWorkerId(worker.getId());
            settlement.setUserId(worker.getUserId());
            settlement.setPoolId(poolId);
            settlement.setSettlementPeriodStart(worker.getLastSettlementTime() != null ? 
                    worker.getLastSettlementTime() : worker.getJoinTime());
            settlement.setSettlementPeriodEnd(now);
            settlement.setHashrateContribution(worker.getHashrateContribution());
            settlement.setPoolTotalHashrate(totalHashrate);
            settlement.setPoolRevenue(poolRevenue);
            settlement.setUserRevenue(userRevenue);
            settlement.setCurrency(currency);
            settlement.setDistributionRate(distributionRate);
            settlement.setSettlementType("BOTH");
            settlement.setSettlementStatus("SETTLED");
            settlement.setSettlementTime(now);
            settlement.setSettlementOrderNo(OrderNoGenerator.generateSettlementOrderNo());

            settlements.add(settlementRepository.save(settlement));

            // 更新矿工收益
            worker.setTotalRevenue(worker.getTotalRevenue().add(userRevenue));
            worker.setAccumulatedRevenue(worker.getAccumulatedRevenue().add(userRevenue));
            worker.setLastSettlementTime(now);
            workerRepository.save(worker);
        }

        // 更新矿池收益
        pool.setPoolRevenue(pool.getPoolRevenue().add(poolRevenue));
        poolRepository.save(pool);

        // TODO: 将收益发放到用户账户

        return settlements;
    }

    @Override
    @Transactional
    public List<MiningSettlement> batchSettlePoolRevenue(String settlementCycle) {
        List<MiningSettlement> allSettlements = new ArrayList<>();

        // 获取需要结算的矿池
        List<MiningPool> pools = poolRepository.findByStatus("ACTIVE");
        for (MiningPool pool : pools) {
            if (!settlementCycle.equals(pool.getSettlementCycle())) {
                continue;
            }

            // TODO: 从实际挖矿结果中获取收益，这里使用示例值
            BigDecimal poolRevenue = BigDecimal.valueOf(100); // 示例收益
            String currency = pool.getMiningCurrency();

            try {
                List<MiningSettlement> settlements = settlePoolRevenue(pool.getId(), poolRevenue, currency);
                allSettlements.addAll(settlements);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return allSettlements;
    }

    @Override
    public List<MiningSettlement> getWorkerSettlements(Long workerId) {
        return settlementRepository.findByWorkerId(workerId);
    }

    @Override
    public Page<MiningSettlement> getUserSettlements(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return settlementRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<MiningSettlement> getPoolSettlements(Long poolId) {
        return settlementRepository.findByPoolId(poolId);
    }

    @Override
    public List<MiningSettlement> getPendingSettlements() {
        return settlementRepository.findBySettlementStatus("PENDING");
    }

    @Override
    public BigDecimal calculateUserRevenue(Long workerId, BigDecimal poolRevenue) {
        MiningWorker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("矿工记录不存在: " + workerId));

        MiningPool pool = poolRepository.findById(worker.getPoolId())
                .orElseThrow(() -> new RuntimeException("矿池不存在"));

        BigDecimal totalHashrate = pool.getTotalHashrate();
        if (totalHashrate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal distributionRate = worker.getHashrateContribution().divide(totalHashrate, 6, BigDecimal.ROUND_HALF_UP);
        return poolRevenue.multiply(distributionRate);
    }
}














