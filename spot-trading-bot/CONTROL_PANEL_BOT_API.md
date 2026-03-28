# 控盘机器人模块API文档

## 概述

控盘机器人模块允许用户自定义币种的行情与K线数据，可以设置起始价格、机器人下单参数以及二十四小时的基础信息（最高、最低、交易额、交易量），并绘制K线图查看效果。

## 基础信息

- **基础路径**: `/api/control-panel-bot`
- **认证方式**: Bearer Token (JWT)

## API接口

### 1. 配置管理

#### 1.1 创建控盘机器人配置

**POST** `/api/control-panel-bot/configs`

创建新的控盘机器人配置。

**请求体**:
```json
{
  "pairName": "BTC/USDT",
  "baseCurrency": "BTC",
  "quoteCurrency": "USDT",
  "initialPrice": 50000.00,
  "pricePrecision": 8,
  "quantityPrecision": 8,
  "orderIntervalSeconds": 5,
  "initialOrderQuantity": 0.01,
  "priceDiffType": "RATIO",
  "maxPriceDiff": 0.5,
  "priceChangeStepPercent": 0.1,
  "minTradeQuantity": 0.0001,
  "volumeRandomFactor1": 1.00,
  "volumeRandomFactor2": 9.00,
  "volumeRandomFactor3": 20.00,
  "volumeRandomFactor4": 20.00,
  "volumeRandomFactor5": 20.00,
  "volumeRandomFactor6": 20.00,
  "volumeRandomFactor7": 10.00,
  "dailyHigh": 51000.00,
  "dailyLow": 49000.00,
  "dailyVolume": 1000.00,
  "dailyAmount": 50000000.00,
  "status": "ACTIVE"
}
```

**响应**: 返回创建的配置对象

#### 1.2 更新控盘机器人配置

**PUT** `/api/control-panel-bot/configs/{id}`

更新控盘机器人配置信息。

**路径参数**:
- `id`: 配置ID

**请求体**: 同创建接口

#### 1.3 获取控盘机器人配置

**GET** `/api/control-panel-bot/configs/{id}`

根据ID获取控盘机器人配置。

**路径参数**:
- `id`: 配置ID

#### 1.4 根据交易对获取配置

**GET** `/api/control-panel-bot/configs/pair/{pairName}`

根据交易对名称获取控盘机器人配置。

**路径参数**:
- `pairName`: 交易对名称，如: BTC/USDT

#### 1.5 获取所有配置

**GET** `/api/control-panel-bot/configs`

获取所有控盘机器人配置列表。

#### 1.6 删除配置

**DELETE** `/api/control-panel-bot/configs/{id}`

根据ID删除控盘机器人配置。

**路径参数**:
- `id`: 配置ID

#### 1.7 更新配置状态

**PUT** `/api/control-panel-bot/configs/{id}/status`

启用或禁用控盘机器人配置。

**路径参数**:
- `id`: 配置ID

**查询参数**:
- `status`: 状态值，ACTIVE 或 INACTIVE

### 2. K线数据管理

#### 2.1 创建K线数据

**POST** `/api/control-panel-bot/kline`

创建单条K线数据。

**请求体**:
```json
{
  "pairName": "BTC/USDT",
  "interval": "5m",
  "openTime": "2024-01-01T00:00:00",
  "closeTime": "2024-01-01T00:05:00",
  "open": 50000.00,
  "high": 51000.00,
  "low": 49000.00,
  "close": 50500.00,
  "volume": 100.00,
  "amount": 5000000.00
}
```

**字段说明**:
- `interval`: K线周期，支持: 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）
- K线的时段与行情时段相同，即K线数据的开盘时间和收盘时间对应实际的行情时间段

#### 2.2 批量创建K线数据

**POST** `/api/control-panel-bot/kline/batch`

批量创建K线数据。

**请求体**: K线数据对象数组

#### 2.3 更新K线数据

**PUT** `/api/control-panel-bot/kline/{id}`

更新K线数据。

**路径参数**:
- `id`: K线数据ID

**请求体**: 同创建接口

#### 2.4 获取K线数据

**GET** `/api/control-panel-bot/kline/{id}`

根据ID获取K线数据。

**路径参数**:
- `id`: K线数据ID

#### 2.5 获取K线数据列表

**GET** `/api/control-panel-bot/kline/pair/{pairName}/interval/{interval}`

根据交易对名称和K线周期获取K线数据列表。

**路径参数**:
- `pairName`: 交易对名称
- `interval`: K线周期

#### 2.6 获取时间范围内的K线数据

**GET** `/api/control-panel-bot/kline/pair/{pairName}/interval/{interval}/range`

根据交易对名称、K线周期和时间范围获取K线数据。

**路径参数**:
- `pairName`: 交易对名称
- `interval`: K线周期（5m, 15m, 30m, 1h, 4h, 1d, 1w）

**查询参数**:
- `startTime`: 开始时间（格式: 2024-01-01T00:00:00）
- `endTime`: 结束时间（格式: 2024-01-01T23:59:59）

#### 2.7 删除K线数据

**DELETE** `/api/control-panel-bot/kline/{id}`

根据ID删除K线数据。

**路径参数**:
- `id`: K线数据ID

#### 2.8 删除交易对的所有K线数据

**DELETE** `/api/control-panel-bot/kline/pair/{pairName}`

删除指定交易对的所有K线数据。

**路径参数**:
- `pairName`: 交易对名称

## 使用说明

1. **创建配置**: 首先创建控盘机器人配置，设置起始价格、下单参数和二十四小时基础信息
2. **绘制K线**: 通过K线数据接口创建K线数据，可以单条创建或批量创建
3. **查看效果**: 通过查询接口获取K线数据，可以用于绘制K线图查看效果
4. **更新数据**: 可以随时更新配置和K线数据

## 注意事项

- K线数据存储在MongoDB中，配置数据存储在MySQL中
- 控盘机器人的行情和K线需要用户手动输入和描绘
- **K线周期最短为5分钟（5m），最长为1周（1w）**，支持: 5m, 15m, 30m, 1h, 4h, 1d, 1w
- **K线的时段与行情时段相同**，即K线数据的开盘时间和收盘时间必须对应实际的行情时间段
- 时间格式使用ISO 8601标准格式（yyyy-MM-ddTHH:mm:ss）
- 交易量随机因子总和应为100%

