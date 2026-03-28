# 系统管理模块API文档

## 基础信息

- **模块名称**：系统管理模块（System Management）
- **基础路径**：`/api/admin`
- **认证方式**：Bearer Token (JWT)

---

## 1. 系统管理员管理

### 1.1 创建管理员

**接口地址**：`POST /api/admin/system-admin/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**：
```json
{
  "username": "admin",
  "password": "password123",
  "email": "admin@example.com",
  "phone": "13800138000",
  "roleId": 1,
  "status": "ACTIVE",
  "remark": "系统管理员"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名（唯一） |
| password | String | 是 | 密码 |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 手机号 |
| roleId | Long | 是 | 角色ID |
| status | String | 否 | 状态：ACTIVE, DISABLED |
| remark | String | 否 | 备注 |

**响应示例**：
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "phone": "13800138000",
    "roleId": 1,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 1.2 获取管理员列表

**接口地址**：`GET /api/admin/system-admin/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| username | String | 否 | 用户名（模糊查询） |
| status | String | 否 | 状态筛选 |

**请求示例**：
```
GET /api/admin/system-admin/list?page=0&size=20&status=ACTIVE
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
        "username": "admin",
        "email": "admin@example.com",
        "status": "ACTIVE"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

### 1.3 获取管理员详情

**接口地址**：`GET /api/admin/system-admin/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 管理员ID |

**请求示例**：
```
GET /api/admin/system-admin/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "phone": "13800138000",
    "roleId": 1,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 1.4 更新管理员

**接口地址**：`PUT /api/admin/system-admin/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 管理员ID |

**请求参数**：
```json
{
  "email": "newemail@example.com",
  "phone": "13900139000",
  "roleId": 2,
  "status": "ACTIVE",
  "remark": "更新后的备注"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "newemail@example.com",
    "status": "ACTIVE"
  }
}
```

### 1.5 删除管理员

**接口地址**：`DELETE /api/admin/system-admin/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 管理员ID |

**请求示例**：
```
DELETE /api/admin/system-admin/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 2. 系统角色管理

### 2.1 创建角色

**接口地址**：`POST /api/admin/system-role/create`

**请求参数**：
```json
{
  "roleName": "财务管理员",
  "roleCode": "FINANCE_ADMIN",
  "description": "财务相关权限",
  "status": "ACTIVE"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleName | String | 是 | 角色名称 |
| roleCode | String | 是 | 角色代码（唯一） |
| description | String | 否 | 描述 |
| status | String | 否 | 状态：ACTIVE, DISABLED |

**响应示例**：
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "roleName": "财务管理员",
    "roleCode": "FINANCE_ADMIN",
    "status": "ACTIVE"
  }
}
```

### 2.2 获取角色列表

**接口地址**：`GET /api/admin/system-role/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/admin/system-role/list?page=0&size=20
```

### 2.3 分配权限

**接口地址**：`POST /api/admin/system-role/{roleId}/permissions`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleId | Long | 是 | 角色ID |

**请求参数**：
```json
{
  "permissionIds": [1, 2, 3, 4, 5]
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| permissionIds | Array<Long> | 是 | 权限ID列表 |

**响应示例**：
```json
{
  "code": 200,
  "message": "权限分配成功",
  "data": null
}
```

---

## 3. 币种管理

### 3.1 创建币种

**接口地址**：`POST /api/admin/currency/create`

**请求参数**：
```json
{
  "currencyCode": "BTC",
  "symbol": "₿",
  "iconUrl": "https://example.com/btc.png",
  "decimals": 8,
  "minWithdrawAmount": "0.001",
  "maxWithdrawAmount": "100",
  "withdrawFee": "0.0001",
  "depositEnabled": true,
  "withdrawEnabled": true,
  "spotEnabled": true,
  "futuresUsdtEnabled": false,
  "futuresCoinEnabled": false,
  "optionsEnabled": false,
  "leveragedEnabled": false,
  "status": "ACTIVE",
  "sortOrder": 1,
  "agentId": null,
  "totalSupply": "21000000",
  "detailLink": "https://example.com/btc",
  "launchDate": "2009-01-03",
  "logoUrl": "https://example.com/btc-logo.png",
  "exchangeRateToCny": "300000",
  "exchangeRateToUsd": "50000",
  "introduction": "比特币简介"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "currencyCode": "BTC",
    "status": "ACTIVE"
  }
}
```

### 3.2 获取币种列表

**接口地址**：`GET /api/admin/currency/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| currencyCode | String | 否 | 币种代码（模糊查询） |
| status | String | 否 | 状态筛选 |

**请求示例**：
```
GET /api/admin/currency/list?page=0&size=20&status=ACTIVE
```

### 3.3 更新币种

**接口地址**：`PUT /api/admin/currency/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 币种ID |

**请求参数**：同创建币种（所有字段可选）

---

## 4. 币种分类管理

### 4.1 创建分类

**接口地址**：`POST /api/admin/currency-category/create`

**请求参数**：
```json
{
  "categoryName": "TOP",
  "sortOrder": 1,
  "status": "ACTIVE"
}
```

### 4.2 获取分类列表

**接口地址**：`GET /api/admin/currency-category/list`

**请求示例**：
```
GET /api/admin/currency-category/list
```

### 4.3 分配币种到分类

**接口地址**：`POST /api/admin/currency-category/{categoryId}/currencies`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 是 | 分类ID |

**请求参数**：
```json
{
  "currencyIds": [1, 2, 3]
}
```

---

## 5. 区域管理

### 5.1 创建区域

**接口地址**：`POST /api/admin/region/create`

**请求参数**：
```json
{
  "regionCode": "CN",
  "regionName": "中国",
  "isBlocked": false,
  "remark": "允许访问"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| regionCode | String | 是 | 区域代码（唯一） |
| regionName | String | 是 | 区域名称 |
| isBlocked | Boolean | 否 | 是否禁止访问（默认false） |
| remark | String | 否 | 备注 |

**响应示例**：
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "regionCode": "CN",
    "regionName": "中国",
    "isBlocked": false
  }
}
```

### 5.2 获取区域列表

**接口地址**：`GET /api/admin/region/list`

**请求示例**：
```
GET /api/admin/region/list
```

---

## 6. 验证码服务配置

### 6.1 创建验证码服务配置

**接口地址**：`POST /api/admin/captcha-service-config/create`

**请求参数**：
```json
{
  "serviceName": "阿里云滑块验证码",
  "serviceType": "ALIBABA_SLIDER",
  "serviceCode": "ALIBABA_CAPTCHA",
  "isEnabled": true,
  "apiKey": "your-api-key",
  "apiSecret": "your-api-secret",
  "apiUrl": "https://captcha-api.aliyun.com",
  "description": "阿里云验证码服务"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| serviceName | String | 是 | 服务名称 |
| serviceType | String | 是 | 服务类型：ALIBABA_SLIDER, TENCENT, AWS等 |
| serviceCode | String | 是 | 服务代码（唯一） |
| isEnabled | Boolean | 否 | 是否启用 |
| apiKey | String | 否 | API密钥 |
| apiSecret | String | 否 | API密钥 |
| apiUrl | String | 否 | API地址 |
| description | String | 否 | 描述 |

### 6.2 启用验证码服务

**接口地址**：`PUT /api/admin/captcha-service-config/enable/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 配置ID |

**请求示例**：
```
PUT /api/admin/captcha-service-config/enable/1
```

---

## 7. OSS存储配置

### 7.1 创建OSS配置

**接口地址**：`POST /api/admin/oss-storage-config/create`

**请求参数**：
```json
{
  "serviceName": "阿里云OSS",
  "serviceType": "ALIBABA",
  "serviceCode": "ALIBABA_OSS",
  "isEnabled": true,
  "endpoint": "oss-cn-hangzhou.aliyuncs.com",
  "accessKeyId": "your-access-key-id",
  "accessKeySecret": "your-access-key-secret",
  "bucketName": "my-bucket",
  "region": "cn-hangzhou",
  "description": "阿里云对象存储"
}
```

### 7.2 启用OSS服务

**接口地址**：`PUT /api/admin/oss-storage-config/enable/{id}`

---

## 8. 邮件SMTP配置

### 8.1 创建SMTP配置

**接口地址**：`POST /api/admin/email-smtp-config/create`

**请求参数**：
```json
{
  "smtpHost": "smtp.gmail.com",
  "smtpPort": 587,
  "senderEmail": "noreply@example.com",
  "senderName": "系统通知",
  "username": "your-email@gmail.com",
  "password": "your-password",
  "isEnabled": true,
  "useSsl": true,
  "description": "Gmail SMTP配置"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| smtpHost | String | 是 | SMTP服务器地址 |
| smtpPort | Integer | 是 | SMTP端口 |
| senderEmail | String | 是 | 发件人邮箱 |
| senderName | String | 否 | 发件人名称 |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| isEnabled | Boolean | 否 | 是否启用 |
| useSsl | Boolean | 否 | 是否使用SSL |
| description | String | 否 | 描述 |

---

## 9. 短信服务配置

### 9.1 创建短信服务配置

**接口地址**：`POST /api/admin/sms-service-config/create`

**请求参数**：
```json
{
  "serviceName": "短信宝",
  "serviceType": "SMS_BAO",
  "serviceCode": "SMSBAO",
  "isEnabled": true,
  "apiUrl": "https://api.smsbao.com/sms",
  "apiKey": "your-api-key",
  "apiSecret": "your-api-secret",
  "username": "your-username",
  "password": "your-password",
  "description": "短信宝服务配置"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| serviceName | String | 是 | 服务名称 |
| serviceType | String | 是 | 服务类型：SMS_BAO, ALIBABA, TENCENT等 |
| serviceCode | String | 是 | 服务代码（唯一） |
| isEnabled | Boolean | 否 | 是否启用 |
| apiUrl | String | 否 | API地址 |
| apiKey | String | 否 | API密钥 |
| apiSecret | String | 否 | API密钥 |
| username | String | 否 | 用户名 |
| password | String | 否 | 密码 |

---

## 10. 市场数据源配置

### 10.1 创建市场数据源配置

**接口地址**：`POST /api/admin/market-data-source-config/create`

**请求参数**：
```json
{
  "dataSource": "BINANCE",
  "apiUrl": "https://api.binance.com",
  "apiKey": "your-api-key",
  "apiSecret": "your-api-secret",
  "isActive": true,
  "priority": 1,
  "description": "币安市场数据源"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| dataSource | String | 是 | 数据源：BINANCE, HUOBI, OKX, GATE |
| apiUrl | String | 是 | API地址 |
| apiKey | String | 否 | API密钥 |
| apiSecret | String | 否 | API密钥 |
| isActive | Boolean | 否 | 是否激活 |
| priority | Integer | 否 | 优先级（数字越小优先级越高） |

---

## 11. 验证码限制配置

### 11.1 创建验证码限制配置

**接口地址**：`POST /api/admin/verification-limit-config/create`

**请求参数**：
```json
{
  "limitType": "IP",
  "operationType": "REGISTER",
  "limitCount": 5,
  "timeWindowMinutes": 60,
  "status": "ACTIVE",
  "description": "IP注册验证码限制：每小时5次"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limitType | String | 是 | 限制类型：IP, DEVICE, USER |
| operationType | String | 是 | 操作类型：REGISTER, LOGIN, RESET_PASSWORD等 |
| limitCount | Integer | 是 | 限制次数 |
| timeWindowMinutes | Integer | 是 | 时间窗口（分钟） |
| status | String | 否 | 状态：ACTIVE, INACTIVE |

---

## 12. 黑名单管理

### 12.1 添加IP黑名单

**接口地址**：`POST /api/admin/blacklist/create`

**请求参数**：
```json
{
  "blacklistType": "IP",
  "value": "192.168.1.100",
  "reason": "恶意注册",
  "status": "ACTIVE",
  "remark": "禁止该IP访问"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| blacklistType | String | 是 | 黑名单类型：IP, USER, DEVICE |
| value | String | 是 | 黑名单值（IP地址、用户ID、设备ID） |
| reason | String | 否 | 原因 |
| status | String | 否 | 状态：ACTIVE, INACTIVE |
| remark | String | 否 | 备注 |

### 12.2 获取黑名单列表

**接口地址**：`GET /api/admin/blacklist/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| blacklistType | String | 否 | 黑名单类型筛选 |
| status | String | 否 | 状态筛选 |

---

## 13. 文件上传配置

### 13.1 创建文件上传配置

**接口地址**：`POST /api/admin/file-upload-config/create`

**请求参数**：
```json
{
  "allowedTypes": "image/jpeg,image/png,image/gif,video/mp4",
  "maxFileSize": 10485760,
  "maxFiles": 10,
  "uploadPath": "/uploads",
  "status": "ACTIVE",
  "description": "允许上传图片和视频，最大10MB"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| allowedTypes | String | 是 | 允许的文件类型（MIME类型，逗号分隔） |
| maxFileSize | Long | 是 | 最大文件大小（字节） |
| maxFiles | Integer | 否 | 最大文件数量 |
| uploadPath | String | 是 | 上传路径 |
| status | String | 否 | 状态：ACTIVE, INACTIVE |

---

## 14. APP版本管理

### 14.1 创建APP版本

**接口地址**：`POST /api/admin/app-version/create`

**请求参数**：
```json
{
  "platform": "ANDROID",
  "version": "1.0.0",
  "minVersion": "1.0.0",
  "downloadUrl": "https://example.com/app.apk",
  "updateDescription": "修复了一些bug，提升了性能",
  "isForcedUpdate": false,
  "status": "ACTIVE",
  "sortOrder": 1
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| platform | String | 是 | 平台：ANDROID, IOS |
| version | String | 是 | 版本号 |
| minVersion | String | 是 | 最低可运行版本 |
| downloadUrl | String | 是 | 下载地址 |
| updateDescription | String | 否 | 更新说明 |
| isForcedUpdate | Boolean | 否 | 是否强制更新 |
| status | String | 否 | 状态：ACTIVE, INACTIVE |
| sortOrder | Integer | 否 | 排序顺序 |

### 14.2 获取APP版本列表

**接口地址**：`GET /api/admin/app-version/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| platform | String | 否 | 平台筛选 |

---

## 15. 系统日志管理

### 15.1 获取系统日志列表

**接口地址**：`GET /api/admin/system-log/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| operatorId | Long | 否 | 操作人ID |
| operationModule | String | 否 | 操作模块 |
| responseStatus | String | 否 | 响应状态 |
| startTime | String | 否 | 开始时间（格式：yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式：yyyy-MM-dd HH:mm:ss） |

**请求示例**：
```
GET /api/admin/system-log/list?page=0&size=20&operationModule=USER_MANAGEMENT&startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
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
        "ipAddress": "192.168.1.100",
        "deviceId": "device-123",
        "region": "CN",
        "requestMethod": "POST",
        "operationUser": "admin",
        "operationModule": "USER_MANAGEMENT",
        "operationPath": "/api/admin/user/create",
        "requestParams": "{\"username\":\"test\"}",
        "responseStatus": 200,
        "executionTime": 150,
        "createdAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

### 15.2 清理旧日志

**接口地址**：`POST /api/admin/system-log/cleanup`

**请求参数**：
```json
{
  "beforeDays": 30
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| beforeDays | Integer | 是 | 清理多少天前的日志 |

---

## 16. 翻译服务配置

### 16.1 创建翻译服务配置

**接口地址**：`POST /api/admin/translation-service/create`

**请求参数**：
```json
{
  "serviceName": "Google翻译",
  "serviceType": "GOOGLE",
  "serviceCode": "GOOGLE_TRANSLATE",
  "isEnabled": false,
  "apiUrl": "https://translation.googleapis.com/language/translate/v2",
  "apiKey": "your-google-api-key",
  "region": null,
  "sourceLanguage": "en-US",
  "dailyLimit": 1000000,
  "priority": 10,
  "description": "Google Cloud Translation API"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| serviceName | String | 是 | 服务名称 |
| serviceType | String | 是 | 服务类型：GOOGLE, AZURE, BAIDU, YOUDAO, DEEPL |
| serviceCode | String | 是 | 服务代码（唯一） |
| isEnabled | Boolean | 否 | 是否启用 |
| apiUrl | String | 否 | API地址 |
| apiKey | String | 否 | API密钥 |
| apiSecret | String | 否 | API密钥（部分服务需要） |
| appId | String | 否 | 应用ID（百度等需要） |
| region | String | 否 | 区域（Azure等需要） |
| sourceLanguage | String | 否 | 源语言（默认en-US） |
| dailyLimit | Integer | 否 | 每日字符数限制 |
| priority | Integer | 否 | 优先级 |

### 16.2 启用翻译服务

**接口地址**：`PUT /api/admin/translation-service/enable/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 配置ID |

**请求示例**：
```
PUT /api/admin/translation-service/enable/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "启用成功",
  "data": {
    "id": 1,
    "serviceName": "Google翻译",
    "isEnabled": true
  }
}
```

### 16.3 测试翻译服务

**接口地址**：`POST /api/admin/translation-service/test/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 配置ID |

**请求示例**：
```
POST /api/admin/translation-service/test/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "测试成功",
  "data": true
}
```

### 16.4 获取当前启用的翻译服务

**接口地址**：`GET /api/admin/translation-service/active`

**请求示例**：
```
GET /api/admin/translation-service/active
```

---

## 17. 会员管理

### 17.1 获取会员列表

**接口地址**：`GET /api/admin/member/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| email | String | 否 | 邮箱（模糊查询） |
| phone | String | 否 | 手机号（模糊查询） |
| memberUid | String | 否 | 会员UID（精确查询） |
| status | String | 否 | 状态筛选：NORMAL, DISABLED |

**请求示例**：
```
GET /api/admin/member/list?page=0&size=20&status=NORMAL
```

### 17.2 获取会员详情

**接口地址**：`GET /api/admin/member/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 会员ID |

### 17.3 更新会员信息

**接口地址**：`PUT /api/admin/member/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 会员ID |

**请求参数**：
```json
{
  "email": "newemail@example.com",
  "phone": "13900139000",
  "country": "CN",
  "tradingStatus": "NORMAL",
  "userStatus": "NORMAL",
  "withdrawalStatus": "ALLOWED"
}
```

### 17.4 重置会员密码

**接口地址**：`POST /api/admin/member/{id}/reset-password`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 会员ID |

**请求参数**：
```json
{
  "newPassword": "newPassword123",
  "passwordType": "LOGIN"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newPassword | String | 是 | 新密码 |
| passwordType | String | 是 | 密码类型：LOGIN（登录密码）, ASSET（资产密码） |

---

## 18. 会员资产管理

### 18.1 获取会员资产列表

**接口地址**：`GET /api/admin/member/{memberId}/assets`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| memberId | Long | 是 | 会员ID |

**请求示例**：
```
GET /api/admin/member/1/assets
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "currencyCode": "BTC",
      "available": "1.50000000",
      "frozen": "0.50000000",
      "pendingRelease": "0.00000000"
    },
    {
      "id": 2,
      "currencyCode": "USDT",
      "available": "10000.00000000",
      "frozen": "0.00000000",
      "pendingRelease": "0.00000000"
    }
  ]
}
```

### 18.2 充值资产

**接口地址**：`POST /api/admin/member-asset/deposit`

**请求参数**：
```json
{
  "memberId": 1,
  "currencyCode": "USDT",
  "amount": "1000.00000000",
  "remark": "管理员充值"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| memberId | Long | 是 | 会员ID |
| currencyCode | String | 是 | 币种代码 |
| amount | String | 是 | 充值数量 |
| remark | String | 否 | 备注 |

### 18.3 冻结资产

**接口地址**：`POST /api/admin/member-asset/freeze`

**请求参数**：
```json
{
  "memberId": 1,
  "currencyCode": "BTC",
  "amount": "0.10000000",
  "reason": "风控冻结",
  "remark": "因异常交易冻结"
}
```

### 18.4 解锁资产

**接口地址**：`POST /api/admin/member-asset/unfreeze`

**请求参数**：
```json
{
  "memberId": 1,
  "currencyCode": "BTC",
  "amount": "0.10000000",
  "reason": "风控解除",
  "remark": "核实后解除冻结"
}
```

### 18.5 重置充值地址

**接口地址**：`POST /api/admin/member-asset/reset-deposit-address`

**请求参数**：
```json
{
  "memberId": 1,
  "currencyCode": "BTC"
}
```

---

## 19. 订单管理（现货）

### 19.1 获取现货订单列表（当前订单）

**接口地址**：`GET /api/admin/spot-order/current`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| email | String | 否 | 会员邮箱 |
| userId | Long | 否 | 用户ID |
| memberUid | String | 否 | 会员UID |
| phone | String | 否 | 手机号 |
| tradingCurrency | String | 否 | 交易币种 |
| settlementCurrency | String | 否 | 结算币种 |
| orderNo | String | 否 | 订单号 |
| pairName | String | 否 | 交易对 |
| status | String | 否 | 订单状态：TRADING, COMPLETED, CANCELLED, TIMEOUT, ALL |
| direction | String | 否 | 订单方向：BUY, SELL, ALL |
| orderType | String | 否 | 订单类型：LIMIT, MARKET, ALL |
| orderSource | String | 否 | 订单来源：USER_ONLY, NOT_USER, BOT_ONLY, NOT_BOT |

**请求示例**：
```
GET /api/admin/spot-order/current?page=0&size=20&status=TRADING&direction=BUY
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
        "orderNo": "SO-20240101120000-123456",
        "userId": 1,
        "memberUid": "UID001",
        "memberName": "张三",
        "pairName": "BTC/USDT",
        "entrustedQuantity": "1.00000000",
        "transactionAmount": "50000.00000000",
        "transactionVolume": "1.00000000",
        "orderType": "LIMIT",
        "orderDirection": "BUY",
        "entrustedPrice": "50000.00000000",
        "entrustedTime": "2024-01-01T12:00:00",
        "fees": "50.00000000",
        "status": "TRADING"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

### 19.2 获取现货订单列表（历史订单）

**接口地址**：`GET /api/admin/spot-order/history`

**请求参数**：同当前订单（参数相同）

### 19.3 获取订单详情

**接口地址**：`GET /api/admin/spot-order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

### 19.4 取消订单

**接口地址**：`POST /api/admin/spot-order/cancel/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
POST /api/admin/spot-order/cancel/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "取消成功",
  "data": null
}
```

---

## 20. 订单管理（U本位合约）

### 20.1 获取U本位合约订单列表（当前订单）

**接口地址**：`GET /api/admin/usdt-futures-order/current`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| email | String | 否 | 会员邮箱 |
| userId | Long | 否 | 用户ID |
| memberUid | String | 否 | 会员UID |
| phone | String | 否 | 手机号 |
| startTime | String | 否 | 开始时间（格式：yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式：yyyy-MM-dd HH:mm:ss） |
| pairName | String | 否 | 合约交易对 |
| orderCategory | String | 否 | 委托分类：OPEN, CLOSE, ALL |
| orderDirection | String | 否 | 委托方向：OPEN_LONG, OPEN_SHORT, ALL |
| orderType | String | 否 | 委托类型：LIMIT, MARKET, STOP_LOSS, ALL |
| isLiquidation | Boolean | 否 | 是否爆仓单：true, false, null（全部） |
| isPlannedOrder | Boolean | 否 | 是否计划委托：true, false, null（全部） |
| status | String | 否 | 订单状态：PENDING, CANCELLED, FILLED, FAILED, ALL |
| agentId | Long | 否 | 代理商ID |

**请求示例**：
```
GET /api/admin/usdt-futures-order/current?page=0&size=20&status=PENDING&orderCategory=OPEN
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
        "orderNo": "UF-20240101120000-123456",
        "userId": 1,
        "memberName": "张三",
        "contractName": "BTC永续合约",
        "orderType": "LIMIT",
        "orderTypeDisplay": "限价委托",
        "orderDirection": "OPEN_LONG",
        "orderDirectionDisplay": "买入开多",
        "status": "PENDING",
        "statusDisplay": "交易中",
        "quantity": "1.00000000",
        "price": "50000.00000000",
        "marginFrozen": "5000.00000000",
        "isLiquidation": false,
        "isLiquidationDisplay": "否",
        "isPlannedOrder": false,
        "isPlannedOrderDisplay": "否",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

### 20.2 获取U本位合约订单列表（历史订单）

**接口地址**：`GET /api/admin/usdt-futures-order/history`

**请求参数**：同当前订单

### 20.3 强制取消订单

**接口地址**：`POST /api/admin/usdt-futures-order/force-cancel/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

---

## 21. 仓位管理（U本位合约）

### 21.1 获取U本位合约仓位列表

**接口地址**：`GET /api/admin/usdt-futures-position/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| email | String | 否 | 会员邮箱 |
| userId | Long | 否 | 用户ID |
| memberUid | String | 否 | 会员UID |
| phone | String | 否 | 手机号 |
| availableBalanceGt | String | 否 | 可用余额大于 |
| frozenBalanceGt | String | 否 | 冻结余额大于 |
| longLeverageGt | Integer | 否 | 多仓杠杆大于 |
| longPositionGt | String | 否 | 多仓数量大于 |
| longMarginGt | String | 否 | 多仓保证金大于 |
| shortLeverageGt | Integer | 否 | 空仓杠杆大于 |
| shortPositionGt | String | 否 | 空仓数量大于 |
| shortMarginGt | String | 否 | 空仓保证金大于 |
| pairName | String | 否 | 合约交易对 |
| marginMode | String | 否 | 仓位模式：CROSS, ISOLATED, SEGMENTED, COMBINED |

**请求示例**：
```
GET /api/admin/usdt-futures-position/list?page=0&size=20&marginMode=CROSS
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
        "memberName": "张三",
        "contractName": "BTC永续合约",
        "accountType": "FUTURES_USDT",
        "accountTypeDisplay": "USDT合约账户",
        "availableBalance": "95000.00000000",
        "frozenBalance": "5000.00000000",
        "marginMode": "CROSS",
        "marginModeDisplay": "全仓",
        "longPosition": "1.00000000",
        "longLeverage": 10,
        "longLeverageDisplay": "10X",
        "longAvgPrice": "50000.00000000",
        "longUnrealizedPnl": "1000.00000000",
        "longUnrealizedPnlPercent": "0.02000000",
        "longPnlDisplay": "1000.00 | 2.00%",
        "longMargin": "5000.00000000",
        "shortPosition": "0.00000000",
        "shortLeverage": null,
        "shortPnlDisplay": "0.00 | 0.00%"
      }
    ],
    "totalElements": 30,
    "totalPages": 2
  }
}
```

---

## 22. 订单管理（币本位合约）

### 22.1 获取币本位合约订单列表（当前订单）

**接口地址**：`GET /api/admin/coin-futures-order/current`

**请求参数**：同U本位合约订单

### 22.2 获取币本位合约订单列表（历史订单）

**接口地址**：`GET /api/admin/coin-futures-order/history`

**请求参数**：同U本位合约订单

### 22.3 强制取消订单

**接口地址**：`POST /api/admin/coin-futures-order/force-cancel/{orderId}`

---

## 23. 仓位管理（币本位合约）

### 23.1 获取币本位合约仓位列表

**接口地址**：`GET /api/admin/coin-futures-position/list`

**请求参数**：同U本位合约仓位

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














