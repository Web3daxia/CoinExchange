# 用户模块API文档

## 基础信息

- **模块名称**：用户模块（User）
- **用户端基础路径**：`/user`
- **认证方式**：Bearer Token (JWT)

---

## 用户注册与登录

### 1. 用户注册

#### 1.1 用户注册（通用接口）

**接口地址**：`POST /user/register`

**请求参数**：
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

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| registerType | String | 是 | 注册类型：PHONE（手机）, EMAIL（邮箱） |
| phone | String | 条件 | 手机号（手机注册时必填） |
| countryCode | String | 条件 | 国家代码（手机注册时必填） |
| email | String | 条件 | 邮箱（邮箱注册时必填） |
| verificationCode | String | 条件 | 验证码（手机/邮箱注册时必填） |
| password | String | 条件 | 密码（手机/邮箱注册时必填，8-20位） |

**注意**：系统会自动生成256位字母数字组合作为注册私钥，私钥将在注册响应中返回一次，请妥善保管。

**响应示例**：
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 1,
    "username": "user123",
    "registrationKey": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2A3B4C5D6E7F8G9H0I1J2K3L4M5N6O7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0",
    "warning": "注册私钥仅显示一次，请妥善保管，丢失后无法找回"
  }
}
```

#### 1.2 手机号注册

**接口地址**：`POST /user/register/phone`

**请求参数**：
```json
{
  "phone": "13800138000",
  "countryCode": "+86",
  "verificationCode": "123456",
  "password": "Password123!"
}
```

#### 1.3 邮箱注册

**接口地址**：`POST /user/register/email`

**请求参数**：
```json
{
  "email": "user@example.com",
  "verificationCode": "123456",
  "password": "Password123!"
}
```

**注意**：注册时系统会自动生成256位字母数字组合作为注册私钥，私钥将单独保存到用户注册私钥表中。注册成功后，私钥仅在首次返回，请妥善保管。

---

### 2. 验证码发送

#### 2.1 发送手机验证码

**接口地址**：`POST /user/send-verification/phone`

**请求参数**：
```json
{
  "phone": "13800138000",
  "countryCode": "+86"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "验证码已发送",
  "data": null
}
```

#### 2.2 发送邮箱验证码

**接口地址**：`POST /user/send-verification/email`

**请求参数**：
```json
{
  "email": "user@example.com"
}
```

---

### 3. 用户登录

#### 3.1 用户登录（通用接口）

**接口地址**：`POST /user/login`

**请求参数**：
```json
{
  "loginType": "PASSWORD",
  "phone": "13800138000",
  "email": "user@example.com",
  "password": "Password123!",
  "registrationKey": "...",
  "provider": "GOOGLE",
  "oauthToken": "..."
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| loginType | String | 是 | 登录类型：PASSWORD（密码）, REGISTRATION_KEY（注册私钥）, OAUTH（第三方） |
| phone | String | 条件 | 手机号（密码登录时） |
| email | String | 条件 | 邮箱（密码登录时） |
| password | String | 条件 | 密码（密码登录时必填） |
| registrationKey | String | 条件 | 注册私钥（注册私钥登录时必填，系统注册时生成的256位字符串） |
| provider | String | 条件 | OAuth提供商（OAuth登录时必填） |
| oauthToken | String | 条件 | OAuth令牌（OAuth登录时必填） |

**响应示例**：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here",
    "userId": 1,
    "username": "user123",
    "expiresIn": 3600
  }
}
```

#### 3.2 私钥登录

**接口地址**：`POST /user/login/private-key`

**请求参数**：
```json
{
  "registrationKey": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2A3B4C5D6E7F8G9H0I1J2K3L4M5N6O7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| registrationKey | String | 是 | 注册私钥（注册时系统自动生成的256位字母数字组合） |

**说明**：使用注册时系统生成的注册私钥进行登录。注册私钥可以从注册接口响应中获取，或通过查询接口获取（需登录后查询）。

#### 3.3 Google OAuth登录

**接口地址**：`POST /user/login/google`

**请求参数**：
```json
{
  "oauthToken": "..."
}
```

#### 3.4 Facebook OAuth登录

**接口地址**：`POST /user/login/facebook`

#### 3.5 Twitter OAuth登录

**接口地址**：`POST /user/login/twitter`

---

## 账户管理

### 1. 用户信息

#### 1.1 获取用户基本信息

**接口地址**：`GET /account/profile`

**请求头**：
```
X-User-Id: 1
Authorization: Bearer {token}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "userId": 1,
    "username": "user123",
    "email": "user@example.com",
    "phone": "13800138000",
    "countryCode": "+86",
    "avatar": "https://example.com/avatar.jpg",
    "nickname": "昵称",
    "birthday": "1990-01-01",
    "gender": "MALE",
    "country": "CN",
    "kycLevel": "BASIC",
    "registrationTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 更新用户个人资料

**接口地址**：`POST /account/profile/update`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | String | 否 | 昵称 |
| avatar | File | 否 | 头像文件 |
| birthday | String | 否 | 生日（格式：yyyy-MM-dd） |
| gender | String | 否 | 性别：MALE, FEMALE, OTHER |
| country | String | 否 | 国家代码 |

**请求示例**：
```
POST /account/profile/update
Content-Type: multipart/form-data

nickname=新昵称
birthday=1990-01-01
gender=MALE
```

#### 1.3 上传头像

**接口地址**：`POST /account/avatar/upload`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| avatar | File | 是 | 头像文件 |

**响应示例**：
```json
{
  "code": 200,
  "message": "头像上传成功",
  "data": {
    "avatarUrl": "https://example.com/avatar/xxx.jpg"
  }
}
```

---

### 2. 账户绑定

#### 2.1 绑定邮箱

**接口地址**：`POST /account/bind/email`

**请求参数**：
```json
{
  "email": "newemail@example.com",
  "verificationCode": "123456"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| email | String | 是 | 邮箱地址 |
| verificationCode | String | 是 | 邮箱验证码 |

**响应示例**：
```json
{
  "code": 200,
  "message": "邮箱绑定成功",
  "data": null
}
```

#### 2.2 绑定手机号

**接口地址**：`POST /account/bind/phone`

**请求参数**：
```json
{
  "phone": "13800138000",
  "countryCode": "+86",
  "verificationCode": "123456"
}
```

#### 2.3 解绑邮箱

**接口地址**：`POST /account/unbind/email`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
POST /account/unbind/email
```

#### 2.4 解绑手机号

**接口地址**：`POST /account/unbind/phone`

**请求头**：
```
X-User-Id: 1
```

#### 2.5 绑定OAuth账户

**接口地址**：`POST /account/bind/oauth`

**请求参数**：
```json
{
  "provider": "GOOGLE",
  "oauthToken": "..."
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| provider | String | 是 | OAuth提供商：GOOGLE, FACEBOOK, TWITTER |
| oauthToken | String | 是 | OAuth令牌 |

#### 2.6 解绑OAuth账户

**接口地址**：`POST /account/unbind/oauth`

**请求参数**：
```json
{
  "provider": "GOOGLE"
}
```

---

### 3. 密码管理

#### 3.1 修改密码

**接口地址**：`POST /account/password/update`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "oldPassword": "OldPassword123!",
  "newPassword": "NewPassword123!"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | String | 是 | 旧密码 |
| newPassword | String | 是 | 新密码（8-20位） |

**响应示例**：
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

---

### 4. 登录历史

#### 4.1 获取登录历史

**接口地址**：`GET /account/login-history`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /account/login-history?page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "content": [
      {
        "id": 1,
        "userId": 1,
        "loginTime": "2024-01-01T12:00:00",
        "loginIp": "192.168.1.1",
        "loginDevice": "Chrome",
        "loginLocation": "北京市",
        "status": "SUCCESS"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

---

## KYC身份认证

### 1. 基础KYC认证

#### 1.1 提交基础身份认证

**接口地址**：`POST /kyc/basic/submit`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| realName | String | 是 | 真实姓名 |
| idType | String | 是 | 证件类型：ID_CARD（身份证）, PASSPORT（护照）, DRIVER_LICENSE（驾驶证） |
| idNumber | String | 是 | 证件号码 |
| idFront | File | 是 | 证件正面照片 |
| idBack | File | 是 | 证件反面照片 |

**请求示例**：
```
POST /kyc/basic/submit
Content-Type: multipart/form-data

realName=张三
idType=ID_CARD
idNumber=110101199001011234
idFront=<file>
idBack=<file>
```

**响应示例**：
```json
{
  "code": 200,
  "message": "基础KYC认证资料已提交，正在审核中",
  "data": null
}
```

#### 1.2 查询基础身份认证状态

**接口地址**：`GET /kyc/basic/status`

**请求头**：
```
X-User-Id: 1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "kycLevel": "BASIC",
    "status": "PENDING",
    "statusDisplay": "审核中",
    "submitTime": "2024-01-01T12:00:00",
    "reviewTime": null,
    "remark": null
  }
}
```

---

### 2. 高级KYC认证

#### 2.1 提交高级身份认证

**接口地址**：`POST /kyc/advanced/submit`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| idHandheld | File | 是 | 手持证件照片 |
| video | File | 是 | 验证视频 |

**请求示例**：
```
POST /kyc/advanced/submit
Content-Type: multipart/form-data

idHandheld=<file>
video=<file>
```

**响应示例**：
```json
{
  "code": 200,
  "message": "高级KYC认证资料已提交，正在审核中",
  "data": null
}
```

#### 2.2 查询高级身份认证状态

**接口地址**：`GET /kyc/advanced/status`

**请求头**：
```
X-User-Id: 1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "kycLevel": "ADVANCED",
    "status": "APPROVED",
    "statusDisplay": "已通过",
    "submitTime": "2024-01-01T12:00:00",
    "reviewTime": "2024-01-02T10:00:00",
    "remark": "认证通过"
  }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 通用响应格式

所有API响应统一使用以下格式：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

- `code`: HTTP状态码或业务状态码
- `message`: 响应消息
- `data`: 响应数据（可能为对象、数组或null）

---

**文档版本**：v1.0  
**最后更新**：2024-01-01

