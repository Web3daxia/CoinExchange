# 系统管理模块 (System Management Module)

## 模块说明

系统管理模块提供系统级别的管理功能，包括：

1. **角色权限管理**: 管理系统角色和权限，支持角色-权限关联
2. **管理员管理**: 管理后台管理员账户，分配角色和权限
3. **币种管理**: 管理平台支持的币种，控制币种在各个交易区的启用状态
4. **地区管理**: 管理地区访问限制，控制哪些国家/地区可以访问API
5. **多语言支持**: 管理系统的多语言消息，支持16种语言

## 主要功能

### 1. 角色权限管理

- **角色管理**: 创建、编辑、删除系统角色
- **权限管理**: 定义系统权限，包括模块、操作、资源
- **角色权限分配**: 为角色分配权限，支持批量分配

**角色级别**:
- ROOT (级别0): 超级管理员，拥有所有权限
- ADMIN (级别1): 管理员，拥有系统管理权限
- OPERATOR (级别2): 操作员，拥有日常操作权限
- VIEWER (级别3): 查看者，只能查看数据

### 2. 管理员管理

- **管理员账户管理**: 创建、编辑、删除管理员
- **角色分配**: 为管理员分配角色
- **权限检查**: 检查管理员是否拥有指定权限
- **特殊保护**: ROOT管理员不能删除

### 3. 币种管理

- **币种CRUD**: 添加、编辑、删除、查询币种
- **交易区控制**: 控制币种在以下交易区的启用状态：
  - 现货交易 (spot_enabled)
  - USDT本位合约 (futures_usdt_enabled)
  - 币本位合约 (futures_coin_enabled)
  - 期权交易 (options_enabled)
  - 杠杆交易 (leveraged_enabled)
- **币种配置**: 配置币种的小数位数、提现限制、手续费等

### 4. 地区管理

- **地区配置**: 添加、编辑、删除地区配置
- **访问控制**: 控制API访问和前端访问权限
- **限制消息**: 支持多语言的限制提示信息
- **访问检查**: 检查指定国家代码的访问权限

### 5. 多语言支持

- **消息管理**: 创建、编辑、删除多语言消息
- **语言包查询**: 查询指定语言的所有消息
- **支持语言**: 支持16种语言（详见API文档）

## 数据库结构

### 主要表

1. `system_roles` - 系统角色表
2. `system_permissions` - 系统权限表
3. `role_permissions` - 角色权限关联表
4. `system_admins` - 系统管理员表
5. `currencies` - 币种表
6. `regions` - 地区管理表
7. `system_messages` - 系统消息表（多语言）

### 数据库脚本

- 表结构: `database/system_management_schema.sql`
- 测试数据: `database/system_management_test_data.sql`

## API接口

详细API文档请参考: `API文档.md`

### 主要接口

- `/admin/system/role/**` - 角色管理接口
- `/admin/system/admin/**` - 管理员管理接口
- `/admin/system/currency/**` - 币种管理接口
- `/admin/system/region/**` - 地区管理接口
- `/admin/system/message/**` - 消息管理接口

## 使用说明

### 1. 初始化数据库

```sql
-- 执行表结构脚本
source database/system_management_schema.sql;

-- 执行测试数据脚本（可选）
source database/system_management_test_data.sql;
```

### 2. 配置模块

在父pom.xml中添加模块依赖：

```xml
<modules>
    ...
    <module>system-management</module>
</modules>
```

### 3. 使用多语言

系统消息支持16种语言，默认返回中文。可以通过`SystemMessageService`获取多语言消息：

```java
@Autowired
private SystemMessageService systemMessageService;

String message = systemMessageService.getMessageValue("error.currency.not_found", "en-US");
```

### 4. 权限检查

在需要权限验证的地方，可以使用`SystemAdminService`检查权限：

```java
@Autowired
private SystemAdminService systemAdminService;

boolean hasPermission = systemAdminService.hasPermission(adminId, "currency:edit");
```

### 5. 地区限制检查

在API接口中检查地区访问权限：

```java
@Autowired
private RegionService regionService;

if (!regionService.isApiAccessAllowed(countryCode)) {
    String message = regionService.getBlockMessage(countryCode, languageCode);
    return Result.error(message);
}
```

## 测试数据说明

测试数据SQL脚本包含了：

1. **角色数据**: ROOT, ADMIN, OPERATOR, VIEWER
2. **权限数据**: 用户、币种、地区、系统管理等权限
3. **角色权限关联**: 各角色的权限分配
4. **管理员数据**: 4个测试管理员账户
5. **币种数据**: 10个常见币种（BTC, ETH, USDT等）
6. **地区数据**: 10个国家和地区配置
7. **消息数据**: 16种语言的常用消息

## 注意事项

1. **ROOT权限**: ROOT角色拥有所有权限，无需在数据库中存储权限关联
2. **币种启用**: 币种需要在对应的交易区启用后，才能在相应区域使用
3. **地区限制**: 被限制的地区访问API时会返回对应的多语言提示信息
4. **多语言默认**: 系统默认使用中文（zh-CN），如果找不到指定语言的消息，会尝试返回中文或英文
5. **管理员关联**: 管理员必须关联已存在的用户（users表）

## 扩展说明

### 添加新权限

1. 在`system_permissions`表中添加权限记录
2. 通过角色权限管理接口为角色分配权限
3. 在代码中使用权限代码进行权限检查

### 添加新语言

1. 在`system_messages`表中添加对应语言的消息
2. 更新地区限制消息的多语言JSON
3. 确保语言代码符合ISO 639-1标准

### 自定义角色

1. 通过角色管理接口创建新角色
2. 设置合适的角色级别（数字越小权限越高）
3. 为角色分配相应的权限














