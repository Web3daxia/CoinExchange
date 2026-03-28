/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.repository;

import com.cryptotrade.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社区评论Repository
 */
@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    /**
     * 根据内容ID查询评论
     */
    List<CommunityComment> findByContentIdAndStatus(Long contentId, String status);
}















