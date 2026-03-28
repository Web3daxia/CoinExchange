# 杠杆交易模块 (Leveraged Trading Module)

## 模块概述

杠杆交易模块提供了完整的杠杆交易功能，允许用户使用借入资金进行更大规模的交易，支持多种订单类型、交易策略和风险管理机制。

## 主要功能

### 1. 杠杆设置
- 支持1x到100x的杠杆倍数设置
- 动态调整杠杆（根据市场波动情况）
- 最大杠杆限制设置
- 杠杆变更限制（未开仓时才能调整）

### 2. 订单类型
- **市价单（Market Order）**：立即按市场价格成交
- **限价单（Limit Order）**：设定价格，达到价格时成交
- **止损单（Stop Loss）**：价格触及止损价时自动平仓
- **止盈单（Take Profit）**：价格触及止盈价时自动平仓
- **止损限价单（Stop-Limit）**：价格触及止损价时提交限价单
- **条件单（Conditional Order）**：满足条件时自动提交订单

### 3. 仓位管理
- 仓位查询
- 未实现盈亏和已实现盈亏计算
- 强平价格计算
- 保证金率计算
- 保证金补充

### 4. 风险控制
- 初始保证金和维持保证金管理
- 强平机制（保证金不足时自动平仓）
- 风险警报（保证金不足、强平风险、杠杆过高等）
- 实时风险监控

### 5. 交易策略
- **网格交易（Grid）**：在价格区间内自动买卖
- **趋势跟踪（Trend Following）**：跟随市场趋势交易
- **反转策略（Reverse）**：在市场反转时交易
- 策略回测（待实现）

### 6. 风险管理
- 账户风险查询
- 风险警报设置
- 自动强平
- 仓位风险监控

## API接口

### 市场数据
- `GET /leveraged/market/{pair}` - 获取杠杆交易市场数据

### 订单管理
- `POST /leveraged/order` - 创建杠杆订单
- `GET /leveraged/order/history` - 查询订单历史
- `POST /leveraged/order/market` - 提交市价单
- `POST /leveraged/order/limit` - 提交限价单
- `POST /leveraged/order/stop-loss` - 提交止损单
- `POST /leveraged/order/take-profit` - 提交止盈单
- `POST /leveraged/order/stop-limit` - 提交止损限价单
- `POST /leveraged/order/conditional` - 提交条件单

### 杠杆设置
- `POST /leveraged/account/leverage` - 调整杠杆倍数
- `GET /leveraged/account/leverage` - 查询杠杆倍数

### 仓位管理
- `GET /leveraged/account/positions` - 查询仓位信息
- `GET /leveraged/account/risk` - 查询账户风险
- `POST /leveraged/account/top-up` - 补充保证金
- `POST /leveraged/account/liquidate` - 强制平仓

### 交易策略
- `POST /leveraged/strategy/configure` - 配置交易策略
- `POST /leveraged/strategy/execute` - 执行交易策略
- `POST /leveraged/robot/configure` - 配置交易机器人
- `POST /leveraged/robot/start` - 启动交易机器人
- `POST /leveraged/robot/stop` - 停止交易机器人

### 风险管理
- `POST /leveraged/account/risk-alert` - 设置风险警报

## 实体类

- `LeveragedAccount` - 杠杆账户
- `LeveragedOrder` - 杠杆订单
- `LeveragedPosition` - 杠杆仓位
- `LeveragedStrategy` - 杠杆交易策略
- `LeveragedRiskAlert` - 杠杆风险警报
- `LeveragedLiquidation` - 杠杆清算记录

## 服务类

- `LeverageService` - 杠杆设置服务
- `LeveragedOrderService` - 杠杆订单服务
- `LeveragedPositionService` - 杠杆仓位管理服务
- `LeveragedStrategyService` - 杠杆交易策略服务
- `LeveragedRiskManagementService` - 杠杆风险管理服务

## 定时任务

- 检查并触发条件单（每10秒）
- 更新所有仓位盈亏（每30秒）
- 检查并触发风险警报（每30秒）
- 检查并执行强平（每分钟）
- 监控仓位风险（每30秒）
- 执行所有活跃策略（每30秒）

## 依赖

- `common` - 公共模块
- `wallet` - 钱包模块
- `user` - 用户模块
- `spot-trading` - 现货交易模块（用于市场数据）

## 注意事项

1. **杠杆风险**：高杠杆交易可以快速带来较大收益，但也容易触发强平，用户需要谨慎使用
2. **保证金管理**：需要保持足够的保证金，避免触发强平
3. **止损止盈**：建议设置止损和止盈价格，控制风险
4. **风险警报**：建议设置风险警报，及时了解账户风险状况
5. **策略回测**：在使用策略前，建议先进行回测，验证策略有效性

## 强平机制

当仓位的保证金率低于1.0时，系统会自动触发强平：
- 强平价格 = 开仓价格 * (1 ± 1/杠杆倍数 * (1 - 维持保证金率))
- 做多：强平价格 = 开仓价格 * (1 - 1/杠杆倍数 * (1 - 维持保证金率))
- 做空：强平价格 = 开仓价格 * (1 + 1/杠杆倍数 * (1 - 维持保证金率))

## 保证金计算

- 初始保证金 = 交易金额 / 杠杆倍数
- 维持保证金 = 初始保证金 * 维持保证金率（通常为50%）
- 保证金率 = (保证金 + 未实现盈亏) / 维持保证金















