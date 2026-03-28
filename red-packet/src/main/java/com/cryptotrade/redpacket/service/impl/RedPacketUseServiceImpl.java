/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service.impl;

import com.cryptotrade.redpacket.dto.request.RedPacketUseRequest;
import com.cryptotrade.redpacket.entity.RedPacketReceive;
import com.cryptotrade.redpacket.entity.RedPacketUse;
import com.cryptotrade.redpacket.repository.RedPacketReceiveRepository;
import com.cryptotrade.redpacket.repository.RedPacketUseRepository;
import com.cryptotrade.redpacket.service.RedPacketUseService;
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
 * 红包使用Service实现
 */
@Service
public class RedPacketUseServiceImpl implements RedPacketUseService {

    @Autowired
    private RedPacketUseRepository useRepository;

    @Autowired
    private RedPacketReceiveRepository receiveRepository;

    @Override
    @Transactional
    public RedPacketUse usePacket(Long userId, RedPacketUseRequest request) {
        // 获取红包领取记录
        RedPacketReceive receive = receiveRepository.findById(request.getPacketId())
                .orElseThrow(() -> new RuntimeException("红包不存在: " + request.getPacketId()));

        // 检查用户权限
        if (!receive.getUserId().equals(userId)) {
            throw new RuntimeException("无权使用此红包");
        }

        // 检查红包状态
        if (!"VALID".equals(receive.getStatus())) {
            throw new RuntimeException("红包状态不允许使用: " + receive.getStatus());
        }

        // 检查是否过期
        if (LocalDateTime.now().isAfter(receive.getExpireTime())) {
            // 更新红包状态为已过期
            receive.setStatus("EXPIRED");
            receiveRepository.save(receive);
            throw new RuntimeException("红包已过期");
        }

        // 检查使用次数
        if (receive.getMaxUseCount() != null && receive.getUseCount() >= receive.getMaxUseCount()) {
            throw new RuntimeException("红包已达到最大使用次数");
        }

        // 检查剩余金额
        if (request.getUseAmount().compareTo(receive.getRemainingAmount()) > 0) {
            throw new RuntimeException("红包剩余金额不足");
        }

        BigDecimal beforeAmount = receive.getRemainingAmount();
        BigDecimal afterAmount = beforeAmount.subtract(request.getUseAmount());

        // 创建使用记录
        RedPacketUse use = new RedPacketUse();
        use.setPacketId(receive.getId());
        use.setUserId(userId);
        use.setActivityId(receive.getActivityId());
        use.setPacketNo(receive.getPacketNo());
        use.setUseAmount(request.getUseAmount());
        use.setUseType(request.getUseType());
        use.setOrderNo(request.getOrderNo());
        use.setUseTarget(request.getUseTarget());
        use.setBeforeAmount(beforeAmount);
        use.setAfterAmount(afterAmount);
        use.setUseTime(LocalDateTime.now());

        RedPacketUse savedUse = useRepository.save(use);

        // 更新红包状态
        receive.setRemainingAmount(afterAmount);
        receive.setUseCount(receive.getUseCount() + 1);

        if (afterAmount.compareTo(BigDecimal.ZERO) <= 0) {
            receive.setStatus("USED");
        }

        receiveRepository.save(receive);

        // TODO: 执行实际的使用操作（如抵扣手续费、发放现金等）

        return savedUse;
    }

    @Override
    public RedPacketUse getUseById(Long useId) {
        return useRepository.findById(useId)
                .orElseThrow(() -> new RuntimeException("红包使用记录不存在: " + useId));
    }

    @Override
    public List<RedPacketUse> getPacketUses(Long packetId) {
        return useRepository.findByPacketId(packetId);
    }

    @Override
    public List<RedPacketUse> getUserUses(Long userId) {
        return useRepository.findByUserId(userId);
    }

    @Override
    public Page<RedPacketUse> getUserUses(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return useRepository.findByUserId(userId, pageable);
    }
}














