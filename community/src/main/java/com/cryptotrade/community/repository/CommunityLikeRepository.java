/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.repository;

import com.cryptotrade.community.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 社区点赞Repository
 */
@Repository
public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    /**
     * 查询用户是否已点赞
     */
    Optional<CommunityLike> findByContentIdAndUserId(Long contentId, Long userId);

    /**
     * 统计内容点赞数
     */
    long countByContentId(Long contentId);
}















