/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.repository;

import com.cryptotrade.community.entity.CommunityContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社区内容Repository
 */
@Repository
public interface CommunityContentRepository extends JpaRepository<CommunityContent, Long> {
    /**
     * 根据用户ID查询
     */
    List<CommunityContent> findByUserIdAndStatus(Long userId, String status);

    /**
     * 根据分类查询
     */
    List<CommunityContent> findByCategoryAndStatus(String category, String status);

    /**
     * 查询置顶内容
     */
    List<CommunityContent> findByIsTopAndStatus(Boolean isTop, String status);

    /**
     * 根据状态查询
     */
    List<CommunityContent> findByStatus(String status);
}




