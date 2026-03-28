-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第9部分
-- 包含：模拟交易模块、闪兑模块、任务模块、储备证明模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 27. 模拟交易模块 (Simulated Trading Module)
-- =====================================================

-- 模拟交易账户表
CREATE TABLE IF NOT EXISTS `simulated_trading_accounts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_no` VARCHAR(50) NOT NULL COMMENT '模拟账户编号（唯一标识）',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '账户余额',
    `initial_balance` DECIMAL(20,8) NOT NULL DEFAULT 10000.00000000 COMMENT '初始余额（默认10000 USDT）',
    `currency` VARCHAR(20) NOT NULL DEFAULT 'USDT' COMMENT '币种',
    `max_leverage` INT(11) NOT NULL DEFAULT 10 COMMENT '最大杠杆倍数',
    `max_position` DECIMAL(20,8) DEFAULT 1000.00000000 COMMENT '最大仓位限制',
    `max_trade_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '每笔交易最大金额限制',
    `account_status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '账户状态: ACTIVE（活跃）, FROZEN（已冻结）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
    `last_reset_time` DATETIME DEFAULT NULL COMMENT '上次重置时间',
    `reset_count` INT(11) NOT NULL DEFAULT 0 COMMENT '重置次数',
    `total_profit` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '累计盈利',
    `total_loss` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '累计亏损',
    `total_trades` INT(11) NOT NULL DEFAULT 0 COMMENT '总交易次数',
    `total_win_trades` INT(11) NOT NULL DEFAULT 0 COMMENT '盈利交易次数',
    `total_lose_trades` INT(11) NOT NULL DEFAULT 0 COMMENT '亏损交易次数',
    `max_drawdown` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '最大回撤',
    `win_rate` DECIMAL(10,6) DEFAULT 0.000000 COMMENT '胜率',
    `last_trade_time` DATETIME DEFAULT NULL COMMENT '最后交易时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account_no` (`account_no`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_account_status` (`account_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模拟交易账户表';

-- 模拟交易记录表
CREATE TABLE IF NOT EXISTS `simulated_trading_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '模拟账户ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `trade_no` VARCHAR(50) NOT NULL COMMENT '交易编号',
    `trade_type` VARCHAR(50) NOT NULL COMMENT '交易类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）, DELIVERY（交割合约）, LEVERAGE（杠杆交易）, OPTIONS（期权合约）',
    `contract_type` VARCHAR(50) DEFAULT NULL COMMENT '合约类型（用于合约交易）',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对',
    `order_type` VARCHAR(50) NOT NULL COMMENT '订单类型: MARKET（市价）, LIMIT（限价）, STOP_LOSS（止损）, TAKE_PROFIT（止盈）等',
    `position_side` VARCHAR(50) DEFAULT NULL COMMENT '仓位方向: LONG（多仓）, SHORT（空仓）（用于合约交易）',
    `side` VARCHAR(20) NOT NULL COMMENT '买卖方向: BUY（买入）, SELL（卖出）',
    `leverage` INT(11) DEFAULT 1 COMMENT '杠杆倍数（默认1倍，现货为1倍）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '交易数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '交易价格（限价单）',
    `executed_price` DECIMAL(20,8) DEFAULT NULL COMMENT '成交价格',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '手续费',
    `margin` DECIMAL(20,8) DEFAULT NULL COMMENT '保证金（用于杠杆和合约交易）',
    `entry_price` DECIMAL(20,8) DEFAULT NULL COMMENT '开仓价格（用于合约交易）',
    `exit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '平仓价格（用于合约交易）',
    `profit_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '盈亏金额',
    `profit_loss_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '盈亏比例',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `open_time` DATETIME NOT NULL COMMENT '开仓时间/下单时间',
    `close_time` DATETIME DEFAULT NULL COMMENT '平仓时间/成交时间',
    `trade_status` VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '交易状态: OPEN（持仓中/委托中）, FILLED（已成交）, CLOSED（已平仓）, CANCELLED（已取消）, STOPPED（止损）, PROFITED（止盈）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`trade_no`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_trade_type` (`trade_type`),
    KEY `idx_trade_status` (`trade_status`),
    KEY `idx_open_time` (`open_time`),
    KEY `idx_close_time` (`close_time`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模拟交易记录表';

-- 模拟交易账户变更记录表
CREATE TABLE IF NOT EXISTS `simulated_trading_account_changes` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '模拟账户ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `change_type` VARCHAR(50) NOT NULL COMMENT '变更类型: INIT（初始化）, RESET（重置）, TRADE_PROFIT（交易盈利）, TRADE_LOSS（交易亏损）, DEPOSIT（充值）, WITHDRAW（提现）',
    `change_amount` DECIMAL(20,8) NOT NULL COMMENT '变更金额（正数为增加，负数为减少）',
    `before_balance` DECIMAL(20,8) NOT NULL COMMENT '变更前余额',
    `after_balance` DECIMAL(20,8) NOT NULL COMMENT '变更后余额',
    `trade_id` BIGINT(20) DEFAULT NULL COMMENT '关联交易记录ID',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_change_type` (`change_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模拟交易账户变更记录表';

-- 模拟交易规则配置表
CREATE TABLE IF NOT EXISTS `simulated_trading_rules` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `rule_name` VARCHAR(200) NOT NULL COMMENT '规则名称',
    `rule_key` VARCHAR(100) NOT NULL COMMENT '规则键（唯一标识）',
    `rule_value` VARCHAR(500) NOT NULL COMMENT '规则值（JSON格式或具体值）',
    `rule_type` VARCHAR(50) NOT NULL COMMENT '规则类型: INITIAL_BALANCE（初始余额）, MAX_LEVERAGE（最大杠杆）, MAX_POSITION（最大仓位）, MAX_TRADE_AMOUNT（最大交易金额）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '规则描述',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_rule_key` (`rule_key`),
    KEY `idx_rule_type` (`rule_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模拟交易规则配置表';

-- 模拟交易活动表
CREATE TABLE IF NOT EXISTS `simulated_trading_activities` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `activity_name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `activity_code` VARCHAR(50) NOT NULL COMMENT '活动代码（唯一标识）',
    `activity_type` VARCHAR(50) NOT NULL COMMENT '活动类型: COMPETITION（竞赛）, REWARD（奖励）',
    `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
    `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
    `reward_rules` TEXT DEFAULT NULL COMMENT '奖励规则（JSON格式）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '活动状态: ACTIVE, SUSPENDED, ENDED',
    `participant_count` INT(11) NOT NULL DEFAULT 0 COMMENT '参与人数',
    `description` TEXT DEFAULT NULL COMMENT '活动描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_code` (`activity_code`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模拟交易活动表';

-- =====================================================
-- 28. 闪兑模块 (Flash Exchange Module)
-- =====================================================

-- 闪兑订单表（已在第5部分包含，这里仅作注释说明）
-- 表名: flash_exchange_orders
-- 已在 complete_schema_part_05.sql 中定义

-- =====================================================
-- 29. 任务模块 (Task Module)
-- =====================================================

-- 任务表
CREATE TABLE IF NOT EXISTS `tasks` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `task_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
    `task_type` VARCHAR(50) NOT NULL COMMENT '任务类型: INVITE（邀请）、REGISTER_KYC（注册认证）、DEPOSIT（入金）、TRADE（交易）、COMPREHENSIVE（综合）',
    `task_description` TEXT DEFAULT NULL COMMENT '任务描述',
    `task_conditions` TEXT DEFAULT NULL COMMENT '任务条件（JSON格式）',
    `reward_currency` VARCHAR(20) NOT NULL COMMENT '奖励币种',
    `reward_amount` DECIMAL(20,8) NOT NULL COMMENT '奖励金额',
    `max_reward_count` INT(11) DEFAULT NULL COMMENT '最大奖励次数，NULL表示无限制',
    `daily_limit` INT(11) DEFAULT NULL COMMENT '每日限制',
    `weekly_limit` INT(11) DEFAULT NULL COMMENT '每周限制',
    `monthly_limit` INT(11) DEFAULT NULL COMMENT '每月限制',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `completion_deadline` INT(11) DEFAULT NULL COMMENT '完成期限（天数）',
    `reward_method` VARCHAR(20) NOT NULL DEFAULT 'INSTANT' COMMENT '奖励发放方式: INSTANT（即时）、PERIODIC（按周期）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_task_type` (`task_type`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表';

-- 任务进度表
CREATE TABLE IF NOT EXISTS `task_progress` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
    `progress_data` TEXT DEFAULT NULL COMMENT '进度数据（JSON格式）',
    `completion_rate` DECIMAL(5,2) DEFAULT NULL COMMENT '完成率（0-100）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS' COMMENT '状态: IN_PROGRESS（进行中）、COMPLETED（已完成）、REWARDED（已奖励）、EXPIRED（已过期）',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_task` (`user_id`, `task_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务进度表';

-- 任务奖励表
CREATE TABLE IF NOT EXISTS `task_rewards` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `reward_no` VARCHAR(50) NOT NULL COMMENT '奖励单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `task_id` BIGINT(20) NOT NULL COMMENT '任务ID',
    `progress_id` BIGINT(20) NOT NULL COMMENT '任务进度ID',
    `reward_currency` VARCHAR(20) NOT NULL COMMENT '奖励币种',
    `reward_amount` DECIMAL(20,8) NOT NULL COMMENT '奖励金额',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待发放）、CLAIMED（已领取）、DISTRIBUTED（已发放）、FAILED（发放失败）',
    `claimed_at` DATETIME DEFAULT NULL COMMENT '领取时间',
    `distributed_at` DATETIME DEFAULT NULL COMMENT '发放时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_reward_no` (`reward_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务奖励表';

-- =====================================================
-- 30. 储备证明模块 (Proof of Reserves Module)
-- =====================================================

-- 平台资产表
CREATE TABLE IF NOT EXISTS `platform_assets` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `asset_type` VARCHAR(50) NOT NULL COMMENT '资产类型: SPOT（现货）、CONTRACT（合约）、COLD_WALLET（冷钱包）、FIAT（法币）',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `balance` DECIMAL(36,18) NOT NULL COMMENT '余额',
    `wallet_address` VARCHAR(100) DEFAULT NULL COMMENT '钱包地址',
    `account_type` VARCHAR(50) DEFAULT NULL COMMENT '账户类型',
    `sync_time` DATETIME NOT NULL COMMENT '同步时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_asset_type` (`asset_type`),
    KEY `idx_currency` (`currency`),
    KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='平台资产表';

-- 用户存款表
CREATE TABLE IF NOT EXISTS `user_deposits` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_type` VARCHAR(50) NOT NULL COMMENT '账户类型: SPOT（现货）、CONTRACT（合约）、LENDING（借贷）',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `balance` DECIMAL(36,18) NOT NULL COMMENT '余额',
    `deposit_hash` VARCHAR(100) NOT NULL COMMENT '存款哈希（用于Merkle树）',
    `sync_time` DATETIME NOT NULL COMMENT '同步时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_deposit_hash` (`deposit_hash`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_type` (`account_type`),
    KEY `idx_currency` (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户存款表';

-- Merkle树表
CREATE TABLE IF NOT EXISTS `merkle_trees` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `tree_root` VARCHAR(100) NOT NULL COMMENT 'Merkle树根节点哈希',
    `tree_version` VARCHAR(50) NOT NULL COMMENT '树版本号',
    `total_assets` DECIMAL(36,18) NOT NULL COMMENT '总资产',
    `total_deposits` DECIMAL(36,18) NOT NULL COMMENT '总存款',
    `coverage_ratio` DECIMAL(10,4) NOT NULL COMMENT '覆盖率（总资产/总存款）',
    `tree_data` LONGTEXT DEFAULT NULL COMMENT '树数据（JSON格式）',
    `signature` VARCHAR(500) DEFAULT NULL COMMENT '平台签名',
    `blockchain_hash` VARCHAR(100) DEFAULT NULL COMMENT '区块链存证哈希',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tree_root` (`tree_root`),
    KEY `idx_tree_version` (`tree_version`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Merkle树表';

-- Merkle证明表
CREATE TABLE IF NOT EXISTS `merkle_proofs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `tree_id` BIGINT(20) NOT NULL COMMENT 'Merkle树ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `deposit_id` BIGINT(20) NOT NULL COMMENT '存款ID',
    `leaf_hash` VARCHAR(100) NOT NULL COMMENT '叶子节点哈希',
    `proof_path` TEXT NOT NULL COMMENT '证明路径（JSON数组）',
    `verified` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已验证',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tree_user` (`tree_id`, `user_id`),
    KEY `idx_tree_id` (`tree_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_verified` (`verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Merkle证明表';

-- 储备金证明报告表
CREATE TABLE IF NOT EXISTS `reserve_reports` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `report_no` VARCHAR(50) NOT NULL COMMENT '报告编号',
    `tree_id` BIGINT(20) NOT NULL COMMENT 'Merkle树ID',
    `report_date` DATETIME NOT NULL COMMENT '报告日期',
    `total_assets` DECIMAL(36,18) NOT NULL COMMENT '总资产',
    `total_deposits` DECIMAL(36,18) NOT NULL COMMENT '总存款',
    `coverage_ratio` DECIMAL(10,4) NOT NULL COMMENT '覆盖率',
    `report_content` LONGTEXT DEFAULT NULL COMMENT '报告内容（JSON格式）',
    `auditor_name` VARCHAR(200) DEFAULT NULL COMMENT '审计机构名称',
    `audit_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '审计状态: PENDING（待审计）、AUDITED（已审计）、REJECTED（已拒绝）',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审计时间',
    `public_url` VARCHAR(500) DEFAULT NULL COMMENT '公开URL',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_report_no` (`report_no`),
    KEY `idx_tree_id` (`tree_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_report_date` (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='储备金证明报告表';

-- =====================================================
-- 测试数据插入
-- =====================================================

-- 模拟交易账户测试数据
INSERT INTO `simulated_trading_accounts` (`user_id`, `account_no`, `balance`, `initial_balance`, `currency`, `max_leverage`, `max_position`, `account_status`, `create_time`, `total_profit`, `total_loss`, `total_trades`, `total_win_trades`, `total_lose_trades`, `win_rate`) VALUES
(1, 'SIM-ACC-001', 10500.00000000, 10000.00000000, 'USDT', 10, 1000.00000000, 'ACTIVE', '2024-01-15 10:00:00', 500.00000000, 0.00000000, 10, 7, 3, 0.700000),
(2, 'SIM-ACC-002', 9800.00000000, 10000.00000000, 'USDT', 10, 1000.00000000, 'ACTIVE', '2024-01-15 09:00:00', 0.00000000, 200.00000000, 8, 3, 5, 0.375000);

-- 模拟交易记录测试数据
INSERT INTO `simulated_trading_records` (`account_id`, `user_id`, `trade_no`, `trade_type`, `pair_name`, `order_type`, `side`, `leverage`, `quantity`, `executed_price`, `amount`, `fee`, `profit_loss`, `open_time`, `close_time`, `trade_status`) VALUES
(1, 1, 'SIM-TRADE-001', 'SPOT', 'BTC/USDT', 'MARKET', 'BUY', 1, 0.01, 45000.00, 450.00, 0.45, 50.00000000, '2024-01-15 10:30:00', '2024-01-15 15:00:00', 'CLOSED'),
(1, 1, 'SIM-TRADE-002', 'FUTURES_USDT', 'ETH/USDT', 'LIMIT', 'BUY', 10, 1.0, 2800.00, 2800.00, 2.80, 100.00000000, '2024-01-15 11:00:00', '2024-01-15 16:00:00', 'CLOSED'),
(2, 2, 'SIM-TRADE-003', 'SPOT', 'BTC/USDT', 'MARKET', 'SELL', 1, 0.01, 44000.00, 440.00, 0.44, -50.00000000, '2024-01-15 09:30:00', '2024-01-15 14:00:00', 'CLOSED');

-- 模拟交易账户变更记录测试数据
INSERT INTO `simulated_trading_account_changes` (`account_id`, `user_id`, `change_type`, `change_amount`, `before_balance`, `after_balance`, `trade_id`, `remark`) VALUES
(1, 1, 'INIT', 10000.00000000, 0.00000000, 10000.00000000, NULL, '初始化模拟账户'),
(1, 1, 'TRADE_PROFIT', 50.00000000, 10000.00000000, 10050.00000000, 1, '交易盈利'),
(2, 2, 'INIT', 10000.00000000, 0.00000000, 10000.00000000, NULL, '初始化模拟账户'),
(2, 2, 'TRADE_LOSS', -200.00000000, 10000.00000000, 9800.00000000, 3, '交易亏损');

-- 模拟交易规则配置测试数据
INSERT INTO `simulated_trading_rules` (`rule_name`, `rule_key`, `rule_value`, `rule_type`, `description`, `status`) VALUES
('初始余额', 'INITIAL_BALANCE', '10000', 'INITIAL_BALANCE', '模拟交易账户初始余额为10000 USDT', 'ACTIVE'),
('最大杠杆', 'MAX_LEVERAGE', '10', 'MAX_LEVERAGE', '模拟交易最大杠杆倍数为10倍', 'ACTIVE'),
('最大仓位', 'MAX_POSITION', '1000', 'MAX_POSITION', '模拟交易最大仓位限制为1000 USDT', 'ACTIVE');

-- 模拟交易活动测试数据
INSERT INTO `simulated_trading_activities` (`activity_name`, `activity_code`, `activity_type`, `start_time`, `end_time`, `status`, `participant_count`, `description`) VALUES
('模拟交易大赛', 'SIM-COMP-2024-01', 'COMPETITION', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 'ACTIVE', 150, '2024年1月模拟交易大赛，奖励丰厚'),
('新手训练营', 'SIM-TRAIN-2024-01', 'REWARD', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'ACTIVE', 500, '新手模拟交易训练营，完成训练可获得奖励');

-- 任务测试数据
INSERT INTO `tasks` (`task_name`, `task_type`, `task_description`, `task_conditions`, `reward_currency`, `reward_amount`, `max_reward_count`, `reward_method`, `status`, `start_time`, `end_time`) VALUES
('邀请好友', 'INVITE', '邀请好友注册并完成KYC认证', '{"invite_count": 1, "kyc_required": true}', 'USDT', 50.00000000, NULL, 'INSTANT', 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('完成首笔交易', 'TRADE', '完成首笔现货交易', '{"min_amount": 100, "trade_type": "SPOT"}', 'USDT', 20.00000000, 1, 'INSTANT', 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('每日签到', 'COMPREHENSIVE', '每日登录平台签到', '{"checkin_days": 7}', 'USDT', 10.00000000, NULL, 'PERIODIC', 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59');

-- 任务进度测试数据
INSERT INTO `task_progress` (`user_id`, `task_id`, `progress_data`, `completion_rate`, `status`, `completed_at`, `deadline`) VALUES
(1, 1, '{"invited_users": 1, "kyc_completed": 1}', 100.00, 'COMPLETED', '2024-01-15 10:00:00', '2024-01-22 10:00:00'),
(1, 2, '{"trade_count": 1, "trade_amount": 500}', 100.00, 'COMPLETED', '2024-01-15 11:00:00', '2024-01-22 11:00:00'),
(2, 1, '{"invited_users": 0, "kyc_completed": 0}', 0.00, 'IN_PROGRESS', NULL, '2024-01-22 09:00:00');

-- 任务奖励测试数据
INSERT INTO `task_rewards` (`reward_no`, `user_id`, `task_id`, `progress_id`, `reward_currency`, `reward_amount`, `status`, `claimed_at`, `distributed_at`) VALUES
('REWARD-20240115-001', 1, 1, 1, 'USDT', 50.00000000, 'DISTRIBUTED', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
('REWARD-20240115-002', 1, 2, 2, 'USDT', 20.00000000, 'DISTRIBUTED', '2024-01-15 11:00:00', '2024-01-15 11:00:00');

-- 平台资产测试数据
INSERT INTO `platform_assets` (`asset_type`, `currency`, `balance`, `wallet_address`, `account_type`, `sync_time`) VALUES
('SPOT', 'USDT', 10000000.000000000000000000, NULL, 'SPOT', '2024-01-15 10:00:00'),
('SPOT', 'BTC', 100.000000000000000000, '1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa', 'SPOT', '2024-01-15 10:00:00'),
('COLD_WALLET', 'BTC', 500.000000000000000000, '1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2', 'COLD', '2024-01-15 10:00:00'),
('COLD_WALLET', 'ETH', 1000.000000000000000000, '0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb', 'COLD', '2024-01-15 10:00:00');

-- 用户存款测试数据
INSERT INTO `user_deposits` (`user_id`, `account_type`, `currency`, `balance`, `deposit_hash`, `sync_time`) VALUES
(1, 'SPOT', 'USDT', 5000.000000000000000000, 'hash_user1_spot_usdt_001', '2024-01-15 10:00:00'),
(1, 'SPOT', 'BTC', 0.1, 'hash_user1_spot_btc_001', '2024-01-15 10:00:00'),
(2, 'SPOT', 'USDT', 3000.000000000000000000, 'hash_user2_spot_usdt_001', '2024-01-15 10:00:00'),
(2, 'CONTRACT', 'USDT', 2000.000000000000000000, 'hash_user2_contract_usdt_001', '2024-01-15 10:00:00');

-- Merkle树测试数据
INSERT INTO `merkle_trees` (`tree_root`, `tree_version`, `total_assets`, `total_deposits`, `coverage_ratio`, `signature`, `blockchain_hash`) VALUES
('0xabc123def456...', 'v1.0.20240115', 10000000.000000000000000000, 10000.000000000000000000, 1000.0000, 'platform_signature_001', '0xblockchain_hash_001'),
('0xdef789abc123...', 'v1.0.20240116', 10100000.000000000000000000, 10100.000000000000000000, 1000.0000, 'platform_signature_002', '0xblockchain_hash_002');

-- Merkle证明测试数据
INSERT INTO `merkle_proofs` (`tree_id`, `user_id`, `deposit_id`, `leaf_hash`, `proof_path`, `verified`) VALUES
(1, 1, 1, '0xleaf_hash_user1_001', '["0xproof1", "0xproof2", "0xproof3"]', 1),
(1, 2, 3, '0xleaf_hash_user2_001', '["0xproof1", "0xproof2", "0xproof3"]', 1);

-- 储备金证明报告测试数据
INSERT INTO `reserve_reports` (`report_no`, `tree_id`, `report_date`, `total_assets`, `total_deposits`, `coverage_ratio`, `auditor_name`, `audit_status`, `audit_time`, `public_url`) VALUES
('REPORT-20240115-001', 1, '2024-01-15 10:00:00', 10000000.000000000000000000, 10000.000000000000000000, 1000.0000, '第三方审计机构A', 'AUDITED', '2024-01-15 12:00:00', 'https://example.com/reports/20240115'),
('REPORT-20240116-001', 2, '2024-01-16 10:00:00', 10100000.000000000000000000, 10100.000000000000000000, 1000.0000, '第三方审计机构A', 'PENDING', NULL, NULL);












