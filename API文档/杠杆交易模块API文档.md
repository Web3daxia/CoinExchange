# 杠杆交易模块 API 文档

## 模块说明
杠杆交易允许用户通过借贷机制提高交易资金的倍数，从而放大收益，但也同样增加了风险。平台提供了不同的杠杆倍数，用户可以选择不同的杠杆进行现货交易，并需支付相应的借款利息。平台需要有效的风险控制机制来防止用户亏损过大。

## Base URL
```
/leveraged
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 交易对管理

### 1.1 获取杠杆交易对列表

**接口地址**: `GET /leveraged/pairs`

**接口描述**: 获取支持杠杆交易的交易对列表，支持多种筛选条件

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| baseCurrency | String | 否 | 基础币种筛选，如: BTC, ETH |
| quoteCurrency | String | 否 | 计价货币筛选，如: USDT |
| status | String | 否 | 状态筛选: ACTIVE（启用）, INACTIVE（禁用）。不传则返回所有状态 |
| enabled | Boolean | 否 | 是否启用筛选: true, false |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /leveraged/pairs?baseCurrency=BTC&status=ACTIVE&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "contractName": "BTC/USDT杠杆交易",
      "baseCurrency": "BTC",
      "quoteCurrency": "USDT",
      "maxLeverage": 100.00,
      "leverageType": "DISCRETE",
      "leverageMultipliers": "2,3,5,10,20,50,100",
      "availableLeverages": [2, 3, 5, 10, 20, 50, 100],
      "maintenanceMarginRate": 0.005000,
      "initialMarginRate": 0.010000,
      "openLongEnabled": true,
      "openShortEnabled": true,
      "marketOpenLongEnabled": true,
      "marketOpenShortEnabled": true,
      "stopOrderEnabled": true,
      "advancedOrderEnabled": true,
      "tradeable": true,
      "openFeeRate": 0.000200,
      "closeFeeRate": 0.000200,
      "makerFeeRate": 0.000200,
      "takerFeeRate": 0.000500,
      "interestRate": 0.000100,
      "interestSettlementCycle": 8,
      "minQuantity": 0.00100000,
      "maxQuantity": 1000.00000000,
      "status": "ACTIVE",
      "enabled": true,
      "visible": true,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    },
    {
      "id": 2,
      "pairName": "ETH/USDT",
      "contractName": "ETH/USDT杠杆交易",
      "baseCurrency": "ETH",
      "quoteCurrency": "USDT",
      "maxLeverage": 50.00,
      "leverageType": "DISCRETE",
      "leverageMultipliers": "2,3,5,10,20,50",
      "availableLeverages": [2, 3, 5, 10, 20, 50],
      "maintenanceMarginRate": 0.005000,
      "initialMarginRate": 0.010000,
      "openLongEnabled": true,
      "openShortEnabled": true,
      "marketOpenLongEnabled": true,
      "marketOpenShortEnabled": true,
      "stopOrderEnabled": true,
      "advancedOrderEnabled": true,
      "tradeable": true,
      "openFeeRate": 0.000200,
      "closeFeeRate": 0.000200,
      "makerFeeRate": 0.000200,
      "takerFeeRate": 0.000500,
      "interestRate": 0.000100,
      "interestSettlementCycle": 8,
      "minQuantity": 0.01000000,
      "maxQuantity": 5000.00000000,
      "status": "ACTIVE",
      "enabled": true,
      "visible": true,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

---

### 1.2 获取交易对详情

**接口地址**: `GET /leveraged/pairs/{pairName}`

**接口描述**: 获取指定交易对的详细信息，包括杠杆倍数、费率、保证金率等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称，如: BTC/USDT |

**请求示例**:
```
GET /leveraged/pairs/BTC%2FUSDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "pairName": "BTC/USDT",
    "contractName": "BTC/USDT杠杆交易",
    "baseCurrency": "BTC",
    "quoteCurrency": "USDT",
    "maxLeverage": 100.00,
    "leverageType": "DISCRETE",
    "leverageMultipliers": "2,3,5,10,20,50,100",
    "availableLeverages": [2, 3, 5, 10, 20, 50, 100],
    "maintenanceMarginRate": 0.005000,
    "initialMarginRate": 0.010000,
    "openLongEnabled": true,
    "openShortEnabled": true,
    "marketOpenLongEnabled": true,
    "marketOpenShortEnabled": true,
    "stopOrderEnabled": true,
    "advancedOrderEnabled": true,
    "tradeable": true,
    "openFeeRate": 0.000200,
    "closeFeeRate": 0.000200,
    "makerFeeRate": 0.000200,
    "takerFeeRate": 0.000500,
    "interestRate": 0.000100,
    "interestSettlementCycle": 8,
    "minQuantity": 0.00100000,
    "maxQuantity": 1000.00000000,
    "status": "ACTIVE",
    "enabled": true,
    "visible": true,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
}
```

---

### 1.3 获取杠杆交易市场数据

**接口地址**: `GET /leveraged/market/{pairName}`

**接口描述**: 获取指定交易对的杠杆交易市场数据，包括价格、可用杠杆倍数等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称，如: BTC/USDT |

**请求示例**:
```
GET /leveraged/market/BTC%2FUSDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "currentPrice": 50000.00000000,
    "priceChange24h": 0.0250,
    "high24h": 51000.00000000,
    "low24h": 49000.00000000,
    "volume24h": 1000.50000000,
    "availableLeverage": [2, 3, 5, 10, 20, 50, 100],
    "maxLeverage": 100,
    "interestRate": 0.000100,
    "interestSettlementCycle": 8
  }
}
```

---

---

## 2. 订单管理

### 2.1 创建杠杆订单（通用接口）

**接口地址**: `POST /leveraged/order`

**接口描述**: 创建杠杆交易订单，支持多种订单类型（市价单、限价单、止损限价单、条件单等）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "action": "OPEN",
  "quantity": 0.1,
  "price": 50000.00,
  "leverage": 10,
  "stopPrice": null,
  "triggerPrice": null,
  "conditionType": null
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型: MARKET（市价单）, LIMIT（限价单）, STOP_LIMIT（止损限价单）, CONDITIONAL（条件单） |
| side | String | 是 | 方向: BUY（买入）, SELL（卖出） |
| action | String | 是 | 操作: OPEN（开仓）, CLOSE（平仓） |
| quantity | BigDecimal | 是 | 数量 |
| price | BigDecimal | 否 | 限价价格（限价单、止损限价单、条件单必填） |
| leverage | Integer | 是 | 杠杆倍数 |
| stopPrice | BigDecimal | 否 | 止损价格（止损限价单必填） |
| triggerPrice | BigDecimal | 否 | 触发价格（条件单必填） |
| conditionType | String | 否 | 条件类型（条件单必填）: GREATER_THAN（大于）, LESS_THAN（小于） |

**响应示例**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "action": "OPEN",
    "quantity": 0.10000000,
    "price": 50000.00000000,
    "stopPrice": null,
    "takeProfitPrice": null,
    "triggerPrice": null,
    "conditionType": null,
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "filledAmount": 0.00000000,
    "fee": 0.00000000,
    "status": "PENDING",
    "filledAt": null,
    "triggeredAt": null,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.2 提交市价单

**接口地址**: `POST /leveraged/order/market`

**接口描述**: 提交市价单，立即按市场价格成交

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "action": "OPEN",
  "quantity": 0.1,
  "leverage": 10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 方向: BUY, SELL |
| action | String | 是 | 操作: OPEN, CLOSE |
| quantity | BigDecimal | 是 | 数量 |
| leverage | Integer | 是 | 杠杆倍数 |

**响应示例**:
```json
{
  "code": 200,
  "message": "市价单创建成功",
  "data": {
    "id": 1002,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "MARKET",
    "side": "BUY",
    "action": "OPEN",
    "quantity": 0.10000000,
    "price": null,
    "leverage": 10,
    "filledQuantity": 0.10000000,
    "filledAmount": 5000.00000000,
    "fee": 2.50000000,
    "status": "FILLED",
    "filledAt": "2024-01-16T10:00:05",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:05"
  }
}
```

---

### 2.3 提交限价单

**接口地址**: `POST /leveraged/order/limit`

**接口描述**: 提交限价单，达到指定价格时成交

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "action": "OPEN",
  "quantity": 0.1,
  "price": 50000.00,
  "leverage": 10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 方向: BUY, SELL |
| action | String | 是 | 操作: OPEN, CLOSE |
| quantity | BigDecimal | 是 | 数量 |
| price | BigDecimal | 是 | 限价价格 |
| leverage | Integer | 是 | 杠杆倍数 |

**响应示例**:
```json
{
  "code": 200,
  "message": "限价单创建成功",
  "data": {
    "id": 1003,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "action": "OPEN",
    "quantity": 0.10000000,
    "price": 50000.00000000,
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "filledAmount": 0.00000000,
    "fee": 0.00000000,
    "status": "PENDING",
    "filledAt": null,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.4 提交止损单

**接口地址**: `POST /leveraged/order/stop-loss`

**接口描述**: 为指定持仓设置止损单，价格触及止损价时自动平仓

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |
| stopPrice | BigDecimal | 是 | 止损价格 |

**请求示例**:
```
POST /leveraged/order/stop-loss?positionId=2001&stopPrice=49000.00
```

**响应示例**:
```json
{
  "code": 200,
  "message": "止损单创建成功",
  "data": {
    "id": 1004,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "STOP_LOSS",
    "side": "SELL",
    "action": "CLOSE",
    "quantity": 0.10000000,
    "price": null,
    "stopPrice": 49000.00000000,
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "status": "PENDING",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.5 提交止盈单

**接口地址**: `POST /leveraged/order/take-profit`

**接口描述**: 为指定持仓设置止盈单，价格触及止盈价时自动平仓

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |
| takeProfitPrice | BigDecimal | 是 | 止盈价格 |

**请求示例**:
```
POST /leveraged/order/take-profit?positionId=2001&takeProfitPrice=51000.00
```

**响应示例**:
```json
{
  "code": 200,
  "message": "止盈单创建成功",
  "data": {
    "id": 1005,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "TAKE_PROFIT",
    "side": "SELL",
    "action": "CLOSE",
    "quantity": 0.10000000,
    "price": null,
    "takeProfitPrice": 51000.00000000,
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "status": "PENDING",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.6 提交止损限价单

**接口地址**: `POST /leveraged/order/stop-limit`

**接口描述**: 提交止损限价单，价格触及止损价时提交限价单

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "action": "OPEN",
  "quantity": 0.1,
  "stopPrice": 49000.00,
  "price": 48900.00,
  "leverage": 10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 方向: BUY, SELL |
| action | String | 是 | 操作: OPEN, CLOSE |
| quantity | BigDecimal | 是 | 数量 |
| stopPrice | BigDecimal | 是 | 止损价格（触发价格） |
| price | BigDecimal | 是 | 限价价格 |
| leverage | Integer | 是 | 杠杆倍数 |

**响应示例**:
```json
{
  "code": 200,
  "message": "止损限价单创建成功",
  "data": {
    "id": 1006,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "STOP_LIMIT",
    "side": "BUY",
    "action": "OPEN",
    "quantity": 0.10000000,
    "price": 48900.00000000,
    "stopPrice": 49000.00000000,
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "status": "PENDING",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.7 提交条件单

**接口地址**: `POST /leveraged/order/conditional`

**接口描述**: 提交条件单，满足条件时自动提交订单

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "action": "OPEN",
  "quantity": 0.1,
  "triggerPrice": 51000.00,
  "conditionType": "GREATER_THAN",
  "price": 51000.00,
  "leverage": 10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 方向: BUY, SELL |
| action | String | 是 | 操作: OPEN, CLOSE |
| quantity | BigDecimal | 是 | 数量 |
| triggerPrice | BigDecimal | 是 | 触发价格 |
| conditionType | String | 是 | 条件类型: GREATER_THAN（大于）, LESS_THAN（小于） |
| price | BigDecimal | 是 | 限价价格 |
| leverage | Integer | 是 | 杠杆倍数 |

**响应示例**:
```json
{
  "code": 200,
  "message": "条件单创建成功",
  "data": {
    "id": 1007,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "orderType": "CONDITIONAL",
    "side": "BUY",
    "action": "OPEN",
    "quantity": 0.10000000,
    "price": 51000.00000000,
    "triggerPrice": 51000.00000000,
    "conditionType": "GREATER_THAN",
    "leverage": 10,
    "filledQuantity": 0.00000000,
    "status": "PENDING",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.8 查询订单历史

**接口地址**: `GET /leveraged/order/history`

**接口描述**: 查询用户的杠杆交易订单历史，支持多种筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| orderType | String | 否 | 订单类型筛选: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT, STOP_LIMIT, CONDITIONAL |
| side | String | 否 | 方向筛选: BUY, SELL |
| action | String | 否 | 操作筛选: OPEN, CLOSE |
| status | String | 否 | 状态筛选: PENDING, FILLED, PARTIALLY_FILLED, CANCELLED, REJECTED, TRIGGERED |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /leveraged/order/history?pairName=BTC/USDT&status=FILLED&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1001,
      "userId": 1,
      "accountId": 1,
      "pairName": "BTC/USDT",
      "orderType": "LIMIT",
      "side": "BUY",
      "action": "OPEN",
      "quantity": 0.10000000,
      "price": 50000.00000000,
      "leverage": 10,
      "filledQuantity": 0.10000000,
      "filledAmount": 5000.00000000,
      "fee": 2.50000000,
      "status": "FILLED",
      "filledAt": "2024-01-16T10:05:00",
      "createdAt": "2024-01-16T10:00:00",
      "updatedAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

---

## 3. 持仓管理

### 3.1 查询持仓列表

**接口地址**: `GET /leveraged/account/positions`

**接口描述**: 查询用户当前的所有持仓，支持筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| side | String | 否 | 方向筛选: LONG（做多）, SHORT（做空） |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, CLOSED（已平仓）, LIQUIDATED（已强平） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /leveraged/account/positions?pairName=BTC/USDT&status=ACTIVE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 2001,
      "userId": 1,
      "accountId": 1,
      "pairName": "BTC/USDT",
      "side": "LONG",
      "quantity": 0.10000000,
      "entryPrice": 50000.00000000,
      "currentPrice": 50500.00000000,
      "leverage": 10,
      "margin": 500.00000000,
      "initialMargin": 500.00000000,
      "maintenanceMargin": 250.00000000,
      "borrowedAmount": 4500.00000000,
      "unrealizedPnl": 50.00000000,
      "realizedPnl": 0.00000000,
      "liquidationPrice": 45000.00000000,
      "marginRatio": 2.2000,
      "stopLossPrice": 49000.00000000,
      "takeProfitPrice": 51000.00000000,
      "status": "ACTIVE",
      "createdAt": "2024-01-16T10:00:00",
      "updatedAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

### 3.2 查询账户风险信息

**接口地址**: `GET /leveraged/account/risk`

**接口描述**: 查询账户风险信息，包括保证金、杠杆、可用资金、风险等级等

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选。不传则返回所有交易对的汇总 |

**请求示例**:
```
GET /leveraged/account/risk?pairName=BTC/USDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "pairName": "BTC/USDT",
    "accountId": 1,
    "availableBalance": 9500.00000000,
    "borrowedAmount": 4500.00000000,
    "totalMargin": 500.00000000,
    "usedMargin": 500.00000000,
    "availableMargin": 0.00000000,
    "initialMargin": 500.00000000,
    "maintenanceMargin": 250.00000000,
    "marginRatio": 2.2000,
    "leverage": 10,
    "maxLeverage": 100,
    "unrealizedPnl": 50.00000000,
    "riskLevel": "LOW",
    "liquidationRisk": false,
    "positions": [
      {
        "positionId": 2001,
        "pairName": "BTC/USDT",
        "side": "LONG",
        "marginRatio": 2.2000,
        "liquidationPrice": 45000.00000000,
        "currentPrice": 50500.00000000,
        "distanceToLiquidation": 5500.00000000
      }
    ]
  }
}
```

---

### 3.3 补充保证金

**接口地址**: `POST /leveraged/account/top-up`

**接口描述**: 为指定持仓补充保证金，降低强平风险

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "positionId": 2001,
  "amount": 100.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| positionId | Long | 是 | 持仓ID |
| amount | BigDecimal | 是 | 补充保证金金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "保证金补充成功",
  "data": {
    "id": 2001,
    "userId": 1,
    "accountId": 1,
    "pairName": "BTC/USDT",
    "side": "LONG",
    "quantity": 0.10000000,
    "entryPrice": 50000.00000000,
    "currentPrice": 50500.00000000,
    "leverage": 10,
    "margin": 600.00000000,
    "initialMargin": 500.00000000,
    "maintenanceMargin": 300.00000000,
    "borrowedAmount": 4500.00000000,
    "unrealizedPnl": 50.00000000,
    "realizedPnl": 0.00000000,
    "liquidationPrice": 45000.00000000,
    "marginRatio": 2.6000,
    "status": "ACTIVE",
    "updatedAt": "2024-01-16T10:10:00"
  }
}
```

---

### 3.4 强制平仓

**接口地址**: `POST /leveraged/account/liquidate`

**接口描述**: 手动触发强制平仓（仅当保证金不足时系统自动触发，用户也可手动触发）

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |

**请求示例**:
```
POST /leveraged/account/liquidate?positionId=2001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "强平成功",
  "data": null
}
```

---

## 4. 账户与资金管理

### 4.1 查询杠杆账户信息

**接口地址**: `GET /leveraged/account/info`

**接口描述**: 查询用户的杠杆账户信息，包括余额、借款金额、保证金等

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选。不传则返回所有交易对的汇总 |

**请求示例**:
```
GET /leveraged/account/info?pairName=BTC/USDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 1,
    "pairName": "BTC/USDT",
    "leverage": 10,
    "maxLeverage": 100,
    "availableBalance": 9500.00000000,
    "borrowedAmount": 4500.00000000,
    "margin": 500.00000000,
    "initialMargin": 500.00000000,
    "maintenanceMargin": 250.00000000,
    "marginRatio": 2.2000,
    "interestRate": 0.000100,
    "status": "ACTIVE",
    "createdAt": "2024-01-16T09:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.2 调整杠杆倍数

**接口地址**: `POST /leveraged/account/leverage`

**接口描述**: 调整用户账户的杠杆倍数（仅未开仓时可调整）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "leverage": 20
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 新的杠杆倍数 |

**响应示例**:
```json
{
  "code": 200,
  "message": "杠杆倍数调整成功",
  "data": {
    "id": 1,
    "userId": 1,
    "pairName": "BTC/USDT",
    "leverage": 20,
    "maxLeverage": 100,
    "availableBalance": 9500.00000000,
    "borrowedAmount": 0.00000000,
    "margin": 0.00000000,
    "status": "ACTIVE",
    "updatedAt": "2024-01-16T10:15:00"
  }
}
```

---

### 4.3 查询杠杆倍数

**接口地址**: `GET /leveraged/account/leverage`

**接口描述**: 查询用户当前杠杆倍数

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |

**请求示例**:
```
GET /leveraged/account/leverage?pairName=BTC/USDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 1,
    "pairName": "BTC/USDT",
    "leverage": 10,
    "maxLeverage": 100,
    "status": "ACTIVE"
  }
}
```

---

### 4.4 转账到杠杆账户

**接口地址**: `POST /leveraged/account/transfer/in`

**接口描述**: 从现货账户转账到杠杆账户

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "currency": "USDT",
  "amount": 1000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| currency | String | 是 | 币种，如: USDT, BTC |
| amount | BigDecimal | 是 | 转账金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "转账成功",
  "data": {
    "transferId": "TF20240116100001",
    "pairName": "BTC/USDT",
    "currency": "USDT",
    "amount": 1000.00000000,
    "fromAccount": "SPOT",
    "toAccount": "LEVERAGED",
    "status": "SUCCESS",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.5 从杠杆账户转出

**接口地址**: `POST /leveraged/account/transfer/out`

**接口描述**: 从杠杆账户转账到现货账户（需确保无未平仓持仓或保证金充足）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "currency": "USDT",
  "amount": 500.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| currency | String | 是 | 币种 |
| amount | BigDecimal | 是 | 转账金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "转账成功",
  "data": {
    "transferId": "TF20240116100002",
    "pairName": "BTC/USDT",
    "currency": "USDT",
    "amount": 500.00000000,
    "fromAccount": "LEVERAGED",
    "toAccount": "SPOT",
    "status": "SUCCESS",
    "createdAt": "2024-01-16T10:05:00"
  }
}
```

---

---

## 5. 借款与利息管理

### 5.1 查询借款记录

**接口地址**: `GET /leveraged/account/borrow/history`

**接口描述**: 查询用户的借款历史记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /leveraged/account/borrow/history?pairName=BTC/USDT&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "borrowId": "BR20240116100001",
      "userId": 1,
      "pairName": "BTC/USDT",
      "borrowedAmount": 4500.00000000,
      "interestRate": 0.000100,
      "borrowTime": "2024-01-16T10:00:00",
      "repayTime": null,
      "status": "ACTIVE",
      "totalInterest": 0.36000000
    }
  ]
}
```

---

### 5.2 查询利息记录

**接口地址**: `GET /leveraged/account/interest/history`

**接口描述**: 查询用户的借款利息记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| settlementCycle | String | 否 | 结算周期筛选: HOURLY（每小时）, DAILY（每天）, WEEKLY（每周） |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /leveraged/account/interest/history?pairName=BTC/USDT&settlementCycle=HOURLY&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "interestId": "INT20240116100001",
      "userId": 1,
      "pairName": "BTC/USDT",
      "borrowedAmount": 4500.00000000,
      "interestRate": 0.000100,
      "interestAmount": 0.36000000,
      "settlementCycle": "HOURLY",
      "settlementTime": "2024-01-16T11:00:00",
      "status": "SETTLED"
    },
    {
      "interestId": "INT20240116100002",
      "userId": 1,
      "pairName": "BTC/USDT",
      "borrowedAmount": 4500.00000000,
      "interestRate": 0.000100,
      "interestAmount": 0.36000000,
      "settlementCycle": "HOURLY",
      "settlementTime": "2024-01-16T12:00:00",
      "status": "SETTLED"
    }
  ]
}
```

---

### 5.3 查询实时利息预览

**接口地址**: `GET /leveraged/account/interest/preview`

**接口描述**: 查询当前借款的实时利息预览（未结算）

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选。不传则返回所有交易对的汇总 |

**请求示例**:
```
GET /leveraged/account/interest/preview?pairName=BTC/USDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "pairName": "BTC/USDT",
    "borrowedAmount": 4500.00000000,
    "interestRate": 0.000100,
    "currentInterest": 0.18000000,
    "nextSettlementTime": "2024-01-16T13:00:00",
    "estimatedDailyInterest": 10.80000000,
    "estimatedWeeklyInterest": 75.60000000,
    "estimatedMonthlyInterest": 324.00000000
  }
}
```

---

## 6. 风控管理

### 6.1 设置风险警报

**接口地址**: `POST /leveraged/account/risk-alert`

**接口描述**: 设置风险警报，当账户风险达到阈值时触发提醒

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 否 | 账户ID（可选） |
| positionId | Long | 否 | 持仓ID（可选） |
| alertType | String | 是 | 警报类型: MARGIN_RATIO_LOW（保证金率低）, LIQUIDATION_RISK（强平风险）, LEVERAGE_HIGH（杠杆过高） |
| threshold | BigDecimal | 否 | 阈值（绝对值） |
| thresholdPercentage | BigDecimal | 否 | 阈值（百分比） |
| notificationMethod | String | 否 | 通知方式: EMAIL（邮件）, SMS（短信）, PUSH（推送）, ALL（全部） |

**请求示例**:
```
POST /leveraged/account/risk-alert?alertType=MARGIN_RATIO_LOW&thresholdPercentage=0.15&notificationMethod=ALL
```

**响应示例**:
```json
{
  "code": 200,
  "message": "风险警报设置成功",
  "data": {
    "id": 3001,
    "userId": 1,
    "accountId": 1,
    "positionId": null,
    "alertType": "MARGIN_RATIO_LOW",
    "threshold": null,
    "thresholdPercentage": 0.1500,
    "notificationMethod": "ALL",
    "status": "ACTIVE",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 6.2 查询风险警报列表

**接口地址**: `GET /leveraged/account/risk-alerts`

**接口描述**: 查询用户设置的风险警报列表

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| alertType | String | 否 | 警报类型筛选 |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, TRIGGERED（已触发）, DISABLED（已禁用） |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /leveraged/account/risk-alerts?status=ACTIVE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 3001,
      "userId": 1,
      "accountId": 1,
      "positionId": null,
      "alertType": "MARGIN_RATIO_LOW",
      "threshold": null,
      "thresholdPercentage": 0.1500,
      "notificationMethod": "ALL",
      "status": "ACTIVE",
      "triggeredAt": null,
      "createdAt": "2024-01-16T10:00:00"
    }
  ]
}
```

---

### 6.3 查询强平记录

**接口地址**: `GET /leveraged/account/liquidation/history`

**接口描述**: 查询用户的强平历史记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /leveraged/account/liquidation/history?pairName=BTC/USDT&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "liquidationId": "LIQ20240115100001",
      "userId": 1,
      "positionId": 2002,
      "pairName": "BTC/USDT",
      "liquidationPrice": 45000.00000000,
      "liquidationQuantity": 0.10000000,
      "liquidationPnl": -500.00000000,
      "reason": "MARGIN_INSUFFICIENT",
      "liquidationTime": "2024-01-15T10:00:00"
    }
  ]
}
```

---

---

## 7. 交易策略管理

### 7.1 配置交易策略

**接口地址**: `POST /leveraged/strategy/configure`

**接口描述**: 配置杠杆交易策略（网格交易、趋势跟踪、反转策略等）

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |
| strategyName | String | 否 | 策略名称 |
| strategyType | String | 是 | 策略类型: GRID（网格交易）, TREND_FOLLOWING（趋势跟踪）, REVERSE（反转策略） |
| pairName | String | 是 | 交易对名称 |
| strategyParams | String | 否 | 策略参数（JSON字符串） |
| leverage | Integer | 是 | 杠杆倍数 |

**请求示例**:
```
POST /leveraged/strategy/configure?accountId=1&strategyType=GRID&pairName=BTC/USDT&leverage=10&strategyParams={"gridCount":10,"upperPrice":51000,"lowerPrice":49000}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略配置成功",
  "data": {
    "id": 4001,
    "userId": 1,
    "accountId": 1,
    "strategyName": "BTC/USDT网格策略",
    "strategyType": "GRID",
    "pairName": "BTC/USDT",
    "strategyParams": "{\"gridCount\":10,\"upperPrice\":51000,\"lowerPrice\":49000}",
    "leverage": 10,
    "status": "INACTIVE",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 7.2 执行交易策略

**接口地址**: `POST /leveraged/strategy/execute`

**接口描述**: 执行已配置的交易策略

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求示例**:
```
POST /leveraged/strategy/execute?strategyId=4001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略执行成功",
  "data": null
}
```

---

### 7.3 停止交易策略

**接口地址**: `POST /leveraged/strategy/stop`

**接口描述**: 停止正在执行的交易策略

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求示例**:
```
POST /leveraged/strategy/stop?strategyId=4001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略停止成功",
  "data": null
}
```

---

### 7.4 查询策略列表

**接口地址**: `GET /leveraged/strategy/list`

**接口描述**: 查询用户的交易策略列表

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| strategyType | String | 否 | 策略类型筛选 |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, INACTIVE（未激活）, STOPPED（已停止） |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /leveraged/strategy/list?pairName=BTC/USDT&status=ACTIVE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 4001,
      "userId": 1,
      "accountId": 1,
      "strategyName": "BTC/USDT网格策略",
      "strategyType": "GRID",
      "pairName": "BTC/USDT",
      "strategyParams": "{\"gridCount\":10,\"upperPrice\":51000,\"lowerPrice\":49000}",
      "leverage": 10,
      "status": "ACTIVE",
      "createdAt": "2024-01-16T10:00:00",
      "updatedAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

## 8. 交易机器人管理

### 8.1 配置交易机器人

**接口地址**: `POST /leveraged/robot/configure`

**接口描述**: 配置交易机器人（与策略配置类似，但用于自动化交易）

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |
| strategyName | String | 否 | 机器人名称 |
| strategyType | String | 是 | 策略类型 |
| pairName | String | 是 | 交易对名称 |
| strategyParams | String | 否 | 策略参数（JSON字符串） |
| leverage | Integer | 是 | 杠杆倍数 |

**请求示例**:
```
POST /leveraged/robot/configure?accountId=1&strategyType=TREND_FOLLOWING&pairName=BTC/USDT&leverage=10
```

**响应示例**:
```json
{
  "code": 200,
  "message": "交易机器人配置成功",
  "data": {
    "id": 5001,
    "userId": 1,
    "accountId": 1,
    "strategyName": "BTC/USDT趋势跟踪机器人",
    "strategyType": "TREND_FOLLOWING",
    "pairName": "BTC/USDT",
    "strategyParams": null,
    "leverage": 10,
    "status": "INACTIVE",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 8.2 启动交易机器人

**接口地址**: `POST /leveraged/robot/start`

**接口描述**: 启动交易机器人

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID（机器人ID） |

**请求示例**:
```
POST /leveraged/robot/start?strategyId=5001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "交易机器人启动成功",
  "data": null
}
```

---

### 8.3 停止交易机器人

**接口地址**: `POST /leveraged/robot/stop`

**接口描述**: 停止交易机器人

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID（机器人ID） |

**请求示例**:
```
POST /leveraged/robot/stop?strategyId=5001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "交易机器人停止成功",
  "data": null
}
```

---

---

## 9. 后台管理接口

### 9.1 交易对管理

#### 9.1.1 创建杠杆交易对

**接口地址**: `POST /admin/leveraged/pairs`

**接口描述**: 后台创建新的杠杆交易对

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "contractName": "BTC/USDT杠杆交易",
  "baseCurrency": "BTC",
  "quoteCurrency": "USDT",
  "maxLeverage": 100.00,
  "leverageType": "DISCRETE",
  "leverageMultipliers": "2,3,5,10,20,50,100",
  "maintenanceMarginRate": 0.005000,
  "initialMarginRate": 0.010000,
  "openFeeRate": 0.000200,
  "closeFeeRate": 0.000200,
  "makerFeeRate": 0.000200,
  "takerFeeRate": 0.000500,
  "interestRate": 0.000100,
  "interestSettlementCycle": 8,
  "minQuantity": 0.00100000,
  "maxQuantity": 1000.00000000
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "交易对创建成功",
  "data": {
    "id": 10,
    "pairName": "BTC/USDT",
    "maxLeverage": 100.00,
    "status": "ACTIVE",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

#### 9.1.2 更新杠杆交易对

**接口地址**: `PUT /admin/leveraged/pairs/{id}`

**接口描述**: 后台更新杠杆交易对配置

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 交易对ID |

**请求参数**:
```json
{
  "maxLeverage": 50.00,
  "interestRate": 0.000150,
  "status": "ACTIVE"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "交易对更新成功",
  "data": {
    "id": 1,
    "pairName": "BTC/USDT",
    "maxLeverage": 50.00,
    "interestRate": 0.000150,
    "status": "ACTIVE",
    "updatedAt": "2024-01-16T10:05:00"
  }
}
```

---

#### 9.1.3 删除杠杆交易对

**接口地址**: `DELETE /admin/leveraged/pairs/{id}`

**接口描述**: 后台删除杠杆交易对（仅允许删除未激活的交易对）

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 交易对ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "交易对删除成功",
  "data": null
}
```

---

### 9.2 订单管理（后台）

#### 9.2.1 查询所有订单

**接口地址**: `GET /admin/leveraged/orders`

**接口描述**: 后台查询所有用户的订单列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1001,
        "userId": 1,
        "pairName": "BTC/USDT",
        "orderType": "LIMIT",
        "status": "FILLED",
        "createdAt": "2024-01-16T10:00:00"
      }
    ],
    "totalElements": 1000,
    "totalPages": 50
  }
}
```

---

### 9.3 持仓管理（后台）

#### 9.3.1 查询所有持仓

**接口地址**: `GET /admin/leveraged/positions`

**接口描述**: 后台查询所有用户的持仓列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 2001,
        "userId": 1,
        "pairName": "BTC/USDT",
        "side": "LONG",
        "quantity": 0.10000000,
        "status": "ACTIVE"
      }
    ],
    "totalElements": 500,
    "totalPages": 25
  }
}
```

---

### 9.4 数据统计

#### 9.4.1 交易量统计

**接口地址**: `GET /admin/leveraged/statistics/volume`

**接口描述**: 查询杠杆交易量统计

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期: HOUR, DAY, WEEK, MONTH（默认DAY） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "DAY",
    "totalVolume": 10000.50000000,
    "totalAmount": 500000000.00000000,
    "totalTrades": 5000,
    "details": [
      {
        "date": "2024-01-16",
        "volume": 500.30000000,
        "amount": 25000000.00000000,
        "trades": 250
      }
    ]
  }
}
```

---

#### 9.4.2 盈亏统计

**接口地址**: `GET /admin/leveraged/statistics/pnl`

**接口描述**: 查询杠杆交易盈亏统计

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "DAY",
    "totalRealizedPnl": 100000.00000000,
    "totalFees": 50000.00000000,
    "totalInterest": 20000.00000000,
    "netProfit": 30000.00000000,
    "profitableUsers": 300,
    "losingUsers": 200
  }
}
```

---

#### 9.4.3 用户风险统计

**接口地址**: `GET /admin/leveraged/statistics/risk`

**接口描述**: 查询用户风险统计

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalUsers": 1000,
    "activeUsers": 800,
    "highRiskUsers": 50,
    "totalLiquidations": 100,
    "totalLiquidationAmount": 100000.00000000,
    "averageMarginRatio": 0.5000,
    "riskDistribution": {
      "LOW": 700,
      "MEDIUM": 50,
      "HIGH": 50
    }
  }
}
```

---

#### 9.4.4 手动强制平仓

**接口地址**: `POST /admin/leveraged/risk/liquidation/manual`

**接口描述**: 后台手动触发强制平仓（紧急情况使用）

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "positionId": 2001,
  "reason": "MANUAL_INTERVENTION"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "强制平仓执行成功",
  "data": {
    "liquidationId": "LIQ20240116100001",
    "positionId": 2001,
    "liquidationPrice": 45000.00000000,
    "liquidationTime": "2024-01-16T10:10:00"
  }
}
```

---

---

## 10. 数据字典

### 10.1 订单类型（orderType）
- `MARKET`：市价单
- `LIMIT`：限价单
- `STOP_LOSS`：止损单
- `TAKE_PROFIT`：止盈单
- `STOP_LIMIT`：止损限价单
- `CONDITIONAL`：条件单

### 10.2 订单方向（side）
- `BUY`：买入
- `SELL`：卖出

### 10.3 订单操作（action）
- `OPEN`：开仓
- `CLOSE`：平仓

### 10.4 订单状态（status）
- `PENDING`：待成交
- `FILLED`：已成交
- `PARTIALLY_FILLED`：部分成交
- `CANCELLED`：已取消
- `REJECTED`：已拒绝
- `TRIGGERED`：已触发（条件单）

### 10.5 持仓方向（side）
- `LONG`：做多
- `SHORT`：做空

### 10.6 持仓状态（status）
- `ACTIVE`：活跃
- `CLOSED`：已平仓
- `LIQUIDATED`：已强平

### 10.7 账户状态（status）
- `ACTIVE`：活跃
- `FROZEN`：冻结
- `CLOSED`：关闭

### 10.8 交易对状态（status）
- `ACTIVE`：启用
- `INACTIVE`：禁用

### 10.9 杠杆类型（leverageType）
- `DISCRETE`：离散杠杆（固定倍数，如2x, 5x, 10x）
- `CONTINUOUS`：连续杠杆（任意倍数）

### 10.10 策略类型（strategyType）
- `GRID`：网格交易
- `TREND_FOLLOWING`：趋势跟踪
- `REVERSE`：反转策略

### 10.11 策略状态（status）
- `ACTIVE`：活跃
- `INACTIVE`：未激活
- `STOPPED`：已停止

### 10.12 警报类型（alertType）
- `MARGIN_RATIO_LOW`：保证金率低
- `LIQUIDATION_RISK`：强平风险
- `LEVERAGE_HIGH`：杠杆过高

### 10.13 警报状态（status）
- `ACTIVE`：活跃
- `TRIGGERED`：已触发
- `DISABLED`：已禁用

### 10.14 通知方式（notificationMethod）
- `EMAIL`：邮件
- `SMS`：短信
- `PUSH`：推送
- `ALL`：全部

### 10.15 条件类型（conditionType）
- `GREATER_THAN`：大于
- `LESS_THAN`：小于

### 10.16 利息结算周期（interestSettlementCycle）
- `1`：每小时结算
- `8`：每8小时结算
- `24`：每天结算

---

## 11. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

**常见错误示例**:

```json
{
  "code": 400,
  "message": "订单数量不能为空",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "保证金不足",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "杠杆倍数超出限制",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "存在未平仓持仓，无法调整杠杆倍数",
  "data": null
}
```

```json
{
  "code": 404,
  "message": "交易对不存在",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权限执行此操作",
  "data": null
}
```

---

## 12. 注意事项

1. **杠杆风险**：
   - 高杠杆交易可以快速带来较大收益，但也容易触发强平
   - 建议用户根据自身风险承受能力选择合适的杠杆倍数
   - 系统会根据市场情况动态调整杠杆限制

2. **保证金管理**：
   - 用户需要保持足够的保证金，避免触发强平
   - 当保证金比例低于维持保证金率时，系统会发出警告
   - 当保证金比例过低时，系统会自动强制平仓

3. **借款利息**：
   - 使用杠杆交易会产生借款利息
   - 利息按小时、日或周结算，具体取决于交易对配置
   - 用户可以在账户中查看实时利息预览

4. **订单类型**：
   - 限价单：指定价格，可能不会立即成交
   - 市价单：按当前市场价格立即成交
   - 止损单和止盈单：条件触发后自动执行
   - 条件单：满足条件时自动提交订单

5. **强平机制**：
   - 当账户保证金不足时，系统会自动强制平仓
   - 强平价格可能不是用户期望的价格，存在滑点风险
   - 建议用户主动管理风险，避免被强制平仓

6. **风险警报**：
   - 用户可以设置风险警报，及时了解账户风险状况
   - 当风险达到阈值时，系统会通过邮件、短信或推送通知用户
   - 建议用户设置合理的风险警报阈值

7. **交易策略**：
   - 支持网格交易、趋势跟踪、反转策略等多种交易策略
   - 在使用策略前，建议先了解策略的工作原理和风险
   - 策略执行过程中可以随时停止

8. **资金划转**：
   - 支持在现货账户和杠杆账户之间划转资金
   - 划转需要确认账户有足够余额
   - 存在未平仓持仓时，转出资金可能受限

9. **杠杆调整**：
   - 仅未开仓时可以调整杠杆倍数
   - 存在未平仓持仓时，无法调整杠杆倍数
   - 调整杠杆倍数会影响可用资金和借款额度

10. **利息计算**：
    - 利息 = 借款金额 × 利率 × 时间
    - 利息在每次结算时自动扣除
    - 用户可以在账户中查看利息记录和预览

---

**文档版本**：v2.0  
**最后更新**：2024-01-16  
**文档状态**：已完成






