# 现货交易机器人模块 (Spot Trading Bot Module)

## 模块说明

现货交易机器人模块提供订单撮合功能，使前端的订单与深度表流动起来。主要功能包括：

1. **订单撮合**: 在用户下单时立即进行买入/卖出撮合
2. **自主交易**: 在没有用户订单时，机器人按照配置自动生成订单
3. **参数配置**: 为每个交易对配置独立的机器人参数
4. **交易测算**: 根据配置参数进行交易测算，预估交易量和金额
5. **数据存储**: 使用MySQL存储配置，使用MongoDB存储交易数据和历史行情

## 主要特性

### 1. 即时撮合

- 用户下单时（市价单或限价单），机器人立即进行撮合
- 支持大额订单撮合（如1000000 USDT的BTC）
- 撮合成功立即返回结果

### 2. 独立机器人

- 每个现货交易对有一个独立的交易机器人
- 每个机器人有独立的配置参数
- 可以单独启动和停止

### 3. 参数配置

机器人配置参数包括：

- **下单时间间隔** (orderIntervalSeconds): 机器人自主下单的时间间隔（秒）
- **初始订单数量** (initialOrderQuantity): 基础订单数量
- **价格精度** (pricePrecision): 价格的小数位数
- **数量精度** (quantityPrecision): 数量的小数位数
- **差价类型** (priceDiffType): RATIO（比例）或 VALUE（值）
- **最高差价** (maxPriceDiff): 买卖盘最高差价
- **价格变化步涨%** (priceChangeStepPercent): 价格变化步长
- **最低交易量** (minTradeQuantity): 最低交易量限制
- **交易量随机因子** (volumeRandomFactor1-7): 7个随机因子（总和应接近100%）
- **当前价格** (currentPrice): 币种当前价格

### 4. 交易测算

根据机器人参数进行测算，返回：

- **预估数据**: 预估交易币(平均/安全)、预估结算币(平均/安全)
- **卖盘数据**: 最高价、最低价、差价、差价百分比、币种量、金额
- **买盘数据**: 最高价、最低价、差价、差价百分比、币种量、金额
- **盘口数据**: 50条买卖盘数据（25条卖盘 + 25条买盘）

### 5. 数据存储

- **MySQL**: 存储机器人配置数据
- **MongoDB**: 存储机器人交易记录和历史行情数据

## 数据库结构

### MySQL表

1. `spot_trading_bot_configs` - 现货交易机器人配置表

### MongoDB集合

1. `bot_trade_records` - 机器人交易记录集合
   - 存储所有机器人交易数据
   - 包含与用户订单的撮合记录
   - 包含机器人自主生成的订单记录

2. `market_data_history` - 市场数据历史记录集合
   - 存储现货和合约区域的历史行情数据
   - 支持不同周期的K线数据（1m, 5m, 15m, 30m, 1h, 4h, 1d等）

## API接口

详细API文档请参考: `API文档.md`

### 主要接口

- `/spot-bot/config/**` - 机器人配置管理接口
- `/spot-bot/simulate/**` - 机器人测算接口
- `/spot-bot/match` - 订单撮合接口
- `/spot-bot/generate/{pairName}` - 生成机器人订单
- `/spot-bot/start/{pairName}` - 启动机器人
- `/spot-bot/stop/{pairName}` - 停止机器人

## 使用说明

### 1. 配置MongoDB

在`application.yml`中配置MongoDB连接：

```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: crypto_exchange
```

### 2. 初始化数据库

```sql
-- 执行表结构脚本
source database/spot_trading_bot_schema.sql;

-- 执行测试数据脚本（可选）
source database/spot_trading_bot_test_data.sql;
```

### 3. 创建机器人配置

```bash
POST /spot-bot/config/create
{
  "pairName": "BTC/USDT",
  "baseCurrency": "BTC",
  "quoteCurrency": "USDT",
  "orderIntervalSeconds": 5,
  "initialOrderQuantity": 0.01,
  ...
}
```

### 4. 进行测算

```bash
POST /spot-bot/simulate
{
  "pairName": "BTC/USDT",
  ...
}
```

### 5. 启动机器人

```bash
POST /spot-bot/start/BTC/USDT
```

### 6. 用户下单撮合

```bash
POST /spot-bot/match
{
  "pairName": "BTC/USDT",
  "side": "BUY",
  "orderType": "MARKET",
  "quantity": 20.0,
  "userId": 1,
  "userOrderId": "ORDER123456"
}
```

## 工作原理

### 用户订单撮合

1. 用户提交订单（市价单或限价单）
2. 系统调用撮合接口
3. 机器人根据当前价格和配置参数生成对应的撮合订单
4. 撮合成功，记录交易数据到MongoDB
5. 更新用户订单状态

### 机器人自主交易

1. 机器人启动后，按照配置的时间间隔运行
2. 随机生成买卖方向
3. 根据配置参数生成合理的价格和数量
4. 创建交易记录并保存到MongoDB
5. 更新深度表和订单簿

## 注意事项

1. **数据分离**: 用户交易数据存储在MySQL，机器人交易数据存储在MongoDB
2. **数据量**: 机器人交易数据量可能很大，使用MongoDB可以更好地处理
3. **实时性**: 用户下单时立即撮合，保证交易的实时性
4. **独立性**: 每个交易对都有独立的机器人，互不影响
5. **参数验证**: 交易量随机因子7个值的总和应接近100%
6. **价格精度**: 价格和数量精度需要根据交易对的实际情况设置

## 扩展说明

### 添加新的交易对机器人

1. 通过配置接口创建新的机器人配置
2. 设置合适的参数
3. 启动机器人

### 调整机器人参数

1. 通过更新配置接口修改参数
2. 可以先进行测算，查看效果
3. 确认后保存配置

### 查看交易数据

1. 通过MongoDB查询`bot_trade_records`集合
2. 可以按交易对、时间范围等条件查询
3. 支持分页查询

## 技术栈

- Spring Boot
- Spring Data JPA (MySQL)
- Spring Data MongoDB
- Redis (可选，用于缓存)
- Swagger (API文档)














