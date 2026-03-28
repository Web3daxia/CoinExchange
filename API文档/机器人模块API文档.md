# 网格交易机器人模块 API 文档

## 模块说明
网格交易机器人（Grid Trading Bot）是一种自动化交易策略，通过设置一系列价格区间，在市场波动中买入和卖出资产。网格交易机器人的核心目的是在市场震荡的环境中捕获波动，自动执行交易。该模块支持现货和合约市场，同时提供灵活的配置选项，能够帮助用户设定和管理机器人策略。

## Base URL
```
/robot
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 网格机器人创建与配置

### 1.1 创建现货网格机器人

**接口地址**: `POST /robot/grid/spot/create`

**接口描述**: 创建现货市场的网格交易机器人

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "robotName": "BTC/USDT现货网格机器人",
  "pairName": "BTC/USDT",
  "gridCount": 10,
  "upperPrice": 51000.00,
  "lowerPrice": 49000.00,
  "startPrice": 50000.00,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| robotName | String | 否 | 机器人名称（不传则自动生成） |
| pairName | String | 是 | 交易对名称，如: BTC/USDT |
| gridCount | Integer | 是 | 网格数量（建议5-50） |
| upperPrice | BigDecimal | 是 | 卖出区间上限价格 |
| lowerPrice | BigDecimal | 是 | 买入区间下限价格 |
| startPrice | BigDecimal | 是 | 起始价格（网格开始的价格） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例（0-1，默认0.5，表示使用50%的资金） |
| stopLossPrice | BigDecimal | 否 | 止损价格（绝对值） |
| takeProfitPrice | BigDecimal | 否 | 止盈价格（绝对值） |
| stopLossPercentage | BigDecimal | 否 | 止损百分比（相对起始价格） |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比（相对起始价格） |

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人创建成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "robotName": "BTC/USDT现货网格机器人",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "GRID",
    "status": "STOPPED",
    "gridCount": 10,
    "upperPrice": 51000.00000000,
    "lowerPrice": 49000.00000000,
    "startPrice": 50000.00000000,
    "initialCapital": 10000.00000000,
    "investmentRatio": 0.5000,
    "stopLossPrice": 48000.00000000,
    "takeProfitPrice": 52000.00000000,
    "totalProfit": 0.00000000,
    "totalLoss": 0.00000000,
    "totalTrades": 0,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 1.2 创建合约网格机器人（USDT本位）

**接口地址**: `POST /robot/grid/futures-usdt/create`

**接口描述**: 创建USDT本位合约市场的网格交易机器人

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "robotName": "BTC/USDT合约网格机器人",
  "pairName": "BTC/USDT",
  "gridCount": 10,
  "upperPrice": 51000.00,
  "lowerPrice": 49000.00,
  "startPrice": 50000.00,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "leverage": 10,
  "marginMode": "ISOLATED",
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| robotName | String | 否 | 机器人名称 |
| pairName | String | 是 | 交易对名称 |
| gridCount | Integer | 是 | 网格数量 |
| upperPrice | BigDecimal | 是 | 卖出区间上限价格 |
| lowerPrice | BigDecimal | 是 | 买入区间下限价格 |
| startPrice | BigDecimal | 是 | 起始价格 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例（默认0.5） |
| leverage | Integer | 是 | 杠杆倍数（合约市场必填） |
| marginMode | String | 是 | 保证金模式: ISOLATED（逐仓）, CROSS（全仓） |
| stopLossPrice | BigDecimal | 否 | 止损价格 |
| takeProfitPrice | BigDecimal | 否 | 止盈价格 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "合约网格机器人创建成功",
  "data": {
    "id": 1002,
    "userId": 1,
    "robotName": "BTC/USDT合约网格机器人",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "GRID",
    "status": "STOPPED",
    "gridCount": 10,
    "upperPrice": 51000.00000000,
    "lowerPrice": 49000.00000000,
    "startPrice": 50000.00000000,
    "initialCapital": 10000.00000000,
    "investmentRatio": 0.5000,
    "leverage": 10,
    "marginMode": "ISOLATED",
    "stopLossPrice": 48000.00000000,
    "takeProfitPrice": 52000.00000000,
    "totalProfit": 0.00000000,
    "totalLoss": 0.00000000,
    "totalTrades": 0,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 1.3 创建合约网格机器人（币本位）

**接口地址**: `POST /robot/grid/futures-coin/create`

**接口描述**: 创建币本位合约市场的网格交易机器人

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "robotName": "BTC/BTC币本位网格机器人",
  "pairName": "BTC/BTC",
  "gridCount": 10,
  "upperPrice": 51000.00,
  "lowerPrice": 49000.00,
  "startPrice": 50000.00,
  "initialCapital": 1.0,
  "investmentRatio": 0.5,
  "leverage": 10,
  "marginMode": "ISOLATED",
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00
}
```

**字段说明**: 与USDT本位合约类似，但initialCapital为币本位数量

**响应示例**:
```json
{
  "code": 200,
  "message": "币本位合约网格机器人创建成功",
  "data": {
    "id": 1003,
    "userId": 1,
    "robotName": "BTC/BTC币本位网格机器人",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "GRID",
    "status": "STOPPED",
    "gridCount": 10,
    "leverage": 10,
    "marginMode": "ISOLATED",
    "totalProfit": 0.00000000,
    "totalTrades": 0,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 1.4 使用默认策略创建网格机器人

**接口地址**: `POST /robot/grid/create/with-default`

**接口描述**: 使用平台默认策略快速创建网格机器人

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "robotName": "BTC/USDT网格机器人（默认策略）",
  "pairName": "BTC/USDT",
  "marketType": "SPOT",
  "defaultStrategyId": 1,
  "initialCapital": 10000.00,
  "customParams": {
    "gridCount": 15,
    "upperPrice": 52000.00,
    "lowerPrice": 48000.00
  }
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| robotName | String | 否 | 机器人名称 |
| pairName | String | 是 | 交易对名称 |
| marketType | String | 是 | 市场类型: SPOT, FUTURES_USDT, FUTURES_COIN |
| defaultStrategyId | Long | 否 | 默认策略ID（不传则使用平台推荐策略） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| customParams | Map<String, Object> | 否 | 自定义参数（覆盖默认策略参数） |

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人创建成功",
  "data": {
    "id": 1004,
    "userId": 1,
    "robotName": "BTC/USDT网格机器人（默认策略）",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "GRID",
    "status": "STOPPED",
    "gridCount": 15,
    "upperPrice": 52000.00000000,
    "lowerPrice": 48000.00000000,
    "initialCapital": 10000.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 1.5 查询默认策略列表

**接口地址**: `GET /robot/grid/default-strategies`

**接口描述**: 查询平台提供的默认网格策略列表

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选: SPOT, FUTURES_USDT, FUTURES_COIN |
| pairName | String | 否 | 交易对筛选 |

**请求示例**:
```
GET /robot/grid/default-strategies?marketType=SPOT
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "strategyName": "保守型网格策略",
      "marketType": "SPOT",
      "gridCount": 10,
      "defaultUpperPrice": null,
      "defaultLowerPrice": null,
      "defaultStopLossPercentage": 0.05,
      "defaultTakeProfitPercentage": 0.10,
      "description": "适合震荡市场，风险较低",
      "expectedReturnRate": 0.08,
      "riskLevel": "LOW"
    },
    {
      "id": 2,
      "strategyName": "激进型网格策略",
      "marketType": "SPOT",
      "gridCount": 20,
      "defaultUpperPrice": null,
      "defaultLowerPrice": null,
      "defaultStopLossPercentage": 0.10,
      "defaultTakeProfitPercentage": 0.20,
      "description": "适合波动较大的市场，收益较高但风险也较高",
      "expectedReturnRate": 0.15,
      "riskLevel": "HIGH"
    }
  ]
}
```

---

---

## 2. 网格机器人运行监控

### 2.1 查询网格机器人列表

**接口地址**: `GET /robot/grid/list`

**接口描述**: 查询用户的所有网格机器人列表，支持多种筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选: SPOT, FUTURES_USDT, FUTURES_COIN |
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选: STOPPED（已停止）, RUNNING（运行中）, PAUSED（已暂停）, SETTLED（已结算） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /robot/grid/list?marketType=SPOT&status=RUNNING&page=1&size=20
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
      "robotName": "BTC/USDT现货网格机器人",
      "pairName": "BTC/USDT",
      "marketType": "SPOT",
      "strategyType": "GRID",
      "status": "RUNNING",
      "gridCount": 10,
      "upperPrice": 51000.00000000,
      "lowerPrice": 49000.00000000,
      "startPrice": 50000.00000000,
      "currentPrice": 50200.00000000,
      "initialCapital": 10000.00000000,
      "currentCapital": 10100.00000000,
      "totalProfit": 100.00000000,
      "totalLoss": 0.00000000,
      "netProfit": 100.00000000,
      "profitRate": 0.0100,
      "totalTrades": 25,
      "completedTrades": 20,
      "activeOrders": 5,
      "currentPosition": 0.20000000,
      "startTime": "2024-01-16T10:00:00",
      "lastExecutionTime": "2024-01-16T11:30:00",
      "createdAt": "2024-01-16T09:00:00",
      "updatedAt": "2024-01-16T11:30:00"
    }
  ]
}
```

---

### 2.2 查询网格机器人详情

**接口地址**: `GET /robot/grid/{robotId}`

**接口描述**: 查询指定网格机器人的详细信息，包括实时状态、持仓情况、盈亏等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
GET /robot/grid/1001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "robotName": "BTC/USDT现货网格机器人",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "GRID",
    "status": "RUNNING",
    "gridCount": 10,
    "upperPrice": 51000.00000000,
    "lowerPrice": 49000.00000000,
    "startPrice": 50000.00000000,
    "currentPrice": 50200.00000000,
    "initialCapital": 10000.00000000,
    "currentCapital": 10100.00000000,
    "availableCapital": 5000.00000000,
    "usedCapital": 5000.00000000,
    "totalProfit": 100.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 100.00000000,
    "profitRate": 0.0100,
    "totalTrades": 25,
    "completedTrades": 20,
    "winningTrades": 15,
    "losingTrades": 5,
    "winRate": 0.7500,
    "activeOrders": 5,
    "currentPosition": 0.20000000,
    "averageBuyPrice": 49900.00000000,
    "averageSellPrice": 50100.00000000,
    "stopLossPrice": 48000.00000000,
    "takeProfitPrice": 52000.00000000,
    "stopLossTriggered": false,
    "takeProfitTriggered": false,
    "startTime": "2024-01-16T10:00:00",
    "lastExecutionTime": "2024-01-16T11:30:00",
    "createdAt": "2024-01-16T09:00:00",
    "updatedAt": "2024-01-16T11:30:00"
  }
}
```

---

### 2.3 查询网格机器人实时状态

**接口地址**: `GET /robot/grid/{robotId}/status`

**接口描述**: 查询网格机器人的实时运行状态，包括当前价格、持仓、订单等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
GET /robot/grid/1001/status
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "robotId": 1001,
    "status": "RUNNING",
    "currentPrice": 50200.00000000,
    "currentPosition": 0.20000000,
    "activeBuyOrders": 3,
    "activeSellOrders": 2,
    "pendingOrders": [
      {
        "orderId": 2001,
        "side": "BUY",
        "price": 49900.00000000,
        "quantity": 0.10000000,
        "status": "PENDING"
      },
      {
        "orderId": 2002,
        "side": "SELL",
        "price": 50100.00000000,
        "quantity": 0.10000000,
        "status": "PENDING"
      }
    ],
    "unrealizedProfit": 40.00000000,
    "realizedProfit": 100.00000000,
    "totalProfit": 140.00000000,
    "lastExecutionTime": "2024-01-16T11:30:00"
  }
}
```

---

### 2.4 查询网格机器人交易记录

**接口地址**: `GET /robot/grid/{robotId}/trades`

**接口描述**: 查询网格机器人的所有交易记录，包括买入、卖出、成交时间、价格、盈亏等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| side | String | 否 | 方向筛选: BUY（买入）, SELL（卖出） |
| action | String | 否 | 操作筛选: OPEN（开仓）, CLOSE（平仓） |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /robot/grid/1001/trades?side=BUY&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 3001,
      "robotId": 1001,
      "userId": 1,
      "marketType": "SPOT",
      "orderId": 2001,
      "pairName": "BTC/USDT",
      "action": "OPEN",
      "side": "BUY",
      "quantity": 0.10000000,
      "price": 49900.00000000,
      "amount": 4990.00000000,
      "fee": 2.49500000,
      "profitLoss": null,
      "strategyType": "GRID",
      "gridLevel": 5,
      "createdAt": "2024-01-16T10:05:00"
    },
    {
      "id": 3002,
      "robotId": 1001,
      "userId": 1,
      "marketType": "SPOT",
      "orderId": 2002,
      "pairName": "BTC/USDT",
      "action": "CLOSE",
      "side": "SELL",
      "quantity": 0.10000000,
      "price": 50100.00000000,
      "amount": 5010.00000000,
      "fee": 2.50500000,
      "profitLoss": 15.00000000,
      "strategyType": "GRID",
      "gridLevel": 5,
      "createdAt": "2024-01-16T10:15:00"
    }
  ]
}
```

---

### 2.5 查询网格机器人订单列表

**接口地址**: `GET /robot/grid/{robotId}/orders`

**接口描述**: 查询网格机器人的所有订单（包括待成交和已成交）

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选: PENDING（待成交）, FILLED（已成交）, CANCELLED（已取消） |
| side | String | 否 | 方向筛选: BUY, SELL |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /robot/grid/1001/orders?status=PENDING
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "orderId": 2003,
      "robotId": 1001,
      "pairName": "BTC/USDT",
      "side": "BUY",
      "price": 49800.00000000,
      "quantity": 0.10000000,
      "amount": 4980.00000000,
      "filledQuantity": 0.00000000,
      "status": "PENDING",
      "gridLevel": 4,
      "createdAt": "2024-01-16T11:00:00"
    },
    {
      "orderId": 2004,
      "robotId": 1001,
      "pairName": "BTC/USDT",
      "side": "SELL",
      "price": 50200.00000000,
      "quantity": 0.10000000,
      "amount": 5020.00000000,
      "filledQuantity": 0.00000000,
      "status": "PENDING",
      "gridLevel": 6,
      "createdAt": "2024-01-16T11:00:00"
    }
  ]
}
```

---

### 2.6 查询网格机器人持仓情况

**接口地址**: `GET /robot/grid/{robotId}/positions`

**接口描述**: 查询网格机器人的当前持仓情况（合约市场）

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
GET /robot/grid/1002/positions
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "robotId": 1002,
    "marketType": "FUTURES_USDT",
    "positions": [
      {
        "positionId": 4001,
        "side": "LONG",
        "quantity": 0.20000000,
        "entryPrice": 50000.00000000,
        "currentPrice": 50200.00000000,
        "unrealizedPnl": 40.00000000,
        "leverage": 10,
        "margin": 1000.00000000
      }
    ],
    "totalPosition": 0.20000000,
    "totalUnrealizedPnl": 40.00000000
  }
}
```

---

---

## 3. 网格机器人控制操作

### 3.1 启动网格机器人

**接口地址**: `POST /robot/grid/{robotId}/start`

**接口描述**: 启动指定的网格机器人，开始执行网格交易策略

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
POST /robot/grid/1001/start
```

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人启动成功",
  "data": {
    "id": 1001,
    "status": "RUNNING",
    "startTime": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.2 暂停网格机器人

**接口地址**: `POST /robot/grid/{robotId}/pause`

**接口描述**: 暂停网格机器人，暂停后不会创建新订单，但已创建的订单会继续执行

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
POST /robot/grid/1001/pause
```

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人暂停成功",
  "data": {
    "id": 1001,
    "status": "PAUSED",
    "pausedAt": "2024-01-16T11:00:00",
    "updatedAt": "2024-01-16T11:00:00"
  }
}
```

---

### 3.3 恢复网格机器人

**接口地址**: `POST /robot/grid/{robotId}/resume`

**接口描述**: 恢复已暂停的网格机器人

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
POST /robot/grid/1001/resume
```

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人恢复成功",
  "data": {
    "id": 1001,
    "status": "RUNNING",
    "resumedAt": "2024-01-16T11:05:00",
    "updatedAt": "2024-01-16T11:05:00"
  }
}
```

---

### 3.4 停止网格机器人

**接口地址**: `POST /robot/grid/{robotId}/stop`

**接口描述**: 停止网格机器人，停止时会取消所有未成交订单，并平掉所有持仓（合约市场），然后结算当前盈亏

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| closePositions | Boolean | 否 | 是否平掉所有持仓（合约市场，默认true） |
| cancelOrders | Boolean | 否 | 是否取消所有未成交订单（默认true） |

**请求示例**:
```
POST /robot/grid/1001/stop?closePositions=true&cancelOrders=true
```

**响应示例**:
```json
{
  "code": 200,
  "message": "网格机器人停止成功",
  "data": {
    "id": 1001,
    "status": "STOPPED",
    "stoppedAt": "2024-01-16T12:00:00",
    "finalProfit": 150.00000000,
    "finalProfitRate": 0.0150,
    "totalTrades": 30,
    "cancelledOrders": 5,
    "closedPositions": 1,
    "settlementStatus": "COMPLETED",
    "updatedAt": "2024-01-16T12:00:05"
  }
}
```

---

### 3.5 结算网格机器人

**接口地址**: `POST /robot/grid/{robotId}/settle`

**接口描述**: 结算已停止的网格机器人，将盈亏转入用户账户

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求示例**:
```
POST /robot/grid/1001/settle
```

**响应示例**:
```json
{
  "code": 200,
  "message": "结算成功",
  "data": {
    "robotId": 1001,
    "settlementId": "ST20240116120001",
    "initialCapital": 10000.00000000,
    "finalCapital": 10150.00000000,
    "totalProfit": 150.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 150.00000000,
    "totalFees": 50.00000000,
    "settlementAmount": 10150.00000000,
    "settlementTime": "2024-01-16T12:00:00",
    "status": "SETTLED"
  }
}
```

---

## 4. 网格机器人参数调整

### 4.1 更新网格机器人参数

**接口地址**: `PUT /robot/grid/{robotId}/settings`

**接口描述**: 更新网格机器人的参数（仅停止或暂停状态可更新）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
```json
{
  "robotName": "BTC/USDT现货网格机器人（已更新）",
  "gridCount": 15,
  "upperPrice": 52000.00,
  "lowerPrice": 48000.00,
  "startPrice": 50000.00,
  "initialCapital": 12000.00,
  "investmentRatio": 0.6,
  "stopLossPrice": 47000.00,
  "takeProfitPrice": 53000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| robotName | String | 否 | 机器人名称 |
| gridCount | Integer | 否 | 网格数量 |
| upperPrice | BigDecimal | 否 | 卖出区间上限价格 |
| lowerPrice | BigDecimal | 否 | 买入区间下限价格 |
| startPrice | BigDecimal | 否 | 起始价格 |
| initialCapital | BigDecimal | 否 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPrice | BigDecimal | 否 | 止损价格 |
| takeProfitPrice | BigDecimal | 否 | 止盈价格 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "参数更新成功",
  "data": {
    "id": 1001,
    "robotName": "BTC/USDT现货网格机器人（已更新）",
    "gridCount": 15,
    "upperPrice": 52000.00000000,
    "lowerPrice": 48000.00000000,
    "startPrice": 50000.00000000,
    "initialCapital": 12000.00000000,
    "investmentRatio": 0.6000,
    "stopLossPrice": 47000.00000000,
    "takeProfitPrice": 53000.00000000,
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

---

### 4.2 智能调整网格参数

**接口地址**: `POST /robot/grid/{robotId}/auto-adjust`

**接口描述**: 根据市场波动情况，智能调整网格参数（价格区间、网格数量等）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
```json
{
  "adjustType": "AUTO",
  "volatilityPeriod": 24,
  "targetGridCount": null,
  "adjustPriceRange": true
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| adjustType | String | 是 | 调整类型: AUTO（自动）, MANUAL（手动） |
| volatilityPeriod | Integer | 否 | 波动率计算周期（小时，默认24） |
| targetGridCount | Integer | 否 | 目标网格数量（手动调整时必填） |
| adjustPriceRange | Boolean | 否 | 是否调整价格区间（默认true） |

**响应示例**:
```json
{
  "code": 200,
  "message": "智能调整成功",
  "data": {
    "robotId": 1001,
    "oldGridCount": 10,
    "newGridCount": 12,
    "oldUpperPrice": 51000.00000000,
    "newUpperPrice": 51500.00000000,
    "oldLowerPrice": 49000.00000000,
    "newLowerPrice": 48500.00000000,
    "adjustmentReason": "市场波动率增加，扩大价格区间并增加网格数量",
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

---

### 4.3 查询参数调整历史

**接口地址**: `GET /robot/grid/{robotId}/adjustment-history`

**接口描述**: 查询网格机器人的参数调整历史记录

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /robot/grid/1001/adjustment-history?page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "adjustmentId": 5001,
      "robotId": 1001,
      "adjustType": "AUTO",
      "oldGridCount": 10,
      "newGridCount": 12,
      "oldUpperPrice": 51000.00000000,
      "newUpperPrice": 51500.00000000,
      "oldLowerPrice": 49000.00000000,
      "newLowerPrice": 48500.00000000,
      "adjustmentReason": "市场波动率增加",
      "adjustedAt": "2024-01-16T12:00:00"
    }
  ]
}
```

---

---

## 5. 回测功能

### 5.1 创建回测任务

**接口地址**: `POST /robot/grid/backtest/create`

**接口描述**: 创建网格策略回测任务，使用历史数据验证策略有效性

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "marketType": "SPOT",
  "gridCount": 10,
  "upperPrice": 51000.00,
  "lowerPrice": 49000.00,
  "startPrice": 50000.00,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "startTime": "2024-01-01 00:00:00",
  "endTime": "2024-01-15 23:59:59",
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| marketType | String | 是 | 市场类型: SPOT, FUTURES_USDT, FUTURES_COIN |
| gridCount | Integer | 是 | 网格数量 |
| upperPrice | BigDecimal | 是 | 卖出区间上限价格 |
| lowerPrice | BigDecimal | 是 | 买入区间下限价格 |
| startPrice | BigDecimal | 是 | 起始价格 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例（默认0.5） |
| startTime | String | 是 | 回测开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 是 | 回测结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| stopLossPrice | BigDecimal | 否 | 止损价格 |
| takeProfitPrice | BigDecimal | 否 | 止盈价格 |
| leverage | Integer | 否 | 杠杆倍数（合约市场） |
| marginMode | String | 否 | 保证金模式（合约市场） |

**响应示例**:
```json
{
  "code": 200,
  "message": "回测任务创建成功",
  "data": {
    "backtestId": "BT20240116100001",
    "status": "PENDING",
    "estimatedDuration": 300,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 5.2 查询回测任务状态

**接口地址**: `GET /robot/grid/backtest/{backtestId}/status`

**接口描述**: 查询回测任务的执行状态

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**请求示例**:
```
GET /robot/grid/backtest/BT20240116100001/status
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "backtestId": "BT20240116100001",
    "status": "RUNNING",
    "progress": 65.5,
    "startTime": "2024-01-16T10:00:00",
    "estimatedCompletionTime": "2024-01-16T10:05:00"
  }
}
```

---

### 5.3 查询回测结果

**接口地址**: `GET /robot/grid/backtest/{backtestId}/result`

**接口描述**: 查询回测任务的详细结果，包括盈亏、交易统计、收益率等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**请求示例**:
```
GET /robot/grid/backtest/BT20240116100001/result
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "backtestId": "BT20240116100001",
    "status": "COMPLETED",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "startTime": "2024-01-01T00:00:00",
    "endTime": "2024-01-15T23:59:59",
    "initialCapital": 10000.00000000,
    "finalCapital": 10800.00000000,
    "totalProfit": 800.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 800.00000000,
    "profitRate": 0.0800,
    "annualizedReturnRate": 0.1920,
    "totalTrades": 150,
    "winningTrades": 120,
    "losingTrades": 30,
    "winRate": 0.8000,
    "averageProfit": 6.67000000,
    "averageLoss": -5.00000000,
    "profitLossRatio": 1.3340,
    "maxDrawdown": 200.00000000,
    "maxDrawdownRate": 0.0200,
    "sharpeRatio": 1.8500,
    "totalFees": 75.00000000,
    "gridCount": 10,
    "completedAt": "2024-01-16T10:05:00"
  }
}
```

---

### 5.4 查询回测交易记录

**接口地址**: `GET /robot/grid/backtest/{backtestId}/trades`

**接口描述**: 查询回测任务的所有交易记录

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| side | String | 否 | 方向筛选: BUY, SELL |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /robot/grid/backtest/BT20240116100001/trades?page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "tradeId": 6001,
      "backtestId": "BT20240116100001",
      "tradeTime": "2024-01-01T01:00:00",
      "side": "BUY",
      "price": 49900.00000000,
      "quantity": 0.10000000,
      "amount": 4990.00000000,
      "fee": 2.49500000,
      "gridLevel": 5
    },
    {
      "tradeId": 6002,
      "backtestId": "BT20240116100001",
      "tradeTime": "2024-01-01T02:00:00",
      "side": "SELL",
      "price": 50100.00000000,
      "quantity": 0.10000000,
      "amount": 5010.00000000,
      "fee": 2.50500000,
      "profitLoss": 15.00000000,
      "gridLevel": 5
    }
  ]
}
```

---

### 5.5 查询回测任务列表

**接口地址**: `GET /robot/grid/backtest/list`

**接口描述**: 查询用户的所有回测任务列表

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选: PENDING（待执行）, RUNNING（执行中）, COMPLETED（已完成）, FAILED（失败） |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**请求示例**:
```
GET /robot/grid/backtest/list?status=COMPLETED&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "backtestId": "BT20240116100001",
      "pairName": "BTC/USDT",
      "marketType": "SPOT",
      "status": "COMPLETED",
      "startTime": "2024-01-01T00:00:00",
      "endTime": "2024-01-15T23:59:59",
      "profitRate": 0.0800,
      "totalTrades": 150,
      "winRate": 0.8000,
      "createdAt": "2024-01-16T10:00:00",
      "completedAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

## 6. 历史表现分析

### 6.1 查询网格机器人历史表现

**接口地址**: `GET /robot/grid/{robotId}/performance`

**接口描述**: 查询网格机器人的历史表现数据，包括盈亏比例、最大回撤、收益率等指标

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 统计周期: TODAY（今天）, WEEK（本周）, MONTH（本月）, YEAR（本年）, ALL（全部，默认） |

**请求示例**:
```
GET /robot/grid/1001/performance?period=MONTH
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "robotId": 1001,
    "period": "MONTH",
    "initialCapital": 10000.00000000,
    "currentCapital": 10800.00000000,
    "totalProfit": 800.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 800.00000000,
    "profitRate": 0.0800,
    "annualizedReturnRate": 0.9600,
    "totalTrades": 150,
    "winningTrades": 120,
    "losingTrades": 30,
    "winRate": 0.8000,
    "averageProfit": 6.67000000,
    "averageLoss": -5.00000000,
    "profitLossRatio": 1.3340,
    "maxDrawdown": 200.00000000,
    "maxDrawdownRate": 0.0200,
    "sharpeRatio": 1.8500,
    "totalFees": 75.00000000,
    "netProfitAfterFees": 725.00000000,
    "dailyAverageProfit": 26.67000000,
    "dailyAverageTrades": 5.00
  }
}
```

---

### 6.2 查询网格机器人收益趋势

**接口地址**: `GET /robot/grid/{robotId}/profit-trend`

**接口描述**: 查询网格机器人的收益趋势数据，用于图表展示

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 周期: 7D（7天）, 30D（30天）, 90D（90天）, 180D（180天）, 360D（360天）。默认30D |
| interval | String | 否 | 数据间隔: HOUR（小时）, DAY（天）。默认DAY |

**请求示例**:
```
GET /robot/grid/1001/profit-trend?period=30D&interval=DAY
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "robotId": 1001,
    "period": "30D",
    "interval": "DAY",
    "trendData": [
      {
        "date": "2024-01-01",
        "capital": 10000.00000000,
        "profit": 0.00000000,
        "profitRate": 0.0000,
        "trades": 0
      },
      {
        "date": "2024-01-02",
        "capital": 10050.00000000,
        "profit": 50.00000000,
        "profitRate": 0.0050,
        "trades": 5
      },
      {
        "date": "2024-01-16",
        "capital": 10800.00000000,
        "profit": 800.00000000,
        "profitRate": 0.0800,
        "trades": 150
      }
    ]
  }
}
```

---

### 6.3 查询网格机器人交易统计

**接口地址**: `GET /robot/grid/{robotId}/trade-statistics`

**接口描述**: 查询网格机器人的交易统计数据

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 统计周期: TODAY, WEEK, MONTH, YEAR, ALL |

**请求示例**:
```
GET /robot/grid/1001/trade-statistics?period=MONTH
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "robotId": 1001,
    "period": "MONTH",
    "totalTrades": 150,
    "buyTrades": 75,
    "sellTrades": 75,
    "winningTrades": 120,
    "losingTrades": 30,
    "winRate": 0.8000,
    "averageTradeAmount": 5000.00000000,
    "totalTradeAmount": 750000.00000000,
    "averageProfit": 6.67000000,
    "averageLoss": -5.00000000,
    "maxSingleProfit": 50.00000000,
    "maxSingleLoss": -20.00000000,
    "dailyAverageTrades": 5.00,
    "hourlyAverageTrades": 0.21
  }
}
```

---

---

## 7. 后台管理接口

### 7.1 网格策略管理

#### 7.1.1 创建默认网格策略

**接口地址**: `POST /admin/robot/grid/default-strategy/create`

**接口描述**: 后台创建平台默认网格策略

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "保守型网格策略",
  "marketType": "SPOT",
  "gridCount": 10,
  "defaultUpperPrice": null,
  "defaultLowerPrice": null,
  "defaultStopLossPercentage": 0.05,
  "defaultTakeProfitPercentage": 0.10,
  "description": "适合震荡市场，风险较低",
  "expectedReturnRate": 0.08,
  "riskLevel": "LOW",
  "recommendedPairs": ["BTC/USDT", "ETH/USDT"]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "默认策略创建成功",
  "data": {
    "id": 1,
    "strategyName": "保守型网格策略",
    "marketType": "SPOT",
    "gridCount": 10,
    "riskLevel": "LOW",
    "status": "ACTIVE",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

#### 7.1.2 更新默认网格策略

**接口地址**: `PUT /admin/robot/grid/default-strategy/{strategyId}`

**接口描述**: 后台更新默认网格策略

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
```json
{
  "strategyName": "保守型网格策略（已更新）",
  "gridCount": 12,
  "defaultStopLossPercentage": 0.06,
  "expectedReturnRate": 0.10
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "默认策略更新成功",
  "data": {
    "id": 1,
    "strategyName": "保守型网格策略（已更新）",
    "gridCount": 12,
    "updatedAt": "2024-01-16T10:05:00"
  }
}
```

---

#### 7.1.3 删除默认网格策略

**接口地址**: `DELETE /admin/robot/grid/default-strategy/{strategyId}`

**接口描述**: 后台删除默认网格策略

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "默认策略删除成功",
  "data": null
}
```

---

### 7.2 机器人管理（后台）

#### 7.2.1 查询所有网格机器人

**接口地址**: `GET /admin/robot/grid/list`

**接口描述**: 后台查询所有用户的网格机器人列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| marketType | String | 否 | 市场类型筛选 |
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
        "robotName": "BTC/USDT现货网格机器人",
        "pairName": "BTC/USDT",
        "marketType": "SPOT",
        "status": "RUNNING",
        "totalProfit": 800.00000000,
        "totalTrades": 150,
        "createdAt": "2024-01-16T09:00:00"
      }
    ],
    "totalElements": 1000,
    "totalPages": 50
  }
}
```

---

#### 7.2.2 查询网格机器人详情（后台）

**接口地址**: `GET /admin/robot/grid/{robotId}`

**接口描述**: 后台查询指定网格机器人的详细信息

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "robotName": "BTC/USDT现货网格机器人",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "status": "RUNNING",
    "gridCount": 10,
    "totalProfit": 800.00000000,
    "totalTrades": 150,
    "userInfo": {
      "userId": 1,
      "username": "user001",
      "totalAssets": 50000.00000000
    }
  }
}
```

---

#### 7.2.3 强制停止网格机器人

**接口地址**: `POST /admin/robot/grid/{robotId}/force-stop`

**接口描述**: 后台强制停止网格机器人（紧急情况使用）

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reason | String | 是 | 强制停止原因 |

**请求示例**:
```
POST /admin/robot/grid/1001/force-stop?reason=SYSTEM_MAINTENANCE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "强制停止成功",
  "data": {
    "robotId": 1001,
    "status": "STOPPED",
    "stoppedAt": "2024-01-16T12:00:00",
    "reason": "SYSTEM_MAINTENANCE"
  }
}
```

---

### 7.3 用户权限管理

#### 7.3.1 设置最低资产要求

**接口地址**: `PUT /admin/robot/grid/min-asset-requirement`

**接口描述**: 设置创建网格机器人的最低资产要求

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "marketType": "SPOT",
  "minAsset": 1000.00,
  "currency": "USDT"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| marketType | String | 是 | 市场类型: SPOT, FUTURES_USDT, FUTURES_COIN |
| minAsset | BigDecimal | 是 | 最低资产要求 |
| currency | String | 是 | 币种，如: USDT, BTC |

**响应示例**:
```json
{
  "code": 200,
  "message": "最低资产要求设置成功",
  "data": {
    "marketType": "SPOT",
    "minAsset": 1000.00000000,
    "currency": "USDT",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

#### 7.3.2 查询最低资产要求

**接口地址**: `GET /admin/robot/grid/min-asset-requirement`

**接口描述**: 查询各市场类型的最低资产要求

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "marketType": "SPOT",
      "minAsset": 1000.00000000,
      "currency": "USDT"
    },
    {
      "marketType": "FUTURES_USDT",
      "minAsset": 500.00000000,
      "currency": "USDT"
    },
    {
      "marketType": "FUTURES_COIN",
      "minAsset": 0.01,
      "currency": "BTC"
    }
  ]
}
```

---

### 7.4 数据统计与分析

#### 7.4.1 网格策略表现统计

**接口地址**: `GET /admin/robot/grid/statistics/strategy-performance`

**接口描述**: 查询网格策略的表现统计，包括每种策略的盈亏情况、最大回撤、交易频率等

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期: DAY, WEEK, MONTH |

**请求示例**:
```
GET /admin/robot/grid/statistics/strategy-performance?period=MONTH
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "MONTH",
    "totalRobots": 1000,
    "activeRobots": 800,
    "stoppedRobots": 200,
    "totalProfit": 100000.00000000,
    "totalLoss": 20000.00000000,
    "netProfit": 80000.00000000,
    "averageProfitRate": 0.0800,
    "averageWinRate": 0.7500,
    "averageMaxDrawdown": 0.0200,
    "averageSharpeRatio": 1.8500,
    "strategyDistribution": {
      "保守型": 400,
      "稳健型": 400,
      "激进型": 200
    },
    "performanceByMarketType": {
      "SPOT": {
        "totalRobots": 600,
        "averageProfitRate": 0.0700
      },
      "FUTURES_USDT": {
        "totalRobots": 300,
        "averageProfitRate": 0.1000
      },
      "FUTURES_COIN": {
        "totalRobots": 100,
        "averageProfitRate": 0.0900
      }
    }
  }
}
```

---

#### 7.4.2 用户参与情况统计

**接口地址**: `GET /admin/robot/grid/statistics/user-participation`

**接口描述**: 查询用户参与网格交易的活跃情况

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "MONTH",
    "totalUsers": 500,
    "activeUsers": 400,
    "newUsers": 100,
    "averageRobotsPerUser": 2.5,
    "totalRobotsCreated": 1000,
    "totalRobotsRunning": 800,
    "totalTrades": 150000,
    "totalTradeVolume": 75000000.00000000,
    "userDistribution": {
      "1-5 robots": 300,
      "6-10 robots": 150,
      "11+ robots": 50
    },
    "popularPairs": [
      {
        "pairName": "BTC/USDT",
        "robotCount": 400,
        "totalProfit": 40000.00000000
      },
      {
        "pairName": "ETH/USDT",
        "robotCount": 300,
        "totalProfit": 30000.00000000
      }
    ]
  }
}
```

---

#### 7.4.3 风险监控统计

**接口地址**: `GET /admin/robot/grid/statistics/risk-monitoring`

**接口描述**: 查询网格机器人的风险监控统计

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
    "totalRobots": 1000,
    "highRiskRobots": 50,
    "mediumRiskRobots": 100,
    "lowRiskRobots": 850,
    "stopLossTriggered": 20,
    "takeProfitTriggered": 150,
    "maxDrawdownExceeded": 10,
    "riskAlerts": 30,
    "averageMarginRatio": 0.5000,
    "riskDistribution": {
      "LOW": 850,
      "MEDIUM": 100,
      "HIGH": 50
    }
  }
}
```

---

---

## 8. 数据字典

### 8.1 市场类型（marketType）
- `SPOT`：现货市场
- `FUTURES_USDT`：USDT本位合约市场
- `FUTURES_COIN`：币本位合约市场
- `LEVERAGED`：杠杆交易市场

### 8.2 策略类型（strategyType）
- `GRID`：网格交易策略

### 8.3 机器人状态（status）
- `STOPPED`：已停止
- `RUNNING`：运行中
- `PAUSED`：已暂停
- `SETTLED`：已结算

### 8.4 交易方向（side）
- `BUY`：买入
- `SELL`：卖出

### 8.5 交易操作（action）
- `OPEN`：开仓
- `CLOSE`：平仓

### 8.6 保证金模式（marginMode）
- `ISOLATED`：逐仓
- `CROSS`：全仓

### 8.7 调整类型（adjustType）
- `AUTO`：自动调整
- `MANUAL`：手动调整

### 8.8 回测状态（status）
- `PENDING`：待执行
- `RUNNING`：执行中
- `COMPLETED`：已完成
- `FAILED`：失败

### 8.9 风险等级（riskLevel）
- `LOW`：低风险
- `MEDIUM`：中风险
- `HIGH`：高风险

### 8.10 统计周期（period）
- `TODAY`：今天
- `WEEK`：本周
- `MONTH`：本月
- `YEAR`：本年
- `ALL`：全部

### 8.11 趋势数据间隔（interval）
- `HOUR`：小时
- `DAY`：天

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
  "message": "网格数量必须在5-50之间",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "初始资金不足，最低要求1000 USDT",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "价格区间设置错误，上限价格必须大于下限价格",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "机器人正在运行中，无法修改参数",
  "data": null
}
```

```json
{
  "code": 404,
  "message": "网格机器人不存在",
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

1. **网格机器人创建**：
   - 需要满足最低资产要求（由后台配置）
   - 网格数量建议在5-50之间，过多会增加手续费成本
   - 价格区间应该根据市场波动情况合理设置

2. **市场选择**：
   - 现货市场：适合长期持有，风险相对较低
   - 合约市场：支持杠杆，收益和风险都更高
   - 币本位合约：使用标的资产作为保证金

3. **参数设置**：
   - 起始价格：建议设置为当前市场价格或预期价格
   - 价格区间：应该覆盖预期的价格波动范围
   - 止损止盈：建议设置止损和止盈，控制风险

4. **运行监控**：
   - 定期查看机器人运行状态和盈亏情况
   - 当市场出现大幅波动时，及时调整参数或停止机器人
   - 关注风险警报，及时处理异常情况

5. **智能调整**：
   - 系统可以根据市场波动自动调整网格参数
   - 用户也可以手动调整参数，但需要先暂停机器人
   - 调整参数后建议先进行回测验证

6. **回测功能**：
   - 回测使用历史数据，结果仅供参考
   - 实际交易结果可能与回测结果有差异
   - 建议在不同市场环境下进行多次回测

7. **停止与结算**：
   - 停止机器人时会取消所有未成交订单
   - 合约市场会平掉所有持仓
   - 结算后盈亏会转入用户账户

8. **风险控制**：
   - 网格交易适合震荡市场，单边市场可能亏损
   - 建议设置合理的止损和止盈
   - 不要将所有资金投入一个机器人

9. **手续费成本**：
   - 网格交易会产生较多交易，注意手续费成本
   - 频繁交易可能侵蚀利润
   - 建议选择手续费较低的交易对

10. **杠杆使用**（合约市场）：
    - 杠杆可以放大收益，但也放大风险
    - 建议根据自身风险承受能力选择合适的杠杆倍数
    - 高杠杆可能导致快速强平

---

## 11. 网格交易策略说明

### 11.1 网格交易原理

网格交易是一种在价格区间内设置多个买卖点的交易策略。当价格下跌到买入网格时，自动买入；当价格上涨到卖出网格时，自动卖出。通过反复买卖赚取差价。

### 11.2 网格数量选择

- **少量网格（5-10）**：适合波动较大的市场，单次收益较高，但交易频率较低
- **中等网格（10-20）**：适合一般波动市场，收益和频率平衡
- **大量网格（20-50）**：适合波动较小的市场，交易频率高，但单次收益较低

### 11.3 价格区间设置

- **窄区间**：适合震荡较小的市场，交易频率高，但可能错过趋势
- **宽区间**：适合波动较大的市场，能捕获更多波动，但资金占用时间较长

### 11.4 止损止盈设置

- **止损**：当价格跌破止损价时，机器人会停止并平仓，避免更大亏损
- **止盈**：当价格达到止盈价时，机器人会停止并平仓，锁定利润

### 11.5 智能调整机制

系统会根据市场波动率自动调整：
- **波动率增加**：扩大价格区间，增加网格数量
- **波动率减少**：缩小价格区间，减少网格数量
- **趋势明显**：暂停或调整策略

---

**文档版本**：v2.0  
**最后更新**：2024-01-16  
**文档状态**：已完成





