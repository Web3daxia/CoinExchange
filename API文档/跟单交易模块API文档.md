# 跟单交易模块 API 文档

## 模块说明
跟单交易模块提供跟单交易功能，包括带单员管理、跟单操作、带单员表现查询等。支持现货跟单和合约跟单，公域跟单和私域跟单。

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

**接口描述**: 提交带单员申请，需要提供交易员类型、联系信息、资产证明等

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "traderType": "SPOT",
  "contactInfo": {
    "facebook": "https://facebook.com/username",
    "twitter": "https://twitter.com/username",
    "telegram": "@username",
    "wechat": "wechat_id",
    "qq": "123456789",
    "phone": "+86 13800138000",
    "email": "trader@example.com"
  },
  "assetProof": "https://example.com/asset-proof.jpg",
  "totalAssets": 100000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| traderType | String | 是 | 交易员类型: SPOT（现货）, FUTURES（合约）, BOTH（两者都是） |
| contactInfo | Map<String, String> | 是 | 联系信息（JSON格式）：Facebook, Twitter, Telegram, WeChat, QQ, Phone, Email等 |
| assetProof | String | 是 | 资产证明（文件路径或URL） |
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
    "contactInfo": "{\"facebook\":\"https://facebook.com/username\",\"twitter\":\"https://twitter.com/username\"}",
    "assetProof": "https://example.com/asset-proof.jpg",
    "totalAssets": 100000.00,
    "rejectReason": null,
    "reviewedBy": null,
    "reviewedAt": null,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

---

### 1.2 查询交易员列表（公域）

**接口地址**: `GET /copy-trading/market`

**接口描述**: 查询平台上的公开交易员列表，支持按交易员类型筛选

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderType | String | 否 | 交易员类型筛选: SPOT（现货）, FUTURES（合约）, BOTH（两者都是）。不传则返回所有类型 |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, SUSPENDED（已暂停）。不传则返回所有状态 |
| level | String | 否 | 等级筛选: BEGINNER（初级）, INTERMEDIATE（中级）, ADVANCED（高级）, TOP（顶级）。不传则返回所有等级 |
| sortBy | String | 否 | 排序方式: totalAum（资产管理规模，默认）, totalProfit（总盈利）, winRate（胜率）, sharpeRatio（夏普比率）, totalFollowers（跟单人数） |
| order | String | 否 | 排序方向: ASC（升序）, DESC（降序，默认） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /copy-trading/market?traderType=SPOT&status=ACTIVE&sortBy=totalAum&order=DESC&page=1&size=20
```

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
      "level": "ADVANCED",
      "status": "ACTIVE",
      "publicEnabled": true,
      "privateEnabled": false,
      "subscriptionFee": null,
      "profitShareRate": 0.7000,
      "totalFollowers": 100,
      "totalAum": 1000000.00,
      "totalProfit": 50000.00,
      "totalLoss": 10000.00,
      "winRate": 0.6500,
      "sharpeRatio": 1.5000,
      "maxDrawdown": 0.1500,
      "lastLiquidationTime": null
    },
    {
      "id": 2,
      "userId": 101,
      "traderType": "FUTURES",
      "level": "TOP",
      "status": "ACTIVE",
      "publicEnabled": true,
      "privateEnabled": true,
      "subscriptionFee": 100.00,
      "profitShareRate": 0.8000,
      "totalFollowers": 500,
      "totalAum": 5000000.00,
      "totalProfit": 200000.00,
      "totalLoss": 30000.00,
      "winRate": 0.7500,
      "sharpeRatio": 2.0000,
      "maxDrawdown": 0.1000,
      "lastLiquidationTime": "2024-01-15T10:30:00"
    }
  ]
}
```

---

### 1.3 查询带单员详情

**接口地址**: `GET /copy-trading/trader/{traderId}`

**接口描述**: 查询单个带单员的详细信息，包括等级、状态、表现数据等

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |

**请求示例**:
```
GET /copy-trading/trader/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 100,
    "traderType": "SPOT",
    "level": "ADVANCED",
    "status": "ACTIVE",
    "publicEnabled": true,
    "privateEnabled": false,
    "subscriptionFee": null,
    "profitShareRate": 0.7000,
    "totalFollowers": 100,
    "totalAum": 1000000.00,
    "totalProfit": 50000.00,
    "totalLoss": 10000.00,
    "winRate": 0.6500,
    "sharpeRatio": 1.5000,
    "maxDrawdown": 0.1500,
    "lastLiquidationTime": null
  }
}
```

---

### 1.4 生成邀请码（私域跟单）

**接口地址**: `POST /copy-trading/trader/invite`

**接口描述**: 带单员生成私域跟单邀请码，用于私域跟单的授权

**请求头**:
```
X-User-Id: 100
```

**请求示例**:
```
POST /copy-trading/trader/invite
```

**响应示例**:
```json
{
  "code": 200,
  "message": "邀请码生成成功",
  "data": "INVITE123456789"
}
```

---

### 1.5 审核带单员申请（管理员接口）

**接口地址**: `POST /copy-trading/trader/review`

**接口描述**: 后台审核带单员申请，只有管理员可以调用

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "applicationId": 1,
  "approved": true,
  "rejectReason": null
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| applicationId | Long | 是 | 申请ID |
| approved | Boolean | 是 | 是否通过: true（通过）, false（拒绝） |
| rejectReason | String | 否 | 拒绝原因（当approved为false时建议填写） |

**响应示例**（审核通过）:
```json
{
  "code": 200,
  "message": "审核成功",
  "data": {
    "id": 1,
    "userId": 1,
    "traderType": "SPOT",
    "level": "BEGINNER",
    "status": "ACTIVE",
    "publicEnabled": false,
    "privateEnabled": false,
    "subscriptionFee": null,
    "profitShareRate": 0.5000,
    "totalFollowers": 0,
    "totalAum": 0.00,
    "totalProfit": 0.00,
    "totalLoss": 0.00,
    "winRate": null,
    "sharpeRatio": null,
    "maxDrawdown": null,
    "lastLiquidationTime": null
  }
}
```

**响应示例**（审核拒绝）:
```json
{
  "code": 200,
  "message": "审核完成（已拒绝）",
  "data": null
}
```

---

### 1.6 查询我的带单员申请状态

**接口地址**: `GET /copy-trading/trader/my-application`

**接口描述**: 查询当前用户的带单员申请状态

**请求头**:
```
X-User-Id: 1
```

**请求示例**:
```
GET /copy-trading/trader/my-application
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 1,
    "traderType": "SPOT",
    "status": "PENDING",
    "contactInfo": "{\"facebook\":\"https://facebook.com/username\"}",
    "assetProof": "https://example.com/asset-proof.jpg",
    "totalAssets": 100000.00,
    "rejectReason": null,
    "reviewedBy": null,
    "reviewedAt": null,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

---

## 2. 跟单操作

### 2.1 跟随带单员

**接口地址**: `POST /copy-trading/follow`

**接口描述**: 跟随某个带单员的操作（现货或合约），支持公域跟单和私域跟单

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "traderId": 100,
  "marketType": "SPOT",
  "copyType": "PUBLIC",
  "allocationAmount": 10000.00,
  "copyRatio": 1.0,
  "inviteCode": null,
  "settings": {
    "leverage": null,
    "marginMode": null,
    "stopLossPrice": 49000.00,
    "takeProfitPrice": 51000.00,
    "stopLossPercentage": 0.05,
    "takeProfitPercentage": 0.10
  }
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| traderId | Long | 是 | 带单员ID |
| marketType | String | 是 | 市场类型: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约） |
| copyType | String | 是 | 跟单类型: PUBLIC（公域跟单）, PRIVATE（私域跟单） |
| allocationAmount | BigDecimal | 是 | 分配金额 |
| copyRatio | BigDecimal | 是 | 跟单比例（0.1-1.0，1.0表示100%复制） |
| inviteCode | String | 否 | 邀请码（私域跟单必填） |
| settings | Map<String, Object> | 否 | 跟单设置（JSON格式）：<br>- leverage: 杠杆倍数（合约跟单）<br>- marginMode: 保证金模式（合约跟单）ISOLATED（逐仓）, CROSS（全仓）<br>- stopLossPrice: 止损价格<br>- takeProfitPrice: 止盈价格<br>- stopLossPercentage: 止损百分比<br>- takeProfitPercentage: 止盈百分比 |

**合约跟单示例**:
```json
{
  "traderId": 101,
  "marketType": "FUTURES_USDT",
  "copyType": "PRIVATE",
  "allocationAmount": 5000.00,
  "copyRatio": 0.5,
  "inviteCode": "INVITE123456789",
  "settings": {
    "leverage": 10,
    "marginMode": "ISOLATED",
    "stopLossPercentage": 0.05,
    "takeProfitPercentage": 0.20
  }
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "跟单成功",
  "data": {
    "id": 1,
    "traderId": 100,
    "followerId": 1,
    "marketType": "SPOT",
    "copyType": "PUBLIC",
    "allocationAmount": 10000.00,
    "allocationPercentage": null,
    "leverage": null,
    "marginMode": null,
    "stopLossPrice": 49000.00,
    "takeProfitPrice": 51000.00,
    "stopLossPercentage": 0.05,
    "takeProfitPercentage": 0.10,
    "copyRatio": 1.0,
    "status": "ACTIVE",
    "totalProfit": 0.00,
    "totalLoss": 0.00,
    "totalCommission": 0.00,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

---

### 2.2 停止跟单

**接口地址**: `POST /copy-trading/stop`

**接口描述**: 停止跟随带单员，停止后不会再复制带单员的交易

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relationId | Long | 是 | 跟单关系ID |

**请求示例**:
```
POST /copy-trading/stop?relationId=1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "停止跟单成功",
  "data": null
}
```

---

### 2.3 暂停跟单

**接口地址**: `POST /copy-trading/pause`

**接口描述**: 暂停跟随带单员，暂停后不会复制新的交易，但已跟单的订单不受影响

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relationId | Long | 是 | 跟单关系ID |

**请求示例**:
```
POST /copy-trading/pause?relationId=1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "暂停跟单成功",
  "data": null
}
```

---

### 2.4 恢复跟单

**接口地址**: `POST /copy-trading/resume`

**接口描述**: 恢复已暂停的跟单关系

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relationId | Long | 是 | 跟单关系ID |

**请求示例**:
```
POST /copy-trading/resume?relationId=1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "恢复跟单成功",
  "data": null
}
```

---

### 2.5 查询跟单状态

**接口地址**: `GET /copy-trading/account/status`

**接口描述**: 查询当前用户的所有跟单状态

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选: SPOT, FUTURES_USDT, FUTURES_COIN。不传则返回所有类型 |
| copyType | String | 否 | 跟单类型筛选: PUBLIC, PRIVATE。不传则返回所有类型 |
| status | String | 否 | 状态筛选: ACTIVE（活跃）, PAUSED（已暂停）, STOPPED（已停止）。不传则返回所有状态 |
| traderId | Long | 否 | 带单员ID筛选。不传则返回所有带单员 |

**请求示例**:
```
GET /copy-trading/account/status?marketType=SPOT&status=ACTIVE
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "traderId": 100,
      "followerId": 1,
      "marketType": "SPOT",
      "copyType": "PUBLIC",
      "allocationAmount": 10000.00,
      "allocationPercentage": null,
      "leverage": null,
      "marginMode": null,
      "stopLossPrice": 49000.00,
      "takeProfitPrice": 51000.00,
      "stopLossPercentage": 0.05,
      "takeProfitPercentage": 0.10,
      "copyRatio": 1.0,
      "status": "ACTIVE",
      "totalProfit": 500.00,
      "totalLoss": 100.00,
      "totalCommission": 50.00,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-15T10:00:00"
    },
    {
      "id": 2,
      "traderId": 101,
      "followerId": 1,
      "marketType": "FUTURES_USDT",
      "copyType": "PRIVATE",
      "allocationAmount": 5000.00,
      "allocationPercentage": null,
      "leverage": 10,
      "marginMode": "ISOLATED",
      "stopLossPrice": null,
      "takeProfitPrice": null,
      "stopLossPercentage": 0.05,
      "takeProfitPercentage": 0.20,
      "copyRatio": 0.5,
      "status": "PAUSED",
      "totalProfit": 200.00,
      "totalLoss": 50.00,
      "totalCommission": 30.00,
      "createdAt": "2024-01-05T10:00:00",
      "updatedAt": "2024-01-10T10:00:00"
    }
  ]
}
```

---

### 2.6 设置跟单参数

**接口地址**: `POST /copy-trading/settings`

**接口描述**: 更新跟单参数（资金分配、止损、止盈等）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relationId | Long | 是 | 跟单关系ID（Query参数） |

**请求体**:
```json
{
  "allocationAmount": 15000.00,
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00,
  "copyRatio": 1.2,
  "settings": {
    "stopLossPercentage": 0.06,
    "takeProfitPercentage": 0.12
  }
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| allocationAmount | BigDecimal | 否 | 分配金额 |
| stopLossPrice | BigDecimal | 否 | 止损价格 |
| takeProfitPrice | BigDecimal | 否 | 止盈价格 |
| copyRatio | BigDecimal | 否 | 跟单比例 |
| settings | Map<String, Object> | 否 | 其他设置（JSON格式） |

**请求示例**:
```
POST /copy-trading/settings?relationId=1
Content-Type: application/json

{
  "allocationAmount": 15000.00,
  "stopLossPrice": 48000.00,
  "takeProfitPrice": 52000.00,
  "copyRatio": 1.2
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "设置更新成功",
  "data": {
    "id": 1,
    "traderId": 100,
    "followerId": 1,
    "marketType": "SPOT",
    "copyType": "PUBLIC",
    "allocationAmount": 15000.00,
    "allocationPercentage": null,
    "leverage": null,
    "marginMode": null,
    "stopLossPrice": 48000.00,
    "takeProfitPrice": 52000.00,
    "stopLossPercentage": 0.06,
    "takeProfitPercentage": 0.12,
    "copyRatio": 1.2,
    "status": "ACTIVE",
    "totalProfit": 500.00,
    "totalLoss": 100.00,
    "totalCommission": 50.00,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

## 3. 带单员表现

### 3.1 查询带单员排行榜

**接口地址**: `GET /copy-trading/leaderboard`

**接口描述**: 查询带单员的绩效排名，支持多种排序方式

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderType | String | 否 | 交易员类型筛选: SPOT, FUTURES, BOTH。不传则返回所有类型 |
| sortBy | String | 否 | 排序方式: totalAum（资产管理规模，默认）, totalProfit（总盈利）, winRate（胜率）, sharpeRatio（夏普比率）, totalFollowers（跟单人数）, returnRate（收益率） |
| order | String | 否 | 排序方向: ASC（升序）, DESC（降序，默认） |
| limit | Integer | 否 | 返回数量（默认100，最大500） |

**请求示例**:
```
GET /copy-trading/leaderboard?traderType=SPOT&sortBy=totalProfit&order=DESC&limit=50
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "traderId": 101,
      "userId": 101,
      "traderType": "FUTURES",
      "level": "TOP",
      "totalAum": 5000000.00,
      "totalProfit": 200000.00,
      "totalLoss": 30000.00,
      "netProfit": 170000.00,
      "returnRate": 0.3400,
      "winRate": 0.7500,
      "sharpeRatio": 2.0000,
      "maxDrawdown": 0.1000,
      "totalFollowers": 500,
      "totalTrades": 1000,
      "winningTrades": 750,
      "losingTrades": 250,
      "rank": 1
    },
    {
      "traderId": 100,
      "userId": 100,
      "traderType": "SPOT",
      "level": "ADVANCED",
      "totalAum": 1000000.00,
      "totalProfit": 50000.00,
      "totalLoss": 10000.00,
      "netProfit": 40000.00,
      "returnRate": 0.4000,
      "winRate": 0.6500,
      "sharpeRatio": 1.5000,
      "maxDrawdown": 0.1500,
      "totalFollowers": 100,
      "totalTrades": 200,
      "winningTrades": 130,
      "losingTrades": 70,
      "rank": 2
    }
  ]
}
```

---

### 3.2 查询单个带单员详细表现

**接口地址**: `GET /copy-trading/leaderboard/{traderId}`

**接口描述**: 查询单个带单员的详细表现数据，包括各项指标和统计数据

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |

**请求示例**:
```
GET /copy-trading/leaderboard/100
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "traderId": 100,
    "userId": 100,
    "traderType": "SPOT",
    "level": "ADVANCED",
    "status": "ACTIVE",
    "totalAum": 1000000.00,
    "totalProfit": 50000.00,
    "totalLoss": 10000.00,
    "netProfit": 40000.00,
    "returnRate": 0.4000,
    "winRate": 0.6500,
    "sharpeRatio": 1.5000,
    "maxDrawdown": 0.1500,
    "totalFollowers": 100,
    "totalTrades": 200,
    "winningTrades": 130,
    "losingTrades": 70,
    "avgProfit": 384.62,
    "avgLoss": 142.86,
    "profitLossRatio": 2.6923,
    "dailyAvgTrades": 5.5,
    "lastLiquidationTime": null,
    "performanceByPeriod": {
      "7d": {
        "netProfit": 5000.00,
        "returnRate": 0.0500,
        "winRate": 0.7000,
        "totalTrades": 35
      },
      "30d": {
        "netProfit": 15000.00,
        "returnRate": 0.1500,
        "winRate": 0.6800,
        "totalTrades": 120
      },
      "90d": {
        "netProfit": 30000.00,
        "returnRate": 0.3000,
        "winRate": 0.6500,
        "totalTrades": 180
      }
    }
  }
}
```

---

### 3.3 获取带单员历史业绩数据

**接口地址**: `GET /copy-trading/leaderboard/{traderId}/performance`

**接口描述**: 获取带单员的历史业绩数据，支持按周期类型筛选

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| periodType | String | 否 | 周期类型筛选: DAILY（日）, WEEKLY（周）, MONTHLY（月）, YEARLY（年）。不传则返回所有类型 |
| startDate | String | 否 | 开始日期（格式: yyyy-MM-dd） |
| endDate | String | 否 | 结束日期（格式: yyyy-MM-dd） |
| limit | Integer | 否 | 返回数量（默认100，最大500） |

**请求示例**:
```
GET /copy-trading/leaderboard/100/performance?periodType=MONTHLY&limit=12
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "traderId": 100,
      "periodType": "MONTHLY",
      "periodStart": "2024-01-01T00:00:00",
      "periodEnd": "2024-01-31T23:59:59",
      "totalProfit": 15000.00,
      "totalLoss": 3000.00,
      "netProfit": 12000.00,
      "returnRate": 0.1200,
      "winRate": 0.6800,
      "totalTrades": 120,
      "winningTrades": 82,
      "losingTrades": 38,
      "avgProfit": 182.93,
      "avgLoss": 78.95,
      "profitLossRatio": 2.3167,
      "sharpeRatio": 1.4500,
      "maxDrawdown": 0.0800,
      "totalAum": 1000000.00,
      "totalFollowers": 100,
      "dailyAvgTrades": 3.87
    },
    {
      "traderId": 100,
      "periodType": "MONTHLY",
      "periodStart": "2023-12-01T00:00:00",
      "periodEnd": "2023-12-31T23:59:59",
      "totalProfit": 12000.00,
      "totalLoss": 4000.00,
      "netProfit": 8000.00,
      "returnRate": 0.0800,
      "winRate": 0.6200,
      "totalTrades": 80,
      "winningTrades": 50,
      "losingTrades": 30,
      "avgProfit": 240.00,
      "avgLoss": 133.33,
      "profitLossRatio": 1.8000,
      "sharpeRatio": 1.2000,
      "maxDrawdown": 0.1200,
      "totalAum": 950000.00,
      "totalFollowers": 95,
      "dailyAvgTrades": 2.58
    }
  ]
}
```

---

### 3.4 按周期查询带单员表现

**接口地址**: `GET /copy-trading/leaderboard/performance/period`

**接口描述**: 按指定周期（如7天、30天、90天、180天、360天）查询带单员的表现数据

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |
| periodType | String | 是 | 周期类型: DAYS_7（7天）, DAYS_30（30天）, DAYS_90（90天）, DAYS_180（180天）, DAYS_360（360天）, DAILY（日）, WEEKLY（周）, MONTHLY（月）, YEARLY（年） |

**请求示例**:
```
GET /copy-trading/leaderboard/performance/period?traderId=100&periodType=DAYS_30
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "traderId": 100,
      "periodType": "DAYS_30",
      "periodStart": "2023-12-02T00:00:00",
      "periodEnd": "2024-01-01T23:59:59",
      "totalProfit": 15000.00,
      "totalLoss": 3000.00,
      "netProfit": 12000.00,
      "returnRate": 0.1200,
      "winRate": 0.6800,
      "totalTrades": 120,
      "winningTrades": 82,
      "losingTrades": 38,
      "avgProfit": 182.93,
      "avgLoss": 78.95,
      "profitLossRatio": 2.3167,
      "sharpeRatio": 1.4500,
      "maxDrawdown": 0.0800,
      "totalAum": 1000000.00,
      "totalFollowers": 100,
      "dailyAvgTrades": 4.00
    }
  ]
}
```

---

### 3.5 查询带单员表现趋势数据

**接口地址**: `GET /copy-trading/leaderboard/{traderId}/trend`

**接口描述**: 查询带单员的表现趋势数据，用于展示图表（如跟单人数增长、AUM变化等）

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| traderId | Long | 是 | 带单员ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 周期: 7D（7天，默认）, 30D（30天）, 90D（90天）, 180D（180天）, 360D（360天） |
| metric | String | 否 | 指标类型: FOLLOWERS（跟单人数）, AUM（资产管理规模）, PROFIT（盈利）, RETURN_RATE（收益率）。不传则返回所有指标 |

**请求示例**:
```
GET /copy-trading/leaderboard/100/trend?period=30D&metric=FOLLOWERS
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "traderId": 100,
    "period": "30D",
    "metrics": {
      "FOLLOWERS": [
        {"date": "2023-12-02", "value": 90},
        {"date": "2023-12-03", "value": 92},
        {"date": "2023-12-04", "value": 95},
        {"date": "2024-01-01", "value": 100}
      ],
      "AUM": [
        {"date": "2023-12-02", "value": 900000.00},
        {"date": "2023-12-03", "value": 920000.00},
        {"date": "2024-01-01", "value": 1000000.00}
      ],
      "PROFIT": [
        {"date": "2023-12-02", "value": 0.00},
        {"date": "2023-12-03", "value": 500.00},
        {"date": "2024-01-01", "value": 12000.00}
      ],
      "RETURN_RATE": [
        {"date": "2023-12-02", "value": 0.0000},
        {"date": "2023-12-03", "value": 0.0006},
        {"date": "2024-01-01", "value": 0.1200}
      ]
    }
  }
}
```

---

## 4. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

**常见错误示例**:
```json
{
  "code": 400,
  "message": "带单员ID不能为空",
  "data": null
}
```

```json
{
  "code": 404,
  "message": "带单员不存在",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "该带单员仅支持私域跟单，请提供邀请码",
  "data": null
}
```

---

## 5. 注意事项

1. **带单员申请**：
   - 需要满足资产门槛（由后台配置）
   - 需要提交完整的联系信息和资产证明
   - 申请提交后需要等待后台审核

2. **保证金管理**：
   - 成为带单员需要冻结保证金（由后台配置金额）
   - 退出带单员时退还保证金
   - 保证金冻结期间无法使用

3. **跟单类型**：
   - 公域跟单：任何用户都可以选择跟随，通常免费或低费用
   - 私域跟单：需要邀请码，可以设置订阅费或成功交易佣金

4. **跟单风险**：
   - 跟单存在风险，跟单员应设置合理的止损和止盈
   - 合约跟单需要设置杠杆倍数和保证金模式
   - 可以随时暂停或停止跟单

5. **分润机制**：
   - 分润比例根据带单员等级动态调整（50%-80%）
   - 分润从跟单员的盈利中抽取
   - 分润比例：BEGINNER（50%）、INTERMEDIATE（60%）、ADVANCED（70%）、TOP（80%）

6. **表现数据**：
   - 带单员表现数据实时更新
   - 支持按周期查询（7天、30天、90天、180天、360天）
   - 数据包括：收益率、胜率、夏普比率、最大回撤、盈亏比等

7. **周期说明**：
   - DAYS_7：最近7天
   - DAYS_30：最近30天
   - DAYS_90：最近90天
   - DAYS_180：最近180天
   - DAYS_360：最近360天
   - DAILY：按日统计
   - WEEKLY：按周统计
   - MONTHLY：按月统计
   - YEARLY：按年统计

---

## 6. 数据字典

### 6.1 交易员类型（traderType）
- `SPOT`：现货交易员
- `FUTURES`：合约交易员
- `BOTH`：两者都是

### 6.2 市场类型（marketType）
- `SPOT`：现货市场
- `FUTURES_USDT`：USDT本位合约
- `FUTURES_COIN`：币本位合约

### 6.3 跟单类型（copyType）
- `PUBLIC`：公域跟单
- `PRIVATE`：私域跟单

### 6.4 带单员等级（level）
- `BEGINNER`：初级（分润比例50%）
- `INTERMEDIATE`：中级（分润比例60%）
- `ADVANCED`：高级（分润比例70%）
- `TOP`：顶级（分润比例80%）

### 6.5 带单员状态（status）
- `PENDING`：待审核
- `APPROVED`：已通过（已废弃，使用ACTIVE）
- `ACTIVE`：活跃
- `REJECTED`：已拒绝
- `SUSPENDED`：已暂停
- `CANCELLED`：已取消

### 6.6 跟单关系状态（status）
- `ACTIVE`：活跃
- `PAUSED`：已暂停
- `STOPPED`：已停止

### 6.7 保证金模式（marginMode）
- `ISOLATED`：逐仓
- `CROSS`：全仓

### 6.8 周期类型（periodType）
- `DAYS_7`：7天
- `DAYS_30`：30天
- `DAYS_90`：90天
- `DAYS_180`：180天
- `DAYS_360`：360天
- `DAILY`：日
- `WEEKLY`：周
- `MONTHLY`：月
- `YEARLY`：年

---

**文档版本**：v2.0  
**最后更新**：2024-01-16
