# 系统管理员 API 文档

## 概述

系统管理员模块提供了完整的系统管理员管理功能，包括创建、更新、删除、查询、登录、密码管理等功能。

## 数据库表结构

### system_admins 表

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | ID | PRIMARY KEY, AUTO_INCREMENT |
| role_id | BIGINT | 角色ID | NOT NULL |
| avatar | VARCHAR(500) | 头像URL | NULL |
| username | VARCHAR(100) | 用户名 | NOT NULL, UNIQUE |
| password | VARCHAR(255) | 密码（BCrypt加密） | NOT NULL |
| phone | VARCHAR(20) | 手机号码 | NULL, UNIQUE |
| email | VARCHAR(100) | 邮箱号码 | NULL, UNIQUE |
| security_code | VARCHAR(50) | 安全码 | NULL |
| enable_google_auth | TINYINT(1) | 是否启用谷歌验证码 | NOT NULL, DEFAULT 0 |
| status | VARCHAR(20) | 状态 | NOT NULL, DEFAULT 'ACTIVE' |
| last_login_ip | VARCHAR(50) | 最后登录IP | NULL |
| last_login_device | VARCHAR(200) | 最后登录设备 | NULL |
| created_at | DATETIME | 添加时间 | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| last_login_time | DATETIME | 最后登录时间 | NULL |
| updated_at | DATETIME | 更新时间 | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

## API 接口

### 1. 管理员登录

**接口地址：** `POST /admin/system/admin/login`

**请求参数：**
```json
{
  "username": "admin",
  "password": "Admin123!",
  "googleAuthCode": "123456",
  "securityCode": "123456",
  "loginIp": "192.168.1.100",
  "loginDevice": "Chrome/Windows 10"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "admin": {
      "id": 1,
      "roleId": 1,
      "roleName": "超级管理员",
      "avatar": "https://example.com/avatars/admin.jpg",
      "username": "admin",
      "phone": "13800138000",
      "email": "admin@example.com",
      "enableGoogleAuth": true,
      "status": "ACTIVE",
      "lastLoginIp": "192.168.1.100",
      "lastLoginDevice": "Chrome/Windows 10",
      "createdAt": "2024-01-15T10:00:00",
      "lastLoginTime": "2024-01-15T10:30:00"
    },
    "accessToken": "access_token_1_1234567890",
    "refreshToken": "refresh_token_1_1234567890",
    "expiresIn": 3600
  }
}
```

### 2. 创建管理员

**接口地址：** `POST /admin/system/admin/create`

**请求参数：**
```json
{
  "roleId": 1,
  "avatar": "https://example.com/avatars/admin.jpg",
  "username": "admin",
  "password": "Admin123!",
  "phone": "13800138000",
  "email": "admin@example.com",
  "securityCode": "123456",
  "enableGoogleAuth": false
}
```

**密码规则：**
- 必须包含至少一个小写字母
- 必须包含至少一个大写字母
- 必须包含至少一个数字
- 必须包含至少一个特殊字符（@$!%*?&）
- 长度至少8位

### 3. 更新管理员

**接口地址：** `POST /admin/system/admin/update/{adminId}`

**请求参数：**
```json
{
  "roleId": 2,
  "avatar": "https://example.com/avatars/new.jpg",
  "phone": "13800138001",
  "email": "newemail@example.com",
  "securityCode": "654321",
  "enableGoogleAuth": true,
  "status": "ACTIVE"
}
```

### 4. 删除管理员

**接口地址：** `DELETE /admin/system/admin/{adminId}`

**注意：** 不能删除超级管理员（ROOT角色）

### 5. 查询管理员列表

**接口地址：** `GET /admin/system/admin/list`

### 6. 根据ID查询管理员

**接口地址：** `GET /admin/system/admin/{adminId}`

### 7. 根据用户名查询管理员

**接口地址：** `GET /admin/system/admin/username/{username}`

### 8. 根据手机号查询管理员

**接口地址：** `GET /admin/system/admin/phone/{phone}`

### 9. 根据邮箱查询管理员

**接口地址：** `GET /admin/system/admin/email/{email}`

### 10. 根据角色查询管理员

**接口地址：** `GET /admin/system/admin/role/{roleId}`

### 11. 根据状态查询管理员

**接口地址：** `GET /admin/system/admin/status/{status}`

**状态值：** ACTIVE, INACTIVE, SUSPENDED

### 12. 修改密码

**接口地址：** `POST /admin/system/admin/{adminId}/change-password`

**请求参数：**
```json
{
  "oldPassword": "OldPassword123!",
  "newPassword": "NewPassword123!"
}
```

### 13. 重置密码（管理员操作）

**接口地址：** `POST /admin/system/admin/{adminId}/reset-password`

**请求参数：**
- newPassword: 新密码（Query参数）

### 14. 检查权限

**接口地址：** `GET /admin/system/admin/check-permission`

**请求参数：**
- adminId: 管理员ID
- permissionCode: 权限代码

## 状态说明

- **ACTIVE**: 激活状态，可以正常登录和使用
- **INACTIVE**: 禁用状态，不能登录
- **SUSPENDED**: 暂停状态，临时禁用

## 安全说明

1. **密码加密**：所有密码使用 BCrypt 算法加密存储
2. **谷歌验证码**：支持启用谷歌验证码进行二次验证
3. **安全码**：支持设置安全码进行额外验证
4. **登录记录**：自动记录登录IP和设备信息
5. **权限控制**：ROOT角色拥有所有权限，其他角色根据权限配置

## 测试数据

默认测试账户：
- 用户名：admin
- 密码：Admin123!
- 角色：超级管理员（ROOT）

**注意：** 生产环境请务必修改默认密码！












