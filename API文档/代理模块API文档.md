# 代理模块API文档

## 基础信息

- **模块名称**：代理模块（Agent）
- **用户端基础路径**：`/api/agent`
- **后台管理基础路径**：`/admin/agent`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 代理信息

#### 1.1 获取我的代理信息

**接口地址**：`GET /api/agent/my-info`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /api/agent/my-info
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "agentId": 1,
    "agentCode": "AGENT001",
    "agentName": "代理A",
    "agentLevel": "LEVEL1",
    "status": "ACTIVE",
    "totalSubAgents": 100,
    "totalUsers": 1000,
    "totalCommission": "10000.00000000",
    "pendingCommission": "5000.00000000",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 申请成为代理

**接口地址**：`POST /api/agent/apply`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "agentName": "代理A",
  "contactInfo": "contact@example.com",
  "remark": "申请成为代理"
}
```

---

### 2. 下级管理

#### 2.1 获取下级列表

**接口地址**：`GET /api/agent/sub-agents`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| level | Integer | 否 | 层级筛选（1-3级） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/agent/sub-agents?level=1&page=0&size=20
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
        "agentLevel": "LEVEL1",
        "registerTime": "2024-01-01T12:00:00",
        "totalUsers": 50,
        "totalCommission": "5000.00000000"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

#### 2.2 获取用户列表

**接口地址**：`GET /api/agent/users`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

### 3. 佣金管理

#### 3.1 获取佣金记录

**接口地址**：`GET /api/agent/commissions`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| commissionType | String | 否 | 佣金类型：TRADING（交易）, REGISTER（注册） |
| status | String | 否 | 状态：PENDING（待结算）, SETTLED（已结算） |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/agent/commissions?status=SETTLED&page=0&size=20
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
        "commissionType": "TRADING",
        "amount": "100.00000000",
        "currency": "USDT",
        "sourceUserId": 100,
        "sourceOrderId": 123456,
        "commissionRate": "0.00100000",
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

#### 3.2 获取佣金统计

**接口地址**：`GET /api/agent/commission/statistics`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 统计周期：DAY（日）, WEEK（周）, MONTH（月）, ALL（全部） |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalCommission": "10000.00000000",
    "pendingCommission": "5000.00000000",
    "settledCommission": "5000.00000000",
    "todayCommission": "100.00000000",
    "monthlyCommission": "3000.00000000"
  }
}
```

---

## 后台管理API

### 1. 代理管理

#### 1.1 获取所有代理

**接口地址**：`GET /admin/agent/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| agentLevel | String | 否 | 代理级别筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 1.2 审核代理申请

**接口地址**：`POST /admin/agent/approve/{agentId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| approved | Boolean | 是 | 是否通过 |
| remark | String | 否 | 审核备注 |

#### 1.3 设置代理级别

**接口地址**：`POST /admin/agent/set-level/{agentId}`

**请求参数**：
```json
{
  "agentLevel": "LEVEL2",
  "commissionRate": "0.00150000"
}
```

---

### 2. 佣金管理

#### 2.1 手动结算佣金

**接口地址**：`POST /admin/agent/commission/settle`

**请求参数**：
```json
{
  "agentId": 1,
  "amount": "1000.00000000",
  "remark": "手动结算佣金"
}
```

---

## 代理级别说明

- **LEVEL1**：一级代理
- **LEVEL2**：二级代理
- **LEVEL3**：三级代理

不同级别的代理享受不同的佣金比例。

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














