-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 系统管理员表结构迁移脚本
-- 从旧版本迁移到新版本
-- =====================================================

USE `coin`;

-- 备份旧表（如果存在）
CREATE TABLE IF NOT EXISTS `system_admins_backup` LIKE `system_admins`;

-- 如果旧表存在数据，先备份
INSERT INTO `system_admins_backup` SELECT * FROM `system_admins`;

-- 删除旧表
DROP TABLE IF EXISTS `system_admins`;

-- 创建新表结构
CREATE TABLE IF NOT EXISTS `system_admins` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `username` VARCHAR(100) NOT NULL COMMENT '管理员用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱号码',
    `security_code` VARCHAR(50) DEFAULT NULL COMMENT '安全码（用于二次验证）',
    `enable_google_auth` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用谷歌验证码: 0否, 1是',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）、SUSPENDED（暂停）',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `last_login_device` VARCHAR(200) DEFAULT NULL COMMENT '最后登录设备',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统管理员表';

-- 插入测试数据（使用 INSERT IGNORE 避免重复插入）
-- 密码使用 BCrypt 加密，示例密码为 "Admin123!"
-- 生成BCrypt密码的Java代码: BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); String hash = encoder.encode("Admin123!");
INSERT IGNORE INTO `system_admins` (`role_id`, `avatar`, `username`, `password`, `phone`, `email`, `security_code`, `enable_google_auth`, `status`, `last_login_ip`, `last_login_device`, `created_at`, `last_login_time`) VALUES
(1, 'https://example.com/avatars/admin.jpg', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '13800138000', 'admin@example.com', '123456', 1, 'ACTIVE', '192.168.1.100', 'Chrome/Windows 10', '2024-01-15 10:00:00', '2024-01-15 10:30:00'),
(2, 'https://example.com/avatars/operator1.jpg', 'operator1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '13800138001', 'operator1@example.com', '654321', 0, 'ACTIVE', '192.168.1.101', 'Firefox/Windows 10', '2024-01-15 09:00:00', '2024-01-15 09:15:00');

-- 注意：
-- 1. 如果旧表中有数据，需要手动迁移数据
-- 2. 旧表中的 user_id 和 admin_code 字段已移除
-- 3. 需要为新表设置密码（使用 BCrypt 加密）
-- 4. 建议在生产环境执行前先备份数据












