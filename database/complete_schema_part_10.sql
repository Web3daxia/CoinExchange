-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第10部分（最后一部分）
-- 包含：Web3钱包模块、现货交易机器人模块、控盘机器人模块、其他补充表
-- =====================================================

USE `coin`;

-- =====================================================
-- 31. Web3钱包模块 (Web3 Wallet Module)
-- =====================================================

-- Web3钱包表
CREATE TABLE IF NOT EXISTS `web3_wallets` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `chain_type` VARCHAR(50) NOT NULL COMMENT '链类型: ETHEREUM, BSC, POLYGON, ARBITRUM, OPTIMISM',
    `wallet_address` VARCHAR(100) NOT NULL COMMENT '钱包地址',
    `private_key_encrypted` TEXT DEFAULT NULL COMMENT '加密的私钥',
    `mnemonic_encrypted` TEXT DEFAULT NULL COMMENT '加密的助记词',
    `wallet_name` VARCHAR(100) DEFAULT NULL COMMENT '钱包名称',
    `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认钱包',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、DISABLED（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_wallet_address` (`wallet_address`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_chain_type` (`chain_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Web3钱包表';

-- Web3代币余额表
CREATE TABLE IF NOT EXISTS `web3_token_balances` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `wallet_id` BIGINT(20) NOT NULL COMMENT '钱包ID',
    `token_contract_address` VARCHAR(100) DEFAULT NULL COMMENT '代币合约地址（NULL表示原生币）',
    `token_symbol` VARCHAR(50) NOT NULL COMMENT '代币符号',
    `token_name` VARCHAR(100) DEFAULT NULL COMMENT '代币名称',
    `token_decimals` INT(11) DEFAULT NULL COMMENT '代币精度',
    `balance` DECIMAL(36,18) NOT NULL DEFAULT 0 COMMENT '余额',
    `last_sync_time` DATETIME DEFAULT NULL COMMENT '最后同步时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_wallet_token` (`wallet_id`, `token_symbol`),
    KEY `idx_wallet_id` (`wallet_id`),
    KEY `idx_token_symbol` (`token_symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Web3代币余额表';

-- Web3交易记录表
CREATE TABLE IF NOT EXISTS `web3_transactions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `wallet_id` BIGINT(20) NOT NULL COMMENT '钱包ID',
    `tx_hash` VARCHAR(100) NOT NULL COMMENT '交易哈希',
    `from_address` VARCHAR(100) NOT NULL COMMENT '发送地址',
    `to_address` VARCHAR(100) NOT NULL COMMENT '接收地址',
    `token_contract_address` VARCHAR(100) DEFAULT NULL COMMENT '代币合约地址（NULL表示原生币）',
    `amount` DECIMAL(36,18) NOT NULL COMMENT '金额',
    `gas_price` DECIMAL(36,18) DEFAULT NULL COMMENT 'Gas价格',
    `gas_used` BIGINT(20) DEFAULT NULL COMMENT 'Gas使用量',
    `tx_fee` DECIMAL(36,18) DEFAULT NULL COMMENT '交易手续费',
    `block_number` BIGINT(20) DEFAULT NULL COMMENT '区块号',
    `block_hash` VARCHAR(100) DEFAULT NULL COMMENT '区块哈希',
    `tx_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '交易状态: PENDING（待确认）、SUCCESS（成功）、FAILED（失败）',
    `tx_type` VARCHAR(20) NOT NULL COMMENT '交易类型: SEND（发送）、RECEIVE（接收）、CONTRACT（合约调用）',
    `confirmed_at` DATETIME DEFAULT NULL COMMENT '确认时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tx_hash` (`tx_hash`),
    KEY `idx_wallet_id` (`wallet_id`),
    KEY `idx_tx_status` (`tx_status`),
    KEY `idx_tx_type` (`tx_type`),
    KEY `idx_block_number` (`block_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Web3交易记录表';

-- =====================================================
-- 32. 现货交易机器人模块 (Spot Trading Bot Module)
-- =====================================================

-- 现货交易机器人表
CREATE TABLE IF NOT EXISTS `spot_trading_bots` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `bot_name` VARCHAR(100) DEFAULT NULL COMMENT '机器人名称',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型: GRID（网格交易）, DCA（定投策略）, ARBITRAGE（套利策略）',
    `strategy_params` TEXT DEFAULT NULL COMMENT '策略参数（JSON格式）',
    `initial_capital` DECIMAL(20,8) NOT NULL COMMENT '初始资金',
    `current_capital` DECIMAL(20,8) NOT NULL COMMENT '当前资金',
    `total_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总亏损',
    `win_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '胜率',
    `total_trades` INT(11) DEFAULT 0 COMMENT '总交易次数',
    `status` VARCHAR(20) NOT NULL DEFAULT 'STOPPED' COMMENT '状态: STOPPED（已停止）、RUNNING（运行中）、PAUSED（已暂停）',
    `last_execution_time` DATETIME DEFAULT NULL COMMENT '上次执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pair_name` (`pair_name`),
    KEY `idx_strategy_type` (`strategy_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='现货交易机器人表';

-- 现货交易机器人订单记录表
CREATE TABLE IF NOT EXISTS `spot_trading_bot_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bot_id` BIGINT(20) NOT NULL COMMENT '机器人ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '关联的现货订单ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入）、SELL（卖出）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '交易数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '交易价格',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `profit_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '盈亏（卖出时计算）',
    `strategy_type` VARCHAR(50) NOT NULL COMMENT '策略类型',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_bot_id` (`bot_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='现货交易机器人订单记录表';

-- 现货交易机器人配置表
CREATE TABLE IF NOT EXISTS `spot_trading_bot_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如: BTC/USDT',
    `base_currency` VARCHAR(20) NOT NULL COMMENT '基础货币，如: BTC',
    `quote_currency` VARCHAR(20) NOT NULL COMMENT '计价货币，如: USDT',
    `order_interval_seconds` INT(11) NOT NULL COMMENT '下单时间间隔（秒）',
    `initial_order_quantity` DECIMAL(20,8) NOT NULL COMMENT '初始订单数量',
    `price_precision` INT(11) NOT NULL COMMENT '价格精度要求（小数位数）',
    `quantity_precision` INT(11) NOT NULL COMMENT '数量精度要求（小数位数）',
    `price_diff_type` VARCHAR(20) NOT NULL COMMENT '差价类型: RATIO（比例）, VALUE（值）',
    `max_price_diff` DECIMAL(20,8) NOT NULL COMMENT '买卖盘最高差价',
    `price_change_step_percent` DECIMAL(10,4) DEFAULT NULL COMMENT '价格变化步涨%（比例）',
    `min_trade_quantity` DECIMAL(20,8) NOT NULL COMMENT '最低交易量',
    `volume_random_factor_1` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%1',
    `volume_random_factor_2` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%2',
    `volume_random_factor_3` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%3',
    `volume_random_factor_4` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%4',
    `volume_random_factor_5` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%5',
    `volume_random_factor_6` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%6',
    `volume_random_factor_7` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%7',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '币种当前价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_name` (`pair_name`),
    KEY `idx_status` (`status`),
    KEY `idx_base_quote_currency` (`base_currency`, `quote_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='现货交易机器人配置表';

-- =====================================================
-- 33. 控盘机器人模块 (Control Panel Bot Module)
-- =====================================================

-- 控盘机器人表
CREATE TABLE IF NOT EXISTS `control_panel_bots` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `bot_name` VARCHAR(100) DEFAULT NULL COMMENT '机器人名称',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `control_type` VARCHAR(50) NOT NULL COMMENT '控盘类型: PRICE_CONTROL（价格控制）, VOLUME_CONTROL（成交量控制）, MARKET_MAKING（做市）',
    `control_params` TEXT DEFAULT NULL COMMENT '控盘参数（JSON格式）',
    `target_price` DECIMAL(20,8) DEFAULT NULL COMMENT '目标价格',
    `price_range_min` DECIMAL(20,8) DEFAULT NULL COMMENT '价格区间最小值',
    `price_range_max` DECIMAL(20,8) DEFAULT NULL COMMENT '价格区间最大值',
    `volume_target` DECIMAL(20,8) DEFAULT NULL COMMENT '目标成交量',
    `capital_allocated` DECIMAL(20,8) NOT NULL COMMENT '分配的资金',
    `capital_used` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已使用资金',
    `total_trades` INT(11) DEFAULT 0 COMMENT '总交易次数',
    `status` VARCHAR(20) NOT NULL DEFAULT 'STOPPED' COMMENT '状态: STOPPED（已停止）、RUNNING（运行中）、PAUSED（已暂停）',
    `last_execution_time` DATETIME DEFAULT NULL COMMENT '上次执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pair_name` (`pair_name`),
    KEY `idx_control_type` (`control_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='控盘机器人表';

-- 控盘机器人订单记录表
CREATE TABLE IF NOT EXISTS `control_panel_bot_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bot_id` BIGINT(20) NOT NULL COMMENT '机器人ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '关联的订单ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY（买入）、SELL（卖出）',
    `quantity` DECIMAL(20,8) NOT NULL COMMENT '交易数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '交易价格',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `control_type` VARCHAR(50) NOT NULL COMMENT '控盘类型',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_bot_id` (`bot_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='控盘机器人订单记录表';

-- 控盘机器人配置表
CREATE TABLE IF NOT EXISTS `control_panel_bot_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称，如: BTC/USDT',
    `base_currency` VARCHAR(20) NOT NULL COMMENT '基础货币，如: BTC',
    `quote_currency` VARCHAR(20) NOT NULL COMMENT '计价货币，如: USDT',
    `initial_price` DECIMAL(20,8) NOT NULL COMMENT '起始价格',
    `price_precision` INT(11) NOT NULL DEFAULT 8 COMMENT '价格精度（小数位数）',
    `quantity_precision` INT(11) NOT NULL DEFAULT 8 COMMENT '数量精度（小数位数）',
    `order_interval_seconds` INT(11) NOT NULL COMMENT '下单时间间隔（秒）',
    `initial_order_quantity` DECIMAL(20,8) NOT NULL COMMENT '初始订单数量',
    `price_diff_type` VARCHAR(20) NOT NULL COMMENT '差价类型: RATIO（比例）, VALUE（值）',
    `max_price_diff` DECIMAL(20,8) NOT NULL COMMENT '买卖盘最高差价',
    `price_change_step_percent` DECIMAL(10,4) DEFAULT NULL COMMENT '价格变化步涨%（比例）',
    `min_trade_quantity` DECIMAL(20,8) NOT NULL COMMENT '最低交易量',
    `volume_random_factor_1` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%1',
    `volume_random_factor_2` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%2',
    `volume_random_factor_3` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%3',
    `volume_random_factor_4` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%4',
    `volume_random_factor_5` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%5',
    `volume_random_factor_6` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%6',
    `volume_random_factor_7` DECIMAL(5,2) DEFAULT NULL COMMENT '交易量随机因子%7',
    `daily_high` DECIMAL(20,8) DEFAULT NULL COMMENT '二十四小时最高价',
    `daily_low` DECIMAL(20,8) DEFAULT NULL COMMENT '二十四小时最低价',
    `daily_volume` DECIMAL(20,8) DEFAULT NULL COMMENT '二十四小时交易量',
    `daily_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '二十四小时交易额',
    `current_price` DECIMAL(20,8) DEFAULT NULL COMMENT '当前价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pair_name` (`pair_name`),
    KEY `idx_status` (`status`),
    KEY `idx_base_quote_currency` (`base_currency`, `quote_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='控盘机器人配置表';

-- =====================================================
-- 34. 其他补充表 (Other Supplementary Tables)
-- =====================================================

-- 应用版本管理表
CREATE TABLE IF NOT EXISTS `app_versions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `platform` VARCHAR(50) NOT NULL COMMENT '平台: IOS, ANDROID, WEB',
    `version_code` VARCHAR(50) NOT NULL COMMENT '版本代码',
    `version_name` VARCHAR(100) NOT NULL COMMENT '版本名称',
    `update_type` VARCHAR(20) NOT NULL COMMENT '更新类型: FORCE（强制更新）, OPTIONAL（可选更新）, NONE（无需更新）',
    `download_url` VARCHAR(500) DEFAULT NULL COMMENT '下载地址',
    `update_description` TEXT DEFAULT NULL COMMENT '更新说明',
    `min_supported_version` VARCHAR(50) DEFAULT NULL COMMENT '最低支持版本',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `release_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_platform_version` (`platform`, `version_code`),
    KEY `idx_platform` (`platform`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用版本管理表';

-- 黑名单表
CREATE TABLE IF NOT EXISTS `blacklists` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `blacklist_type` VARCHAR(50) NOT NULL COMMENT '黑名单类型: IP（IP地址）, EMAIL（邮箱）, PHONE（手机号）, USER_ID（用户ID）, WALLET_ADDRESS（钱包地址）',
    `blacklist_value` VARCHAR(500) NOT NULL COMMENT '黑名单值',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '加入黑名单原因',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、INACTIVE（无效）',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间（NULL表示永久）',
    `created_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_blacklist_type` (`blacklist_type`),
    KEY `idx_blacklist_value` (`blacklist_value`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单表';

-- 区域表（多语言支持）
CREATE TABLE IF NOT EXISTS `regions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `region_code` VARCHAR(50) NOT NULL COMMENT '区域代码（唯一标识）',
    `region_name` VARCHAR(100) NOT NULL COMMENT '区域名称',
    `country_code` VARCHAR(10) DEFAULT NULL COMMENT '国家代码',
    `country_name` VARCHAR(100) DEFAULT NULL COMMENT '国家名称',
    `currency_code` VARCHAR(10) DEFAULT NULL COMMENT '货币代码',
    `timezone` VARCHAR(50) DEFAULT NULL COMMENT '时区',
    `language_code` VARCHAR(20) DEFAULT NULL COMMENT '语言代码',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_region_code` (`region_code`),
    KEY `idx_country_code` (`country_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区域表';

-- =====================================================
-- 测试数据插入
-- =====================================================

-- Web3钱包测试数据
INSERT INTO `web3_wallets` (`user_id`, `chain_type`, `wallet_address`, `wallet_name`, `is_default`, `status`) VALUES
(1, 'ETHEREUM', '0x1234567890123456789012345678901234567890', '我的以太坊钱包', 1, 'ACTIVE'),
(1, 'BSC', '0xabcdefabcdefabcdefabcdefabcdefabcdefabcd', '我的BSC钱包', 0, 'ACTIVE'),
(2, 'ETHEREUM', '0x9876543210987654321098765432109876543210', '以太坊钱包', 1, 'ACTIVE');

-- Web3代币余额测试数据
INSERT INTO `web3_token_balances` (`wallet_id`, `token_contract_address`, `token_symbol`, `token_name`, `token_decimals`, `balance`, `last_sync_time`) VALUES
(1, NULL, 'ETH', 'Ethereum', 18, 10.500000000000000000, '2024-01-15 10:00:00'),
(1, '0xdAC17F958D2ee523a2206206994597C13D831ec7', 'USDT', 'Tether USD', 6, 1000.000000, '2024-01-15 10:00:00'),
(2, NULL, 'BNB', 'Binance Coin', 18, 5.200000000000000000, '2024-01-15 09:00:00'),
(3, NULL, 'ETH', 'Ethereum', 18, 2.100000000000000000, '2024-01-15 11:00:00');

-- Web3交易记录测试数据
INSERT INTO `web3_transactions` (`wallet_id`, `tx_hash`, `from_address`, `to_address`, `token_contract_address`, `amount`, `gas_price`, `gas_used`, `tx_fee`, `block_number`, `tx_status`, `tx_type`, `confirmed_at`) VALUES
(1, '0xtx_hash_001', '0x1234567890123456789012345678901234567890', '0x9876543210987654321098765432109876543210', NULL, 1.000000000000000000, 0.000000020000000000, 21000, 0.000420000000000000, 18500000, 'SUCCESS', 'SEND', '2024-01-15 10:30:00'),
(1, '0xtx_hash_002', '0x9876543210987654321098765432109876543210', '0x1234567890123456789012345678901234567890', '0xdAC17F958D2ee523a2206206994597C13D831ec7', 100.000000, 0.000000020000000000, 65000, 0.001300000000000000, 18500001, 'SUCCESS', 'RECEIVE', '2024-01-15 11:00:00');

-- 现货交易机器人测试数据
INSERT INTO `spot_trading_bots` (`user_id`, `bot_name`, `pair_name`, `strategy_type`, `initial_capital`, `current_capital`, `total_profit`, `total_loss`, `win_rate`, `total_trades`, `status`) VALUES
(1, 'BTC网格机器人', 'BTC/USDT', 'GRID', 10000.00000000, 10500.00000000, 500.00000000, 0.00000000, 0.750000, 20, 'RUNNING'),
(2, 'ETH定投机器人', 'ETH/USDT', 'DCA', 5000.00000000, 5100.00000000, 100.00000000, 0.00000000, 0.600000, 10, 'RUNNING');

-- 现货交易机器人订单记录测试数据
INSERT INTO `spot_trading_bot_orders` (`bot_id`, `user_id`, `order_id`, `pair_name`, `side`, `quantity`, `price`, `amount`, `fee`, `profit_loss`, `strategy_type`) VALUES
(1, 1, 3001, 'BTC/USDT', 'BUY', 0.01, 45000.00, 450.00, 0.45, NULL, 'GRID'),
(1, 1, 3002, 'BTC/USDT', 'SELL', 0.01, 45500.00, 455.00, 0.46, 5.00, 'GRID'),
(2, 2, 3003, 'ETH/USDT', 'BUY', 1.0, 2800.00, 2800.00, 2.80, NULL, 'DCA');

-- 现货交易机器人配置测试数据
INSERT INTO `spot_trading_bot_configs` (`pair_name`, `base_currency`, `quote_currency`, `order_interval_seconds`, `initial_order_quantity`, `price_precision`, `quantity_precision`, `price_diff_type`, `max_price_diff`, `price_change_step_percent`, `min_trade_quantity`, `volume_random_factor_1`, `volume_random_factor_2`, `volume_random_factor_3`, `volume_random_factor_4`, `volume_random_factor_5`, `volume_random_factor_6`, `volume_random_factor_7`, `current_price`, `status`) VALUES
('BTC/USDT', 'BTC', 'USDT', 5, 0.01000000, 2, 8, 'RATIO', 0.50, 0.10, 0.00010000, 1.00, 9.00, 20.00, 20.00, 20.00, 20.00, 10.00, 50000.00, 'ACTIVE'),
('ETH/USDT', 'ETH', 'USDT', 5, 1.00000000, 2, 8, 'RATIO', 0.50, 0.10, 0.00100000, 1.00, 9.00, 20.00, 20.00, 20.00, 20.00, 10.00, 3000.00, 'ACTIVE');

-- 控盘机器人测试数据
INSERT INTO `control_panel_bots` (`user_id`, `bot_name`, `pair_name`, `control_type`, `target_price`, `price_range_min`, `price_range_max`, `volume_target`, `capital_allocated`, `capital_used`, `total_trades`, `status`) VALUES
(1, 'BTC价格控制机器人', 'BTC/USDT', 'PRICE_CONTROL', 45000.00, 44800.00, 45200.00, NULL, 50000.00000000, 25000.00000000, 15, 'RUNNING'),
(2, 'ETH做市机器人', 'ETH/USDT', 'MARKET_MAKING', NULL, 2780.00, 2820.00, 1000.00000000, 30000.00000000, 15000.00000000, 25, 'RUNNING');

-- 控盘机器人订单记录测试数据
INSERT INTO `control_panel_bot_orders` (`bot_id`, `user_id`, `order_id`, `pair_name`, `side`, `quantity`, `price`, `amount`, `fee`, `control_type`) VALUES
(1, 1, 4001, 'BTC/USDT', 'BUY', 0.1, 44900.00, 4490.00, 4.49, 'PRICE_CONTROL'),
(1, 1, 4002, 'BTC/USDT', 'SELL', 0.1, 45100.00, 4510.00, 4.51, 'PRICE_CONTROL'),
(2, 2, 4003, 'ETH/USDT', 'BUY', 5.0, 2790.00, 13950.00, 13.95, 'MARKET_MAKING'),
(2, 2, 4004, 'ETH/USDT', 'SELL', 5.0, 2810.00, 14050.00, 14.05, 'MARKET_MAKING');

-- 控盘机器人配置测试数据
INSERT INTO `control_panel_bot_configs` (`pair_name`, `base_currency`, `quote_currency`, `initial_price`, `price_precision`, `quantity_precision`, `order_interval_seconds`, `initial_order_quantity`, `price_diff_type`, `max_price_diff`, `price_change_step_percent`, `min_trade_quantity`, `volume_random_factor_1`, `volume_random_factor_2`, `volume_random_factor_3`, `volume_random_factor_4`, `volume_random_factor_5`, `volume_random_factor_6`, `volume_random_factor_7`, `daily_high`, `daily_low`, `daily_volume`, `daily_amount`, `current_price`, `status`) VALUES
('BTC/USDT', 'BTC', 'USDT', 50000.00000000, 8, 8, 5, 0.01000000, 'RATIO', 0.50, 0.10, 0.00010000, 1.00, 9.00, 20.00, 20.00, 20.00, 20.00, 10.00, 51000.00000000, 49000.00000000, 1000.00000000, 50000000.00000000, 50000.00000000, 'ACTIVE'),
('ETH/USDT', 'ETH', 'USDT', 3000.00000000, 8, 8, 5, 1.00000000, 'RATIO', 0.50, 0.10, 0.00100000, 1.00, 9.00, 20.00, 20.00, 20.00, 20.00, 10.00, 3100.00000000, 2900.00000000, 5000.00000000, 15000000.00000000, 3000.00000000, 'ACTIVE');

-- 应用版本管理测试数据
INSERT INTO `app_versions` (`platform`, `version_code`, `version_name`, `update_type`, `download_url`, `update_description`, `min_supported_version`, `status`, `release_time`) VALUES
('IOS', '1.0.0', '1.0.0', 'NONE', 'https://apps.apple.com/app/xxx', '初始版本发布', NULL, 'ACTIVE', '2024-01-01 00:00:00'),
('ANDROID', '1.0.0', '1.0.0', 'NONE', 'https://play.google.com/store/apps/details?id=xxx', '初始版本发布', NULL, 'ACTIVE', '2024-01-01 00:00:00'),
('IOS', '1.1.0', '1.1.0', 'OPTIONAL', 'https://apps.apple.com/app/xxx', '修复已知问题，优化用户体验', '1.0.0', 'ACTIVE', '2024-01-15 10:00:00'),
('ANDROID', '1.1.0', '1.1.0', 'OPTIONAL', 'https://play.google.com/store/apps/details?id=xxx', '修复已知问题，优化用户体验', '1.0.0', 'ACTIVE', '2024-01-15 10:00:00');

-- 黑名单测试数据
INSERT INTO `blacklists` (`blacklist_type`, `blacklist_value`, `reason`, `status`, `expire_time`, `created_by`) VALUES
('IP', '192.168.1.100', '恶意攻击行为', 'ACTIVE', NULL, 1),
('EMAIL', 'spam@example.com', '发送垃圾邮件', 'ACTIVE', '2024-12-31 23:59:59', 1),
('USER_ID', '999', '违规交易行为', 'ACTIVE', NULL, 1);

-- 区域测试数据
INSERT INTO `regions` (`region_code`, `region_name`, `country_code`, `country_name`, `currency_code`, `timezone`, `language_code`, `status`, `sort_order`) VALUES
('CN', '中国', 'CN', 'China', 'CNY', 'Asia/Shanghai', 'zh-CN', 'ACTIVE', 1),
('US', '美国', 'US', 'United States', 'USD', 'America/New_York', 'en-US', 'ACTIVE', 2),
('JP', '日本', 'JP', 'Japan', 'JPY', 'Asia/Tokyo', 'ja', 'ACTIVE', 3),
('KR', '韩国', 'KR', 'South Korea', 'KRW', 'Asia/Seoul', 'ko', 'ACTIVE', 4),
('GB', '英国', 'GB', 'United Kingdom', 'GBP', 'Europe/London', 'en-GB', 'ACTIVE', 5);

