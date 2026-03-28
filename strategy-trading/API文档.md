# 策略交易模块 API 文档

## 模块说明
策略交易模块提供策略交易功能，支持多种交易策略的执行和管理。

## Base URL
```
/strategy
```

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 策略执行接口

### 1.1 执行策略

**接口地址**: `POST /strategy/execute`

**接口描述**: 执行指定的交易策略

**请求参数**:
```json
{
  "pairName": "BTC/USDT",
  "marketType": "SPOT",
  "strategyType": "GRID",
  "strategyParams": {
    "gridCount": 10,
    "upperPrice": 51000,
    "lowerPrice": 49000,
    "investmentAmount": 10000
  }
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| marketType | String | 是 | 市场类型: SPOT, FUTURES |
| strategyType | String | 是 | 策略类型 |
| strategyParams | Object | 是 | 策略参数（JSON对象） |

**策略类型**:
- `GRID`: 网格策略
- `DCA`: 定投策略
- `REBALANCE`: 再平衡策略
- `ARBITRAGE`: 套利策略

**响应示例**:
```json
{
  "code": 200,
  "message": "策略执行成功",
  "data": null
}
```

---

## 2. 策略列表接口

### 2.1 获取支持的策略列表

**接口地址**: `GET /strategy/list`

**接口描述**: 获取所有支持的策略类型

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "supportedStrategyTypes": [
      "GRID",
      "DCA",
      "REBALANCE",
      "ARBITRAGE"
    ],
    "strategyCount": 4
  }
}
```

**响应字段说明**:
| 字段 | 类型 | 说明 |
|------|------|------|
| supportedStrategyTypes | Array | 支持的策略类型列表 |
| strategyCount | Integer | 策略数量 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 注意事项

1. 策略执行需要账户有足够的余额
2. 不同策略类型的参数不同，请参考具体策略文档
3. 策略执行后会自动运行，无需手动干预
4. 可以随时停止策略执行
5. 策略参数需要符合策略要求，否则会执行失败

## 策略参数说明

### 网格策略（GRID）
```json
{
  "gridCount": 10,        // 网格数量
  "upperPrice": 51000,    // 上边界价格
  "lowerPrice": 49000,    // 下边界价格
  "investmentAmount": 10000  // 投资金额
}
```

### 定投策略（DCA）
```json
{
  "investmentAmount": 1000,   // 每次投资金额
  "frequency": "DAILY",        // 投资频率: DAILY, WEEKLY, MONTHLY
  "totalInvestment": 10000     // 总投资金额
}
```

### 再平衡策略（REBALANCE）
```json
{
  "assets": [                  // 资产配置
    {"currency": "BTC", "percentage": 0.5},
    {"currency": "ETH", "percentage": 0.3},
    {"currency": "USDT", "percentage": 0.2}
  ],
  "rebalanceThreshold": 0.05   // 再平衡阈值
}
```














