-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 合约体验金功能模块数据库设计
-- =====================================================
-- 注意：此文件需要在mysql8_schema.sql执行后执行

USE `crypto_exchange`;

-- =====================================================
-- 1. 合约体验金账户表
-- =====================================================

CREATE TABLE IF NOT EXISTS `contract_experience_accounts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_no` VARCHAR(50) NOT NULL COMMENT '体验金账户编号（唯一标识）',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '体验金余额',
    `initial_amount` DECIMAL(20,8) NOT NULL COMMENT '初始体验金金额',
    `currency` VARCHAR(20) NOT NULL DEFAULT 'USDT' COMMENT '币种',
    `max_leverage` INT(11) NOT NULL DEFAULT 10 COMMENT '最大杠杆倍数',
    `max_position` DECIMAL(20,8) DEFAULT NULL COMMENT '最大仓位限制',
    `daily_trade_limit` INT(11) DEFAULT NULL COMMENT '每日最大交易次数',
    `daily_trade_count` INT(11) NOT NULL DEFAULT 0 COMMENT '当日交易次数',
    `last_trade_date` DATE DEFAULT NULL COMMENT '最后交易日期（用于重置每日交易次数）',
    `account_status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '账户状态: ACTIVE（活跃）, FROZEN（已冻结）, EXPIRED（已过期）, CLOSED（已关闭）',
    `create_time` DATETIME NOT NULL COMMENT '账户创建时间',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `last_reset_time` DATETIME DEFAULT NULL COMMENT '上次重置时间（用于重置每日限制）',
    `total_profit` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '累计盈利',
    `total_loss` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '累计亏损',
    `total_trades` INT(11) NOT NULL DEFAULT 0 COMMENT '总交易次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account_no` (`account_no`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_account_status` (`account_status`),
    KEY `idx_expire_time` (`expire_time`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约体验金账户表';

-- =====================================================
-- 2. 合约体验金发放活动表
-- =====================================================

CREATE TABLE IF NOT EXISTS `contract_experience_activities` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `activity_name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `activity_code` VARCHAR(50) NOT NULL COMMENT '活动代码（唯一标识）',
    `experience_amount` DECIMAL(20,8) NOT NULL COMMENT '体验金金额',
    `currency` VARCHAR(20) NOT NULL DEFAULT 'USDT' COMMENT '币种',
    `valid_days` INT(11) NOT NULL DEFAULT 7 COMMENT '有效期（天数）',
    `max_leverage` INT(11) NOT NULL DEFAULT 10 COMMENT '最大杠杆倍数',
    `max_position` DECIMAL(20,8) DEFAULT NULL COMMENT '最大仓位限制',
    `daily_trade_limit` INT(11) DEFAULT NULL COMMENT '每日最大交易次数',
    `target_users` VARCHAR(50) NOT NULL COMMENT '目标用户: NEW（新用户）, ALL（全体用户）, VIP（VIP用户）, SPECIFIC（特定用户）',
    `receive_condition` VARCHAR(100) DEFAULT NULL COMMENT '领取条件: REGISTER（注册）, KYC（完成KYC）, TRADE（完成交易）, NONE（无限制）',
    `total_count` INT(11) DEFAULT NULL COMMENT '发放总数量（NULL表示无限制）',
    `issued_count` INT(11) NOT NULL DEFAULT 0 COMMENT '已发放数量',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '活动状态: ACTIVE（进行中）, SUSPENDED（已暂停）, ENDED（已结束）',
    `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
    `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
    `description` TEXT COMMENT '活动描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_code` (`activity_code`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约体验金发放活动表';

-- =====================================================
-- 3. 合约体验金交易记录表
-- =====================================================

CREATE TABLE IF NOT EXISTS `contract_experience_trades` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '体验金账户ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `trade_no` VARCHAR(50) NOT NULL COMMENT '交易编号',
    `contract_type` VARCHAR(50) NOT NULL COMMENT '合约类型: FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）, PERPETUAL（永续合约）',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对',
    `trade_type` VARCHAR(50) NOT NULL COMMENT '交易类型: OPEN_LONG（开多）, OPEN_SHORT（开空）, CLOSE_LONG（平多）, CLOSE_SHORT（平空）',
    `order_type` VARCHAR(50) NOT NULL COMMENT '订单类型: MARKET（市价）, LIMIT（限价）',
    `leverage` INT(11) NOT NULL COMMENT '杠杆倍数',
    `position_size` DECIMAL(20,8) NOT NULL COMMENT '仓位大小',
    `entry_price` DECIMAL(20,8) DEFAULT NULL COMMENT '开仓价格',
    `exit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '平仓价格',
    `open_fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '开仓手续费',
    `close_fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '平仓手续费',
    `profit_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '盈亏金额',
    `profit_loss_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '盈亏比例',
    `open_time` DATETIME NOT NULL COMMENT '开仓时间',
    `close_time` DATETIME DEFAULT NULL COMMENT '平仓时间',
    `trade_status` VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '交易状态: OPEN（持仓中）, CLOSED（已平仓）, LIQUIDATED（已爆仓）',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`trade_no`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_trade_status` (`trade_status`),
    KEY `idx_open_time` (`open_time`),
    KEY `idx_close_time` (`close_time`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约体验金交易记录表';

-- =====================================================
-- 4. 合约体验金账户变更记录表
-- =====================================================

CREATE TABLE IF NOT EXISTS `contract_experience_account_changes` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '体验金账户ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `change_type` VARCHAR(50) NOT NULL COMMENT '变更类型: INIT（初始化）, TRADE_PROFIT（交易盈利）, TRADE_LOSS（交易亏损）, ADJUST（手动调整）',
    `change_amount` DECIMAL(20,8) NOT NULL COMMENT '变更金额（正数为增加，负数为减少）',
    `before_balance` DECIMAL(20,8) NOT NULL COMMENT '变更前余额',
    `after_balance` DECIMAL(20,8) NOT NULL COMMENT '变更后余额',
    `trade_id` BIGINT(20) DEFAULT NULL COMMENT '关联交易记录ID',
    `activity_id` BIGINT(20) DEFAULT NULL COMMENT '关联活动ID（初始化时）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_change_type` (`change_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约体验金账户变更记录表';

-- =====================================================
-- 测试数据
-- =====================================================

INSERT INTO `contract_experience_activities` (`activity_name`, `activity_code`, `experience_amount`, `valid_days`, `max_leverage`, `max_position`, `daily_trade_limit`, `target_users`, `receive_condition`, `status`, `start_time`, `end_time`, `description`) VALUES
('新用户体验金', 'NEW_USER_EXP-2024', 1000.00000000, 7, 10, 1000.00000000, 5, 'NEW', 'REGISTER', 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY), '新用户注册即可获得1000 USDT体验金，有效期7天');














