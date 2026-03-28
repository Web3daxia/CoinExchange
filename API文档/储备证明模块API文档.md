# 储备证明模块API文档

## 基础信息

- **模块名称**：储备证明模块（Proof of Reserves）
- **用户端基础路径**：`/api/proof-of-reserves`
- **后台管理基础路径**：`/admin/proof-of-reserves`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 储备证明查询

#### 1.1 获取储备证明概览

**接口地址**：`GET /api/proof-of-reserves/overview`

**请求示例**：
```
GET /api/proof-of-reserves/overview
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalReserves": "100000000.00000000",
    "totalUserBalance": "95000000.00000000",
    "reserveRatio": "105.26",
    "lastUpdateTime": "2024-01-01T12:00:00",
    "currencyBreakdown": [
      {
        "currency": "BTC",
        "reserves": "1000.00000000",
        "userBalance": "950.00000000",
        "reserveRatio": "105.26"
      },
      {
        "currency": "USDT",
        "reserves": "50000000.00000000",
        "userBalance": "47500000.00000000",
        "reserveRatio": "105.26"
      }
    ]
  }
}
```

#### 1.2 获取储备证明详情

**接口地址**：`GET /api/proof-of-reserves/detail`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |

**请求示例**：
```
GET /api/proof-of-reserves/detail?currency=BTC
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "currency": "BTC",
    "totalReserves": "1000.00000000",
    "coldWalletBalance": "900.00000000",
    "hotWalletBalance": "100.00000000",
    "userBalance": "950.00000000",
    "reserveRatio": "105.26",
    "merkleRoot": "0x...",
    "proofTimestamp": "2024-01-01T12:00:00",
    "auditor": "第三方审计机构",
    "auditReportUrl": "https://example.com/audit-report.pdf"
  }
}
```

#### 1.3 验证我的余额

**接口地址**：`GET /api/proof-of-reserves/verify-my-balance`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /api/proof-of-reserves/verify-my-balance
```

**响应示例**：
```json
{
  "code": 200,
  "message": "验证成功",
  "data": {
    "userId": 1,
    "totalBalance": "10000.00000000",
    "verified": true,
    "merkleProof": {
      "merkleRoot": "0x...",
      "proofPath": ["0x...", "0x..."],
      "leafIndex": 100
    },
    "verifyTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.4 获取储备证明历史记录

**接口地址**：`GET /api/proof-of-reserves/history`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 后台管理API

### 1. 储备证明管理

#### 1.1 生成储备证明

**接口地址**：`POST /admin/proof-of-reserves/generate`

**请求参数**：
```json
{
  "currency": "BTC",
  "coldWalletAddresses": ["address1", "address2"],
  "hotWalletAddresses": ["address3"],
  "auditor": "第三方审计机构",
  "auditReportUrl": "https://example.com/audit-report.pdf"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "储备证明生成成功",
  "data": {
    "proofId": 1,
    "currency": "BTC",
    "merkleRoot": "0x...",
    "totalReserves": "1000.00000000",
    "generateTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 更新储备证明

**接口地址**：`PUT /admin/proof-of-reserves/update/{proofId}`

#### 1.3 获取储备证明列表

**接口地址**：`GET /admin/proof-of-reserves/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 储备证明说明

储备证明（Proof of Reserves）是一种机制，用于证明交易平台拥有足够的资产来覆盖所有用户的账户余额。

### 核心特性
- **透明性**：公开储备资产总额和用户总余额
- **可验证性**：用户可以通过Merkle树验证自己的余额
- **定期审计**：由第三方审计机构定期审计

### Merkle树验证
- 每个用户的余额作为Merkle树的一个叶子节点
- 用户可以获取自己的Merkle证明路径
- 通过验证路径可以确认自己的余额包含在储备证明中

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














