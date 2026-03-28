/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.repository;

import com.cryptotrade.user.entity.IpWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpWhitelistRepository extends JpaRepository<IpWhitelist, Long> {
    List<IpWhitelist> findByUserIdAndIsEnabledTrue(Long userId);
}















