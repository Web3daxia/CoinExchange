-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第4部分
-- 包含：期权交易模块、杠杆交易模块、交割合约模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 6. 期权交易模块 (Options Trading Module)
-- =====================================================

-- 期权合约表
CREATE TABLE IF NOT EXISTS `option_contracts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如 BTC/USDT',
    `base_currency` VARCHAR(20) NOT NULL COMMENT '基础货币，如 BTC',
    `quote_currency` VARCHAR(20) NOT NULL COMMENT '计价货币，如 USDT',
    `option_type` VARCHAR(20) NOT NULL COMMENT '期权类型: CALL（看涨期权）, PUT（看跌期权）',
    `exercise_type` VARCHAR(20) NOT NULL COMMENT '行使类型: AMERICAN（美式期权）, EUROPEAN（欧式期权）',
    `strike_price` DECIMAL(20,8) NOT NULL COMMENT '执行价格',
    `expiry_date` DATETIME NOT NULL COMMENT '到期日期',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前期权价格（期权费）',
    `underlying_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标的资产当前价格',
    `theoretical_price` DECIMAL(20,8) DEFAULT NULL COMMENT '理论价格（通过定价模型计算）',
    `implied_volatility` DECIMAL(10,8) DEFAULT NULL COMMENT '隐含波动率',
    `volume_24h` DECIMAL(20,8) DEFAULT NULL COMMENT '24小时交易量',
    `open_interest` DECIMAL(20,8) DEFAULT NULL COMMENT '持仓量',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）, EXPIRED（已到期）, SETTLED（已结算）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_pair_name` (`pair_name`),
    KEY `idx_option_type` (`option_type`),
    KEY `idx_expiry_date` (`expiry_date`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权合约表';

-- 期权订单表
CREATE TABLE IF NOT EXISTS `option_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '期权合约ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: OPEN（开仓）, CLOSE（平仓）',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入期权）, SELL（卖出期权）',
    `option_type` VARCHAR(20) NOT NULL COMMENT '期权类型: CALL, PUT',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '期权数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '期权价格（期权费）',
    `total_amount` DECIMAL(20,8) NOT NULL COMMENT '总金额（数量 * 价格）',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, FILLED, CANCELLED, REJECTED',
    `filled_quantity` DECIMAL(20,8) DEFAULT NULL COMMENT '已成交数量',
    `filled_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '已成交金额',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权订单表';

-- 期权持仓表
CREATE TABLE IF NOT EXISTS `option_positions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '持仓ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '期权合约ID',
    `option_type` VARCHAR(20) NOT NULL COMMENT '期权类型: CALL, PUT',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: LONG（持有期权）, SHORT（卖出期权）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '持仓数量',
    `average_price` DECIMAL(20,8) NOT NULL COMMENT '平均开仓价格（期权费）',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前期权价格',
    `underlying_price` DECIMAL(20,8) DEFAULT NULL COMMENT '标的资产当前价格',
    `strike_price` DECIMAL(20,8) NOT NULL COMMENT '执行价格',
    `unrealized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '未实现盈亏',
    `realized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '已实现盈亏',
    `expiry_date` DATETIME NOT NULL COMMENT '到期日期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, EXERCISED, EXPIRED, CLOSED',
    `margin` DECIMAL(20,8) DEFAULT NULL COMMENT '保证金',
    `is_in_the_money` TINYINT(1) DEFAULT NULL COMMENT '是否实值（in-the-money）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`),
    KEY `idx_expiry_date` (`expiry_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权持仓表';

-- 期权行使记录表
CREATE TABLE IF NOT EXISTS `option_exercises` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `position_id` BIGINT(20) NOT NULL COMMENT '期权持仓ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '期权合约ID',
    `option_type` VARCHAR(20) NOT NULL COMMENT '期权类型: CALL, PUT',
    `exercise_type` VARCHAR(20) NOT NULL COMMENT '行使类型: MANUAL（手动行使）, AUTO（自动行使）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '行使数量',
    `strike_price` DECIMAL(20,8) NOT NULL COMMENT '执行价格',
    `underlying_price` DECIMAL(20,8) NOT NULL COMMENT '行使时的标的资产价格',
    `exercise_price` DECIMAL(20,8) NOT NULL COMMENT '行使价格（执行价格）',
    `profit_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '盈亏',
    `exercise_fee` DECIMAL(20,8) DEFAULT NULL COMMENT '行使手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, EXECUTED, FAILED',
    `executed_at` DATETIME DEFAULT NULL COMMENT '执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权行使记录表';

-- 期权策略表
CREATE TABLE IF NOT EXISTS `option_strategies` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `strategy_name` VARCHAR(100) DEFAULT NULL COMMENT '策略名称',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: STRADDLE, BUTTERFLY, VERTICAL, CALENDAR',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `strategy_params` TEXT DEFAULT NULL COMMENT '策略参数（JSON格式）',
    `total_cost` DECIMAL(20,8) DEFAULT NULL COMMENT '总成本',
    `current_value` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价值',
    `unrealized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '未实现盈亏',
    `realized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '已实现盈亏',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, CLOSED, EXPIRED',
    `expiry_date` DATETIME DEFAULT NULL COMMENT '策略到期日期',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_strategy_type` (`strategy_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权策略表';

-- 期权风险警报表
CREATE TABLE IF NOT EXISTS `option_risk_alerts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `position_id` BIGINT(20) DEFAULT NULL COMMENT '期权持仓ID（如果为空，则适用于所有持仓）',
    `alert_type` VARCHAR(20) NOT NULL COMMENT '警报类型: PROFIT（盈利警报）, LOSS（亏损警报）, EXPIRY（到期警报）',
    `threshold` DECIMAL(20,8) NOT NULL COMMENT '阈值（盈利或亏损金额）',
    `threshold_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '阈值百分比',
    `is_triggered` TINYINT(1) DEFAULT 0 COMMENT '是否已触发',
    `triggered_at` DATETIME DEFAULT NULL COMMENT '触发时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, TRIGGERED, CANCELLED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期权风险警报表';

-- =====================================================
-- 7. 杠杆交易模块 (Leveraged Trading Module)
-- =====================================================

-- 杠杆账户表
CREATE TABLE IF NOT EXISTS `leveraged_accounts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如 BTC/USDT',
    `leverage` INT(11) NOT NULL COMMENT '杠杆倍数，如 2, 5, 10, 20',
    `max_leverage` INT(11) NOT NULL COMMENT '最大杠杆倍数限制',
    `available_balance` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '可用余额',
    `borrowed_amount` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '借入资金',
    `margin` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '保证金',
    `initial_margin` DECIMAL(20,8) DEFAULT NULL COMMENT '初始保证金',
    `maintenance_margin` DECIMAL(20,8) DEFAULT NULL COMMENT '维持保证金',
    `margin_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '保证金率',
    `interest_rate` DECIMAL(10,8) DEFAULT NULL COMMENT '借贷利率',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、FROZEN（冻结）、CLOSED（关闭）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pair` (`user_id`, `pair_name`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆账户表';

-- 杠杆订单表
CREATE TABLE IF NOT EXISTS `leveraged_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '关联的杠杆账户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: MARKET（市价单）、LIMIT（限价单）、STOP_LOSS（止损单）、TAKE_PROFIT（止盈单）、STOP_LIMIT（止损限价单）、CONDITIONAL（条件单）',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入）、SELL（卖出）',
    `action` VARCHAR(10) NOT NULL COMMENT '操作: OPEN（开仓）、CLOSE（平仓）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '数量',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '价格（限价单）',
    `stop_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `trigger_price` DECIMAL(20,8) DEFAULT NULL COMMENT '触发价格（条件单）',
    `condition_type` VARCHAR(50) DEFAULT NULL COMMENT '条件类型（条件单）',
    `leverage` INT(11) NOT NULL COMMENT '杠杆倍数',
    `filled_quantity` DECIMAL(20,8) DEFAULT NULL COMMENT '已成交数量',
    `filled_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '已成交金额',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待成交）、FILLED（已成交）、PARTIALLY_FILLED（部分成交）、CANCELLED（已取消）、REJECTED（已拒绝）、TRIGGERED（已触发）',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `triggered_at` DATETIME DEFAULT NULL COMMENT '触发时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_status` (`status`),
    KEY `idx_order_type` (`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆订单表';

-- 杠杆仓位表
CREATE TABLE IF NOT EXISTS `leveraged_positions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '仓位ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '关联的杠杆账户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: LONG（做多）、SHORT（做空）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '持仓数量',
    `entry_price` DECIMAL(20,8) NOT NULL COMMENT '开仓价格',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `leverage` INT(11) NOT NULL COMMENT '杠杆倍数',
    `margin` DECIMAL(20,8) NOT NULL COMMENT '保证金',
    `initial_margin` DECIMAL(20,8) NOT NULL COMMENT '初始保证金',
    `maintenance_margin` DECIMAL(20,8) NOT NULL COMMENT '维持保证金',
    `borrowed_amount` DECIMAL(20,8) NOT NULL COMMENT '借入资金',
    `unrealized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '未实现盈亏',
    `realized_pnl` DECIMAL(20,8) DEFAULT NULL COMMENT '已实现盈亏',
    `liquidation_price` DECIMAL(20,8) DEFAULT NULL COMMENT '强平价格',
    `margin_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '保证金率',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、CLOSED（已平仓）、LIQUIDATED（已强平）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_status` (`status`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆仓位表';

-- 杠杆交易策略表
CREATE TABLE IF NOT EXISTS `leveraged_strategies` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '关联的杠杆账户ID',
    `strategy_name` VARCHAR(100) DEFAULT NULL COMMENT '策略名称',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: GRID（网格交易）、TREND_FOLLOWING（趋势跟踪）、REVERSE（反转策略）',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `strategy_params` TEXT DEFAULT NULL COMMENT '策略参数（JSON格式）',
    `leverage` INT(11) NOT NULL COMMENT '杠杆倍数',
    `total_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '总亏损',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、STOPPED（已停止）、PAUSED（已暂停）',
    `last_execution_time` DATETIME DEFAULT NULL COMMENT '上次执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_strategy_type` (`strategy_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆交易策略表';

-- 杠杆风险警报表
CREATE TABLE IF NOT EXISTS `leveraged_risk_alerts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) DEFAULT NULL COMMENT '关联的杠杆账户ID（如果为空，则适用于所有账户）',
    `position_id` BIGINT(20) DEFAULT NULL COMMENT '关联的仓位ID（如果为空，则适用于所有仓位）',
    `alert_type` VARCHAR(20) NOT NULL COMMENT '警报类型: MARGIN_LOW（保证金不足）、LIQUIDATION_RISK（强平风险）、LEVERAGE_HIGH（杠杆过高）',
    `threshold` DECIMAL(20,8) DEFAULT NULL COMMENT '阈值',
    `threshold_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '阈值百分比',
    `is_triggered` TINYINT(1) DEFAULT 0 COMMENT '是否已触发',
    `triggered_at` DATETIME DEFAULT NULL COMMENT '触发时间',
    `notification_method` VARCHAR(20) DEFAULT NULL COMMENT '通知方式: SMS（短信）、EMAIL（邮件）、IN_APP（站内信）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、TRIGGERED（已触发）、CANCELLED（已取消）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆风险警报表';

-- 杠杆清算记录表
CREATE TABLE IF NOT EXISTS `leveraged_liquidations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '关联的杠杆账户ID',
    `position_id` BIGINT(20) NOT NULL COMMENT '关联的仓位ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `liquidation_price` DECIMAL(20,8) NOT NULL COMMENT '强平价格',
    `liquidation_quantity` DECIMAL(20,8) NOT NULL COMMENT '强平数量',
    `liquidation_amount` DECIMAL(20,8) NOT NULL COMMENT '强平金额',
    `liquidation_fee` DECIMAL(20,8) DEFAULT NULL COMMENT '强平手续费',
    `margin_remaining` DECIMAL(20,8) DEFAULT NULL COMMENT '剩余保证金',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '强平原因',
    `liquidation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '强平时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_liquidation_time` (`liquidation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆清算记录表';

-- 杠杆利息记录表
CREATE TABLE IF NOT EXISTS `leveraged_interest_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '杠杆账户ID',
    `position_id` BIGINT(20) DEFAULT NULL COMMENT '仓位ID',
    `borrowed_amount` DECIMAL(20,8) NOT NULL COMMENT '借入金额',
    `interest_rate` DECIMAL(10,8) NOT NULL COMMENT '利率',
    `interest_amount` DECIMAL(20,8) NOT NULL COMMENT '利息金额',
    `settlement_time` DATETIME NOT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_settlement_time` (`settlement_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆利息记录表';

-- 杠杆资金转账记录表
CREATE TABLE IF NOT EXISTS `leveraged_fund_transfer_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `account_id` BIGINT(20) NOT NULL COMMENT '杠杆账户ID',
    `transfer_type` VARCHAR(20) NOT NULL COMMENT '转账类型: DEPOSIT（转入）, WITHDRAW（转出）',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '转账金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '币种',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, SUCCESS, FAILED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杠杆资金转账记录表';

-- =====================================================
-- 20. 交割合约模块 (Delivery Contract Module)
-- =====================================================

-- 交割合约表
CREATE TABLE IF NOT EXISTS `delivery_contracts` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `contract_symbol` VARCHAR(50) NOT NULL COMMENT '合约交易对，如 BTC_USDT',
    `contract_type` VARCHAR(50) NOT NULL COMMENT '合约类型: USDT_MARGINED（USDT本位）、COIN_MARGINED（币本位）',
    `underlying_asset` VARCHAR(20) NOT NULL COMMENT '标的资产，如 BTC, ETH',
    `quote_currency` VARCHAR(20) NOT NULL COMMENT '计价货币，如 USDT',
    `contract_unit` DECIMAL(20,8) NOT NULL COMMENT '合约单位',
    `delivery_cycle` VARCHAR(20) NOT NULL COMMENT '交割周期: HOURLY（每小时）、DAILY（每天）、WEEKLY（每周）、MONTHLY（每月）',
    `max_leverage` DECIMAL(10,2) NOT NULL COMMENT '最大杠杆倍数',
    `maker_fee_rate` DECIMAL(10,6) NOT NULL COMMENT 'Maker手续费率',
    `taker_fee_rate` DECIMAL(10,6) NOT NULL COMMENT 'Taker手续费率',
    `initial_margin_rate` DECIMAL(10,4) NOT NULL COMMENT '初始保证金率',
    `maintenance_margin_rate` DECIMAL(10,4) NOT NULL COMMENT '维持保证金率',
    `delivery_time` DATETIME DEFAULT NULL COMMENT '交割时间',
    `settlement_price` DECIMAL(20,8) DEFAULT NULL COMMENT '结算价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）、SETTLED（已结算）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contract_symbol` (`contract_symbol`),
    KEY `idx_contract_type` (`contract_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约表';

-- 交割合约订单表
CREATE TABLE IF NOT EXISTS `delivery_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: OPEN（开仓）、CLOSE（平仓）、STOP_LOSS（止损）、TAKE_PROFIT（止盈）',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入/做多）、SELL（卖出/做空）',
    `price_type` VARCHAR(20) NOT NULL COMMENT '价格类型: LIMIT（限价）、MARKET（市价）',
    `price` DECIMAL(20,8) DEFAULT NULL COMMENT '限价价格',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '数量',
    `filled_quantity` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '已成交数量',
    `leverage` DECIMAL(10,2) NOT NULL COMMENT '杠杆倍数',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `margin` DECIMAL(20,8) NOT NULL COMMENT '保证金',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待成交）、PARTIAL（部分成交）、FILLED（已完成）、CANCELLED（已取消）、REJECTED（已拒绝）',
    `filled_at` DATETIME DEFAULT NULL COMMENT '成交时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约订单表';

-- 交割合约持仓表
CREATE TABLE IF NOT EXISTS `delivery_positions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: LONG（多头）、SHORT（空头）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '持仓数量',
    `avg_open_price` DECIMAL(20,8) NOT NULL COMMENT '平均开仓价格',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `leverage` DECIMAL(10,2) NOT NULL COMMENT '杠杆倍数',
    `margin` DECIMAL(20,8) NOT NULL COMMENT '保证金',
    `maintenance_margin` DECIMAL(20,8) NOT NULL COMMENT '维持保证金',
    `unrealized_pnl` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '未实现盈亏',
    `realized_pnl` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '已实现盈亏',
    `liquidation_price` DECIMAL(20,8) DEFAULT NULL COMMENT '强平价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、CLOSED（已平仓）、LIQUIDATED（已强平）',
    `opened_at` DATETIME NOT NULL COMMENT '开仓时间',
    `closed_at` DATETIME DEFAULT NULL COMMENT '平仓时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约持仓表';

-- 交割合约成交记录表
CREATE TABLE IF NOT EXISTS `delivery_trades` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `trade_no` VARCHAR(50) NOT NULL COMMENT '成交单号',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `buy_order_id` BIGINT(20) NOT NULL COMMENT '买方订单ID',
    `sell_order_id` BIGINT(20) NOT NULL COMMENT '卖方订单ID',
    `buyer_id` BIGINT(20) NOT NULL COMMENT '买方用户ID',
    `seller_id` BIGINT(20) NOT NULL COMMENT '卖方用户ID',
    `price` DECIMAL(20,8) NOT NULL COMMENT '成交价格',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '成交数量',
    `fee` DECIMAL(20,8) NOT NULL COMMENT '手续费',
    `fee_currency` VARCHAR(20) NOT NULL COMMENT '手续费币种',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`trade_no`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约成交记录表';

-- 交割合约结算记录表
CREATE TABLE IF NOT EXISTS `delivery_settlements` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `settlement_no` VARCHAR(50) NOT NULL COMMENT '结算单号',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `position_id` BIGINT(20) NOT NULL COMMENT '持仓ID',
    `settlement_price` DECIMAL(20,8) NOT NULL COMMENT '结算价格',
    `settlement_pnl` DECIMAL(20,8) NOT NULL COMMENT '结算盈亏',
    `settlement_type` VARCHAR(20) NOT NULL COMMENT '结算类型: DELIVERY（交割）、LIQUIDATION（强平）、MANUAL（手动）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待结算）、SETTLED（已结算）、FAILED（结算失败）',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_settlement_no` (`settlement_no`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约结算记录表';

-- 交割合约行情数据表
CREATE TABLE IF NOT EXISTS `delivery_market_data` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `contract_id` BIGINT(20) NOT NULL COMMENT '合约ID',
    `price` DECIMAL(20,8) NOT NULL COMMENT '价格',
    `volume_24h` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '24小时成交量',
    `high_24h` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '24小时最高价',
    `low_24h` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '24小时最低价',
    `change_24h` DECIMAL(10,4) NOT NULL DEFAULT 0 COMMENT '24小时涨跌幅',
    `open_interest` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '持仓量',
    `funding_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '资金费率',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约行情数据表';

-- 交割合约审计日志表
CREATE TABLE IF NOT EXISTS `delivery_audit_logs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `contract_id` BIGINT(20) DEFAULT NULL COMMENT '合约ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
    `position_id` BIGINT(20) DEFAULT NULL COMMENT '持仓ID',
    `action_type` VARCHAR(50) NOT NULL COMMENT '操作类型: ORDER_CREATE（订单创建）、ORDER_FILLED（订单成交）、POSITION_OPEN（开仓）、POSITION_CLOSE（平仓）、LIQUIDATION（强平）',
    `action_detail` TEXT COMMENT '操作详情（JSON格式）',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_action_type` (`action_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交割合约审计日志表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 期权合约表测试数据
INSERT INTO `option_contracts` (`id`, `pair_name`, `base_currency`, `quote_currency`, `option_type`, `exercise_type`, `strike_price`, `expiry_date`, `current_price`, `underlying_price`, `theoretical_price`, `implied_volatility`, `volume_24h`, `open_interest`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC/USDT', 'BTC', 'USDT', 'CALL', 'EUROPEAN', 50000.00000000, '2024-02-01 23:59:59', 500.00000000, 50000.00000000, 480.00000000, 0.65000000, 100.00000000, 50.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'BTC/USDT', 'BTC', 'USDT', 'PUT', 'EUROPEAN', 49000.00000000, '2024-02-01 23:59:59', 300.00000000, 50000.00000000, 320.00000000, 0.60000000, 80.00000000, 40.00000000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 期权订单表测试数据
INSERT INTO `option_orders` (`id`, `user_id`, `contract_id`, `order_type`, `side`, `option_type`, `quantity`, `price`, `total_amount`, `fee`, `status`, `filled_quantity`, `filled_amount`, `filled_at`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'OPEN', 'BUY', 'CALL', 1.00000000, 500.00000000, 500.00000000, 2.50000000, 'FILLED', 1.00000000, 500.00000000, '2024-01-10 10:00:00', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 2, 'OPEN', 'BUY', 'PUT', 1.00000000, 300.00000000, 300.00000000, 1.50000000, 'FILLED', 1.00000000, 300.00000000, '2024-01-11 10:00:00', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 期权持仓表测试数据
INSERT INTO `option_positions` (`id`, `user_id`, `contract_id`, `option_type`, `side`, `quantity`, `average_price`, `current_price`, `underlying_price`, `strike_price`, `unrealized_pnl`, `realized_pnl`, `expiry_date`, `status`, `margin`, `is_in_the_money`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'CALL', 'LONG', 1.00000000, 500.00000000, 550.00000000, 51000.00000000, 50000.00000000, 50.00000000, 0.00000000, '2024-02-01 23:59:59', 'ACTIVE', 500.00000000, 1, '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'PUT', 'LONG', 1.00000000, 300.00000000, 250.00000000, 50000.00000000, 49000.00000000, -50.00000000, 0.00000000, '2024-02-01 23:59:59', 'ACTIVE', 300.00000000, 0, '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 期权行使记录表测试数据
INSERT INTO `option_exercises` (`id`, `user_id`, `position_id`, `contract_id`, `option_type`, `exercise_type`, `quantity`, `strike_price`, `underlying_price`, `exercise_price`, `profit_loss`, `exercise_fee`, `status`, `executed_at`, `created_at`) VALUES
(1, 1, 1, 1, 'CALL', 'MANUAL', 1.00000000, 50000.00000000, 51000.00000000, 50000.00000000, 1000.00000000, 10.00000000, 'EXECUTED', '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 2, 2, 2, 'PUT', 'AUTO', 1.00000000, 49000.00000000, 48000.00000000, 49000.00000000, 1000.00000000, 10.00000000, 'EXECUTED', '2024-01-21 10:00:00', '2024-01-21 10:00:00');

-- 期权策略表测试数据
INSERT INTO `option_strategies` (`id`, `user_id`, `strategy_name`, `strategy_type`, `pair_name`, `strategy_params`, `total_cost`, `current_value`, `unrealized_pnl`, `realized_pnl`, `status`, `expiry_date`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC跨式策略', 'STRADDLE', 'BTC/USDT', '{"callStrike": 50000, "putStrike": 50000, "expiry": "2024-02-01"}', 800.00000000, 900.00000000, 100.00000000, 0.00000000, 'ACTIVE', '2024-02-01 23:59:59', '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'BTC蝶式策略', 'BUTTERFLY', 'BTC/USDT', '{"lowerStrike": 48000, "middleStrike": 50000, "upperStrike": 52000, "expiry": "2024-02-01"}', 500.00000000, 550.00000000, 50.00000000, 0.00000000, 'ACTIVE', '2024-02-01 23:59:59', '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 期权风险警报表测试数据
INSERT INTO `option_risk_alerts` (`id`, `user_id`, `position_id`, `alert_type`, `threshold`, `threshold_percentage`, `is_triggered`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'PROFIT', 1000.00000000, NULL, 0, 'ACTIVE', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 2, 'LOSS', -200.00000000, NULL, 0, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 杠杆账户表测试数据
INSERT INTO `leveraged_accounts` (`id`, `user_id`, `pair_name`, `leverage`, `max_leverage`, `available_balance`, `borrowed_amount`, `margin`, `initial_margin`, `maintenance_margin`, `margin_ratio`, `interest_rate`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT', 10, 20, 5000.00000000, 50000.00000000, 5000.00000000, 5000.00000000, 2500.00000000, 0.1000, 0.00010000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 2, 'ETH/USDT', 5, 10, 2000.00000000, 10000.00000000, 2000.00000000, 2000.00000000, 1000.00000000, 0.2000, 0.00008000, 'ACTIVE', '2024-01-02 10:00:00', '2024-01-02 10:00:00');

-- 杠杆订单表测试数据
INSERT INTO `leveraged_orders` (`id`, `user_id`, `account_id`, `pair_name`, `order_type`, `side`, `action`, `quantity`, `price`, `leverage`, `filled_quantity`, `filled_amount`, `fee`, `status`, `filled_at`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'BTC/USDT', 'LIMIT', 'BUY', 'OPEN', 0.10000000, 49000.00000000, 10, 0.10000000, 4900.00000000, 2.45000000, 'FILLED', '2024-01-10 10:00:00', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 2, 'ETH/USDT', 'MARKET', 'SELL', 'OPEN', 1.00000000, NULL, 5, 1.00000000, 3000.00000000, 1.50000000, 'FILLED', '2024-01-11 10:00:00', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 杠杆仓位表测试数据
INSERT INTO `leveraged_positions` (`id`, `user_id`, `account_id`, `pair_name`, `side`, `quantity`, `entry_price`, `current_price`, `leverage`, `margin`, `initial_margin`, `maintenance_margin`, `borrowed_amount`, `unrealized_pnl`, `realized_pnl`, `liquidation_price`, `margin_ratio`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'BTC/USDT', 'LONG', 0.10000000, 49000.00000000, 50000.00000000, 10, 490.00000000, 490.00000000, 245.00000000, 4410.00000000, 100.00000000, 0.00000000, 44100.00000000, 0.0980, 'ACTIVE', '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'ETH/USDT', 'SHORT', 1.00000000, 3000.00000000, 2950.00000000, 5, 600.00000000, 600.00000000, 300.00000000, 2400.00000000, 50.00000000, 0.00000000, 3120.00000000, 0.2034, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 杠杆交易策略表测试数据
INSERT INTO `leveraged_strategies` (`id`, `user_id`, `account_id`, `strategy_name`, `strategy_type`, `pair_name`, `strategy_params`, `leverage`, `total_profit`, `total_loss`, `status`, `last_execution_time`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'BTC网格策略', 'GRID', 'BTC/USDT', '{"gridCount": 10, "upperPrice": 51000, "lowerPrice": 49000}', 10, 200.00000000, 50.00000000, 'ACTIVE', '2024-01-15 10:00:00', '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'ETH趋势跟随策略', 'TREND_FOLLOWING', 'ETH/USDT', '{"shortMaPeriod": 5, "longMaPeriod": 20}', 5, 100.00000000, 30.00000000, 'ACTIVE', '2024-01-15 11:00:00', '2024-01-11 10:00:00', '2024-01-15 11:00:00');

-- 杠杆风险警报表测试数据
INSERT INTO `leveraged_risk_alerts` (`id`, `user_id`, `account_id`, `position_id`, `alert_type`, `threshold`, `threshold_percentage`, `is_triggered`, `notification_method`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 'MARGIN_LOW', NULL, 0.1500, 0, 'EMAIL', 'ACTIVE', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 2, 2, 2, 'LIQUIDATION_RISK', NULL, 0.2000, 0, 'SMS', 'ACTIVE', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 杠杆清算记录表测试数据
INSERT INTO `leveraged_liquidations` (`id`, `user_id`, `account_id`, `position_id`, `pair_name`, `liquidation_price`, `liquidation_quantity`, `liquidation_amount`, `liquidation_fee`, `margin_remaining`, `reason`, `liquidation_time`, `created_at`) VALUES
(1, 1, 1, 1, 'BTC/USDT', 44100.00000000, 0.10000000, 4410.00000000, 22.05000000, 0.00000000, '保证金率低于维持保证金率', '2024-01-12 10:00:00', '2024-01-12 10:00:00'),
(2, 2, 2, 2, 'ETH/USDT', 3120.00000000, 1.00000000, 3120.00000000, 15.60000000, 0.00000000, '保证金率低于维持保证金率', '2024-01-13 10:00:00', '2024-01-13 10:00:00');

-- 杠杆利息记录表测试数据
INSERT INTO `leveraged_interest_records` (`id`, `user_id`, `account_id`, `position_id`, `borrowed_amount`, `interest_rate`, `interest_amount`, `settlement_time`, `created_at`) VALUES
(1, 1, 1, 1, 4410.00000000, 0.00010000, 0.44100000, '2024-01-16 00:00:00', '2024-01-16 00:00:00'),
(2, 2, 2, 2, 2400.00000000, 0.00008000, 0.19200000, '2024-01-16 00:00:00', '2024-01-16 00:00:00');

-- 杠杆资金转账记录表测试数据
INSERT INTO `leveraged_fund_transfer_records` (`id`, `user_id`, `account_id`, `transfer_type`, `amount`, `currency`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'DEPOSIT', 5000.00000000, 'USDT', 'SUCCESS', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 2, 2, 'WITHDRAW', 1000.00000000, 'USDT', 'SUCCESS', '2024-01-02 10:00:00', '2024-01-02 10:00:00');

-- 交割合约表测试数据
INSERT INTO `delivery_contracts` (`id`, `contract_symbol`, `contract_type`, `underlying_asset`, `quote_currency`, `contract_unit`, `delivery_cycle`, `max_leverage`, `maker_fee_rate`, `taker_fee_rate`, `initial_margin_rate`, `maintenance_margin_rate`, `delivery_time`, `settlement_price`, `status`, `created_at`, `updated_at`) VALUES
(1, 'BTC_USDT_WEEKLY', 'USDT_MARGINED', 'BTC', 'USDT', 1.00000000, 'WEEKLY', 125.00, 0.000200, 0.000500, 0.0100, 0.0050, '2024-01-28 16:00:00', NULL, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH_USDT_MONTHLY', 'USDT_MARGINED', 'ETH', 'USDT', 1.00000000, 'MONTHLY', 125.00, 0.000200, 0.000500, 0.0100, 0.0050, '2024-01-31 16:00:00', NULL, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 交割合约订单表测试数据
INSERT INTO `delivery_orders` (`id`, `order_no`, `user_id`, `contract_id`, `order_type`, `side`, `price_type`, `price`, `quantity`, `filled_quantity`, `leverage`, `margin`, `status`, `filled_at`, `created_at`, `updated_at`) VALUES
(1, 'DC20240110001', 1, 1, 'OPEN', 'BUY', 'LIMIT', 49000.00000000, 0.10000000, 0.10000000, 10.00, 490.00000000, 'FILLED', '2024-01-10 10:00:00', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 'DC20240111001', 2, 2, 'OPEN', 'SELL', 'MARKET', NULL, 1.00000000, 1.00000000, 20.00, 150.00000000, 'FILLED', '2024-01-11 10:00:00', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 交割合约持仓表测试数据
INSERT INTO `delivery_positions` (`id`, `user_id`, `contract_id`, `side`, `quantity`, `avg_open_price`, `current_price`, `leverage`, `margin`, `maintenance_margin`, `unrealized_pnl`, `realized_pnl`, `liquidation_price`, `status`, `opened_at`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'LONG', 0.10000000, 49000.00000000, 50000.00000000, 10.00, 490.00000000, 245.00000000, 100.00000000, 0.00000000, 44100.00000000, 'ACTIVE', '2024-01-10 10:00:00', '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'SHORT', 1.00000000, 3000.00000000, 2950.00000000, 20.00, 150.00000000, 75.00000000, 50.00000000, 0.00000000, 3150.00000000, 'ACTIVE', '2024-01-11 10:00:00', '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 交割合约成交记录表测试数据
INSERT INTO `delivery_trades` (`id`, `trade_no`, `contract_id`, `buy_order_id`, `sell_order_id`, `buyer_id`, `seller_id`, `price`, `quantity`, `fee`, `fee_currency`, `created_at`) VALUES
(1, 'DT20240110001', 1, 1, 2, 1, 2, 49000.00000000, 0.10000000, 4.90000000, 'USDT', '2024-01-10 10:00:00'),
(2, 'DT20240111001', 2, 3, 4, 1, 2, 3000.00000000, 1.00000000, 3.00000000, 'USDT', '2024-01-11 10:00:00');

-- 交割合约结算记录表测试数据
INSERT INTO `delivery_settlements` (`id`, `settlement_no`, `contract_id`, `user_id`, `position_id`, `settlement_price`, `settlement_pnl`, `settlement_type`, `status`, `settled_at`, `created_at`, `updated_at`) VALUES
(1, 'DS20240128001', 1, 1, 1, 50000.00000000, 100.00000000, 'DELIVERY', 'SETTLED', '2024-01-28 16:00:00', '2024-01-28 16:00:00', '2024-01-28 16:00:00'),
(2, 'DS20240131001', 2, 2, 2, 2950.00000000, 50.00000000, 'DELIVERY', 'SETTLED', '2024-01-31 16:00:00', '2024-01-31 16:00:00', '2024-01-31 16:00:00');

-- 交割合约行情数据表测试数据
INSERT INTO `delivery_market_data` (`id`, `contract_id`, `price`, `volume_24h`, `high_24h`, `low_24h`, `change_24h`, `open_interest`, `funding_rate`, `created_at`) VALUES
(1, 1, 50000.00000000, 1000.00000000, 51000.00000000, 49000.00000000, 2.5000, 500.00000000, 0.000100, '2024-01-15 10:00:00'),
(2, 2, 3000.00000000, 5000.00000000, 3100.00000000, 2950.00000000, -1.2000, 2000.00000000, 0.000080, '2024-01-15 10:00:00');

-- 交割合约审计日志表测试数据
INSERT INTO `delivery_audit_logs` (`id`, `user_id`, `contract_id`, `order_id`, `position_id`, `action_type`, `action_detail`, `ip_address`, `created_at`) VALUES
(1, 1, 1, 1, 1, 'ORDER_CREATE', '{"orderNo": "DC20240110001", "side": "BUY", "quantity": 0.1}', '192.168.1.100', '2024-01-10 10:00:00'),
(2, 2, 2, 2, 2, 'POSITION_OPEN', '{"positionId": 2, "side": "SHORT", "quantity": 1.0}', '192.168.1.101', '2024-01-11 10:00:00');












