# 策略交易模块 (Strategy Trading Module)

## 模块概述

策略交易模块提供了丰富的现货和合约交易策略实现，支持用户通过预设的策略自动执行交易操作。

## 主要功能

### 现货交易策略

1. **均值回归策略（Mean Reversion）**
   - 当价格偏离均衡价时买入或卖出，等待价格回归
   - 适用场景：市场波动幅度较大且价格会回归到均值的环境

2. **动量交易策略（Momentum Trading）**
   - 基于市场的动量信号进行交易
   - 适用场景：具有强烈动量的市场

3. **布林带突破策略（Bollinger Bands Breakout）**
   - 当价格突破布林带的上轨或下轨时执行交易
   - 适用场景：波动性较高、频繁突破的市场

4. **反向网格交易（Reverse Grid）**
   - 与传统网格交易相反，价格突破网格范围时做出相反的交易决策
   - 适用场景：市场出现明显趋势反转时

### 合约交易策略

1. **网格合约交易（Grid Futures）**
   - 在合约市场中设置多个买卖订单，利用价格波动产生收益
   - 适用场景：震荡市场和短期波动

2. **对冲策略（Hedging）**
   - 通过持有多头和空头仓位来规避风险
   - 适用场景：用户对现货市场有较大持仓，想要对冲风险

3. **反向交易策略（Contrarian）**
   - 在市场极度看涨或看跌时，采取与市场主流相反的仓位
   - 适用场景：市场处于极端情绪时

4. **套利策略（Arbitrage）**
   - 利用不同市场或合约之间的价差进行无风险套利
   - 适用场景：不同交易平台或合约市场之间的价差

5. **资金费率套利策略（Funding Rate Arbitrage）**
   - 通过做多和做空合约同时进行，从资金费率中获益
   - 适用场景：波动较小，资金费率较高的市场

6. **趋势反转策略（Trend Reversal）**
   - 基于市场的价格走势进行反向操作
   - 适用场景：强烈的市场趋势反转

7. **做市策略（Market Making）**
   - 通过不断提供买单和卖单来维持市场流动性，赚取买卖价差
   - 适用场景：流动性较低的市场

8. **趋势跟随合约策略（Trend Following Futures）**
   - 根据市场趋势（如均线交叉、ADX指标等）来选择开仓方向
   - 适用场景：市场趋势明显的环境

## API接口

- `POST /strategy/execute` - 执行策略
- `GET /strategy/list` - 获取支持的策略列表

## 策略类型代码

### 现货策略
- `MEAN_REVERSION` - 均值回归策略
- `MOMENTUM_TRADING` - 动量交易策略
- `BOLLINGER_BANDS_BREAKOUT` - 布林带突破策略
- `REVERSE_GRID` - 反向网格交易策略

### 合约策略
- `GRID_FUTURES` - 网格合约交易策略
- `HEDGING` - 对冲策略
- `CONTRARIAN` - 反向交易策略
- `ARBITRAGE` - 套利策略
- `FUNDING_RATE_ARBITRAGE` - 资金费率套利策略
- `TREND_REVERSAL` - 趋势反转策略
- `MARKET_MAKING` - 做市策略
- `TREND_FOLLOWING_FUTURES` - 趋势跟随合约策略

## 策略参数说明

### 通用参数
- `orderAmount` - 订单金额
- `orderQuantity` - 订单数量

### 均值回归策略参数
- `lookbackPeriod` - 回看周期（默认20）
- `deviationThreshold` - 偏离阈值（默认0.02，即2%）

### 动量交易策略参数
- `period` - 周期（默认14）
- `momentumThreshold` - 动量阈值（默认0.01，即1%）

### 布林带突破策略参数
- `period` - 周期（默认20）
- `stdDev` - 标准差倍数（默认2.0）

### 网格策略参数
- `gridLowerPrice` - 网格下限价格
- `gridUpperPrice` - 网格上限价格
- `gridCount` - 网格数量（默认10）
- `gridSpacing` - 网格间距

### 合约策略参数
- `leverage` - 杠杆倍数（默认10）
- `positionSide` - 仓位方向（LONG, SHORT, BOTH）

## 使用示例

### 执行均值回归策略

```json
POST /strategy/execute
{
  "pairName": "BTC/USDT",
  "marketType": "SPOT",
  "strategyType": "MEAN_REVERSION",
  "strategyParams": {
    "lookbackPeriod": 20,
    "deviationThreshold": 0.02,
    "orderAmount": 100
  }
}
```

### 执行网格合约交易策略

```json
POST /strategy/execute
{
  "pairName": "BTC/USDT",
  "marketType": "FUTURES_USDT",
  "strategyType": "GRID_FUTURES",
  "strategyParams": {
    "gridLowerPrice": 48000,
    "gridUpperPrice": 52000,
    "gridCount": 10,
    "orderAmount": 100,
    "leverage": 10
  }
}
```

## 注意事项

1. **策略参数**：确保策略参数配置正确，符合交易对的特点
2. **市场类型**：不同策略支持不同的市场类型，请确保市场类型匹配
3. **资金管理**：建议设置合理的订单金额，避免过度交易
4. **风险控制**：策略执行前应检查账户余额和风险限制
5. **实时监控**：建议监控策略执行结果，及时调整参数

## 模块依赖

- `common` - 公共模块
- `spot-trading` - 现货交易模块
- `futures-usdt` - USDT本位合约模块
- `futures-coin` - 币本位合约模块
- `leveraged-trading` - 杠杆交易模块

## 架构设计

### 策略接口
所有策略实现 `StrategyService` 接口，提供统一的执行方法。

### 策略工厂
`StrategyFactory` 负责管理所有策略实例，根据策略类型获取对应的策略服务。

### 策略管理服务
`StrategyManagementService` 提供策略执行和管理功能。

## 扩展说明

如需添加新的策略，只需：
1. 实现 `StrategyService` 接口
2. 使用 `@Component` 注解标记为Spring组件
3. 实现 `getStrategyType()` 和 `getStrategyName()` 方法
4. 策略工厂会自动注册新策略















