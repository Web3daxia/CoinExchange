# U本位合约模块API文档

## 基础信息

- **模块名称**：U本位合约模块（USDT-margined Futures）
- **用户端基础路径**：`/api/futures`
- **后台管理基础路径**：`/admin/futures-usdt`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 市场数据

#### 1.1 获取市场数据

**接口地址**：`GET /api/futures/market/{pair}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pair | String | 是 | 交易对名称，如BTC/USDT |

**请求示例**：
```
GET /api/futures/market/BTC%2FUSDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pairName": "BTC/USDT",
    "currentPrice": "50000.00000000",
    "markPrice": "50001.00000000",
    "indexPrice": "50000.50000000",
    "fundingRate": "0.00010000",
    "nextFundingTime": "2024-01-01T12:00:00",
    "openInterest": "1000000.00000000",
    "volume24h": "50000000.00000000",
    "priceChange24h": "2.50000000",
    "priceChangePercent24h": "2.50",
    "high24h": "51000.00000000",
    "low24h": "49000.00000000"
  }
}
```

#### 1.2 获取K线数据

**接口地址**：`GET /api/futures/market/{pair}/chart`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interval | String | 否 | 时间粒度：1m, 5m, 15m, 30m, 1h, 4h, 1d, 1w（默认1h） |
| limit | Integer | 否 | 数据条数（默认100，最大1000） |

**请求示例**：
```
GET /api/futures/market/BTC%2FUSDT/chart?interval=1h&limit=100
```

#### 1.3 获取市场深度

**接口地址**：`GET /api/futures/market/{pair}/depth`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 深度档位（默认20，最大100） |

---

### 2. 订单管理

#### 2.1 提交订单（通用接口）

**接口地址**：`POST /api/futures/order`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "orderType": "LIMIT",
  "side": "BUY",
  "positionSide": "OPEN_LONG",
  "price": "50000.00000000",
  "quantity": "0.10000000",
  "leverage": 10,
  "timeInForce": "GTC",
  "reduceOnly": false
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 是 | 交易对名称 |
| orderType | String | 是 | 订单类型：MARKET（市价）, LIMIT（限价）, STOP_LOSS（止损）, TAKE_PROFIT（止盈）, STOP_LIMIT（止损限价）, CONDITIONAL（条件单） |
| side | String | 是 | 买卖方向：BUY（买入）, SELL（卖出） |
| positionSide | String | 是 | 仓位方向：OPEN_LONG（开多）, OPEN_SHORT（开空）, CLOSE_LONG（平多）, CLOSE_SHORT（平空） |
| price | String | 条件 | 委托价格（限价单必填） |
| quantity | String | 是 | 委托数量 |
| leverage | Integer | 是 | 杠杆倍数 |
| timeInForce | String | 否 | 有效期：GTC（一直有效）, IOC（立即成交或取消）, FOK（全部成交或取消），默认GTC |
| reduceOnly | Boolean | 否 | 只减仓（默认false） |
| triggerPrice | String | 条件 | 触发价格（止损/止盈/条件单必填） |
| stopPrice | String | 条件 | 止损价格（止损限价单必填） |

**响应示例**：
```json
{
  "code": 200,
  "message": "订单提交成功",
  "data": {
    "orderId": 123456,
    "orderNo": "FO-20240101120000-123456",
    "pairName": "BTC/USDT",
    "orderType": "LIMIT",
    "side": "BUY",
    "positionSide": "OPEN_LONG",
    "quantity": "0.10000000",
    "price": "50000.00000000",
    "leverage": 10,
    "status": "PENDING",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 提交市价单

**接口地址**：`POST /api/futures/order/market`

#### 2.3 提交限价单

**接口地址**：`POST /api/futures/order/limit`

#### 2.4 提交止损单

**接口地址**：`POST /api/futures/order/stop-loss`

#### 2.5 提交止盈单

**接口地址**：`POST /api/futures/order/take-profit`

#### 2.6 提交止损限价单

**接口地址**：`POST /api/futures/order/stop-limit`

#### 2.7 提交条件单

**接口地址**：`POST /api/futures/order/conditional`

#### 2.8 取消订单

**接口地址**：`POST /api/futures/order/cancel`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

#### 2.9 批量取消订单

**接口地址**：`POST /api/futures/order/cancel-batch`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "orderIds": [123456, 123457]
}
```

#### 2.10 查询订单状态

**接口地址**：`GET /api/futures/order/status`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | Long | 是 | 订单ID |

#### 2.11 查询当前订单

**接口地址**：`GET /api/futures/order/current`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 2.12 查询订单历史

**接口地址**：`GET /api/futures/order/history`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |
| orderType | String | 否 | 订单类型筛选 |
| status | String | 否 | 状态筛选：PENDING, FILLED, CANCELLED, REJECTED |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

### 3. 高级订单

#### 3.1 提交高级限价单

**接口地址**：`POST /api/futures/order/advanced-limit`

#### 3.2 提交追踪委托

**接口地址**：`POST /api/futures/order/trailing`

#### 3.3 提交追逐限价单

**接口地址**：`POST /api/futures/order/trailing-limit`

#### 3.4 提交冰山策略

**接口地址**：`POST /api/futures/order/iceberg`

#### 3.5 提交分段委托

**接口地址**：`POST /api/futures/order/segmented`

#### 3.6 提交分时委托

**接口地址**：`POST /api/futures/order/time-weighted`

#### 3.7 查询高级订单列表

**接口地址**：`GET /api/futures/order/advanced/list`

#### 3.8 取消高级订单

**接口地址**：`POST /api/futures/order/advanced/cancel`

---

### 4. 仓位管理

#### 4.1 查询仓位列表

**接口地址**：`GET /api/futures/account/positions`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pairName | String | 否 | 交易对筛选 |

**请求示例**：
```
GET /api/futures/account/positions?pairName=BTC%2FUSDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "positionSide": "LONG",
      "quantity": "0.10000000",
      "entryPrice": "50000.00000000",
      "markPrice": "51000.00000000",
      "leverage": 10,
      "margin": "500.00000000",
      "unrealizedProfit": "100.00000000",
      "unrealizedProfitPercent": "20.00",
      "liquidationPrice": "45000.00000000",
      "marginMode": "ISOLATED"
    }
  ]
}
```

#### 4.2 查询账户信息

**接口地址**：`GET /api/futures/account/info`

**请求头**：
```
X-User-Id: 1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "availableBalance": "5000.00000000",
    "marginBalance": "5500.00000000",
    "unrealizedProfit": "100.00000000",
    "totalMargin": "500.00000000",
    "marginMode": "ISOLATED",
    "positionMode": "HEDGE"
  }
}
```

#### 4.3 调整杠杆

**接口地址**：`POST /api/futures/account/adjust-leverage`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "leverage": 20
}
```

#### 4.4 设置保证金模式

**接口地址**：`POST /api/futures/account/set-margin-mode`

**请求参数**：
```json
{
  "marginMode": "ISOLATED"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marginMode | String | 是 | 保证金模式：ISOLATED（逐仓）, CROSSED（全仓） |

#### 4.5 设置持仓模式

**接口地址**：`POST /api/futures/account/set-position-mode`

**请求参数**：
```json
{
  "positionMode": "HEDGE"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionMode | String | 是 | 持仓模式：ONE_WAY（单向）, HEDGE（双向） |

---

## 后台管理API

### 1. 交易对管理

#### 1.1 创建交易对

**接口地址**：`POST /admin/futures-usdt/trading-pair/create`

**请求参数**：详见系统管理模块API文档中的交易对管理部分

#### 1.2 更新交易对

**接口地址**：`POST /admin/futures-usdt/trading-pair/update/{id}`

#### 1.3 删除交易对

**接口地址**：`DELETE /admin/futures-usdt/trading-pair/{id}`

#### 1.4 查询交易对列表

**接口地址**：`GET /admin/futures-usdt/trading-pair/list`

#### 1.5 查询交易对详情

**接口地址**：`GET /admin/futures-usdt/trading-pair/{id}`

---

## 订单类型说明

### 基础订单类型
- **MARKET（市价单）**：按市场价格立即成交
- **LIMIT（限价单）**：指定价格下单
- **STOP_LOSS（止损单）**：价格达到触发价时按市价成交
- **TAKE_PROFIT（止盈单）**：价格达到目标价时按市价成交
- **STOP_LIMIT（止损限价单）**：价格达到触发价时按限价成交
- **CONDITIONAL（条件单）**：满足条件时触发下单

### 高级订单类型
- **ADVANCED_LIMIT（高级限价单）**：支持GTC/IOC/FOK
- **TRAILING（追踪委托）**：随价格变动自动调整
- **ICEBERG（冰山订单）**：大单分批显示
- **SEGMENTED（分段委托）**：按价格区间分段下单
- **TIME_WEIGHTED（分时委托）**：按时间加权平均价格执行

---

## 核心交易规则

### 手续费计算

**公式**：
```
手续费 = 手续费率 × (成交张数 × 合约乘数 × 合约面值 × 成交价格)
```

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1，成交价格50000 USDT
- 成交张数：10张
- Taker手续费率：0.05%
- 手续费 = 0.0005 × (10 × 1 × 1 × 50000) = 2.5 USDT

---

### 保证金计算

#### 全仓模式

账户里所有的可用资金都视作可用保证金。

**USDT本位合约 - 全仓初始保证金**：
```
初始保证金 = 面值 × |张数| × 合约乘数 × 标记价格 / 杠杆倍数
```

**特点**：初始保证金将随交易币种价格变化而变动。

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1
- 开仓张数：10张
- 标记价格：50000 USDT
- 杠杆倍数：10倍
- 初始保证金 = 1 × 10 × 1 × 50000 / 10 = 50000 USDT

#### 逐仓模式

各个仓位单独核算保证金，盈亏互不影响。

**USDT本位合约 - 逐仓开仓保证金**：
```
开仓保证金 = 面值 × |张数| × 合约乘数 × 开仓均价 / 杠杆倍数
```

**特点**：初始保证金固定不变。

```
保证金余额 = 开仓保证金 + 手动追加（或减少）的保证金
```

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1
- 开仓张数：10张
- 开仓均价：50000 USDT
- 杠杆倍数：10倍
- 开仓保证金 = 1 × 10 × 1 × 50000 / 10 = 50000 USDT（固定）

---

### 维持保证金计算

**USDT保证金合约**：
```
维持保证金 = 面值 × |张数| × 合约乘数 × 维持保证金率 × 标记价格
```

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1
- 持仓张数：10张
- 维持保证金率：0.5%
- 标记价格：50000 USDT
- 维持保证金 = 1 × 10 × 1 × 0.005 × 50000 = 2500 USDT

**触发强平条件**：当保证金率低于维持保证金率+平仓手续费率时，即触发强平或强制部分减仓。

---

### 保证金率计算

#### 全仓模式

**现货和合约保证金 - 全仓**：
```
保证金率 = (该币种全仓余额 + 全仓收益 - 该币种挂单卖出数量 - 期权买单所需要的该币种数量 - 逐仓开仓所需要的该币种数量 - 所有挂单手续费) / (维持保证金 + 爆仓手续费)
```

#### 逐仓模式

**USDT保证金合约 - 逐仓**：
```
保证金率 = (保证金余额 + 收益) / (面值 × |张数| × 标记价格 × (维持保证金率 + 手续费率))
```

**示例**：
- 保证金余额：50000 USDT
- 收益：5000 USDT
- 面值：1 USDT，张数：10张，标记价格：50000 USDT
- 维持保证金率：0.5%，手续费率：0.05%
- 保证金率 = (50000 + 5000) / (1 × 10 × 50000 × (0.005 + 0.0005)) = 55000 / 2750 = 20（2000%）

---

### 收益计算

#### 多仓收益

**USDT保证金合约**：
```
多仓收益 = 面值 × |张数| × 合约乘数 × (标记价格 - 开仓均价)
```

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1
- 持仓张数：10张
- 开仓均价：50000 USDT
- 标记价格：51000 USDT
- 多仓收益 = 1 × 10 × 1 × (51000 - 50000) = 10000 USDT

#### 空仓收益

**USDT保证金合约**：
```
空仓收益 = 面值 × |张数| × 合约乘数 × (开仓均价 - 标记价格)
```

**示例**：
- 合约：BTC/USDT，面值1 USDT，合约乘数1
- 持仓张数：10张
- 开仓均价：50000 USDT
- 标记价格：49000 USDT
- 空仓收益 = 1 × 10 × 1 × (50000 - 49000) = 10000 USDT

#### 收益率

```
收益率 = 收益 / 开仓保证金
```

---

### 预估强平价计算

**单币种/跨币种/投资组合保证金模式 - 逐仓**：

#### 多仓预估强平价

**USDT保证金合约**：
```
多仓预估强平价 = (保证金余额 - 面值 × |张数| × 开仓均价) / (面值 × |张数| × (维持保证金率 + 手续费率 - 1))
```

**示例**：
- 保证金余额：50000 USDT
- 面值：1 USDT，张数：10张，开仓均价：50000 USDT
- 维持保证金率：0.5%，手续费率：0.05%
- 多仓预估强平价 = (50000 - 1 × 10 × 50000) / (1 × 10 × (0.005 + 0.0005 - 1)) = -450000 / -9.945 = 45248.36 USDT

#### 空仓预估强平价

**USDT保证金合约**：
```
空仓预估强平价 = (保证金余额 + 面值 × |张数| × 开仓均价) / (面值 × |张数| × (维持保证金率 + 手续费率 + 1))
```

**示例**：
- 保证金余额：50000 USDT
- 面值：1 USDT，张数：10张，开仓均价：50000 USDT
- 维持保证金率：0.5%，手续费率：0.05%
- 空仓预估强平价 = (50000 + 1 × 10 × 50000) / (1 × 10 × (0.005 + 0.0005 + 1)) = 550000 / 10.055 = 54649.43 USDT

---

### 资金费用

- **收取频率**：每8小时收取一次
- **收取时间**：8:00、16:00、24:00（UTC时间）
- **记录表**：`funding_fee_records`
- **计算方式**：根据资金费率、持仓张数、标记价格计算

**资金费用计算公式**（USDT本位合约）：
```
资金费用 = 面值 × |张数| × 合约乘数 × 资金费率 × 标记价格
```

---

## 保证金模式说明

- **ISOLATED（逐仓）**：每个仓位独立保证金，盈亏互不影响
- **CROSSED（全仓）**：所有仓位共享保证金，账户里所有的可用资金都视作可用保证金

## 持仓模式说明

- **ONE_WAY（单向）**：只能持有一个方向的仓位
- **HEDGE（双向）**：可以同时持有多空两个方向的仓位

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

