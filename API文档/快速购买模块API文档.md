# 快速购买模块API文档

## 基础信息

- **模块名称**：快速购买模块（Quick Buy）
- **用户端基础路径**：`/api/quick-buy`
- **后台管理基础路径**：`/admin/quick-buy`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 快速购买

#### 1.1 获取支持的币种列表

**接口地址**：`GET /api/quick-buy/supported-currencies`

**请求示例**：
```
GET /api/quick-buy/supported-currencies
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "currency": "BTC",
      "currencyName": "Bitcoin",
      "minAmount": "0.00010000",
      "maxAmount": "1.00000000",
      "price": "50000.00000000",
      "feeRate": "0.01000000",
      "status": "ACTIVE"
    },
    {
      "currency": "ETH",
      "currencyName": "Ethereum",
      "minAmount": "0.01000000",
      "maxAmount": "10.00000000",
      "price": "3000.00000000",
      "feeRate": "0.01000000",
      "status": "ACTIVE"
    }
  ]
}
```

#### 1.2 创建快速购买订单

**接口地址**：`POST /api/quick-buy/order/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "currency": "BTC",
  "paymentMethod": "CREDIT_CARD",
  "amount": "0.01000000",
  "paymentCurrency": "USD",
  "paymentAmount": "500.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 购买的币种 |
| paymentMethod | String | 是 | 支付方式：CREDIT_CARD（信用卡）, BANK_TRANSFER（银行转账）, ALIPAY（支付宝）, WECHAT（微信） |
| amount | String | 条件 | 购买数量（按数量购买时必填） |
| paymentCurrency | String | 是 | 支付货币：USD, CNY, EUR等 |
| paymentAmount | String | 条件 | 支付金额（按金额购买时必填） |

**响应示例**：
```json
{
  "code": 200,
  "message": "快速购买订单创建成功",
  "data": {
    "orderId": 123456,
    "orderNo": "QB-20240101120000-123456",
    "currency": "BTC",
    "amount": "0.01000000",
    "paymentCurrency": "USD",
    "paymentAmount": "500.00000000",
    "fee": "5.00000000",
    "actualAmount": "0.00995000",
    "status": "PENDING_PAYMENT",
    "paymentUrl": "https://payment.example.com/pay/xxx",
    "createTime": "2024-01-01T12:00:00",
    "expireTime": "2024-01-01T12:15:00"
  }
}
```

#### 1.3 查询快速购买订单状态

**接口地址**：`GET /api/quick-buy/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /api/quick-buy/order/123456
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "orderId": 123456,
    "orderNo": "QB-20240101120000-123456",
    "currency": "BTC",
    "amount": "0.01000000",
    "paymentCurrency": "USD",
    "paymentAmount": "500.00000000",
    "fee": "5.00000000",
    "actualAmount": "0.00995000",
    "status": "SUCCESS",
    "createTime": "2024-01-01T12:00:00",
    "payTime": "2024-01-01T12:05:00",
    "completeTime": "2024-01-01T12:05:30"
  }
}
```

#### 1.4 查询我的快速购买订单列表

**接口地址**：`GET /api/quick-buy/order/list`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：PENDING_PAYMENT, SUCCESS, FAILED, CANCELLED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 后台管理API

### 1. 快速购买配置

#### 1.1 获取支持的币种配置

**接口地址**：`GET /admin/quick-buy/currencies`

#### 1.2 添加支持的币种

**接口地址**：`POST /admin/quick-buy/currency/add`

**请求参数**：
```json
{
  "currency": "BTC",
  "minAmount": "0.00010000",
  "maxAmount": "1.00000000",
  "feeRate": "0.01000000",
  "status": "ACTIVE"
}
```

#### 1.3 更新币种配置

**接口地址**：`PUT /admin/quick-buy/currency/update/{currency}`

#### 1.4 删除币种配置

**接口地址**：`DELETE /admin/quick-buy/currency/{currency}`

---

### 2. 订单管理

#### 2.1 获取所有快速购买订单

**接口地址**：`GET /admin/quick-buy/order/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| currency | String | 否 | 币种筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 订单状态说明

- **PENDING_PAYMENT**：待支付
- **PAID**：已支付（待确认）
- **SUCCESS**：成功
- **FAILED**：失败
- **CANCELLED**：已取消
- **EXPIRED**：已过期

## 支付方式说明

- **CREDIT_CARD**：信用卡
- **BANK_TRANSFER**：银行转账
- **ALIPAY**：支付宝
- **WECHAT**：微信支付
- **PAYPAL**：PayPal

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














