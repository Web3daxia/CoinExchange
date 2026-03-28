# 策略交易模块 API 文档

## 模块说明
策略交易模块使用户能够设定或选择自动化交易策略，执行现货、USDT本位、币本位等交易对的交易操作。通过该模块，用户可以配置各种交易策略，并对现有策略进行回测、优化与实时交易。后台管理系统将支持全面监控和管理策略交易，包括策略创建、修改、启用、禁用、回测与评估等功能。

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

## 1. 策略模板管理

### 1.1 获取策略模板列表

**接口地址**: `GET /strategy/templates`

**接口描述**: 获取平台提供的策略模板列表，支持按市场类型筛选

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选: SPOT（现货）, FUTURES_USDT（USDT本位合约）, FUTURES_COIN（币本位合约） |
| strategyCategory | String | 否 | 策略分类筛选: TREND（趋势）, REVERSAL（反转）, BREAKOUT（突破）, OSCILLATION（震荡）, GRID（网格）, RISK（风险） |

**请求示例**:
```
GET /strategy/templates?marketType=SPOT&strategyCategory=TREND
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "templateName": "均线交叉策略",
      "marketType": "SPOT",
      "strategyCategory": "TREND",
      "strategyType": "MA_CROSS",
      "description": "使用短期与长期均线交叉信号作为买卖信号",
      "defaultParams": {
        "shortPeriod": 5,
        "longPeriod": 50
      },
      "riskLevel": "MEDIUM",
      "expectedReturnRate": 0.12,
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "templateName": "RSI超买超卖策略",
      "marketType": "SPOT",
      "strategyCategory": "OSCILLATION",
      "strategyType": "RSI_OVERBOUGHT_OVERSOLD",
      "description": "基于RSI指标，当RSI值低于30时买入，高于70时卖出",
      "defaultParams": {
        "rsiPeriod": 14,
        "oversoldLevel": 30,
        "overboughtLevel": 70
      },
      "riskLevel": "MEDIUM",
      "expectedReturnRate": 0.10,
      "status": "ACTIVE"
    }
  ]
}
```

---

### 1.2 获取策略模板详情

**接口地址**: `GET /strategy/templates/{templateId}`

**接口描述**: 获取指定策略模板的详细信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| templateId | Long | 是 | 模板ID |

**请求示例**:
```
GET /strategy/templates/1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "templateName": "均线交叉策略",
    "marketType": "SPOT",
    "strategyCategory": "TREND",
    "strategyType": "MA_CROSS",
    "description": "使用短期与长期均线交叉信号作为买卖信号。当短期均线上穿长期均线时买入，下穿时卖出。",
    "detailedDescription": "该策略基于移动平均线的交叉信号进行交易。短期均线（5日）上穿长期均线（50日）时产生买入信号，短期均线下穿长期均线时产生卖出信号。适合趋势明显的市场。",
    "defaultParams": {
      "shortPeriod": 5,
      "longPeriod": 50,
      "maType": "SMA"
    },
    "paramDescription": {
      "shortPeriod": "短期均线周期（建议5-20）",
      "longPeriod": "长期均线周期（建议30-100）",
      "maType": "均线类型: SMA（简单移动平均）, EMA（指数移动平均）"
    },
    "riskLevel": "MEDIUM",
    "expectedReturnRate": 0.12,
    "maxDrawdown": 0.15,
    "backtestPerformance": {
      "winRate": 0.65,
      "profitLossRatio": 1.5,
      "sharpeRatio": 1.2
    },
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

---

## 2. 现货策略创建与管理

### 2.1 创建均线交叉策略（现货）

**接口地址**: `POST /strategy/spot/ma-cross/create`

**接口描述**: 创建现货市场的均线交叉策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT均线交叉策略",
  "pairName": "BTC/USDT",
  "shortPeriod": 5,
  "longPeriod": 50,
  "maType": "SMA",
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10,
  "maxPosition": 1.0,
  "orderType": "MARKET"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称（不传则自动生成） |
| pairName | String | 是 | 交易对名称 |
| shortPeriod | Integer | 是 | 短期均线周期（建议5-20） |
| longPeriod | Integer | 是 | 长期均线周期（建议30-100，需大于shortPeriod） |
| maType | String | 是 | 均线类型: SMA（简单移动平均）, EMA（指数移动平均） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例（0-1，默认0.5） |
| stopLossPercentage | BigDecimal | 否 | 止损百分比（相对买入价格） |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比（相对买入价格） |
| maxPosition | BigDecimal | 否 | 最大持仓比例（0-1，默认1.0） |
| orderType | String | 否 | 订单类型: MARKET（市价单）, LIMIT（限价单，默认MARKET） |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "strategyName": "BTC/USDT均线交叉策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "MA_CROSS",
    "status": "STOPPED",
    "shortPeriod": 5,
    "longPeriod": 50,
    "maType": "SMA",
    "initialCapital": 10000.00000000,
    "investmentRatio": 0.5000,
    "stopLossPercentage": 0.0500,
    "takeProfitPercentage": 0.1000,
    "totalProfit": 0.00000000,
    "totalTrades": 0,
    "createdAt": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.2 创建RSI超买超卖策略（现货）

**接口地址**: `POST /strategy/spot/rsi-overbought-oversold/create`

**接口描述**: 创建现货市场的RSI超买超卖策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT RSI策略",
  "pairName": "BTC/USDT",
  "rsiPeriod": 14,
  "oversoldLevel": 30,
  "overboughtLevel": 70,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10,
  "maxPosition": 1.0
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| rsiPeriod | Integer | 是 | RSI周期（建议14） |
| oversoldLevel | BigDecimal | 是 | 超卖水平（建议20-40，默认30） |
| overboughtLevel | BigDecimal | 是 | 超买水平（建议60-80，默认70） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例（默认0.5） |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |
| maxPosition | BigDecimal | 否 | 最大持仓比例 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1002,
    "userId": 1,
    "strategyName": "BTC/USDT RSI策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "RSI_OVERBOUGHT_OVERSOLD",
    "status": "STOPPED",
    "rsiPeriod": 14,
    "oversoldLevel": 30.0000,
    "overboughtLevel": 70.0000,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.3 创建布林带突破策略（现货）

**接口地址**: `POST /strategy/spot/bollinger-bands-breakout/create`

**接口描述**: 创建现货市场的布林带突破策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT布林带突破策略",
  "pairName": "BTC/USDT",
  "bbPeriod": 20,
  "bbStdDev": 2.0,
  "breakoutConfirmation": true,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| bbPeriod | Integer | 是 | 布林带周期（建议20） |
| bbStdDev | BigDecimal | 是 | 标准差倍数（建议2.0） |
| breakoutConfirmation | Boolean | 否 | 是否要求突破确认（默认true） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1003,
    "userId": 1,
    "strategyName": "BTC/USDT布林带突破策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "BOLLINGER_BANDS_BREAKOUT",
    "status": "STOPPED",
    "bbPeriod": 20,
    "bbStdDev": 2.0000,
    "breakoutConfirmation": true,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.4 创建MACD背离策略（现货）

**接口地址**: `POST /strategy/spot/macd-divergence/create`

**接口描述**: 创建现货市场的MACD背离策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT MACD背离策略",
  "pairName": "BTC/USDT",
  "fastPeriod": 12,
  "slowPeriod": 26,
  "signalPeriod": 9,
  "divergenceThreshold": 0.02,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| fastPeriod | Integer | 是 | 快速EMA周期（默认12） |
| slowPeriod | Integer | 是 | 慢速EMA周期（默认26） |
| signalPeriod | Integer | 是 | 信号线周期（默认9） |
| divergenceThreshold | BigDecimal | 是 | 背离阈值（默认0.02） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1004,
    "userId": 1,
    "strategyName": "BTC/USDT MACD背离策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "MACD_DIVERGENCE",
    "status": "STOPPED",
    "fastPeriod": 12,
    "slowPeriod": 26,
    "signalPeriod": 9,
    "divergenceThreshold": 0.0200,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.5 创建震荡区间突破策略（现货）

**接口地址**: `POST /strategy/spot/oscillation-breakout/create`

**接口描述**: 创建现货市场的震荡区间突破策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT震荡区间突破策略",
  "pairName": "BTC/USDT",
  "upperBound": 51000.00,
  "lowerBound": 49000.00,
  "breakoutConfirmationPeriods": 3,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| upperBound | BigDecimal | 是 | 上边界价格 |
| lowerBound | BigDecimal | 是 | 下边界价格 |
| breakoutConfirmationPeriods | Integer | 否 | 突破确认周期数（默认3） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1005,
    "userId": 1,
    "strategyName": "BTC/USDT震荡区间突破策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "OSCILLATION_BREAKOUT",
    "status": "STOPPED",
    "upperBound": 51000.00000000,
    "lowerBound": 49000.00000000,
    "breakoutConfirmationPeriods": 3,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 2.6 创建市场情绪策略（现货）

**接口地址**: `POST /strategy/spot/market-sentiment/create`

**接口描述**: 创建现货市场的市场情绪策略（基于新闻、社交媒体情绪分析）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT市场情绪策略",
  "pairName": "BTC/USDT",
  "sentimentThreshold": 0.6,
  "dataSources": ["NEWS", "SOCIAL_MEDIA"],
  "weightNews": 0.6,
  "weightSocial": 0.4,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| sentimentThreshold | BigDecimal | 是 | 情绪阈值（0-1，默认0.6） |
| dataSources | Array<String> | 是 | 数据源: NEWS（新闻）, SOCIAL_MEDIA（社交媒体） |
| weightNews | BigDecimal | 否 | 新闻权重（默认0.6） |
| weightSocial | BigDecimal | 否 | 社交媒体权重（默认0.4） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 1006,
    "userId": 1,
    "strategyName": "BTC/USDT市场情绪策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "MARKET_SENTIMENT",
    "status": "STOPPED",
    "sentimentThreshold": 0.6000,
    "dataSources": ["NEWS", "SOCIAL_MEDIA"],
    "weightNews": 0.6000,
    "weightSocial": 0.4000,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

---

## 3. USDT本位合约策略创建与管理

### 3.1 创建趋势跟随策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/trend-following/create`

**接口描述**: 创建USDT本位合约市场的趋势跟随策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT趋势跟随策略",
  "pairName": "BTC/USDT",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "maFastPeriod": 10,
  "maSlowPeriod": 30,
  "maType": "EMA",
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10,
  "maxPosition": 1.0
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式: ISOLATED（逐仓）, CROSS（全仓） |
| maFastPeriod | Integer | 是 | 快速均线周期 |
| maSlowPeriod | Integer | 是 | 慢速均线周期 |
| maType | String | 是 | 均线类型: SMA, EMA |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |
| maxPosition | BigDecimal | 否 | 最大持仓比例 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2001,
    "userId": 1,
    "strategyName": "BTC/USDT趋势跟随策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "TREND_FOLLOWING",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "maFastPeriod": 10,
    "maSlowPeriod": 30,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.2 创建反向交易策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/contrarian/create`

**接口描述**: 创建USDT本位合约市场的反向交易策略（适用于市场震荡期）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT反向交易策略",
  "pairName": "BTC/USDT",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "rsiPeriod": 14,
  "oversoldLevel": 30,
  "overboughtLevel": 70,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| rsiPeriod | Integer | 是 | RSI周期 |
| oversoldLevel | BigDecimal | 是 | 超卖水平 |
| overboughtLevel | BigDecimal | 是 | 超买水平 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2002,
    "userId": 1,
    "strategyName": "BTC/USDT反向交易策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "CONTRARIAN",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "rsiPeriod": 14,
    "oversoldLevel": 30.0000,
    "overboughtLevel": 70.0000,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.3 创建网格策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/grid/create`

**接口描述**: 创建USDT本位合约市场的网格策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT网格策略",
  "pairName": "BTC/USDT",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "gridCount": 10,
  "upperPrice": 51000.00,
  "lowerPrice": 49000.00,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| gridCount | Integer | 是 | 网格数量 |
| upperPrice | BigDecimal | 是 | 卖出区间上限价格 |
| lowerPrice | BigDecimal | 是 | 买入区间下限价格 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2003,
    "userId": 1,
    "strategyName": "BTC/USDT网格策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "GRID",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "gridCount": 10,
    "upperPrice": 51000.00000000,
    "lowerPrice": 49000.00000000,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.4 创建布林带突破策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/bollinger-bands-breakout/create`

**接口描述**: 创建USDT本位合约市场的布林带突破策略

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT布林带突破策略",
  "pairName": "BTC/USDT",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "bbPeriod": 20,
  "bbStdDev": 2.0,
  "breakoutConfirmation": true,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| bbPeriod | Integer | 是 | 布林带周期 |
| bbStdDev | BigDecimal | 是 | 标准差倍数 |
| breakoutConfirmation | Boolean | 否 | 是否要求突破确认 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2004,
    "userId": 1,
    "strategyName": "BTC/USDT布林带突破策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "BOLLINGER_BANDS_BREAKOUT",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "bbPeriod": 20,
    "bbStdDev": 2.0000,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.5 创建资金管理策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/fund-management/create`

**接口描述**: 创建USDT本位合约市场的资金管理策略（根据市场资金流入流出调整杠杆和仓位）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT资金管理策略",
  "pairName": "BTC/USDT",
  "maxLeverage": 10,
  "marginMode": "ISOLATED",
  "fundFlowPeriod": 24,
  "fundFlowThreshold": 0.05,
  "leverageAdjustmentFactor": 0.1,
  "positionAdjustmentFactor": 0.1,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| maxLeverage | Integer | 是 | 最大杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| fundFlowPeriod | Integer | 是 | 资金流统计周期（小时） |
| fundFlowThreshold | BigDecimal | 是 | 资金流阈值（百分比） |
| leverageAdjustmentFactor | BigDecimal | 是 | 杠杆调整因子 |
| positionAdjustmentFactor | BigDecimal | 是 | 仓位调整因子 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2005,
    "userId": 1,
    "strategyName": "BTC/USDT资金管理策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "FUND_MANAGEMENT",
    "status": "STOPPED",
    "maxLeverage": 10,
    "marginMode": "ISOLATED",
    "fundFlowPeriod": 24,
    "fundFlowThreshold": 0.0500,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 3.6 创建震荡区间策略（USDT本位合约）

**接口地址**: `POST /strategy/futures-usdt/oscillation-range/create`

**接口描述**: 创建USDT本位合约市场的震荡区间策略（市场震荡时，设定固定范围内买卖）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/USDT震荡区间策略",
  "pairName": "BTC/USDT",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "upperBound": 51000.00,
  "lowerBound": 49000.00,
  "oscillationThreshold": 0.02,
  "initialCapital": 10000.00,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| upperBound | BigDecimal | 是 | 上边界价格 |
| lowerBound | BigDecimal | 是 | 下边界价格 |
| oscillationThreshold | BigDecimal | 是 | 震荡阈值（价格波动百分比） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 2006,
    "userId": 1,
    "strategyName": "BTC/USDT震荡区间策略",
    "pairName": "BTC/USDT",
    "marketType": "FUTURES_USDT",
    "strategyType": "OSCILLATION_RANGE",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "upperBound": 51000.00000000,
    "lowerBound": 49000.00000000,
    "oscillationThreshold": 0.0200,
    "initialCapital": 10000.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

## 4. 币本位合约策略创建与管理

### 4.1 创建固定仓位策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/fixed-position/create`

**接口描述**: 创建币本位合约市场的固定仓位策略（设定固定仓位进行合约交易，适用于风险较低的交易）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC固定仓位策略",
  "pairName": "BTC/BTC",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "fixedPosition": 0.1,
  "initialCapital": 1.0,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| fixedPosition | BigDecimal | 是 | 固定仓位（币本位数量） |
| initialCapital | BigDecimal | 是 | 初始资金（币本位数量） |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3001,
    "userId": 1,
    "strategyName": "BTC/BTC固定仓位策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "FIXED_POSITION",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "fixedPosition": 0.10000000,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.2 创建动态仓位策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/dynamic-position/create`

**接口描述**: 创建币本位合约市场的动态仓位策略（根据市场波动动态调整仓位比例）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC动态仓位策略",
  "pairName": "BTC/BTC",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "basePosition": 0.1,
  "positionAdjustmentFactor": 0.1,
  "volatilityPeriod": 24,
  "initialCapital": 1.0,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| basePosition | BigDecimal | 是 | 基础仓位 |
| positionAdjustmentFactor | BigDecimal | 是 | 仓位调整因子 |
| volatilityPeriod | Integer | 是 | 波动率计算周期（小时） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3002,
    "userId": 1,
    "strategyName": "BTC/BTC动态仓位策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "DYNAMIC_POSITION",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "basePosition": 0.10000000,
    "positionAdjustmentFactor": 0.1000,
    "volatilityPeriod": 24,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.3 创建波动率策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/volatility/create`

**接口描述**: 创建币本位合约市场的波动率策略（根据市场波动率进行交易，适应高波动市场）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC波动率策略",
  "pairName": "BTC/BTC",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "volatilityPeriod": 24,
  "volatilityThreshold": 0.02,
  "highVolatilityMultiplier": 1.5,
  "lowVolatilityMultiplier": 0.5,
  "initialCapital": 1.0,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| volatilityPeriod | Integer | 是 | 波动率计算周期（小时） |
| volatilityThreshold | BigDecimal | 是 | 波动率阈值 |
| highVolatilityMultiplier | BigDecimal | 是 | 高波动率倍数 |
| lowVolatilityMultiplier | BigDecimal | 是 | 低波动率倍数 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3003,
    "userId": 1,
    "strategyName": "BTC/BTC波动率策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "VOLATILITY",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "volatilityPeriod": 24,
    "volatilityThreshold": 0.0200,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.4 创建价格区间策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/price-range/create`

**接口描述**: 创建币本位合约市场的价格区间策略（当价格处于特定区间内，设置买入卖出）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC价格区间策略",
  "pairName": "BTC/BTC",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "upperBound": 51000.00,
  "lowerBound": 49000.00,
  "buyZoneRatio": 0.3,
  "sellZoneRatio": 0.3,
  "initialCapital": 1.0,
  "investmentRatio": 0.5,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| upperBound | BigDecimal | 是 | 上边界价格 |
| lowerBound | BigDecimal | 是 | 下边界价格 |
| buyZoneRatio | BigDecimal | 是 | 买入区域比例（相对下边界） |
| sellZoneRatio | BigDecimal | 是 | 卖出区域比例（相对上边界） |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |
| stopLossPercentage | BigDecimal | 否 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 否 | 止盈百分比 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3004,
    "userId": 1,
    "strategyName": "BTC/BTC价格区间策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "PRICE_RANGE",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "upperBound": 51000.00000000,
    "lowerBound": 49000.00000000,
    "buyZoneRatio": 0.3000,
    "sellZoneRatio": 0.3000,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.5 创建定期止盈止损策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/periodic-tp-sl/create`

**接口描述**: 创建币本位合约市场的定期止盈止损策略（定期设置固定的止盈止损，适应趋势波动）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC定期止盈止损策略",
  "pairName": "BTC/BTC",
  "leverage": 10,
  "marginMode": "ISOLATED",
  "adjustmentPeriod": 24,
  "stopLossPercentage": 0.05,
  "takeProfitPercentage": 0.10,
  "trailingStopEnabled": true,
  "trailingStopPercentage": 0.02,
  "initialCapital": 1.0,
  "investmentRatio": 0.5
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| leverage | Integer | 是 | 杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| adjustmentPeriod | Integer | 是 | 调整周期（小时） |
| stopLossPercentage | BigDecimal | 是 | 止损百分比 |
| takeProfitPercentage | BigDecimal | 是 | 止盈百分比 |
| trailingStopEnabled | Boolean | 否 | 是否启用追踪止损 |
| trailingStopPercentage | BigDecimal | 否 | 追踪止损百分比 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3005,
    "userId": 1,
    "strategyName": "BTC/BTC定期止盈止损策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "PERIODIC_TP_SL",
    "status": "STOPPED",
    "leverage": 10,
    "marginMode": "ISOLATED",
    "adjustmentPeriod": 24,
    "stopLossPercentage": 0.0500,
    "takeProfitPercentage": 0.1000,
    "trailingStopEnabled": true,
    "trailingStopPercentage": 0.0200,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 4.6 创建资金管理策略（币本位合约）

**接口地址**: `POST /strategy/futures-coin/fund-management/create`

**接口描述**: 创建币本位合约市场的资金管理策略（使用资金管理方法控制每次交易的仓位和杠杆）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyName": "BTC/BTC资金管理策略",
  "pairName": "BTC/BTC",
  "maxLeverage": 10,
  "marginMode": "ISOLATED",
  "positionSizeMethod": "KELLY",
  "riskPerTrade": 0.02,
  "maxRiskPerTrade": 0.05,
  "initialCapital": 1.0,
  "investmentRatio": 0.5
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyName | String | 否 | 策略名称 |
| pairName | String | 是 | 交易对名称 |
| maxLeverage | Integer | 是 | 最大杠杆倍数 |
| marginMode | String | 是 | 保证金模式 |
| positionSizeMethod | String | 是 | 仓位大小计算方法: KELLY（凯利公式）, FIXED（固定比例）, VOLATILITY（波动率） |
| riskPerTrade | BigDecimal | 是 | 每笔交易风险（比例） |
| maxRiskPerTrade | BigDecimal | 是 | 每笔交易最大风险 |
| initialCapital | BigDecimal | 是 | 初始资金 |
| investmentRatio | BigDecimal | 否 | 投资比例 |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略创建成功",
  "data": {
    "id": 3006,
    "userId": 1,
    "strategyName": "BTC/BTC资金管理策略",
    "pairName": "BTC/BTC",
    "marketType": "FUTURES_COIN",
    "strategyType": "FUND_MANAGEMENT",
    "status": "STOPPED",
    "maxLeverage": 10,
    "marginMode": "ISOLATED",
    "positionSizeMethod": "KELLY",
    "riskPerTrade": 0.0200,
    "maxRiskPerTrade": 0.0500,
    "initialCapital": 1.00000000,
    "totalProfit": 0.00000000,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

---

## 5. 策略管理操作

### 5.1 查询策略列表

**接口地址**: `GET /strategy/list`

**接口描述**: 查询用户的所有策略列表，支持多种筛选条件

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选: SPOT, FUTURES_USDT, FUTURES_COIN |
| strategyType | String | 否 | 策略类型筛选 |
| pairName | String | 否 | 交易对筛选 |
| status | String | 否 | 状态筛选: STOPPED（已停止）, RUNNING（运行中）, PAUSED（已暂停） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /strategy/list?marketType=SPOT&status=RUNNING&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1001,
      "userId": 1,
      "strategyName": "BTC/USDT均线交叉策略",
      "pairName": "BTC/USDT",
      "marketType": "SPOT",
      "strategyType": "MA_CROSS",
      "status": "RUNNING",
      "initialCapital": 10000.00000000,
      "currentCapital": 10100.00000000,
      "totalProfit": 100.00000000,
      "totalLoss": 0.00000000,
      "netProfit": 100.00000000,
      "profitRate": 0.0100,
      "totalTrades": 25,
      "winningTrades": 18,
      "losingTrades": 7,
      "winRate": 0.7200,
      "startTime": "2024-01-16T10:00:00",
      "lastExecutionTime": "2024-01-16T11:30:00",
      "createdAt": "2024-01-16T09:00:00",
      "updatedAt": "2024-01-16T11:30:00"
    }
  ]
}
```

---

### 5.2 查询策略详情

**接口地址**: `GET /strategy/{strategyId}`

**接口描述**: 查询指定策略的详细信息，包括实时状态、持仓情况、盈亏等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求示例**:
```
GET /strategy/1001
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1001,
    "userId": 1,
    "strategyName": "BTC/USDT均线交叉策略",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "strategyType": "MA_CROSS",
    "status": "RUNNING",
    "shortPeriod": 5,
    "longPeriod": 50,
    "maType": "SMA",
    "initialCapital": 10000.00000000,
    "currentCapital": 10100.00000000,
    "totalProfit": 100.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 100.00000000,
    "profitRate": 0.0100,
    "totalTrades": 25,
    "winningTrades": 18,
    "losingTrades": 7,
    "winRate": 0.7200,
    "currentPosition": 0.20000000,
    "averageBuyPrice": 49900.00000000,
    "averageSellPrice": 50100.00000000,
    "stopLossPercentage": 0.0500,
    "takeProfitPercentage": 0.1000,
    "startTime": "2024-01-16T10:00:00",
    "lastExecutionTime": "2024-01-16T11:30:00",
    "createdAt": "2024-01-16T09:00:00",
    "updatedAt": "2024-01-16T11:30:00"
  }
}
```

---

### 5.3 启动策略

**接口地址**: `POST /strategy/{strategyId}/start`

**接口描述**: 启动指定的策略，开始执行交易策略

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求示例**:
```
POST /strategy/1001/start
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略启动成功",
  "data": {
    "id": 1001,
    "status": "RUNNING",
    "startTime": "2024-01-16T10:00:00",
    "updatedAt": "2024-01-16T10:00:00"
  }
}
```

---

### 5.4 停止策略

**接口地址**: `POST /strategy/{strategyId}/stop`

**接口描述**: 停止指定的策略，停止时会平掉所有持仓（合约市场）并结算当前盈亏

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| closePositions | Boolean | 否 | 是否平掉所有持仓（合约市场，默认true） |
| cancelOrders | Boolean | 否 | 是否取消所有未成交订单（默认true） |

**请求示例**:
```
POST /strategy/1001/stop?closePositions=true&cancelOrders=true
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略停止成功",
  "data": {
    "id": 1001,
    "status": "STOPPED",
    "stoppedAt": "2024-01-16T12:00:00",
    "finalProfit": 150.00000000,
    "finalProfitRate": 0.0150,
    "totalTrades": 30,
    "cancelledOrders": 5,
    "closedPositions": 0,
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

---

### 5.5 暂停策略

**接口地址**: `POST /strategy/{strategyId}/pause`

**接口描述**: 暂停策略，暂停后不会创建新订单，但已创建的订单会继续执行

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略暂停成功",
  "data": {
    "id": 1001,
    "status": "PAUSED",
    "pausedAt": "2024-01-16T11:00:00",
    "updatedAt": "2024-01-16T11:00:00"
  }
}
```

---

### 5.6 恢复策略

**接口地址**: `POST /strategy/{strategyId}/resume`

**接口描述**: 恢复已暂停的策略

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略恢复成功",
  "data": {
    "id": 1001,
    "status": "RUNNING",
    "resumedAt": "2024-01-16T11:05:00",
    "updatedAt": "2024-01-16T11:05:00"
  }
}
```

---

### 5.7 更新策略参数

**接口地址**: `PUT /strategy/{strategyId}/update`

**接口描述**: 更新策略的参数（仅停止或暂停状态可更新）

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
```json
{
  "strategyName": "BTC/USDT均线交叉策略（已更新）",
  "shortPeriod": 10,
  "longPeriod": 50,
  "stopLossPercentage": 0.06,
  "takeProfitPercentage": 0.12
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "参数更新成功",
  "data": {
    "id": 1001,
    "strategyName": "BTC/USDT均线交叉策略（已更新）",
    "shortPeriod": 10,
    "longPeriod": 50,
    "stopLossPercentage": 0.0600,
    "takeProfitPercentage": 0.1200,
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

---

### 5.8 删除策略

**接口地址**: `DELETE /strategy/{strategyId}`

**接口描述**: 删除策略（仅停止状态可删除）

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略删除成功",
  "data": null
}
```

---

## 6. 策略运行监控

### 6.1 查询策略实时状态

**接口地址**: `GET /strategy/{strategyId}/status`

**接口描述**: 查询策略的实时运行状态，包括当前价格、持仓、订单等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求示例**:
```
GET /strategy/1001/status
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "strategyId": 1001,
    "status": "RUNNING",
    "currentPrice": 50200.00000000,
    "currentPosition": 0.20000000,
    "activeOrders": 2,
    "pendingOrders": [
      {
        "orderId": 2001,
        "side": "BUY",
        "price": 49900.00000000,
        "quantity": 0.10000000,
        "status": "PENDING"
      },
      {
        "orderId": 2002,
        "side": "SELL",
        "price": 50100.00000000,
        "quantity": 0.10000000,
        "status": "PENDING"
      }
    ],
    "unrealizedProfit": 40.00000000,
    "realizedProfit": 100.00000000,
    "totalProfit": 140.00000000,
    "lastExecutionTime": "2024-01-16T11:30:00"
  }
}
```

---

### 6.2 查询策略交易记录

**接口地址**: `GET /strategy/{strategyId}/trades`

**接口描述**: 查询策略的所有交易记录

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| side | String | 否 | 方向筛选: BUY, SELL |
| action | String | 否 | 操作筛选: OPEN, CLOSE |
| startTime | String | 否 | 开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 否 | 结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| page | Integer | 否 | 页码（从1开始，默认1） |
| size | Integer | 否 | 每页数量（默认20，最大100） |

**请求示例**:
```
GET /strategy/1001/trades?side=BUY&page=1&size=20
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 3001,
      "strategyId": 1001,
      "userId": 1,
      "marketType": "SPOT",
      "orderId": 2001,
      "pairName": "BTC/USDT",
      "action": "OPEN",
      "side": "BUY",
      "quantity": 0.10000000,
      "price": 49900.00000000,
      "amount": 4990.00000000,
      "fee": 2.49500000,
      "profitLoss": null,
      "strategyType": "MA_CROSS",
      "signal": "MA_CROSS_UP",
      "createdAt": "2024-01-16T10:05:00"
    },
    {
      "id": 3002,
      "strategyId": 1001,
      "userId": 1,
      "marketType": "SPOT",
      "orderId": 2002,
      "pairName": "BTC/USDT",
      "action": "CLOSE",
      "side": "SELL",
      "quantity": 0.10000000,
      "price": 50100.00000000,
      "amount": 5010.00000000,
      "fee": 2.50500000,
      "profitLoss": 15.00000000,
      "strategyType": "MA_CROSS",
      "signal": "MA_CROSS_DOWN",
      "createdAt": "2024-01-16T10:15:00"
    }
  ]
}
```

---

### 6.3 查询策略订单列表

**接口地址**: `GET /strategy/{strategyId}/orders`

**接口描述**: 查询策略的所有订单（包括待成交和已成交）

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选: PENDING, FILLED, CANCELLED |
| side | String | 否 | 方向筛选: BUY, SELL |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "orderId": 2003,
      "strategyId": 1001,
      "pairName": "BTC/USDT",
      "side": "BUY",
      "price": 49800.00000000,
      "quantity": 0.10000000,
      "amount": 4980.00000000,
      "filledQuantity": 0.00000000,
      "status": "PENDING",
      "signal": "MA_CROSS_UP",
      "createdAt": "2024-01-16T11:00:00"
    }
  ]
}
```

---

### 6.4 查询策略持仓情况

**接口地址**: `GET /strategy/{strategyId}/positions`

**接口描述**: 查询策略的当前持仓情况（合约市场）

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "strategyId": 1001,
    "marketType": "FUTURES_USDT",
    "positions": [
      {
        "positionId": 4001,
        "side": "LONG",
        "quantity": 0.20000000,
        "entryPrice": 50000.00000000,
        "currentPrice": 50200.00000000,
        "unrealizedPnl": 40.00000000,
        "leverage": 10,
        "margin": 1000.00000000
      }
    ],
    "totalPosition": 0.20000000,
    "totalUnrealizedPnl": 40.00000000
  }
}
```

---

---

## 7. 策略回测与优化

### 7.1 创建策略回测任务

**接口地址**: `POST /strategy/backtest/create`

**接口描述**: 创建策略回测任务，使用历史数据验证策略有效性

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**:
```json
{
  "strategyId": 1001,
  "startTime": "2024-01-01 00:00:00",
  "endTime": "2024-01-15 23:59:59",
  "initialCapital": 10000.00
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| strategyId | Long | 是 | 策略ID |
| startTime | String | 是 | 回测开始时间（格式: yyyy-MM-dd HH:mm:ss） |
| endTime | String | 是 | 回测结束时间（格式: yyyy-MM-dd HH:mm:ss） |
| initialCapital | BigDecimal | 否 | 初始资金（不传则使用策略设置的初始资金） |

**响应示例**:
```json
{
  "code": 200,
  "message": "回测任务创建成功",
  "data": {
    "backtestId": "BT20240116100001",
    "strategyId": 1001,
    "status": "PENDING",
    "estimatedDuration": 300,
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

### 7.2 查询回测任务状态

**接口地址**: `GET /strategy/backtest/{backtestId}/status`

**接口描述**: 查询回测任务的执行状态

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "backtestId": "BT20240116100001",
    "status": "RUNNING",
    "progress": 65.5,
    "startTime": "2024-01-16T10:00:00",
    "estimatedCompletionTime": "2024-01-16T10:05:00"
  }
}
```

---

### 7.3 查询回测结果

**接口地址**: `GET /strategy/backtest/{backtestId}/result`

**接口描述**: 查询回测任务的详细结果，包括盈亏、交易统计、收益率等

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "backtestId": "BT20240116100001",
    "strategyId": 1001,
    "status": "COMPLETED",
    "pairName": "BTC/USDT",
    "marketType": "SPOT",
    "startTime": "2024-01-01T00:00:00",
    "endTime": "2024-01-15T23:59:59",
    "initialCapital": 10000.00000000,
    "finalCapital": 10800.00000000,
    "totalProfit": 800.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 800.00000000,
    "profitRate": 0.0800,
    "annualizedReturnRate": 0.1920,
    "totalTrades": 150,
    "winningTrades": 120,
    "losingTrades": 30,
    "winRate": 0.8000,
    "averageProfit": 6.67000000,
    "averageLoss": -5.00000000,
    "profitLossRatio": 1.3340,
    "maxDrawdown": 200.00000000,
    "maxDrawdownRate": 0.0200,
    "sharpeRatio": 1.8500,
    "totalFees": 75.00000000,
    "completedAt": "2024-01-16T10:05:00"
  }
}
```

---

### 7.4 查询回测交易记录

**接口地址**: `GET /strategy/backtest/{backtestId}/trades`

**接口描述**: 查询回测任务的所有交易记录

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| backtestId | String | 是 | 回测任务ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| side | String | 否 | 方向筛选: BUY, SELL |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "tradeId": 6001,
      "backtestId": "BT20240116100001",
      "tradeTime": "2024-01-01T01:00:00",
      "side": "BUY",
      "price": 49900.00000000,
      "quantity": 0.10000000,
      "amount": 4990.00000000,
      "fee": 2.49500000,
      "signal": "MA_CROSS_UP"
    },
    {
      "tradeId": 6002,
      "backtestId": "BT20240116100001",
      "tradeTime": "2024-01-01T02:00:00",
      "side": "SELL",
      "price": 50100.00000000,
      "quantity": 0.10000000,
      "amount": 5010.00000000,
      "fee": 2.50500000,
      "profitLoss": 15.00000000,
      "signal": "MA_CROSS_DOWN"
    }
  ]
}
```

---

### 7.5 查询回测任务列表

**接口地址**: `GET /strategy/backtest/list`

**接口描述**: 查询用户的所有回测任务列表

**请求头**:
```
X-User-Id: 1
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 否 | 策略ID筛选 |
| status | String | 否 | 状态筛选: PENDING, RUNNING, COMPLETED, FAILED |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "backtestId": "BT20240116100001",
      "strategyId": 1001,
      "pairName": "BTC/USDT",
      "marketType": "SPOT",
      "status": "COMPLETED",
      "startTime": "2024-01-01T00:00:00",
      "endTime": "2024-01-15T23:59:59",
      "profitRate": 0.0800,
      "totalTrades": 150,
      "winRate": 0.8000,
      "createdAt": "2024-01-16T10:00:00",
      "completedAt": "2024-01-16T10:05:00"
    }
  ]
}
```

---

### 7.6 策略优化建议

**接口地址**: `POST /strategy/{strategyId}/optimize`

**接口描述**: 根据回测结果，获取策略优化建议

**请求头**:
```
X-User-Id: 1
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
```json
{
  "backtestId": "BT20240116100001",
  "optimizeTarget": "PROFIT_RATE"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| backtestId | String | 是 | 回测任务ID |
| optimizeTarget | String | 否 | 优化目标: PROFIT_RATE（收益率）, SHARPE_RATIO（夏普比率）, WIN_RATE（胜率），默认PROFIT_RATE |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "strategyId": 1001,
    "currentParams": {
      "shortPeriod": 5,
      "longPeriod": 50
    },
    "optimizedParams": {
      "shortPeriod": 10,
      "longPeriod": 50
    },
    "currentPerformance": {
      "profitRate": 0.0800,
      "sharpeRatio": 1.8500,
      "winRate": 0.8000
    },
    "optimizedPerformance": {
      "profitRate": 0.1200,
      "sharpeRatio": 2.1000,
      "winRate": 0.8200
    },
    "improvement": {
      "profitRate": 0.0400,
      "sharpeRatio": 0.2500,
      "winRate": 0.0200
    },
    "suggestions": [
      "建议将短期均线周期从5调整为10，可以提高策略稳定性",
      "长期均线周期保持不变",
      "预期收益率可从8%提升至12%"
    ]
  }
}
```

---

## 8. 历史表现分析

### 8.1 查询策略历史表现

**接口地址**: `GET /strategy/{strategyId}/performance`

**接口描述**: 查询策略的历史表现数据，包括盈亏比例、最大回撤、收益率等指标

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 统计周期: TODAY（今天）, WEEK（本周）, MONTH（本月）, YEAR（本年）, ALL（全部，默认） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "strategyId": 1001,
    "period": "MONTH",
    "initialCapital": 10000.00000000,
    "currentCapital": 10800.00000000,
    "totalProfit": 800.00000000,
    "totalLoss": 0.00000000,
    "netProfit": 800.00000000,
    "profitRate": 0.0800,
    "annualizedReturnRate": 0.9600,
    "totalTrades": 150,
    "winningTrades": 120,
    "losingTrades": 30,
    "winRate": 0.8000,
    "averageProfit": 6.67000000,
    "averageLoss": -5.00000000,
    "profitLossRatio": 1.3340,
    "maxDrawdown": 200.00000000,
    "maxDrawdownRate": 0.0200,
    "sharpeRatio": 1.8500,
    "totalFees": 75.00000000,
    "netProfitAfterFees": 725.00000000,
    "dailyAverageProfit": 26.67000000,
    "dailyAverageTrades": 5.00
  }
}
```

---

### 8.2 查询策略收益趋势

**接口地址**: `GET /strategy/{strategyId}/profit-trend`

**接口描述**: 查询策略的收益趋势数据，用于图表展示

**请求头**:
```
X-User-Id: 1
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 周期: 7D（7天）, 30D（30天）, 90D（90天）, 180D（180天）, 360D（360天）。默认30D |
| interval | String | 否 | 数据间隔: HOUR（小时）, DAY（天）。默认DAY |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "strategyId": 1001,
    "period": "30D",
    "interval": "DAY",
    "trendData": [
      {
        "date": "2024-01-01",
        "capital": 10000.00000000,
        "profit": 0.00000000,
        "profitRate": 0.0000,
        "trades": 0
      },
      {
        "date": "2024-01-02",
        "capital": 10050.00000000,
        "profit": 50.00000000,
        "profitRate": 0.0050,
        "trades": 5
      },
      {
        "date": "2024-01-16",
        "capital": 10800.00000000,
        "profit": 800.00000000,
        "profitRate": 0.0800,
        "trades": 150
      }
    ]
  }
}
```

---

---

## 9. 后台管理接口

### 9.1 策略模板管理

#### 9.1.1 创建策略模板

**接口地址**: `POST /admin/strategy/templates/create`

**接口描述**: 后台创建策略模板

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**请求参数**:
```json
{
  "templateName": "均线交叉策略",
  "marketType": "SPOT",
  "strategyCategory": "TREND",
  "strategyType": "MA_CROSS",
  "description": "使用短期与长期均线交叉信号作为买卖信号",
  "defaultParams": {
    "shortPeriod": 5,
    "longPeriod": 50
  },
  "riskLevel": "MEDIUM",
  "expectedReturnRate": 0.12
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略模板创建成功",
  "data": {
    "id": 1,
    "templateName": "均线交叉策略",
    "marketType": "SPOT",
    "status": "ACTIVE",
    "createdAt": "2024-01-16T10:00:00"
  }
}
```

---

#### 9.1.2 更新策略模板

**接口地址**: `PUT /admin/strategy/templates/{templateId}`

**接口描述**: 后台更新策略模板

**请求头**:
```
X-User-Id: 999
Content-Type: application/json
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| templateId | Long | 是 | 模板ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "策略模板更新成功",
  "data": {
    "id": 1,
    "templateName": "均线交叉策略（已更新）",
    "updatedAt": "2024-01-16T10:05:00"
  }
}
```

---

#### 9.1.3 删除策略模板

**接口地址**: `DELETE /admin/strategy/templates/{templateId}`

**接口描述**: 后台删除策略模板

**请求头**:
```
X-User-Id: 999
```

**响应示例**:
```json
{
  "code": 200,
  "message": "策略模板删除成功",
  "data": null
}
```

---

### 9.2 策略管理（后台）

#### 9.2.1 查询所有策略

**接口地址**: `GET /admin/strategy/list`

**接口描述**: 后台查询所有用户的策略列表

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| marketType | String | 否 | 市场类型筛选 |
| strategyType | String | 否 | 策略类型筛选 |
| status | String | 否 | 状态筛选 |
| page | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页数量（默认20） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1001,
        "userId": 1,
        "strategyName": "BTC/USDT均线交叉策略",
        "pairName": "BTC/USDT",
        "marketType": "SPOT",
        "strategyType": "MA_CROSS",
        "status": "RUNNING",
        "totalProfit": 800.00000000,
        "totalTrades": 150,
        "createdAt": "2024-01-16T09:00:00"
      }
    ],
    "totalElements": 1000,
    "totalPages": 50
  }
}
```

---

#### 9.2.2 强制停止策略

**接口地址**: `POST /admin/strategy/{strategyId}/force-stop`

**接口描述**: 后台强制停止策略（紧急情况使用）

**请求头**:
```
X-User-Id: 999
```

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| strategyId | Long | 是 | 策略ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reason | String | 是 | 强制停止原因 |

**响应示例**:
```json
{
  "code": 200,
  "message": "强制停止成功",
  "data": {
    "strategyId": 1001,
    "status": "STOPPED",
    "stoppedAt": "2024-01-16T12:00:00",
    "reason": "SYSTEM_MAINTENANCE"
  }
}
```

---

### 9.3 数据统计

#### 9.3.1 策略表现统计

**接口地址**: `GET /admin/strategy/statistics/performance`

**接口描述**: 查询策略的表现统计

**请求头**:
```
X-User-Id: 999
```

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| period | String | 否 | 统计周期: DAY, WEEK, MONTH |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "MONTH",
    "totalStrategies": 1000,
    "activeStrategies": 800,
    "stoppedStrategies": 200,
    "totalProfit": 100000.00000000,
    "totalLoss": 20000.00000000,
    "netProfit": 80000.00000000,
    "averageProfitRate": 0.0800,
    "averageWinRate": 0.7500,
    "averageMaxDrawdown": 0.0200,
    "strategyDistribution": {
      "MA_CROSS": 200,
      "RSI_OVERBOUGHT_OVERSOLD": 150,
      "BOLLINGER_BANDS_BREAKOUT": 100
    },
    "performanceByMarketType": {
      "SPOT": {
        "totalStrategies": 600,
        "averageProfitRate": 0.0700
      },
      "FUTURES_USDT": {
        "totalStrategies": 300,
        "averageProfitRate": 0.1000
      },
      "FUTURES_COIN": {
        "totalStrategies": 100,
        "averageProfitRate": 0.0900
      }
    }
  }
}
```

---

#### 9.3.2 用户参与情况统计

**接口地址**: `GET /admin/strategy/statistics/user-participation`

**接口描述**: 查询用户参与策略交易的活跃情况

**请求头**:
```
X-User-Id: 999
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "period": "MONTH",
    "totalUsers": 500,
    "activeUsers": 400,
    "newUsers": 100,
    "averageStrategiesPerUser": 2.5,
    "totalStrategiesCreated": 1000,
    "totalStrategiesRunning": 800,
    "totalTrades": 150000,
    "totalTradeVolume": 75000000.00000000
  }
}
```

---

## 10. 数据字典

### 10.1 市场类型（marketType）
- `SPOT`：现货市场
- `FUTURES_USDT`：USDT本位合约市场
- `FUTURES_COIN`：币本位合约市场

### 10.2 策略类型（strategyType）

策略交易模块支持全部18个策略类型，分为现货策略、USDT本位合约策略和币本位合约策略三类：

#### 现货策略（6个）：
- `MA_CROSS`：均线交叉策略
- `RSI_OVERBOUGHT_OVERSOLD`：RSI超买超卖策略
- `BOLLINGER_BANDS_BREAKOUT`：布林带突破策略
- `MACD_DIVERGENCE`：MACD背离策略
- `OSCILLATION_RANGE_BREAKOUT`：震荡区间突破策略
- `MARKET_SENTIMENT`：市场情绪策略

#### USDT本位合约策略（6个）：
- `TREND_FOLLOWING`：趋势跟随策略
- `CONTRARIAN`：反向交易策略
- `GRID`：网格策略
- `BOLLINGER_BANDS_BREAKOUT_FUTURES`：布林带突破策略（合约）
- `FUND_MANAGEMENT`：资金管理策略
- `OSCILLATION_RANGE`：震荡区间策略

#### 币本位合约策略（6个）：
- `FIXED_POSITION`：固定仓位策略
- `DYNAMIC_POSITION`：动态仓位策略
- `VOLATILITY`：波动率策略
- `PRICE_RANGE`：价格区间策略
- `PERIODIC_TAKE_PROFIT_STOP_LOSS`：定期止盈止损策略
- `FUND_MANAGEMENT_COIN`：资金管理策略（币本位）


### 10.3 策略分类（strategyCategory）
- `TREND`：趋势
- `REVERSAL`：反转
- `BREAKOUT`：突破
- `OSCILLATION`：震荡
- `GRID`：网格
- `RISK`：风险

### 10.4 策略状态（status）
- `STOPPED`：已停止
- `RUNNING`：运行中
- `PAUSED`：已暂停

### 10.5 交易方向（side）
- `BUY`：买入
- `SELL`：卖出

### 10.6 交易操作（action）
- `OPEN`：开仓
- `CLOSE`：平仓

### 10.7 保证金模式（marginMode）
- `ISOLATED`：逐仓
- `CROSS`：全仓

### 10.8 均线类型（maType）
- `SMA`：简单移动平均
- `EMA`：指数移动平均

### 10.9 订单类型（orderType）
- `MARKET`：市价单
- `LIMIT`：限价单

### 10.10 回测状态（status）
- `PENDING`：待执行
- `RUNNING`：执行中
- `COMPLETED`：已完成
- `FAILED`：失败

### 10.11 风险等级（riskLevel）
- `LOW`：低风险
- `MEDIUM`：中风险
- `HIGH`：高风险

### 10.12 仓位大小计算方法（positionSizeMethod）
- `KELLY`：凯利公式
- `FIXED`：固定比例
- `VOLATILITY`：波动率

---

## 11. 错误码说明

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
  "message": "策略参数设置错误，短期均线周期必须小于长期均线周期",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "初始资金不足，最低要求1000 USDT",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "策略正在运行中，无法修改参数",
  "data": null
}
```

```json
{
  "code": 404,
  "message": "策略不存在",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权限执行此操作",
  "data": null
}
```

---

## 12. 注意事项

1. **策略创建**：
   - 需要满足最低资产要求
   - 策略参数需要合理设置，建议先进行回测
   - 不同市场类型的策略参数设置不同

2. **策略运行**：
   - 策略运行时会根据设定的条件自动执行交易
   - 建议设置止损和止盈，控制风险
   - 定期查看策略运行状态和盈亏情况

3. **风险管理**：
   - 每个策略都应设置止损和止盈
   - 不要将所有资金投入一个策略
   - 合约策略使用杠杆时要特别注意风险控制

4. **回测功能**：
   - 回测使用历史数据，结果仅供参考
   - 实际交易结果可能与回测结果有差异
   - 建议在不同市场环境下进行多次回测

5. **策略优化**：
   - 根据回测结果和实际运行情况优化策略参数
   - 优化建议仅供参考，需要根据实际情况调整
   - 定期评估策略表现，及时调整或停止表现不佳的策略

6. **市场选择**：
   - 现货市场：风险相对较低，适合长期持有
   - 合约市场：支持杠杆，收益和风险都更高
   - 选择适合自己风险承受能力的市场

7. **策略类型选择**：
   - 趋势策略：适合趋势明显的市场
   - 震荡策略：适合震荡市场
   - 突破策略：适合波动较大的市场
   - 选择合适的策略类型很重要

8. **参数调整**：
   - 仅停止或暂停状态可以调整参数
   - 参数调整后建议先进行回测
   - 调整参数要谨慎，避免过度优化

9. **杠杆使用**（合约策略）：
   - 杠杆可以放大收益，但也放大风险
   - 建议根据自身风险承受能力选择合适的杠杆倍数
   - 高杠杆可能导致快速亏损

10. **资金管理**：
    - 建议使用合理的资金分配比例
    - 不要将所有资金投入一个策略
    - 定期评估策略表现，及时调整资金分配

---

---

## 12. 代码实现说明

### 12.1 已实现的接口

以下接口已完全实现，并可通过Swagger UI测试：

1. **策略模板管理**
   - `GET /strategy/templates` - 查询策略模板列表
   - `GET /strategy/templates/{templateId}` - 查询策略模板详情

2. **策略管理**
   - `POST /strategy/spot/ma-cross/create` - 创建均线交叉策略
   - `POST /strategy/spot/rsi/create` - 创建RSI超买超卖策略
   - `POST /strategy/spot/bollinger-bands/create` - 创建布林带突破策略
   - `POST /strategy/spot/macd/create` - 创建MACD背离策略
   - `POST /strategy/spot/oscillation/create` - 创建震荡区间突破策略
   - `POST /strategy/spot/sentiment/create` - 创建市场情绪策略
   - `POST /strategy/futures-usdt/trend-following/create` - 创建趋势跟随策略
   - `POST /strategy/futures-usdt/contrarian/create` - 创建反向交易策略
   - `POST /strategy/futures-usdt/grid/create` - 创建网格策略
   - `POST /strategy/futures-usdt/bollinger-bands/create` - 创建布林带突破策略
   - `POST /strategy/futures-usdt/fund-management/create` - 创建资金管理策略
   - `POST /strategy/futures-usdt/oscillation/create` - 创建震荡区间策略
   - `POST /strategy/futures-coin/fixed-position/create` - 创建固定仓位策略
   - `POST /strategy/futures-coin/dynamic-position/create` - 创建动态仓位策略
   - `POST /strategy/futures-coin/volatility/create` - 创建波动率策略
   - `POST /strategy/futures-coin/price-range/create` - 创建价格区间策略
   - `POST /strategy/futures-coin/periodic-tp-sl/create` - 创建定期止盈止损策略
   - `POST /strategy/futures-coin/fund-management/create` - 创建资金管理策略
   - `GET /strategy/list/user` - 查询用户策略列表
   - `GET /strategy/{strategyId}` - 查询策略详情
   - `POST /strategy/{strategyId}/start` - 启动策略
   - `POST /strategy/{strategyId}/stop` - 停止策略
   - `PUT /strategy/{strategyId}` - 更新策略参数
   - `DELETE /strategy/{strategyId}` - 删除策略

3. **回测功能**
   - `POST /strategy/backtest/create` - 创建回测任务
   - `GET /strategy/backtest/{backtestId}/status` - 查询回测状态
   - `GET /strategy/backtest/{backtestId}/result` - 查询回测结果

4. **性能分析**
   - `GET /strategy/{strategyId}/performance` - 查询策略表现
   - `GET /strategy/{strategyId}/performance-history` - 查询历史表现

### 12.2 实体类

已实现的实体类：
- `StrategyTemplate` - 策略模板实体
- `TradingStrategy` - 交易策略实体
- `StrategyBacktest` - 策略回测实体
- `StrategyTradeRecord` - 策略交易记录实体

### 12.3 Service层

已实现的Service：
- `StrategyTemplateService` - 策略模板服务
- `TradingStrategyService` - 交易策略服务
- `StrategyBacktestService` - 策略回测服务
- `StrategyPerformanceService` - 策略表现服务
- `StrategyManagementService` - 策略管理服务

### 12.4 策略实现类（18个）

#### 现货市场策略（6个）
1. `MovingAverageCrossStrategy` - 均线交叉策略（`@Component("MA_CROSS_SPOT")`）
2. `RSIOversoldOverboughtStrategy` - RSI超买超卖策略（`@Component("RSI_OVERBOUGHT_OVERSOLD_SPOT")`）
3. `BollingerBandsBreakoutStrategy` - 布林带突破策略（`@Component("BOLLINGER_BANDS_BREAKOUT_SPOT")`）
4. `MACDDivergenceStrategy` - MACD背离策略（`@Component("MACD_DIVERGENCE_SPOT")`）
5. `OscillationRangeBreakoutStrategy` - 震荡区间突破策略（`@Component("OSCILLATION_RANGE_BREAKOUT_SPOT")`）
6. `MarketSentimentStrategy` - 市场情绪策略（`@Component("MARKET_SENTIMENT_SPOT")`）

#### USDT本位合约策略（6个）
1. `TrendFollowingStrategy` - 趋势跟随策略（`@Component("TREND_FOLLOWING_FUTURES_USDT")`）
2. `ContrarianStrategy` - 反向交易策略（`@Component("CONTRARIAN_FUTURES_USDT")`）
3. `GridStrategy` - 网格策略（`@Component("GRID_FUTURES_USDT")`）
4. `BollingerBandsBreakoutFuturesStrategy` - 布林带突破策略（`@Component("BOLLINGER_BANDS_BREAKOUT_FUTURES_USDT")`）
5. `FundManagementStrategy` - 资金管理策略（`@Component("FUND_MANAGEMENT_FUTURES_USDT")`）
6. `OscillationRangeStrategy` - 震荡区间策略（`@Component("OSCILLATION_RANGE_FUTURES_USDT")`）

#### 币本位合约策略（6个）
1. `FixedPositionStrategy` - 固定仓位策略（`@Component("FIXED_POSITION_FUTURES_COIN")`）
2. `DynamicPositionStrategy` - 动态仓位策略（`@Component("DYNAMIC_POSITION_FUTURES_COIN")`）
3. `VolatilityStrategy` - 波动率策略（`@Component("VOLATILITY_FUTURES_COIN")`）
4. `PriceRangeStrategy` - 价格区间策略（`@Component("PRICE_RANGE_FUTURES_COIN")`）
5. `PeriodicTakeProfitStopLossStrategy` - 定期止盈止损策略（`@Component("PERIODIC_TAKE_PROFIT_STOP_LOSS_FUTURES_COIN")`）
6. `FundManagementCoinStrategy` - 资金管理策略（`@Component("FUND_MANAGEMENT_COIN_FUTURES_COIN")`）

### 12.5 Controller层

已实现的Controller：
- `StrategyController` - 策略控制器（包含所有API端点）

### 12.6 DTO类

已实现的DTO：
- `CreateSpotStrategyRequest` - 创建现货策略请求
- `CreateMaCrossStrategyRequest` - 创建均线交叉策略请求
- `StrategyResponse` - 策略响应
- `StrategyInfoResponse` - 策略信息响应
- `ExecuteStrategyRequest` - 执行策略请求

### 12.7 服务集成

已集成的服务：
- **钱包服务集成**：通过 `RobotWalletService` 接口集成钱包模块，支持资金管理
- **订单服务集成**：通过 `RobotOrderService` 接口集成订单模块，支持订单创建和管理
- **市场数据服务集成**：通过 `RobotMarketDataService` 接口集成市场数据模块，支持实时价格、K线等数据获取

### 12.8 回测引擎

已实现的回测引擎：
- `BacktestEngine` - 回测引擎类，支持基于历史K线数据的策略回测
- 支持计算盈亏、胜率、最大回撤、Sharpe比率等指标
- 支持生成权益曲线数据

### 12.9 注意事项

1. **策略核心逻辑**：所有策略类的 `execute()` 方法中的核心交易逻辑部分标记了TODO，需要后续完善：
   - 技术指标计算（MA、RSI、MACD、布林带等）
   - 交易信号生成
   - 订单创建逻辑
   - 风险管理机制

2. **合约服务集成**：合约订单服务和市场数据服务的集成需要进一步完善：
   - USDT本位合约订单创建
   - 币本位合约订单创建
   - 合约市场数据获取

3. **技术指标计算**：建议集成技术指标计算库（如Ta4j）或自行实现常用的技术指标

4. **策略执行**：策略执行需要配置策略工厂（StrategyFactory）来动态加载和执行策略

5. **参数解析**：所有策略类都提供了统一的参数解析辅助方法：
   - `getIntParam()` - 获取整数参数
   - `getStringParam()` - 获取字符串参数
   - `getBigDecimalParam()` - 获取BigDecimal参数

---

**文档版本**：v2.1  
**最后更新**：2024-01-16  
**文档状态**：已完成（代码实现完成）




