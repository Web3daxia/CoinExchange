/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.repository;

import com.cryptotrade.academy.entity.AcademyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学院评论Repository
 */
@Repository
public interface AcademyCommentRepository extends JpaRepository<AcademyComment, Long> {
    /**
     * 根据内容ID查询评论
     */
    List<AcademyComment> findByContentIdAndStatus(Long contentId, String status);

    /**
     * 根据父评论ID查询回复
     */
    List<AcademyComment> findByParentId(Long parentId);
}















