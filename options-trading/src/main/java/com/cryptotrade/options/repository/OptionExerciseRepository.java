/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionExerciseRepository extends JpaRepository<OptionExercise, Long> {
    List<OptionExercise> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<OptionExercise> findByPositionId(Long positionId);

    List<OptionExercise> findByStatus(String status);
}















