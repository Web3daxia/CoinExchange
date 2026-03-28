# 现货交易机器人模块 API 文档

## 模块说明
现货交易机器人模块提供订单撮合功能，在用户下单时或没有用户下单时进行现货区域的订单撮合，使前端的订单与深度表流动起来。每个现货交易对有独立的交易机器人。

## Base URL
```
/spot-bot
```

## 认证
所有接口需要用户认证，请在请求头中携带：
```
Authorization: Bearer {token}
```

---

## 1. 机器人配置管理接口

### 1.1 创建机器人配置

**接口地址**: `POST /spot-bot/config/create`

**接口描述**: 为指定交易对创建交易机器人配置

**请求体**:
```json
{
  "pairName": "BTC/USDT",
  "baseCurrency": "BTC",
  "quoteCurrency": "USDT",
  "orderIntervalSeconds": 5,
  "initialOrderQuantity": 0.01,
  "pricePrecision": 2,
  "quantityPrecision": 8,
  "priceDiffType": "RATIO",
  "maxPriceDiff": 0.50,
  "priceChangeStepPercent": 0.10,
  "minTradeQuantity": 0.0001,
  "volumeRandomFactor1": 1.00,
  "volumeRandomFactor2": 9.00,
  "volumeRandomFactor3": 20.00,
  "volumeRandomFactor4": 20.00,
  "volumeRandomFactor5": 20.00,
  "volumeRandomFactor6": 20.00,
  "volumeRandomFactor7": 10.00,
  "currentPrice": 50000.00
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称，如: BTC/USDT |
| baseCurrency | String | 是 | 基础货币，如: BTC |
| quoteCurrency | String | 是 | 计价货币，如: USDT |
| orderIntervalSeconds | Integer | 是 | 下单时间间隔（秒） |
| initialOrderQuantity | BigDecimal | 是 | 初始订单数量 |
| pricePrecision | Integer | 是 | 价格精度要求（小数位数） |
| quantityPrecision | Integer | 是 | 数量精度要求（小数位数） |
| priceDiffType | String | 是 | 差价类型: RATIO（比例）, VALUE（值） |
| maxPriceDiff | BigDecimal | 是 | 买卖盘最高差价 |
| priceChangeStepPercent | BigDecimal | 否 | 价格变化步涨%（比例） |
| minTradeQuantity | BigDecimal | 是 | 最低交易量 |
| volumeRandomFactor1-7 | BigDecimal | 否 | 交易量随机因子%（7个因子，总和应为100%） |
| currentPrice | BigDecimal | 否 | 币种当前价格 |

**响应示例**:
```json
{
  "code": 200,
  "message": "机器人配置创建成功",
  "data": {
    "id": 1,
    "pairName": "BTC/USDT",
    "baseCurrency": "BTC",
    "quoteCurrency": "USDT",
    "orderIntervalSeconds": 5,
    "initialOrderQuantity": 0.01,
    "pricePrecision": 2,
    "quantityPrecision": 8,
    "priceDiffType": "RATIO",
    "maxPriceDiff": 0.50,
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 1.2 更新机器人配置

**接口地址**: `POST /spot-bot/config/update/{configId}`

**接口描述**: 更新机器人配置参数

### 1.3 删除机器人配置

**接口地址**: `DELETE /spot-bot/config/{configId}`

### 1.4 查询机器人配置列表

**接口地址**: `GET /spot-bot/config/list`

### 1.5 查询机器人配置详情

**接口地址**: `GET /spot-bot/config/{configId}`

### 1.6 根据交易对查询配置

**接口地址**: `GET /spot-bot/config/pair/{pairName}`

---

## 2. 机器人测算接口

### 2.1 机器人参数测算

**接口地址**: `POST /spot-bot/simulate`

**接口描述**: 根据输入的机器人参数进行测算，返回预估数据和盘口数据（50条）

**请求体**: 同创建机器人配置的请求体

**响应示例**:
```json
{
  "code": 200,
  "message": "测算完成",
  "data": {
    "estimatedTradeCoinAverage": 0.5,
    "estimatedTradeCoinSafe": 0.45,
    "estimatedSettlementCoinAverage": 25000.00,
    "estimatedSettlementCoinSafe": 22500.00,
    "sellMaxPrice": 50250.00,
    "sellMinPrice": 50000.00,
    "sellPriceDiff": 250.00,
    "sellPriceDiffPercent": 0.50,
    "sellCoinQuantity": 0.25,
    "sellAmount": 12531.25,
    "buyMaxPrice": 50000.00,
    "buyMinPrice": 49750.00,
    "buyPriceDiff": 250.00,
    "buyPriceDiffPercent": 0.50,
    "buyCoinQuantity": 0.25,
    "buyAmount": 12468.75,
    "orderBookItems": [
      {
        "side": "SELL",
        "price": 50010.00,
        "quantity": 0.01,
        "amount": 500.10,
        "index": 1
      },
      {
        "side": "BUY",
        "price": 49990.00,
        "quantity": 0.01,
        "amount": 499.90,
        "index": 1
      }
      // ... 共50条数据（25条卖盘 + 25条买盘）
    ]
  }
}
```

**响应字段说明**:
| 字段名 | 类型 | 说明 |
|--------|------|------|
| estimatedTradeCoinAverage | BigDecimal | 预估交易币(平均) |
| estimatedTradeCoinSafe | BigDecimal | 预估交易币(安全) |
| estimatedSettlementCoinAverage | BigDecimal | 预估结算币(平均) |
| estimatedSettlementCoinSafe | BigDecimal | 预估结算币(安全) |
| sellMaxPrice | BigDecimal | 卖盘最高 |
| sellMinPrice | BigDecimal | 卖盘最低 |
| sellPriceDiff | BigDecimal | 卖盘差价 |
| sellPriceDiffPercent | BigDecimal | 差价百分比 |
| sellCoinQuantity | BigDecimal | 卖盘币种量(BTC) |
| sellAmount | BigDecimal | 卖盘金额(USDT) |
| buyMaxPrice | BigDecimal | 买盘最高 |
| buyMinPrice | BigDecimal | 买盘最低 |
| buyPriceDiff | BigDecimal | 买盘差价 |
| buyPriceDiffPercent | BigDecimal | 差价百分比 |
| buyCoinQuantity | BigDecimal | 买盘币种量(BTC) |
| buyAmount | BigDecimal | 买盘金额(USDT) |
| orderBookItems | List | 盘口数据（50条） |

### 2.2 根据配置ID进行测算

**接口地址**: `GET /spot-bot/simulate/{configId}`

**接口描述**: 根据已保存的机器人配置进行测算

---

## 3. 订单撮合接口

### 3.1 撮合用户订单

**接口地址**: `POST /spot-bot/match`

**接口描述**: 用户下单时立即进行撮合，买入或卖出操作。例如：用户下单1000000 USDT的BTC，以市价模式，机器人会马上为订单实现撮合，买入成功。

**请求体**:
```json
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "orderType": "MARKET",
  "quantity": 20.0,
  "userId": 1,
  "userOrderId": "ORDER123456"
}
```

**参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| side | String | 是 | 订单方向: BUY, SELL |
| orderType | String | 是 | 订单类型: MARKET, LIMIT |
| price | BigDecimal | 否 | 限价单价格（限价单必填） |
| quantity | BigDecimal | 是 | 订单数量 |
| userId | Long | 否 | 用户ID |
| userOrderId | String | 否 | 用户订单ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "success": true,
    "botOrderId": "BOT123456",
    "userOrderId": "ORDER123456",
    "matchedPrice": 50000.00,
    "matchedQuantity": 20.0,
    "matchedAmount": 1000000.00,
    "matchedTime": "2024-01-01T10:00:00",
    "message": "订单撮合成功"
  }
}
```

### 3.2 生成机器人订单

**接口地址**: `POST /spot-bot/generate/{pairName}`

**接口描述**: 无用户订单时生成机器人订单

### 3.3 启动机器人

**接口地址**: `POST /spot-bot/start/{pairName}`

**接口描述**: 启动指定交易对的交易机器人，机器人会按照配置的时间间隔自动生成订单

### 3.4 停止机器人

**接口地址**: `POST /spot-bot/stop/{pairName}`

**接口描述**: 停止指定交易对的交易机器人

---

## 数据库说明

### MySQL表（配置数据）

- `spot_trading_bot_configs` - 现货交易机器人配置表

### MongoDB集合（交易数据和历史数据）

- `bot_trade_records` - 机器人交易记录集合
  - 存储机器人的所有交易数据
  - 包含与用户订单的撮合记录
  - 包含机器人自主生成的订单记录

- `market_data_history` - 市场数据历史记录集合
  - 存储现货和合约区域的历史行情数据
  - 支持不同周期的K线数据
  - 用于历史数据查询和分析

---

## 机器人工作原理

### 1. 用户下单撮合流程

1. 用户提交订单（市价单或限价单）
2. 系统调用撮合接口
3. 机器人根据配置参数立即生成对应的撮合订单
4. 撮合成功，记录交易数据到MongoDB
5. 更新用户订单状态

### 2. 机器人自主交易流程

1. 机器人启动后，按照配置的`orderIntervalSeconds`时间间隔运行
2. 随机生成买卖方向（BUY或SELL）
3. 根据当前价格和配置参数生成合理的价格和数量
4. 创建交易记录并保存到MongoDB
5. 更新深度表和订单簿

### 3. 价格和数量生成规则

- **价格生成**: 根据当前价格和`maxPriceDiff`参数，在合理范围内随机生成
- **数量生成**: 根据`initialOrderQuantity`和7个随机因子，按比例生成数量
- **精度控制**: 严格按照`pricePrecision`和`quantityPrecision`进行精度控制

---

## 注意事项

1. **数据存储**: 用户交易数据存储在MySQL中，机器人交易数据存储在MongoDB中
2. **数据量**: 机器人交易数据量可能很大，使用MongoDB可以更好地处理大数据量
3. **实时性**: 用户下单时立即撮合，保证交易的实时性
4. **独立性**: 每个交易对都有独立的机器人，互不影响
5. **配置参数**: 交易量随机因子7个值的总和应接近100%
6. **价格精度**: 价格和数量精度需要根据交易对的实际情况设置

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |














