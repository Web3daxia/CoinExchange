/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.repository;

import com.cryptotrade.user.entity.OAuthBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuthBindingRepository extends JpaRepository<OAuthBinding, Long> {
    List<OAuthBinding> findByUserIdAndIsActiveTrue(Long userId);
    Optional<OAuthBinding> findByProviderAndOauthId(String provider, String oauthId);
    Optional<OAuthBinding> findByUserIdAndProvider(Long userId, String provider);
}















