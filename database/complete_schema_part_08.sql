-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第8部分
-- 包含：理财产品模块、矿池模块、红包模块、合约体验金模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 23. 理财产品模块 (Finance Product Module)
-- =====================================================

-- 理财产品表
CREATE TABLE IF NOT EXISTS `finance_products` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '产品名称',
    `product_code` VARCHAR(50) NOT NULL COMMENT '产品代码（唯一标识）',
    `product_type` VARCHAR(50) NOT NULL COMMENT '产品类型: FIXED（定期理财）, FLEXIBLE（活期理财）',
    `risk_level` VARCHAR(50) NOT NULL COMMENT '风险等级: CONSERVATIVE（稳健型）, HIGH_RISK（高风险高收益）',
    `annual_rate` DECIMAL(10,6) NOT NULL COMMENT '年化收益率（例如：0.050000表示5%）',
    `investment_cycle` INT(11) DEFAULT NULL COMMENT '投资周期（天数），灵活型为NULL',
    `min_investment_amount` DECIMAL(20,8) NOT NULL COMMENT '最小投资金额',
    `max_investment_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最大投资金额',
    `total_raise_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '总募集金额',
    `invested_amount` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '已投资金额',
    `available_amount` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '剩余可投资金额',
    `supported_currency` VARCHAR(20) NOT NULL COMMENT '支持的投资币种（如: USDT, BTC, ETH）',
    `lock_period` INT(11) DEFAULT NULL COMMENT '锁仓期（天数），活期理财为NULL',
    `status` VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '产品状态: AVAILABLE（可购买）, SOLD_OUT（售罄）, ENDED（结束）, SUSPENDED（暂停）',
    `start_time` DATETIME DEFAULT NULL COMMENT '产品开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '产品结束时间',
    `description` TEXT DEFAULT NULL COMMENT '产品描述',
    `risk_warning` TEXT DEFAULT NULL COMMENT '风险提示',
    `settlement_method` VARCHAR(50) NOT NULL DEFAULT 'AUTO' COMMENT '结算方式: AUTO（自动）, MANUAL（手动）',
    `settlement_cycle` VARCHAR(50) DEFAULT NULL COMMENT '收益结算周期: DAILY（每日）, WEEKLY（每周）, MONTHLY（每月）, MATURITY（到期结算）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_code` (`product_code`),
    KEY `idx_product_type` (`product_type`),
    KEY `idx_risk_level` (`risk_level`),
    KEY `idx_status` (`status`),
    KEY `idx_supported_currency` (`supported_currency`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='理财产品表';

-- 用户理财产品投资记录表
CREATE TABLE IF NOT EXISTS `finance_investments` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_id` BIGINT(20) NOT NULL COMMENT '理财产品ID',
    `product_code` VARCHAR(50) NOT NULL COMMENT '产品代码（冗余字段，方便查询）',
    `investment_amount` DECIMAL(20,8) NOT NULL COMMENT '投资金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '投资币种',
    `start_date` DATETIME NOT NULL COMMENT '投资开始时间',
    `end_date` DATETIME DEFAULT NULL COMMENT '投资结束时间（定期理财），活期理财为NULL',
    `expected_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '预期收益',
    `actual_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '实际收益',
    `accumulated_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '累计收益',
    `last_settlement_time` DATETIME DEFAULT NULL COMMENT '上次结算时间',
    `next_settlement_time` DATETIME DEFAULT NULL COMMENT '下次结算时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '投资状态: ACTIVE（进行中）, MATURED（已到期）, CANCELLED（已取消）, REDEEMED（已赎回）',
    `lock_until` DATETIME DEFAULT NULL COMMENT '锁仓到期时间',
    `redeemed_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已赎回金额',
    `remaining_principal` DECIMAL(20,8) NOT NULL COMMENT '剩余本金',
    `investment_order_no` VARCHAR(50) NOT NULL COMMENT '投资订单号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_investment_order_no` (`investment_order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_product_code` (`product_code`),
    KEY `idx_status` (`status`),
    KEY `idx_start_date` (`start_date`),
    KEY `idx_end_date` (`end_date`),
    KEY `idx_lock_until` (`lock_until`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户理财产品投资记录表';

-- 理财产品收益结算记录表
CREATE TABLE IF NOT EXISTS `finance_profit_settlements` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `investment_id` BIGINT(20) NOT NULL COMMENT '投资记录ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_id` BIGINT(20) NOT NULL COMMENT '理财产品ID',
    `settlement_period_start` DATETIME NOT NULL COMMENT '结算周期开始时间',
    `settlement_period_end` DATETIME NOT NULL COMMENT '结算周期结束时间',
    `principal_amount` DECIMAL(20,8) NOT NULL COMMENT '本金金额（结算周期内的平均本金）',
    `profit_amount` DECIMAL(20,8) NOT NULL COMMENT '收益金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `annual_rate` DECIMAL(10,6) NOT NULL COMMENT '年化收益率（结算时的利率）',
    `settlement_type` VARCHAR(50) NOT NULL COMMENT '结算类型: INTEREST（利息）, PRINCIPAL（本金）, BOTH（本息）',
    `settlement_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '结算状态: PENDING（待结算）, SETTLED（已结算）, FAILED（结算失败）',
    `settlement_time` DATETIME DEFAULT NULL COMMENT '结算时间',
    `settlement_order_no` VARCHAR(50) DEFAULT NULL COMMENT '结算订单号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_investment_id` (`investment_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_settlement_status` (`settlement_status`),
    KEY `idx_settlement_period_end` (`settlement_period_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='理财产品收益结算记录表';

-- 理财产品赎回记录表
CREATE TABLE IF NOT EXISTS `finance_redemptions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `investment_id` BIGINT(20) NOT NULL COMMENT '投资记录ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_id` BIGINT(20) NOT NULL COMMENT '理财产品ID',
    `redemption_amount` DECIMAL(20,8) NOT NULL COMMENT '赎回金额',
    `principal_amount` DECIMAL(20,8) NOT NULL COMMENT '赎回本金',
    `profit_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '赎回收益',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `redemption_type` VARCHAR(50) NOT NULL COMMENT '赎回类型: FULL（全额赎回）, PARTIAL（部分赎回）, AUTO（自动到期赎回）',
    `redemption_fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '赎回手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '赎回状态: PENDING（待处理）, PROCESSING（处理中）, COMPLETED（已完成）, FAILED（失败）',
    `redemption_order_no` VARCHAR(50) NOT NULL COMMENT '赎回订单号',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_redemption_order_no` (`redemption_order_no`),
    KEY `idx_investment_id` (`investment_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='理财产品赎回记录表';

-- =====================================================
-- 24. 矿池模块 (Mining Pool Module)
-- =====================================================

-- 矿池表
CREATE TABLE IF NOT EXISTS `mining_pools` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pool_name` VARCHAR(200) NOT NULL COMMENT '矿池名称',
    `pool_code` VARCHAR(50) NOT NULL COMMENT '矿池代码（唯一标识）',
    `mining_currency` VARCHAR(20) NOT NULL COMMENT '挖矿币种（如: BTC, ETH）',
    `algorithm` VARCHAR(50) NOT NULL COMMENT '挖矿算法（如: SHA-256, Ethash）',
    `total_hashrate` DECIMAL(30,8) DEFAULT 0.00000000 COMMENT '矿池总算力',
    `active_hashrate` DECIMAL(30,8) DEFAULT 0.00000000 COMMENT '活跃算力',
    `pool_revenue` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '矿池总收益',
    `distribution_method` VARCHAR(50) NOT NULL DEFAULT 'PPS' COMMENT '收益分配方式: PPS（按股份支付）, PPLNS（按最后N股份）, PROP（按比例）',
    `hashrate_price` DECIMAL(20,8) NOT NULL COMMENT '算力价格（每单位算力的价格）',
    `min_hashrate` DECIMAL(30,8) DEFAULT NULL COMMENT '最低算力门槛',
    `max_participants` INT(11) DEFAULT NULL COMMENT '最大参与用户数',
    `current_participants` INT(11) DEFAULT 0 COMMENT '当前参与用户数',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '矿池状态: ACTIVE（活跃）, SUSPENDED（暂停）, CLOSED（关闭）',
    `risk_level` VARCHAR(50) DEFAULT 'MEDIUM' COMMENT '风险等级: LOW（低风险）, MEDIUM（中风险）, HIGH（高风险）',
    `settlement_cycle` VARCHAR(50) NOT NULL DEFAULT 'DAILY' COMMENT '收益结算周期: DAILY（每日）, WEEKLY（每周）, MONTHLY（每月）',
    `description` TEXT DEFAULT NULL COMMENT '矿池描述',
    `risk_warning` TEXT DEFAULT NULL COMMENT '风险提示',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pool_code` (`pool_code`),
    KEY `idx_mining_currency` (`mining_currency`),
    KEY `idx_algorithm` (`algorithm`),
    KEY `idx_status` (`status`),
    KEY `idx_risk_level` (`risk_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='矿池表';

-- 算力类型表
CREATE TABLE IF NOT EXISTS `hashrate_types` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `type_name` VARCHAR(100) NOT NULL COMMENT '算力类型名称（如: SHA-256, Ethash）',
    `type_code` VARCHAR(50) NOT NULL COMMENT '算力类型代码',
    `algorithm` VARCHAR(50) NOT NULL COMMENT '挖矿算法',
    `unit` VARCHAR(20) NOT NULL DEFAULT 'TH/s' COMMENT '算力单位（如: TH/s, GH/s, MH/s）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '类型描述',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_code` (`type_code`),
    KEY `idx_algorithm` (`algorithm`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='算力类型表';

-- 用户算力租赁记录表
CREATE TABLE IF NOT EXISTS `hashrate_rentals` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pool_id` BIGINT(20) NOT NULL COMMENT '矿池ID',
    `hashrate_type_id` BIGINT(20) NOT NULL COMMENT '算力类型ID',
    `hashrate_amount` DECIMAL(30,8) NOT NULL COMMENT '租赁的算力数量',
    `unit` VARCHAR(20) NOT NULL COMMENT '算力单位',
    `rental_period` INT(11) NOT NULL COMMENT '租赁周期（天数）',
    `start_date` DATETIME NOT NULL COMMENT '租赁开始时间',
    `end_date` DATETIME NOT NULL COMMENT '租赁结束时间',
    `total_cost` DECIMAL(20,8) NOT NULL COMMENT '总租赁费用',
    `paid_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已支付金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '支付币种',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '租赁状态: ACTIVE（进行中）, EXPIRED（已过期）, CANCELLED（已取消）',
    `rental_order_no` VARCHAR(50) NOT NULL COMMENT '租赁订单号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_rental_order_no` (`rental_order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pool_id` (`pool_id`),
    KEY `idx_hashrate_type_id` (`hashrate_type_id`),
    KEY `idx_status` (`status`),
    KEY `idx_start_date` (`start_date`),
    KEY `idx_end_date` (`end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户算力租赁记录表';

-- 用户矿工记录表（用户分配到矿池的算力）
CREATE TABLE IF NOT EXISTS `mining_workers` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID（矿工）',
    `pool_id` BIGINT(20) NOT NULL COMMENT '矿池ID',
    `rental_id` BIGINT(20) DEFAULT NULL COMMENT '算力租赁记录ID',
    `hashrate_contribution` DECIMAL(30,8) NOT NULL COMMENT '算力贡献',
    `unit` VARCHAR(20) NOT NULL COMMENT '算力单位',
    `total_revenue` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总收益',
    `accumulated_revenue` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '累计收益',
    `last_settlement_time` DATETIME DEFAULT NULL COMMENT '上次结算时间',
    `join_time` DATETIME NOT NULL COMMENT '加入矿池时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）, INACTIVE（非活跃）, EXITED（已退出）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pool` (`user_id`, `pool_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pool_id` (`pool_id`),
    KEY `idx_rental_id` (`rental_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户矿工记录表';

-- 挖矿收益结算记录表
CREATE TABLE IF NOT EXISTS `mining_settlements` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `worker_id` BIGINT(20) NOT NULL COMMENT '矿工记录ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pool_id` BIGINT(20) NOT NULL COMMENT '矿池ID',
    `settlement_period_start` DATETIME NOT NULL COMMENT '结算周期开始时间',
    `settlement_period_end` DATETIME NOT NULL COMMENT '结算周期结束时间',
    `hashrate_contribution` DECIMAL(30,8) NOT NULL COMMENT '算力贡献',
    `pool_total_hashrate` DECIMAL(30,8) NOT NULL COMMENT '矿池总算力',
    `pool_revenue` DECIMAL(20,8) NOT NULL COMMENT '矿池收益（该周期）',
    `user_revenue` DECIMAL(20,8) NOT NULL COMMENT '用户应得收益',
    `currency` VARCHAR(20) NOT NULL COMMENT '收益币种',
    `distribution_rate` DECIMAL(10,6) NOT NULL COMMENT '分配比例（用户算力/矿池总算力）',
    `settlement_type` VARCHAR(50) NOT NULL COMMENT '结算类型: BLOCK_REWARD（区块奖励）, FEE（手续费）, BOTH（区块奖励+手续费）',
    `settlement_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '结算状态: PENDING（待结算）, SETTLED（已结算）, FAILED（结算失败）',
    `settlement_time` DATETIME DEFAULT NULL COMMENT '结算时间',
    `settlement_order_no` VARCHAR(50) DEFAULT NULL COMMENT '结算订单号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_worker_id` (`worker_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pool_id` (`pool_id`),
    KEY `idx_settlement_status` (`settlement_status`),
    KEY `idx_settlement_period_end` (`settlement_period_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挖矿收益结算记录表';

-- =====================================================
-- 25. 红包模块 (Red Packet Module)
-- =====================================================

-- 红包活动表
CREATE TABLE IF NOT EXISTS `red_packet_activities` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `activity_name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `activity_code` VARCHAR(50) NOT NULL COMMENT '活动代码（唯一标识）',
    `packet_type` VARCHAR(50) NOT NULL COMMENT '红包类型: CASH（现金红包）, COUPON（优惠券红包）, POINTS（积分红包）, DISCOUNT（折扣红包）',
    `amount_type` VARCHAR(50) NOT NULL COMMENT '金额类型: FIXED（固定金额）, RANDOM（随机金额）',
    `fixed_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '固定金额（固定金额红包）',
    `min_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最小金额（随机金额红包）',
    `max_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最大金额（随机金额红包）',
    `total_amount` DECIMAL(20,8) NOT NULL COMMENT '红包总金额',
    `total_count` INT(11) NOT NULL COMMENT '红包总数量',
    `issued_count` INT(11) NOT NULL DEFAULT 0 COMMENT '已发放数量',
    `used_count` INT(11) NOT NULL DEFAULT 0 COMMENT '已使用数量',
    `distribution_scope` VARCHAR(50) NOT NULL COMMENT '发放范围: SPECIFIC（特定用户）, ALL（全体用户）, VIP（VIP用户）',
    `receive_condition` VARCHAR(100) DEFAULT NULL COMMENT '领取条件: TRADE（交易金额限制）, CHECKIN（签到奖励）, INVITE（邀请奖励）, NONE（无限制）',
    `condition_value` DECIMAL(20,8) DEFAULT NULL COMMENT '条件值（如交易金额限制）',
    `valid_days` INT(11) NOT NULL DEFAULT 7 COMMENT '有效期（天数）',
    `use_scope` VARCHAR(500) DEFAULT NULL COMMENT '使用范围限制（JSON格式，如币种、交易类型等）',
    `use_time_limit` VARCHAR(500) DEFAULT NULL COMMENT '使用时间限制（JSON格式，如活动期间）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '活动状态: ACTIVE（进行中）, SUSPENDED（已暂停）, ENDED（已结束）',
    `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
    `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
    `description` TEXT DEFAULT NULL COMMENT '活动描述',
    `auto_issue` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否自动发放: 0否, 1是',
    `issue_cycle` VARCHAR(50) DEFAULT NULL COMMENT '发放周期: DAILY（每日）, WEEKLY（每周）, MONTHLY（每月）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_code` (`activity_code`),
    KEY `idx_packet_type` (`packet_type`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='红包活动表';

-- 用户红包领取记录表
CREATE TABLE IF NOT EXISTS `red_packet_receives` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `activity_id` BIGINT(20) NOT NULL COMMENT '红包活动ID',
    `packet_no` VARCHAR(50) NOT NULL COMMENT '红包编号（唯一标识）',
    `packet_type` VARCHAR(50) NOT NULL COMMENT '红包类型',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '红包金额',
    `remaining_amount` DECIMAL(20,8) NOT NULL COMMENT '剩余金额',
    `currency` VARCHAR(20) DEFAULT NULL COMMENT '币种（现金红包）',
    `discount_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '折扣比例（折扣红包）',
    `receive_time` DATETIME NOT NULL COMMENT '领取时间',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'VALID' COMMENT '状态: VALID（有效）, USED（已使用）, EXPIRED（已过期）, CANCELLED（已取消）',
    `use_count` INT(11) NOT NULL DEFAULT 0 COMMENT '使用次数',
    `max_use_count` INT(11) DEFAULT NULL COMMENT '最大使用次数（如优惠券可多次使用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_packet_no` (`packet_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_status` (`status`),
    KEY `idx_expire_time` (`expire_time`),
    KEY `idx_receive_time` (`receive_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户红包领取记录表';

-- 红包使用记录表
CREATE TABLE IF NOT EXISTS `red_packet_uses` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `packet_id` BIGINT(20) NOT NULL COMMENT '红包领取记录ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `activity_id` BIGINT(20) NOT NULL COMMENT '红包活动ID',
    `packet_no` VARCHAR(50) NOT NULL COMMENT '红包编号',
    `use_amount` DECIMAL(20,8) NOT NULL COMMENT '使用金额',
    `use_type` VARCHAR(50) NOT NULL COMMENT '使用类型: TRADE_FEE（交易手续费）, TRADE（交易）, WITHDRAW（提现）, OTHER（其他）',
    `use_target` VARCHAR(100) DEFAULT NULL COMMENT '使用目标（如交易对、订单号等）',
    `order_no` VARCHAR(50) DEFAULT NULL COMMENT '关联订单号',
    `before_amount` DECIMAL(20,8) NOT NULL COMMENT '使用前红包余额',
    `after_amount` DECIMAL(20,8) NOT NULL COMMENT '使用后红包余额',
    `use_time` DATETIME NOT NULL COMMENT '使用时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_packet_id` (`packet_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_packet_no` (`packet_no`),
    KEY `idx_use_time` (`use_time`),
    KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='红包使用记录表';

-- 红包过期记录表
CREATE TABLE IF NOT EXISTS `red_packet_expired` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `packet_id` BIGINT(20) NOT NULL COMMENT '红包领取记录ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `activity_id` BIGINT(20) NOT NULL COMMENT '红包活动ID',
    `packet_no` VARCHAR(50) NOT NULL COMMENT '红包编号',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '过期金额',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `process_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '处理状态: PENDING（待处理）, PROCESSED（已处理）, REISSUED（已重新发放）',
    `processed_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_packet_id` (`packet_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_expire_time` (`expire_time`),
    KEY `idx_process_status` (`process_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='红包过期记录表';

-- =====================================================
-- 26. 合约体验金模块 (Contract Experience Fund Module)
-- =====================================================

-- 合约体验金账户表
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

-- 合约体验金发放活动表
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
    `description` TEXT DEFAULT NULL COMMENT '活动描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_code` (`activity_code`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合约体验金发放活动表';

-- 合约体验金交易记录表
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

-- 合约体验金账户变更记录表
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
-- 测试数据插入
-- =====================================================

-- 理财产品测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `finance_products` (`product_name`, `product_code`, `product_type`, `risk_level`, `annual_rate`, `investment_cycle`, `min_investment_amount`, `max_investment_amount`, `total_raise_amount`, `supported_currency`, `lock_period`, `status`, `settlement_cycle`, `description`) VALUES
('USDT稳健理财30天', 'USDT-FIXED-30D', 'FIXED', 'CONSERVATIVE', 0.060000, 30, 100.00000000, 100000.00000000, 1000000.00000000, 'USDT', 30, 'AVAILABLE', 'MATURITY', '稳健型定期理财产品，年化收益率6%，锁仓30天'),
('BTC高收益理财180天', 'BTC-FIXED-180D', 'FIXED', 'HIGH_RISK', 0.150000, 180, 0.01000000, 10.00000000, 100.00000000, 'BTC', 180, 'AVAILABLE', 'MONTHLY', '高风险高收益理财产品，年化收益率15%，锁仓180天，按月结算'),
('USDT活期理财', 'USDT-FLEXIBLE', 'FLEXIBLE', 'CONSERVATIVE', 0.030000, NULL, 10.00000000, NULL, NULL, 'USDT', NULL, 'AVAILABLE', 'DAILY', '灵活型活期理财产品，年化收益率3%，可随时赎回，每日结算收益');

-- 用户投资记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `finance_investments` (`user_id`, `product_id`, `product_code`, `investment_amount`, `currency`, `start_date`, `end_date`, `remaining_principal`, `investment_order_no`, `status`, `lock_until`) VALUES
(1, 1, 'USDT-FIXED-30D', 1000.00000000, 'USDT', '2024-01-15 10:00:00', '2024-02-14 10:00:00', 1000.00000000, 'INV-20240115-001', 'ACTIVE', '2024-02-14 10:00:00'),
(2, 3, 'USDT-FLEXIBLE', 5000.00000000, 'USDT', '2024-01-15 09:00:00', NULL, 5000.00000000, 'INV-20240115-002', 'ACTIVE', NULL);

-- 理财产品收益结算记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `finance_profit_settlements` (`investment_id`, `user_id`, `product_id`, `settlement_period_start`, `settlement_period_end`, `principal_amount`, `profit_amount`, `currency`, `annual_rate`, `settlement_type`, `settlement_status`, `settlement_time`, `settlement_order_no`) VALUES
(2, 2, 3, '2024-01-15 00:00:00', '2024-01-16 00:00:00', 5000.00000000, 0.41095890, 'USDT', 0.030000, 'INTEREST', 'SETTLED', '2024-01-16 10:00:00', 'SETTLE-20240116-001'),
(2, 2, 3, '2024-01-16 00:00:00', '2024-01-17 00:00:00', 5000.00000000, 0.41095890, 'USDT', 0.030000, 'INTEREST', 'SETTLED', '2024-01-17 10:00:00', 'SETTLE-20240117-001');

-- 理财产品赎回记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `finance_redemptions` (`investment_id`, `user_id`, `product_id`, `redemption_amount`, `principal_amount`, `profit_amount`, `currency`, `redemption_type`, `redemption_fee`, `status`, `redemption_order_no`, `completed_time`) VALUES
(2, 2, 3, 1000.00000000, 1000.00000000, 0.00, 'USDT', 'PARTIAL', 0.00, 'COMPLETED', 'REDEEM-20240120-001', '2024-01-20 15:00:00');

-- 算力类型测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `hashrate_types` (`type_name`, `type_code`, `algorithm`, `unit`, `description`, `status`) VALUES
('SHA-256算力', 'SHA256', 'SHA-256', 'TH/s', '适用于BTC等SHA-256算法的算力', 'ACTIVE'),
('Ethash算力', 'ETHASH', 'Ethash', 'MH/s', '适用于ETH等Ethash算法的算力', 'ACTIVE');

-- 矿池测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `mining_pools` (`pool_name`, `pool_code`, `mining_currency`, `algorithm`, `total_hashrate`, `hashrate_price`, `distribution_method`, `settlement_cycle`, `description`, `status`) VALUES
('BTC矿池', 'BTC-POOL-001', 'BTC', 'SHA-256', 1000.00000000, 0.00100000, 'PPS', 'DAILY', 'BTC挖矿矿池，支持SHA-256算法，每日结算收益', 'ACTIVE'),
('ETH矿池', 'ETH-POOL-001', 'ETH', 'Ethash', 5000.00000000, 0.00050000, 'PPLNS', 'DAILY', 'ETH挖矿矿池，支持Ethash算法，每日结算收益', 'ACTIVE');

-- 用户算力租赁测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `hashrate_rentals` (`user_id`, `pool_id`, `hashrate_type_id`, `hashrate_amount`, `unit`, `rental_period`, `start_date`, `end_date`, `total_cost`, `paid_amount`, `currency`, `status`, `rental_order_no`) VALUES
(1, 1, 1, 10.00000000, 'TH/s', 30, '2024-01-15 10:00:00', '2024-02-14 10:00:00', 300.00000000, 300.00000000, 'USDT', 'ACTIVE', 'RENT-20240115-001'),
(2, 2, 2, 50.00000000, 'MH/s', 60, '2024-01-15 09:00:00', '2024-03-15 09:00:00', 1500.00000000, 1500.00000000, 'USDT', 'ACTIVE', 'RENT-20240115-002');

-- 用户矿工记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `mining_workers` (`user_id`, `pool_id`, `rental_id`, `hashrate_contribution`, `unit`, `join_time`, `status`) VALUES
(1, 1, 1, 10.00000000, 'TH/s', '2024-01-15 10:00:00', 'ACTIVE'),
(2, 2, 2, 50.00000000, 'MH/s', '2024-01-15 09:00:00', 'ACTIVE');

-- 挖矿收益结算记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `mining_settlements` (`worker_id`, `user_id`, `pool_id`, `settlement_period_start`, `settlement_period_end`, `hashrate_contribution`, `pool_total_hashrate`, `pool_revenue`, `user_revenue`, `currency`, `distribution_rate`, `settlement_type`, `settlement_status`, `settlement_time`, `settlement_order_no`) VALUES
(1, 1, 1, '2024-01-15 00:00:00', '2024-01-16 00:00:00', 10.00000000, 1000.00000000, 0.1, 0.001, 'BTC', 0.010000, 'BLOCK_REWARD', 'SETTLED', '2024-01-16 10:00:00', 'MINING-SETTLE-001'),
(2, 2, 2, '2024-01-15 00:00:00', '2024-01-16 00:00:00', 50.00000000, 5000.00000000, 0.5, 0.005, 'ETH', 0.010000, 'BLOCK_REWARD', 'SETTLED', '2024-01-16 10:00:00', 'MINING-SETTLE-002');

-- 红包活动测试数据（使用 INSERT IGNORE 避免重复插入）
-- 注意：total_amount 字段为 NOT NULL，折扣券类型也需要提供总金额（可以是0或一个合理的值）
INSERT IGNORE INTO `red_packet_activities` (`activity_name`, `activity_code`, `packet_type`, `amount_type`, `fixed_amount`, `total_amount`, `total_count`, `distribution_scope`, `receive_condition`, `valid_days`, `status`, `start_time`, `end_time`, `description`) VALUES
('新用户注册红包', 'REGISTER-2024', 'CASH', 'FIXED', 10.00000000, 100000.00000000, 10000, 'ALL', 'NONE', 7, 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59', '新用户注册即可领取10 USDT现金红包'),
('交易手续费折扣券', 'TRADE-FEE-2024', 'DISCOUNT', 'FIXED', 0.10000000, 500.00000000, 5000, 'ALL', 'TRADE', 30, 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59', '完成交易后领取，交易手续费9折优惠券');

-- 用户红包领取记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `red_packet_receives` (`user_id`, `activity_id`, `packet_no`, `packet_type`, `amount`, `remaining_amount`, `currency`, `receive_time`, `expire_time`, `status`) VALUES
(1, 1, 'RP-20240115-001', 'CASH', 10.00000000, 10.00000000, 'USDT', '2024-01-15 10:00:00', '2024-01-22 10:00:00', 'VALID'),
(2, 1, 'RP-20240115-002', 'CASH', 10.00000000, 10.00000000, 'USDT', '2024-01-15 09:00:00', '2024-01-22 09:00:00', 'VALID'),
(1, 2, 'RP-20240115-003', 'DISCOUNT', 0.10000000, 0.10000000, NULL, '2024-01-15 11:00:00', '2024-02-14 11:00:00', 'VALID');

-- 红包使用记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `red_packet_uses` (`packet_id`, `user_id`, `activity_id`, `packet_no`, `use_amount`, `use_type`, `use_target`, `order_no`, `before_amount`, `after_amount`, `use_time`) VALUES
(1, 1, 1, 'RP-20240115-001', 5.00000000, 'TRADE_FEE', 'BTC/USDT', 'ORDER-001', 10.00000000, 5.00000000, '2024-01-16 10:00:00');

-- 合约体验金发放活动测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `contract_experience_activities` (`activity_name`, `activity_code`, `experience_amount`, `valid_days`, `max_leverage`, `max_position`, `daily_trade_limit`, `target_users`, `receive_condition`, `status`, `start_time`, `end_time`, `description`) VALUES
('新用户体验金', 'NEW_USER_EXP-2024', 1000.00000000, 7, 10, 1000.00000000, 5, 'NEW', 'REGISTER', 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59', '新用户注册即可获得1000 USDT体验金，有效期7天'),
('VIP用户体验金', 'VIP_USER_EXP-2024', 5000.00000000, 30, 20, 5000.00000000, 10, 'VIP', 'KYC', 'ACTIVE', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'VIP用户完成KYC认证可获得5000 USDT体验金，有效期30天');

-- 合约体验金账户测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `contract_experience_accounts` (`user_id`, `account_no`, `balance`, `initial_amount`, `currency`, `max_leverage`, `max_position`, `daily_trade_limit`, `account_status`, `create_time`, `expire_time`, `total_profit`, `total_loss`, `total_trades`) VALUES
(1, 'EXP-ACC-001', 1050.00000000, 1000.00000000, 'USDT', 10, 1000.00000000, 5, 'ACTIVE', '2024-01-15 10:00:00', '2024-01-22 10:00:00', 50.00000000, 0.00000000, 3),
(2, 'EXP-ACC-002', 4800.00000000, 5000.00000000, 'USDT', 20, 5000.00000000, 10, 'ACTIVE', '2024-01-15 09:00:00', '2024-02-14 09:00:00', 0.00000000, 200.00000000, 5);

-- 合约体验金交易记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `contract_experience_trades` (`account_id`, `user_id`, `trade_no`, `contract_type`, `pair_name`, `trade_type`, `order_type`, `leverage`, `position_size`, `entry_price`, `exit_price`, `profit_loss`, `open_time`, `close_time`, `trade_status`) VALUES
(1, 1, 'EXP-TRADE-001', 'FUTURES_USDT', 'BTC/USDT', 'OPEN_LONG', 'MARKET', 10, 0.1, 45000.00, 45500.00, 50.00000000, '2024-01-15 10:30:00', '2024-01-15 15:00:00', 'CLOSED'),
(2, 2, 'EXP-TRADE-002', 'FUTURES_USDT', 'ETH/USDT', 'OPEN_SHORT', 'LIMIT', 20, 1.0, 2800.00, 2820.00, -20.00000000, '2024-01-15 09:30:00', '2024-01-15 14:00:00', 'CLOSED');

-- 合约体验金账户变更记录测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `contract_experience_account_changes` (`account_id`, `user_id`, `change_type`, `change_amount`, `before_balance`, `after_balance`, `trade_id`, `activity_id`, `remark`) VALUES
(1, 1, 'INIT', 1000.00000000, 0.00000000, 1000.00000000, NULL, 1, '新用户注册获得体验金'),
(1, 1, 'TRADE_PROFIT', 50.00000000, 1000.00000000, 1050.00000000, 1, NULL, '交易盈利'),
(2, 2, 'INIT', 5000.00000000, 0.00000000, 5000.00000000, NULL, 2, 'VIP用户获得体验金'),
(2, 2, 'TRADE_LOSS', -200.00000000, 5000.00000000, 4800.00000000, 2, NULL, '交易亏损');

