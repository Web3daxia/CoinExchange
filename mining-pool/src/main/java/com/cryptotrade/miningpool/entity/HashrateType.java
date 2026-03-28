/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 算力类型实体
 */
@Entity
@Table(name = "hashrate_types")
@Data
@EntityListeners(AuditingEntityListener.class)
public class HashrateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName; // 算力类型名称（如: SHA-256, Ethash）

    @Column(name = "type_code", unique = true, nullable = false, length = 50)
    private String typeCode; // 算力类型代码

    @Column(name = "algorithm", nullable = false, length = 50)
    private String algorithm; // 挖矿算法

    @Column(name = "unit", nullable = false, length = 20)
    private String unit = "TH/s"; // 算力单位（如: TH/s, GH/s, MH/s）

    @Column(name = "description", length = 500)
    private String description; // 类型描述

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














