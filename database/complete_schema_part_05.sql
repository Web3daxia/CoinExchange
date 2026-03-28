-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第5部分
-- 包含：跟单交易模块、自交易模块、质押借币模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 9. 跟单交易模块 (Copy Trading Module)
-- =====================================================

-- 带单员表
CREATE TABLE IF NOT EXISTS `traders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `trader_type` VARCHAR(20) NOT NULL COMMENT '交易员类型: SPOT（现货交易员）、FUTURES（合约交易员）、BOTH（两者都是）',
    `level` VARCHAR(20) NOT NULL DEFAULT 'BEGINNER' COMMENT '等级: BEGINNER（初级）、INTERMEDIATE（中级）、ADVANCED（高级）、TOP（顶级）',
    `margin_frozen` DECIMAL(20,8) NOT NULL COMMENT '冻结的保证金',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）、CANCELLED（已取消）',
    `public_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否开启公域跟单',
    `private_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否开启私域跟单',
    `invite_code` VARCHAR(50) DEFAULT NULL COMMENT '邀请码（私域跟单）',
    `subscription_fee` DECIMAL(20,8) DEFAULT NULL COMMENT '订阅费（私域跟单）',
    `commission_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '佣金比例',
    `profit_share_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '分润比例',
    `total_followers` INT(11) DEFAULT 0 COMMENT '总跟单人数',
    `total_aum` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总资产管理规模（AUM）',
    `total_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总亏损',
    `win_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '胜率',
    `sharpe_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '夏普比率',
    `max_drawdown` DECIMAL(10,4) DEFAULT NULL COMMENT '最大回撤',
    `last_liquidation_time` DATETIME DEFAULT NULL COMMENT '最近强制平仓时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_invite_code` (`invite_code`),
    KEY `idx_status` (`status`),
    KEY `idx_trader_type` (`trader_type`),
    KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='带单员表';

-- 带单员申请表
CREATE TABLE IF NOT EXISTS `trader_applications` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `trader_type` VARCHAR(20) NOT NULL COMMENT '交易员类型: SPOT（现货交易员）、FUTURES（合约交易员）、BOTH（两者都是）',
    `contact_info` TEXT DEFAULT NULL COMMENT '联系信息（JSON格式：Facebook, Twitter, Telegram, WeChat, QQ, Phone等）',
    `asset_proof` TEXT DEFAULT NULL COMMENT '资产证明（文件路径或URL）',
    `total_assets` DECIMAL(20,8) DEFAULT NULL COMMENT '总资产',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）',
    `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    `reviewed_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
    `reviewed_at` DATETIME DEFAULT NULL COMMENT '审核时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_trader_type` (`trader_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='带单员申请表';

-- 跟单关系表
CREATE TABLE IF NOT EXISTS `copy_trading_relations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `trader_id` BIGINT(20) NOT NULL COMMENT '带单员ID',
    `follower_id` BIGINT(20) NOT NULL COMMENT '跟单员ID',
    `market_type` VARCHAR(20) NOT NULL COMMENT '市场类型: SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）',
    `copy_type` VARCHAR(20) NOT NULL COMMENT '跟单类型: PUBLIC（公域跟单）、PRIVATE（私域跟单）',
    `allocation_amount` DECIMAL(20,8) NOT NULL COMMENT '分配的资金金额',
    `allocation_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '分配的资金比例',
    `leverage` INT(11) DEFAULT NULL COMMENT '杠杆倍数（合约跟单）',
    `margin_mode` VARCHAR(20) DEFAULT NULL COMMENT '保证金模式（合约跟单）: ISOLATED（逐仓）、CROSS（全仓）',
    `stop_loss_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止损价格',
    `take_profit_price` DECIMAL(20,8) DEFAULT NULL COMMENT '止盈价格',
    `stop_loss_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '止损百分比',
    `take_profit_percentage` DECIMAL(10,4) DEFAULT NULL COMMENT '止盈百分比',
    `copy_ratio` DECIMAL(10,4) NOT NULL DEFAULT 1.0000 COMMENT '跟单比例（1.0表示100%复制）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、PAUSED（已暂停）、STOPPED（已停止）',
    `total_profit` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总亏损',
    `total_commission` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总佣金',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_trader_id` (`trader_id`),
    KEY `idx_follower_id` (`follower_id`),
    KEY `idx_status` (`status`),
    KEY `idx_market_type` (`market_type`),
    KEY `idx_copy_type` (`copy_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跟单关系表';

-- 跟单订单表
CREATE TABLE IF NOT EXISTS `copy_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `trader_id` BIGINT(20) NOT NULL COMMENT '带单员ID',
    `trader_order_id` BIGINT(20) NOT NULL COMMENT '带单员的原始订单ID',
    `follower_id` BIGINT(20) NOT NULL COMMENT '跟单员ID',
    `relation_id` BIGINT(20) NOT NULL COMMENT '跟单关系ID',
    `follower_order_id` BIGINT(20) DEFAULT NULL COMMENT '跟单员的复制订单ID',
    `market_type` VARCHAR(20) NOT NULL COMMENT '市场类型: SPOT、FUTURES_USDT、FUTURES_COIN',
    `pair_name` VARCHAR(50) NOT NULL COMMENT '交易对名称',
    `side` VARCHAR(10) NOT NULL COMMENT '方向: BUY、SELL',
    `action` VARCHAR(10) NOT NULL COMMENT '操作: OPEN、CLOSE',
    `trader_quantity` DECIMAL(20,8) NOT NULL COMMENT '带单员的订单数量',
    `follower_quantity` DECIMAL(20,8) NOT NULL COMMENT '跟单员的订单数量',
    `price` DECIMAL(20,8) NOT NULL COMMENT '成交价格',
    `copy_ratio` DECIMAL(10,4) NOT NULL COMMENT '跟单比例',
    `commission` DECIMAL(20,8) DEFAULT NULL COMMENT '佣金',
    `profit_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '盈亏',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待执行）、EXECUTED（已执行）、FAILED（执行失败）',
    `executed_at` DATETIME DEFAULT NULL COMMENT '执行时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_trader_id` (`trader_id`),
    KEY `idx_follower_id` (`follower_id`),
    KEY `idx_relation_id` (`relation_id`),
    KEY `idx_trader_order_id` (`trader_order_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跟单订单表';

-- 带单员表现数据表
CREATE TABLE IF NOT EXISTS `trader_performances` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `trader_id` BIGINT(20) NOT NULL COMMENT '带单员ID',
    `period_type` VARCHAR(20) NOT NULL COMMENT '周期类型: DAILY（日）、WEEKLY（周）、MONTHLY（月）、YEARLY（年）',
    `period_start` DATETIME NOT NULL COMMENT '周期开始时间',
    `period_end` DATETIME NOT NULL COMMENT '周期结束时间',
    `total_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '总盈利',
    `total_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '总亏损',
    `net_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '净利润',
    `return_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '收益率',
    `win_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '胜率',
    `total_trades` INT(11) DEFAULT NULL COMMENT '总交易笔数',
    `winning_trades` INT(11) DEFAULT NULL COMMENT '盈利笔数',
    `losing_trades` INT(11) DEFAULT NULL COMMENT '亏损笔数',
    `avg_profit` DECIMAL(20,8) DEFAULT NULL COMMENT '平均盈利',
    `avg_loss` DECIMAL(20,8) DEFAULT NULL COMMENT '平均亏损',
    `profit_loss_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '盈亏比',
    `sharpe_ratio` DECIMAL(10,4) DEFAULT NULL COMMENT '夏普比率',
    `max_drawdown` DECIMAL(10,4) DEFAULT NULL COMMENT '最大回撤',
    `total_aum` DECIMAL(20,8) DEFAULT NULL COMMENT '总资产管理规模',
    `total_followers` INT(11) DEFAULT NULL COMMENT '总跟单人数',
    `daily_avg_trades` DECIMAL(10,2) DEFAULT NULL COMMENT '日均交易频次',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_trader_id` (`trader_id`),
    KEY `idx_period_type` (`period_type`),
    KEY `idx_period_start` (`period_start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='带单员表现数据表';

-- =====================================================
-- 11. 自选交易模块 (Self Trading Module)
-- =====================================================

-- 商家表
CREATE TABLE IF NOT EXISTS `merchants` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `merchant_name` VARCHAR(100) NOT NULL COMMENT '商家名称',
    `avatar` TEXT DEFAULT NULL COMMENT '头像URL',
    `signature` VARCHAR(500) DEFAULT NULL COMMENT '签名',
    `bio` TEXT DEFAULT NULL COMMENT '个人简介',
    `country` VARCHAR(50) DEFAULT NULL COMMENT '国家',
    `region` VARCHAR(50) DEFAULT NULL COMMENT '地区',
    `level` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '等级: CROWN（皇冠商家）、BLUE_V（蓝V商家）、NORMAL（普通商家）',
    `is_shield_merchant` TINYINT(1) DEFAULT 0 COMMENT '是否神盾商家',
    `margin_frozen` DECIMAL(20,8) NOT NULL COMMENT '冻结的保证金',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）、CANCELLED（已取消）',
    `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否可交易（是否接单）',
    `completed_orders` INT(11) DEFAULT 0 COMMENT '完成订单量',
    `completion_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '完成订单率',
    `avg_release_time` DECIMAL(10,2) DEFAULT NULL COMMENT '平均放币时间（分钟）',
    `total_rating` DECIMAL(10,2) DEFAULT NULL COMMENT '总评分',
    `rating_count` INT(11) DEFAULT 0 COMMENT '评分次数',
    `avg_rating` DECIMAL(10,2) DEFAULT NULL COMMENT '平均评分',
    `followers_count` INT(11) DEFAULT 0 COMMENT '关注人数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_level` (`level`),
    KEY `idx_country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家表';

-- 商家申请表
CREATE TABLE IF NOT EXISTS `merchant_applications` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `merchant_name` VARCHAR(100) NOT NULL COMMENT '商家名称',
    `avatar` TEXT DEFAULT NULL COMMENT '头像URL',
    `signature` VARCHAR(500) DEFAULT NULL COMMENT '签名',
    `bio` TEXT DEFAULT NULL COMMENT '个人简介',
    `country` VARCHAR(50) DEFAULT NULL COMMENT '国家',
    `region` VARCHAR(50) DEFAULT NULL COMMENT '地区',
    `asset_proof` TEXT DEFAULT NULL COMMENT '资产证明图片（JSON数组）',
    `total_assets` DECIMAL(20,8) DEFAULT NULL COMMENT '总资产',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）',
    `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    `reviewed_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
    `reviewed_at` DATETIME DEFAULT NULL COMMENT '审核时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家申请表';

-- 交易广告表
CREATE TABLE IF NOT EXISTS `trading_ads` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `merchant_id` BIGINT(20) NOT NULL COMMENT '商家ID',
    `ad_type` VARCHAR(20) NOT NULL COMMENT '广告类型: SELL（出售广告）、BUY（买入广告）',
    `crypto_currency` VARCHAR(20) NOT NULL COMMENT '加密货币类型，如 USDT, BTC, ETH',
    `fiat_currency` VARCHAR(20) NOT NULL COMMENT '支付法币，如 USD, CNY, EUR',
    `price` DECIMAL(20,8) NOT NULL COMMENT '广告单价（法币）',
    `min_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最小交易金额',
    `max_amount` DECIMAL(20,8) NOT NULL COMMENT '最大交易金额（限额）',
    `available_amount` DECIMAL(20,8) NOT NULL COMMENT '可用金额',
    `payment_methods` TEXT NOT NULL COMMENT '支持的支付方式（JSON数组）',
    `require_kyc` TINYINT(1) DEFAULT 0 COMMENT '是否需要KYC验证',
    `require_asset_proof` TINYINT(1) DEFAULT 0 COMMENT '是否需要资产证明',
    `require_transaction_history` TINYINT(1) DEFAULT 0 COMMENT '是否需要交易流水',
    `auto_reply_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否自动回复',
    `auto_reply_content` TEXT DEFAULT NULL COMMENT '自动回复内容',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（活跃）、PAUSED（已暂停）、CLOSED（已关闭）',
    `order_count` INT(11) DEFAULT 0 COMMENT '订单数量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_status` (`status`),
    KEY `idx_ad_type` (`ad_type`),
    KEY `idx_crypto_fiat` (`crypto_currency`, `fiat_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易广告表';

-- 自选交易订单表
CREATE TABLE IF NOT EXISTS `self_trading_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `merchant_id` BIGINT(20) NOT NULL COMMENT '商家ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `ad_id` BIGINT(20) NOT NULL COMMENT '广告ID',
    `order_type` VARCHAR(20) NOT NULL COMMENT '订单类型: BUY（买入）、SELL（卖出）',
    `crypto_currency` VARCHAR(20) NOT NULL COMMENT '加密货币类型',
    `fiat_currency` VARCHAR(20) NOT NULL COMMENT '法币类型',
    `payment_method` VARCHAR(50) NOT NULL COMMENT '支付方式',
    `crypto_amount` DECIMAL(20,8) NOT NULL COMMENT '加密货币数量',
    `fiat_amount` DECIMAL(20,8) NOT NULL COMMENT '法币金额',
    `price` DECIMAL(20,8) NOT NULL COMMENT '成交价格',
    `fee` DECIMAL(20,8) DEFAULT NULL COMMENT '手续费',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待付款）、PAID（已付款）、RELEASED（已放币）、COMPLETED（已完成）、CANCELLED（已取消）、DISPUTED（申诉中）',
    `payment_proof` TEXT DEFAULT NULL COMMENT '支付凭证（图片URL）',
    `processing_timeout` INT(11) DEFAULT NULL COMMENT '处理时限（分钟）',
    `paid_at` DATETIME DEFAULT NULL COMMENT '支付时间',
    `released_at` DATETIME DEFAULT NULL COMMENT '放币时间',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `cancelled_at` DATETIME DEFAULT NULL COMMENT '取消时间',
    `cancelled_reason` VARCHAR(500) DEFAULT NULL COMMENT '取消原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_ad_id` (`ad_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自选交易订单表';

-- 聊天消息表
CREATE TABLE IF NOT EXISTS `chat_messages` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '关联的订单ID',
    `sender_id` BIGINT(20) NOT NULL COMMENT '发送者ID',
    `receiver_id` BIGINT(20) NOT NULL COMMENT '接收者ID',
    `message_type` VARCHAR(20) NOT NULL COMMENT '消息类型: TEXT（文字）、IMAGE（图片）、VIDEO（视频）、FILE（文件）、EMOJI（表情）',
    `content` TEXT DEFAULT NULL COMMENT '消息内容',
    `file_url` TEXT DEFAULT NULL COMMENT '文件URL（图片、视频、文件）',
    `file_name` VARCHAR(255) DEFAULT NULL COMMENT '文件名',
    `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小（字节）',
    `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- 商家评价表
CREATE TABLE IF NOT EXISTS `merchant_ratings` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `merchant_id` BIGINT(20) NOT NULL COMMENT '商家ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '评价用户ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '关联的订单ID',
    `rating` INT(11) NOT NULL COMMENT '评分（1-5星）',
    `comment` TEXT DEFAULT NULL COMMENT '评论内容',
    `is_anonymous` TINYINT(1) DEFAULT 0 COMMENT '是否匿名评价',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、DELETED（已删除）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家评价表';

-- 商家关注表
CREATE TABLE IF NOT EXISTS `merchant_follows` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `merchant_id` BIGINT(20) NOT NULL COMMENT '商家ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '关注用户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_merchant_user` (`merchant_id`, `user_id`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家关注表';

-- =====================================================
-- 17. 质押借币模块 (Pledge Loan Module)
-- =====================================================

-- 质押币种配置表
CREATE TABLE IF NOT EXISTS `pledge_currency_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `currency_code` VARCHAR(20) NOT NULL COMMENT '币种代码，如: BTC, ETH, USDT',
    `currency_name` VARCHAR(100) NOT NULL COMMENT '币种名称',
    `loan_ratio` DECIMAL(10,4) NOT NULL COMMENT '借款比例（质押比例），如: 0.5000 表示50%，即质押1个币可以借0.5个币',
    `interest_rate` DECIMAL(10,6) NOT NULL COMMENT '借款利率（年化），如: 0.050000 表示5%',
    `min_pledge_amount` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '最小质押金额',
    `max_loan_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最大借款额度（单个用户）',
    `risk_rate` DECIMAL(10,4) NOT NULL DEFAULT 0.8000 COMMENT '风险率（平仓线），如: 0.8000 表示80%，当质押资产价值/借款价值低于80%时触发平仓',
    `maintenance_rate` DECIMAL(10,4) NOT NULL DEFAULT 0.8500 COMMENT '维持质押率（预警线），如: 0.8500 表示85%，低于此值需要补仓',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）, INACTIVE（停用）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_currency_code` (`currency_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押币种配置表';

-- 借款币种配置表
CREATE TABLE IF NOT EXISTS `loan_currency_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `currency_code` VARCHAR(20) NOT NULL COMMENT '币种代码，如: USDT, BTC, ETH',
    `currency_name` VARCHAR(100) NOT NULL COMMENT '币种名称',
    `interest_rate` DECIMAL(10,6) NOT NULL COMMENT '借款利率（年化），如: 0.050000 表示5%',
    `min_loan_amount` DECIMAL(20,8) NOT NULL DEFAULT 0.00000000 COMMENT '最小借款金额',
    `max_loan_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最大借款额度（单个用户）',
    `loan_term_options` VARCHAR(200) DEFAULT NULL COMMENT '借款期限选项（JSON格式，如: ["7", "30", "90", "180", "365"]，单位：天）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）, INACTIVE（停用）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_currency_code` (`currency_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借款币种配置表';

-- 质押借币订单表
CREATE TABLE IF NOT EXISTS `pledge_loan_orders` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号（唯一标识）',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `pledge_currency` VARCHAR(20) NOT NULL COMMENT '质押币种',
    `pledge_amount` DECIMAL(20,8) NOT NULL COMMENT '质押数量',
    `pledge_value` DECIMAL(20,8) NOT NULL COMMENT '质押价值（USDT）',
    `loan_currency` VARCHAR(20) NOT NULL COMMENT '借款币种',
    `loan_amount` DECIMAL(20,8) NOT NULL COMMENT '借款数量',
    `loan_value` DECIMAL(20,8) NOT NULL COMMENT '借款价值（USDT）',
    `interest_rate` DECIMAL(10,6) NOT NULL COMMENT '借款利率（年化）',
    `loan_term_days` INT(11) NOT NULL COMMENT '借款期限（天）',
    `pledge_ratio` DECIMAL(10,4) NOT NULL COMMENT '质押比例（质押价值/借款价值）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '订单状态: PENDING（待审核）, APPROVED（已批准）, REJECTED（已拒绝）, ACTIVE（生效中）, REPAID（已还清）, LIQUIDATED（已平仓）, CLOSED（已关闭）',
    `approval_status` VARCHAR(20) DEFAULT NULL COMMENT '审批状态: AUTO（自动审批）, MANUAL（人工审批）, PENDING（待审批）',
    `approver_id` BIGINT(20) DEFAULT NULL COMMENT '审批人ID',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批备注',
    `start_time` DATETIME DEFAULT NULL COMMENT '借款开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '借款到期时间',
    `actual_end_time` DATETIME DEFAULT NULL COMMENT '实际还款时间',
    `total_interest` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总利息（累计应付利息）',
    `paid_interest` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已付利息',
    `remaining_principal` DECIMAL(20,8) NOT NULL COMMENT '剩余本金',
    `liquidation_price` DECIMAL(20,8) DEFAULT NULL COMMENT '平仓价格（当质押资产价格跌至此价格时触发平仓）',
    `health_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '健康度（质押价值/借款价值）',
    `last_health_check_time` DATETIME DEFAULT NULL COMMENT '上次健康度检查时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_pledge_currency` (`pledge_currency`),
    KEY `idx_loan_currency` (`loan_currency`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币订单表';

-- 质押借币还款记录表
CREATE TABLE IF NOT EXISTS `pledge_loan_repayments` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `repayment_no` VARCHAR(50) NOT NULL COMMENT '还款单号（唯一标识）',
    `repayment_type` VARCHAR(20) NOT NULL COMMENT '还款类型: PRINCIPAL（本金）, INTEREST（利息）, FULL（全额还款）, PARTIAL（部分还款）',
    `principal_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '还款本金',
    `interest_amount` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '还款利息',
    `total_amount` DECIMAL(20,8) NOT NULL COMMENT '还款总额',
    `repayment_currency` VARCHAR(20) NOT NULL COMMENT '还款币种',
    `repayment_time` DATETIME NOT NULL COMMENT '还款时间',
    `is_early_repayment` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否提前还款: 0否, 1是',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_repayment_no` (`repayment_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_repayment_time` (`repayment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币还款记录表';

-- 质押借币平仓记录表
CREATE TABLE IF NOT EXISTS `pledge_loan_liquidations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `liquidation_no` VARCHAR(50) NOT NULL COMMENT '平仓单号（唯一标识）',
    `liquidation_type` VARCHAR(20) NOT NULL COMMENT '平仓类型: AUTO（自动平仓）, MANUAL（手动平仓）',
    `liquidation_reason` VARCHAR(500) NOT NULL COMMENT '平仓原因',
    `pledge_currency` VARCHAR(20) NOT NULL COMMENT '质押币种',
    `pledge_amount` DECIMAL(20,8) NOT NULL COMMENT '质押数量',
    `pledge_value_before` DECIMAL(20,8) NOT NULL COMMENT '平仓前质押价值（USDT）',
    `pledge_value_after` DECIMAL(20,8) DEFAULT NULL COMMENT '平仓后质押价值（USDT）',
    `loan_currency` VARCHAR(20) NOT NULL COMMENT '借款币种',
    `loan_amount` DECIMAL(20,8) NOT NULL COMMENT '借款数量',
    `loan_value` DECIMAL(20,8) NOT NULL COMMENT '借款价值（USDT）',
    `health_rate_before` DECIMAL(10,6) NOT NULL COMMENT '平仓前健康度',
    `liquidation_price` DECIMAL(20,8) NOT NULL COMMENT '平仓价格',
    `liquidation_time` DATETIME NOT NULL COMMENT '平仓时间',
    `liquidator_id` BIGINT(20) DEFAULT NULL COMMENT '平仓操作人ID',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_liquidation_no` (`liquidation_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_liquidation_time` (`liquidation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币平仓记录表';

-- 质押借币补仓记录表
CREATE TABLE IF NOT EXISTS `pledge_loan_topups` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `topup_no` VARCHAR(50) NOT NULL COMMENT '补仓单号（唯一标识）',
    `topup_currency` VARCHAR(20) NOT NULL COMMENT '补仓币种',
    `topup_amount` DECIMAL(20,8) NOT NULL COMMENT '补仓数量',
    `topup_value` DECIMAL(20,8) NOT NULL COMMENT '补仓价值（USDT）',
    `pledge_amount_before` DECIMAL(20,8) NOT NULL COMMENT '补仓前质押数量',
    `pledge_amount_after` DECIMAL(20,8) NOT NULL COMMENT '补仓后质押数量',
    `pledge_value_before` DECIMAL(20,8) NOT NULL COMMENT '补仓前质押价值（USDT）',
    `pledge_value_after` DECIMAL(20,8) NOT NULL COMMENT '补仓后质押价值（USDT）',
    `health_rate_before` DECIMAL(10,6) NOT NULL COMMENT '补仓前健康度',
    `health_rate_after` DECIMAL(10,6) NOT NULL COMMENT '补仓后健康度',
    `topup_time` DATETIME NOT NULL COMMENT '补仓时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_topup_no` (`topup_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_topup_time` (`topup_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币补仓记录表';

-- 质押借币利率历史表
CREATE TABLE IF NOT EXISTS `pledge_loan_rate_histories` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `old_interest_rate` DECIMAL(10,6) NOT NULL COMMENT '旧利率',
    `new_interest_rate` DECIMAL(10,6) NOT NULL COMMENT '新利率',
    `change_time` DATETIME NOT NULL COMMENT '变更时间',
    `change_reason` VARCHAR(500) DEFAULT NULL COMMENT '变更原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_change_time` (`change_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币利率历史表';

-- 质押借币风险记录表
CREATE TABLE IF NOT EXISTS `pledge_loan_risk_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `risk_type` VARCHAR(20) NOT NULL COMMENT '风险类型: HEALTH_RATE_LOW（健康度低）, LIQUIDATION_RISK（平仓风险）, INTEREST_OVERDUE（利息逾期）',
    `risk_level` VARCHAR(20) NOT NULL COMMENT '风险等级: LOW（低）, MEDIUM（中）, HIGH（高）, CRITICAL（严重）',
    `health_rate` DECIMAL(10,6) DEFAULT NULL COMMENT '健康度',
    `pledge_value` DECIMAL(20,8) DEFAULT NULL COMMENT '质押价值',
    `loan_value` DECIMAL(20,8) DEFAULT NULL COMMENT '借款价值',
    `risk_message` VARCHAR(500) DEFAULT NULL COMMENT '风险信息',
    `is_resolved` TINYINT(1) DEFAULT 0 COMMENT '是否已解决',
    `resolved_time` DATETIME DEFAULT NULL COMMENT '解决时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_risk_type` (`risk_type`),
    KEY `idx_risk_level` (`risk_level`),
    KEY `idx_is_resolved` (`is_resolved`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质押借币风险记录表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 带单员表测试数据
INSERT INTO `traders` (`id`, `user_id`, `trader_type`, `level`, `margin_frozen`, `status`, `public_enabled`, `private_enabled`, `invite_code`, `commission_rate`, `profit_share_rate`, `total_followers`, `total_aum`, `total_profit`, `total_loss`, `win_rate`, `sharpe_ratio`, `max_drawdown`, `created_at`, `updated_at`) VALUES
(1, 1, 'BOTH', 'ADVANCED', 10000.00000000, 'APPROVED', 1, 1, 'TRADER001', 0.1000, 0.2000, 50, 500000.00000000, 10000.00000000, 2000.00000000, 70.0000, 1.5000, 0.1500, '2024-01-01 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'FUTURES', 'TOP', 20000.00000000, 'APPROVED', 1, 0, 'TRADER002', 0.1500, 0.2500, 100, 1000000.00000000, 20000.00000000, 3000.00000000, 75.0000, 2.0000, 0.1200, '2024-01-02 10:00:00', '2024-01-15 10:00:00');

-- 带单员申请表测试数据
INSERT INTO `trader_applications` (`id`, `user_id`, `trader_type`, `contact_info`, `asset_proof`, `total_assets`, `status`, `reviewed_by`, `reviewed_at`, `created_at`, `updated_at`) VALUES
(1, 1, 'BOTH', '{"telegram": "@trader1", "wechat": "trader1_wx"}', 'https://example.com/proof1.jpg', 100000.00000000, 'APPROVED', 1, '2024-01-01 11:00:00', '2024-01-01 10:00:00', '2024-01-01 11:00:00'),
(2, 2, 'FUTURES', '{"telegram": "@trader2", "twitter": "@trader2"}', 'https://example.com/proof2.jpg', 200000.00000000, 'APPROVED', 1, '2024-01-02 11:00:00', '2024-01-02 10:00:00', '2024-01-02 11:00:00');

-- 跟单关系表测试数据
INSERT INTO `copy_trading_relations` (`id`, `trader_id`, `follower_id`, `market_type`, `copy_type`, `allocation_amount`, `allocation_percentage`, `leverage`, `margin_mode`, `copy_ratio`, `status`, `total_profit`, `total_loss`, `total_commission`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'SPOT', 'PUBLIC', 10000.00000000, 0.2000, NULL, NULL, 1.0000, 'ACTIVE', 500.00000000, 100.00000000, 50.00000000, '2024-01-10 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 2, 'FUTURES_USDT', 'PRIVATE', 20000.00000000, 0.3000, 10, 'ISOLATED', 0.8000, 'ACTIVE', 1000.00000000, 200.00000000, 150.00000000, '2024-01-11 10:00:00', '2024-01-15 10:00:00');

-- 跟单订单表测试数据
INSERT INTO `copy_orders` (`id`, `trader_id`, `trader_order_id`, `follower_id`, `relation_id`, `follower_order_id`, `market_type`, `pair_name`, `side`, `action`, `trader_quantity`, `follower_quantity`, `price`, `copy_ratio`, `commission`, `profit_loss`, `status`, `executed_at`, `created_at`) VALUES
(1, 1, 1, 1, 1, 2, 'SPOT', 'BTC/USDT', 'BUY', 'OPEN', 0.10000000, 0.10000000, 49000.00000000, 1.0000, 4.90000000, NULL, 'EXECUTED', '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 1, 3, 1, 1, 4, 'SPOT', 'BTC/USDT', 'SELL', 'CLOSE', 0.10000000, 0.10000000, 50000.00000000, 1.0000, 5.00000000, 100.00000000, 'EXECUTED', '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 带单员表现数据表测试数据
INSERT INTO `trader_performances` (`id`, `trader_id`, `period_type`, `period_start`, `period_end`, `total_profit`, `total_loss`, `net_profit`, `return_rate`, `win_rate`, `total_trades`, `winning_trades`, `losing_trades`, `avg_profit`, `avg_loss`, `profit_loss_ratio`, `sharpe_ratio`, `max_drawdown`, `total_aum`, `total_followers`, `daily_avg_trades`, `created_at`, `updated_at`) VALUES
(1, 1, 'MONTHLY', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 10000.00000000, 2000.00000000, 8000.00000000, 8.0000, 70.0000, 100, 70, 30, 142.85714286, -66.66666667, 2.1429, 1.5000, 0.1500, 500000.00000000, 50, 3.23, '2024-01-31 23:59:59', '2024-01-31 23:59:59'),
(2, 2, 'MONTHLY', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 20000.00000000, 3000.00000000, 17000.00000000, 8.5000, 75.0000, 150, 113, 37, 176.99115044, -81.08108108, 2.1837, 2.0000, 0.1200, 1000000.00000000, 100, 4.84, '2024-01-31 23:59:59', '2024-01-31 23:59:59');

-- 商家表测试数据
INSERT INTO `merchants` (`id`, `user_id`, `merchant_name`, `avatar`, `signature`, `bio`, `country`, `region`, `level`, `is_shield_merchant`, `margin_frozen`, `status`, `is_active`, `completed_orders`, `completion_rate`, `avg_release_time`, `total_rating`, `rating_count`, `avg_rating`, `followers_count`, `created_at`, `updated_at`) VALUES
(1, 1, '诚信商家001', 'https://example.com/avatar1.jpg', '诚信经营，快速放币', '专业加密货币交易商家', 'CN', 'Beijing', 'CROWN', 1, 50000.00000000, 'APPROVED', 1, 1000, 99.5000, 5.00, 4900.00, 980, 5.0000, 500, '2024-01-01 10:00:00', '2024-01-15 10:00:00'),
(2, 2, '快速交易002', 'https://example.com/avatar2.jpg', '24小时在线，秒放币', '资深加密货币交易商家', 'US', 'New York', 'BLUE_V', 0, 30000.00000000, 'APPROVED', 1, 500, 98.0000, 3.00, 2400.00, 490, 4.8979, 300, '2024-01-02 10:00:00', '2024-01-15 10:00:00');

-- 商家申请表测试数据
INSERT INTO `merchant_applications` (`id`, `user_id`, `merchant_name`, `avatar`, `signature`, `bio`, `country`, `region`, `asset_proof`, `total_assets`, `status`, `reviewed_by`, `reviewed_at`, `created_at`, `updated_at`) VALUES
(1, 1, '诚信商家001', 'https://example.com/avatar1.jpg', '诚信经营，快速放币', '专业加密货币交易商家', 'CN', 'Beijing', '["https://example.com/proof1.jpg", "https://example.com/proof2.jpg"]', 100000.00000000, 'APPROVED', 1, '2024-01-01 11:00:00', '2024-01-01 10:00:00', '2024-01-01 11:00:00'),
(2, 2, '快速交易002', 'https://example.com/avatar2.jpg', '24小时在线，秒放币', '资深加密货币交易商家', 'US', 'New York', '["https://example.com/proof3.jpg"]', 50000.00000000, 'APPROVED', 1, '2024-01-02 11:00:00', '2024-01-02 10:00:00', '2024-01-02 11:00:00');

-- 交易广告表测试数据
INSERT INTO `trading_ads` (`id`, `merchant_id`, `ad_type`, `crypto_currency`, `fiat_currency`, `price`, `min_amount`, `max_amount`, `available_amount`, `payment_methods`, `require_kyc`, `require_asset_proof`, `auto_reply_enabled`, `auto_reply_content`, `status`, `order_count`, `created_at`, `updated_at`) VALUES
(1, 1, 'SELL', 'USDT', 'CNY', 7.20000000, 100.00000000, 50000.00000000, 50000.00000000, '["ALIPAY", "WECHAT", "BANK_TRANSFER"]', 0, 0, 1, '请先付款，付款后立即放币', 'ACTIVE', 100, '2024-01-01 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'BUY', 'BTC', 'USD', 50000.00000000, 0.00100000, 1.00000000, 1.00000000, '["PAYPAL", "BANK_TRANSFER"]', 1, 1, 1, '请先放币，确认后立即付款', 'ACTIVE', 50, '2024-01-02 10:00:00', '2024-01-15 10:00:00');

-- 自选交易订单表测试数据
INSERT INTO `self_trading_orders` (`id`, `order_no`, `merchant_id`, `user_id`, `ad_id`, `order_type`, `crypto_currency`, `fiat_currency`, `payment_method`, `crypto_amount`, `fiat_amount`, `price`, `fee`, `status`, `paid_at`, `released_at`, `completed_at`, `created_at`, `updated_at`) VALUES
(1, 'ST20240110001', 1, 1, 1, 'BUY', 'USDT', 'CNY', 'ALIPAY', 1000.00000000, 7200.00000000, 7.20000000, 7.20000000, 'COMPLETED', '2024-01-10 10:00:00', '2024-01-10 10:05:00', '2024-01-10 10:05:00', '2024-01-10 10:00:00', '2024-01-10 10:05:00'),
(2, 'ST20240111001', 2, 2, 2, 'SELL', 'BTC', 'USD', 'PAYPAL', 0.01000000, 500.00000000, 50000.00000000, 2.50000000, 'COMPLETED', '2024-01-11 10:00:00', '2024-01-11 10:03:00', '2024-01-11 10:03:00', '2024-01-11 10:00:00', '2024-01-11 10:03:00');

-- 聊天消息表测试数据
INSERT INTO `chat_messages` (`id`, `order_id`, `sender_id`, `receiver_id`, `message_type`, `content`, `is_read`, `created_at`) VALUES
(1, 1, 1, 1, 'TEXT', '已付款，请查收', 1, '2024-01-10 10:01:00'),
(2, 1, 1, 1, 'TEXT', '已放币，请查收', 1, '2024-01-10 10:05:00');

-- 商家评价表测试数据
INSERT INTO `merchant_ratings` (`id`, `merchant_id`, `user_id`, `order_id`, `rating`, `comment`, `is_anonymous`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 5, '商家很诚信，放币很快', 0, 'ACTIVE', '2024-01-10 10:10:00', '2024-01-10 10:10:00'),
(2, 2, 2, 2, 4, '交易顺利，服务不错', 0, 'ACTIVE', '2024-01-11 10:10:00', '2024-01-11 10:10:00');

-- 商家关注表测试数据
INSERT INTO `merchant_follows` (`id`, `merchant_id`, `user_id`, `created_at`) VALUES
(1, 1, 1, '2024-01-10 10:00:00'),
(2, 2, 2, '2024-01-11 10:00:00');

-- 质押币种配置表测试数据
INSERT INTO `pledge_currency_configs` (`id`, `currency_code`, `currency_name`, `loan_ratio`, `interest_rate`, `min_pledge_amount`, `max_loan_amount`, `risk_rate`, `maintenance_rate`, `status`, `sort_order`, `description`, `created_at`, `updated_at`) VALUES
(1, 'BTC', 'Bitcoin', 0.7000, 0.080000, 0.10000000, 100.00000000, 0.8000, 0.8500, 'ACTIVE', 1, 'BTC质押借币配置', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ETH', 'Ethereum', 0.6000, 0.100000, 1.00000000, 1000.00000000, 0.8000, 0.8500, 'ACTIVE', 2, 'ETH质押借币配置', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 借款币种配置表测试数据
INSERT INTO `loan_currency_configs` (`id`, `currency_code`, `currency_name`, `interest_rate`, `min_loan_amount`, `max_loan_amount`, `loan_term_options`, `status`, `sort_order`, `description`, `created_at`, `updated_at`) VALUES
(1, 'USDT', 'Tether', 0.080000, 100.00000000, 100000.00000000, '["7", "30", "90", "180", "365"]', 'ACTIVE', 1, 'USDT借款配置', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'BTC', 'Bitcoin', 0.100000, 0.00100000, 10.00000000, '["7", "30", "90", "180", "365"]', 'ACTIVE', 2, 'BTC借款配置', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 质押借币订单表测试数据
INSERT INTO `pledge_loan_orders` (`id`, `order_no`, `user_id`, `pledge_currency`, `pledge_amount`, `pledge_value`, `loan_currency`, `loan_amount`, `loan_value`, `interest_rate`, `loan_term_days`, `pledge_ratio`, `status`, `approval_status`, `start_time`, `end_time`, `total_interest`, `paid_interest`, `remaining_principal`, `liquidation_price`, `health_rate`, `created_at`, `updated_at`) VALUES
(1, 'PLO20240110001', 1, 'BTC', 1.00000000, 50000.00000000, 'USDT', 35000.00000000, 35000.00000000, 0.080000, 30, 1.4286, 'ACTIVE', 'AUTO', '2024-01-10 10:00:00', '2024-02-09 10:00:00', 230.13698630, 0.00000000, 35000.00000000, 40000.00000000, 1.4286, '2024-01-10 10:00:00', '2024-01-10 10:00:00'),
(2, 'PLO20240111001', 2, 'ETH', 10.00000000, 30000.00000000, 'USDT', 18000.00000000, 18000.00000000, 0.100000, 90, 1.6667, 'ACTIVE', 'AUTO', '2024-01-11 10:00:00', '2024-04-10 10:00:00', 443.83561644, 0.00000000, 18000.00000000, 2400.00000000, 1.6667, '2024-01-11 10:00:00', '2024-01-11 10:00:00');

-- 质押借币还款记录表测试数据
INSERT INTO `pledge_loan_repayments` (`id`, `order_id`, `order_no`, `user_id`, `repayment_no`, `repayment_type`, `principal_amount`, `interest_amount`, `total_amount`, `repayment_currency`, `repayment_time`, `is_early_repayment`, `created_at`) VALUES
(1, 1, 'PLO20240110001', 1, 'REPAY20240120001', 'INTEREST', 0.00000000, 76.71232877, 76.71232877, 'USDT', '2024-01-20 10:00:00', 0, '2024-01-20 10:00:00'),
(2, 2, 'PLO20240111001', 2, 'REPAY20240121001', 'INTEREST', 0.00000000, 49.31506849, 49.31506849, 'USDT', '2024-01-21 10:00:00', 0, '2024-01-21 10:00:00');

-- 质押借币平仓记录表测试数据
INSERT INTO `pledge_loan_liquidations` (`id`, `order_id`, `order_no`, `user_id`, `liquidation_no`, `liquidation_type`, `liquidation_reason`, `pledge_currency`, `pledge_amount`, `pledge_value_before`, `pledge_value_after`, `loan_currency`, `loan_amount`, `loan_value`, `health_rate_before`, `liquidation_price`, `liquidation_time`, `created_at`) VALUES
(1, 1, 'PLO20240110001', 1, 'LIQ20240125001', 'AUTO', '健康度低于平仓线', 'BTC', 1.00000000, 40000.00000000, 40000.00000000, 'USDT', 35000.00000000, 35000.00000000, 0.8000, 40000.00000000, '2024-01-25 10:00:00', '2024-01-25 10:00:00'),
(2, 2, 'PLO20240111001', 2, 'LIQ20240126001', 'AUTO', '健康度低于平仓线', 'ETH', 10.00000000, 24000.00000000, 24000.00000000, 'USDT', 18000.00000000, 18000.00000000, 0.8000, 2400.00000000, '2024-01-26 10:00:00', '2024-01-26 10:00:00');

-- 质押借币补仓记录表测试数据
INSERT INTO `pledge_loan_topups` (`id`, `order_id`, `order_no`, `user_id`, `topup_no`, `topup_currency`, `topup_amount`, `topup_value`, `pledge_amount_before`, `pledge_amount_after`, `pledge_value_before`, `pledge_value_after`, `health_rate_before`, `health_rate_after`, `topup_time`, `created_at`) VALUES
(1, 1, 'PLO20240110001', 1, 'TOPUP20240115001', 'BTC', 0.20000000, 10000.00000000, 1.00000000, 1.20000000, 45000.00000000, 60000.00000000, 1.2857, 1.7143, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 2, 'PLO20240111001', 2, 'TOPUP20240116001', 'ETH', 2.00000000, 6000.00000000, 10.00000000, 12.00000000, 27000.00000000, 36000.00000000, 1.5000, 2.0000, '2024-01-16 10:00:00', '2024-01-16 10:00:00');

-- 质押借币利率历史表测试数据
INSERT INTO `pledge_loan_rate_histories` (`id`, `order_id`, `old_interest_rate`, `new_interest_rate`, `change_time`, `change_reason`, `created_at`) VALUES
(1, 1, 0.080000, 0.090000, '2024-01-15 10:00:00', '市场利率调整', '2024-01-15 10:00:00'),
(2, 2, 0.100000, 0.110000, '2024-01-16 10:00:00', '市场利率调整', '2024-01-16 10:00:00');

-- 质押借币风险记录表测试数据
INSERT INTO `pledge_loan_risk_records` (`id`, `order_id`, `user_id`, `risk_type`, `risk_level`, `health_rate`, `pledge_value`, `loan_value`, `risk_message`, `is_resolved`, `created_at`) VALUES
(1, 1, 1, 'HEALTH_RATE_LOW', 'MEDIUM', 0.8500, 42500.00000000, 35000.00000000, '健康度低于预警线，建议补仓', 1, '2024-01-14 10:00:00'),
(2, 2, 2, 'LIQUIDATION_RISK', 'HIGH', 0.8200, 14760.00000000, 18000.00000000, '健康度接近平仓线，风险较高', 1, '2024-01-15 10:00:00');












