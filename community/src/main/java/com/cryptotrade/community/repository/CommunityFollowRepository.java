/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.repository;

import com.cryptotrade.community.entity.CommunityFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社区关注Repository
 */
@Repository
public interface CommunityFollowRepository extends JpaRepository<CommunityFollow, Long> {
    /**
     * 查询是否已关注
     */
    Optional<CommunityFollow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * 查询用户的关注列表
     */
    List<CommunityFollow> findByFollowerId(Long followerId);

    /**
     * 查询用户的粉丝列表
     */
    List<CommunityFollow> findByFollowingId(Long followingId);
}















