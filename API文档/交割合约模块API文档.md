# 交割合约模块 API 文档

## 模块说明
交割合约（Futures Contract）是一种协议，用户通过期货合约来预先锁定未来某一日期的价格进行买卖。该模块提供合约交易、杠杆设置、保证金管理、结算机制、到期日管理等功能。支持USDT本位和币本位合约。

## Base URL
```
/delivery-contract
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 合约品种管理

### 1.1 获取合约列表

**接口地址**: `GET /delivery-contract/contracts`

**接口描述**: 获取可交易的交割合约列表，支持多种筛选条件

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractType | String | 否 | 合约类型筛选: USDT_MARGINED（USDT本位）, COIN_MARGINED（币本位）。不传则返回所有类型 |
| deliveryCycle | String | 否 | 交割周期筛选: HOURLY（每小时）, DAILY（每天）, WEEKLY（每周）, MONTHLY（每月）。不传则返回所有周期 |
| status | String | 否 | 状态筛选: ACTIVE（启用）, INACTIVE（禁用）, SETTLED（已结算）。不传则返回所有状态 |
| underlyingAsset | String | 否 | 标的资产筛选，如: BTC, ETH。不传则返回所有标的资产 |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/contracts?contractType=USDT_MARGINED&status=ACTIVE&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "contractSymbol": "BTC_USDT",
      "contractType": "USDT_MARGINED",
      "underlyingAsset": "BTC",
      "quoteCurrency": "USDT",
      "contractUnit": 1.00000000,
      "deliveryCycle": "WEEKLY",
      "maxLeverage": 125.00,
      "makerFeeRate": 0.000200,
      "takerFeeRate": 0.000500,
      "initialMarginRate": 0.010000,
      "maintenanceMarginRate": 0.005000,
      "deliveryTime": "2024-01-07T16:00:00",
      "settlementPrice": null,
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    },
    {
      "id": 2,
      "contractSymbol": "ETH_USDT",
      "contractType": "USDT_MARGINED",
      "underlyingAsset": "ETH",
      "quoteCurrency": "USDT",
      "contractUnit": 1.00000000,
      "deliveryCycle": "WEEKLY",
      "maxLeverage": 125.00,
      "makerFeeRate": 0.000200,
      "takerFeeRate": 0.000500,
      "initialMarginRate": 0.010000,
      "maintenanceMarginRate": 0.005000,
      "deliveryTime": "2024-01-07T16:00:00",
      "settlementPrice": null,
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

---

### 1.2 获取合约详情

**接口地址**: `GET /delivery-contract/contracts/{contractId}`

**接口描述**: 获取指定合约的详细信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**请求示例**:
```
GET /delivery-contract/contracts/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "contractSymbol": "BTC_USDT",
    "contractType": "USDT_MARGINED",
    "underlyingAsset": "BTC",
    "quoteCurrency": "USDT",
    "contractUnit": 1.00000000,
    "deliveryCycle": "WEEKLY",
    "maxLeverage": 125.00,
    "makerFeeRate": 0.000200,
    "takerFeeRate": 0.000500,
    "initialMarginRate": 0.010000,
    "maintenanceMarginRate": 0.005000,
    "deliveryTime": "2024-01-07T16:00:00",
    "settlementPrice": null,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
}
```

---

### 1.3 获取合约行情数据

**接口地址**: `GET /delivery-contract/market-data/{contractId}`

**接口描述**: 获取指定合约的实时行情数据，包括价格、成交量、持仓量等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**请求示例**:
```
GET /delivery-contract/market-data/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "contractId": 1,
    "price": 50000.00000000,
    "volume24h": 1000.50000000,
    "high24h": 51000.00000000,
    "low24h": 49000.00000000,
    "change24h": 0.0200,
    "openInterest": 5000.00000000,
    "fundingRate": null,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 1.4 获取合约K线数据

**接口地址**: `GET /delivery-contract/market-data/{contractId}/kline`

**接口描述**: 获取指定合约的K线数据

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interval | String | 否 | 时间粒度: 1m, 5m, 15m, 30m, 1h, 4h, 1d（默认1h） |
| limit | Integer | 否 | 返回数量（默认100，最大1000） |
| startTime | Long | 否 | 开始时间戳（毫秒） |
| endTime | Long | 否 | 结束时间戳（毫秒） |

**请求示例**:
```
GET /delivery-contract/market-data/1/kline?interval=1h&limit=100
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "openTime": 1705392000000,
      "open": 50000.00,
      "high": 50500.00,
      "low": 49800.00,
      "close": 50200.00,
      "volume": 100.50,
      "closeTime": 1705395599999
    },
    {
      "openTime": 1705395600000,
      "open": 50200.00,
      "high": 50800.00,
      "low": 50100.00,
      "close": 50700.00,
      "volume": 120.30,
      "closeTime": 1705399199999
    }
  ]
}
```

---

## 2. 订单管理

### 2.1 创建订单（开仓/平仓）

**接口地址**: `POST /delivery-contract/order`

**接口描述**: 创建交割合约订单，支持开仓和平仓操作，支持限价单、市价单、止损单、止盈单等多种订单类型

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "contractId": 1,
  "orderType": "OPEN",
  "side": "BUY",
  "priceType": "LIMIT",
  "price": 50000.00,
  "quantity": 0.1,
  "leverage": 10,
  "stopLossPrice": 49000.00,
  "takeProfitPrice": 51000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| contractId | Long | 是 | 合约ID |
| orderType | String | 是 | 订单类型: OPEN（开仓）, CLOSE（平仓）, STOP_LOSS（止损）, TAKE_PROFIT（止盈） |
| side | String | 是 | 方向: BUY（买入/做多）, SELL（卖出/做空） |
| priceType | String | 是 | 价格类型: LIMIT（限价）, MARKET（市价） |
| price | BigDecimal | 否 | 限价价格（限价单必填） |
| quantity | BigDecimal | 是 | 数量（合约张数） |
| leverage | BigDecimal | 是 | 杠杆倍数（开仓必填） |
| stopLossPrice | BigDecimal | 否 | 止损价格 |
| takeProfitPrice | BigDecimal | 否 | 止盈价格 |

**市价单示例**:
```json
{
  "contractId": 1,
  "orderType": "OPEN",
  "side": "BUY",
  "priceType": "MARKET",
  "quantity": 0.1,
  "leverage": 10
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "id": 1001,
    "orderNo": "DC20240116100001",
    "userId": 1,
    "contractId": 1,
    "orderType": "OPEN",
    "side": "BUY",
    "priceType": "LIMIT",
    "price": 50000.00000000,
    "quantity": 0.10000000,
    "filledQuantity": 0.00000000,
    "leverage": 10.00,
    "stopLossPrice": 49000.00000000,
    "takeProfitPrice": 51000.00000000,
    "margin": 500.00000000,
    "status": "PENDING",
    "filledAt": null,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.2 取消订单

**接口地址**: `POST /delivery-contract/order/{orderId}/cancel`

**接口描述**: 取消未成交的订单

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**:
```
POST /delivery-contract/order/1001/cancel
```

**响应示例**:
```json
{
  "code": 200,
  "message": "订单取消成功",
  "data": null
}
```

---

### 2.3 修改订单

**接口地址**: `PUT /delivery-contract/order/{orderId}`

**接口描述**: 修改未成交的限价单价格

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求参数**:
```json
{
  "price": 50100.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| price | BigDecimal | 是 | 新的限价价格 |

**响应示例**:
```json
{
  "code": 200,
  "message": "订单修改成功",
  "data": {
    "id": 1001,
    "orderNo": "DC20240116100001",
    "userId": 1,
    "contractId": 1,
    "orderType": "OPEN",
    "side": "BUY",
    "priceType": "LIMIT",
    "price": 50100.00000000,
    "quantity": 0.10000000,
    "filledQuantity": 0.00000000,
    "leverage": 10.00,
    "stopLossPrice": 49000.00000000,
    "takeProfitPrice": 51000.00000000,
    "margin": 501.00000000,
    "status": "PENDING",
    "filledAt": null,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:05:00"
  }
}
```

---

### 2.4 查询订单列表

**接口地址**: `GET /delivery-contract/orders`

**接口描述**: 查询当前用户的订单列表，支持多种筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| orderType | String | 否 | 订单类型筛选: OPEN, CLOSE, STOP_LOSS, TAKE_PROFIT |
| side | String | 否 | 方向筛选: BUY, SELL |
| priceType | String | 否 | 价格类型筛选: LIMIT, MARKET |
| status | String | 否 | 状态筛选: PENDING（待成交）, PARTIAL（部分成交）, FILLED（已完成）, CANCELLED（已取消）, REJECTED（已拒绝）。不传则返回所有状态 |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/orders?contractId=1&status=PENDING&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1001,
      "orderNo": "DC20240116100001",
      "userId": 1,
      "contractId": 1,
      "orderType": "OPEN",
      "side": "BUY",
      "priceType": "LIMIT",
      "price": 50000.00000000,
      "quantity": 0.10000000,
      "filledQuantity": 0.00000000,
      "leverage": 10.00,
      "stopLossPrice": 49000.00000000,
      "takeProfitPrice": 51000.00000000,
      "margin": 500.00000000,
      "status": "PENDING",
      "filledAt": null,
      "createdAt": "2024-01-16T10:00:00",
      "updatedAt": "2024-01-16T10:00:00"
    },
    {
      "id": 1002,
      "orderNo": "DC20240116100002",
      "userId": 1,
      "contractId": 1,
      "orderType": "OPEN",
      "side": "SELL",
      "priceType": "MARKET",
      "price": null,
      "quantity": 0.20000000,
      "filledQuantity": 0.20000000,
      "leverage": 5.00,
      "stopLossPrice": null,
      "takeProfitPrice": null,
      "margin": 2000.00000000,
      "status": "FILLED",
      "filledAt": "2024-01-16T09:30:00",
      "createdAt": "2024-01-16T09:30:00",
      "updatedAt": "2024-01-16T09:30:05"
    }
  ]
}
```

---

### 2.5 查询订单详情

**接口地址**: `GET /delivery-contract/order/{orderId}`

**接口描述**: 查询指定订单的详细信息

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**:
```
GET /delivery-contract/order/1001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1001,
    "orderNo": "DC20240116100001",
    "userId": 1,
    "contractId": 1,
    "orderType": "OPEN",
    "side": "BUY",
    "priceType": "LIMIT",
    "price": 50000.00000000,
    "quantity": 0.10000000,
    "filledQuantity": 0.00000000,
    "leverage": 10.00,
    "stopLossPrice": 49000.00000000,
    "takeProfitPrice": 51000.00000000,
    "margin": 500.00000000,
    "status": "PENDING",
    "filledAt": null,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

---

## 3. 持仓管理

### 3.1 查询持仓列表

**接口地址**: `GET /delivery-contract/positions`

**接口描述**: 查询当前用户的所有持仓，支持多种筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| side | String | 否 | 方向筛选: LONG（多头）, SHORT（空头） |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, CLOSED（已平仓）, LIQUIDATED（已强平）。不传则返回所有状态 |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/positions?contractId=1&status=ACTIVE&page=1&size=20
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
      "contractId": 1,
      "side": "LONG",
      "quantity": 0.10000000,
      "avgOpenPrice": 50000.00000000,
      "currentPrice": 50500.00000000,
      "leverage": 10.00,
      "margin": 500.00000000,
      "maintenanceMargin": 250.00000000,
      "unrealizedPnl": 50.00000000,
      "realizedPnl": 0.00000000,
      "liquidationPrice": 45000.00000000,
      "status": "ACTIVE",
      "openedAt": "2024-01-16T09:30:00",
      "closedAt": null,
      "createdAt": "2024-01-16T09:30:00",
      "updatedAt": "2024-01-16T10:00:00"
    },
    {
      "id": 2002,
      "userId": 1,
      "contractId": 1,
      "side": "SHORT",
      "quantity": 0.20000000,
      "avgOpenPrice": 51000.00000000,
      "currentPrice": 50500.00000000,
      "leverage": 5.00,
      "margin": 2040.00000000,
      "maintenanceMargin": 1020.00000000,
      "unrealizedPnl": 100.00000000,
      "realizedPnl": 0.00000000,
      "liquidationPrice": 53500.00000000,
      "status": "ACTIVE",
      "openedAt": "2024-01-16T09:00:00",
      "closedAt": null,
      "createdAt": "2024-01-16T09:00:00",
      "updatedAt": "2024-01-16T10:00:00"
    }
  ]
}
```

---

### 3.2 查询持仓详情

**接口地址**: `GET /delivery-contract/position/{positionId}`

**接口描述**: 查询指定持仓的详细信息，包括实时盈亏、强平价格等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |

**请求示例**:
```
GET /delivery-contract/position/2001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 2001,
    "userId": 1,
    "contractId": 1,
    "side": "LONG",
    "quantity": 0.10000000,
    "avgOpenPrice": 50000.00000000,
    "currentPrice": 50500.00000000,
    "leverage": 10.00,
    "margin": 500.00000000,
    "maintenanceMargin": 250.00000000,
    "unrealizedPnl": 50.00000000,
    "realizedPnl": 0.00000000,
    "liquidationPrice": 45000.00000000,
    "status": "ACTIVE",
    "openedAt": "2024-01-16T09:30:00",
    "closedAt": null,
    "createdAt": "2024-01-16T09:30:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.3 平仓

**接口地址**: `POST /delivery-contract/position/{positionId}/close`

**接口描述**: 平掉指定持仓，支持全部平仓和部分平仓

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |

**请求参数**:
```json
{
  "quantity": 0.05,
  "priceType": "MARKET",
  "price": null
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| quantity | BigDecimal | 否 | 平仓数量（不传则全部平仓） |
| priceType | String | 是 | 价格类型: LIMIT（限价）, MARKET（市价） |
| price | BigDecimal | 否 | 限价价格（限价单必填） |

**全部平仓示例**:
```json
{
  "priceType": "MARKET"
}
```

**部分平仓示例（限价单）**:
```json
{
  "quantity": 0.05,
  "priceType": "LIMIT",
  "price": 51000.00
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "平仓成功",
  "data": {
    "id": 3001,
    "orderNo": "DC20240116100003",
    "userId": 1,
    "contractId": 1,
    "orderType": "CLOSE",
    "side": "SELL",
    "priceType": "MARKET",
    "price": null,
    "quantity": 0.05000000,
    "filledQuantity": 0.05000000,
    "leverage": 10.00,
    "stopLossPrice": null,
    "takeProfitPrice": null,
    "margin": 0.00000000,
    "status": "FILLED",
    "filledAt": "2024-01-16T10:05:00",
    "createdAt": "2024-01-16T10:05:00",
    "updatedAt": "2024-01-16T10:05:05"
  }
}
```

---

### 3.4 查询持仓历史记录

**接口地址**: `GET /delivery-contract/positions/history`

**接口描述**: 查询已平仓的持仓历史记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| side | String | 否 | 方向筛选: LONG, SHORT |
| status | String | 否 | 状态筛选: CLOSED（已平仓）, LIQUIDATED（已强平） |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/positions/history?contractId=1&status=CLOSED&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 2003,
      "userId": 1,
      "contractId": 1,
      "side": "LONG",
      "quantity": 0.10000000,
      "avgOpenPrice": 49000.00000000,
      "currentPrice": 51000.00000000,
      "leverage": 10.00,
      "margin": 490.00000000,
      "maintenanceMargin": 245.00000000,
      "unrealizedPnl": 0.00000000,
      "realizedPnl": 200.00000000,
      "liquidationPrice": 44100.00000000,
      "status": "CLOSED",
      "openedAt": "2024-01-15T09:00:00",
      "closedAt": "2024-01-16T09:00:00",
      "createdAt": "2024-01-15T09:00:00",
      "updatedAt": "2024-01-16T09:00:00"
    }
  ]
}
```

---

---

## 4. 账户与资金管理

### 4.1 查询合约账户余额

**接口地址**: `GET /delivery-contract/account/balance`

**接口描述**: 查询用户的合约账户余额信息

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选，如: USDT, BTC。不传则返回所有币种 |

**请求示例**:
```
GET /delivery-contract/account/balance?currency=USDT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "balances": [
      {
        "currency": "USDT",
        "available": 10000.00000000,
        "frozen": 500.00000000,
        "total": 10500.00000000,
        "unrealizedPnl": 50.00000000
      },
      {
        "currency": "BTC",
        "available": 1.50000000,
        "frozen": 0.10000000,
        "total": 1.60000000,
        "unrealizedPnl": 0.00100000
      }
    ],
    "totalEquity": 10500.50000000,
    "totalMargin": 600.00000000,
    "totalUnrealizedPnl": 50.00100000
  }
}
```

---

### 4.2 查询账户保证金信息

**接口地址**: `GET /delivery-contract/account/margin`

**接口描述**: 查询用户的保证金使用情况，包括已用保证金、可用保证金、维持保证金等

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选。不传则返回所有合约的汇总 |

**请求示例**:
```
GET /delivery-contract/account/margin
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "totalMargin": 600.00000000,
    "usedMargin": 500.00000000,
    "availableMargin": 100.00000000,
    "maintenanceMargin": 250.00000000,
    "marginRatio": 0.8333,
    "marginLevel": 2.1000,
    "positions": [
      {
        "contractId": 1,
        "contractSymbol": "BTC_USDT",
        "margin": 500.00000000,
        "maintenanceMargin": 250.00000000,
        "leverage": 10.00
      }
    ]
  }
}
```

---

### 4.3 查询账户盈亏统计

**接口地址**: `GET /delivery-contract/account/pnl`

**接口描述**: 查询用户的盈亏统计信息，包括未实现盈亏、已实现盈亏等

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选。不传则返回所有合约的汇总 |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| period | String | 否 | 统计周期: TODAY（今天）, WEEK（本周）, MONTH（本月）, YEAR（本年）, ALL（全部）。默认ALL |

**请求示例**:
```
GET /delivery-contract/account/pnl?period=TODAY
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "period": "TODAY",
    "totalUnrealizedPnl": 50.00000000,
    "totalRealizedPnl": 200.00000000,
    "totalPnl": 250.00000000,
    "totalFees": 10.50000000,
    "netPnl": 239.50000000,
    "positions": [
      {
        "contractId": 1,
        "contractSymbol": "BTC_USDT",
        "unrealizedPnl": 50.00000000,
        "realizedPnl": 150.00000000,
        "fees": 7.50000000
      },
      {
        "contractId": 2,
        "contractSymbol": "ETH_USDT",
        "unrealizedPnl": 0.00000000,
        "realizedPnl": 50.00000000,
        "fees": 3.00000000
      }
    ]
  }
}
```

---

### 4.4 转账到合约账户

**接口地址**: `POST /delivery-contract/account/transfer/in`

**接口描述**: 从现货账户转账到合约账户

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "currency": "USDT",
  "amount": 1000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| currency | String | 是 | 币种，如: USDT, BTC |
| amount | BigDecimal | 是 | 转账金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "转账成功",
  "data": {
    "transferId": "TF20240116100001",
    "currency": "USDT",
    "amount": 1000.00000000,
    "fromAccount": "SPOT",
    "toAccount": "DELIVERY_CONTRACT",
    "status": "SUCCESS",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.5 从合约账户转出

**接口地址**: `POST /delivery-contract/account/transfer/out`

**接口描述**: 从合约账户转账到现货账户

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "currency": "USDT",
  "amount": 500.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| currency | String | 是 | 币种，如: USDT, BTC |
| amount | BigDecimal | 是 | 转账金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "转账成功",
  "data": {
    "transferId": "TF20240116100002",
    "currency": "USDT",
    "amount": 500.00000000,
    "fromAccount": "DELIVERY_CONTRACT",
    "toAccount": "SPOT",
    "status": "SUCCESS",
    "createdAt": "2024-01-16T10:05:00"
  }
}
```

---

### 4.6 查询转账记录

**接口地址**: `GET /delivery-contract/account/transfer/history`

**接口描述**: 查询账户转账历史记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |
| direction | String | 否 | 方向筛选: IN（转入）, OUT（转出） |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/account/transfer/history?currency=USDT&direction=IN&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "transferId": "TF20240116100001",
      "currency": "USDT",
      "amount": 1000.00000000,
      "fromAccount": "SPOT",
      "toAccount": "DELIVERY_CONTRACT",
      "status": "SUCCESS",
      "createdAt": "2024-01-16T10:00:00"
    },
    {
      "transferId": "TF20240116100002",
      "currency": "USDT",
      "amount": 500.00000000,
      "fromAccount": "DELIVERY_CONTRACT",
      "toAccount": "SPOT",
      "status": "SUCCESS",
      "createdAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

---

## 5. 结算管理

### 5.1 查询结算记录

**接口地址**: `GET /delivery-contract/settlements`

**接口描述**: 查询用户的合约结算记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| settlementType | String | 否 | 结算类型筛选: DELIVERY（交割）, LIQUIDATION（强平）, MANUAL（手动） |
| status | String | 否 | 状态筛选: PENDING（待结算）, SETTLED（已结算）, FAILED（结算失败） |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/settlements?contractId=1&settlementType=DELIVERY&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 4001,
      "settlementNo": "ST20240107160001",
      "contractId": 1,
      "userId": 1,
      "positionId": 2001,
      "settlementPrice": 51000.00000000,
      "settlementPnl": 100.00000000,
      "settlementType": "DELIVERY",
      "status": "SETTLED",
      "settledAt": "2024-01-07T16:00:00",
      "createdAt": "2024-01-07T16:00:00",
      "updatedAt": "2024-01-07T16:00:05"
    }
  ]
}
```

---

### 5.2 查询结算详情

**接口地址**: `GET /delivery-contract/settlement/{settlementId}`

**接口描述**: 查询指定结算记录的详细信息

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| settlementId | Long | 是 | 结算记录ID |

**请求示例**:
```
GET /delivery-contract/settlement/4001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 4001,
    "settlementNo": "ST20240107160001",
    "contractId": 1,
    "contractSymbol": "BTC_USDT",
    "userId": 1,
    "positionId": 2001,
    "settlementPrice": 51000.00000000,
    "settlementPnl": 100.00000000,
    "settlementType": "DELIVERY",
    "status": "SETTLED",
    "settledAt": "2024-01-07T16:00:00",
    "createdAt": "2024-01-07T16:00:00",
    "updatedAt": "2024-01-07T16:00:05"
  }
}
```

---

## 6. 风控管理

### 6.1 查询账户风险状态

**接口地址**: `GET /delivery-contract/risk/status`

**接口描述**: 查询用户账户的风险状态，包括保证金比例、强平风险等

**请求头**:
```
X-User-Id: 1
```

**请求示例**:
```
GET /delivery-contract/risk/status
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "marginRatio": 0.8333,
    "marginLevel": 2.1000,
    "riskLevel": "NORMAL",
    "liquidationRisk": false,
    "warningLevel": "NONE",
    "positions": [
      {
        "positionId": 2001,
        "contractId": 1,
        "contractSymbol": "BTC_USDT",
        "marginRatio": 0.5000,
        "liquidationPrice": 45000.00000000,
        "currentPrice": 50500.00000000,
        "distanceToLiquidation": 5500.00000000,
        "riskLevel": "LOW"
      }
    ],
    "totalMargin": 500.00000000,
    "availableMargin": 100.00000000,
    "maintenanceMargin": 250.00000000
  }
}
```

---

### 6.2 查询强平记录

**接口地址**: `GET /delivery-contract/risk/liquidation-history`

**接口描述**: 查询用户的强平历史记录

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /delivery-contract/risk/liquidation-history?contractId=1&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "liquidationId": "LIQ20240115100001",
      "positionId": 2002,
      "contractId": 1,
      "contractSymbol": "BTC_USDT",
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

### 6.3 查询风险预警

**接口地址**: `GET /delivery-contract/risk/warnings`

**接口描述**: 查询当前账户的风险预警信息

**请求头**:
```
X-User-Id: 1
```

**请求示例**:
```
GET /delivery-contract/risk/warnings
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "warnings": [
      {
        "type": "MARGIN_RATIO_LOW",
        "level": "WARNING",
        "message": "您的保证金比例较低，建议增加保证金",
        "marginRatio": 0.1500,
        "recommendedAction": "ADD_MARGIN"
      }
    ],
    "hasWarning": true,
    "warningCount": 1
  }
}
```

---

---

## 7. 后台管理接口

### 7.1 合约管理

#### 7.1.1 创建合约

**接口地址**: `POST /admin/delivery-contract/contracts`

**接口描述**: 后台创建新的交割合约

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "contractSymbol": "BTC_USDT_WEEKLY",
  "contractType": "USDT_MARGINED",
  "underlyingAsset": "BTC",
  "quoteCurrency": "USDT",
  "contractUnit": 1.00000000,
  "deliveryCycle": "WEEKLY",
  "maxLeverage": 125.00,
  "makerFeeRate": 0.000200,
  "takerFeeRate": 0.000500,
  "initialMarginRate": 0.010000,
  "maintenanceMarginRate": 0.005000,
  "deliveryTime": "2024-01-14T16:00:00"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "合约创建成功",
  "data": {
    "id": 10,
    "contractSymbol": "BTC_USDT_WEEKLY",
    "contractType": "USDT_MARGINED",
    "underlyingAsset": "BTC",
    "quoteCurrency": "USDT",
    "contractUnit": 1.00000000,
    "deliveryCycle": "WEEKLY",
    "maxLeverage": 125.00,
    "makerFeeRate": 0.000200,
    "takerFeeRate": 0.000500,
    "initialMarginRate": 0.010000,
    "maintenanceMarginRate": 0.005000,
    "deliveryTime": "2024-01-14T16:00:00",
    "settlementPrice": null,
    "status": "ACTIVE",
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

#### 7.1.2 更新合约

**接口地址**: `PUT /admin/delivery-contract/contracts/{contractId}`

**接口描述**: 后台更新合约信息

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**请求参数**:
```json
{
  "maxLeverage": 100.00,
  "makerFeeRate": 0.000150,
  "takerFeeRate": 0.000450,
  "status": "ACTIVE"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "合约更新成功",
  "data": {
    "id": 1,
    "contractSymbol": "BTC_USDT",
    "maxLeverage": 100.00,
    "makerFeeRate": 0.000150,
    "takerFeeRate": 0.000450,
    "status": "ACTIVE",
    "updatedAt": "2024-01-16T10:05:00"
  }
}
```

---

#### 7.1.3 删除合约

**接口地址**: `DELETE /admin/delivery-contract/contracts/{contractId}`

**接口描述**: 后台删除合约（仅允许删除未激活的合约）

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "合约删除成功",
  "data": null
}
```

---

#### 7.1.4 查询所有订单（后台）

**接口地址**: `GET /admin/delivery-contract/orders`

**接口描述**: 后台查询所有用户的订单列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| contractId | Long | 否 | 合约ID筛选 |
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
        "orderNo": "DC20240116100001",
        "userId": 1,
        "contractId": 1,
        "orderType": "OPEN",
        "side": "BUY",
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

#### 7.1.5 查询所有持仓（后台）

**接口地址**: `GET /admin/delivery-contract/positions`

**接口描述**: 后台查询所有用户的持仓列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| contractId | Long | 否 | 合约ID筛选 |
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
        "contractId": 1,
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

### 7.2 数据统计

#### 7.2.1 交易量统计

**接口地址**: `GET /admin/delivery-contract/statistics/volume`

**接口描述**: 查询交易量统计，支持按合约、时间等维度统计

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| period | String | 否 | 统计周期: HOUR（小时）, DAY（天）, WEEK（周）, MONTH（月）。默认DAY |

**请求示例**:
```
GET /admin/delivery-contract/statistics/volume?period=DAY&startTime=2024-01-01 00:00:00&endTime=2024-01-16 23:59:59
```

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
      },
      {
        "date": "2024-01-15",
        "volume": 480.20000000,
        "amount": 24000000.00000000,
        "trades": 240
      }
    ]
  }
}
```

---

#### 7.2.2 盈亏统计

**接口地址**: `GET /admin/delivery-contract/statistics/pnl`

**接口描述**: 查询盈亏统计，包括平台总盈亏、用户盈亏分布等

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 否 | 合约ID筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期: DAY, WEEK, MONTH |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "DAY",
    "totalRealizedPnl": 100000.00000000,
    "totalFees": 50000.00000000,
    "netProfit": 50000.00000000,
    "profitableUsers": 300,
    "losingUsers": 200,
    "details": [
      {
        "date": "2024-01-16",
        "realizedPnl": 5000.00000000,
        "fees": 2500.00000000
      }
    ]
  }
}
```

---

#### 7.2.3 用户风险统计

**接口地址**: `GET /admin/delivery-contract/statistics/risk`

**接口描述**: 查询用户风险统计，包括高风险用户数量、强平统计等

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

#### 7.2.4 手动强制平仓

**接口地址**: `POST /admin/delivery-contract/risk/liquidation/manual`

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

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| positionId | Long | 是 | 持仓ID |
| reason | String | 是 | 强平原因 |

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

## 8. 数据字典

### 8.1 合约类型（contractType）
- `USDT_MARGINED`：USDT本位合约
- `COIN_MARGINED`：币本位合约

### 8.2 交割周期（deliveryCycle）
- `HOURLY`：每小时交割
- `DAILY`：每天交割
- `WEEKLY`：每周交割
- `MONTHLY`：每月交割

### 8.3 合约状态（status）
- `ACTIVE`：启用
- `INACTIVE`：禁用
- `SETTLED`：已结算

### 8.4 订单类型（orderType）
- `OPEN`：开仓
- `CLOSE`：平仓
- `STOP_LOSS`：止损
- `TAKE_PROFIT`：止盈

### 8.5 订单方向（side）
- `BUY`：买入/做多
- `SELL`：卖出/做空

### 8.6 价格类型（priceType）
- `LIMIT`：限价
- `MARKET`：市价

### 8.7 订单状态（status）
- `PENDING`：待成交
- `PARTIAL`：部分成交
- `FILLED`：已完成
- `CANCELLED`：已取消
- `REJECTED`：已拒绝

### 8.8 持仓方向（side）
- `LONG`：多头
- `SHORT`：空头

### 8.9 持仓状态（status）
- `ACTIVE`：活跃
- `CLOSED`：已平仓
- `LIQUIDATED`：已强平

### 8.10 结算类型（settlementType）
- `DELIVERY`：交割结算
- `LIQUIDATION`：强平结算
- `MANUAL`：手动结算

### 8.11 结算状态（status）
- `PENDING`：待结算
- `SETTLED`：已结算
- `FAILED`：结算失败

### 8.12 风险等级（riskLevel）
- `LOW`：低风险
- `MEDIUM`：中风险
- `HIGH`：高风险
- `CRITICAL`：严重风险

---

## 9. 错误码说明

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
  "code": 404,
  "message": "合约不存在",
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

## 10. 注意事项

1. **合约到期处理**：
   - 合约到期前系统会自动提醒用户
   - 到期时自动结算，无需用户手动操作
   - 结算价格按到期时的市场价格确定

2. **保证金管理**：
   - 用户需要确保账户有足够的保证金
   - 当保证金比例低于维持保证金率时，系统会发出警告
   - 当保证金比例过低时，系统会自动强制平仓

3. **杠杆风险**：
   - 高杠杆意味着高风险
   - 建议用户根据自身风险承受能力选择合适的杠杆倍数
   - 系统会根据市场情况动态调整杠杆限制

4. **强平机制**：
   - 当账户保证金不足时，系统会自动强制平仓
   - 强平价格可能不是用户期望的价格，存在滑点风险
   - 建议用户主动管理风险，避免被强制平仓

5. **订单类型**：
   - 限价单：指定价格，可能不会立即成交
   - 市价单：按当前市场价格立即成交
   - 止损单和止盈单：条件触发后自动执行

6. **结算说明**：
   - 合约到期时自动结算
   - 用户可以提前平仓，提前结算
   - 结算盈亏会自动转入账户余额

7. **资金划转**：
   - 支持在现货账户和合约账户之间划转资金
   - 划转需要确认账户有足够余额
   - 划转记录会保存在账户历史中

---

**文档版本**：v2.0  
**最后更新**：2024-01-16  
**文档状态**：已完成
