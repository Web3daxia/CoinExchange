/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByBlacklistTypeAndBlacklistValue(String blacklistType, String blacklistValue);
    
    List<Blacklist> findByBlacklistType(String blacklistType);
    
    List<Blacklist> findByStatus(String status);
    
    @Query("SELECT b FROM Blacklist b WHERE b.blacklistType = :type " +
           "AND b.blacklistValue = :value AND b.status = 'ACTIVE' " +
           "AND (b.expireTime IS NULL OR b.expireTime > :now)")
    Optional<Blacklist> findActiveBlacklist(@Param("type") String type,
                                            @Param("value") String value,
                                            @Param("now") LocalDateTime now);
}

