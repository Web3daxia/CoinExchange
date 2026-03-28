-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- 加密货币交易平台完整数据库设计 - 第6部分
-- 包含：系统管理模块、基础信息模块、社区模块
-- =====================================================

USE `coin`;

-- =====================================================
-- 17. 系统管理模块 (System Management Module)
-- =====================================================

-- 系统管理员表
CREATE TABLE IF NOT EXISTS `system_admins` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `username` VARCHAR(100) NOT NULL COMMENT '管理员用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱号码',
    `security_code` VARCHAR(50) DEFAULT NULL COMMENT '安全码（用于二次验证）',
    `enable_google_auth` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用谷歌验证码: 0否, 1是',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）、SUSPENDED（暂停）',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `last_login_device` VARCHAR(200) DEFAULT NULL COMMENT '最后登录设备',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统管理员表';

-- 如果表已存在但缺少字段，添加所有缺失的字段
-- 使用存储过程来检查并添加所有缺失的字段（兼容所有 MySQL 版本）
DELIMITER $$
DROP PROCEDURE IF EXISTS `migrate_system_admins_table`$$
CREATE PROCEDURE `migrate_system_admins_table`()
BEGIN
    DECLARE column_exists INT DEFAULT 0;
    DECLARE role_id_exists INT DEFAULT 0;
    DECLARE avatar_exists INT DEFAULT 0;
    DECLARE username_exists INT DEFAULT 0;
    
    -- 检查基础字段是否存在
    SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'role_id';
    SELECT COUNT(*) INTO avatar_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'avatar';
    SELECT COUNT(*) INTO username_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'username';
    
    -- 检查并添加 role_id 字段（在 id 之后）
    IF role_id_exists = 0 THEN
        ALTER TABLE `system_admins` ADD COLUMN `role_id` BIGINT(20) NOT NULL COMMENT '角色ID' AFTER `id`;
        SET role_id_exists = 1;
    END IF;
    
    -- 检查并添加 avatar 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'avatar';
    IF column_exists = 0 THEN
        IF role_id_exists = 1 THEN
            ALTER TABLE `system_admins` ADD COLUMN `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL' AFTER `role_id`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL';
        END IF;
        SET avatar_exists = 1;
    END IF;
    
    -- 检查并添加 username 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'username';
    IF column_exists = 0 THEN
        IF avatar_exists = 1 THEN
            ALTER TABLE `system_admins` ADD COLUMN `username` VARCHAR(100) NOT NULL COMMENT '管理员用户名' AFTER `avatar`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `username` VARCHAR(100) NOT NULL COMMENT '管理员用户名';
        END IF;
        SET username_exists = 1;
    END IF;
    
    -- 检查并添加 password 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'password';
    IF column_exists = 0 THEN
        IF username_exists = 1 THEN
            ALTER TABLE `system_admins` ADD COLUMN `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）' AFTER `username`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）';
        END IF;
    END IF;
    
    -- 检查并添加 phone 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'phone';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'password';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码' AFTER `password`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码';
        END IF;
    END IF;
    
    -- 检查并添加 email 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'email';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'phone';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱号码' AFTER `phone`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱号码';
        END IF;
    END IF;
    
    -- 检查并添加 security_code 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'security_code';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'email';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `security_code` VARCHAR(50) DEFAULT NULL COMMENT '安全码（用于二次验证）' AFTER `email`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `security_code` VARCHAR(50) DEFAULT NULL COMMENT '安全码（用于二次验证）';
        END IF;
    END IF;
    
    -- 检查并添加 enable_google_auth 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'enable_google_auth';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'security_code';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `enable_google_auth` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用谷歌验证码: 0否, 1是' AFTER `security_code`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `enable_google_auth` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用谷歌验证码: 0否, 1是';
        END IF;
    END IF;
    
    -- 检查并添加 status 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'status';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'enable_google_auth';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）、SUSPENDED（暂停）' AFTER `enable_google_auth`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）、SUSPENDED（暂停）';
        END IF;
    END IF;
    
    -- 检查并添加 last_login_ip 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_ip';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'status';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP' AFTER `status`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP';
        END IF;
    END IF;
    
    -- 检查并添加 last_login_device 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_device';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_ip';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `last_login_device` VARCHAR(200) DEFAULT NULL COMMENT '最后登录设备' AFTER `last_login_ip`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `last_login_device` VARCHAR(200) DEFAULT NULL COMMENT '最后登录设备';
        END IF;
    END IF;
    
    -- 检查并添加 created_at 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'created_at';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_device';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间' AFTER `last_login_device`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间';
        END IF;
    END IF;
    
    -- 检查并添加 last_login_time 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_time';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'created_at';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间' AFTER `created_at`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间';
        END IF;
    END IF;
    
    -- 检查并添加 updated_at 字段
    SELECT COUNT(*) INTO column_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'updated_at';
    IF column_exists = 0 THEN
        SELECT COUNT(*) INTO role_id_exists FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'system_admins' AND COLUMN_NAME = 'last_login_time';
        IF role_id_exists > 0 THEN
            ALTER TABLE `system_admins` ADD COLUMN `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `last_login_time`;
        ELSE
            ALTER TABLE `system_admins` ADD COLUMN `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';
        END IF;
    END IF;
    
END$$
DELIMITER ;

CALL `migrate_system_admins_table`();
DROP PROCEDURE IF EXISTS `migrate_system_admins_table`;

-- 系统角色表
CREATE TABLE IF NOT EXISTS `system_roles` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色代码: ROOT, ADMIN, OPERATOR, VIEWER等',
    `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '角色描述',
    `level` INT(11) NOT NULL COMMENT '角色级别，数字越小权限越高，ROOT=0, ADMIN=1',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 系统权限表
CREATE TABLE IF NOT EXISTS `system_permissions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限代码，如: user:view, currency:edit',
    `permission_name` VARCHAR(200) NOT NULL COMMENT '权限名称',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '模块名称：USER, CURRENCY, REGION, SYSTEM等',
    `action` VARCHAR(50) DEFAULT NULL COMMENT '操作：VIEW, CREATE, EDIT, DELETE, MANAGE',
    `resource` VARCHAR(100) DEFAULT NULL COMMENT '资源：user, currency, region等',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '权限描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permissions` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT(20) NOT NULL COMMENT '权限ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 币种表
CREATE TABLE IF NOT EXISTS `currencies` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `currency_code` VARCHAR(20) NOT NULL COMMENT '币种代码: BTC, ETH, USDT等',
    `currency_name` VARCHAR(100) NOT NULL COMMENT '币种名称',
    `symbol` VARCHAR(10) DEFAULT NULL COMMENT '符号，如: ₿, Ξ',
    `icon_url` VARCHAR(500) DEFAULT NULL COMMENT '图标URL',
    `logo_url` VARCHAR(500) DEFAULT NULL COMMENT '币种logo URL',
    `logo_file_path` VARCHAR(500) DEFAULT NULL COMMENT '币种logo文件路径',
    `decimals` INT(11) NOT NULL DEFAULT 8 COMMENT '小数位数',
    `currency_unit` VARCHAR(20) DEFAULT NULL COMMENT '币种单位',
    `min_withdraw_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最小提现金额',
    `max_withdraw_amount` DECIMAL(20,8) DEFAULT NULL COMMENT '最大提现金额',
    `withdraw_fee` DECIMAL(20,8) DEFAULT NULL COMMENT '提现手续费',
    `deposit_enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用充值',
    `withdraw_enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用提现',
    `spot_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '现货交易区是否启用',
    `futures_usdt_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'USDT本位合约是否启用',
    `futures_coin_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '币本位合约是否启用',
    `options_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '期权交易是否启用',
    `leveraged_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '杠杆交易是否启用',
    `total_supply` DECIMAL(30,8) DEFAULT NULL COMMENT '总数量（总供应量）',
    `base_exchange_rate_cny` DECIMAL(20,8) DEFAULT NULL COMMENT '基础汇率（相对于人民币CNY）',
    `base_exchange_rate_usd` DECIMAL(20,8) DEFAULT NULL COMMENT '基础汇率（相对于美元USD）',
    `listing_date` DATE DEFAULT NULL COMMENT '上线日期',
    `detail_url` VARCHAR(500) DEFAULT NULL COMMENT '详情链接',
    `intro` TEXT DEFAULT NULL COMMENT '币种简介',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '币种描述',
    `agent_id` BIGINT(20) DEFAULT NULL COMMENT '币种所属代理商ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_currency_code` (`currency_code`),
    KEY `idx_status` (`status`),
    KEY `idx_agent_id` (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币种表';

-- 币种分类表
CREATE TABLE IF NOT EXISTS `currency_categories` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `category_code` VARCHAR(50) NOT NULL COMMENT '分类代码',
    `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父分类ID',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_code` (`category_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币种分类表';

-- 币种分类关联表
CREATE TABLE IF NOT EXISTS `currency_category_relations` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `currency_id` BIGINT(20) NOT NULL COMMENT '币种ID',
    `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_currency_category` (`currency_id`, `category_id`),
    KEY `idx_currency_id` (`currency_id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币种分类关联表';

-- 币种名称表（多语言）
CREATE TABLE IF NOT EXISTS `currency_names` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `currency_id` BIGINT(20) NOT NULL COMMENT '币种ID',
    `language_code` VARCHAR(20) NOT NULL COMMENT '语言代码',
    `currency_name` VARCHAR(100) NOT NULL COMMENT '币种名称（多语言）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_currency_language` (`currency_id`, `language_code`),
    KEY `idx_currency_id` (`currency_id`),
    KEY `idx_language_code` (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='币种名称表（多语言）';

-- 汇率表
CREATE TABLE IF NOT EXISTS `currency_exchange_rates` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `from_currency` VARCHAR(20) NOT NULL COMMENT '源货币，如 BTC, ETH, USDT',
    `to_currency` VARCHAR(20) NOT NULL COMMENT '目标货币，如 USD, EUR, CNY',
    `rate` DECIMAL(20,8) NOT NULL COMMENT '汇率',
    `source` VARCHAR(50) NOT NULL COMMENT '数据源，如 BINANCE, COINBASE, MANUAL',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、INACTIVE（无效）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_from_to_currency` (`from_currency`, `to_currency`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='汇率表';

-- 系统消息表（多语言）
CREATE TABLE IF NOT EXISTS `system_messages` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `message_key` VARCHAR(200) NOT NULL COMMENT '消息键，如: error.currency.not_found',
    `language_code` VARCHAR(20) NOT NULL COMMENT '语言代码，如: zh-CN, en-US',
    `message_value` TEXT NOT NULL COMMENT '消息值（翻译内容）',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '模块名称',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_key_language` (`message_key`, `language_code`),
    KEY `idx_language_code` (`language_code`),
    KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息表（多语言）';

-- 翻译服务配置表
CREATE TABLE IF NOT EXISTS `translation_service_configs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `service_name` VARCHAR(100) NOT NULL COMMENT '服务名称',
    `service_type` VARCHAR(50) NOT NULL COMMENT '服务类型: GOOGLE, AZURE, BAIDU, YOUDAO, DEEPL, OTHER',
    `service_code` VARCHAR(50) NOT NULL COMMENT '服务代码（唯一标识）',
    `is_enabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
    `api_url` VARCHAR(500) DEFAULT NULL COMMENT 'API地址',
    `api_key` VARCHAR(500) DEFAULT NULL COMMENT 'API密钥',
    `api_secret` VARCHAR(500) DEFAULT NULL COMMENT 'API密钥（部分服务需要）',
    `app_id` VARCHAR(200) DEFAULT NULL COMMENT '应用ID（部分服务需要，如百度）',
    `region` VARCHAR(100) DEFAULT NULL COMMENT '区域（部分服务需要，如Azure）',
    `source_language` VARCHAR(20) DEFAULT 'en-US' COMMENT '源语言（默认英文）',
    `target_languages` TEXT DEFAULT NULL COMMENT '目标语言列表（JSON格式）',
    `config_params` TEXT DEFAULT NULL COMMENT '其他配置参数（JSON格式）',
    `priority` INT(11) NOT NULL DEFAULT 0 COMMENT '优先级',
    `daily_limit` INT(11) DEFAULT NULL COMMENT '每日翻译字符数限制',
    `current_daily_usage` BIGINT(20) DEFAULT 0 COMMENT '当日已使用字符数',
    `last_usage_reset_time` DATETIME DEFAULT NULL COMMENT '上次使用量重置时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（激活）、INACTIVE（禁用）、ERROR（错误）',
    `error_message` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_service_code` (`service_code`),
    KEY `idx_service_type` (`service_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='翻译服务配置表';

-- 系统日志表
CREATE TABLE IF NOT EXISTS `system_logs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `admin_id` BIGINT(20) DEFAULT NULL COMMENT '管理员ID',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `operation_desc` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
    `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(20) DEFAULT NULL COMMENT '请求方法',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数（JSON格式）',
    `response_data` TEXT DEFAULT NULL COMMENT '响应数据（JSON格式）',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `execution_time` BIGINT(20) DEFAULT NULL COMMENT '执行时间（毫秒）',
    `status` VARCHAR(20) DEFAULT NULL COMMENT '状态: SUCCESS（成功）、FAILED（失败）',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- =====================================================
-- 18. 基础信息模块 (Basic Info Module)
-- =====================================================

-- 汇率表（与系统管理模块的currency_exchange_rates表相同，这里保留exchange_rates作为别名）
CREATE TABLE IF NOT EXISTS `exchange_rates` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `from_currency` VARCHAR(20) NOT NULL COMMENT '源货币，如 BTC, ETH, USDT',
    `to_currency` VARCHAR(20) NOT NULL COMMENT '目标货币，如 USD, EUR, CNY',
    `rate` DECIMAL(20,8) NOT NULL COMMENT '汇率',
    `source` VARCHAR(50) NOT NULL COMMENT '数据源，如 BINANCE, COINBASE, MANUAL',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、INACTIVE（无效）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_from_to_currency` (`from_currency`, `to_currency`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='汇率表';

-- 语言包表
CREATE TABLE IF NOT EXISTS `language_packs` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `language_code` VARCHAR(20) NOT NULL COMMENT '语言代码，如 en, zh-CN, fr, es, de, ja, ko, ar',
    `language_name` VARCHAR(100) NOT NULL COMMENT '语言名称，如 English, 中文, Français',
    `native_name` VARCHAR(100) DEFAULT NULL COMMENT '本地名称，如 English, 中文, Français',
    `country_code` VARCHAR(10) DEFAULT NULL COMMENT '国家代码，如 US, CN, FR',
    `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认语言',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）',
    `translation_data` LONGTEXT DEFAULT NULL COMMENT '翻译数据（JSON格式）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_language_code` (`language_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='语言包表';

-- API端点配置表（行情线路）
CREATE TABLE IF NOT EXISTS `api_endpoints` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `endpoint_name` VARCHAR(100) NOT NULL COMMENT '端点名称，如 主线路、备用线路1、备用线路2',
    `endpoint_type` VARCHAR(50) NOT NULL COMMENT '端点类型，如 MARKET_DATA（行情数据）、TRADING（交易）、WEBSOCKET（WebSocket）',
    `base_url` VARCHAR(255) NOT NULL COMMENT '基础URL',
    `region` VARCHAR(50) DEFAULT NULL COMMENT '区域，如 ASIA, EUROPE, AMERICA',
    `priority` INT(11) NOT NULL DEFAULT 0 COMMENT '优先级（数字越小优先级越高）',
    `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认线路',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（启用）、INACTIVE（禁用）、MAINTENANCE（维护中）',
    `response_time` BIGINT(20) DEFAULT NULL COMMENT '响应时间（毫秒）',
    `last_check_time` DATETIME DEFAULT NULL COMMENT '最后检查时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_endpoint_type` (`endpoint_type`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API端点配置表';

-- 用户偏好设置表
CREATE TABLE IF NOT EXISTS `user_preferences` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `language_code` VARCHAR(20) NOT NULL DEFAULT 'en' COMMENT '用户选择的语言代码',
    `currency` VARCHAR(20) NOT NULL DEFAULT 'USD' COMMENT '用户选择的法币，如 USD, EUR, CNY',
    `api_endpoint_id` BIGINT(20) DEFAULT NULL COMMENT '用户选择的API端点ID',
    `timezone` VARCHAR(50) DEFAULT NULL COMMENT '时区',
    `date_format` VARCHAR(50) DEFAULT NULL COMMENT '日期格式',
    `number_format` VARCHAR(50) DEFAULT NULL COMMENT '数字格式',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_language_code` (`language_code`),
    KEY `idx_currency` (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好设置表';

-- 通知表（站内信）
CREATE TABLE IF NOT EXISTS `notifications` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `type` VARCHAR(50) NOT NULL COMMENT '通知类型，如 SYSTEM（系统通知）、TRADE（交易提醒）、MARKET（市场波动）、ACTIVITY（平台活动）',
    `title` VARCHAR(255) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
    `is_email_sent` TINYINT(1) DEFAULT 0 COMMENT '是否已发送邮件',
    `is_push_sent` TINYINT(1) DEFAULT 0 COMMENT '是否已推送',
    `link_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- =====================================================
-- 19. 社区模块 (Community Module)
-- =====================================================

-- 社区内容表
CREATE TABLE IF NOT EXISTS `community_contents` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '发布者ID',
    `content_type` VARCHAR(50) NOT NULL COMMENT '内容类型: ARTICLE（文章）、IMAGE（图片）、VIDEO（视频）',
    `title` VARCHAR(255) DEFAULT NULL COMMENT '标题',
    `content` LONGTEXT DEFAULT NULL COMMENT '内容（富文本）',
    `image_urls` TEXT DEFAULT NULL COMMENT '图片URLs（JSON数组）',
    `video_url` VARCHAR(500) DEFAULT NULL COMMENT '视频URL',
    `category` VARCHAR(100) DEFAULT NULL COMMENT '分类',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（逗号分隔）',
    `like_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `comment_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '评论数',
    `share_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '分享数',
    `reward_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '赞赏数',
    `view_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '浏览量',
    `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED' COMMENT '状态: DRAFT（草稿）、PUBLISHED（已发布）、DELETED（已删除）、HIDDEN（已隐藏）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content_type` (`content_type`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区内容表';

-- 社区评论表
CREATE TABLE IF NOT EXISTS `community_comments` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父评论ID',
    `comment_text` TEXT NOT NULL COMMENT '评论内容',
    `like_count` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE（有效）、DELETED（已删除）、HIDDEN（已隐藏）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_content_id` (`content_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区评论表';

-- 社区点赞表
CREATE TABLE IF NOT EXISTS `community_likes` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_content_user` (`content_id`, `user_id`),
    KEY `idx_content_id` (`content_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区点赞表';

-- 社区关注表
CREATE TABLE IF NOT EXISTS `community_follows` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `follower_id` BIGINT(20) NOT NULL COMMENT '关注者ID',
    `following_id` BIGINT(20) NOT NULL COMMENT '被关注者ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`),
    KEY `idx_follower_id` (`follower_id`),
    KEY `idx_following_id` (`following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区关注表';

-- 社区赞赏表
CREATE TABLE IF NOT EXISTS `community_rewards` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
    `from_user_id` BIGINT(20) NOT NULL COMMENT '赞赏者ID',
    `to_user_id` BIGINT(20) NOT NULL COMMENT '被赞赏者ID',
    `reward_currency` VARCHAR(20) NOT NULL COMMENT '奖励币种',
    `reward_amount` DECIMAL(20,8) NOT NULL COMMENT '奖励金额',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_content_id` (`content_id`),
    KEY `idx_from_user_id` (`from_user_id`),
    KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社区赞赏表';

-- =====================================================
-- 测试数据插入
-- =====================================================

-- 系统管理员测试数据（使用 INSERT IGNORE 避免重复插入）
-- 密码使用 BCrypt 加密，admin 用户初始密码为 "123456"
-- 生成BCrypt密码的Java代码: BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); String hash = encoder.encode("123456");
-- 注意：密码字段不允许为空，必须使用 BCrypt 加密后的哈希值
-- 以下 BCrypt 哈希值对应密码 "123456"（60个字符的标准BCrypt格式）
-- 由于 BCrypt 每次生成的哈希值都不同（包含随机盐），但都能正确验证相同的密码
-- 如果需要重新生成，请运行: BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); System.out.println(encoder.encode("123456"));
INSERT IGNORE INTO `system_admins` (`role_id`, `avatar`, `username`, `password`, `phone`, `email`, `security_code`, `enable_google_auth`, `status`, `last_login_ip`, `last_login_device`, `created_at`, `last_login_time`) VALUES
(1, 'https://example.com/avatars/admin.jpg', 'admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '13800138000', 'admin@example.com', '123456', 1, 'ACTIVE', '192.168.1.100', 'Chrome/Windows 10', '2024-01-15 10:00:00', '2024-01-15 10:30:00'),
(2, 'https://example.com/avatars/operator1.jpg', 'operator1', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '13800138001', 'operator1@example.com', '654321', 0, 'ACTIVE', '192.168.1.101', 'Firefox/Windows 10', '2024-01-15 09:00:00', '2024-01-15 09:15:00');

-- 如果 admin 用户已存在但密码为空或为空字符串，更新密码为 123456 的 BCrypt 哈希
-- 以下哈希值对应密码 "123456": $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW
UPDATE `system_admins` SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE `username` = 'admin' AND (`password` IS NULL OR `password` = '' OR LENGTH(TRIM(`password`)) = 0);

-- 系统角色测试数据
INSERT IGNORE INTO `system_roles` (`role_code`, `role_name`, `description`, `level`, `status`) VALUES
('ROOT', '超级管理员', '拥有所有权限的超级管理员', 0, 'ACTIVE'),
('ADMIN', '管理员', '拥有大部分管理权限', 1, 'ACTIVE'),
('OPERATOR', '运营人员', '拥有日常运营权限', 2, 'ACTIVE'),
('VIEWER', '查看者', '仅拥有查看权限', 3, 'ACTIVE');

-- 系统权限测试数据
INSERT IGNORE INTO `system_permissions` (`permission_code`, `permission_name`, `module`, `action`, `resource`, `description`) VALUES
('user:view', '查看用户', 'USER', 'VIEW', 'user', '查看用户信息'),
('user:create', '创建用户', 'USER', 'CREATE', 'user', '创建新用户'),
('user:edit', '编辑用户', 'USER', 'EDIT', 'user', '编辑用户信息'),
('user:delete', '删除用户', 'USER', 'DELETE', 'user', '删除用户'),
('currency:view', '查看币种', 'CURRENCY', 'VIEW', 'currency', '查看币种信息'),
('currency:edit', '编辑币种', 'CURRENCY', 'EDIT', 'currency', '编辑币种信息'),
('system:manage', '系统管理', 'SYSTEM', 'MANAGE', 'system', '系统管理权限');

-- 角色权限关联测试数据
INSERT IGNORE INTO `role_permissions` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
(2, 1), (2, 2), (2, 3), (2, 5), (2, 6),
(3, 1), (3, 5),
(4, 1), (4, 5);

-- 币种测试数据
INSERT IGNORE INTO `currencies` (`currency_code`, `currency_name`, `symbol`, `icon_url`, `decimals`, `min_withdraw_amount`, `max_withdraw_amount`, `withdraw_fee`, `deposit_enabled`, `withdraw_enabled`, `spot_enabled`, `futures_usdt_enabled`, `futures_coin_enabled`, `status`, `sort_order`) VALUES
('BTC', 'Bitcoin', '₿', 'https://example.com/icons/btc.png', 8, 0.001, 100.0, 0.0005, 1, 1, 1, 1, 1, 'ACTIVE', 1),
('ETH', 'Ethereum', 'Ξ', 'https://example.com/icons/eth.png', 8, 0.01, 1000.0, 0.01, 1, 1, 1, 1, 1, 'ACTIVE', 2),
('USDT', 'Tether', '₮', 'https://example.com/icons/usdt.png', 8, 10.0, 100000.0, 1.0, 1, 1, 1, 1, 0, 'ACTIVE', 3);

-- 币种分类测试数据
INSERT IGNORE INTO `currency_categories` (`category_code`, `category_name`, `parent_id`, `sort_order`, `status`) VALUES
('MAJOR', '主流币', NULL, 1, 'ACTIVE'),
('DEFI', 'DeFi代币', NULL, 2, 'ACTIVE'),
('LAYER1', 'Layer1公链', 1, 1, 'ACTIVE'),
('STABLE', '稳定币', 1, 2, 'ACTIVE');

-- 币种分类关联测试数据
INSERT IGNORE INTO `currency_category_relations` (`currency_id`, `category_id`) VALUES
(1, 1), (1, 3),
(2, 1), (2, 3),
(3, 1), (3, 4);

-- 币种名称测试数据（多语言）
INSERT IGNORE INTO `currency_names` (`currency_id`, `language_code`, `currency_name`) VALUES
(1, 'zh-CN', '比特币'),
(1, 'en-US', 'Bitcoin'),
(2, 'zh-CN', '以太坊'),
(2, 'en-US', 'Ethereum'),
(3, 'zh-CN', '泰达币'),
(3, 'en-US', 'Tether');

-- 汇率测试数据
INSERT IGNORE INTO `currency_exchange_rates` (`from_currency`, `to_currency`, `rate`, `source`, `status`) VALUES
('BTC', 'USD', 45000.00, 'BINANCE', 'ACTIVE'),
('ETH', 'USD', 2800.00, 'BINANCE', 'ACTIVE'),
('USDT', 'USD', 1.00, 'MANUAL', 'ACTIVE'),
('BTC', 'CNY', 315000.00, 'BINANCE', 'ACTIVE'),
('ETH', 'CNY', 19600.00, 'BINANCE', 'ACTIVE');

-- 系统消息测试数据（多语言）
INSERT IGNORE INTO `system_messages` (`message_key`, `language_code`, `message_value`, `module`) VALUES
('error.currency.not_found', 'zh-CN', '币种不存在', 'CURRENCY'),
('error.currency.not_found', 'en-US', 'Currency not found', 'CURRENCY'),
('success.operation', 'zh-CN', '操作成功', 'SYSTEM'),
('success.operation', 'en-US', 'Operation successful', 'SYSTEM');

-- 翻译服务配置测试数据
INSERT IGNORE INTO `translation_service_configs` (`service_name`, `service_type`, `service_code`, `is_enabled`, `api_key`, `priority`, `status`) VALUES
('Google翻译', 'GOOGLE', 'GOOGLE_001', 1, 'test_api_key_google', 1, 'ACTIVE'),
('百度翻译', 'BAIDU', 'BAIDU_001', 0, 'test_api_key_baidu', 2, 'INACTIVE');

-- 系统日志测试数据（日志数据不需要 IGNORE，可以重复记录）
INSERT INTO `system_logs` (`admin_id`, `user_id`, `operation_type`, `operation_desc`, `ip_address`, `status`) VALUES
(1, NULL, 'LOGIN', '管理员登录', '192.168.1.100', 'SUCCESS'),
(1, 1, 'USER_EDIT', '编辑用户信息', '192.168.1.100', 'SUCCESS');

-- 汇率表测试数据（exchange_rates）
INSERT IGNORE INTO `exchange_rates` (`from_currency`, `to_currency`, `rate`, `source`, `status`) VALUES
('BTC', 'USD', 45000.00, 'BINANCE', 'ACTIVE'),
('ETH', 'USD', 2800.00, 'BINANCE', 'ACTIVE');

-- 语言包测试数据
INSERT IGNORE INTO `language_packs` (`language_code`, `language_name`, `native_name`, `country_code`, `is_default`, `status`) VALUES
('en', 'English', 'English', 'US', 1, 'ACTIVE'),
('zh-CN', '简体中文', '简体中文', 'CN', 0, 'ACTIVE'),
('zh-TW', '繁體中文', '繁體中文', 'TW', 0, 'ACTIVE'),
('ja', 'Japanese', '日本語', 'JP', 0, 'ACTIVE');

-- API端点配置测试数据
INSERT IGNORE INTO `api_endpoints` (`endpoint_name`, `endpoint_type`, `base_url`, `region`, `priority`, `is_default`, `status`) VALUES
('主线路', 'MARKET_DATA', 'https://api.example.com/v1', 'ASIA', 1, 1, 'ACTIVE'),
('备用线路1', 'MARKET_DATA', 'https://api-backup1.example.com/v1', 'ASIA', 2, 0, 'ACTIVE'),
('交易主线路', 'TRADING', 'https://trade.example.com/v1', 'ASIA', 1, 1, 'ACTIVE');

-- 用户偏好设置测试数据
INSERT IGNORE INTO `user_preferences` (`user_id`, `language_code`, `currency`, `api_endpoint_id`, `timezone`, `date_format`, `number_format`) VALUES
(1, 'zh-CN', 'CNY', 1, 'Asia/Shanghai', 'YYYY-MM-DD', '0,0.00'),
(2, 'en', 'USD', 1, 'America/New_York', 'MM/DD/YYYY', '0,0.00');

-- 通知测试数据（通知数据不需要 IGNORE，可以重复记录）
INSERT INTO `notifications` (`user_id`, `type`, `title`, `content`, `is_read`, `is_email_sent`, `is_push_sent`) VALUES
(1, 'SYSTEM', '系统维护通知', '系统将于今晚22:00进行维护，预计持续2小时', 0, 1, 1),
(1, 'TRADE', '订单成交提醒', '您的BTC/USDT买单已成交', 1, 0, 1),
(2, 'MARKET', '价格波动提醒', 'BTC价格24小时上涨超过5%', 0, 0, 0);

-- 社区内容测试数据（社区内容不需要 IGNORE，可以重复记录）
INSERT INTO `community_contents` (`user_id`, `content_type`, `title`, `content`, `category`, `tags`, `like_count`, `comment_count`, `view_count`, `status`) VALUES
(1, 'ARTICLE', '比特币投资指南', '这是一篇关于比特币投资的详细指南...', '投资', 'BTC,投资,指南', 25, 8, 150, 'PUBLISHED'),
(2, 'IMAGE', '以太坊2.0升级进展', '以太坊2.0升级的最新进展和影响分析...', '技术', 'ETH,升级', 42, 15, 320, 'PUBLISHED');

-- 社区评论测试数据（社区评论不需要 IGNORE，可以重复记录）
INSERT INTO `community_comments` (`content_id`, `user_id`, `parent_id`, `comment_text`, `like_count`, `status`) VALUES
(1, 2, NULL, '非常详细的指南，感谢分享！', 5, 'ACTIVE'),
(1, 3, NULL, '对我很有帮助，期待更多内容', 3, 'ACTIVE'),
(1, 4, 1, '同意，内容很实用', 2, 'ACTIVE'),
(2, 1, NULL, '升级进展确实值得关注', 8, 'ACTIVE');

-- 社区点赞测试数据
INSERT IGNORE INTO `community_likes` (`content_id`, `user_id`) VALUES
(1, 2), (1, 3), (1, 4),
(2, 1), (2, 3), (2, 5);

-- 社区关注测试数据
INSERT IGNORE INTO `community_follows` (`follower_id`, `following_id`) VALUES
(2, 1), (3, 1), (4, 1),
(1, 2), (3, 2);

-- 社区赞赏测试数据（社区赞赏不需要 IGNORE，可以重复记录）
INSERT INTO `community_rewards` (`content_id`, `from_user_id`, `to_user_id`, `reward_currency`, `reward_amount`) VALUES
(1, 2, 1, 'USDT', 10.00),
(1, 3, 1, 'USDT', 5.00),
(2, 1, 2, 'USDT', 20.00);

