-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第1部分
-- 包含：数据库创建、用户模块、钱包模块
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `coin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `coin`;

-- =====================================================
-- 1. 用户模块 (User Module)
-- =====================================================

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `country_code` VARCHAR(10) DEFAULT NULL COMMENT '国家代码',
    `password` VARCHAR(255) DEFAULT NULL COMMENT '密码（加密）',
    `private_key_hash` VARCHAR(255) DEFAULT NULL COMMENT '私钥哈希',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `birthday` DATETIME DEFAULT NULL COMMENT '生日',
    `account_status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '账户状态: ACTIVE, FROZEN, DISABLED',
    `kyc_level` INT(1) DEFAULT 0 COMMENT 'KYC等级: 0未认证, 1基础认证, 2高级认证',
    `kyc_status` VARCHAR(20) DEFAULT NULL COMMENT 'KYC状态: PENDING, APPROVED, REJECTED',
    `two_fa_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否启用双因素认证',
    `two_fa_secret` VARCHAR(255) DEFAULT NULL COMMENT '双因素认证密钥',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`),
    KEY `idx_account_status` (`account_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 登录历史表
CREATE TABLE IF NOT EXISTS `login_history` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `status` VARCHAR(20) DEFAULT NULL COMMENT '登录状态: SUCCESS, FAILED',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录历史表';

-- 用户设备表
CREATE TABLE IF NOT EXISTS `user_devices` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `device_id` VARCHAR(100) NOT NULL COMMENT '设备ID',
    `device_type` VARCHAR(50) DEFAULT NULL COMMENT '设备类型',
    `device_name` VARCHAR(200) DEFAULT NULL COMMENT '设备名称',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设备表';

-- KYC基础认证表
CREATE TABLE IF NOT EXISTS `kyc_basic` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `real_name` VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
    `id_card_number` VARCHAR(50) DEFAULT NULL COMMENT '身份证号',
    `id_card_front_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面URL',
    `id_card_back_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证反面URL',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING, APPROVED, REJECTED',
    `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    `verified_at` DATETIME DEFAULT NULL COMMENT '认证时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='KYC基础认证表';

-- KYC高级认证表
CREATE TABLE IF NOT EXISTS `kyc_advanced` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `address_proof_url` VARCHAR(500) DEFAULT NULL COMMENT '地址证明URL',
    `bank_statement_url` VARCHAR(500) DEFAULT NULL COMMENT '银行对账单URL',
    `selfie_url` VARCHAR(500) DEFAULT NULL COMMENT '自拍照片URL',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING, APPROVED, REJECTED',
    `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    `verified_at` DATETIME DEFAULT NULL COMMENT '认证时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='KYC高级认证表';

-- IP白名单表
CREATE TABLE IF NOT EXISTS `ip_whitelist` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `ip_address` VARCHAR(50) NOT NULL COMMENT 'IP地址',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_ip_address` (`ip_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IP白名单表';

-- OAuth绑定表
CREATE TABLE IF NOT EXISTS `oauth_bindings` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `provider` VARCHAR(50) NOT NULL COMMENT '提供商: GOOGLE, FACEBOOK, APPLE等',
    `provider_user_id` VARCHAR(100) NOT NULL COMMENT '提供商用户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_provider_user` (`provider`, `provider_user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth绑定表';

-- 账户操作日志表
CREATE TABLE IF NOT EXISTS `account_operation_logs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `operation_desc` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户操作日志表';

-- 用户注册密钥表
CREATE TABLE IF NOT EXISTS `user_registration_keys` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `registration_key` VARCHAR(100) NOT NULL COMMENT '注册密钥',
    `private_key` VARCHAR(255) NOT NULL COMMENT '私钥',
    `status` VARCHAR(20) DEFAULT 'UNUSED' COMMENT '状态: UNUSED, USED, EXPIRED',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '使用该密钥的用户ID',
    `used_at` DATETIME DEFAULT NULL COMMENT '使用时间',
    `expires_at` DATETIME DEFAULT NULL COMMENT '过期时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_registration_key` (`registration_key`),
    KEY `idx_status` (`status`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户注册密钥表';

-- =====================================================
-- 2. 钱包模块 (Wallet Module)
-- =====================================================

-- 钱包表
CREATE TABLE IF NOT EXISTS `wallets` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '钱包ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_type` VARCHAR(50) NOT NULL COMMENT '账户类型: SPOT, FUTURES_USDT, FUTURES_COIN, OPTIONS, COPY_TRADING',
    `currency` VARCHAR(20) NOT NULL COMMENT '货币类型: USDT, BTC, ETH等',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '余额',
    `frozen_balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '冻结余额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_account_currency` (`user_id`, `account_type`, `currency`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_type` (`account_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='钱包表';

-- 资产转账记录表
CREATE TABLE IF NOT EXISTS `asset_transfer_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `from_account_type` VARCHAR(50) NOT NULL COMMENT '转出账户类型',
    `to_account_type` VARCHAR(50) NOT NULL COMMENT '转入账户类型',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '转账金额',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING, SUCCESS, FAILED',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资产转账记录表';

-- 理财产品账户表
CREATE TABLE IF NOT EXISTS `finance_accounts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '余额',
    `frozen_balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '冻结余额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_currency` (`user_id`, `currency`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='理财产品账户表';

-- 交割合约账户表
CREATE TABLE IF NOT EXISTS `delivery_contract_accounts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '余额',
    `frozen_balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '冻结余额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_currency` (`user_id`, `currency`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约账户表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 用户表测试数据
INSERT INTO `users` (`id`, `username`, `nickname`, `email`, `phone`, `country_code`, `password`, `avatar_url`, `gender`, `account_status`, `kyc_level`, `kyc_status`, `two_fa_enabled`, `created_at`, `updated_at`) VALUES
(1, 'testuser001', '测试用户1', 'testuser001@example.com', '13800138001', '+86', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://example.com/avatar1.jpg', 'MALE', 'ACTIVE', 1, 'APPROVED', 0, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'testuser002', '测试用户2', 'testuser002@example.com', '13800138002', '+86', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://example.com/avatar2.jpg', 'FEMALE', 'ACTIVE', 2, 'APPROVED', 1, '2024-01-02 10:00:00', '2024-01-02 10:00:00');

-- 登录历史表测试数据
INSERT INTO `login_history` (`id`, `user_id`, `ip_address`, `user_agent`, `login_time`, `status`) VALUES
(1, 1, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', '2024-01-15 10:00:00', 'SUCCESS'),
(2, 2, '192.168.1.101', 'Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X)', '2024-01-15 11:00:00', 'SUCCESS');

-- 用户设备表测试数据
INSERT INTO `user_devices` (`id`, `user_id`, `device_id`, `device_type`, `device_name`, `last_login_time`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'device_001_android', 'ANDROID', 'Samsung Galaxy S21', '2024-01-15 10:00:00', 'ACTIVE', '2024-01-01 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'device_002_ios', 'IOS', 'iPhone 13 Pro', '2024-01-15 11:00:00', 'ACTIVE', '2024-01-02 10:00:00', '2024-01-15 11:00:00');

-- KYC基础认证表测试数据
INSERT INTO `kyc_basic` (`id`, `user_id`, `real_name`, `id_card_number`, `id_card_front_url`, `id_card_back_url`, `status`, `verified_at`, `created_at`, `updated_at`) VALUES
(1, 1, '张三', '110101199001011234', 'https://example.com/idcard/front1.jpg', 'https://example.com/idcard/back1.jpg', 'APPROVED', '2024-01-05 10:00:00', '2024-01-03 10:00:00', '2024-01-05 10:00:00'),
(2, 2, '李四', '110101199002021234', 'https://example.com/idcard/front2.jpg', 'https://example.com/idcard/back2.jpg', 'APPROVED', '2024-01-06 10:00:00', '2024-01-04 10:00:00', '2024-01-06 10:00:00');

-- KYC高级认证表测试数据
INSERT INTO `kyc_advanced` (`id`, `user_id`, `address_proof_url`, `bank_statement_url`, `selfie_url`, `status`, `verified_at`, `created_at`, `updated_at`) VALUES
(1, 2, 'https://example.com/kyc/address1.pdf', 'https://example.com/kyc/bank1.pdf', 'https://example.com/kyc/selfie1.jpg', 'APPROVED', '2024-01-07 10:00:00', '2024-01-06 10:00:00', '2024-01-07 10:00:00'),
(2, 1, 'https://example.com/kyc/address2.pdf', 'https://example.com/kyc/bank2.pdf', 'https://example.com/kyc/selfie2.jpg', 'PENDING', NULL, '2024-01-08 10:00:00', '2024-01-08 10:00:00');

-- IP白名单表测试数据
INSERT INTO `ip_whitelist` (`id`, `user_id`, `ip_address`, `description`, `status`, `created_at`) VALUES
(1, 1, '192.168.1.100', '家庭网络', 'ACTIVE', '2024-01-01 10:00:00'),
(2, 2, '192.168.1.101', '办公室网络', 'ACTIVE', '2024-01-02 10:00:00');

-- OAuth绑定表测试数据
INSERT INTO `oauth_bindings` (`id`, `user_id`, `provider`, `provider_user_id`, `created_at`) VALUES
(1, 1, 'GOOGLE', 'google_user_123456', '2024-01-01 10:00:00'),
(2, 2, 'FACEBOOK', 'facebook_user_789012', '2024-01-02 10:00:00');

-- 账户操作日志表测试数据
INSERT INTO `account_operation_logs` (`id`, `user_id`, `operation_type`, `operation_desc`, `ip_address`, `created_at`) VALUES
(1, 1, 'PASSWORD_CHANGE', '用户修改密码', '192.168.1.100', '2024-01-10 10:00:00'),
(2, 2, 'EMAIL_CHANGE', '用户修改邮箱', '192.168.1.101', '2024-01-11 10:00:00');

-- 用户注册密钥表测试数据
INSERT INTO `user_registration_keys` (`id`, `registration_key`, `private_key`, `status`, `user_id`, `used_at`, `expires_at`, `created_at`, `updated_at`) VALUES
(1, 'REG_KEY_001', 'PRIVATE_KEY_001_HASH', 'USED', 1, '2024-01-01 10:00:00', '2024-12-31 23:59:59', '2023-12-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'REG_KEY_002', 'PRIVATE_KEY_002_HASH', 'USED', 2, '2024-01-02 10:00:00', '2024-12-31 23:59:59', '2023-12-01 10:00:00', '2024-01-02 10:00:00');

-- 钱包表测试数据
INSERT INTO `wallets` (`id`, `user_id`, `account_type`, `currency`, `balance`, `frozen_balance`, `created_at`, `updated_at`) VALUES
(1, 1, 'SPOT', 'USDT', 10000.00000000, 0.00000000, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 1, 'SPOT', 'BTC', 1.50000000, 0.20000000, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 2, 'SPOT', 'USDT', 50000.00000000, 1000.00000000, '2024-01-02 10:00:00', '2024-01-02 10:00:00'),
(4, 2, 'FUTURES_USDT', 'USDT', 20000.00000000, 5000.00000000, '2024-01-02 10:00:00', '2024-01-02 10:00:00');

-- 资产转账记录表测试数据
INSERT INTO `asset_transfer_records` (`id`, `user_id`, `from_account_type`, `to_account_type`, `currency`, `amount`, `status`, `remark`, `created_at`, `updated_at`) VALUES
(1, 1, 'SPOT', 'FUTURES_USDT', 'USDT', 1000.00000000, 'SUCCESS', '转入合约账户', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 'FUTURES_USDT', 'SPOT', 'USDT', 2000.00000000, 'SUCCESS', '转出合约账户', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 理财产品账户表测试数据
INSERT INTO `finance_accounts` (`id`, `user_id`, `currency`, `balance`, `frozen_balance`, `created_at`, `updated_at`) VALUES
(1, 1, 'USDT', 5000.00000000, 0.00000000, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 2, 'USDT', 10000.00000000, 2000.00000000, '2024-01-02 10:00:00', '2024-01-02 10:00:00');

-- 交割合约账户表测试数据
INSERT INTO `delivery_contract_accounts` (`id`, `user_id`, `currency`, `balance`, `frozen_balance`, `created_at`, `updated_at`) VALUES
(1, 1, 'USDT', 3000.00000000, 500.00000000, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 2, 'USDT', 8000.00000000, 1000.00000000, '2024-01-02 10:00:00', '2024-01-02 10:00:00');












