# 闪兑模块API文档

## 基础信息

- **模块名称**：闪兑模块（Flash Exchange）
- **用户端基础路径**：`/api/flash-exchange`
- **后台管理基础路径**：`/admin/flash-exchange`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 闪兑交易

#### 1.1 获取支持的交易对

**接口地址**：`GET /api/flash-exchange/pairs`

**请求示例**：
```
GET /api/flash-exchange/pairs
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "pair": "BTC/USDT",
      "fromCurrency": "BTC",
      "toCurrency": "USDT",
      "minAmount": "0.00100000",
      "maxAmount": "1.00000000",
      "exchangeRate": "50000.00000000",
      "feeRate": "0.00100000",
      "status": "ACTIVE"
    },
    {
      "pair": "ETH/USDT",
      "fromCurrency": "ETH",
      "toCurrency": "USDT",
      "minAmount": "0.01000000",
      "maxAmount": "10.00000000",
      "exchangeRate": "3000.00000000",
      "feeRate": "0.00100000",
      "status": "ACTIVE"
    }
  ]
}
```

#### 1.2 获取实时汇率

**接口地址**：`GET /api/flash-exchange/rate`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fromCurrency | String | 是 | 源币种 |
| toCurrency | String | 是 | 目标币种 |
| amount | String | 否 | 数量（用于计算总金额） |

**请求示例**：
```
GET /api/flash-exchange/rate?fromCurrency=BTC&toCurrency=USDT&amount=0.1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "fromCurrency": "BTC",
    "toCurrency": "USDT",
    "exchangeRate": "50000.00000000",
    "feeRate": "0.00100000",
    "amount": "0.10000000",
    "fee": "5.00000000",
    "receiveAmount": "4995.00000000",
    "validTime": 30,
    "timestamp": "2024-01-01T12:00:00"
  }
}
```

#### 1.3 创建闪兑订单

**接口地址**：`POST /api/flash-exchange/order/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "fromCurrency": "BTC",
  "toCurrency": "USDT",
  "amount": "0.10000000",
  "rate": "50000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fromCurrency | String | 是 | 源币种 |
| toCurrency | String | 是 | 目标币种 |
| amount | String | 是 | 兑换数量 |
| rate | String | 是 | 汇率（需要与实时汇率一致，防止价格波动） |

**响应示例**：
```json
{
  "code": 200,
  "message": "闪兑订单创建成功",
  "data": {
    "orderId": 123456,
    "orderNo": "FE-20240101120000-123456",
    "fromCurrency": "BTC",
    "toCurrency": "USDT",
    "amount": "0.10000000",
    "rate": "50000.00000000",
    "fee": "5.00000000",
    "receiveAmount": "4995.00000000",
    "status": "SUCCESS",
    "createTime": "2024-01-01T12:00:00",
    "completeTime": "2024-01-01T12:00:01"
  }
}
```

#### 1.4 查询闪兑订单状态

**接口地址**：`GET /api/flash-exchange/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /api/flash-exchange/order/123456
```

#### 1.5 查询我的闪兑订单列表

**接口地址**：`GET /api/flash-exchange/order/list`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fromCurrency | String | 否 | 源币种筛选 |
| toCurrency | String | 否 | 目标币种筛选 |
| status | String | 否 | 状态筛选：SUCCESS, FAILED, PENDING |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/flash-exchange/order/list?page=0&size=20
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
        "orderNo": "FE-20240101120000-123456",
        "fromCurrency": "BTC",
        "toCurrency": "USDT",
        "amount": "0.10000000",
        "rate": "50000.00000000",
        "fee": "5.00000000",
        "receiveAmount": "4995.00000000",
        "status": "SUCCESS",
        "createTime": "2024-01-01T12:00:00",
        "completeTime": "2024-01-01T12:00:01"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

---

## 后台管理API

### 1. 闪兑配置

#### 1.1 获取交易对配置列表

**接口地址**：`GET /admin/flash-exchange/pairs`

#### 1.2 添加交易对配置

**接口地址**：`POST /admin/flash-exchange/pair/add`

**请求参数**：
```json
{
  "fromCurrency": "BTC",
  "toCurrency": "USDT",
  "minAmount": "0.00100000",
  "maxAmount": "1.00000000",
  "feeRate": "0.00100000",
  "status": "ACTIVE"
}
```

#### 1.3 更新交易对配置

**接口地址**：`PUT /admin/flash-exchange/pair/update/{pairId}`

#### 1.4 删除交易对配置

**接口地址**：`DELETE /admin/flash-exchange/pair/{pairId}`

---

### 2. 订单管理

#### 2.1 获取所有闪兑订单

**接口地址**：`GET /admin/flash-exchange/order/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| fromCurrency | String | 否 | 源币种筛选 |
| toCurrency | String | 否 | 目标币种筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 订单状态说明

- **PENDING**：待处理
- **SUCCESS**：成功
- **FAILED**：失败
- **CANCELLED**：已取消

## 闪兑说明

闪兑（Flash Exchange）是一种即时兑换服务，用户可以快速将一种加密货币兑换成另一种，无需等待订单撮合。

### 核心特性
- **即时到账**：兑换完成后立即到账
- **固定汇率**：使用下单时的汇率，不受市场波动影响
- **低手续费**：相比传统交易，手续费更低

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














