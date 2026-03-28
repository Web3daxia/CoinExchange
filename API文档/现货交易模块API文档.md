# 现货交易模块API文档

## 基础信息

- **模块名称**：现货交易模块（Spot Trading）
- **用户端基础路径**：`/spot`
- **后台管理基础路径**：`/admin/spot`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 市场数据

#### 1.1 获取交易对市场数据

**接口地址**：`GET /spot/market/{pair}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称，如BTC/USDT |

**请求示例**：
```
GET /spot/market/BTC%2FUSDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "currentPrice": "50000.00000000",
    "priceChange24h": "2.50000000",
    "priceChangePercent24h": "2.50",
    "volume24h": "1000000.00000000",
    "high24h": "51000.00000000",
    "low24h": "49000.00000000",
    "bidPrice": "49999.00000000",
    "askPrice": "50001.00000000",
    "lastPrice": "50000.00000000",
    "amount24h": "50000000.00000000"
  }
}
```

#### 1.2 获取K线数据

**接口地址**：`GET /spot/market/{pair}/chart`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称 |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interval | String | 否 | 时间粒度：1m, 5m, 15m, 30m, 1h, 4h, 1d, 1w（默认1h） |
| limit | Integer | 否 | 数据条数（默认100，最大1000） |

**请求示例**：
```
GET /spot/market/BTC%2FUSDT/chart?interval=1h&limit=100
```

**响应示例**：
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
        "open": "50000.00000000",
        "high": "50500.00000000",
        "low": "49800.00000000",
        "close": "50200.00000000",
        "volume": "1000.00000000"
      }
    ]
  }
}
```

#### 1.3 获取市场深度

**接口地址**：`GET /spot/market/{pair}/depth`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称 |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 深度档位（默认20，最大100） |

**请求示例**：
```
GET /spot/market/BTC%2FUSDT/depth?limit=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "bids": [
      {"price": "49999.00000000", "quantity": "1.50000000"},
      {"price": "49998.00000000", "quantity": "2.00000000"}
    ],
    "asks": [
      {"price": "50001.00000000", "quantity": "1.20000000"},
      {"price": "50002.00000000", "quantity": "1.80000000"}
    ]
  }
}
```

#### 1.4 获取最近成交记录

**接口地址**：`GET /spot/market/{pair}/trades`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称 |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 数据条数（默认100，最大1000） |

**请求示例**：
```
GET /spot/market/BTC%2FUSDT/trades?limit=100
```

---

### 2. 交易下单

#### 2.1 限价单下单

**接口地址**：`POST /spot/order/limit`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "quantity": "0.10000000",
  "price": "50000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 买卖方向：BUY（买入）, SELL（卖出） |
| quantity | String | 是 | 交易数量 |
| price | String | 是 | 委托价格 |

**响应示例**：
```json
{
  "code": 200,
  "message": "下单成功",
  "data": {
    "orderId": 123456,
    "orderNo": "SO-20240101120000-123456",
    "pairName": "BTC/USDT",
    "side": "BUY",
    "orderType": "LIMIT",
    "quantity": "0.10000000",
    "price": "50000.00000000",
    "status": "PENDING",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 市价单下单

**接口地址**：`POST /spot/order/market`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "quantity": "0.10000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 买卖方向：BUY, SELL |
| quantity | String | 是 | 交易数量（买入时使用） |
| amount | String | 否 | 交易金额（卖出时使用） |

---

### 3. 订单管理

#### 3.1 查询当前订单

**接口地址**：`GET /spot/order/current`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| side | String | 否 | 方向筛选：BUY, SELL |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /spot/order/current?pairName=BTC%2FUSDT&page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "content": [
      {
        "orderId": 123456,
        "orderNo": "SO-20240101120000-123456",
        "pairName": "BTC/USDT",
        "side": "BUY",
        "orderType": "LIMIT",
        "quantity": "0.10000000",
        "filledQuantity": "0.00000000",
        "price": "50000.00000000",
        "status": "PENDING",
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

#### 3.2 查询历史订单

**接口地址**：`GET /spot/order/history`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| side | String | 否 | 方向筛选 |
| status | String | 否 | 状态筛选：FILLED, CANCELLED, PARTIAL_FILLED |
| startTime | String | 否 | 开始时间（格式：yyyy-MM-ddTHH:mm:ss） |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 3.3 查询订单详情

**接口地址**：`GET /spot/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

#### 3.4 取消订单

**接口地址**：`POST /spot/order/cancel/{orderId}`

**请求头**：
```
X-User-Id: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
POST /spot/order/cancel/123456
```

**响应示例**：
```json
{
  "code": 200,
  "message": "订单取消成功",
  "data": null
}
```

#### 3.5 批量取消订单

**接口地址**：`POST /spot/order/cancel-batch`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "orderIds": [123456, 123457]
}
```

---

### 4. 高级订单

#### 4.1 创建高级订单

**接口地址**：`POST /spot/order/advanced`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "orderType": "STOP_LOSS",
  "side": "BUY",
  "quantity": "0.10000000",
  "triggerPrice": "48000.00000000",
  "price": "50000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型：STOP_LOSS（止损）, TAKE_PROFIT（止盈）, TRAILING_STOP（追踪止损）, ICEBERG（冰山）, TWAP（分时委托） |
| side | String | 是 | 买卖方向 |
| quantity | String | 是 | 交易数量 |
| triggerPrice | String | 条件 | 触发价格（止损/止盈时必填） |
| price | String | 条件 | 委托价格 |
| trailingPercent | String | 条件 | 追踪百分比（追踪止损时必填） |
| displayQuantity | String | 条件 | 显示数量（冰山订单时必填） |

**响应示例**：
```json
{
  "code": 200,
  "message": "高级订单创建成功",
  "data": {
    "orderId": 123456,
    "orderNo": "SO-20240101120000-123456",
    "orderType": "STOP_LOSS",
    "status": "PENDING",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 4.2 查询高级订单列表

**接口地址**：`GET /spot/order/advanced/list`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderType | String | 否 | 订单类型筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

### 5. 市场异动提醒

#### 5.1 设置市场异动提醒

**接口地址**：`POST /market/alerts/set`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "alertType": "PRICE",
  "condition": "GREATER_THAN",
  "targetValue": "51000.00000000",
  "notificationType": "PUSH"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| alertType | String | 是 | 提醒类型：PRICE（价格）, VOLUME（成交量） |
| condition | String | 是 | 条件：GREATER_THAN（大于）, LESS_THAN（小于）, EQUAL（等于） |
| targetValue | String | 是 | 目标值 |
| notificationType | String | 是 | 通知类型：PUSH（推送）, EMAIL（邮件）, SMS（短信） |

**响应示例**：
```json
{
  "code": 200,
  "message": "提醒设置成功",
  "data": {
    "alertId": 1,
    "pairName": "BTC/USDT",
    "alertType": "PRICE",
    "status": "ACTIVE",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 5.2 查询提醒列表

**接口地址**：`GET /market/alerts/list`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /market/alerts/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "alertId": 1,
      "pairName": "BTC/USDT",
      "alertType": "PRICE",
      "condition": "GREATER_THAN",
      "targetValue": "51000.00000000",
      "status": "ACTIVE",
      "createTime": "2024-01-01T12:00:00"
    }
  ]
}
```

#### 5.3 查询提醒状态

**接口地址**：`GET /market/alerts/{alertId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| alertId | Long | 是 | 提醒ID |

#### 5.4 删除提醒

**接口地址**：`DELETE /market/alerts/{alertId}`

**请求头**：
```
X-User-Id: 1
```

#### 5.5 启用/禁用提醒

**接口地址**：`POST /market/alerts/{alertId}/toggle`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enabled | Boolean | 是 | 是否启用 |

---

## 后台管理API

### 1. 交易对管理

#### 1.1 创建交易对

**接口地址**：`POST /admin/spot/trading-pair/create`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "baseCurrency": "BTC",
  "quoteCurrency": "USDT",
  "feeRate": "0.00100000",
  "pricePrecision": 2,
  "quantityPrecision": 8,
  "minOrderAmount": "10.00000000",
  "maxOrderAmount": "1000000.00000000",
  "status": "ACTIVE"
}
```

#### 1.2 更新交易对

**接口地址**：`POST /admin/spot/trading-pair/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 交易对ID |

#### 1.3 删除交易对

**接口地址**：`DELETE /admin/spot/trading-pair/{id}`

#### 1.4 查询交易对列表

**接口地址**：`GET /admin/spot/trading-pair/list`

#### 1.5 查询可用交易对

**接口地址**：`GET /admin/spot/trading-pair/active`

#### 1.6 查询交易对详情

**接口地址**：`GET /admin/spot/trading-pair/{id}`

---

## 订单类型说明

### 基础订单类型
- **LIMIT（限价单）**：指定价格下单
- **MARKET（市价单）**：按市场价格立即成交

### 高级订单类型
- **STOP_LOSS（止损单）**：价格达到触发价时下单
- **TAKE_PROFIT（止盈单）**：价格达到目标价时下单
- **TRAILING_STOP（追踪止损）**：随价格变动自动调整止损价
- **ICEBERG（冰山订单）**：大单分批显示
- **TWAP（分时委托）**：按时间加权平均价格执行

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














