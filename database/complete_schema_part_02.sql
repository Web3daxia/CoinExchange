-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第2部分
-- 包含：现货交易模块、USDT本位合约模块、币本位合约模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 3. 现货交易模块 (Spot Trading Module)
-- =====================================================

-- 交易对表
CREATE TABLE IF NOT EXISTS `trading_pairs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如 BTC/USDT',
    `base_currency` VARCHAR(20) DEFAULT NULL COMMENT '基础货币，如 BTC',
    `quote_currency` VARCHAR(20) DEFAULT NULL COMMENT '计价货币，如 USDT',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `price_change_24h` DECIMAL(10,4) DEFAULT NULL COMMENT '24小时价格变化',
    `volume_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时交易量',
    `high_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最高价',
    `low_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最低价',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_name` (`pair_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易对表';

-- 现货订单表
CREATE TABLE IF NOT EXISTS `spot_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY, SELL',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '价格（限价单）',
    `stop_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `filled_quantity` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交数量',
    `filled_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交金额',
    `fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, FILLED, PARTIALLY_FILLED, CANCELLED, REJECTED',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pair_name` (`pair_name`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='现货订单表';

-- 高级订单表
CREATE TABLE IF NOT EXISTS `advanced_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `order_type` VARCHAR(50) NOT NULL COMMENT '订单类型: ICEBERG, TWAP, VWAP等',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY, SELL',
    `total_quantity` DECIMAL(20,8) NOT NULL COMMENT '总数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '价格',
    `strategy_params` TEXT DEFAULT NULL COMMENT '策略参数（JSON格式）',
    `filled_quantity` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交数量',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, EXECUTING, FILLED, CANCELLED',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='高级订单表';

-- 市场数据配置表
CREATE TABLE IF NOT EXISTS `market_data_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `data_source` VARCHAR(50) NOT NULL COMMENT '数据源: BINANCE, OKX, GATE, HUOBI',
    `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否激活',
    `priority` INT(11) DEFAULT 0 COMMENT '优先级',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_source` (`pair_name`, `data_source`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场数据配置表';

-- 市场警报表
CREATE TABLE IF NOT EXISTS `market_alerts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `alert_type` VARCHAR(50) NOT NULL COMMENT '警报类型: PRICE_ABOVE, PRICE_BELOW, VOLUME等',
    `target_price` DECIMAL(20,8) DEFAULT NULL COMMENT '目标价格',
    `is_triggered` TINYINT(1) DEFAULT 0 COMMENT '是否已触发',
    `triggered_at` DATETIME DEFAULT NULL COMMENT '触发时间',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, TRIGGERED, CANCELLED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pair_name` (`pair_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场警报表';

-- =====================================================
-- 4. USDT本位永续合约模块 (Futures USDT Module)
-- =====================================================

-- USDT本位合约表
CREATE TABLE IF NOT EXISTS `futures_contracts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如 BTC/USDT',
    `base_currency` VARCHAR(20) DEFAULT NULL COMMENT '基础货币，如 BTC',
    `quote_currency` VARCHAR(20) DEFAULT NULL COMMENT '计价货币，如 USDT',
    `contract_type` VARCHAR(50) DEFAULT 'USDT_MARGINED' COMMENT '合约类型: USDT_MARGINED, COIN_MARGINED',
    `settlement_currency` VARCHAR(20) DEFAULT NULL COMMENT '结算货币',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `index_price` DECIMAL(20,8) DEFAULT NULL COMMENT '指数价格',
    `mark_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标记价格',
    `funding_rate` DECIMAL(10,8) DEFAULT NULL COMMENT '资金费率',
    `next_funding_time` DATETIME DEFAULT NULL COMMENT '下次资金费率结算时间',
    `price_change_24h` DECIMAL(10,4) DEFAULT NULL COMMENT '24小时价格变化',
    `volume_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时交易量',
    `amount_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时成交额',
    `high_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最高价',
    `low_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最低价',
    `max_leverage` INT(11) DEFAULT NULL COMMENT '最大杠杆倍数',
    `min_leverage` INT(11) DEFAULT NULL COMMENT '最小杠杆倍数',
    `contract_size` DECIMAL(20,8) DEFAULT NULL COMMENT '合约面值',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_name` (`pair_name`),
    KEY `idx_contract_type` (`contract_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='USDT本位合约表';

-- USDT本位订单表
CREATE TABLE IF NOT EXISTS `futures_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: MARKET, LIMIT, STOP等',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY, SELL',
    `position_side` VARCHAR(10) DEFAULT NULL COMMENT '仓位方向: LONG, SHORT',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '价格',
    `stop_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `leverage` INT(11) DEFAULT NULL COMMENT '杠杆倍数',
    `margin_mode` VARCHAR(20) DEFAULT NULL COMMENT '保证金模式: CROSS, ISOLATED',
    `filled_quantity` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交数量',
    `filled_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交金额',
    `fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='USDT本位订单表';

-- USDT本位仓位表
CREATE TABLE IF NOT EXISTS `futures_positions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '仓位ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `position_side` VARCHAR(10) NOT NULL COMMENT '仓位方向: LONG, SHORT',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '持仓数量',
    `entry_price` DECIMAL(20,8) NOT NULL COMMENT '开仓价格',
    `mark_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标记价格',
    `leverage` INT(11) DEFAULT NULL COMMENT '杠杆倍数',
    `margin_mode` VARCHAR(20) DEFAULT NULL COMMENT '保证金模式: CROSS, ISOLATED',
    `margin` DECIMAL(20,8) DEFAULT NULL COMMENT '保证金',
    `unrealized_pnl` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '未实现盈亏',
    `realized_pnl` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已实现盈亏',
    `liquidation_price` DECIMAL(20,8) DEFAULT NULL COMMENT '强平价格',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, CLOSED, LIQUIDATED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='USDT本位仓位表';

-- USDT本位合约交易规则配置表
CREATE TABLE IF NOT EXISTS `futures_usdt_contract_rules` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `contract_symbol` VARCHAR(50) NOT NULL COMMENT '合约交易对，如: BTC/USDT',
    `face_value` DECIMAL(30,8) NOT NULL COMMENT '合约面值',
    `contract_multiplier` DECIMAL(30,8) NOT NULL DEFAULT 1.00000000 COMMENT '合约乘数',
    `maker_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00020000 COMMENT 'Maker手续费率',
    `taker_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00050000 COMMENT 'Taker手续费率',
    `maintenance_margin_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00500000 COMMENT '维持保证金率',
    `liquidation_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00050000 COMMENT '爆仓手续费率',
    `funding_rate_interval` INT(11) NOT NULL DEFAULT 8 COMMENT '资金费用收取间隔（小时），默认8小时',
    `funding_rate_times` VARCHAR(50) NOT NULL DEFAULT '08:00,16:00,00:00' COMMENT '资金费用收取时间点（24小时制，逗号分隔）',
    `max_leverage` INT(11) NOT NULL DEFAULT 125 COMMENT '最大杠杆倍数',
    `min_leverage` INT(11) NOT NULL DEFAULT 1 COMMENT '最小杠杆倍数',
    `default_leverage` INT(11) NOT NULL DEFAULT 10 COMMENT '默认杠杆倍数',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contract_symbol` (`contract_symbol`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='USDT本位合约交易规则配置表';

-- USDT本位合约资金费用记录表
CREATE TABLE IF NOT EXISTS `futures_usdt_funding_fee_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `contract_symbol` VARCHAR(50) NOT NULL COMMENT '合约交易对',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `position_id` BIGINT(20) NOT NULL COMMENT '持仓ID',
    `funding_rate` DECIMAL(20,8) NOT NULL COMMENT '资金费率',
    `position_size` DECIMAL(30,8) NOT NULL COMMENT '持仓张数',
    `mark_price` DECIMAL(30,8) NOT NULL COMMENT '标记价格',
    `funding_fee` DECIMAL(30,8) NOT NULL COMMENT '资金费用（正数为收取，负数为支付）',
    `margin_mode` VARCHAR(20) NOT NULL COMMENT '保证金模式：CROSS（全仓）, ISOLATED（逐仓）',
    `settlement_time` DATETIME NOT NULL COMMENT '结算时间（8:00, 16:00, 00:00）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_contract_symbol` (`contract_symbol`),
    KEY `idx_settlement_time` (`settlement_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='USDT本位合约资金费用记录表';

-- =====================================================
-- 5. 币本位永续合约模块 (Futures Coin Module)
-- =====================================================

-- 币本位合约表
CREATE TABLE IF NOT EXISTS `coin_futures_contracts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如 BTC/BTC',
    `base_currency` VARCHAR(20) DEFAULT NULL COMMENT '基础货币，如 BTC',
    `quote_currency` VARCHAR(20) DEFAULT NULL COMMENT '计价货币，如 BTC',
    `contract_type` VARCHAR(50) DEFAULT 'COIN_MARGINED' COMMENT '合约类型',
    `settlement_currency` VARCHAR(20) DEFAULT NULL COMMENT '结算货币',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `index_price` DECIMAL(20,8) DEFAULT NULL COMMENT '指数价格',
    `mark_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标记价格',
    `funding_rate` DECIMAL(10,8) DEFAULT NULL COMMENT '资金费率',
    `next_funding_time` DATETIME DEFAULT NULL COMMENT '下次资金费率结算时间',
    `price_change_24h` DECIMAL(10,4) DEFAULT NULL COMMENT '24小时价格变化',
    `volume_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时交易量',
    `amount_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时成交额',
    `high_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最高价',
    `low_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时最低价',
    `max_leverage` INT(11) DEFAULT NULL COMMENT '最大杠杆倍数',
    `min_leverage` INT(11) DEFAULT NULL COMMENT '最小杠杆倍数',
    `contract_size` DECIMAL(20,8) DEFAULT NULL COMMENT '合约面值',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_name` (`pair_name`),
    KEY `idx_contract_type` (`contract_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币本位合约表';

-- 币本位订单表
CREATE TABLE IF NOT EXISTS `coin_futures_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY, SELL',
    `position_side` VARCHAR(10) DEFAULT NULL COMMENT '仓位方向: LONG, SHORT',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '价格',
    `stop_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `leverage` INT(11) DEFAULT NULL COMMENT '杠杆倍数',
    `margin_mode` VARCHAR(20) DEFAULT NULL COMMENT '保证金模式: CROSS, ISOLATED',
    `filled_quantity` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交数量',
    `filled_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已成交金额',
    `fee` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币本位订单表';

-- 币本位仓位表
CREATE TABLE IF NOT EXISTS `coin_futures_positions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '仓位ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `position_side` VARCHAR(10) NOT NULL COMMENT '仓位方向: LONG, SHORT',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '持仓数量',
    `entry_price` DECIMAL(20,8) NOT NULL COMMENT '开仓价格',
    `mark_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标记价格',
    `leverage` INT(11) DEFAULT NULL COMMENT '杠杆倍数',
    `margin_mode` VARCHAR(20) DEFAULT NULL COMMENT '保证金模式: CROSS, ISOLATED',
    `margin` DECIMAL(20,8) DEFAULT NULL COMMENT '保证金（基础资产）',
    `unrealized_pnl` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '未实现盈亏（基础资产）',
    `realized_pnl` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已实现盈亏（基础资产）',
    `liquidation_price` DECIMAL(20,8) DEFAULT NULL COMMENT '强平价格',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, CLOSED, LIQUIDATED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币本位仓位表';

-- 币本位合约交易规则配置表
CREATE TABLE IF NOT EXISTS `futures_coin_contract_rules` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `contract_symbol` VARCHAR(50) NOT NULL COMMENT '合约交易对，如: BTC/USD',
    `face_value` DECIMAL(30,8) NOT NULL COMMENT '合约面值',
    `contract_multiplier` DECIMAL(30,8) NOT NULL DEFAULT 0.00010000 COMMENT '合约乘数',
    `maker_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00020000 COMMENT 'Maker手续费率',
    `taker_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00050000 COMMENT 'Taker手续费率',
    `maintenance_margin_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00500000 COMMENT '维持保证金率',
    `liquidation_fee_rate` DECIMAL(20,8) NOT NULL DEFAULT 0.00050000 COMMENT '爆仓手续费率',
    `funding_rate_interval` INT(11) NOT NULL DEFAULT 8 COMMENT '资金费用收取间隔（小时），默认8小时',
    `funding_rate_times` VARCHAR(50) NOT NULL DEFAULT '08:00,16:00,00:00' COMMENT '资金费用收取时间点（24小时制，逗号分隔）',
    `max_leverage` INT(11) NOT NULL DEFAULT 125 COMMENT '最大杠杆倍数',
    `min_leverage` INT(11) NOT NULL DEFAULT 1 COMMENT '最小杠杆倍数',
    `default_leverage` INT(11) NOT NULL DEFAULT 10 COMMENT '默认杠杆倍数',
    `base_currency` VARCHAR(20) NOT NULL COMMENT '基础币种（如BTC）',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contract_symbol` (`contract_symbol`),
    KEY `idx_status` (`status`),
    KEY `idx_base_currency` (`base_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='币本位合约交易规则配置表';

-- 币本位合约资金费用记录表
CREATE TABLE IF NOT EXISTS `futures_coin_funding_fee_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `contract_symbol` VARCHAR(50) NOT NULL COMMENT '合约交易对',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `position_id` BIGINT(20) NOT NULL COMMENT '持仓ID',
    `funding_rate` DECIMAL(20,8) NOT NULL COMMENT '资金费率',
    `position_size` DECIMAL(30,8) NOT NULL COMMENT '持仓张数',
    `mark_price` DECIMAL(30,8) NOT NULL COMMENT '标记价格',
    `funding_fee` DECIMAL(30,8) NOT NULL COMMENT '资金费用（正数为收取，负数为支付）',
    `margin_mode` VARCHAR(20) NOT NULL COMMENT '保证金模式：CROSS（全仓）, ISOLATED（逐仓）',
    `base_currency` VARCHAR(20) NOT NULL COMMENT '基础币种',
    `settlement_time` DATETIME NOT NULL COMMENT '结算时间（8:00, 16:00, 00:00）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_contract_symbol` (`contract_symbol`),
    KEY `idx_settlement_time` (`settlement_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='币本位合约资金费用记录表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 交易对表测试数据
INSERT INTO `trading_pairs` (`id`, `pair_name`, `base_currency`, `quote_currency`, `current_price`, `price_change_24h`, `volume_24h`, `high_24h`, `low_24h`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USDT', 'BTC', 'USDT', 50000.00000000, 2.5000, 1000.50000000, 51000.00000000, 49000.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USDT', 'ETH', 'USDT', 3000.00000000, -1.2000, 5000.00000000, 3100.00000000, 2950.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 现货订单表测试数据
INSERT INTO `spot_orders` (`id`, `user_id`, `pair_name`, `order_type`, `side`, `quantity`, `price`, `filled_quantity`, `filled_amount`, `fee`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT', 'LIMIT', 'BUY', 0.10000000, 49000.00000000, 0.10000000, 4900.00000000, 4.90000000, 'FILLED', '2024-01-10 10:00:00', '2024-01-10 10:05:00'),
(2, 2, 'ETH/USDT', 'MARKET', 'SELL', 1.00000000, NULL, 1.00000000, 3000.00000000, 3.00000000, 'FILLED', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 高级订单表测试数据
INSERT INTO `advanced_orders` (`id`, `user_id`, `pair_name`, `order_type`, `side`, `total_quantity`, `price`, `strategy_params`, `filled_quantity`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT', 'ICEBERG', 'BUY', 1.00000000, 50000.00000000, '{"visibleQuantity": 0.1, "totalQuantity": 1.0}', 0.20000000, 'EXECUTING', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 'ETH/USDT', 'TWAP', 'SELL', 5.00000000, 3000.00000000, '{"duration": 3600, "interval": 300}', 1.50000000, 'EXECUTING', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 市场数据配置表测试数据
INSERT INTO `market_data_configs` (`id`, `pair_name`, `data_source`, `is_active`, `priority`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USDT', 'BINANCE', 1, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USDT', 'OKX', 1, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 市场警报表测试数据
INSERT INTO `market_alerts` (`id`, `user_id`, `pair_name`, `alert_type`, `target_price`, `is_triggered`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT', 'PRICE_ABOVE', 51000.00000000, 0, 'ACTIVE', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 'ETH/USDT', 'PRICE_BELOW', 2900.00000000, 0, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- USDT本位合约表测试数据
INSERT INTO `futures_contracts` (`id`, `pair_name`, `base_currency`, `quote_currency`, `contract_type`, `settlement_currency`, `current_price`, `index_price`, `mark_price`, `funding_rate`, `next_funding_time`, `price_change_24h`, `volume_24h`, `amount_24h`, `high_24h`, `low_24h`, `max_leverage`, `min_leverage`, `contract_size`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USDT', 'BTC', 'USDT', 'USDT_MARGINED', 'USDT', 50000.00000000, 50010.00000000, 50005.00000000, 0.00010000, '2024-01-16 08:00:00', 2.5000, 5000.00000000, 250000000.00000000, 51000.00000000, 49000.00000000, 125, 1, 1.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USDT', 'ETH', 'USDT', 'USDT_MARGINED', 'USDT', 3000.00000000, 3005.00000000, 3002.00000000, 0.00008000, '2024-01-16 08:00:00', -1.2000, 10000.00000000, 30000000.00000000, 3100.00000000, 2950.00000000, 125, 1, 1.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- USDT本位订单表测试数据
INSERT INTO `futures_orders` (`id`, `user_id`, `contract_id`, `order_type`, `side`, `position_side`, `quantity`, `price`, `leverage`, `margin_mode`, `filled_quantity`, `filled_amount`, `fee`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'LIMIT', 'BUY', 'LONG', 0.10000000, 49000.00000000, 10, 'ISOLATED', 0.10000000, 4900.00000000, 2.45000000, 'FILLED', '2024-01-10 10:00:00', '2024-01-10 10:05:00'),
(2, 2, 2, 'MARKET', 'SELL', 'SHORT', 1.00000000, NULL, 20, 'CROSS', 1.00000000, 3000.00000000, 1.50000000, 'FILLED', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- USDT本位仓位表测试数据
INSERT INTO `futures_positions` (`id`, `user_id`, `contract_id`, `position_side`, `quantity`, `entry_price`, `mark_price`, `leverage`, `margin_mode`, `margin`, `unrealized_pnl`, `realized_pnl`, `liquidation_price`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'LONG', 0.10000000, 49000.00000000, 50005.00000000, 10, 'ISOLATED', 490.00000000, 100.50000000, 0.00000000, 44100.00000000, 'ACTIVE', '2024-01-10 10:05:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'SHORT', 1.00000000, 3000.00000000, 3002.00000000, 20, 'CROSS', 150.00000000, -2.00000000, 0.00000000, 3150.00000000, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- USDT本位合约交易规则配置表测试数据
INSERT INTO `futures_usdt_contract_rules` (`id`, `contract_symbol`, `face_value`, `contract_multiplier`, `maker_fee_rate`, `taker_fee_rate`, `maintenance_margin_rate`, `liquidation_fee_rate`, `max_leverage`, `default_leverage`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USDT', 1.00000000, 1.00000000, 0.00020000, 0.00050000, 0.00500000, 0.00050000, 125, 10, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USDT', 1.00000000, 1.00000000, 0.00020000, 0.00050000, 0.00500000, 0.00050000, 125, 10, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- USDT本位合约资金费用记录表测试数据
INSERT INTO `futures_usdt_funding_fee_records` (`id`, `contract_symbol`, `user_id`, `position_id`, `funding_rate`, `position_size`, `mark_price`, `funding_fee`, `margin_mode`, `settlement_time`, `created_at`) VALUES
(1, 'BTC/USDT', 1, 1, 0.00010000, 0.10000000, 50005.00000000, 5.00050000, 'ISOLATED', '2024-01-16 08:00:00', '2024-01-16 08:00:00'),
(2, 'ETH/USDT', 2, 2, 0.00008000, 1.00000000, 3002.00000000, 0.24016000, 'CROSS', '2024-01-16 08:00:00', '2024-01-16 08:00:00');

-- 币本位合约表测试数据
INSERT INTO `coin_futures_contracts` (`id`, `pair_name`, `base_currency`, `quote_currency`, `contract_type`, `settlement_currency`, `current_price`, `index_price`, `mark_price`, `funding_rate`, `next_funding_time`, `price_change_24h`, `volume_24h`, `amount_24h`, `high_24h`, `low_24h`, `max_leverage`, `min_leverage`, `contract_size`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USD', 'BTC', 'BTC', 'COIN_MARGINED', 'BTC', 50000.00000000, 50010.00000000, 50005.00000000, 0.00010000, '2024-01-16 08:00:00', 2.5000, 100.00000000, 5000000.00000000, 51000.00000000, 49000.00000000, 125, 1, 100.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USD', 'ETH', 'ETH', 'COIN_MARGINED', 'ETH', 3000.00000000, 3005.00000000, 3002.00000000, 0.00008000, '2024-01-16 08:00:00', -1.2000, 500.00000000, 1500000.00000000, 3100.00000000, 2950.00000000, 125, 1, 10.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 币本位订单表测试数据
INSERT INTO `coin_futures_orders` (`id`, `user_id`, `contract_id`, `order_type`, `side`, `position_side`, `quantity`, `price`, `leverage`, `margin_mode`, `filled_quantity`, `filled_amount`, `fee`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'LIMIT', 'BUY', 'LONG', 0.10000000, 49000.00000000, 10, 'ISOLATED', 0.10000000, 4900.00000000, 0.00009800, 'FILLED', '2024-01-10 10:00:00', '2024-01-10 10:05:00'),
(2, 2, 2, 'MARKET', 'SELL', 'SHORT', 1.00000000, NULL, 20, 'CROSS', 1.00000000, 3000.00000000, 0.00300000, 'FILLED', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 币本位仓位表测试数据
INSERT INTO `coin_futures_positions` (`id`, `user_id`, `contract_id`, `position_side`, `quantity`, `entry_price`, `mark_price`, `leverage`, `margin_mode`, `margin`, `unrealized_pnl`, `realized_pnl`, `liquidation_price`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'LONG', 0.10000000, 49000.00000000, 50005.00000000, 10, 'ISOLATED', 0.04900000, 0.00100500, 0.00000000, 44100.00000000, 'ACTIVE', '2024-01-10 10:05:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'SHORT', 1.00000000, 3000.00000000, 3002.00000000, 20, 'CROSS', 0.15000000, -0.00000200, 0.00000000, 3150.00000000, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 币本位合约交易规则配置表测试数据
INSERT INTO `futures_coin_contract_rules` (`id`, `contract_symbol`, `face_value`, `contract_multiplier`, `maker_fee_rate`, `taker_fee_rate`, `maintenance_margin_rate`, `liquidation_fee_rate`, `max_leverage`, `default_leverage`, `base_currency`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USD', 100.00000000, 0.00010000, 0.00020000, 0.00050000, 0.00500000, 0.00050000, 125, 10, 'BTC', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH/USD', 10.00000000, 0.00100000, 0.00020000, 0.00050000, 0.00500000, 0.00050000, 125, 10, 'ETH', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 币本位合约资金费用记录表测试数据
INSERT INTO `futures_coin_funding_fee_records` (`id`, `contract_symbol`, `user_id`, `position_id`, `funding_rate`, `position_size`, `mark_price`, `funding_fee`, `margin_mode`, `base_currency`, `settlement_time`, `created_at`) VALUES
(1, 'BTC/USD', 1, 1, 0.00010000, 0.10000000, 50005.00000000, 0.00050005, 'ISOLATED', 'BTC', '2024-01-16 08:00:00', '2024-01-16 08:00:00'),
(2, 'ETH/USD', 2, 2, 0.00008000, 1.00000000, 3002.00000000, 0.00024016, 'CROSS', 'ETH', '2024-01-16 08:00:00', '2024-01-16 08:00:00');












