/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网格机器人参数调整历史实体
 */
@Entity
@Table(name = "grid_robot_adjustment_histories")
@Data
@EntityListeners(AuditingEntityListener.class)
public class GridRobotAdjustmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "robot_id", nullable = false)
    private Long robotId; // 机器人ID

    @Column(name = "adjust_type", nullable = false)
    private String adjustType; // AUTO, MANUAL

    @Column(name = "old_grid_count")
    private Integer oldGridCount;

    @Column(name = "new_grid_count")
    private Integer newGridCount;

    @Column(name = "old_upper_price", precision = 20, scale = 8)
    private BigDecimal oldUpperPrice;

    @Column(name = "new_upper_price", precision = 20, scale = 8)
    private BigDecimal newUpperPrice;

    @Column(name = "old_lower_price", precision = 20, scale = 8)
    private BigDecimal oldLowerPrice;

    @Column(name = "new_lower_price", precision = 20, scale = 8)
    private BigDecimal newLowerPrice;

    @Column(name = "old_start_price", precision = 20, scale = 8)
    private BigDecimal oldStartPrice;

    @Column(name = "new_start_price", precision = 20, scale = 8)
    private BigDecimal newStartPrice;

    @Column(name = "adjustment_reason", columnDefinition = "TEXT")
    private String adjustmentReason; // 调整原因

    @Column(name = "adjustment_params", columnDefinition = "TEXT")
    private String adjustmentParams; // JSON格式存储调整参数

    @CreatedDate
    @Column(name = "adjusted_at")
    private LocalDateTime adjustedAt;
}













