# 自交易模块API文档

## 基础信息

- **模块名称**：自交易模块（Self Trading / Internal Trading）
- **用户端基础路径**：`/api/self-trading`
- **后台管理基础路径**：`/admin/self-trading`
- **认证方式**：Bearer Token (JWT)

**说明**：自交易是指用户在同一账户内进行买卖操作，系统会自动匹配买单和卖单。主要用于测试交易功能、生成交易流水、提高账户活跃度等场景。

---

## 用户端API

### 1. 自交易管理

#### 1.1 创建自交易订单

**接口地址**：`POST /api/self-trading/order/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "quantity": "0.10000000",
  "price": "50000.00000000",
  "autoMatch": true
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型：MARKET（市价）, LIMIT（限价） |
| side | String | 是 | 买卖方向：BUY（买入）, SELL（卖出） |
| quantity | String | 是 | 交易数量 |
| price | String | 条件 | 委托价格（限价单必填） |
| autoMatch | Boolean | 否 | 是否自动匹配（默认true） |

**响应示例**：
```json
{
  "code": 200,
  "message": "自交易订单创建成功",
  "data": {
    "orderId": 123456,
    "orderNo": "ST-20240101120000-123456",
    "pairName": "BTC/USDT",
    "side": "BUY",
    "orderType": "LIMIT",
    "quantity": "0.10000000",
    "price": "50000.00000000",
    "status": "FILLED",
    "matchOrderId": 123457,
    "createTime": "2024-01-01T12:00:00",
    "matchTime": "2024-01-01T12:00:01"
  }
}
```

#### 1.2 查询自交易订单列表

**接口地址**：`GET /api/self-trading/order/list`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选：PENDING, FILLED, CANCELLED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/self-trading/order/list?page=0&size=20
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
        "orderNo": "ST-20240101120000-123456",
        "pairName": "BTC/USDT",
        "side": "BUY",
        "orderType": "LIMIT",
        "quantity": "0.10000000",
        "price": "50000.00000000",
        "status": "FILLED",
        "matchOrderId": 123457,
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 1.3 查询自交易订单详情

**接口地址**：`GET /api/self-trading/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

#### 1.4 取消自交易订单

**接口地址**：`POST /api/self-trading/order/{orderId}/cancel`

**请求头**：
```
X-User-Id: 1
```

---

### 2. 自交易统计

#### 2.1 获取自交易统计

**接口地址**：`GET /api/self-trading/statistics`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 统计周期：DAY（日）, WEEK（周）, MONTH（月）, ALL（全部） |

**请求示例**：
```
GET /api/self-trading/statistics?period=MONTH
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalOrders": 100,
    "filledOrders": 80,
    "totalVolume": "10.00000000",
    "totalAmount": "500000.00000000",
    "averagePrice": "50000.00000000",
    "todayOrders": 10,
    "todayVolume": "1.00000000",
    "monthlyOrders": 50,
    "monthlyVolume": "5.00000000"
  }
}
```

---

## 后台管理API

### 1. 自交易管理

#### 1.1 获取所有自交易订单

**接口地址**：`GET /admin/self-trading/order/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 1.2 获取自交易统计（后台）

**接口地址**：`GET /admin/self-trading/statistics`

---

## 订单状态说明

- **PENDING**：待成交
- **FILLED**：已成交
- **CANCELLED**：已取消
- **PARTIAL_FILLED**：部分成交

## 自交易说明

自交易是指用户在同一账户内进行买卖操作，系统会自动匹配买单和卖单，用于：
- 测试交易功能
- 生成交易流水
- 提高账户活跃度

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

