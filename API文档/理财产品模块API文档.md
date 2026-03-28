# 理财产品模块API文档

## 基础信息

- **模块名称**：理财产品模块（Finance Product）
- **用户端基础路径**：`/api/finance`
- **后台管理基础路径**：`/admin/finance`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 产品列表和详情

#### 1.1 获取可用理财产品列表

**接口地址**：`GET /api/finance/product/available`

**请求示例**：
```
GET /api/finance/product/available
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "productName": "USDT定期理财30天",
      "productCode": "USDT_FIXED_30",
      "productType": "FIXED",
      "riskLevel": "CONSERVATIVE",
      "annualRate": "0.08000000",
      "investmentCycle": 30,
      "minInvestmentAmount": "100.00000000",
      "maxInvestmentAmount": "100000.00000000",
      "totalRaiseAmount": "1000000.00000000",
      "supportedCurrency": "USDT",
      "lockPeriod": 30,
      "status": "AVAILABLE",
      "currentRaiseAmount": "500000.00000000",
      "description": "USDT定期理财产品，年化收益8%",
      "startTime": "2024-01-01T00:00:00",
      "endTime": "2024-12-31T23:59:59"
    }
  ]
}
```

#### 1.2 获取产品详情

**接口地址**：`GET /api/finance/product/{productId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |

**请求示例**：
```
GET /api/finance/product/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "productName": "USDT定期理财30天",
    "productCode": "USDT_FIXED_30",
    "productType": "FIXED",
    "productTypeDisplay": "定期",
    "riskLevel": "CONSERVATIVE",
    "riskLevelDisplay": "保守型",
    "annualRate": "0.08000000",
    "annualRatePercent": "8.00%",
    "investmentCycle": 30,
    "minInvestmentAmount": "100.00000000",
    "maxInvestmentAmount": "100000.00000000",
    "totalRaiseAmount": "1000000.00000000",
    "supportedCurrency": "USDT",
    "lockPeriod": 30,
    "status": "AVAILABLE",
    "statusDisplay": "可购买",
    "currentRaiseAmount": "500000.00000000",
    "description": "USDT定期理财产品，年化收益8%",
    "riskWarning": "投资有风险，入市需谨慎",
    "settlementMethod": "AUTO",
    "settlementCycle": "MATURITY"
  }
}
```

---

### 2. 投资管理

#### 2.1 投资理财产品

**接口地址**：`POST /api/finance/investment/invest`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "productId": 1,
  "investmentAmount": "1000.00000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |
| investmentAmount | String | 是 | 投资金额（支持8位小数） |

**请求示例**：
```json
POST /api/finance/investment/invest
Content-Type: application/json

{
  "productId": 1,
  "investmentAmount": "1000.00000000"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "投资成功",
  "data": {
    "id": 1,
    "investmentNo": "FI-20240101120000-123456",
    "userId": 1,
    "productId": 1,
    "productName": "USDT定期理财30天",
    "investmentAmount": "1000.00000000",
    "expectedProfit": "6.57534247",
    "startTime": "2024-01-01T12:00:00",
    "endTime": "2024-01-31T12:00:00",
    "status": "ACTIVE"
  }
}
```

#### 2.2 获取我的投资记录

**接口地址**：`GET /api/finance/investment/my-investments`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/finance/investment/my-investments
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "investmentNo": "FI-20240101120000-123456",
      "productName": "USDT定期理财30天",
      "investmentAmount": "1000.00000000",
      "expectedProfit": "6.57534247",
      "startTime": "2024-01-01T12:00:00",
      "endTime": "2024-01-31T12:00:00",
      "status": "ACTIVE"
    }
  ]
}
```

#### 2.3 分页获取我的投资记录

**接口地址**：`GET /api/finance/investment/my-investments/page`

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
GET /api/finance/investment/my-investments/page?page=0&size=20
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
        "investmentNo": "FI-20240101120000-123456",
        "productName": "USDT定期理财30天",
        "investmentAmount": "1000.00000000",
        "status": "ACTIVE"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

#### 2.4 获取投资详情

**接口地址**：`GET /api/finance/investment/{investmentId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| investmentId | Long | 是 | 投资ID |

**请求示例**：
```
GET /api/finance/investment/1
```

#### 2.5 计算预期收益

**接口地址**：`GET /api/finance/investment/expected-profit/{investmentId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| investmentId | Long | 是 | 投资ID |

**请求示例**：
```
GET /api/finance/investment/expected-profit/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": "6.57534247"
}
```

---

### 3. 赎回管理

#### 3.1 赎回理财产品

**接口地址**：`POST /api/finance/redemption/redeem`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "investmentId": 1,
  "redemptionAmount": "1000.00000000",
  "redemptionType": "FULL"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| investmentId | Long | 是 | 投资ID |
| redemptionAmount | String | 否 | 赎回金额（部分赎回时必填） |
| redemptionType | String | 是 | 赎回类型：FULL（全额）, PARTIAL（部分） |

**请求示例**：
```json
POST /api/finance/redemption/redeem
Content-Type: application/json

{
  "investmentId": 1,
  "redemptionAmount": "1000.00000000",
  "redemptionType": "FULL"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "赎回成功",
  "data": {
    "id": 1,
    "redemptionNo": "FR-20240131120000-123456",
    "investmentId": 1,
    "redemptionType": "FULL",
    "redemptionAmount": "1000.00000000",
    "profitAmount": "6.57534247",
    "totalAmount": "1006.57534247",
    "redemptionTime": "2024-01-31T12:00:00",
    "status": "SUCCESS"
  }
}
```

#### 3.2 获取我的赎回记录

**接口地址**：`GET /api/finance/redemption/my-redemptions`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/finance/redemption/my-redemptions
```

---

## 后台管理API

### 1. 产品管理

#### 1.1 创建理财产品

**接口地址**：`POST /admin/finance/product/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**：
```json
{
  "productName": "USDT定期理财30天",
  "productCode": "USDT_FIXED_30",
  "productType": "FIXED",
  "riskLevel": "CONSERVATIVE",
  "annualRate": "0.08000000",
  "investmentCycle": 30,
  "minInvestmentAmount": "100.00000000",
  "maxInvestmentAmount": "100000.00000000",
  "totalRaiseAmount": "1000000.00000000",
  "supportedCurrency": "USDT",
  "lockPeriod": 30,
  "status": "AVAILABLE",
  "startTime": "2024-01-01T00:00:00",
  "endTime": "2024-12-31T23:59:59",
  "description": "USDT定期理财产品，年化收益8%",
  "riskWarning": "投资有风险，入市需谨慎",
  "settlementMethod": "AUTO",
  "settlementCycle": "MATURITY",
  "sortOrder": 1
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productName | String | 是 | 产品名称 |
| productCode | String | 是 | 产品代码（唯一） |
| productType | String | 是 | 产品类型：FIXED（定期）, FLEXIBLE（灵活） |
| riskLevel | String | 是 | 风险等级：CONSERVATIVE（保守型）, HIGH_RISK（高风险） |
| annualRate | String | 是 | 年化收益率（小数形式，如0.08表示8%） |
| investmentCycle | Integer | 是 | 投资周期（天数） |
| minInvestmentAmount | String | 是 | 最小投资金额 |
| maxInvestmentAmount | String | 是 | 最大投资金额 |
| totalRaiseAmount | String | 是 | 总募集金额 |
| supportedCurrency | String | 是 | 支持的投资币种 |
| lockPeriod | Integer | 是 | 锁仓期（天数） |
| status | String | 否 | 产品状态：AVAILABLE（可购买）, SOLD_OUT（已售罄）, ENDED（已结束） |
| startTime | String | 是 | 产品开始时间（格式：yyyy-MM-ddTHH:mm:ss） |
| endTime | String | 是 | 产品结束时间 |
| description | String | 否 | 产品描述 |
| riskWarning | String | 否 | 风险提示 |
| settlementMethod | String | 否 | 结算方式：AUTO（自动）, MANUAL（手动） |
| settlementCycle | String | 否 | 收益结算周期：DAILY, WEEKLY, MONTHLY, MATURITY |
| sortOrder | Integer | 否 | 排序顺序 |

**响应示例**：
```json
{
  "code": 200,
  "message": "理财产品创建成功",
  "data": {
    "id": 1,
    "productName": "USDT定期理财30天",
    "productCode": "USDT_FIXED_30",
    "status": "AVAILABLE"
  }
}
```

#### 1.2 更新理财产品

**接口地址**：`POST /admin/finance/product/update/{productId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |

**请求参数**：同创建产品（所有字段可选）

#### 1.3 删除理财产品

**接口地址**：`DELETE /admin/finance/product/{productId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |

**请求示例**：
```
DELETE /admin/finance/product/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "理财产品删除成功",
  "data": null
}
```

#### 1.4 获取所有理财产品

**接口地址**：`GET /admin/finance/product/list`

**请求示例**：
```
GET /admin/finance/product/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "productName": "USDT定期理财30天",
      "productCode": "USDT_FIXED_30",
      "status": "AVAILABLE",
      "currentRaiseAmount": "500000.00000000",
      "totalRaiseAmount": "1000000.00000000"
    }
  ]
}
```

#### 1.5 获取可用理财产品（后台）

**接口地址**：`GET /admin/finance/product/available`

**请求示例**：
```
GET /admin/finance/product/available
```

#### 1.6 获取产品详情（后台）

**接口地址**：`GET /admin/finance/product/{productId}`

---

### 2. 投资记录管理

#### 2.1 获取所有投资记录

**接口地址**：`GET /admin/finance/investment/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| productId | Long | 否 | 产品ID筛选 |
| status | String | 否 | 状态筛选：ACTIVE, COMPLETED, CANCELLED |

**请求示例**：
```
GET /admin/finance/investment/list?page=0&size=20&status=ACTIVE
```

#### 2.2 获取用户投资记录

**接口地址**：`GET /admin/finance/investment/user/{userId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**请求示例**：
```
GET /admin/finance/investment/user/1
```

#### 2.3 获取产品投资记录

**接口地址**：`GET /admin/finance/investment/product/{productId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |

---

### 3. 收益结算管理

#### 3.1 手动结算收益

**接口地址**：`POST /admin/finance/settlement/settle`

**请求参数**：
```json
{
  "investmentId": 1,
  "settlementType": "MANUAL",
  "profitAmount": "6.57534247",
  "remark": "手动结算收益"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| investmentId | Long | 是 | 投资ID |
| settlementType | String | 是 | 结算类型：MANUAL（手动）, AUTO（自动） |
| profitAmount | String | 是 | 收益金额 |
| remark | String | 否 | 备注 |

**请求示例**：
```json
POST /admin/finance/settlement/settle
Content-Type: application/json

{
  "investmentId": 1,
  "settlementType": "MANUAL",
  "profitAmount": "6.57534247",
  "remark": "手动结算收益"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "结算成功",
  "data": {
    "id": 1,
    "settlementNo": "FS-20240131120000-123456",
    "investmentId": 1,
    "settlementType": "MANUAL",
    "profitAmount": "6.57534247",
    "settlementTime": "2024-01-31T12:00:00",
    "status": "SUCCESS"
  }
}
```

#### 3.2 批量结算收益

**接口地址**：`POST /admin/finance/settlement/batch-settle`

**请求参数**：
```json
{
  "productId": 1,
  "settlementType": "AUTO",
  "remark": "批量自动结算"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 产品ID |
| settlementType | String | 是 | 结算类型 |
| remark | String | 否 | 备注 |

#### 3.3 获取结算记录列表

**接口地址**：`GET /admin/finance/settlement/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| investmentId | Long | 否 | 投资ID筛选 |
| productId | Long | 否 | 产品ID筛选 |

**请求示例**：
```
GET /admin/finance/settlement/list?page=0&size=20
```

#### 3.4 获取结算记录详情

**接口地址**：`GET /admin/finance/settlement/{settlementId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| settlementId | Long | 是 | 结算ID |

---

### 4. 赎回记录管理

#### 4.1 获取所有赎回记录

**接口地址**：`GET /admin/finance/redemption/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| userId | Long | 否 | 用户ID筛选 |
| investmentId | Long | 否 | 投资ID筛选 |
| status | String | 否 | 状态筛选：PENDING, SUCCESS, FAILED |

**请求示例**：
```
GET /admin/finance/redemption/list?page=0&size=20
```

#### 4.2 获取赎回记录详情

**接口地址**：`GET /admin/finance/redemption/{redemptionId}`

---

## 产品类型说明

### 定期产品（FIXED）
- 固定投资周期
- 固定年化收益率
- 到期后自动结算
- 锁仓期内不可赎回

### 灵活产品（FLEXIBLE）
- 无固定投资周期
- 可随时赎回
- 收益按天计算
- 收益率可能浮动

## 风险等级说明

- **CONSERVATIVE（保守型）**：低风险，收益率相对较低
- **HIGH_RISK（高风险）**：高风险高收益，如BTC、ETH等数字资产产品

## 收益计算说明

### 定期产品收益计算
```
收益 = 投资金额 × 年化收益率 × (投资周期 / 365)
```

### 灵活产品收益计算
```
每日收益 = 投资金额 × 年化收益率 / 365
总收益 = 每日收益 × 持有天数
```

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














