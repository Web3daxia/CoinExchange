# 交割合约模块 API 文档

## 模块说明
交割合约模块提供交割合约交易功能，包括订单管理、仓位管理、市场数据查询等。

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

## 1. 订单管理接口

### 1.1 创建订单

**接口地址**: `POST /delivery-contract/order`

**接口描述**: 创建交割合约订单

**请求参数**:
```json
{
  "contractId": 1,
  "orderType": "LIMIT",
  "side": "BUY",
  "positionSide": "LONG",
  "price": 50000.00,
  "quantity": 0.1,
  "leverage": 10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| contractId | Long | 是 | 合约ID |
| orderType | String | 是 | 订单类型: MARKET, LIMIT |
| side | String | 是 | 方向: BUY, SELL |
| positionSide | String | 是 | 仓位方向: LONG, SHORT |
| price | BigDecimal | 条件 | 价格（限价单必填） |
| quantity | BigDecimal | 是 | 数量 |
| leverage | Integer | 是 | 杠杆倍数 |

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
    "price": 50000.00,
    "quantity": 0.1,
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 1.2 取消订单

**接口地址**: `POST /delivery-contract/order/{orderId}/cancel`

**接口描述**: 取消未成交的订单

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "订单取消成功",
  "data": null
}
```

### 1.3 获取订单列表

**接口地址**: `GET /delivery-contract/orders`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 订单状态（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1234567890,
      "contractId": 1,
      "orderType": "LIMIT",
      "side": "BUY",
      "price": 50000.00,
      "quantity": 0.1,
      "filledQuantity": 0.1,
      "status": "FILLED",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

---

## 2. 仓位管理接口

### 2.1 获取持仓列表

**接口地址**: `GET /delivery-contract/positions`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "contractId": 1,
      "positionSide": "LONG",
      "quantity": 0.1,
      "entryPrice": 50000.00,
      "currentPrice": 50200.00,
      "unrealizedPnl": 20.00,
      "margin": 500.00,
      "leverage": 10,
      "liquidationPrice": 45000.00,
      "deliveryDate": "2024-01-31T00:00:00"
    }
  ]
}
```

### 2.2 平仓

**接口地址**: `POST /delivery-contract/position/{positionId}/close`

**接口描述**: 平掉指定持仓

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionId | Long | 是 | 持仓ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| quantity | BigDecimal | 否 | 平仓数量（不填则全部平仓） |

**响应示例**:
```json
{
  "code": 200,
  "message": "平仓成功",
  "data": {
    "id": 1234567890,
    "contractId": 1,
    "side": "SELL",
    "quantity": 0.1,
    "price": 50200.00,
    "status": "FILLED",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

---

## 3. 市场数据接口

### 3.1 获取行情数据

**接口地址**: `GET /delivery-contract/market-data/{contractId}`

**接口描述**: 获取合约的实时行情数据

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| contractId | Long | 是 | 合约ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "contractId": 1,
    "contractName": "BTC/USDT-20240131",
    "currentPrice": 50000.00,
    "markPrice": 50001.00,
    "indexPrice": 50000.50,
    "openInterest": 100000.00,
    "volume24h": 5000000.00,
    "priceChange24h": 2.5,
    "high24h": 51000.00,
    "low24h": 49000.00,
    "deliveryDate": "2024-01-31T00:00:00"
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

1. 交割合约有固定的交割日期
2. 合约到期前需要平仓，否则会进行交割
3. 交割价格通常使用标记价格或指数价格
4. 持仓到交割日期会自动交割
5. 交割合约与永续合约的主要区别是有到期日














