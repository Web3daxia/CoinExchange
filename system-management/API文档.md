# 系统管理模块 API 文档

## 模块说明
系统管理模块提供系统级别的管理功能，包括角色权限管理、管理员管理、币种管理、地区管理、多语言支持等。

## Base URL
```
/admin/system
```

## 认证
所有接口需要管理员认证，请在请求头中携带：
```
X-Admin-Id: {adminId}
Authorization: Bearer {token}
```

**注意**: ROOT角色拥有所有权限，ADMIN角色拥有管理权限，其他角色权限受限。

---

## 1. 系统角色管理接口

### 1.1 创建角色

**接口地址**: `POST /admin/system/role/create`

**接口描述**: 创建系统角色

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleCode | String | 是 | 角色代码，如: ROOT, ADMIN, OPERATOR, VIEWER |
| roleName | String | 是 | 角色名称 |
| description | String | 否 | 角色描述 |
| level | Integer | 是 | 角色级别，数字越小权限越高（ROOT=0, ADMIN=1） |

**响应示例**:
```json
{
  "code": 200,
  "message": "角色创建成功",
  "data": {
    "id": 1,
    "roleCode": "ADMIN",
    "roleName": "管理员",
    "description": "拥有系统管理权限",
    "level": 1,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 1.2 更新角色

**接口地址**: `POST /admin/system/role/update/{roleId}`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleName | String | 否 | 角色名称 |
| description | String | 否 | 角色描述 |
| level | Integer | 否 | 角色级别 |
| status | String | 否 | 状态: ACTIVE, INACTIVE |

### 1.3 删除角色

**接口地址**: `DELETE /admin/system/role/{roleId}`

### 1.4 查询角色列表

**接口地址**: `GET /admin/system/role/list`

### 1.5 查询角色详情

**接口地址**: `GET /admin/system/role/{roleId}`

### 1.6 分配权限

**接口地址**: `POST /admin/system/role/{roleId}/permission/{permissionId}`

**接口描述**: 为角色分配权限

### 1.7 移除权限

**接口地址**: `DELETE /admin/system/role/{roleId}/permission/{permissionId}`

---

## 2. 系统管理员管理接口

### 2.1 创建管理员

**接口地址**: `POST /admin/system/admin/create`

**接口描述**: 创建系统管理员账户

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID（关联users表） |
| username | String | 是 | 管理员用户名 |
| adminCode | String | 是 | 管理员代码，如: ROOT, ADMIN001 |
| roleId | Long | 是 | 角色ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "管理员创建成功",
  "data": {
    "id": 1,
    "userId": 1,
    "username": "admin",
    "adminCode": "ADMIN001",
    "roleId": 2,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 2.2 更新管理员

**接口地址**: `POST /admin/system/admin/update/{adminId}`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleId | Long | 否 | 角色ID |
| status | String | 否 | 状态: ACTIVE, INACTIVE, SUSPENDED |

### 2.3 删除管理员

**接口地址**: `DELETE /admin/system/admin/{adminId}`

**注意**: 不能删除ROOT管理员

### 2.4 查询管理员列表

**接口地址**: `GET /admin/system/admin/list`

### 2.5 查询管理员详情

**接口地址**: `GET /admin/system/admin/{adminId}`

### 2.6 检查权限

**接口地址**: `GET /admin/system/admin/check-permission`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| adminId | Long | 是 | 管理员ID |
| permissionCode | String | 是 | 权限代码 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

---

## 3. 币种管理接口

### 3.1 添加币种

**接口地址**: `POST /admin/system/currency/create`

**接口描述**: 添加新的币种

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyCode | String | 是 | 币种代码，如: BTC, ETH, USDT |
| currencyName | String | 是 | 币种名称 |
| symbol | String | 否 | 符号，如: ₿, Ξ |
| decimals | Integer | 否 | 小数位数（默认8） |
| spotEnabled | Boolean | 否 | 现货交易区是否启用（默认false） |
| futuresUsdtEnabled | Boolean | 否 | USDT本位合约是否启用（默认false） |
| futuresCoinEnabled | Boolean | 否 | 币本位合约是否启用（默认false） |
| optionsEnabled | Boolean | 否 | 期权交易是否启用（默认false） |
| leveragedEnabled | Boolean | 否 | 杠杆交易是否启用（默认false） |

**响应示例**:
```json
{
  "code": 200,
  "message": "币种添加成功",
  "data": {
    "id": 1,
    "currencyCode": "BTC",
    "currencyName": "Bitcoin",
    "symbol": "₿",
    "decimals": 8,
    "spotEnabled": true,
    "futuresUsdtEnabled": true,
    "futuresCoinEnabled": true,
    "optionsEnabled": true,
    "leveragedEnabled": true,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 3.2 编辑币种

**接口地址**: `POST /admin/system/currency/update/{currencyId}`

**接口描述**: 编辑币种的详细信息

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyName | String | 否 | 币种名称 |
| symbol | String | 否 | 符号 |
| iconUrl | String | 否 | 图标URL |
| decimals | Integer | 否 | 小数位数 |
| minWithdrawAmount | BigDecimal | 否 | 最小提现金额 |
| maxWithdrawAmount | BigDecimal | 否 | 最大提现金额 |
| withdrawFee | BigDecimal | 否 | 提现手续费 |
| depositEnabled | Boolean | 否 | 充值启用 |
| withdrawEnabled | Boolean | 否 | 提现启用 |
| spotEnabled | Boolean | 否 | 现货交易启用 |
| futuresUsdtEnabled | Boolean | 否 | USDT本位合约启用 |
| futuresCoinEnabled | Boolean | 否 | 币本位合约启用 |
| optionsEnabled | Boolean | 否 | 期权交易启用 |
| leveragedEnabled | Boolean | 否 | 杠杆交易启用 |
| status | String | 否 | 状态: ACTIVE, INACTIVE |
| sortOrder | Integer | 否 | 排序顺序 |
| description | String | 否 | 币种描述 |

### 3.3 删除币种

**接口地址**: `DELETE /admin/system/currency/{currencyId}`

### 3.4 查看币种列表

**接口地址**: `GET /admin/system/currency/list`

### 3.5 查看币种详情

**接口地址**: `GET /admin/system/currency/{currencyId}`

### 3.6 根据代码查询币种

**接口地址**: `GET /admin/system/currency/code/{currencyCode}`

### 3.7 查询现货启用币种

**接口地址**: `GET /admin/system/currency/spot-enabled`

### 3.8 查询USDT合约启用币种

**接口地址**: `GET /admin/system/currency/futures-usdt-enabled`

### 3.9 查询币本位合约启用币种

**接口地址**: `GET /admin/system/currency/futures-coin-enabled`

---

## 4. 地区管理接口

### 4.1 添加地区

**接口地址**: `POST /admin/system/region/create`

**接口描述**: 添加新的地区配置，限制哪些国家不可以访问API接口

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| countryCode | String | 是 | 国家代码，ISO 3166-1 alpha-2，如: CN, US |
| countryName | String | 是 | 国家名称 |
| regionCode | String | 否 | 地区代码 |
| regionName | String | 否 | 地区名称 |
| apiAccessEnabled | Boolean | 否 | API访问是否启用（默认true） |
| frontendAccessEnabled | Boolean | 否 | 前端访问是否启用（默认true） |
| blockReason | String | 否 | 限制原因 |

**响应示例**:
```json
{
  "code": 200,
  "message": "地区添加成功",
  "data": {
    "id": 1,
    "countryCode": "KP",
    "countryName": "朝鲜",
    "apiAccessEnabled": false,
    "frontendAccessEnabled": false,
    "blockReason": "政策限制",
    "status": "BLOCKED",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 4.2 更新地区

**接口地址**: `POST /admin/system/region/update/{regionId}`

### 4.3 删除地区

**接口地址**: `DELETE /admin/system/region/{regionId}`

### 4.4 查询地区列表

**接口地址**: `GET /admin/system/region/list`

### 4.5 查询被限制地区

**接口地址**: `GET /admin/system/region/blocked`

### 4.6 查询地区详情

**接口地址**: `GET /admin/system/region/{regionId}`

### 4.7 检查地区访问权限

**接口地址**: `GET /admin/system/region/check/{countryCode}`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "apiAccessEnabled": false,
    "frontendAccessEnabled": false,
    "blockMessage": "您所在的地区无法访问该服务"
  }
}
```

---

## 5. 系统消息管理接口（多语言）

### 5.1 创建消息

**接口地址**: `POST /admin/system/message/create`

**接口描述**: 创建多语言消息

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageKey | String | 是 | 消息键，如: error.currency.not_found |
| languageCode | String | 是 | 语言代码，如: zh-CN, en-US |
| messageValue | String | 是 | 消息值（翻译内容） |
| module | String | 否 | 模块名称 |

### 5.2 更新消息

**接口地址**: `POST /admin/system/message/update/{messageId}`

### 5.3 删除消息

**接口地址**: `DELETE /admin/system/message/{messageId}`

### 5.4 查询语言包

**接口地址**: `GET /admin/system/message/language/{languageCode}`

**接口描述**: 查询指定语言的所有消息

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "success.operation": "操作成功",
    "error.currency.not_found": "币种不存在",
    "error.region.blocked": "您所在的地区无法访问该服务"
  }
}
```

### 5.5 查询消息

**接口地址**: `GET /admin/system/message/key/{messageKey}`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| languageCode | String | 否 | 语言代码（默认zh-CN） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "币种不存在"
}
```

---

## 支持的语言列表

系统支持以下16种语言（默认中文）:

1. **zh-CN** - 简体中文（默认）
2. **en-US** - English (US)
3. **ja-JP** - 日本語
4. **ko-KR** - 한국어
5. **es-ES** - Español
6. **fr-FR** - Français
7. **de-DE** - Deutsch
8. **it-IT** - Italiano
9. **pt-BR** - Português (Brasil)
10. **ru-RU** - Русский
11. **ar-SA** - العربية
12. **hi-IN** - हिंदी
13. **tr-TR** - Türkçe
14. **vi-VN** - Tiếng Việt
15. **th-TH** - ไทย
16. **id-ID** - Bahasa Indonesia

---

## 角色权限说明

### ROOT角色（超级管理员）
- 级别：0（最高）
- 权限：拥有所有权限，可以管理所有功能和数据
- 特殊：不能删除ROOT管理员

### ADMIN角色（管理员）
- 级别：1
- 权限：拥有系统管理权限，可以管理用户、币种、地区等

### OPERATOR角色（操作员）
- 级别：2
- 权限：拥有日常操作权限，可以查看和处理业务数据

### VIEWER角色（查看者）
- 级别：3
- 权限：只能查看数据，无操作权限

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

1. **权限控制**: 所有接口都需要相应的权限，ROOT角色拥有所有权限
2. **币种启用**: 币种需要在对应的交易区启用后，才能在相应区域使用
3. **地区限制**: 被限制的地区访问API时会返回对应的多语言提示信息
4. **多语言**: 系统消息支持16种语言，默认返回中文
5. **角色级别**: 数字越小权限越高，ROOT=0拥有最高权限
6. **管理员关联**: 管理员必须关联已存在的用户（users表）














