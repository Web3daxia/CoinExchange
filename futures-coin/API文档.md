# 币本位永续合约模块 API 文档

## 模块说明
币本位永续合约模块提供币本位（如BTC）的永续合约交易功能，包括市场数据、订单管理、仓位管理、保证金模式、梯度杠杆、交易机器人、合约策略、风险管理等。

## Base URL
```
/api/coin-futures
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 市场数据接口

### 1.1 获取市场数据

**接口地址**: `GET /api/coin-futures/market/{pair}`

**接口描述**: 获取指定币本位合约的实时价格、标记价格、指数价格、资金费率等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称，如BTC/BTC |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/BTC",
    "currentPrice": 50000.00,
    "markPrice": 50001.00,
    "indexPrice": 50000.50,
    "fundingRate": 0.0001,
    "nextFundingTime": "2024-01-01T12:00:00",
    "openInterest": 10000.00,
    "volume24h": 5000.00,
    "priceChange24h": 2.5
  }
}
```

### 1.2 获取K线数据

**接口地址**: `GET /api/coin-futures/market/{pair}/chart`

### 1.3 获取市场深度

**接口地址**: `GET /api/coin-futures/market/{pair}/depth`

---

## 2. 订单管理接口

### 2.1 提交订单

**接口地址**: `POST /api/coin-futures/order`

**请求参数**:
```json
{
  "pairName": "BTC/BTC",
  "orderType": "LIMIT",
  "side": "BUY",
  "positionSide": "LONG",
  "price": 50000.00,
  "quantity": 0.1,
  "leverage": 10
}
```

### 2.2 取消订单

**接口地址**: `POST /api/coin-futures/order/cancel`

### 2.3 查询订单状态

**接口地址**: `GET /api/coin-futures/order/status`

### 2.4 查询订单历史

**接口地址**: `GET /api/coin-futures/order/history`

---

## 3. 仓位管理接口

### 3.1 查询仓位列表

**接口地址**: `GET /api/coin-futures/account/positions`

### 3.2 查询单个仓位

**接口地址**: `GET /api/coin-futures/account/position/{positionId}`

---

## 4. 保证金模式接口

### 4.1 设置保证金模式

**接口地址**: `POST /api/coin-futures/account/margin`

### 4.2 查询保证金模式

**接口地址**: `GET /api/coin-futures/account/margin`

### 4.3 创建分仓

**接口地址**: `POST /api/coin-futures/account/segment`

### 4.4 查询分仓列表

**接口地址**: `GET /api/coin-futures/account/segments`

---

## 5. 梯度杠杆接口

### 5.1 查询梯度杠杆规则

**接口地址**: `GET /api/coin-futures/account/gradient`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| positionQuantity | BigDecimal | 是 | 仓位数量 |

### 5.2 调整杠杆

**接口地址**: `POST /api/coin-futures/order/gradient-adjust`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 仓位ID |
| newLeverage | Integer | 是 | 新杠杆倍数 |

---

## 6. 高级订单接口

### 6.1 创建高级订单

**接口地址**: `POST /api/coin-futures/order/advanced`

### 6.2 取消高级订单

**接口地址**: `POST /api/coin-futures/order/advanced/cancel`

### 6.3 查询高级订单列表

**接口地址**: `GET /api/coin-futures/order/advanced/list`

---

## 7. 交易机器人接口

### 7.1 配置交易机器人策略

**接口地址**: `POST /api/coin-futures/robot/configure`

**接口描述**: 配置币本位永续合约交易机器人（网格、趋势、反转策略）

**请求参数**:
```json
{
  "robotName": "网格交易机器人",
  "pairName": "BTC/BTC",
  "strategyType": "GRID",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "positionSide": "LONG",
  "strategyParams": {
    "gridCount": 10,
    "upperPrice": 51000,
    "lowerPrice": 49000
  }
}
```

**策略类型**:
- `GRID`: 网格策略
- `TREND`: 趋势策略
- `REVERSAL`: 反转策略

**响应示例**:
```json
{
  "code": 200,
  "message": "机器人配置成功",
  "data": {
    "id": 1,
    "userId": 1,
    "robotName": "网格交易机器人",
    "pairName": "BTC/BTC",
    "strategyType": "GRID",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "positionSide": "LONG",
    "totalProfit": 0.00,
    "totalLoss": 0.00,
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 7.2 启动交易机器人

**接口地址**: `POST /api/coin-futures/robot/start`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| robotId | Long | 是 | 机器人ID |

### 7.3 停止交易机器人

**接口地址**: `POST /api/coin-futures/robot/stop`

### 7.4 查询交易机器人状态

**接口地址**: `GET /api/coin-futures/robot/status`

### 7.5 查询机器人列表

**接口地址**: `GET /api/coin-futures/robot/list`

---

## 8. 合约策略接口

### 8.1 配置合约策略

**接口地址**: `POST /api/coin-futures/strategy/configure`

**接口描述**: 配置币本位永续合约策略（套利、对冲、跨期套利）

**请求参数**:
```json
{
  "strategyName": "套利策略",
  "strategyType": "ARBITRAGE",
  "pairName": "BTC/BTC",
  "strategyParams": {
    "priceDiff": 100,
    "maxPosition": 1.0
  }
}
```

**策略类型**:
- `ARBITRAGE`: 套利策略
- `HEDGE`: 对冲策略
- `SPREAD`: 跨期套利策略

### 8.2 执行合约策略

**接口地址**: `POST /api/coin-futures/strategy/execute`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

### 8.3 停止合约策略

**接口地址**: `POST /api/coin-futures/strategy/stop`

### 8.4 查询策略列表

**接口地址**: `GET /api/coin-futures/strategy/list`

### 8.5 查询策略状态

**接口地址**: `GET /api/coin-futures/strategy/status`

---

## 9. 风险管理接口

### 9.1 查询账户风险

**接口地址**: `GET /api/coin-futures/account/risk`

**接口描述**: 查询币本位永续合约账户的风险状况，包括保证金率、风险等级等

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "totalMargin": 10000.00,
    "usedMargin": 5000.00,
    "availableMargin": 5000.00,
    "marginRatio": 0.5,
    "riskLevel": "NORMAL",
    "liquidationRisk": false,
    "positions": [
      {
        "positionId": 1,
        "pairName": "BTC/BTC",
        "unrealizedPnl": 100.00,
        "marginRatio": 0.1
      }
    ]
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
| 500 | 服务器内部错误 |

## 注意事项

1. 币本位合约使用标的币种作为保证金
2. 交易机器人需要足够的保证金才能运行
3. 策略执行需要符合风险控制规则
4. 风险管理会实时监控账户风险状况
5. 梯度杠杆根据仓位大小自动调整














