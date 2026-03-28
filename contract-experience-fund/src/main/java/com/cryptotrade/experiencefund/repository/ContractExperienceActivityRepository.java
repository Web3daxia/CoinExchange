/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.repository;

import com.cryptotrade.experiencefund.entity.ContractExperienceActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 合约体验金发放活动Repository
 */
@Repository
public interface ContractExperienceActivityRepository extends JpaRepository<ContractExperienceActivity, Long> {
    
    Optional<ContractExperienceActivity> findByActivityCode(String activityCode);
    
    List<ContractExperienceActivity> findByStatus(String status);
    
    List<ContractExperienceActivity> findByTargetUsers(String targetUsers);
}














