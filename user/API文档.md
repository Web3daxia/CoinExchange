# 用户模块 API 文档

## 模块说明
用户模块提供用户注册、登录、账户管理、KYC认证等功能。

## Base URL
- 用户接口: `/user`
- 账户接口: `/account`
- KYC接口: `/kyc`

## 认证
大部分接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 用户注册与登录

### 1.1 用户注册（通用接口）

**接口地址**: `POST /user/register`

**接口描述**: 支持手机号、邮箱、私钥三种注册方式

**请求参数**:
```json
{
  "registerType": "PHONE",
  "phone": "13800138000",
  "countryCode": "+86",
  "email": "user@example.com",
  "verificationCode": "123456",
  "password": "Password123!",
  "privateKey": "..."
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| registerType | String | 是 | 注册类型: PHONE, EMAIL, PRIVATE_KEY |
| phone | String | 条件 | 手机号（手机注册时必填） |
| countryCode | String | 条件 | 国家代码（手机注册时必填） |
| email | String | 条件 | 邮箱（邮箱注册时必填） |
| verificationCode | String | 条件 | 验证码（手机/邮箱注册时必填） |
| password | String | 条件 | 密码（手机/邮箱注册时必填，8-20位） |
| privateKey | String | 条件 | 私钥（私钥注册时必填） |

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

### 1.2 手机号注册

**接口地址**: `POST /user/register/phone`

**请求参数**:
```json
{
  "phone": "13800138000",
  "countryCode": "+86",
  "verificationCode": "123456",
  "password": "Password123!"
}
```

### 1.3 邮箱注册

**接口地址**: `POST /user/register/email`

**请求参数**:
```json
{
  "email": "user@example.com",
  "verificationCode": "123456",
  "password": "Password123!"
}
```

### 1.4 私钥注册

**接口地址**: `POST /user/register/private-key`

**请求参数**:
```json
{
  "privateKey": "..."
}
```

### 1.5 发送手机验证码

**接口地址**: `POST /user/send-verification/phone`

**请求参数**:
```json
{
  "phone": "13800138000",
  "countryCode": "+86"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证码已发送",
  "data": null
}
```

### 1.6 发送邮箱验证码

**接口地址**: `POST /user/send-verification/email`

**请求参数**:
```json
{
  "email": "user@example.com"
}
```

### 1.7 用户登录（通用接口）

**接口地址**: `POST /user/login`

**接口描述**: 支持密码、私钥、第三方OAuth登录

**请求参数**:
```json
{
  "loginType": "PASSWORD",
  "account": "13800138000",
  "password": "Password123!",
  "privateKey": "...",
  "oauthToken": "...",
  "twoFactorCode": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "userId": 1,
    "username": "user123",
    "needBindContact": false
  }
}
```

### 1.8 私钥登录

**接口地址**: `POST /user/login/private-key`

**请求参数**:
```json
{
  "privateKey": "..."
}
```

### 1.9 OAuth登录

**接口地址**:
- Google: `POST /user/login/google`
- Facebook: `POST /user/login/facebook`
- Twitter: `POST /user/login/twitter`
- Telegram: `POST /user/login/telegram`

**请求参数**:
```json
{
  "oauthToken": "...",
  "provider": "GOOGLE"
}
```

### 1.10 重置密码

**接口地址**: `POST /user/password-reset`

**请求参数**:
```json
{
  "account": "user@example.com",
  "verificationCode": "123456",
  "newPassword": "NewPassword123!"
}
```

---

## 2. 账户管理

### 2.1 获取用户基本信息

**接口地址**: `GET /account/profile`

**请求头**:
```
X-User-Id: 1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "user123",
    "nickname": "昵称",
    "email": "user@example.com",
    "phone": "13800138000",
    "avatarUrl": "https://...",
    "gender": "MALE",
    "birthday": "1990-01-01T00:00:00",
    "kycLevel": 1,
    "kycStatus": "APPROVED",
    "twoFaEnabled": true,
    "accountStatus": "ACTIVE",
    "oauthBindings": [
      {
        "provider": "GOOGLE",
        "bindTime": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

### 2.2 更新用户个人资料

**接口地址**: `POST /account/profile/update`

**请求头**:
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | String | 否 | 昵称 |
| avatar | File | 否 | 头像文件 |
| gender | String | 否 | 性别: MALE, FEMALE |
| birthday | String | 否 | 生日 |

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 2.3 上传头像

**接口地址**: `POST /account/avatar/upload`

**请求头**:
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| avatar | File | 是 | 头像文件 |

**响应示例**:
```json
{
  "code": 200,
  "message": "头像上传成功",
  "data": {
    "avatarUrl": "https://..."
  }
}
```

### 2.4 绑定邮箱

**接口地址**: `POST /account/bind/email`

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
```json
{
  "email": "newemail@example.com",
  "verificationCode": "123456"
}
```

### 2.5 绑定手机号

**接口地址**: `POST /account/bind/phone`

**请求参数**:
```json
{
  "phone": "13800138000",
  "countryCode": "+86",
  "verificationCode": "123456"
}
```

### 2.6 解绑邮箱

**接口地址**: `POST /account/unbind/email`

**请求头**:
```
X-User-Id: 1
```

### 2.7 解绑手机号

**接口地址**: `POST /account/unbind/phone`

**请求头**:
```
X-User-Id: 1
```

### 2.8 绑定OAuth账户

**接口地址**: `POST /account/bind/oauth`

**请求参数**:
```json
{
  "provider": "GOOGLE",
  "oauthToken": "..."
}
```

### 2.9 解绑OAuth账户

**接口地址**: `POST /account/unbind/oauth`

**请求参数**:
```json
{
  "provider": "GOOGLE"
}
```

### 2.10 修改密码

**接口地址**: `POST /account/password/update`

**请求参数**:
```json
{
  "oldPassword": "OldPassword123!",
  "newPassword": "NewPassword123!"
}
```

### 2.11 获取登录历史

**接口地址**: `GET /account/login-history`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码，默认0 |
| size | Integer | 否 | 每页数量，默认20 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1,
        "userId": 1,
        "ipAddress": "192.168.1.1",
        "deviceType": "WEB",
        "userAgent": "Mozilla/5.0...",
        "loginTime": "2024-01-01T10:00:00",
        "status": "SUCCESS"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

### 2.12 获取设备列表

**接口地址**: `GET /account/devices`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "deviceId": 1,
      "deviceType": "WEB",
      "deviceName": "Chrome on Windows",
      "ipAddress": "192.168.1.1",
      "lastLoginTime": "2024-01-01T10:00:00",
      "isCurrentDevice": true
    }
  ]
}
```

### 2.13 退出指定设备

**接口地址**: `POST /account/security/device/exit`

**请求参数**:
```json
{
  "deviceId": 1
}
```

### 2.14 退出所有设备

**接口地址**: `POST /account/security/device/logout-all`

### 2.15 添加IP白名单

**接口地址**: `POST /account/security/ip-whitelist/add`

**请求参数**:
```json
{
  "ipAddress": "192.168.1.1",
  "ipRange": "192.168.1.0/24",
  "remark": "办公室IP"
}
```

### 2.16 移除IP白名单

**接口地址**: `POST /account/security/ip-whitelist/remove`

**请求参数**:
```json
{
  "ipWhitelistId": 1
}
```

### 2.17 获取IP白名单列表

**接口地址**: `GET /account/security/ip-whitelist`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "ipAddress": "192.168.1.1",
      "ipRange": null,
      "remark": "办公室IP",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 2.18 启用2FA

**接口地址**: `POST /account/2fa/enable`

**响应示例**:
```json
{
  "code": 200,
  "message": "2FA已启用，请使用Google Authenticator扫描二维码",
  "data": {
    "secret": "JBSWY3DPEHPK3PXP",
    "qrCode": "data:image/png;base64,..."
  }
}
```

### 2.19 验证2FA

**接口地址**: `POST /account/2fa/verify`

**请求参数**:
```json
{
  "code": "123456"
}
```

### 2.20 禁用2FA

**接口地址**: `POST /account/2fa/disable`

**请求参数**:
```json
{
  "password": "Password123!"
}
```

### 2.21 冻结账户

**接口地址**: `POST /account/freeze`

**请求参数**:
```json
{
  "reason": "账户异常"
}
```

### 2.22 解冻账户

**接口地址**: `POST /account/unfreeze`

### 2.23 查询账户状态

**接口地址**: `GET /account/status`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "status": "ACTIVE"
  }
}
```

---

## 3. KYC身份认证

### 3.1 提交基础身份认证

**接口地址**: `POST /kyc/basic/submit`

**接口描述**: 提交姓名、证件号码和正反面照片，系统会进行OCR识别

**请求头**:
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fullName | String | 是 | 姓名 |
| idNumber | String | 是 | 证件号码 |
| idType | String | 是 | 证件类型: ID_CARD, PASSPORT |
| idFrontImage | File | 是 | 证件正面照片 |
| idBackImage | File | 是 | 证件反面照片 |

**响应示例**:
```json
{
  "code": 200,
  "message": "基础KYC认证资料已提交，正在审核中",
  "data": null
}
```

### 3.2 查询基础身份认证状态

**接口地址**: `GET /kyc/basic/status`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "kycLevel": 1,
    "status": "PENDING",
    "submitTime": "2024-01-01T10:00:00",
    "reviewTime": null,
    "rejectReason": null
  }
}
```

### 3.3 提交高级身份认证

**接口地址**: `POST /kyc/advanced/submit`

**接口描述**: 提交手持证件照片和验证视频，系统会进行人脸识别比对

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| idHoldImage | File | 是 | 手持证件照片 |
| verificationVideo | File | 是 | 验证视频 |

**响应示例**:
```json
{
  "code": 200,
  "message": "高级KYC认证资料已提交，正在审核中",
  "data": null
}
```

### 3.4 查询高级身份认证状态

**接口地址**: `GET /kyc/advanced/status`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "kycLevel": 2,
    "status": "APPROVED",
    "submitTime": "2024-01-01T10:00:00",
    "reviewTime": "2024-01-01T11:00:00",
    "rejectReason": null
  }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 注意事项

1. 密码规则：8-20位，包含大小写字母、数字和特殊字符
2. 验证码有效期为5分钟
3. JWT Token有效期为24小时
4. 2FA启用后，登录和敏感操作需要验证码
5. KYC认证审核时间一般为1-3个工作日
6. 账户状态：ACTIVE（正常）、FROZEN（冻结）、DISABLED（禁用）














