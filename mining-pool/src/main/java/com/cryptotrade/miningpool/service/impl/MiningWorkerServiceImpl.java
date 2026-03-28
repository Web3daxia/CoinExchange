/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service.impl;

import com.cryptotrade.miningpool.entity.MiningPool;
import com.cryptotrade.miningpool.entity.MiningWorker;
import com.cryptotrade.miningpool.repository.MiningPoolRepository;
import com.cryptotrade.miningpool.repository.MiningWorkerRepository;
import com.cryptotrade.miningpool.service.MiningWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 矿工管理Service实现
 */
@Service
public class MiningWorkerServiceImpl implements MiningWorkerService {

    @Autowired
    private MiningWorkerRepository workerRepository;

    @Autowired
    private MiningPoolRepository poolRepository;

    @Override
    @Transactional
    public MiningWorker joinPool(Long userId, Long poolId, Long rentalId, BigDecimal hashrateAmount, String unit) {
        // 检查用户是否已在该矿池
        if (workerRepository.findByUserIdAndPoolId(userId, poolId).isPresent()) {
            throw new RuntimeException("用户已在该矿池中");
        }

        // 获取矿池
        MiningPool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));

        // 创建矿工记录
        MiningWorker worker = new MiningWorker();
        worker.setUserId(userId);
        worker.setPoolId(poolId);
        worker.setRentalId(rentalId);
        worker.setHashrateContribution(hashrateAmount);
        worker.setUnit(unit);
        worker.setJoinTime(LocalDateTime.now());
        worker.setStatus("ACTIVE");

        MiningWorker savedWorker = workerRepository.save(worker);

        // 更新矿池总算力
        pool.setTotalHashrate(pool.getTotalHashrate().add(hashrateAmount));
        pool.setActiveHashrate(pool.getActiveHashrate().add(hashrateAmount));
        poolRepository.save(pool);

        return savedWorker;
    }

    @Override
    @Transactional
    public void exitPool(Long userId, Long poolId) {
        MiningWorker worker = workerRepository.findByUserIdAndPoolId(userId, poolId)
                .orElseThrow(() -> new RuntimeException("矿工记录不存在"));

        // 更新矿工状态
        worker.setStatus("EXITED");
        workerRepository.save(worker);

        // 更新矿池算力
        MiningPool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new RuntimeException("矿池不存在: " + poolId));
        pool.setTotalHashrate(pool.getTotalHashrate().subtract(worker.getHashrateContribution()));
        pool.setActiveHashrate(pool.getActiveHashrate().subtract(worker.getHashrateContribution()));
        pool.setCurrentParticipants(Math.max(0, pool.getCurrentParticipants() - 1));
        poolRepository.save(pool);
    }

    @Override
    public MiningWorker getWorkerById(Long workerId) {
        return workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("矿工记录不存在: " + workerId));
    }

    @Override
    public MiningWorker getWorkerByUserAndPool(Long userId, Long poolId) {
        return workerRepository.findByUserIdAndPoolId(userId, poolId)
                .orElseThrow(() -> new RuntimeException("矿工记录不存在"));
    }

    @Override
    public List<MiningWorker> getUserWorkers(Long userId) {
        return workerRepository.findByUserId(userId);
    }

    @Override
    public List<MiningWorker> getPoolWorkers(Long poolId) {
        return workerRepository.findByPoolId(poolId);
    }

    @Override
    public List<MiningWorker> getActivePoolWorkers(Long poolId) {
        return workerRepository.findByPoolIdAndStatus(poolId, "ACTIVE");
    }

    @Override
    public List<MiningWorker> getPoolWorkerRanking(Long poolId, Integer limit) {
        List<MiningWorker> workers = workerRepository.findAllByPoolIdOrderByHashrateContributionDesc(poolId);
        if (limit != null && limit > 0 && workers.size() > limit) {
            return workers.subList(0, limit);
        }
        return workers;
    }

    @Override
    @Transactional
    public void updateWorkerHashrate(Long workerId, BigDecimal hashrateContribution) {
        MiningWorker worker = getWorkerById(workerId);
        MiningPool pool = poolRepository.findById(worker.getPoolId())
                .orElseThrow(() -> new RuntimeException("矿池不存在"));

        // 更新矿池算力
        BigDecimal oldHashrate = worker.getHashrateContribution();
        pool.setTotalHashrate(pool.getTotalHashrate().subtract(oldHashrate).add(hashrateContribution));
        pool.setActiveHashrate(pool.getActiveHashrate().subtract(oldHashrate).add(hashrateContribution));
        poolRepository.save(pool);

        // 更新矿工算力
        worker.setHashrateContribution(hashrateContribution);
        workerRepository.save(worker);
    }
}














