# 杠杆交易模块 API 文档

## 模块说明
杠杆交易模块提供杠杆交易功能，包括杠杆订单、仓位管理、杠杆设置、交易策略、风险管理等。

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

## 1. 市场数据接口

### 1.1 获取杠杆交易市场数据

**接口地址**: `GET /leveraged/market/{pair}`

**接口描述**: 获取指定交易对的杠杆交易市场数据

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "currentPrice": 50000.00,
    "availableLeverage": [2, 5, 10, 20, 50, 100],
    "maxLeverage": 100,
    "marginRate": 0.01
  }
}
```

---

## 2. 订单接口

### 2.1 创建杠杆订单

**接口地址**: `POST /leveraged/order`

**接口描述**: 提交杠杆交易的开仓或平仓订单

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
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
| orderType | String | 是 | 订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT, STOP_LIMIT, CONDITIONAL |
| side | String | 是 | 方向: BUY, SELL |
| action | String | 是 | 操作: OPEN（开仓）, CLOSE（平仓） |
| quantity | BigDecimal | 是 | 数量 |
| price | BigDecimal | 条件 | 价格（限价单必填） |
| leverage | Integer | 是 | 杠杆倍数 |
| stopPrice | BigDecimal | 条件 | 止损价格（止损单必填） |
| triggerPrice | BigDecimal | 条件 | 触发价格（条件单必填） |

### 2.2 查询订单历史

**接口地址**: `GET /leveraged/order/history`

### 2.3 提交市价单

**接口地址**: `POST /leveraged/order/market`

### 2.4 提交限价单

**接口地址**: `POST /leveraged/order/limit`

### 2.5 提交止损单

**接口地址**: `POST /leveraged/order/stop-loss`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 仓位ID |
| stopPrice | BigDecimal | 是 | 止损价格 |

### 2.6 提交止盈单

**接口地址**: `POST /leveraged/order/take-profit`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 仓位ID |
| takeProfitPrice | BigDecimal | 是 | 止盈价格 |

### 2.7 提交止损限价单

**接口地址**: `POST /leveraged/order/stop-limit`

### 2.8 提交条件单

**接口地址**: `POST /leveraged/order/conditional`

---

## 3. 杠杆设置接口

### 3.1 调整杠杆倍数

**接口地址**: `POST /leveraged/account/leverage`

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "leverage": 20
}
```

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
    "marginRate": 0.05
  }
}
```

### 3.2 查询杠杆倍数

**接口地址**: `GET /leveraged/account/leverage`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |

---

## 4. 仓位管理接口

### 4.1 查询仓位信息

**接口地址**: `GET /leveraged/account/positions`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "side": "LONG",
      "quantity": 0.1,
      "entryPrice": 50000.00,
      "currentPrice": 50200.00,
      "leverage": 10,
      "margin": 500.00,
      "unrealizedPnl": 20.00,
      "realizedPnl": 0.00,
      "liquidationPrice": 45000.00,
      "marginRatio": 0.1,
      "status": "OPEN",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 4.2 查询账户风险

**接口地址**: `GET /leveraged/account/risk`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalMargin": 10000.00,
    "usedMargin": 5000.00,
    "availableMargin": 5000.00,
    "marginRatio": 0.5,
    "riskLevel": "NORMAL",
    "liquidationRisk": false
  }
}
```

### 4.3 补充保证金

**接口地址**: `POST /leveraged/account/top-up`

**请求参数**:
```json
{
  "positionId": 1,
  "amount": 1000.00
}
```

### 4.4 强制平仓

**接口地址**: `POST /leveraged/account/liquidate`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 仓位ID |

---

## 5. 交易策略接口

### 5.1 配置交易策略

**接口地址**: `POST /leveraged/strategy/configure`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 是 | 账户ID |
| strategyName | String | 否 | 策略名称 |
| strategyType | String | 是 | 策略类型 |
| pairName | String | 是 | 交易对名称 |
| strategyParams | String | 否 | 策略参数（JSON字符串） |
| leverage | Integer | 是 | 杠杆倍数 |

### 5.2 执行交易策略

**接口地址**: `POST /leveraged/strategy/execute`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

---

## 6. 交易机器人接口

### 6.1 配置交易机器人

**接口地址**: `POST /leveraged/robot/configure`

### 6.2 启动交易机器人

**接口地址**: `POST /leveraged/robot/start`

### 6.3 停止交易机器人

**接口地址**: `POST /leveraged/robot/stop`

---

## 7. 风险管理接口

### 7.1 设置风险警报

**接口地址**: `POST /leveraged/account/risk-alert`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| accountId | Long | 否 | 账户ID |
| positionId | Long | 否 | 仓位ID |
| alertType | String | 是 | 警报类型 |
| threshold | BigDecimal | 否 | 阈值 |
| thresholdPercentage | BigDecimal | 否 | 阈值百分比 |
| notificationMethod | String | 否 | 通知方式 |

**响应示例**:
```json
{
  "code": 200,
  "message": "风险警报设置成功",
  "data": {
    "id": 1,
    "userId": 1,
    "alertType": "MARGIN_RATIO",
    "threshold": 0.2,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
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

1. 杠杆倍数越高，风险越大
2. 需要维持足够的保证金比例，否则会被强制平仓
3. 止损单和止盈单需要关联现有仓位
4. 补充保证金可以提高仓位安全边际
5. 风险警报会及时通知用户账户风险状况














