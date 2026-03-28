# 跟单交易模块 API 文档

## 模块说明
跟单交易模块提供跟单交易功能，包括带单员管理、跟单操作、带单员表现查询等。

## Base URL
```
/copy-trading
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 带单员管理

### 1.1 申请成为带单员

**接口地址**: `POST /copy-trading/trader/apply`

**请求参数**:
```json
{
  "traderType": "SPOT",
  "contactInfo": "contact@example.com",
  "assetProof": "https://...",
  "totalAssets": 100000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| traderType | String | 是 | 交易员类型: SPOT（现货）, FUTURES（合约） |
| contactInfo | String | 是 | 联系方式 |
| assetProof | String | 是 | 资产证明 |
| totalAssets | BigDecimal | 是 | 总资产 |

**响应示例**:
```json
{
  "code": 200,
  "message": "申请提交成功",
  "data": {
    "id": 1,
    "userId": 1,
    "traderType": "SPOT",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 1.2 查询交易员列表

**接口地址**: `GET /copy-trading/market`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderType | String | 否 | 交易员类型（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 100,
      "traderType": "SPOT",
      "level": "LEVEL1",
      "status": "ACTIVE",
      "totalFollowers": 100,
      "totalAum": 1000000.00,
      "totalProfit": 50000.00,
      "winRate": 0.65,
      "sharpeRatio": 1.5
    }
  ]
}
```

### 1.3 查询带单员详情

**接口地址**: `GET /copy-trading/trader/{traderId}`

### 1.4 生成邀请码

**接口地址**: `POST /copy-trading/trader/invite`

**接口描述**: 带单员生成私域跟单邀请码

**响应示例**:
```json
{
  "code": 200,
  "message": "邀请码生成成功",
  "data": "INVITE123456"
}
```

### 1.5 审核带单员申请

**接口地址**: `POST /copy-trading/trader/review`

**接口描述**: 后台审核带单员申请（管理员接口）

**请求参数**:
```json
{
  "applicationId": 1,
  "approved": true,
  "rejectReason": null
}
```

---

## 2. 跟单操作

### 2.1 跟随带单员

**接口地址**: `POST /copy-trading/follow`

**接口描述**: 跟随某个带单员的操作（现货或合约）

**请求参数**:
```json
{
  "traderId": 1,
  "marketType": "SPOT",
  "copyType": "FIXED",
  "allocationAmount": 10000.00,
  "copyRatio": 1.0,
  "inviteCode": "INVITE123456",
  "settings": {
    "stopLoss": 0.1,
    "takeProfit": 0.2
  }
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| traderId | Long | 是 | 带单员ID |
| marketType | String | 是 | 市场类型: SPOT, FUTURES |
| copyType | String | 是 | 跟单类型: FIXED（固定金额）, RATIO（比例） |
| allocationAmount | BigDecimal | 是 | 分配金额 |
| copyRatio | BigDecimal | 是 | 跟单比例 |
| inviteCode | String | 否 | 邀请码（私域跟单） |
| settings | Object | 否 | 跟单设置（止损、止盈等） |

**响应示例**:
```json
{
  "code": 200,
  "message": "跟单成功",
  "data": {
    "id": 1,
    "userId": 1,
    "traderId": 100,
    "marketType": "SPOT",
    "status": "ACTIVE",
    "allocationAmount": 10000.00,
    "copyRatio": 1.0,
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 2.2 停止跟单

**接口地址**: `POST /copy-trading/stop`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relationId | Long | 是 | 跟单关系ID |

### 2.3 暂停跟单

**接口地址**: `POST /copy-trading/pause`

### 2.4 恢复跟单

**接口地址**: `POST /copy-trading/resume`

### 2.5 查询跟单状态

**接口地址**: `GET /copy-trading/account/status`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "traderId": 100,
      "marketType": "SPOT",
      "status": "ACTIVE",
      "allocationAmount": 10000.00,
      "currentProfit": 500.00,
      "followedOrders": 10
    }
  ]
}
```

### 2.6 设置跟单参数

**接口地址**: `POST /copy-trading/settings`

**请求参数**:
```json
{
  "allocationAmount": 15000.00,
  "stopLossPrice": 49000.00,
  "takeProfitPrice": 51000.00,
  "copyRatio": 1.2
}
```

---

## 3. 带单员表现

### 3.1 查询带单员排行榜

**接口地址**: `GET /copy-trading/leaderboard`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sortBy | String | 否 | 排序方式: totalAum, PROFIT, WIN_RATE（默认totalAum） |
| limit | Integer | 否 | 返回数量（默认100） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "traderId": 100,
      "traderType": "SPOT",
      "totalAum": 1000000.00,
      "totalProfit": 50000.00,
      "winRate": 0.65,
      "rank": 1
    }
  ]
}
```

### 3.2 查询单个带单员详细表现

**接口地址**: `GET /copy-trading/leaderboard/{traderId}`

### 3.3 获取带单员历史业绩数据

**接口地址**: `GET /copy-trading/leaderboard/{traderId}/performance`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| periodType | String | 否 | 周期类型（可选） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "traderId": 100,
      "periodType": "DAILY",
      "periodStart": "2024-01-01",
      "periodEnd": "2024-01-01",
      "totalProfit": 1000.00,
      "totalLoss": 200.00,
      "netProfit": 800.00,
      "returnRate": 0.08,
      "winRate": 0.7,
      "totalTrades": 10,
      "winningTrades": 7,
      "losingTrades": 3
    }
  ]
}
```

### 3.4 按周期查询带单员表现

**接口地址**: `GET /copy-trading/leaderboard/performance/period`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |
| periodType | String | 是 | 周期类型: DAILY, WEEKLY, MONTHLY, YEARLY |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 注意事项

1. 带单员申请需要审核通过后才能开始带单
2. 跟单类型：FIXED（固定金额）、RATIO（按比例）
3. 可以设置止损和止盈参数控制风险
4. 暂停跟单不会取消已跟单的订单
5. 带单员表现数据实时更新














