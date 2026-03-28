# 现货交易模块 API 文档

## 模块说明
现货交易模块提供现货市场的交易功能，包括市场数据查询、订单管理、高级订单、市场异动提醒等。

## Base URL
- 现货交易: `/spot`
- 高级订单: `/spot/order/advanced`
- 市场提醒: `/market/alerts`

## 认证
交易相关接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 市场数据接口

### 1.1 获取交易对市场数据

**接口地址**: `GET /spot/market/{pair}`

**接口描述**: 获取指定交易对的实时价格、涨跌幅、成交量等

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
    "priceChange24h": 2.5,
    "volume24h": 1000000.00,
    "high24h": 51000.00,
    "low24h": 49000.00,
    "bidPrice": 49999.00,
    "askPrice": 50001.00,
    "lastPrice": 50000.00,
    "amount24h": 50000000.00,
    "bids": [
      {"price": 49999.00, "quantity": 1.5},
      {"price": 49998.00, "quantity": 2.0}
    ],
    "asks": [
      {"price": 50001.00, "quantity": 1.2},
      {"price": 50002.00, "quantity": 1.8}
    ]
  }
}
```

### 1.2 获取K线数据

**接口地址**: `GET /spot/market/{pair}/chart`

**接口描述**: 获取指定交易对的K线数据

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称 |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interval | String | 否 | 时间粒度: 1m, 5m, 15m, 30m, 1h, 4h, 1d, 1w（默认1h） |
| limit | Integer | 否 | 数据条数（默认100，最大1000） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "interval": "1h",
    "klines": [
      {
        "timestamp": 1704067200000,
        "open": 50000.00,
        "high": 50500.00,
        "low": 49800.00,
        "close": 50200.00,
        "volume": 1000.00
      }
    ]
  }
}
```

### 1.3 获取市场深度

**接口地址**: `GET /spot/market/{pair}/depth`

**接口描述**: 获取指定交易对的买卖盘深度

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 深度条数（默认20，最大100） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "bids": [
      {"price": 49999.00, "quantity": 1.5},
      {"price": 49998.00, "quantity": 2.0}
    ],
    "asks": [
      {"price": 50001.00, "quantity": 1.2},
      {"price": 50002.00, "quantity": 1.8}
    ],
    "timestamp": 1704067200000
  }
}
```

---

## 2. 订单管理接口

### 2.1 提交现货订单

**接口地址**: `POST /spot/order`

**接口描述**: 提交买入或卖出订单

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "price": 50000.00,
  "quantity": 0.1
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型: MARKET（市价）, LIMIT（限价） |
| side | String | 是 | 方向: BUY（买入）, SELL（卖出） |
| price | BigDecimal | 条件 | 价格（限价单必填） |
| quantity | BigDecimal | 是 | 数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "订单提交成功",
  "data": {
    "id": 1234567890,
    "userId": 1,
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "price": 50000.00,
    "quantity": 0.1,
    "filledQuantity": 0.0,
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 2.2 查询订单状态

**接口地址**: `GET /spot/order/{orderId}`

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1234567890,
    "userId": 1,
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "price": 50000.00,
    "quantity": 0.1,
    "filledQuantity": 0.05,
    "avgPrice": 49999.00,
    "status": "PARTIAL_FILLED",
    "fee": 2.5,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:05:00"
  }
}
```

### 2.3 查询订单历史

**接口地址**: `GET /spot/order/history`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
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
        "id": 1234567890,
        "pairName": "BTC/USDT",
        "orderType": "LIMIT",
        "side": "BUY",
        "price": 50000.00,
        "quantity": 0.1,
        "filledQuantity": 0.1,
        "avgPrice": 49999.00,
        "status": "FILLED",
        "fee": 5.0,
        "createdAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

### 2.4 取消订单

**接口地址**: `POST /spot/order/{orderId}/cancel`

**接口描述**: 取消待成交状态的订单

**响应示例**:
```json
{
  "code": 200,
  "message": "订单取消成功",
  "data": null
}
```

---

## 3. 高级订单接口

### 3.1 创建高级订单

**接口地址**: `POST /spot/order/advanced`

**接口描述**: 创建高级限价单、分时委托、循环委托、追踪委托、冰山策略等

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "orderType": "ADVANCED_LIMIT",
  "side": "BUY",
  "price": 50000.00,
  "quantity": 0.1,
  "timeInForce": "GTC",
  "advancedType": "ICEBERG",
  "advancedParams": {
    "visibleQuantity": 0.01,
    "totalQuantity": 0.1
  }
}
```

**高级订单类型说明**:
- `ADVANCED_LIMIT`: 高级限价单（支持GTC/IOC/FOK）
- `TIME_WEIGHTED`: 分时委托
- `ICEBERG`: 冰山策略
- `TRAILING`: 追踪委托
- `SEGMENTED`: 分段委托

**响应示例**:
```json
{
  "code": 200,
  "message": "高级订单创建成功",
  "data": {
    "id": 1234567890,
    "userId": 1,
    "pairName": "BTC/USDT",
    "orderType": "ADVANCED_LIMIT",
    "advancedType": "ICEBERG",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 3.2 查询高级订单列表

**接口地址**: `GET /spot/order/advanced/list`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1234567890,
      "pairName": "BTC/USDT",
      "orderType": "ADVANCED_LIMIT",
      "advancedType": "ICEBERG",
      "status": "PENDING",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 3.3 取消高级订单

**接口地址**: `POST /spot/order/advanced/{orderId}/cancel`

---

## 4. 市场异动提醒接口

### 4.1 设置市场异动提醒

**接口地址**: `POST /market/alerts/set`

**接口描述**: 设置价格或成交量异动提醒

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "alertType": "PRICE",
  "condition": "GREATER_THAN",
  "targetPrice": 51000.00,
  "notificationMethod": "EMAIL"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| alertType | String | 是 | 提醒类型: PRICE（价格）, VOLUME（成交量） |
| condition | String | 是 | 条件: GREATER_THAN, LESS_THAN |
| targetPrice | BigDecimal | 条件 | 目标价格（价格提醒时必填） |
| targetVolume | BigDecimal | 条件 | 目标成交量（成交量提醒时必填） |
| notificationMethod | String | 是 | 通知方式: EMAIL, SMS, PUSH |

**响应示例**:
```json
{
  "code": 200,
  "message": "提醒设置成功",
  "data": {
    "id": 1,
    "userId": 1,
    "pairName": "BTC/USDT",
    "alertType": "PRICE",
    "targetPrice": 51000.00,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 4.2 查询提醒列表

**接口地址**: `GET /market/alerts/list`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "alertType": "PRICE",
      "targetPrice": 51000.00,
      "status": "ACTIVE",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 4.3 查询提醒状态

**接口地址**: `GET /market/alerts/{alertId}`

### 4.4 删除提醒

**接口地址**: `DELETE /market/alerts/{alertId}`

### 4.5 启用/禁用提醒

**接口地址**: `POST /market/alerts/{alertId}/toggle`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enabled | Boolean | 是 | 是否启用 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |

## 注意事项

1. 订单状态: PENDING（待成交）、PARTIAL_FILLED（部分成交）、FILLED（完全成交）、CANCELLED（已取消）、REJECTED（已拒绝）
2. 市价单会立即成交，限价单需要等待价格匹配
3. 高级订单功能需要账户有足够的余额
4. 市场提醒触发后会立即发送通知
5. K线数据支持多个时间粒度，可根据需求选择
6. 订单取消只能取消PENDING状态的订单














