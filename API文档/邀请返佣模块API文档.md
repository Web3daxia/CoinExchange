# 邀请返佣模块API文档

## 基础信息

- **模块名称**：邀请返佣模块（Invite Rebate）
- **用户端基础路径**：`/api/invite`
- **后台管理基础路径**：`/admin/invite`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 邀请管理

#### 1.1 获取我的邀请码

**接口地址**：`GET /api/invite/my-code`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /api/invite/my-code
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "userId": 1,
    "inviteCode": "INVITE123456",
    "inviteLink": "https://example.com/register?invite=INVITE123456",
    "qrCode": "data:image/png;base64,...",
    "totalInvitees": 100,
    "totalRebate": "5000.00000000",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 获取邀请统计

**接口地址**：`GET /api/invite/statistics`

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
GET /api/invite/statistics?period=MONTH
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalInvitees": 100,
    "activeInvitees": 80,
    "totalRebate": "5000.00000000",
    "pendingRebate": "1000.00000000",
    "settledRebate": "4000.00000000",
    "todayInvitees": 5,
    "monthlyInvitees": 30,
    "monthlyRebate": "1500.00000000"
  }
}
```

---

### 2. 被邀请人管理

#### 2.1 获取我的被邀请人列表

**接口地址**：`GET /api/invite/invitees`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：ACTIVE（活跃）, INACTIVE（非活跃） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/invite/invitees?page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "content": [
      {
        "userId": 100,
        "userName": "用户100",
        "registerTime": "2024-01-01T12:00:00",
        "status": "ACTIVE",
        "totalRebate": "50.00000000",
        "lastRebateTime": "2024-01-01T13:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

---

### 3. 返佣记录

#### 3.1 获取返佣记录

**接口地址**：`GET /api/invite/rebates`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| rebateType | String | 否 | 返佣类型：REGISTER（注册）, TRADING（交易）, DEPOSIT（充值） |
| status | String | 否 | 状态：PENDING（待结算）, SETTLED（已结算） |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/invite/rebates?status=SETTLED&page=0&size=20
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
        "rebateType": "TRADING",
        "amount": "50.00000000",
        "currency": "USDT",
        "inviteeId": 100,
        "inviteeName": "用户100",
        "sourceOrderId": 123456,
        "rebateRate": "0.00100000",
        "status": "SETTLED",
        "settleTime": "2024-01-01T13:00:00",
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

---

## 后台管理API

### 1. 返佣规则管理

#### 1.1 获取返佣规则列表

**接口地址**：`GET /admin/invite/rules`

**请求示例**：
```
GET /admin/invite/rules
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "rebateType": "REGISTER",
      "rebateAmount": "10.00000000",
      "rebateRate": null,
      "status": "ACTIVE",
      "createTime": "2024-01-01T12:00:00"
    },
    {
      "id": 2,
      "rebateType": "TRADING",
      "rebateAmount": null,
      "rebateRate": "0.00100000",
      "status": "ACTIVE",
      "createTime": "2024-01-01T12:00:00"
    }
  ]
}
```

#### 1.2 创建返佣规则

**接口地址**：`POST /admin/invite/rule/create`

**请求参数**：
```json
{
  "rebateType": "TRADING",
  "rebateAmount": null,
  "rebateRate": "0.00100000",
  "status": "ACTIVE"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| rebateType | String | 是 | 返佣类型：REGISTER（注册）, TRADING（交易）, DEPOSIT（充值） |
| rebateAmount | String | 条件 | 固定返佣金额（固定返佣时必填） |
| rebateRate | String | 条件 | 返佣比例（比例返佣时必填） |
| status | String | 否 | 状态：ACTIVE, INACTIVE |

#### 1.3 更新返佣规则

**接口地址**：`PUT /admin/invite/rule/update/{ruleId}`

#### 1.4 删除返佣规则

**接口地址**：`DELETE /admin/invite/rule/{ruleId}`

---

### 2. 返佣记录管理

#### 2.1 获取所有返佣记录

**接口地址**：`GET /admin/invite/rebates`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| inviteeId | Long | 否 | 被邀请人ID筛选 |
| rebateType | String | 否 | 返佣类型筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 2.2 手动结算返佣

**接口地址**：`POST /admin/invite/rebate/settle`

**请求参数**：
```json
{
  "rebateId": 1,
  "amount": "50.00000000",
  "remark": "手动结算返佣"
}
```

---

## 返佣类型说明

- **REGISTER（注册返佣）**：被邀请人注册成功后，邀请人获得返佣
- **TRADING（交易返佣）**：被邀请人交易时，邀请人获得交易手续费返佣
- **DEPOSIT（充值返佣）**：被邀请人充值时，邀请人获得返佣

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














