# 质押借币模块API文档

## 基础信息

- **模块名称**：质押借币模块（Pledge Loan）
- **用户端基础路径**：`/api/pledge-loan`
- **后台管理基础路径**：`/api/admin/pledge-loan`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 订单管理

#### 1.1 创建质押借币订单

**接口地址**：`POST /api/pledge-loan/order/create`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "pledgeCurrency": "BTC",
  "pledgeAmount": "1.00000000",
  "loanCurrency": "USDT",
  "loanAmount": "40000.00000000",
  "loanTermDays": 30
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pledgeCurrency | String | 是 | 质押币种代码（如：BTC, ETH） |
| pledgeAmount | String | 是 | 质押数量（支持8位小数） |
| loanCurrency | String | 是 | 借款币种代码（如：USDT） |
| loanAmount | String | 是 | 借款数量（支持8位小数） |
| loanTermDays | Integer | 是 | 借款期限（天） |

**响应示例**：
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "id": 1,
    "orderNo": "PL-20240101120000-123456",
    "userId": 1,
    "pledgeCurrency": "BTC",
    "pledgeAmount": "1.00000000",
    "loanCurrency": "USDT",
    "loanAmount": "40000.00000000",
    "interestRate": "0.00010000",
    "loanTermDays": 30,
    "status": "PENDING",
    "approvalStatus": "PENDING",
    "healthRate": "2.00000000",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

**错误示例**：
```json
{
  "code": 500,
  "message": "质押币种配置不存在",
  "data": null
}
```

#### 1.2 计算可借额度

**接口地址**：`GET /api/pledge-loan/order/calculate-loan-amount`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pledgeCurrency | String | 是 | 质押币种代码 |
| pledgeAmount | String | 是 | 质押数量 |
| loanCurrency | String | 是 | 借款币种代码 |

**请求示例**：
```
GET /api/pledge-loan/order/calculate-loan-amount?pledgeCurrency=BTC&pledgeAmount=1.00000000&loanCurrency=USDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": "40000.00000000"
}
```

#### 1.3 获取我的订单列表

**接口地址**：`GET /api/pledge-loan/order/my-orders`

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
GET /api/pledge-loan/order/my-orders?page=0&size=20
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
        "orderNo": "PL-20240101120000-123456",
        "pledgeCurrency": "BTC",
        "pledgeAmount": "1.00000000",
        "loanCurrency": "USDT",
        "loanAmount": "40000.00000000",
        "status": "ACTIVE",
        "healthRate": "1.50000000",
        "remainingPrincipal": "40000.00000000",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

#### 1.4 获取订单详情

**接口地址**：`GET /api/pledge-loan/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /api/pledge-loan/order/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "orderNo": "PL-20240101120000-123456",
    "userId": 1,
    "pledgeCurrency": "BTC",
    "pledgeAmount": "1.00000000",
    "pledgeValue": "50000.00000000",
    "loanCurrency": "USDT",
    "loanAmount": "40000.00000000",
    "loanValue": "40000.00000000",
    "interestRate": "0.00010000",
    "loanTermDays": 30,
    "pledgeRatio": "0.80000000",
    "status": "ACTIVE",
    "approvalStatus": "APPROVED",
    "remainingPrincipal": "40000.00000000",
    "totalInterest": "120.00000000",
    "paidInterest": "0.00000000",
    "liquidationPrice": "40000.00000000",
    "healthRate": "1.50000000",
    "createdAt": "2024-01-01T12:00:00",
    "approvedAt": "2024-01-01T12:05:00"
  }
}
```

#### 1.5 计算订单健康度

**接口地址**：`GET /api/pledge-loan/order/health-rate/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /api/pledge-loan/order/health-rate/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": "1.50000000"
}
```

**健康度说明**：
- `健康度 = (质押资产当前价值) / (借款本金 + 未付利息)`
- 健康度 > 1.5：安全
- 1.2 < 健康度 ≤ 1.5：中等风险
- 1.0 < 健康度 ≤ 1.2：高风险
- 健康度 ≤ 1.0：严重风险，触发平仓

---

### 2. 还款管理

#### 2.1 全额还款

**接口地址**：`POST /api/pledge-loan/repayment/repay-full`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
POST /api/pledge-loan/repayment/repay-full?orderId=1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "还款成功",
  "data": {
    "id": 1,
    "repaymentNo": "RP-20240101130000-123456",
    "orderId": 1,
    "repaymentType": "FULL",
    "principalAmount": "40000.00000000",
    "interestAmount": "120.00000000",
    "totalAmount": "40120.00000000",
    "repaymentTime": "2024-01-01T13:00:00"
  }
}
```

#### 2.2 部分还款

**接口地址**：`POST /api/pledge-loan/repayment/repay-partial`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |
| principalAmount | String | 是 | 还款本金数量 |

**请求示例**：
```
POST /api/pledge-loan/repayment/repay-partial?orderId=1&principalAmount=10000.00000000
```

**响应示例**：
```json
{
  "code": 200,
  "message": "还款成功",
  "data": {
    "id": 2,
    "repaymentNo": "RP-20240101140000-123457",
    "orderId": 1,
    "repaymentType": "PARTIAL",
    "principalAmount": "10000.00000000",
    "interestAmount": "0.00000000",
    "totalAmount": "10000.00000000",
    "repaymentTime": "2024-01-01T14:00:00"
  }
}
```

#### 2.3 还款利息

**接口地址**：`POST /api/pledge-loan/repayment/repay-interest`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |
| interestAmount | String | 是 | 还款利息数量 |

**请求示例**：
```
POST /api/pledge-loan/repayment/repay-interest?orderId=1&interestAmount=120.00000000
```

#### 2.4 计算应还利息

**接口地址**：`GET /api/pledge-loan/repayment/calculate-interest/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

**请求示例**：
```
GET /api/pledge-loan/repayment/calculate-interest/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": "120.00000000"
}
```

#### 2.5 获取我的还款记录

**接口地址**：`GET /api/pledge-loan/repayment/my-repayments`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/pledge-loan/repayment/my-repayments
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "repaymentNo": "RP-20240101130000-123456",
      "orderId": 1,
      "repaymentType": "FULL",
      "principalAmount": "40000.00000000",
      "interestAmount": "120.00000000",
      "totalAmount": "40120.00000000",
      "repaymentTime": "2024-01-01T13:00:00"
    }
  ]
}
```

---

### 3. 补仓管理

#### 3.1 补仓

**接口地址**：`POST /api/pledge-loan/topup/topup`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "orderId": 1,
  "topupAmount": "0.50000000"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |
| topupAmount | String | 是 | 补仓数量（质押币种） |

**请求示例**：
```json
POST /api/pledge-loan/topup/topup
Content-Type: application/json

{
  "orderId": 1,
  "topupAmount": "0.50000000"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "补仓成功",
  "data": {
    "id": 1,
    "topupNo": "TP-20240101150000-123456",
    "orderId": 1,
    "topupAmount": "0.50000000",
    "healthRateBefore": "1.20000000",
    "healthRateAfter": "1.50000000",
    "topupTime": "2024-01-01T15:00:00"
  }
}
```

#### 3.2 获取我的补仓记录

**接口地址**：`GET /api/pledge-loan/topup/my-topups`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/pledge-loan/topup/my-topups
```

---

### 4. 质押币种配置

#### 4.1 获取质押币种配置列表

**接口地址**：`GET /api/pledge-loan/pledge-currency/list`

**请求示例**：
```
GET /api/pledge-loan/pledge-currency/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "currencyCode": "BTC",
      "currencyName": "比特币",
      "loanRatio": "0.80000000",
      "interestRate": "0.00010000",
      "minPledgeAmount": "0.01000000",
      "maxLoanAmount": "1000000.00000000",
      "riskRate": "1.20000000",
      "maintenanceRate": "1.30000000",
      "status": "ACTIVE"
    }
  ]
}
```

#### 4.2 获取质押币种配置详情

**接口地址**：`GET /api/pledge-loan/pledge-currency/{currencyCode}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyCode | String | 是 | 币种代码 |

**请求示例**：
```
GET /api/pledge-loan/pledge-currency/BTC
```

---

### 5. 借款币种配置

#### 5.1 获取借款币种配置列表

**接口地址**：`GET /api/pledge-loan/loan-currency/list`

**请求示例**：
```
GET /api/pledge-loan/loan-currency/list
```

#### 5.2 获取借款币种配置详情

**接口地址**：`GET /api/pledge-loan/loan-currency/{currencyCode}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyCode | String | 是 | 币种代码 |

---

### 6. 统计

#### 6.1 获取我的质押统计

**接口地址**：`GET /api/pledge-loan/statistics/my-statistics`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/pledge-loan/statistics/my-statistics
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalPledgeValue": "100000.00000000",
    "totalLoanAmount": "80000.00000000",
    "totalInterestPaid": "500.00000000",
    "activeOrdersCount": 2,
    "completedOrdersCount": 5
  }
}
```

---

## 后台管理API

### 1. 订单管理

#### 1.1 获取订单列表

**接口地址**：`GET /api/admin/pledge-loan/order/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| status | String | 否 | 状态筛选：PENDING, APPROVED, REJECTED, ACTIVE, LIQUIDATED, COMPLETED |
| userId | Long | 否 | 用户ID |
| pledgeCurrency | String | 否 | 质押币种 |
| loanCurrency | String | 否 | 借款币种 |

**请求示例**：
```
GET /api/admin/pledge-loan/order/list?page=0&size=20&status=ACTIVE
```

#### 1.2 获取订单详情

**接口地址**：`GET /api/admin/pledge-loan/order/{orderId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

#### 1.3 审批通过

**接口地址**：`POST /api/admin/pledge-loan/order/approve/{orderId}`

**请求头**：
```
adminId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| approvalStatus | String | 否 | 审批状态：AUTO, MANUAL（默认MANUAL） |
| remark | String | 否 | 备注 |

**请求示例**：
```
POST /api/admin/pledge-loan/order/approve/1?approvalStatus=MANUAL&remark=审批通过
```

#### 1.4 审批拒绝

**接口地址**：`POST /api/admin/pledge-loan/order/reject/{orderId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| remark | String | 是 | 拒绝原因 |

**请求示例**：
```
POST /api/admin/pledge-loan/order/reject/1?remark=不符合借款条件
```

#### 1.5 检查风险订单

**接口地址**：`POST /api/admin/pledge-loan/order/check-risk`

**请求示例**：
```
POST /api/admin/pledge-loan/order/check-risk
```

**响应示例**：
```json
{
  "code": 200,
  "message": "风险检查完成",
  "data": null
}
```

---

### 2. 质押币种配置管理

#### 2.1 获取配置列表

**接口地址**：`GET /api/admin/pledge-loan/pledge-currency/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：ACTIVE, INACTIVE |

#### 2.2 创建配置

**接口地址**：`POST /api/admin/pledge-loan/pledge-currency/create`

**请求参数**：
```json
{
  "currencyCode": "ETH",
  "currencyName": "以太坊",
  "loanRatio": "0.75000000",
  "interestRate": "0.00015000",
  "minPledgeAmount": "0.10000000",
  "maxLoanAmount": "500000.00000000",
  "riskRate": "1.20000000",
  "maintenanceRate": "1.30000000",
  "sortOrder": 2,
  "description": "以太坊质押配置"
}
```

#### 2.3 更新配置

**接口地址**：`PUT /api/admin/pledge-loan/pledge-currency/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 配置ID |

**请求参数**：同创建配置（所有字段可选）

#### 2.4 更新状态

**接口地址**：`PUT /api/admin/pledge-loan/pledge-currency/status/{id}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 状态：ACTIVE, INACTIVE |

#### 2.5 删除配置

**接口地址**：`DELETE /api/admin/pledge-loan/pledge-currency/{id}`

---

### 3. 借款币种配置管理

#### 3.1 获取配置列表

**接口地址**：`GET /api/admin/pledge-loan/loan-currency/list`

#### 3.2 创建配置

**接口地址**：`POST /api/admin/pledge-loan/loan-currency/create`

**请求参数**：
```json
{
  "currencyCode": "USDT",
  "currencyName": "泰达币",
  "interestRate": "0.00010000",
  "minLoanAmount": "100.00000000",
  "maxLoanAmount": "1000000.00000000",
  "loanTermOptions": "7,14,30,90",
  "sortOrder": 1,
  "description": "USDT借款配置"
}
```

#### 3.3 更新配置

**接口地址**：`PUT /api/admin/pledge-loan/loan-currency/update/{id}`

#### 3.4 更新状态

**接口地址**：`PUT /api/admin/pledge-loan/loan-currency/status/{id}`

#### 3.5 删除配置

**接口地址**：`DELETE /api/admin/pledge-loan/loan-currency/{id}`

---

### 4. 风险监控

#### 4.1 获取未处理的风险记录

**接口地址**：`GET /api/admin/pledge-loan/risk/unprocessed`

**请求示例**：
```
GET /api/admin/pledge-loan/risk/unprocessed
```

#### 4.2 获取订单的风险记录

**接口地址**：`GET /api/admin/pledge-loan/risk/order/{orderId}`

#### 4.3 通知用户风险

**接口地址**：`POST /api/admin/pledge-loan/risk/notify/{riskRecordId}`

#### 4.4 标记为已处理

**接口地址**：`POST /api/admin/pledge-loan/risk/mark-processed/{riskRecordId}`

---

### 5. 统计管理

#### 5.1 获取平台统计

**接口地址**：`GET /api/admin/pledge-loan/statistics/platform`

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalPledgeValue": "10000000.00000000",
    "totalLoanAmount": "8000000.00000000",
    "totalInterestIncome": "50000.00000000",
    "activeOrdersCount": 100,
    "completedOrdersCount": 500,
    "liquidationCount": 10
  }
}
```

#### 5.2 获取风险统计

**接口地址**：`GET /api/admin/pledge-loan/statistics/risk`

#### 5.3 获取利息收入统计

**接口地址**：`GET /api/admin/pledge-loan/statistics/interest-income`

#### 5.4 获取平仓统计

**接口地址**：`GET /api/admin/pledge-loan/statistics/liquidation`

---

### 6. 平仓管理

#### 6.1 获取订单的平仓记录

**接口地址**：`GET /api/admin/pledge-loan/liquidation/order/{orderId}`

#### 6.2 手动平仓

**接口地址**：`POST /api/admin/pledge-loan/liquidation/manual/{orderId}`

**请求头**：
```
adminId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reason | String | 是 | 平仓原因 |

**请求示例**：
```
POST /api/admin/pledge-loan/liquidation/manual/1?reason=手动平仓，用户违规操作
```

---

### 7. 利率调整历史

#### 7.1 获取币种的利率调整历史

**接口地址**：`GET /api/admin/pledge-loan/rate-history/currency/{currencyCode}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyCode | String | 是 | 币种代码 |

#### 7.2 获取配置类型的利率调整历史

**接口地址**：`GET /api/admin/pledge-loan/rate-history/config-type/{configType}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| configType | String | 是 | 配置类型：PLEDGE, LOAN |

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














