# 矿池模块API文档

## 基础信息

- **模块名称**：矿池模块（Mining Pool）
- **用户端基础路径**：`/api/mining`
- **后台管理基础路径**：`/admin/mining`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 矿池列表和详情

#### 1.1 获取矿池列表

**接口地址**：`GET /api/mining/pool/list`

**请求示例**：
```
GET /api/mining/pool/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "poolName": "BTC SHA-256矿池",
      "poolCode": "BTC_SHA256",
      "miningCurrency": "BTC",
      "algorithm": "SHA-256",
      "distributionMethod": "PPS",
      "hashratePrice": "0.00100000",
      "minHashrate": "1.00000000",
      "status": "ACTIVE",
      "riskLevel": "MEDIUM",
      "settlementCycle": "DAILY",
      "totalHashrate": "1000000.00000000",
      "participantCount": 100,
      "description": "BTC SHA-256挖矿矿池"
    }
  ]
}
```

#### 1.2 获取活跃矿池列表

**接口地址**：`GET /api/mining/pool/active`

**请求示例**：
```
GET /api/mining/pool/active
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "poolName": "BTC SHA-256矿池",
      "status": "ACTIVE"
    }
  ]
}
```

#### 1.3 获取矿池详情

**接口地址**：`GET /api/mining/pool/{poolId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolId | Long | 是 | 矿池ID |

**请求示例**：
```
GET /api/mining/pool/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "poolName": "BTC SHA-256矿池",
    "poolCode": "BTC_SHA256",
    "miningCurrency": "BTC",
    "algorithm": "SHA-256",
    "distributionMethod": "PPS",
    "distributionMethodDisplay": "按份额支付",
    "hashratePrice": "0.00100000",
    "minHashrate": "1.00000000",
    "maxParticipants": 1000,
    "status": "ACTIVE",
    "statusDisplay": "活跃",
    "riskLevel": "MEDIUM",
    "riskLevelDisplay": "中等风险",
    "settlementCycle": "DAILY",
    "settlementCycleDisplay": "每日结算",
    "totalHashrate": "1000000.00000000",
    "participantCount": 100,
    "totalRevenue": "10.50000000",
    "description": "BTC SHA-256挖矿矿池",
    "riskWarning": "挖矿有风险，投资需谨慎"
  }
}
```

---

### 2. 算力租赁

#### 2.1 租赁算力

**接口地址**：`POST /api/mining/hashrate/rental`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "poolId": 1,
  "hashrateTypeId": 1,
  "hashrateAmount": "100.00000000",
  "rentalDays": 30
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolId | Long | 是 | 矿池ID |
| hashrateTypeId | Long | 是 | 算力类型ID |
| hashrateAmount | String | 是 | 租赁算力数量（TH/s） |
| rentalDays | Integer | 是 | 租赁天数 |

**请求示例**：
```json
POST /api/mining/hashrate/rental
Content-Type: application/json

{
  "poolId": 1,
  "hashrateTypeId": 1,
  "hashrateAmount": "100.00000000",
  "rentalDays": 30
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "算力租赁成功",
  "data": {
    "id": 1,
    "rentalNo": "HR-20240101120000-123456",
    "userId": 1,
    "poolId": 1,
    "poolName": "BTC SHA-256矿池",
    "hashrateTypeId": 1,
    "hashrateAmount": "100.00000000",
    "rentalDays": 30,
    "totalCost": "3.00000000",
    "startTime": "2024-01-01T12:00:00",
    "endTime": "2024-01-31T12:00:00",
    "status": "ACTIVE"
  }
}
```

#### 2.2 获取我的算力租赁记录

**接口地址**：`GET /api/mining/hashrate/my-rentals`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/mining/hashrate/my-rentals
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "rentalNo": "HR-20240101120000-123456",
      "poolName": "BTC SHA-256矿池",
      "hashrateAmount": "100.00000000",
      "rentalDays": 30,
      "status": "ACTIVE",
      "startTime": "2024-01-01T12:00:00",
      "endTime": "2024-01-31T12:00:00"
    }
  ]
}
```

#### 2.3 获取算力租赁详情

**接口地址**：`GET /api/mining/hashrate/rental/{rentalId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| rentalId | Long | 是 | 租赁ID |

---

### 3. 结算记录

#### 3.1 获取我的结算记录

**接口地址**：`GET /api/mining/settlement/my-settlements`

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
GET /api/mining/settlement/my-settlements?page=0&size=20
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
        "settlementNo": "MS-20240101120000-123456",
        "userId": 1,
        "poolId": 1,
        "poolName": "BTC SHA-256矿池",
        "rentalId": 1,
        "settlementDate": "2024-01-01",
        "hashrateContribution": "100.00000000",
        "revenue": "0.00100000",
        "settlementAmount": "0.00100000",
        "status": "SUCCESS",
        "settlementTime": "2024-01-02T00:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

---

## 后台管理API

### 1. 矿池管理

#### 1.1 创建矿池

**接口地址**：`POST /admin/mining/pool/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**：
```json
{
  "poolName": "ETH Ethash矿池",
  "poolCode": "ETH_ETHASH",
  "miningCurrency": "ETH",
  "algorithm": "Ethash",
  "distributionMethod": "PPS",
  "hashratePrice": "0.00050000",
  "minHashrate": "10.00000000",
  "maxParticipants": 500,
  "status": "ACTIVE",
  "riskLevel": "MEDIUM",
  "settlementCycle": "DAILY",
  "description": "ETH Ethash挖矿矿池",
  "riskWarning": "挖矿有风险，投资需谨慎",
  "sortOrder": 1
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolName | String | 是 | 矿池名称 |
| poolCode | String | 是 | 矿池代码（唯一） |
| miningCurrency | String | 是 | 挖矿币种（BTC, ETH等） |
| algorithm | String | 是 | 挖矿算法（SHA-256, Ethash等） |
| distributionMethod | String | 是 | 收益分配方式：PPS（按份额支付）, PPLNS（按最后N个份额）, PROP（按比例） |
| hashratePrice | String | 是 | 算力价格（每TH/s每天的价格） |
| minHashrate | String | 是 | 最低算力门槛 |
| maxParticipants | Integer | 否 | 最大参与用户数 |
| status | String | 否 | 矿池状态：ACTIVE（活跃）, INACTIVE（非活跃） |
| riskLevel | String | 否 | 风险等级：LOW（低风险）, MEDIUM（中等风险）, HIGH（高风险） |
| settlementCycle | String | 否 | 收益结算周期：DAILY（每日）, WEEKLY（每周）, MONTHLY（每月） |
| description | String | 否 | 矿池描述 |
| riskWarning | String | 否 | 风险提示 |
| sortOrder | Integer | 否 | 排序顺序 |

**响应示例**：
```json
{
  "code": 200,
  "message": "矿池创建成功",
  "data": {
    "id": 1,
    "poolName": "ETH Ethash矿池",
    "poolCode": "ETH_ETHASH",
    "status": "ACTIVE"
  }
}
```

#### 1.2 更新矿池

**接口地址**：`PUT /admin/mining/pool/update/{poolId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolId | Long | 是 | 矿池ID |

**请求参数**：同创建矿池（所有字段可选）

#### 1.3 删除矿池

**接口地址**：`DELETE /admin/mining/pool/{poolId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolId | Long | 是 | 矿池ID |

#### 1.4 获取所有矿池

**接口地址**：`GET /admin/mining/pool/list`

**请求示例**：
```
GET /admin/mining/pool/list
```

#### 1.5 获取活跃矿池（后台）

**接口地址**：`GET /admin/mining/pool/active`

#### 1.6 获取矿池详情（后台）

**接口地址**：`GET /admin/mining/pool/{poolId}`

---

### 2. 算力租赁管理

#### 2.1 获取所有算力租赁记录

**接口地址**：`GET /admin/mining/hashrate/rental/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| poolId | Long | 否 | 矿池ID筛选 |
| status | String | 否 | 状态筛选：ACTIVE, EXPIRED, CANCELLED |

**请求示例**：
```
GET /admin/mining/hashrate/rental/list?page=0&size=20&status=ACTIVE
```

#### 2.2 获取算力租赁详情

**接口地址**：`GET /admin/mining/hashrate/rental/{rentalId}`

---

### 3. 结算管理

#### 3.1 手动结算

**接口地址**：`POST /admin/mining/settlement/settle`

**请求参数**：
```json
{
  "poolId": 1,
  "settlementDate": "2024-01-01",
  "settlementType": "MANUAL"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| poolId | Long | 是 | 矿池ID |
| settlementDate | String | 是 | 结算日期（格式：yyyy-MM-dd） |
| settlementType | String | 是 | 结算类型：MANUAL（手动）, AUTO（自动） |

**请求示例**：
```json
POST /admin/mining/settlement/settle
Content-Type: application/json

{
  "poolId": 1,
  "settlementDate": "2024-01-01",
  "settlementType": "MANUAL"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "结算成功",
  "data": {
    "settlementCount": 100,
    "totalAmount": "0.10000000"
  }
}
```

#### 3.2 获取结算记录列表

**接口地址**：`GET /admin/mining/settlement/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| poolId | Long | 否 | 矿池ID筛选 |
| userId | Long | 否 | 用户ID筛选 |
| settlementDate | String | 否 | 结算日期筛选（格式：yyyy-MM-dd） |

**请求示例**：
```
GET /admin/mining/settlement/list?page=0&size=20&poolId=1
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
        "settlementNo": "MS-20240101120000-123456",
        "userId": 1,
        "poolId": 1,
        "poolName": "BTC SHA-256矿池",
        "rentalId": 1,
        "settlementDate": "2024-01-01",
        "hashrateContribution": "100.00000000",
        "revenue": "0.00100000",
        "settlementAmount": "0.00100000",
        "status": "SUCCESS",
        "settlementTime": "2024-01-02T00:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

#### 3.3 获取结算记录详情

**接口地址**：`GET /admin/mining/settlement/{settlementId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| settlementId | Long | 是 | 结算ID |

---

## 算力分配方式说明

### PPS（Pay Per Share，按份额支付）
- 矿工提交的每个有效份额都能立即获得固定报酬
- 收益稳定，不受矿池运气影响
- 适合追求稳定收益的矿工

### PPLNS（Pay Per Last N Shares，按最后N个份额）
- 基于矿池最近N个份额的收益分配
- 收益可能波动，但长期收益较高
- 适合长期挖矿的矿工

### PROP（Proportional，按比例）
- 按矿工贡献的算力比例分配收益
- 收益与矿池收益成正比
- 适合短期挖矿的矿工

## 风险等级说明

- **LOW（低风险）**：稳定币种，收益稳定
- **MEDIUM（中等风险）**：主流币种，收益波动较小
- **HIGH（高风险）**：新兴币种，收益波动较大

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














