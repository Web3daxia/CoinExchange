# 期权合约交易模块 API 文档

## 模块说明
期权合约交易模块提供期权交易功能，包括期权订单、期权定价、期权行使、期权策略等。

## Base URL
```
/options
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 市场数据接口

### 1.1 获取期权市场数据

**接口地址**: `GET /options/market/{pair}`

**接口描述**: 获取指定交易对的期权合约市场数据

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "underlyingPrice": 50000.00,
    "optionContracts": [
      {
        "contractId": 1,
        "optionType": "CALL",
        "strikePrice": 51000.00,
        "expiryDate": "2024-01-31T00:00:00",
        "marketPrice": 1000.00,
        "volume24h": 100.00
      }
    ]
  }
}
```

---

## 2. 订单接口

### 2.1 创建期权订单

**接口地址**: `POST /options/order`

**接口描述**: 提交开仓或平仓订单

**请求参数**:
```json
{
  "contractId": 1,
  "orderType": "LIMIT",
  "side": "BUY",
  "quantity": 1.0,
  "price": 1000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| contractId | Long | 是 | 期权合约ID |
| orderType | String | 是 | 订单类型: MARKET, LIMIT |
| side | String | 是 | 方向: BUY（买入）, SELL（卖出） |
| quantity | BigDecimal | 是 | 数量 |
| price | BigDecimal | 条件 | 价格（限价单必填） |

**响应示例**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "id": 1234567890,
    "contractId": 1,
    "orderType": "LIMIT",
    "side": "BUY",
    "quantity": 1.0,
    "price": 1000.00,
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 2.2 查询订单历史

**接口地址**: `GET /options/order/history`

### 2.3 提交看涨期权订单

**接口地址**: `POST /options/order/call`

**接口描述**: 提交看涨期权开仓订单

### 2.4 提交看跌期权订单

**接口地址**: `POST /options/order/put`

**接口描述**: 提交看跌期权开仓订单

---

## 3. 期权类型接口

### 3.1 查询期权类型

**接口地址**: `GET /options/type`

**接口描述**: 查询期权类型（美式、欧式）

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "exerciseTypes": ["AMERICAN", "EUROPEAN"],
    "optionTypes": ["CALL", "PUT"]
  }
}
```

---

## 4. 期权定价接口

### 4.1 获取期权定价信息

**接口地址**: `GET /options/pricing`

**接口描述**: 获取期权的定价信息（包括市场价格、理论价格、隐含波动率等）

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 期权合约ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "contractId": 1,
    "marketPrice": 1000.00,
    "theoreticalPrice": 980.00,
    "underlyingPrice": 50000.00,
    "strikePrice": 51000.00,
    "timeToExpiry": 30,
    "impliedVolatility": 0.25,
    "intrinsicValue": 0.00,
    "timeValue": 1000.00
  }
}
```

---

## 5. 期权行使接口

### 5.1 行使期权

**接口地址**: `POST /options/exercise`

**接口描述**: 提交期权行使请求

**请求参数**:
```json
{
  "positionId": 1,
  "quantity": 1.0
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "期权行使成功",
  "data": {
    "id": 1,
    "positionId": 1,
    "quantity": 1.0,
    "exercisePrice": 51000.00,
    "status": "SUCCESS",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 5.2 查询期权行使状态

**接口地址**: `GET /options/exercise/status`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |

---

## 6. 期权平仓接口

### 6.1 平仓期权

**接口地址**: `POST /options/close`

**接口描述**: 提交期权平仓订单

**请求参数**:
```json
{
  "contractId": 1,
  "orderType": "CLOSE",
  "side": "SELL",
  "quantity": 1.0,
  "price": 1100.00
}
```

### 6.2 查询平仓历史

**接口地址**: `GET /options/close/history`

---

## 7. 到期日期接口

### 7.1 获取期权到期日期

**接口地址**: `GET /options/expiry`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 期权合约ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "contractId": 1,
    "expiryDate": "2024-01-31T00:00:00",
    "pairName": "BTC/USDT",
    "optionType": "CALL"
  }
}
```

---

## 8. 高级策略接口

### 8.1 创建跨式期权策略

**接口地址**: `POST /options/strategy/straddle`

**接口描述**: 提交跨式期权策略

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| strikePrice | BigDecimal | 是 | 执行价格 |
| expiryDate | LocalDateTime | 是 | 到期日期 |
| quantity | BigDecimal | 是 | 数量 |

### 8.2 创建蝶式期权策略

**接口地址**: `POST /options/strategy/butterfly`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| lowerStrike | BigDecimal | 是 | 较低执行价格 |
| middleStrike | BigDecimal | 是 | 中间执行价格 |
| upperStrike | BigDecimal | 是 | 较高执行价格 |
| expiryDate | LocalDateTime | 是 | 到期日期 |
| quantity | BigDecimal | 是 | 数量 |
| optionType | String | 是 | 期权类型: CALL, PUT |

### 8.3 创建价差策略

**接口地址**: `POST /options/strategy/vertical`

**接口描述**: 提交牛市/熊市价差策略

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| buyStrike | BigDecimal | 是 | 买入执行价格 |
| sellStrike | BigDecimal | 是 | 卖出执行价格 |
| expiryDate | LocalDateTime | 是 | 到期日期 |
| quantity | BigDecimal | 是 | 数量 |
| optionType | String | 是 | 期权类型: CALL, PUT |
| direction | String | 是 | 方向: BULLISH（牛市）, BEARISH（熊市） |

### 8.4 创建日历价差策略

**接口地址**: `POST /options/strategy/calendar`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| strikePrice | BigDecimal | 是 | 执行价格 |
| nearExpiryDate | LocalDateTime | 是 | 近期到期日期 |
| farExpiryDate | LocalDateTime | 是 | 远期到期日期 |
| quantity | BigDecimal | 是 | 数量 |
| optionType | String | 是 | 期权类型: CALL, PUT |

---

## 9. 风险管理接口

### 9.1 查询账户风险

**接口地址**: `GET /options/account/risk`

**接口描述**: 查询期权账户风险情况

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "totalMargin": 10000.00,
    "usedMargin": 5000.00,
    "unrealizedPnl": 500.00,
    "riskLevel": "NORMAL"
  }
}
```

### 9.2 设置止损策略

**接口地址**: `POST /options/account/stop-loss`

**接口描述**: 设置期权强平止损策略

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 否 | 持仓ID（可选） |
| alertType | String | 是 | 提醒类型 |
| threshold | BigDecimal | 否 | 阈值 |
| thresholdPercentage | BigDecimal | 否 | 阈值百分比 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 注意事项

1. 期权类型：CALL（看涨期权）、PUT（看跌期权）
2. 行权类型：AMERICAN（美式，到期前可随时行权）、EUROPEAN（欧式，到期日行权）
3. 期权到期前需及时平仓或行权
4. 期权策略需要同时持有多个期权合约
5. 隐含波动率影响期权定价














