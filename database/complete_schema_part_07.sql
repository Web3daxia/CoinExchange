-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第7部分
-- 包含：快讯模块、代理商模块、邀请返佣模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 20. 快讯模块 (News Feed Module)
-- =====================================================

-- 新闻源配置表
CREATE TABLE IF NOT EXISTS `news_sources` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `source_name` VARCHAR(200) NOT NULL COMMENT '新闻源名称，如: CoinDesk, CoinTelegraph',
    `source_code` VARCHAR(50) NOT NULL COMMENT '新闻源代码（唯一标识）',
    `api_url` VARCHAR(500) NOT NULL COMMENT 'API地址',
    `api_key` VARCHAR(200) DEFAULT NULL COMMENT 'API密钥',
    `api_secret` VARCHAR(200) DEFAULT NULL COMMENT 'API密钥（如果需要）',
    `fetch_interval` INT(11) NOT NULL DEFAULT 3600 COMMENT '采集间隔（秒），如: 1800（半小时）, 3600（一小时）',
    `last_fetch_time` DATETIME DEFAULT NULL COMMENT '上次采集时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）, INACTIVE（停用）, ERROR（错误）',
    `priority` INT(11) NOT NULL DEFAULT 0 COMMENT '优先级（数字越大优先级越高）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_code` (`source_code`),
    KEY `idx_status` (`status`),
    KEY `idx_last_fetch_time` (`last_fetch_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻源配置表';

-- 新闻分类表
CREATE TABLE IF NOT EXISTS `news_categories` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称，如: 行业动态, 技术更新, 市场行情',
    `category_code` VARCHAR(50) NOT NULL COMMENT '分类代码（唯一标识）',
    `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父分类ID（支持多级分类）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_code` (`category_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻分类表';

-- 新闻标签表
CREATE TABLE IF NOT EXISTS `news_tags` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `tag_name` VARCHAR(100) NOT NULL COMMENT '标签名称，如: BTC, ETH, DeFi, NFT',
    `tag_code` VARCHAR(50) NOT NULL COMMENT '标签代码（唯一标识）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_code` (`tag_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻标签表';

-- 新闻表
CREATE TABLE IF NOT EXISTS `news` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `source_id` BIGINT(20) NOT NULL COMMENT '新闻源ID',
    `source_article_id` VARCHAR(200) DEFAULT NULL COMMENT '新闻源的文章ID（用于去重）',
    `title` VARCHAR(500) NOT NULL COMMENT '新闻标题',
    `summary` TEXT DEFAULT NULL COMMENT '新闻摘要',
    `content` LONGTEXT DEFAULT NULL COMMENT '新闻内容（完整内容）',
    `author` VARCHAR(200) DEFAULT NULL COMMENT '作者',
    `cover_image_url` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    `original_url` VARCHAR(500) DEFAULT NULL COMMENT '原文链接',
    `publish_time` DATETIME NOT NULL COMMENT '发布时间（新闻源的发布时间）',
    `category_id` BIGINT(20) DEFAULT NULL COMMENT '新闻分类ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）, APPROVED（已审核）, REJECTED（已拒绝）, PUBLISHED（已发布）',
    `is_hot` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否热门: 0否, 1是',
    `is_recommended` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否推荐: 0否, 1是',
    `view_count` INT(11) NOT NULL DEFAULT 0 COMMENT '阅读量',
    `like_count` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `share_count` INT(11) NOT NULL DEFAULT 0 COMMENT '分享数',
    `comment_count` INT(11) NOT NULL DEFAULT 0 COMMENT '评论数',
    `reviewer_id` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
    `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `review_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    `sensitive_words` TEXT DEFAULT NULL COMMENT '检测到的敏感词（JSON格式）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_article` (`source_id`, `source_article_id`),
    KEY `idx_source_id` (`source_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_publish_time` (`publish_time`),
    KEY `idx_is_hot` (`is_hot`),
    KEY `idx_is_recommended` (`is_recommended`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻表';

-- 新闻标签关联表
CREATE TABLE IF NOT EXISTS `news_tag_relations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `news_id` BIGINT(20) NOT NULL COMMENT '新闻ID',
    `tag_id` BIGINT(20) NOT NULL COMMENT '标签ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_news_tag` (`news_id`, `tag_id`),
    KEY `idx_news_id` (`news_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻标签关联表';

-- 用户新闻收藏表
CREATE TABLE IF NOT EXISTS `news_favorites` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `news_id` BIGINT(20) NOT NULL COMMENT '新闻ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_news` (`user_id`, `news_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_news_id` (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户新闻收藏表';

-- 用户新闻互动表（点赞、分享等）
CREATE TABLE IF NOT EXISTS `news_interactions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `news_id` BIGINT(20) NOT NULL COMMENT '新闻ID',
    `interaction_type` VARCHAR(50) NOT NULL COMMENT '互动类型: LIKE（点赞）, SHARE（分享）, VIEW（浏览）',
    `share_platform` VARCHAR(50) DEFAULT NULL COMMENT '分享平台（分享时使用）: WECHAT, TELEGRAM, TWITTER, FACEBOOK',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_news_id` (`news_id`),
    KEY `idx_interaction_type` (`interaction_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户新闻互动表';

-- 新闻翻译表（多语言）
CREATE TABLE IF NOT EXISTS `news_translations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `news_id` BIGINT(20) NOT NULL COMMENT '新闻ID',
    `language_code` VARCHAR(20) NOT NULL COMMENT '语言代码',
    `title` VARCHAR(500) NOT NULL COMMENT '翻译后的标题',
    `summary` TEXT DEFAULT NULL COMMENT '翻译后的摘要',
    `content` LONGTEXT DEFAULT NULL COMMENT '翻译后的内容',
    `translation_service` VARCHAR(50) DEFAULT NULL COMMENT '翻译服务: GOOGLE, AZURE, BAIDU等',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_news_language` (`news_id`, `language_code`),
    KEY `idx_news_id` (`news_id`),
    KEY `idx_language_code` (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻翻译表（多语言）';

-- =====================================================
-- 21. 代理商模块 (Agent Module)
-- =====================================================

-- 代理商表
CREATE TABLE IF NOT EXISTS `agents` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `agent_code` VARCHAR(50) NOT NULL COMMENT '代理商代码（唯一标识）',
    `agent_name` VARCHAR(100) NOT NULL COMMENT '代理商名称',
    `level` VARCHAR(20) NOT NULL DEFAULT 'LEVEL1' COMMENT '代理商等级: LEVEL1（一级）, LEVEL2（二级）, LEVEL3（三级）',
    `parent_agent_id` BIGINT(20) DEFAULT NULL COMMENT '上级代理商ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）',
    `total_users` INT(11) DEFAULT 0 COMMENT '总用户数',
    `active_users` INT(11) DEFAULT 0 COMMENT '活跃用户数',
    `total_volume` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总交易量',
    `total_rebate` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总返佣',
    `settled_rebate` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '已结算返佣',
    `pending_rebate` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '待结算返佣',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_agent_code` (`agent_code`),
    KEY `idx_parent_agent_id` (`parent_agent_id`),
    KEY `idx_status` (`status`),
    KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理商表';

-- 代理商用户关系表
CREATE TABLE IF NOT EXISTS `agent_user_relations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `agent_id` BIGINT(20) NOT NULL COMMENT '代理商ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `relation_type` VARCHAR(20) NOT NULL DEFAULT 'DIRECT' COMMENT '关系类型: DIRECT（直接）, INDIRECT（间接）',
    `level` INT(11) DEFAULT 1 COMMENT '层级（1表示直接，2表示间接）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_agent_user` (`agent_id`, `user_id`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_relation_type` (`relation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理商用户关系表';

-- 代理商返佣配置表
CREATE TABLE IF NOT EXISTS `agent_rebate_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `agent_id` BIGINT(20) NOT NULL COMMENT '代理商ID',
    `rebate_type` VARCHAR(50) NOT NULL COMMENT '返佣类型: SPOT（现货）, FUTURES_USDT（USDT合约）, FUTURES_COIN（币本位合约）, OPTIONS（期权）',
    `rebate_rate` DECIMAL(10,4) NOT NULL COMMENT '返佣比例',
    `min_volume` DECIMAL(20,8) DEFAULT NULL COMMENT '最小交易量要求',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_rebate_type` (`rebate_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理商返佣配置表';

-- 代理商表现数据表
CREATE TABLE IF NOT EXISTS `agent_performances` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `agent_id` BIGINT(20) NOT NULL COMMENT '代理商ID',
    `period_type` VARCHAR(20) NOT NULL COMMENT '周期类型: DAILY（日）、WEEKLY（周）、MONTHLY（月）、YEARLY（年）',
    `period_start` DATETIME NOT NULL COMMENT '周期开始时间',
    `period_end` DATETIME NOT NULL COMMENT '周期结束时间',
    `new_users` INT(11) DEFAULT 0 COMMENT '新增用户数',
    `total_volume` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总交易量',
    `spot_volume` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '现货交易量',
    `futures_usdt_volume` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT 'USDT合约交易量',
    `futures_coin_volume` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '币本位合约交易量',
    `total_rebate` DECIMAL(20,8) DEFAULT 0.00000000 COMMENT '总返佣',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_period_type` (`period_type`),
    KEY `idx_period_start` (`period_start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理商表现数据表';

-- 代理商返佣记录表
CREATE TABLE IF NOT EXISTS `agent_rebate_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `agent_id` BIGINT(20) NOT NULL COMMENT '代理商ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
    `rebate_type` VARCHAR(50) NOT NULL COMMENT '返佣类型',
    `trade_volume` DECIMAL(20,8) NOT NULL COMMENT '交易量',
    `rebate_rate` DECIMAL(10,4) NOT NULL COMMENT '返佣比例',
    `rebate_amount` DECIMAL(20,8) NOT NULL COMMENT '返佣金额',
    `rebate_currency` VARCHAR(20) NOT NULL COMMENT '返佣币种',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待结算）、SETTLED（已结算）、CANCELLED（已取消）',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代理商返佣记录表';

-- =====================================================
-- 22. 邀请返佣模块 (Invite Rebate Module)
-- =====================================================

-- 邀请关系表
CREATE TABLE IF NOT EXISTS `invite_relations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `inviter_id` BIGINT(20) NOT NULL COMMENT '邀请人ID',
    `invitee_id` BIGINT(20) NOT NULL COMMENT '被邀请人ID',
    `invite_code` VARCHAR(50) NOT NULL COMMENT '邀请码',
    `relation_level` INT(11) DEFAULT 1 COMMENT '关系层级（1表示直接邀请，2表示间接邀请）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、INACTIVE（无效）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_invitee_id` (`invitee_id`),
    KEY `idx_inviter_id` (`inviter_id`),
    KEY `idx_invite_code` (`invite_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邀请关系表';

-- 邀请奖励表
CREATE TABLE IF NOT EXISTS `invite_rewards` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `inviter_id` BIGINT(20) NOT NULL COMMENT '邀请人ID',
    `invitee_id` BIGINT(20) NOT NULL COMMENT '被邀请人ID',
    `reward_type` VARCHAR(50) NOT NULL COMMENT '奖励类型: REGISTER（注册奖励）、FIRST_TRADE（首笔交易奖励）、KYC（KYC认证奖励）',
    `reward_currency` VARCHAR(20) NOT NULL COMMENT '奖励币种',
    `reward_amount` DECIMAL(20,8) NOT NULL COMMENT '奖励金额',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待发放）、ISSUED（已发放）、CANCELLED（已取消）',
    `issued_at` DATETIME DEFAULT NULL COMMENT '发放时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_inviter_id` (`inviter_id`),
    KEY `idx_invitee_id` (`invitee_id`),
    KEY `idx_reward_type` (`reward_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邀请奖励表';

-- 返佣记录表
CREATE TABLE IF NOT EXISTS `rebate_records` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `inviter_id` BIGINT(20) NOT NULL COMMENT '邀请人ID',
    `invitee_id` BIGINT(20) NOT NULL COMMENT '被邀请人ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
    `rebate_type` VARCHAR(50) NOT NULL COMMENT '返佣类型: SPOT（现货）, FUTURES_USDT（USDT合约）, FUTURES_COIN（币本位合约）',
    `trade_volume` DECIMAL(20,8) NOT NULL COMMENT '交易量',
    `rebate_rate` DECIMAL(10,4) NOT NULL COMMENT '返佣比例',
    `rebate_amount` DECIMAL(20,8) NOT NULL COMMENT '返佣金额',
    `rebate_currency` VARCHAR(20) NOT NULL COMMENT '返佣币种',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待结算）、SETTLED（已结算）、CANCELLED（已取消）',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_inviter_id` (`inviter_id`),
    KEY `idx_invitee_id` (`invitee_id`),
    KEY `idx_rebate_type` (`rebate_type`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='返佣记录表';

-- 返佣配置表
CREATE TABLE IF NOT EXISTS `rebate_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `rebate_type` VARCHAR(50) NOT NULL COMMENT '返佣类型',
    `rebate_rate` DECIMAL(10,4) NOT NULL COMMENT '返佣比例',
    `min_volume` DECIMAL(20,8) DEFAULT NULL COMMENT '最小交易量要求',
    `max_rebate_per_day` DECIMAL(20,8) DEFAULT NULL COMMENT '每日最大返佣',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_rebate_type` (`rebate_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='返佣配置表';

-- 系统返佣配置表
CREATE TABLE IF NOT EXISTS `system_rebate_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `user_level` VARCHAR(50) DEFAULT NULL COMMENT '用户等级，如 VIP1, VIP2, NORMAL，NULL表示全局默认',
    `spot_rebate_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '现货交易返佣比例',
    `futures_usdt_rebate_rate` DECIMAL(10,4) DEFAULT NULL COMMENT 'USDT本位合约返佣比例',
    `futures_coin_rebate_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '币本位合约返佣比例',
    `copy_trading_rebate_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '跟单返佣比例',
    `options_rebate_rate` DECIMAL(10,4) DEFAULT NULL COMMENT '期权交易返佣比例',
    `max_daily_rebate` DECIMAL(20,8) DEFAULT NULL COMMENT '每日返佣上限',
    `default_rebate_period` VARCHAR(20) NOT NULL DEFAULT 'DAILY' COMMENT '默认返佣周期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_level` (`user_level`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统返佣配置表';

-- 奖励规则表（一次性奖励配置）
CREATE TABLE IF NOT EXISTS `reward_rules` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `reward_type` VARCHAR(50) NOT NULL COMMENT '奖励类型: REGISTER（注册奖励）、KYC（KYC奖励）、FIRST_TRADE（首笔交易奖励）',
    `reward_name` VARCHAR(100) NOT NULL COMMENT '奖励名称',
    `reward_currency` VARCHAR(20) NOT NULL COMMENT '奖励币种',
    `reward_amount` DECIMAL(20,8) NOT NULL COMMENT '奖励金额',
    `condition_type` VARCHAR(50) DEFAULT NULL COMMENT '条件类型',
    `condition_value` VARCHAR(500) DEFAULT NULL COMMENT '条件值（JSON格式）',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_reward_type` (`reward_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='奖励规则表';

-- 返佣结算表
CREATE TABLE IF NOT EXISTS `rebate_settlements` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `settlement_no` VARCHAR(50) NOT NULL COMMENT '结算单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `settlement_type` VARCHAR(50) NOT NULL COMMENT '结算类型: INVITE（邀请返佣）、AGENT（代理商返佣）',
    `period_start` DATETIME NOT NULL COMMENT '结算周期开始',
    `period_end` DATETIME NOT NULL COMMENT '结算周期结束',
    `total_rebate` DECIMAL(20,8) NOT NULL COMMENT '总返佣金额',
    `rebate_currency` VARCHAR(20) NOT NULL COMMENT '返佣币种',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING（待结算）、SETTLED（已结算）、FAILED（结算失败）',
    `settled_at` DATETIME DEFAULT NULL COMMENT '结算时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_settlement_no` (`settlement_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_settlement_type` (`settlement_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='返佣结算表';

-- =====================================================
-- 测试数据插入
-- =====================================================

-- 新闻源配置测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_sources` (`source_name`, `source_code`, `api_url`, `api_key`, `fetch_interval`, `status`, `priority`) VALUES
('CoinDesk', 'COINDESK', 'https://api.coindesk.com/v1', 'test_api_key_001', 3600, 'ACTIVE', 1),
('CoinTelegraph', 'COINTELEGRAPH', 'https://api.cointelegraph.com/v1', 'test_api_key_002', 1800, 'ACTIVE', 2);

-- 新闻分类测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_categories` (`category_name`, `category_code`, `parent_id`, `sort_order`, `status`) VALUES
('行业动态', 'INDUSTRY', NULL, 1, 'ACTIVE'),
('技术更新', 'TECHNOLOGY', NULL, 2, 'ACTIVE'),
('市场行情', 'MARKET', NULL, 3, 'ACTIVE'),
('DeFi', 'DEFI', 1, 1, 'ACTIVE'),
('NFT', 'NFT', 1, 2, 'ACTIVE');

-- 新闻标签测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_tags` (`tag_name`, `tag_code`, `sort_order`, `status`) VALUES
('BTC', 'BTC', 1, 'ACTIVE'),
('ETH', 'ETH', 2, 'ACTIVE'),
('DeFi', 'DEFI', 3, 'ACTIVE'),
('NFT', 'NFT', 4, 'ACTIVE'),
('Layer2', 'LAYER2', 5, 'ACTIVE');

-- 新闻测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news` (`source_id`, `source_article_id`, `title`, `summary`, `content`, `author`, `cover_image_url`, `original_url`, `publish_time`, `category_id`, `status`, `is_hot`, `is_recommended`, `view_count`, `like_count`, `share_count`, `comment_count`) VALUES
(1, 'CD001', '比特币突破45000美元大关', '比特币价格今日突破45000美元，创近期新高', '详细内容：比特币价格在今日早盘突破45000美元大关，市场情绪高涨...', 'John Doe', 'https://example.com/images/btc_news.jpg', 'https://coindesk.com/news/001', '2024-01-15 10:00:00', 3, 'PUBLISHED', 1, 1, 1250, 85, 42, 28),
(2, 'CT001', '以太坊2.0升级进展顺利', '以太坊2.0升级按计划进行，预计下月完成', '详细内容：以太坊2.0升级工作进展顺利，开发团队表示...', 'Jane Smith', 'https://example.com/images/eth_news.jpg', 'https://cointelegraph.com/news/001', '2024-01-15 09:30:00', 2, 'PUBLISHED', 1, 0, 980, 62, 35, 19);

-- 新闻标签关联测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_tag_relations` (`news_id`, `tag_id`) VALUES
(1, 1), (1, 3),
(2, 2), (2, 5);

-- 用户新闻收藏测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_favorites` (`user_id`, `news_id`) VALUES
(1, 1), (1, 2),
(2, 1);

-- 用户新闻互动测试数据
INSERT IGNORE INTO `news_interactions` (`user_id`, `news_id`, `interaction_type`, `share_platform`) VALUES
(1, 1, 'LIKE', NULL),
(1, 1, 'VIEW', NULL),
(1, 1, 'SHARE', 'TWITTER'),
(2, 1, 'LIKE', NULL),
(2, 2, 'VIEW', NULL);

-- 新闻翻译测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `news_translations` (`news_id`, `language_code`, `title`, `summary`, `translation_service`) VALUES
(1, 'zh-CN', '比特币突破45000美元大关', '比特币价格今日突破45000美元，创近期新高', 'GOOGLE'),
(1, 'en-US', 'Bitcoin Breaks Through $45,000', 'Bitcoin price broke through $45,000 today, hitting a recent high', 'GOOGLE'),
(2, 'zh-CN', '以太坊2.0升级进展顺利', '以太坊2.0升级按计划进行，预计下月完成', 'GOOGLE');

-- 代理商测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `agents` (`user_id`, `agent_code`, `agent_name`, `level`, `parent_agent_id`, `status`, `total_users`, `active_users`, `total_volume`, `total_rebate`) VALUES
(1, 'AGENT001', '一级代理商A', 'LEVEL1', NULL, 'APPROVED', 150, 120, 5000000.00, 50000.00),
(2, 'AGENT002', '二级代理商B', 'LEVEL2', 1, 'APPROVED', 80, 65, 2000000.00, 20000.00);

-- 代理商用户关系测试数据（使用 INSERT IGNORE 避免重复插入）
INSERT IGNORE INTO `agent_user_relations` (`agent_id`, `user_id`, `relation_type`, `level`) VALUES
(1, 3, 'DIRECT', 1),
(1, 4, 'DIRECT', 1),
(2, 5, 'DIRECT', 1),
(2, 6, 'INDIRECT', 2);

-- 代理商返佣配置测试数据
INSERT IGNORE INTO `agent_rebate_configs` (`agent_id`, `rebate_type`, `rebate_rate`, `min_volume`, `status`) VALUES
(1, 'SPOT', 0.0010, 1000.00, 'ACTIVE'),
(1, 'FUTURES_USDT', 0.0015, 5000.00, 'ACTIVE'),
(2, 'SPOT', 0.0008, 500.00, 'ACTIVE');

-- 代理商表现数据测试数据
INSERT IGNORE INTO `agent_performances` (`agent_id`, `period_type`, `period_start`, `period_end`, `new_users`, `total_volume`, `spot_volume`, `futures_usdt_volume`, `total_rebate`) VALUES
(1, 'MONTHLY', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 25, 1500000.00, 800000.00, 700000.00, 15000.00),
(2, 'MONTHLY', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 15, 800000.00, 500000.00, 300000.00, 8000.00);

-- 代理商返佣记录测试数据
INSERT IGNORE INTO `agent_rebate_records` (`agent_id`, `user_id`, `order_id`, `rebate_type`, `trade_volume`, `rebate_rate`, `rebate_amount`, `rebate_currency`, `status`) VALUES
(1, 3, 1001, 'SPOT', 10000.00, 0.0010, 10.00, 'USDT', 'SETTLED'),
(1, 4, 1002, 'FUTURES_USDT', 50000.00, 0.0015, 75.00, 'USDT', 'PENDING'),
(2, 5, 1003, 'SPOT', 5000.00, 0.0008, 4.00, 'USDT', 'SETTLED');

-- 邀请关系测试数据（使用 INSERT IGNORE 避免重复插入）
-- 注意：invitee_id 有唯一约束，每个用户只能被一个邀请人邀请
INSERT IGNORE INTO `invite_relations` (`inviter_id`, `invitee_id`, `invite_code`, `relation_level`, `status`) VALUES
(1, 3, 'INVITE001', 1, 'ACTIVE'),
(1, 4, 'INVITE001', 1, 'ACTIVE'),
(3, 5, 'INVITE003', 1, 'ACTIVE'),
(1, 6, 'INVITE001', 1, 'ACTIVE');

-- 邀请奖励测试数据
INSERT IGNORE INTO `invite_rewards` (`inviter_id`, `invitee_id`, `reward_type`, `reward_currency`, `reward_amount`, `status`, `issued_at`) VALUES
(1, 3, 'REGISTER', 'USDT', 10.00, 'ISSUED', '2024-01-10 10:00:00'),
(1, 3, 'FIRST_TRADE', 'USDT', 20.00, 'ISSUED', '2024-01-11 15:30:00'),
(1, 4, 'REGISTER', 'USDT', 10.00, 'PENDING', NULL),
(3, 5, 'REGISTER', 'USDT', 10.00, 'ISSUED', '2024-01-12 09:00:00'),
(1, 6, 'REGISTER', 'USDT', 10.00, 'ISSUED', '2024-01-13 10:00:00');

-- 返佣记录测试数据
INSERT IGNORE INTO `rebate_records` (`inviter_id`, `invitee_id`, `order_id`, `rebate_type`, `trade_volume`, `rebate_rate`, `rebate_amount`, `rebate_currency`, `status`, `settled_at`) VALUES
(1, 3, 2001, 'SPOT', 5000.00, 0.0010, 5.00, 'USDT', 'SETTLED', '2024-01-15 10:00:00'),
(1, 3, 2002, 'FUTURES_USDT', 20000.00, 0.0015, 30.00, 'USDT', 'SETTLED', '2024-01-15 10:00:00'),
(1, 4, 2003, 'SPOT', 3000.00, 0.0010, 3.00, 'USDT', 'PENDING', NULL),
(3, 5, 2004, 'SPOT', 2000.00, 0.0010, 2.00, 'USDT', 'SETTLED', '2024-01-15 11:00:00'),
(1, 6, 2005, 'SPOT', 1500.00, 0.0010, 1.50, 'USDT', 'SETTLED', '2024-01-15 12:00:00');

-- 返佣配置测试数据
INSERT IGNORE INTO `rebate_configs` (`config_name`, `rebate_type`, `rebate_rate`, `min_volume`, `max_rebate_per_day`, `status`) VALUES
('现货返佣配置', 'SPOT', 0.0010, 100.00, 1000.00, 'ACTIVE'),
('USDT合约返佣配置', 'FUTURES_USDT', 0.0015, 500.00, 2000.00, 'ACTIVE'),
('币本位合约返佣配置', 'FUTURES_COIN', 0.0012, 1000.00, 1500.00, 'ACTIVE');

-- 系统返佣配置测试数据
INSERT IGNORE INTO `system_rebate_configs` (`config_name`, `user_level`, `spot_rebate_rate`, `futures_usdt_rebate_rate`, `futures_coin_rebate_rate`, `max_daily_rebate`, `status`) VALUES
('VIP1返佣配置', 'VIP1', 0.0010, 0.0015, 0.0012, 500.00, 'ACTIVE'),
('默认返佣配置', NULL, 0.0008, 0.0012, 0.0010, 300.00, 'ACTIVE');

-- 奖励规则测试数据
INSERT IGNORE INTO `reward_rules` (`reward_type`, `reward_name`, `reward_currency`, `reward_amount`, `condition_type`, `status`) VALUES
('REGISTER', '注册奖励', 'USDT', 10.00, NULL, 'ACTIVE'),
('FIRST_TRADE', '首笔交易奖励', 'USDT', 20.00, 'MIN_AMOUNT:100', 'ACTIVE'),
('KYC', 'KYC认证奖励', 'USDT', 50.00, NULL, 'ACTIVE');

-- 返佣结算测试数据
INSERT IGNORE INTO `rebate_settlements` (`settlement_no`, `user_id`, `settlement_type`, `period_start`, `period_end`, `total_rebate`, `rebate_currency`, `status`, `settled_at`) VALUES
('SETTLE001', 1, 'INVITE', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 38.00, 'USDT', 'SETTLED', '2024-02-01 10:00:00'),
('SETTLE002', 3, 'INVITE', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 2.00, 'USDT', 'SETTLED', '2024-02-01 10:00:00');

