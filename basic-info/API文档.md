# 基础信息模块 API 文档

## 模块说明
基础信息模块提供平台的基础配置信息，包括汇率、语言设置、API端点配置、通知管理等。

## Base URL
```
/basic-info
```

## 认证
部分接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
```

---

## 1. 汇率相关接口

### 1.1 查询汇率列表

**接口地址**: `GET /basic-info/exchange-rates`

**接口描述**: 查询所有支持的汇率

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| baseCurrency | String | 否 | 基础货币（可选） |

**请求示例**:
```http
GET /basic-info/exchange-rates?baseCurrency=USD HTTP/1.1
Host: localhost:8080
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "currency": "USD",
      "rate": 1.0,
      "symbol": "$"
    },
    {
      "currency": "CNY",
      "rate": 7.2,
      "symbol": "¥"
    }
  ]
}
```

### 1.2 获取汇率

**接口地址**: `GET /basic-info/exchange-rate`

**接口描述**: 获取指定货币对的汇率

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fromCurrency | String | 是 | 源货币 |
| toCurrency | String | 是 | 目标货币 |

**请求示例**:
```http
GET /basic-info/exchange-rate?fromCurrency=USD&toCurrency=CNY HTTP/1.1
Host: localhost:8080
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": 7.2
}
```

### 1.3 货币转换

**接口地址**: `GET /basic-info/convert`

**接口描述**: 转换货币金额

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| amount | BigDecimal | 是 | 金额 |
| fromCurrency | String | 是 | 源货币 |
| toCurrency | String | 是 | 目标货币 |

**请求示例**:
```http
GET /basic-info/convert?amount=100&fromCurrency=USD&toCurrency=CNY HTTP/1.1
Host: localhost:8080
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": 720.0
}
```

---

## 2. 语言相关接口

### 2.1 查询支持的语言

**接口地址**: `GET /basic-info/languages`

**接口描述**: 获取所有支持的语言列表

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "languageCode": "zh-CN",
      "languageName": "简体中文",
      "isDefault": true
    },
    {
      "languageCode": "en-US",
      "languageName": "English",
      "isDefault": false
    }
  ]
}
```

### 2.2 自动识别语言

**接口地址**: `GET /basic-info/language/detect`

**接口描述**: 根据IP地址自动识别语言

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ipAddress | String | 否 | IP地址（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "zh-CN"
}
```

### 2.3 获取语言包

**接口地址**: `GET /basic-info/language/translations`

**接口描述**: 获取指定语言的翻译数据

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| languageCode | String | 是 | 语言代码 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "common.confirm": "确认",
    "common.cancel": "取消",
    "common.submit": "提交"
  }
}
```

### 2.4 设置用户语言

**接口地址**: `POST /basic-info/language/set`

**接口描述**: 设置用户的语言偏好

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| languageCode | String | 是 | 语言代码 |

**请求示例**:
```json
POST /basic-info/language/set HTTP/1.1
Host: localhost:8080
X-User-Id: 1
Content-Type: application/x-www-form-urlencoded

languageCode=zh-CN
```

**响应示例**:
```json
{
  "code": 200,
  "message": "语言设置成功",
  "data": {
    "userId": 1,
    "languageCode": "zh-CN",
    "currency": "USD",
    "timezone": "Asia/Shanghai"
  }
}
```

---

## 3. 用户偏好设置接口

### 3.1 查询用户偏好

**接口地址**: `GET /basic-info/preference`

**接口描述**: 查询用户的语言、货币等偏好设置

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
    "languageCode": "zh-CN",
    "currency": "USD",
    "apiEndpointId": 1,
    "timezone": "Asia/Shanghai",
    "dateFormat": "yyyy-MM-dd",
    "numberFormat": "#,###.##"
  }
}
```

### 3.2 设置用户偏好

**接口地址**: `POST /basic-info/preference`

**接口描述**: 设置用户的语言、货币、API端点等偏好

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "languageCode": "zh-CN",
  "currency": "USD",
  "apiEndpointId": 1,
  "timezone": "Asia/Shanghai",
  "dateFormat": "yyyy-MM-dd",
  "numberFormat": "#,###.##"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "偏好设置成功",
  "data": {
    "userId": 1,
    "languageCode": "zh-CN",
    "currency": "USD",
    "apiEndpointId": 1,
    "timezone": "Asia/Shanghai",
    "dateFormat": "yyyy-MM-dd",
    "numberFormat": "#,###.##"
  }
}
```

---

## 4. API端点配置接口

### 4.1 查询API端点

**接口地址**: `GET /basic-info/api-endpoints`

**接口描述**: 查询所有可用的API端点（行情线路）

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| endpointType | String | 否 | 端点类型（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "endpointType": "MARKET_DATA",
      "endpointUrl": "https://api.example.com/v1",
      "region": "CN",
      "isDefault": true,
      "priority": 1
    }
  ]
}
```

### 4.2 获取默认API端点

**接口地址**: `GET /basic-info/api-endpoint/default`

**接口描述**: 获取默认的API端点

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| endpointType | String | 是 | 端点类型 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "endpointType": "MARKET_DATA",
    "endpointUrl": "https://api.example.com/v1",
    "region": "CN",
    "isDefault": true
  }
}
```

### 4.3 设置用户API端点

**接口地址**: `POST /basic-info/api-endpoint/set`

**接口描述**: 设置用户选择的API端点（行情线路）

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| endpointId | Long | 是 | 端点ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "API端点设置成功",
  "data": null
}
```

### 4.4 获取用户API端点

**接口地址**: `GET /basic-info/api-endpoint/user`

**接口描述**: 获取用户选择的API端点

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| endpointType | String | 是 | 端点类型 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "endpointType": "MARKET_DATA",
    "endpointUrl": "https://api.example.com/v1",
    "region": "CN"
  }
}
```

---

## 5. 通知相关接口

### 5.1 查询通知列表

**接口地址**: `GET /basic-info/notifications`

**接口描述**: 查询用户的通知列表

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 否 | 通知类型（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "type": "SYSTEM",
      "title": "系统公告",
      "content": "平台维护通知",
      "isRead": false,
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 5.2 查询未读通知

**接口地址**: `GET /basic-info/notifications/unread`

**接口描述**: 查询用户的未读通知

**请求头**:
```
X-User-Id: 1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "type": "SYSTEM",
      "title": "系统公告",
      "content": "平台维护通知",
      "isRead": false,
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 5.3 标记通知为已读

**接口地址**: `POST /basic-info/notifications/{notificationId}/read`

**接口描述**: 标记指定通知为已读

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| notificationId | Long | 是 | 通知ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "标记成功",
  "data": null
}
```

### 5.4 标记所有通知为已读

**接口地址**: `POST /basic-info/notifications/read-all`

**接口描述**: 标记用户的所有通知为已读

**请求头**:
```
X-User-Id: 1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "全部标记成功",
  "data": null
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 注意事项

1. 需要用户认证的接口必须在请求头中携带 `X-User-Id`
2. 汇率数据实时更新，建议缓存使用
3. 语言代码遵循ISO 639-1标准
4. API端点配置会影响行情数据的获取速度和稳定性














