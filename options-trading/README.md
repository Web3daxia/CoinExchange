# 期权交易模块 (Options Trading Module)

## 模块概述

期权交易模块提供了完整的期权合约交易功能，包括期权定价、订单管理、期权行使、高级策略和风险管理。

## 主要功能

### 1. 期权合约管理
- 支持看涨期权（Call Option）和看跌期权（Put Option）
- 支持美式期权（American Option）和欧式期权（European Option）
- 期权合约信息管理（执行价格、到期日期等）

### 2. 期权定价
- **Black-Scholes模型**：用于计算欧式期权价格
- **二项式模型**：用于计算美式期权价格
- 隐含波动率计算
- 历史波动率计算
- 理论价格与市场价格对比

### 3. 期权订单管理
- 开仓订单（买入/卖出期权）
- 平仓订单
- 订单历史查询
- 订单状态管理

### 4. 期权持仓管理
- 持仓查询
- 未实现盈亏计算
- 已实现盈亏计算
- 持仓状态管理

### 5. 期权行使
- 手动行使期权
- 自动行使期权（到期时如果处于实值状态）
- 行使记录查询
- 实值/虚值判断

### 6. 高级期权策略
- **跨式期权策略（Straddle）**：同时买入相同执行价格、相同到期日的看涨和看跌期权
- **蝶式期权策略（Butterfly Spread）**：买入较低和较高执行价格期权，卖出两个中间执行价格期权
- **价差策略（Vertical Spread）**：买入一个执行价格期权，卖出另一个执行价格期权（牛市/熊市）
- **日历价差策略（Calendar Spread）**：卖出近期到期期权，买入远期到期期权

### 7. 风险管理
- 账户风险查询
- 止损策略设置
- 风险警报
- 强平机制
- 持仓监控

## API接口

### 市场数据
- `GET /options/market/{pair}` - 获取期权市场数据

### 订单管理
- `POST /options/order` - 创建期权订单
- `GET /options/order/history` - 查询订单历史
- `POST /options/order/call` - 提交看涨期权订单
- `POST /options/order/put` - 提交看跌期权订单

### 期权类型
- `GET /options/type` - 查询期权类型

### 期权定价
- `GET /options/pricing` - 获取期权定价信息

### 期权行使
- `POST /options/exercise` - 行使期权
- `GET /options/exercise/status` - 查询行使状态

### 期权平仓
- `POST /options/close` - 平仓期权
- `GET /options/close/history` - 查询平仓历史

### 到期日期
- `GET /options/expiry` - 获取期权到期日期

### 高级策略
- `POST /options/strategy/straddle` - 创建跨式期权策略
- `POST /options/strategy/butterfly` - 创建蝶式期权策略
- `POST /options/strategy/vertical` - 创建价差策略
- `POST /options/strategy/calendar` - 创建日历价差策略

### 风险管理
- `GET /options/account/risk` - 查询账户风险
- `POST /options/account/stop-loss` - 设置止损策略

## 实体类

- `OptionContract` - 期权合约
- `OptionOrder` - 期权订单
- `OptionPosition` - 期权持仓
- `OptionExercise` - 期权行使记录
- `OptionStrategy` - 期权策略
- `OptionRiskAlert` - 期权风险警报

## 服务类

- `OptionPricingService` - 期权定价服务
- `OptionOrderService` - 期权订单服务
- `OptionExerciseService` - 期权行使服务
- `OptionStrategyService` - 期权策略服务
- `OptionRiskManagementService` - 期权风险管理服务

## 定时任务

- 检查并更新到期期权合约状态（每5分钟）
- 自动行使到期期权（每分钟）
- 更新期权价格和持仓盈亏（每30秒）
- 检查并触发风险警报（每30秒）
- 检查并执行强平（每分钟）

## 依赖

- `common` - 公共模块
- `wallet` - 钱包模块
- `user` - 用户模块
- `spot-trading` - 现货交易模块（用于市场数据）
- `commons-math3` - Apache Commons Math（用于期权定价计算）

## 注意事项

1. 期权定价模型需要准确的波动率数据，建议从市场数据服务获取历史波动率或使用隐含波动率
2. 期权行使会产生手续费，需要在行使时扣除
3. 自动行使只在期权到期且处于实值状态时执行
4. 风险管理需要定期监控持仓，建议设置合理的止损策略
5. 高级策略涉及多个期权合约，需要确保所有合约都能正常交易















