/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.Blacklist;
import com.cryptotrade.systemmanagement.repository.BlacklistRepository;
import com.cryptotrade.systemmanagement.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    
    @Autowired
    private BlacklistRepository repository;
    
    @Override
    @Transactional
    public Blacklist createBlacklist(Blacklist blacklist) {
        return repository.save(blacklist);
    }
    
    @Override
    @Transactional
    public Blacklist updateBlacklist(Long id, Blacklist blacklist) {
        Blacklist existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("黑名单记录不存在"));
        blacklist.setId(id);
        blacklist.setCreatedAt(existing.getCreatedAt());
        return repository.save(blacklist);
    }
    
    @Override
    @Transactional
    public void deleteBlacklist(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public Blacklist getBlacklistById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("黑名单记录不存在"));
    }
    
    @Override
    public boolean isBlacklisted(String blacklistType, String blacklistValue) {
        return repository.findActiveBlacklist(blacklistType, blacklistValue, LocalDateTime.now()).isPresent();
    }
    
    @Override
    public List<Blacklist> getAllBlacklists() {
        return repository.findAll();
    }
    
    @Override
    public List<Blacklist> getBlacklistsByType(String blacklistType) {
        return repository.findByBlacklistType(blacklistType);
    }
    
    @Override
    @Transactional
    public void removeExpiredBlacklists() {
        List<Blacklist> expired = repository.findAll().stream()
                .filter(b -> b.getExpireTime() != null && b.getExpireTime().isBefore(LocalDateTime.now()))
                .filter(b -> "ACTIVE".equals(b.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        
        expired.forEach(b -> {
            b.setStatus("EXPIRED");
            repository.save(b);
        });
    }
}














