# 模拟交易模块API文档

## 基础信息

- **模块名称**：模拟交易模块（Simulated Trading）
- **用户端基础路径**：`/api/simulated-trading`
- **后台管理基础路径**：`/api/admin/simulated-trading`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 账户管理

#### 1.1 创建模拟交易账户

**接口地址**：`POST /api/simulated-trading/account/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "initialBalance": "10000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| initialBalance | String | 否 | 初始余额（默认10000 USDT） |

**请求示例**：
```json
POST /api/simulated-trading/account/create
Content-Type: application/json

{
  "initialBalance": "10000.00000000"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "账户创建成功",
  "data": {
    "id": 1,
    "accountNo": "ST-20240101120000-123456",
    "userId": 1,
    "balance": "10000.00000000",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 获取我的模拟交易账户

**接口地址**：`GET /api/simulated-trading/account/my-account`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/simulated-trading/account/my-account
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "accountNo": "ST-20240101120000-123456",
    "userId": 1,
    "balance": "10000.00000000",
    "frozenBalance": "0.00000000",
    "status": "ACTIVE",
    "totalProfit": "500.00000000",
    "totalLoss": "200.00000000",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 1.3 重置账户余额

**接口地址**：`POST /api/simulated-trading/account/reset`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "resetBalance": "10000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resetBalance | String | 否 | 重置后的余额（默认10000 USDT） |

**请求示例**：
```json
POST /api/simulated-trading/account/reset
Content-Type: application/json

{
  "resetBalance": "10000.00000000"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "账户重置成功",
  "data": {
    "id": 1,
    "balance": "10000.00000000",
    "resetTime": "2024-01-01T13:00:00"
  }
}
```

---

### 2. 交易管理

#### 2.1 下单

**接口地址**：`POST /api/simulated-trading/trade/place-order`

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
  "tradeType": "SPOT",
  "contractType": null,
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "leverage": 1,
  "quantity": "0.10000000",
  "price": "50000.00000000",
  "stopLossPrice": null,
  "takeProfitPrice": null
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |
| tradeType | String | 是 | 交易类型：SPOT（现货）, FUTURES_USDT（U本位合约）, FUTURES_COIN（币本位合约）, DELIVERY（交割合约）, LEVERAGE（杠杆）, OPTIONS（期权） |
| contractType | String | 否 | 合约类型（用于合约交易） |
| pairName | String | 是 | 交易对 |
| orderType | String | 是 | 订单类型：MARKET（市价）, LIMIT（限价）, STOP_LOSS（止损）, TAKE_PROFIT（止盈） |
| side | String | 是 | 买卖方向：BUY（买入）, SELL（卖出） |
| leverage | Integer | 否 | 杠杆倍数（合约交易必填） |
| quantity | String | 是 | 交易数量 |
| price | String | 否 | 交易价格（限价单必填） |
| stopLossPrice | String | 否 | 止损价格 |
| takeProfitPrice | String | 否 | 止盈价格 |

**请求示例**：
```json
POST /api/simulated-trading/trade/place-order
Content-Type: application/json

{
  "accountId": 1,
  "tradeType": "SPOT",
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
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
    "tradeNo": "ST-20240101120000-123456",
    "accountId": 1,
    "tradeType": "SPOT",
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "quantity": "0.10000000",
    "price": "50000.00000000",
    "status": "FILLED",
    "fillPrice": "50000.00000000",
    "fillQuantity": "0.10000000",
    "profit": "0.00000000",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 平仓（合约交易）

**接口地址**：`POST /api/simulated-trading/trade/close-position/{tradeId}`

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
| exitPrice | String | 是 | 平仓价格 |

**请求示例**：
```
POST /api/simulated-trading/trade/close-position/1?exitPrice=51000.00000000
```

**响应示例**：
```json
{
  "code": 200,
  "message": "平仓成功",
  "data": {
    "id": 1,
    "tradeNo": "ST-20240101120000-123456",
    "status": "CLOSED",
    "exitPrice": "51000.00000000",
    "profit": "100.00000000",
    "closeTime": "2024-01-01T13:00:00"
  }
}
```

#### 2.3 取消订单

**接口地址**：`POST /api/simulated-trading/trade/cancel-order/{tradeId}`

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
POST /api/simulated-trading/trade/cancel-order/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "订单取消成功",
  "data": null
}
```

#### 2.4 获取我的交易记录

**接口地址**：`GET /api/simulated-trading/trade/my-trades`

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
| tradeType | String | 否 | 交易类型筛选 |
| status | String | 否 | 状态筛选：PENDING, FILLED, CANCELLED, CLOSED |

**请求示例**：
```
GET /api/simulated-trading/trade/my-trades?page=0&size=20&tradeType=SPOT
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
        "tradeNo": "ST-20240101120000-123456",
        "tradeType": "SPOT",
        "pairName": "BTC/USDT",
        "orderType": "LIMIT",
        "side": "BUY",
        "quantity": "0.10000000",
        "price": "50000.00000000",
        "status": "FILLED",
        "profit": "0.00000000",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 2.5 获取我的持仓（合约交易）

**接口地址**：`GET /api/simulated-trading/trade/my-open-positions/{accountId}`

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
GET /api/simulated-trading/trade/my-open-positions/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "tradeNo": "ST-20240101120000-123456",
      "tradeType": "FUTURES_USDT",
      "pairName": "BTC/USDT",
      "side": "BUY",
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

#### 1.1 获取所有模拟交易账户

**接口地址**：`GET /api/admin/simulated-trading/account/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| status | String | 否 | 状态筛选：ACTIVE, DISABLED |

**请求示例**：
```
GET /api/admin/simulated-trading/account/list?page=0&size=20
```

#### 1.2 获取账户详情

**接口地址**：`GET /api/admin/simulated-trading/account/{accountId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |

#### 1.3 手动调整账户余额

**接口地址**：`POST /api/admin/simulated-trading/account/adjust-balance/{accountId}`

**请求参数**：
```json
{
  "balance": "15000.00000000",
  "remark": "管理员调整余额"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| balance | String | 是 | 调整后的余额 |
| remark | String | 否 | 备注 |

#### 1.4 重置账户余额（后台）

**接口地址**：`POST /api/admin/simulated-trading/account/reset/{accountId}`

**请求参数**：
```json
{
  "resetBalance": "10000.00000000",
  "remark": "管理员重置账户"
}
```

---

### 2. 交易记录管理

#### 2.1 获取所有交易记录

**接口地址**：`GET /api/admin/simulated-trading/trade/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| accountId | Long | 否 | 账户ID筛选 |
| tradeType | String | 否 | 交易类型筛选 |
| status | String | 否 | 状态筛选 |

**请求示例**：
```
GET /api/admin/simulated-trading/trade/list?page=0&size=20&tradeType=SPOT
```

#### 2.2 获取交易记录详情

**接口地址**：`GET /api/admin/simulated-trading/trade/{tradeId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tradeId | Long | 是 | 交易ID |

---

### 3. 规则配置管理

#### 3.1 获取模拟交易规则配置

**接口地址**：`GET /api/admin/simulated-trading/rule/list`

**请求示例**：
```
GET /api/admin/simulated-trading/rule/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "ruleType": "INITIAL_BALANCE",
      "ruleValue": "10000.00000000",
      "description": "初始账户余额"
    },
    {
      "id": 2,
      "ruleType": "MAX_LEVERAGE",
      "ruleValue": "10",
      "description": "最大杠杆倍数"
    }
  ]
}
```

#### 3.2 更新规则配置

**接口地址**：`PUT /api/admin/simulated-trading/rule/update/{ruleId}`

**请求参数**：
```json
{
  "ruleValue": "20000.00000000",
  "description": "更新初始账户余额"
}
```

---

### 4. 统计管理

#### 4.1 获取平台统计

**接口地址**：`GET /api/admin/simulated-trading/statistics/platform`

**请求示例**：
```
GET /api/admin/simulated-trading/statistics/platform
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalAccounts": 1000,
    "activeAccounts": 800,
    "totalTrades": 50000,
    "totalVolume": "10000000.00000000",
    "averageProfit": "100.00000000"
  }
}
```

#### 4.2 获取用户统计

**接口地址**：`GET /api/admin/simulated-trading/statistics/user/{userId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "userId": 1,
    "totalTrades": 100,
    "totalProfit": "500.00000000",
    "winRate": "0.60000000",
    "averageProfit": "5.00000000",
    "maxDrawdown": "200.00000000"
  }
}
```

---

## 交易类型说明

### 现货交易（SPOT）
- 买卖数字资产
- 无杠杆
- 实时成交

### U本位合约（FUTURES_USDT）
- 以USDT作为保证金的永续合约
- 支持杠杆
- 支持开多/开空

### 币本位合约（FUTURES_COIN）
- 以币种作为保证金的永续合约
- 支持杠杆
- 支持开多/开空

### 交割合约（DELIVERY）
- 有到期日的合约
- 到期自动交割

### 杠杆交易（LEVERAGE）
- 现货杠杆交易
- 支持多倍杠杆

### 期权交易（OPTIONS）
- 看涨/看跌期权
- 支持多种策略

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














