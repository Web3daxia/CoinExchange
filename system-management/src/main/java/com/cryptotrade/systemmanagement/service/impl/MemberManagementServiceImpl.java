/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.UpdateMemberInfoRequest;
import com.cryptotrade.systemmanagement.entity.Blacklist;
import com.cryptotrade.systemmanagement.entity.MemberManagement;
import com.cryptotrade.systemmanagement.repository.MemberManagementRepository;
import com.cryptotrade.systemmanagement.service.BlacklistService;
import com.cryptotrade.systemmanagement.service.MemberManagementService;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.entity.UserDevice;
import com.cryptotrade.user.repository.UserDeviceRepository;
import com.cryptotrade.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 会员管理Service实现
 */
@Service
public class MemberManagementServiceImpl implements MemberManagementService {

    @Autowired
    private MemberManagementRepository memberManagementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BlacklistService blacklistService;

    @Override
    public Page<MemberManagement> getAllMembers(Pageable pageable) {
        return memberManagementRepository.findAll(pageable);
    }

    @Override
    public Optional<MemberManagement> getMemberById(Long id) {
        return memberManagementRepository.findById(id);
    }

    @Override
    public Optional<MemberManagement> getMemberByUserId(Long userId) {
        return memberManagementRepository.findByUserId(userId);
    }

    @Override
    public Page<MemberManagement> searchMembers(String keyword, String tradingStatus, String userStatus, Pageable pageable) {
        Specification<MemberManagement> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                Predicate keywordPredicate = cb.or(
                        cb.like(root.get("memberName"), "%" + keyword + "%"),
                        cb.like(root.get("memberUid"), "%" + keyword + "%"),
                        cb.like(root.get("email"), "%" + keyword + "%"),
                        cb.like(root.get("phone"), "%" + keyword + "%")
                );
                predicates.add(keywordPredicate);
            }

            if (tradingStatus != null && !tradingStatus.isEmpty()) {
                predicates.add(cb.equal(root.get("tradingStatus"), tradingStatus));
            }

            if (userStatus != null && !userStatus.isEmpty()) {
                predicates.add(cb.equal(root.get("userStatus"), userStatus));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return memberManagementRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public MemberManagement updateMemberInfo(Long userId, UpdateMemberInfoRequest request) {
        MemberManagement member = memberManagementRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getCountry() != null) {
            member.setCountry(request.getCountry());
        }

        return memberManagementRepository.save(member);
    }

    @Override
    @Transactional
    public void updateLoginPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateAssetPassword(Long userId, String newPassword) {
        // 资产管理密码通常存储在users表的private_key_hash字段或单独字段
        // 这里使用private_key_hash存储资产管理密码的哈希
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPrivateKeyHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteGoogleAuth(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setTwoFaEnabled(false);
        user.setTwoFaSecret(null);
        userRepository.save(user);

        MemberManagement member = memberManagementRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        member.setGoogleAuthEnabled(false);
        memberManagementRepository.save(member);
    }

    @Override
    @Transactional
    public void disableDevice(Long userId, String deviceFingerprint) {
        Optional<UserDevice> deviceOpt = userDeviceRepository.findByUserIdAndDeviceFingerprint(userId, deviceFingerprint);
        if (deviceOpt.isPresent()) {
            UserDevice device = deviceOpt.get();
            device.setIsActive(false);
            userDeviceRepository.save(device);
        } else {
            throw new RuntimeException("设备不存在");
        }
    }

    @Override
    @Transactional
    public void disableIp(Long userId, String ip) {
        // IP禁用通过黑名单实现
        Blacklist blacklist = new Blacklist();
        blacklist.setBlacklistType("IP");
        blacklist.setBlacklistValue(ip);
        blacklist.setReason("管理员禁用用户IP: " + userId);
        blacklist.setStatus("ACTIVE");
        blacklistService.createBlacklist(blacklist);
    }

    @Override
    @Transactional
    public MemberManagement updateTradingStatus(Long userId, String tradingStatus) {
        MemberManagement member = memberManagementRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        member.setTradingStatus(tradingStatus);
        return memberManagementRepository.save(member);
    }

    @Override
    @Transactional
    public MemberManagement updateUserStatus(Long userId, String userStatus) {
        MemberManagement member = memberManagementRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        member.setUserStatus(userStatus);

        // 同步更新users表的account_status
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if ("DISABLED".equals(userStatus)) {
            user.setAccountStatus("DISABLED");
        } else if ("NORMAL".equals(userStatus)) {
            user.setAccountStatus("ACTIVE");
        }
        userRepository.save(user);

        return memberManagementRepository.save(member);
    }

    @Override
    @Transactional
    public MemberManagement updateWithdrawStatus(Long userId, String withdrawStatus) {
        MemberManagement member = memberManagementRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        member.setWithdrawStatus(withdrawStatus);
        return memberManagementRepository.save(member);
    }
}
