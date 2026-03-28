# 合约体验金模块API文档

## 基础信息

- **模块名称**：合约体验金模块（Contract Experience Fund）
- **用户端基础路径**：`/api/experience-fund`
- **后台管理基础路径**：`/api/admin/experience-fund`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 账户管理

#### 1.1 创建体验金账户

**接口地址**：`POST /api/experience-fund/account/create/{activityId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

**请求示例**：
```
POST /api/experience-fund/account/create/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "体验金账户创建成功",
  "data": {
    "id": 1,
    "accountNo": "EF-20240101120000-123456",
    "userId": 1,
    "activityId": 1,
    "initialBalance": "1000.00000000",
    "currentBalance": "1000.00000000",
    "status": "ACTIVE",
    "expireTime": "2024-01-08T12:00:00",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 获取我的体验金账户

**接口地址**：`GET /api/experience-fund/account/my-account`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/experience-fund/account/my-account
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "accountNo": "EF-20240101120000-123456",
    "userId": 1,
    "activityId": 1,
    "initialBalance": "1000.00000000",
    "currentBalance": "950.00000000",
    "frozenBalance": "50.00000000",
    "status": "ACTIVE",
    "expireTime": "2024-01-08T12:00:00",
    "totalProfit": "100.00000000",
    "totalLoss": "50.00000000",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 1.3 检查是否可以交易

**接口地址**：`GET /api/experience-fund/account/can-trade/{accountId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |

**请求示例**：
```
GET /api/experience-fund/account/can-trade/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

### 2. 交易管理

#### 2.1 下单

**接口地址**：`POST /api/experience-fund/trade/place-order`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "accountId": 1,
  "contractType": "FUTURES_USDT",
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "OPEN_LONG",
  "leverage": 10,
  "quantity": "0.10000000",
  "price": "50000.00000000",
  "stopLossPrice": "48000.00000000",
  "takeProfitPrice": "52000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |
| contractType | String | 是 | 合约类型：FUTURES_USDT（U本位）, FUTURES_COIN（币本位）, PERPETUAL（永续） |
| pairName | String | 是 | 交易对 |
| orderType | String | 是 | 订单类型：MARKET（市价）, LIMIT（限价） |
| side | String | 是 | 委托方向：OPEN_LONG（开多）, OPEN_SHORT（开空）, CLOSE_LONG（平多）, CLOSE_SHORT（平空） |
| leverage | Integer | 是 | 杠杆倍数 |
| quantity | String | 是 | 交易数量 |
| price | String | 否 | 交易价格（限价单必填） |
| stopLossPrice | String | 否 | 止损价格 |
| takeProfitPrice | String | 否 | 止盈价格 |

**请求示例**：
```json
POST /api/experience-fund/trade/place-order
Content-Type: application/json

{
  "accountId": 1,
  "contractType": "FUTURES_USDT",
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "OPEN_LONG",
  "leverage": 10,
  "quantity": "0.10000000",
  "price": "50000.00000000"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "下单成功",
  "data": {
    "id": 1,
    "tradeNo": "ET-20240101120000-123456",
    "accountId": 1,
    "contractType": "FUTURES_USDT",
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "OPEN_LONG",
    "leverage": 10,
    "quantity": "0.10000000",
    "price": "50000.00000000",
    "status": "FILLED",
    "fillPrice": "50000.00000000",
    "fillQuantity": "0.10000000",
    "unrealizedProfit": "0.00000000",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 平仓

**接口地址**：`POST /api/experience-fund/trade/close/{tradeId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tradeId | Long | 是 | 交易ID（持仓ID） |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| exitPrice | String | 否 | 平仓价格（不填则使用市价） |

**请求示例**：
```
POST /api/experience-fund/trade/close/1?exitPrice=51000.00000000
```

**响应示例**：
```json
{
  "code": 200,
  "message": "平仓成功",
  "data": {
    "id": 1,
    "tradeNo": "ET-20240101120000-123456",
    "status": "CLOSED",
    "exitPrice": "51000.00000000",
    "profit": "100.00000000",
    "closeTime": "2024-01-01T13:00:00"
  }
}
```

#### 2.3 取消订单

**接口地址**：`POST /api/experience-fund/trade/cancel/{tradeId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tradeId | Long | 是 | 交易ID |

**请求示例**：
```
POST /api/experience-fund/trade/cancel/1
```

#### 2.4 获取我的交易记录

**接口地址**：`GET /api/experience-fund/trade/my-trades`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| contractType | String | 否 | 合约类型筛选 |
| status | String | 否 | 状态筛选：PENDING, FILLED, CANCELLED, CLOSED |

**请求示例**：
```
GET /api/experience-fund/trade/my-trades?page=0&size=20
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
        "tradeNo": "ET-20240101120000-123456",
        "contractType": "FUTURES_USDT",
        "pairName": "BTC/USDT",
        "side": "OPEN_LONG",
        "leverage": 10,
        "quantity": "0.10000000",
        "entryPrice": "50000.00000000",
        "status": "CLOSED",
        "profit": "100.00000000",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 2.5 获取我的持仓

**接口地址**：`GET /api/experience-fund/trade/my-positions/{accountId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |

**请求示例**：
```
GET /api/experience-fund/trade/my-positions/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "tradeNo": "ET-20240101120000-123456",
      "contractType": "FUTURES_USDT",
      "pairName": "BTC/USDT",
      "side": "OPEN_LONG",
      "leverage": 10,
      "quantity": "0.10000000",
      "entryPrice": "50000.00000000",
      "currentPrice": "51000.00000000",
      "unrealizedProfit": "100.00000000",
      "status": "OPEN",
      "createdAt": "2024-01-01T12:00:00"
    }
  ]
}
```

---

## 后台管理API

### 1. 账户管理

#### 1.1 获取所有体验金账户

**接口地址**：`GET /api/admin/experience-fund/account/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| activityId | Long | 否 | 活动ID筛选 |
| status | String | 否 | 状态筛选：ACTIVE, EXPIRED, DISABLED |

**请求示例**：
```
GET /api/admin/experience-fund/account/list?page=0&size=20
```

#### 1.2 获取账户详情

**接口地址**：`GET /api/admin/experience-fund/account/{accountId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |

#### 1.3 手动调整账户余额

**接口地址**：`POST /api/admin/experience-fund/account/adjust-balance/{accountId}`

**请求参数**：
```json
{
  "balance": "1500.00000000",
  "remark": "管理员调整余额"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| balance | String | 是 | 调整后的余额 |
| remark | String | 否 | 备注 |

---

### 2. 活动管理

#### 2.1 创建体验金活动

**接口地址**：`POST /api/admin/experience-fund/activity/create`

**请求参数**：
```json
{
  "activityName": "新用户KYC奖励",
  "activityCode": "NEW_USER_KYC",
  "fundAmount": "1000.00000000",
  "validDays": 7,
  "maxLeverage": 10,
  "maxPosition": "5000.00000000",
  "status": "ACTIVE",
  "startTime": "2024-01-01T00:00:00",
  "endTime": "2024-12-31T23:59:59",
  "description": "新用户完成KYC即可获得1000 USDT体验金"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityName | String | 是 | 活动名称 |
| activityCode | String | 是 | 活动代码（唯一） |
| fundAmount | String | 是 | 体验金金额 |
| validDays | Integer | 是 | 有效期（天数） |
| maxLeverage | Integer | 是 | 最大杠杆倍数 |
| maxPosition | String | 是 | 最大持仓量 |
| status | String | 否 | 活动状态：ACTIVE, INACTIVE |
| startTime | String | 是 | 活动开始时间 |
| endTime | String | 是 | 活动结束时间 |
| description | String | 否 | 活动描述 |

#### 2.2 获取所有活动

**接口地址**：`GET /api/admin/experience-fund/activity/list`

**请求示例**：
```
GET /api/admin/experience-fund/activity/list
```

#### 2.3 获取活动详情

**接口地址**：`GET /api/admin/experience-fund/activity/{activityId}`

---

### 3. 交易记录管理

#### 3.1 获取所有交易记录

**接口地址**：`GET /api/admin/experience-fund/trade/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| accountId | Long | 否 | 账户ID筛选 |
| contractType | String | 否 | 合约类型筛选 |
| status | String | 否 | 状态筛选 |

**请求示例**：
```
GET /api/admin/experience-fund/trade/list?page=0&size=20
```

#### 3.2 获取交易记录详情

**接口地址**：`GET /api/admin/experience-fund/trade/{tradeId}`

---

### 4. 统计管理

#### 4.1 获取活动统计

**接口地址**：`GET /api/admin/experience-fund/statistics/activity/{activityId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "activityId": 1,
    "activityName": "新用户KYC奖励",
    "totalAccounts": 1000,
    "activeAccounts": 800,
    "expiredAccounts": 200,
    "totalTrades": 5000,
    "totalVolume": "1000000.00000000",
    "averageProfit": "100.00000000"
  }
}
```

#### 4.2 获取平台统计

**接口地址**：`GET /api/admin/experience-fund/statistics/platform`

---

## 合约类型说明

### FUTURES_USDT（U本位合约）
- 以USDT作为保证金
- 支持多倍杠杆
- 支持开多/开空

### FUTURES_COIN（币本位合约）
- 以币种作为保证金
- 支持多倍杠杆
- 支持开多/开空

### PERPETUAL（永续合约）
- 无到期日
- 持续持有

---

## 注意事项

1. **体验金账户有效期**：账户创建后有固定有效期，过期后账户将被禁用
2. **资金限制**：体验金不可提现、不可转账，仅用于模拟交易
3. **交易限制**：受最大杠杆倍数和最大持仓量限制
4. **盈亏计算**：盈亏仅影响体验金账户余额，不影响真实资金

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














