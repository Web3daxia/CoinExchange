# 自选交易模块API文档

## 基础信息

- **模块名称**：自选交易模块（Self Trading / P2P Trading）
- **用户端基础路径**：`/self-trading`
- **后台管理基础路径**：`/admin/self-trading`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 商家管理

#### 1.1 申请成为商家

**接口地址**：`POST /self-trading/merchant/apply`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "merchantName": "我的商家",
  "avatar": "https://example.com/avatar.jpg",
  "signature": "诚信商家",
  "bio": "专业交易员",
  "country": "CN",
  "region": "北京",
  "assetProof": "https://example.com/proof.jpg",
  "totalAssets": "1000000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantName | String | 是 | 商家名称 |
| avatar | String | 否 | 头像URL |
| signature | String | 否 | 个性签名 |
| bio | String | 否 | 个人简介 |
| country | String | 是 | 国家代码 |
| region | String | 否 | 地区 |
| assetProof | String | 是 | 资产证明URL |
| totalAssets | String | 是 | 总资产 |

**响应示例**：
```json
{
  "code": 200,
  "message": "申请提交成功",
  "data": {
    "id": 1,
    "userId": 1,
    "merchantName": "我的商家",
    "status": "PENDING",
    "createTime": "2024-01-01T10:00:00"
  }
}
```

#### 1.2 获取商家列表

**接口地址**：`GET /self-trading/market`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| country | String | 否 | 国家筛选 |
| level | String | 否 | 商家等级筛选 |

**请求示例**：
```
GET /self-trading/market?country=CN
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "merchantName": "我的商家",
      "avatar": "https://example.com/avatar.jpg",
      "level": "LEVEL1",
      "country": "CN",
      "region": "北京",
      "totalOrders": 100,
      "completionRate": "98.00",
      "rating": "4.50",
      "status": "ACTIVE"
    }
  ]
}
```

#### 1.3 查询商家详情

**接口地址**：`GET /self-trading/merchant/{merchantId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantId | Long | 是 | 商家ID |

#### 1.4 关注/取消关注商家

**接口地址**：`POST /self-trading/merchant/follow`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantId | Long | 是 | 商家ID |
| follow | Boolean | 是 | 是否关注 |

---

### 2. 广告管理

#### 2.1 创建广告

**接口地址**：`POST /self-trading/create-ad`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "currency": "BTC",
  "tradeType": "BUY",
  "price": "50000.00000000",
  "minAmount": "0.00100000",
  "maxAmount": "1.00000000",
  "paymentMethods": ["ALIPAY", "WECHAT"],
  "remark": "诚信交易，快速放币"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| tradeType | String | 是 | 交易类型：BUY（买入）, SELL（卖出） |
| price | String | 是 | 价格 |
| minAmount | String | 是 | 最小交易金额 |
| maxAmount | String | 是 | 最大交易金额 |
| paymentMethods | String[] | 是 | 支付方式：ALIPAY（支付宝）, WECHAT（微信）, BANK（银行卡） |
| remark | String | 否 | 备注 |

**响应示例**：
```json
{
  "code": 200,
  "message": "广告创建成功",
  "data": {
    "id": 1,
    "merchantId": 1,
    "currency": "BTC",
    "tradeType": "BUY",
    "price": "50000.00000000",
    "status": "ACTIVE",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 查看广告详情

**接口地址**：`GET /self-trading/ad/{adId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| adId | Long | 是 | 广告ID |

---

### 3. 订单管理

#### 3.1 创建订单

**接口地址**：`POST /self-trading/order`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "adId": 1,
  "cryptoAmount": "0.01000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| adId | Long | 是 | 广告ID |
| cryptoAmount | String | 是 | 加密货币数量 |

**响应示例**：
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderId": 123456,
    "orderNo": "ST-20240101120000-123456",
    "adId": 1,
    "merchantId": 1,
    "userId": 2,
    "currency": "BTC",
    "cryptoAmount": "0.01000000",
    "fiatAmount": "500.00000000",
    "price": "50000.00000000",
    "status": "PENDING_PAYMENT",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 3.2 查询订单历史

**接口地址**：`GET /self-trading/orders`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：PENDING_PAYMENT, PAID, RELEASED, COMPLETED, CANCELLED, APPEALING |

**请求示例**：
```
GET /self-trading/orders?status=PENDING_PAYMENT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "orderId": 123456,
      "orderNo": "ST-20240101120000-123456",
      "adId": 1,
      "merchantId": 1,
      "merchantName": "我的商家",
      "currency": "BTC",
      "cryptoAmount": "0.01000000",
      "fiatAmount": "500.00000000",
      "price": "50000.00000000",
      "status": "PENDING_PAYMENT",
      "createTime": "2024-01-01T12:00:00"
    }
  ]
}
```

#### 3.3 确认支付

**接口地址**：`POST /self-trading/order/confirm-payment`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |
| paymentProof | File | 是 | 支付凭证（图片） |

**请求示例**：
```
POST /self-trading/order/confirm-payment
Content-Type: multipart/form-data

orderId=123456
paymentProof=<file>
```

**响应示例**：
```json
{
  "code": 200,
  "message": "支付确认成功",
  "data": {
    "orderId": 123456,
    "status": "PAID",
    "paymentProof": "https://example.com/proof.jpg",
    "updateTime": "2024-01-01T12:05:00"
  }
}
```

#### 3.4 放币

**接口地址**：`POST /self-trading/order/release`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**说明**：只有商家可以执行放币操作。

---

### 4. 聊天对话

#### 4.1 发送消息

**接口地址**：`POST /self-trading/chat/send`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "orderId": 123456,
  "receiverId": 2,
  "messageType": "TEXT",
  "content": "你好，我已经支付了",
  "fileUrl": null
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |
| receiverId | Long | 是 | 接收者ID |
| messageType | String | 是 | 消息类型：TEXT（文本）, IMAGE（图片）, FILE（文件） |
| content | String | 条件 | 消息内容（文本消息时必填） |
| fileUrl | String | 条件 | 文件URL（图片/文件消息时必填） |

**响应示例**：
```json
{
  "code": 200,
  "message": "消息发送成功",
  "data": {
    "id": 1,
    "orderId": 123456,
    "senderId": 1,
    "receiverId": 2,
    "messageType": "TEXT",
    "content": "你好，我已经支付了",
    "createTime": "2024-01-01T12:10:00"
  }
}
```

#### 4.2 查询聊天记录

**接口地址**：`GET /self-trading/chat/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /self-trading/chat/123456
```

---

### 5. 评价系统

#### 5.1 创建评价

**接口地址**：`POST /self-trading/rating`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "merchantId": 1,
  "orderId": 123456,
  "rating": 5,
  "comment": "交易很顺利，商家很诚信",
  "isAnonymous": false
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantId | Long | 是 | 商家ID |
| orderId | Long | 是 | 订单ID |
| rating | Integer | 是 | 评分（1-5分） |
| comment | String | 否 | 评价内容 |
| isAnonymous | Boolean | 否 | 是否匿名（默认false） |

**响应示例**：
```json
{
  "code": 200,
  "message": "评价创建成功",
  "data": {
    "id": 1,
    "merchantId": 1,
    "orderId": 123456,
    "userId": 1,
    "rating": 5,
    "comment": "交易很顺利，商家很诚信",
    "createTime": "2024-01-01T13:00:00"
  }
}
```

#### 5.2 查询商家评价

**接口地址**：`GET /self-trading/rating`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantId | Long | 是 | 商家ID |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

### 6. 商家等级

#### 6.1 获取商家等级信息

**接口地址**：`GET /self-trading/merchant-level`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| merchantId | Long | 是 | 商家ID |

---

## 后台管理API

### 1. 商家管理

#### 1.1 审核商家申请

**接口地址**：`POST /admin/self-trading/merchant/approve/{merchantId}`

**请求参数**：
```json
{
  "approved": true,
  "remark": "审核通过"
}
```

#### 1.2 获取所有商家

**接口地址**：`GET /admin/self-trading/merchant/list`

---

### 2. 订单管理

#### 2.1 获取所有订单

**接口地址**：`GET /admin/self-trading/order/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| merchantId | Long | 否 | 商家ID筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 2.2 处理申诉

**接口地址**：`POST /admin/self-trading/order/appeal`

**请求参数**：
```json
{
  "orderId": 123456,
  "decision": "RELEASE",
  "remark": "申诉处理说明"
}
```

---

## 订单状态说明

- **PENDING_PAYMENT**：待支付
- **PAID**：已支付（待放币）
- **RELEASED**：已放币（待确认）
- **COMPLETED**：已完成
- **CANCELLED**：已取消
- **APPEALING**：申诉中

## 支付方式说明

- **ALIPAY**：支付宝
- **WECHAT**：微信支付
- **BANK**：银行卡转账

## 商家等级说明

- **LEVEL1**：一级商家
- **LEVEL2**：二级商家
- **LEVEL3**：三级商家

商家等级根据交易量、完成率、评价等指标自动提升。

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














