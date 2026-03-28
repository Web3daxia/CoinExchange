# USDT本位永续合约模块 API 文档

## 模块说明
USDT本位永续合约模块提供USDT本位的永续合约交易功能，包括市场数据、订单管理、仓位管理、保证金模式、梯度杠杆等。

## Base URL
```
/api/futures
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

**接口地址**: `GET /api/futures/market/{pair}`

**接口描述**: 获取指定合约的实时价格、标记价格、指数价格、资金费率等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称，如BTC/USDT |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "currentPrice": 50000.00,
    "markPrice": 50001.00,
    "indexPrice": 50000.50,
    "fundingRate": 0.0001,
    "nextFundingTime": "2024-01-01T12:00:00",
    "openInterest": 1000000.00,
    "volume24h": 50000000.00,
    "priceChange24h": 2.5,
    "high24h": 51000.00,
    "low24h": 49000.00
  }
}
```

### 1.2 获取K线数据

**接口地址**: `GET /api/futures/market/{pair}/chart`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interval | String | 否 | 时间粒度（默认1h） |
| limit | Integer | 否 | 数据条数（默认100） |

### 1.3 获取市场深度

**接口地址**: `GET /api/futures/market/{pair}/depth`

---

## 2. 订单管理接口

### 2.1 提交订单（通用接口）

**接口地址**: `POST /api/futures/order`

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "positionSide": "LONG",
  "price": 50000.00,
  "quantity": 0.1,
  "leverage": 10,
  "timeInForce": "GTC"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT, STOP_LIMIT, CONDITIONAL |
| side | String | 是 | 方向: BUY, SELL |
| positionSide | String | 是 | 仓位方向: LONG, SHORT |
| price | BigDecimal | 条件 | 价格（限价单必填） |
| quantity | BigDecimal | 是 | 数量 |
| leverage | Integer | 是 | 杠杆倍数 |
| timeInForce | String | 否 | 有效期: GTC, IOC, FOK（默认GTC） |

### 2.2 提交市价单

**接口地址**: `POST /api/futures/order/market`

### 2.3 提交限价单

**接口地址**: `POST /api/futures/order/limit`

### 2.4 提交止损单

**接口地址**: `POST /api/futures/order/stop-loss`

### 2.5 提交止盈单

**接口地址**: `POST /api/futures/order/take-profit`

### 2.6 提交止损限价单

**接口地址**: `POST /api/futures/order/stop-limit`

### 2.7 提交条件单

**接口地址**: `POST /api/futures/order/conditional`

### 2.8 取消订单

**接口地址**: `POST /api/futures/order/cancel`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

### 2.9 查询订单状态

**接口地址**: `GET /api/futures/order/status`

### 2.10 查询订单历史

**接口地址**: `GET /api/futures/order/history`

---

## 3. 高级订单接口

### 3.1 提交高级限价单

**接口地址**: `POST /api/futures/order/advanced-limit`

**接口描述**: 支持GTC/IOC/FOK时间有效期

### 3.2 提交追踪委托

**接口地址**: `POST /api/futures/order/trailing`

### 3.3 提交追逐限价单

**接口地址**: `POST /api/futures/order/trailing-limit`

### 3.4 提交冰山策略

**接口地址**: `POST /api/futures/order/iceberg`

### 3.5 提交分段委托

**接口地址**: `POST /api/futures/order/segmented`

### 3.6 提交分时委托

**接口地址**: `POST /api/futures/order/time-weighted`

### 3.7 取消高级订单

**接口地址**: `POST /api/futures/order/advanced/cancel`

### 3.8 查询高级订单列表

**接口地址**: `GET /api/futures/order/advanced/list`

---

## 4. 仓位管理接口

### 4.1 查询仓位列表

**接口地址**: `GET /api/futures/account/positions`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "positionSide": "LONG",
      "quantity": 0.1,
      "entryPrice": 50000.00,
      "currentPrice": 50200.00,
      "leverage": 10,
      "margin": 500.00,
      "unrealizedPnl": 20.00,
      "liquidationPrice": 45000.00,
      "marginRatio": 0.1,
      "status": "OPEN"
    }
  ]
}
```

### 4.2 查询单个仓位

**接口地址**: `GET /api/futures/account/position/{positionId}`

---

## 5. 保证金模式接口

### 5.1 设置保证金模式

**接口地址**: `POST /api/futures/account/margin`

**接口描述**: 设置用户的保证金模式（全仓/逐仓/分仓/组合）

**请求参数**:
```json
{
  "marginMode": "ISOLATED",
  "pairName": "BTC/USDT"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| marginMode | String | 是 | 保证金模式: CROSSED（全仓）, ISOLATED（逐仓）, SEGMENTED（分仓）, PORTFOLIO（组合） |
| pairName | String | 条件 | 交易对名称（逐仓模式时必填） |

### 5.2 创建分仓

**接口地址**: `POST /api/futures/account/segment`

**请求参数**:
```json
{
  "segmentName": "策略1",
  "initialMargin": 1000.00
}
```

### 5.3 查询分仓列表

**接口地址**: `GET /api/futures/account/segments`

---

## 6. 梯度杠杆接口

### 6.1 查询梯度杠杆规则

**接口地址**: `GET /api/futures/account/gradient`

**接口描述**: 根据交易对和仓位大小查询适用的梯度杠杆规则

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| positionSize | BigDecimal | 是 | 仓位大小 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "positionSize": 1.0,
    "maxLeverage": 10,
    "initialMarginRate": 0.1,
    "maintenanceMarginRate": 0.05,
    "tier": 1
  }
}
```

### 6.2 提交梯度杠杆调整

**接口地址**: `POST /api/futures/order/gradient-adjust`

**请求参数**:
```json
{
  "positionId": 1,
  "newLeverage": 20
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

1. 杠杆倍数需要符合梯度杠杆规则
2. 全仓模式下，所有仓位共享保证金
3. 逐仓模式下，每个仓位独立保证金
4. 标记价格用于计算未实现盈亏和强平价格
5. 资金费率每8小时结算一次
6. 仓位方向：LONG（做多）、SHORT（做空）














