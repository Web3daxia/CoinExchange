# 红包模块API文档

## 基础信息

- **模块名称**：红包模块（Red Packet）
- **用户端基础路径**：`/api/red-packet`
- **后台管理基础路径**：`/admin/red-packet`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 红包领取

#### 1.1 领取红包

**接口地址**：`POST /api/red-packet/receive`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "activityId": 1
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

**请求示例**：
```json
POST /api/red-packet/receive
Content-Type: application/json

{
  "activityId": 1
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "领取成功",
  "data": {
    "id": 1,
    "packetNo": "RP-20240101120000-123456",
    "userId": 1,
    "activityId": 1,
    "activityName": "新用户注册红包",
    "packetType": "CASH",
    "amount": "10.00000000",
    "currency": "USDT",
    "status": "UNUSED",
    "expireTime": "2024-01-08T12:00:00",
    "receiveTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 获取我的红包列表

**接口地址**：`GET /api/red-packet/my-packets`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：UNUSED（未使用）, USED（已使用）, EXPIRED（已过期） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/red-packet/my-packets?status=UNUSED&page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "content": [
      {
        "id": 1,
        "packetNo": "RP-20240101120000-123456",
        "activityName": "新用户注册红包",
        "packetType": "CASH",
        "amount": "10.00000000",
        "currency": "USDT",
        "status": "UNUSED",
        "expireTime": "2024-01-08T12:00:00",
        "receiveTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

#### 1.3 获取红包详情

**接口地址**：`GET /api/red-packet/packet/{packetId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| packetId | Long | 是 | 红包ID |

**请求示例**：
```
GET /api/red-packet/packet/1
```

---

### 2. 红包使用

#### 2.1 使用红包

**接口地址**：`POST /api/red-packet/use`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "packetId": 1,
  "useScene": "TRADE",
  "relatedId": 123
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| packetId | Long | 是 | 红包ID |
| useScene | String | 是 | 使用场景：TRADE（交易）, FEE_REDUCTION（手续费减免）, WITHDRAW（提现） |
| relatedId | Long | 否 | 关联ID（如订单ID） |

**请求示例**：
```json
POST /api/red-packet/use
Content-Type: application/json

{
  "packetId": 1,
  "useScene": "TRADE",
  "relatedId": 123
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "使用成功",
  "data": {
    "id": 1,
    "useNo": "RU-20240101130000-123456",
    "packetId": 1,
    "packetNo": "RP-20240101120000-123456",
    "amount": "10.00000000",
    "useScene": "TRADE",
    "useTime": "2024-01-01T13:00:00",
    "status": "SUCCESS"
  }
}
```

#### 2.2 获取我的使用记录

**接口地址**：`GET /api/red-packet/use/my-uses`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/red-packet/use/my-uses?page=0&size=20
```

---

### 3. 活动信息

#### 3.1 获取可用活动列表

**接口地址**：`GET /api/red-packet/activity/available`

**请求示例**：
```
GET /api/red-packet/activity/available
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "activityName": "新用户注册红包",
      "activityCode": "NEW_USER_REGISTER",
      "packetType": "CASH",
      "amountType": "FIXED",
      "fixedAmount": "10.00000000",
      "totalAmount": "100000.00000000",
      "totalCount": 10000,
      "remainingCount": 9500,
      "validDays": 7,
      "startTime": "2024-01-01T00:00:00",
      "endTime": "2024-12-31T23:59:59"
    }
  ]
}
```

---

## 后台管理API

### 1. 活动管理

#### 1.1 创建红包活动

**接口地址**：`POST /admin/red-packet/activity/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**：
```json
{
  "activityName": "新用户注册红包",
  "activityCode": "NEW_USER_REGISTER",
  "packetType": "CASH",
  "amountType": "FIXED",
  "fixedAmount": "10.00000000",
  "minAmount": null,
  "maxAmount": null,
  "totalAmount": "100000.00000000",
  "totalCount": 10000,
  "distributionScope": "ALL",
  "receiveCondition": "REGISTER",
  "conditionValue": null,
  "validDays": 7,
  "useScope": "{\"allowedScenes\":[\"TRADE\",\"FEE_REDUCTION\"]}",
  "useTimeLimit": null,
  "status": "ACTIVE",
  "startTime": "2024-01-01T00:00:00",
  "endTime": "2024-12-31T23:59:59",
  "description": "新用户注册即可领取10 USDT红包",
  "autoIssue": true,
  "issueCycle": null
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityName | String | 是 | 活动名称 |
| activityCode | String | 是 | 活动代码（唯一） |
| packetType | String | 是 | 红包类型：CASH（现金）, COUPON（优惠券）, POINTS（积分）, DISCOUNT（折扣） |
| amountType | String | 是 | 金额类型：FIXED（固定金额）, RANDOM（随机金额） |
| fixedAmount | String | 否 | 固定金额（固定金额时必填） |
| minAmount | String | 否 | 最小金额（随机金额时必填） |
| maxAmount | String | 否 | 最大金额（随机金额时必填） |
| totalAmount | String | 是 | 红包总金额 |
| totalCount | Integer | 是 | 红包总数量 |
| distributionScope | String | 是 | 发放范围：SPECIFIC（指定用户）, ALL（所有用户）, VIP（VIP用户） |
| receiveCondition | String | 是 | 领取条件：TRADE（交易）, CHECKIN（签到）, INVITE（邀请）, NONE（无条件） |
| conditionValue | String | 否 | 条件值（如交易金额） |
| validDays | Integer | 是 | 有效期（天数） |
| useScope | String | 否 | 使用范围限制（JSON格式） |
| useTimeLimit | String | 否 | 使用时间限制（JSON格式） |
| status | String | 否 | 活动状态：ACTIVE（活跃）, INACTIVE（非活跃） |
| startTime | String | 是 | 活动开始时间（格式：yyyy-MM-ddTHH:mm:ss） |
| endTime | String | 是 | 活动结束时间 |
| description | String | 否 | 活动描述 |
| autoIssue | Boolean | 否 | 是否自动发放 |
| issueCycle | String | 否 | 发放周期：DAILY, WEEKLY, MONTHLY |

**响应示例**：
```json
{
  "code": 200,
  "message": "红包活动创建成功",
  "data": {
    "id": 1,
    "activityName": "新用户注册红包",
    "activityCode": "NEW_USER_REGISTER",
    "status": "ACTIVE"
  }
}
```

#### 1.2 更新红包活动

**接口地址**：`PUT /admin/red-packet/activity/update/{activityId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

**请求参数**：同创建活动（所有字段可选）

#### 1.3 删除红包活动

**接口地址**：`DELETE /admin/red-packet/activity/{activityId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

#### 1.4 获取所有红包活动

**接口地址**：`GET /admin/red-packet/activity/list`

**请求示例**：
```
GET /admin/red-packet/activity/list
```

#### 1.5 获取活跃的红包活动

**接口地址**：`GET /admin/red-packet/activity/active`

**请求示例**：
```
GET /admin/red-packet/activity/active
```

#### 1.6 获取活动详情

**接口地址**：`GET /admin/red-packet/activity/{activityId}`

---

### 2. 红包领取记录管理

#### 2.1 获取所有领取记录

**接口地址**：`GET /admin/red-packet/receive/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| activityId | Long | 否 | 活动ID筛选 |
| status | String | 否 | 状态筛选：UNUSED, USED, EXPIRED |

**请求示例**：
```
GET /admin/red-packet/receive/list?page=0&size=20&activityId=1
```

#### 2.2 获取领取记录详情

**接口地址**：`GET /admin/red-packet/receive/{receiveId}`

---

### 3. 红包使用记录管理

#### 3.1 获取所有使用记录

**接口地址**：`GET /admin/red-packet/use/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| packetId | Long | 否 | 红包ID筛选 |
| useScene | String | 否 | 使用场景筛选 |

**请求示例**：
```
GET /admin/red-packet/use/list?page=0&size=20
```

---

### 4. 统计管理

#### 4.1 获取活动统计

**接口地址**：`GET /admin/red-packet/statistics/activity/{activityId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | Long | 是 | 活动ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "activityId": 1,
    "activityName": "新用户注册红包",
    "totalCount": 10000,
    "receivedCount": 5000,
    "usedCount": 3000,
    "expiredCount": 100,
    "remainingCount": 1900,
    "totalAmount": "100000.00000000",
    "receivedAmount": "50000.00000000",
    "usedAmount": "30000.00000000",
    "remainingAmount": "19000.00000000"
  }
}
```

#### 4.2 获取平台统计

**接口地址**：`GET /admin/red-packet/statistics/platform`

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalActivities": 10,
    "activeActivities": 5,
    "totalReceivedCount": 50000,
    "totalUsedCount": 30000,
    "totalAmount": "1000000.00000000"
  }
}
```

---

## 红包类型说明

### CASH（现金红包）
- 直接发放到用户账户余额
- 可以用于交易、提现等

### COUPON（优惠券）
- 特定场景使用
- 如交易手续费减免券

### POINTS（积分）
- 积分奖励
- 可用于积分商城兑换

### DISCOUNT（折扣）
- 折扣优惠
- 如交易折扣券

## 领取条件说明

- **TRADE**：需要完成指定金额的交易
- **CHECKIN**：需要签到
- **INVITE**：需要邀请新用户
- **NONE**：无条件，可直接领取

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














