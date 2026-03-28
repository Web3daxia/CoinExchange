-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第3部分
-- 包含：机器人模块、策略交易模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 8. 交易机器人模块 (Robot Module)
-- =====================================================

-- 交易机器人表
CREATE TABLE IF NOT EXISTS `trading_robots` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `robot_name` VARCHAR(100) DEFAULT NULL COMMENT '机器人名称',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `market_type` VARCHAR(50) NOT NULL DEFAULT 'SPOT' COMMENT '市场类型: SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）、LEVERAGED（杠杆）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: GRID, TREND_FOLLOWING, REVERSE',
    `strategy_params` TEXT DEFAULT NULL COMMENT '策略参数（JSON格式）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'STOPPED' COMMENT '状态: STOPPED（已停止）、RUNNING（运行中）、PAUSED（已暂停）',
    `max_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '最大亏损',
    `max_position` DECIMAL(20,8) DEFAULT NULL COMMENT '最大持仓',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `order_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '订单金额',
    `order_quantity` DECIMAL(20,8) DEFAULT NULL COMMENT '订单数量',
    `total_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总亏损',
    `last_execution_time` DATETIME DEFAULT NULL COMMENT '上次执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_market_type` (`market_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易机器人表';

-- 交易机器人交易记录表
CREATE TABLE IF NOT EXISTS `robot_trade_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `robot_id` BIGINT(20) NOT NULL COMMENT '关联的交易机器人ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）、LEVERAGED（杠杆）',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '关联的订单ID（可能为空，如果是不同市场的订单）',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `action` VARCHAR(20) NOT NULL COMMENT '操作: OPEN（开仓）、CLOSE（平仓）',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入）、SELL（卖出）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '交易数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '交易价格',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `profit_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '盈亏（平仓时计算）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型',
    `grid_level` INT(11) DEFAULT NULL COMMENT '网格级别',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_robot_id` (`robot_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易机器人交易记录表';

-- 网格机器人回测表
CREATE TABLE IF NOT EXISTS `grid_robot_backtests` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `backtest_id` VARCHAR(100) NOT NULL COMMENT '回测任务ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `robot_id` BIGINT(20) DEFAULT NULL COMMENT '机器人ID（可选）',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: GRID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待执行）, RUNNING（执行中）, COMPLETED（已完成）, FAILED（失败）',
    `progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
    `start_time` DATETIME NOT NULL COMMENT '回测开始时间',
    `end_time` DATETIME NOT NULL COMMENT '回测结束时间',
    `initial_capital` DECIMAL(20,8) NOT NULL COMMENT '初始资金',
    `final_capital` DECIMAL(20,8) DEFAULT NULL COMMENT '最终资金',
    `total_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '总亏损',
    `net_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '净利润',
    `profit_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '收益率',
    `total_trades` INT(11) DEFAULT NULL COMMENT '总交易次数',
    `winning_trades` INT(11) DEFAULT NULL COMMENT '盈利交易次数',
    `losing_trades` INT(11) DEFAULT NULL COMMENT '亏损交易次数',
    `win_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '胜率',
    `max_drawdown` DECIMAL(20,8) DEFAULT NULL COMMENT '最大回撤',
    `sharpe_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '夏普比率',
    `grid_count` INT(11) DEFAULT NULL COMMENT '网格数量',
    `backtest_params` TEXT COMMENT '回测参数（JSON格式）',
    `equity_curve_data` TEXT COMMENT '权益曲线数据（JSON格式）',
    `estimated_duration` INT(11) DEFAULT NULL COMMENT '预估时长（秒）',
    `started_at` DATETIME DEFAULT NULL COMMENT '实际开始时间',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `error_message` TEXT COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_backtest_id` (`backtest_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_robot_id` (`robot_id`),
    KEY `idx_status` (`status`),
    KEY `idx_market_type` (`market_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网格机器人回测表';

-- 网格机器人结算表
CREATE TABLE IF NOT EXISTS `grid_robot_settlements` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `settlement_id` VARCHAR(100) NOT NULL COMMENT '结算ID',
    `robot_id` BIGINT(20) NOT NULL COMMENT '机器人ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `initial_capital` DECIMAL(20,8) NOT NULL COMMENT '初始资金',
    `final_capital` DECIMAL(20,8) NOT NULL COMMENT '最终资金',
    `total_profit` DECIMAL(20,8) NOT NULL COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) NOT NULL COMMENT '总亏损',
    `net_profit` DECIMAL(20,8) NOT NULL COMMENT '净利润',
    `total_fees` DECIMAL(20,8) NOT NULL COMMENT '总手续费',
    `settlement_amount` DECIMAL(20,8) NOT NULL COMMENT '结算金额',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待处理）, COMPLETED（已完成）, FAILED（失败）',
    `settlement_time` DATETIME DEFAULT NULL COMMENT '结算时间',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_settlement_id` (`settlement_id`),
    KEY `idx_robot_id` (`robot_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网格机器人结算表';

-- 网格机器人默认策略表
CREATE TABLE IF NOT EXISTS `grid_robot_default_strategies` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `strategy_name` VARCHAR(100) NOT NULL COMMENT '策略名称',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `grid_count` INT(11) DEFAULT NULL COMMENT '默认网格数量',
    `default_upper_price` DECIMAL(20,8) DEFAULT NULL COMMENT '默认上限价格',
    `default_lower_price` DECIMAL(20,8) DEFAULT NULL COMMENT '默认下限价格',
    `default_stop_loss_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '默认止损百分比',
    `default_take_profit_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '默认止盈百分比',
    `description` TEXT COMMENT '描述',
    `expected_return_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '预期收益率',
    `risk_level` VARCHAR(20) NOT NULL COMMENT '风险等级: LOW（低）, MEDIUM（中）, HIGH（高）',
    `recommended_pairs` TEXT COMMENT '推荐交易对（JSON格式）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）, INACTIVE（未激活）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_status` (`status`),
    KEY `idx_risk_level` (`risk_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网格机器人默认策略表';

-- 网格机器人参数调整历史表
CREATE TABLE IF NOT EXISTS `grid_robot_adjustment_histories` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `robot_id` BIGINT(20) NOT NULL COMMENT '机器人ID',
    `adjust_type` VARCHAR(50) NOT NULL COMMENT '调整类型: GRID_COUNT（网格数量）, PRICE_RANGE（价格区间）, STOP_LOSS（止损）, TAKE_PROFIT（止盈）, LEVERAGE（杠杆）, MARGIN_MODE（保证金模式）',
    `old_grid_count` INT(11) DEFAULT NULL COMMENT '旧网格数量',
    `new_grid_count` INT(11) DEFAULT NULL COMMENT '新网格数量',
    `old_upper_price` DECIMAL(20,8) DEFAULT NULL COMMENT '旧上限价格',
    `new_upper_price` DECIMAL(20,8) DEFAULT NULL COMMENT '新上限价格',
    `old_lower_price` DECIMAL(20,8) DEFAULT NULL COMMENT '旧下限价格',
    `new_lower_price` DECIMAL(20,8) DEFAULT NULL COMMENT '新下限价格',
    `old_start_price` DECIMAL(20,8) DEFAULT NULL COMMENT '旧起始价格',
    `new_start_price` DECIMAL(20,8) DEFAULT NULL COMMENT '新起始价格',
    `adjustment_reason` TEXT COMMENT '调整原因',
    `adjustment_params` TEXT COMMENT '调整参数（JSON格式）',
    `adjusted_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '调整时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_robot_id` (`robot_id`),
    KEY `idx_adjust_type` (`adjust_type`),
    KEY `idx_adjusted_at` (`adjusted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网格机器人参数调整历史表';

-- =====================================================
-- 9. 策略交易模块 (Strategy Trading Module)
-- =====================================================

-- 策略模板表
CREATE TABLE IF NOT EXISTS `strategy_templates` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `strategy_category` VARCHAR(50) NOT NULL COMMENT '策略分类: TREND（趋势）, REVERSAL（反转）, BREAKOUT（突破）, OSCILLATION（震荡）, GRID（网格）, RISK（风险）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: MA_CROSS（均线交叉）, RSI_OVERBOUGHT_OVERSOLD（RSI超买超卖）, BOLLINGER_BANDS_BREAKOUT（布林带突破）, MACD_DIVERGENCE（MACD背离）, OSCILLATION_RANGE_BREAKOUT（震荡区间突破）, MARKET_SENTIMENT（市场情绪）, TREND_FOLLOWING（趋势跟随）, CONTRARIAN（反向交易）, GRID（网格）, BOLLINGER_BANDS_BREAKOUT_FUTURES（布林带突破-合约）, FUND_MANAGEMENT（资金管理）, OSCILLATION_RANGE（震荡区间）, FIXED_POSITION（固定仓位）, DYNAMIC_POSITION（动态仓位）, VOLATILITY（波动率）, PRICE_RANGE（价格区间）, PERIODIC_TAKE_PROFIT_STOP_LOSS（定期止盈止损）, FUND_MANAGEMENT_COIN（资金管理-币本位）',
    `description` TEXT COMMENT '描述',
    `detailed_description` TEXT COMMENT '详细描述',
    `default_params` TEXT COMMENT '默认参数（JSON格式）',
    `param_description` TEXT COMMENT '参数描述（JSON格式）',
    `risk_level` VARCHAR(20) NOT NULL COMMENT '风险等级: LOW（低）, MEDIUM（中）, HIGH（高）',
    `expected_return_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '预期收益率',
    `max_drawdown` DECIMAL(10,4) DEFAULT NULL COMMENT '最大回撤',
    `backtest_performance` TEXT COMMENT '回测表现（JSON格式）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）, INACTIVE（未激活）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_strategy_category` (`strategy_category`),
    KEY `idx_strategy_type` (`strategy_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略模板表';

-- 交易策略表
CREATE TABLE IF NOT EXISTS `trading_strategies` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `strategy_name` VARCHAR(100) NOT NULL COMMENT '策略名称',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型',
    `status` VARCHAR(20) NOT NULL DEFAULT 'STOPPED' COMMENT '状态: STOPPED（已停止）, RUNNING（运行中）, PAUSED（已暂停）',
    `initial_capital` DECIMAL(20,8) NOT NULL COMMENT '初始资金',
    `current_capital` DECIMAL(20,8) DEFAULT NULL COMMENT '当前资金',
    `total_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总亏损',
    `total_trades` INT(11) DEFAULT 0 COMMENT '总交易次数',
    `winning_trades` INT(11) DEFAULT 0 COMMENT '盈利交易次数',
    `losing_trades` INT(11) DEFAULT 0 COMMENT '亏损交易次数',
    `strategy_params` TEXT COMMENT '策略参数（JSON格式）',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `last_execution_time` DATETIME DEFAULT NULL COMMENT '最后执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_strategy_type` (`strategy_type`),
    KEY `idx_status` (`status`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易策略表';

-- 策略回测表
CREATE TABLE IF NOT EXISTS `strategy_backtests` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `backtest_id` VARCHAR(100) NOT NULL COMMENT '回测任务ID',
    `strategy_id` BIGINT(20) NOT NULL COMMENT '策略ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待执行）, RUNNING（执行中）, COMPLETED（已完成）, FAILED（失败）',
    `progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
    `start_time` DATETIME NOT NULL COMMENT '回测开始时间',
    `end_time` DATETIME NOT NULL COMMENT '回测结束时间',
    `initial_capital` DECIMAL(20,8) NOT NULL COMMENT '初始资金',
    `final_capital` DECIMAL(20,8) DEFAULT NULL COMMENT '最终资金',
    `total_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '总亏损',
    `net_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '净利润',
    `profit_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '收益率',
    `annualized_return_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '年化收益率',
    `total_trades` INT(11) DEFAULT NULL COMMENT '总交易次数',
    `winning_trades` INT(11) DEFAULT NULL COMMENT '盈利交易次数',
    `losing_trades` INT(11) DEFAULT NULL COMMENT '亏损交易次数',
    `win_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '胜率',
    `average_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '平均盈利',
    `average_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '平均亏损',
    `profit_loss_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '盈亏比',
    `max_drawdown` DECIMAL(20,8) DEFAULT NULL COMMENT '最大回撤',
    `max_drawdown_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '最大回撤率',
    `sharpe_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '夏普比率',
    `total_fees` DECIMAL(20,8) DEFAULT NULL COMMENT '总手续费',
    `estimated_duration` INT(11) DEFAULT NULL COMMENT '预估时长（秒）',
    `started_at` DATETIME DEFAULT NULL COMMENT '实际开始时间',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_backtest_id` (`backtest_id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_market_type` (`market_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略回测表';

-- 策略交易记录表
CREATE TABLE IF NOT EXISTS `strategy_trade_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `strategy_id` BIGINT(20) NOT NULL COMMENT '策略ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `market_type` VARCHAR(50) NOT NULL COMMENT '市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约）',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '关联的订单ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `action` VARCHAR(20) NOT NULL COMMENT '操作: OPEN（开仓）, CLOSE（平仓）',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入）, SELL（卖出）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '交易数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '交易价格',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `profit_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '盈亏（平仓时计算）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型',
    `signal` VARCHAR(100) DEFAULT NULL COMMENT '交易信号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_created_at` (`created_at`),
    KEY `idx_pair_name` (`pair_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略交易记录表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 交易机器人表测试数据
INSERT INTO `trading_robots` (`id`, `user_id`, `robot_name`, `pair_name`, `market_type`, `strategy_type`, `strategy_params`, `status`, `total_profit`, `total_loss`, `last_execution_time`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT网格机器人', 'BTC/USDT', 'SPOT', 'GRID', '{"gridCount": 10, "upperPrice": 51000, "lowerPrice": 49000}', 'RUNNING', 500.00000000, 100.00000000, '2024-01-15 10:00:00', '2024-01-01 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'ETH/USDT趋势跟随机器人', 'ETH/USDT', 'FUTURES_USDT', 'TREND_FOLLOWING', '{"shortMaPeriod": 5, "longMaPeriod": 20, "leverage": 10}', 'RUNNING', 300.00000000, 50.00000000, '2024-01-15 11:00:00', '2024-01-02 10:00:00', '2024-01-15 11:00:00');

-- 交易机器人交易记录表测试数据
INSERT INTO `robot_trade_records` (`id`, `robot_id`, `user_id`, `market_type`, `order_id`, `pair_name`, `action`, `side`, `quantity`, `price`, `amount`, `fee`, `profit_loss`, `strategy_type`, `grid_level`, `created_at`) VALUES
(1, 1, 1, 'SPOT', 1, 'BTC/USDT', 'OPEN', 'BUY', 0.10000000, 49000.00000000, 4900.00000000, 4.90000000, NULL, 'GRID', 1, '2024-01-10 10:00:00'),
(2, 1, 1, 'SPOT', 2, 'BTC/USDT', 'CLOSE', 'SELL', 0.10000000, 50000.00000000, 5000.00000000, 5.00000000, 100.00000000, 'GRID', 1, '2024-01-11 10:00:00');

-- 网格机器人回测表测试数据
INSERT INTO `grid_robot_backtests` (`id`, `backtest_id`, `user_id`, `robot_id`, `pair_name`, `market_type`, `strategy_type`, `status`, `progress`, `start_time`, `end_time`, `initial_capital`, `final_capital`, `total_profit`, `total_loss`, `net_profit`, `profit_rate`, `total_trades`, `winning_trades`, `losing_trades`, `win_rate`, `max_drawdown`, `sharpe_ratio`, `grid_count`, `created_at`, `updated_at`) VALUES
(1, 'BACKTEST_001', 1, 1, 'BTC/USDT', 'SPOT', 'GRID', 'COMPLETED', 100.00, '2024-01-01 00:00:00', '2024-01-31 23:59:59', 10000.00000000, 10500.00000000, 600.00000000, 100.00000000, 500.00000000, 5.0000, 100, 60, 40, 60.0000, 200.00000000, 1.5000, 10, '2024-01-01 10:00:00', '2024-02-01 10:00:00'),
(2, 'BACKTEST_002', 2, NULL, 'ETH/USDT', 'FUTURES_USDT', 'GRID', 'COMPLETED', 100.00, '2024-01-01 00:00:00', '2024-01-31 23:59:59', 5000.00000000, 5200.00000000, 300.00000000, 100.00000000, 200.00000000, 4.0000, 80, 50, 30, 62.5000, 150.00000000, 1.2000, 10, '2024-01-02 10:00:00', '2024-02-01 10:00:00');

-- 网格机器人结算表测试数据
INSERT INTO `grid_robot_settlements` (`id`, `settlement_id`, `robot_id`, `user_id`, `initial_capital`, `final_capital`, `total_profit`, `total_loss`, `net_profit`, `total_fees`, `settlement_amount`, `status`, `settlement_time`, `completed_at`, `created_at`, `updated_at`) VALUES
(1, 'SETTLE_001', 1, 1, 10000.00000000, 10500.00000000, 600.00000000, 100.00000000, 500.00000000, 50.00000000, 10500.00000000, 'COMPLETED', '2024-01-31 23:59:59', '2024-02-01 00:00:00', '2024-01-31 23:00:00', '2024-02-01 00:00:00'),
(2, 'SETTLE_002', 2, 2, 5000.00000000, 5200.00000000, 300.00000000, 100.00000000, 200.00000000, 25.00000000, 5200.00000000, 'COMPLETED', '2024-01-31 23:59:59', '2024-02-01 00:00:00', '2024-01-31 23:00:00', '2024-02-01 00:00:00');

-- 网格机器人默认策略表测试数据
INSERT INTO `grid_robot_default_strategies` (`id`, `strategy_name`, `market_type`, `grid_count`, `default_upper_price`, `default_lower_price`, `default_stop_loss_percentage`, `default_take_profit_percentage`, `description`, `expected_return_rate`, `risk_level`, `recommended_pairs`, `status`, `created_at`, `updated_at`) VALUES
(1, '保守型网格策略', 'SPOT', 10, NULL, NULL, 0.0500, 0.1000, '适合波动较小的市场，风险较低', 0.0800, 'LOW', '["BTC/USDT", "ETH/USDT"]', 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, '激进型网格策略', 'FUTURES_USDT', 20, NULL, NULL, 0.1000, 0.2000, '适合波动较大的市场，风险较高', 0.1500, 'HIGH', '["BTC/USDT", "ETH/USDT"]', 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 网格机器人参数调整历史表测试数据
INSERT INTO `grid_robot_adjustment_histories` (`id`, `robot_id`, `adjust_type`, `old_grid_count`, `new_grid_count`, `old_upper_price`, `new_upper_price`, `old_lower_price`, `new_lower_price`, `adjustment_reason`, `adjusted_at`, `created_at`) VALUES
(1, 1, 'GRID_COUNT', 10, 15, NULL, NULL, NULL, NULL, '增加网格数量以提高收益', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 1, 'PRICE_RANGE', NULL, NULL, 51000.00000000, 52000.00000000, 49000.00000000, 48000.00000000, '扩大价格区间以适应市场波动', '2024-01-12 10:00:00', '2024-01-12 10:00:00');

-- 策略模板表测试数据
INSERT INTO `strategy_templates` (`id`, `template_name`, `market_type`, `strategy_category`, `strategy_type`, `description`, `default_params`, `risk_level`, `expected_return_rate`, `max_drawdown`, `status`, `created_at`, `updated_at`) VALUES
(1, '均线交叉策略', 'SPOT', 'TREND', 'MA_CROSS', '使用短期与长期均线交叉信号作为买卖信号', '{"shortPeriod": 5, "longPeriod": 50, "maType": "SMA"}', 'MEDIUM', 0.1200, 0.1500, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, '趋势跟随策略', 'FUTURES_USDT', 'TREND', 'TREND_FOLLOWING', '上升趋势时做多，下降趋势时做空', '{"shortMaPeriod": 5, "longMaPeriod": 20, "leverage": 10}', 'MEDIUM', 0.1500, 0.2000, 'ACTIVE', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 交易策略表测试数据
INSERT INTO `trading_strategies` (`id`, `user_id`, `strategy_name`, `pair_name`, `market_type`, `strategy_type`, `status`, `initial_capital`, `current_capital`, `total_profit`, `total_loss`, `total_trades`, `winning_trades`, `losing_trades`, `strategy_params`, `start_time`, `last_execution_time`, `created_at`, `updated_at`) VALUES
(1, 1, 'BTC/USDT均线交叉策略', 'BTC/USDT', 'SPOT', 'MA_CROSS', 'RUNNING', 10000.00000000, 10200.00000000, 300.00000000, 100.00000000, 20, 12, 8, '{"shortPeriod": 5, "longPeriod": 50, "maType": "SMA"}', '2024-01-10 10:00:00', '2024-01-15 10:00:00', '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'ETH/USDT趋势跟随策略', 'ETH/USDT', 'FUTURES_USDT', 'TREND_FOLLOWING', 'RUNNING', 5000.00000000, 5150.00000000, 200.00000000, 50.00000000, 15, 10, 5, '{"shortMaPeriod": 5, "longMaPeriod": 20, "leverage": 10}', '2024-01-11 10:00:00', '2024-01-15 11:00:00', '2024-01-11 10:00:00', '2024-01-15 11:00:00');

-- 策略回测表测试数据
INSERT INTO `strategy_backtests` (`id`, `backtest_id`, `strategy_id`, `user_id`, `pair_name`, `market_type`, `status`, `progress`, `start_time`, `end_time`, `initial_capital`, `final_capital`, `total_profit`, `total_loss`, `net_profit`, `profit_rate`, `annualized_return_rate`, `total_trades`, `winning_trades`, `losing_trades`, `win_rate`, `average_profit`, `average_loss`, `profit_loss_ratio`, `max_drawdown`, `max_drawdown_rate`, `sharpe_ratio`, `total_fees`, `started_at`, `completed_at`, `created_at`, `updated_at`) VALUES
(1, 'STRATEGY_BACKTEST_001', 1, 1, 'BTC/USDT', 'SPOT', 'COMPLETED', 100.00, '2024-01-01 00:00:00', '2024-01-31 23:59:59', 10000.00000000, 10200.00000000, 300.00000000, 100.00000000, 200.00000000, 2.0000, 24.0000, 20, 12, 8, 60.0000, 25.00000000, -12.50000000, 2.0000, 150.00000000, 1.5000, 1.2000, 50.00000000, '2024-01-01 00:00:00', '2024-02-01 00:00:00', '2024-01-01 10:00:00', '2024-02-01 00:00:00'),
(2, 'STRATEGY_BACKTEST_002', 2, 2, 'ETH/USDT', 'FUTURES_USDT', 'COMPLETED', 100.00, '2024-01-01 00:00:00', '2024-01-31 23:59:59', 5000.00000000, 5150.00000000, 200.00000000, 50.00000000, 150.00000000, 3.0000, 36.0000, 15, 10, 5, 66.6667, 20.00000000, -10.00000000, 2.0000, 100.00000000, 2.0000, 1.5000, 25.00000000, '2024-01-01 00:00:00', '2024-02-01 00:00:00', '2024-01-02 10:00:00', '2024-02-01 00:00:00');

-- 策略交易记录表测试数据
INSERT INTO `strategy_trade_records` (`id`, `strategy_id`, `user_id`, `market_type`, `order_id`, `pair_name`, `action`, `side`, `quantity`, `price`, `amount`, `fee`, `profit_loss`, `strategy_type`, `signal`, `created_at`) VALUES
(1, 1, 1, 'SPOT', 1, 'BTC/USDT', 'OPEN', 'BUY', 0.10000000, 49000.00000000, 4900.00000000, 4.90000000, NULL, 'MA_CROSS', 'MA_CROSS_UP', '2024-01-10 10:00:00'),
(2, 1, 1, 'SPOT', 2, 'BTC/USDT', 'CLOSE', 'SELL', 0.10000000, 50000.00000000, 5000.00000000, 5.00000000, 100.00000000, 'MA_CROSS', 'MA_CROSS_DOWN', '2024-01-11 10:00:00');












