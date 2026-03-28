# 模拟交易模块

## 模块说明

模拟交易模块用于提供用户模拟交易体验功能，用户可以使用虚拟资金进行各种交易类型的模拟操作，包括现货、合约、杠杆、期权等。

## 目录结构

```
simulated-trading/
├── src/main/java/com/cryptotrade/simulatedtrading/
│   ├── entity/              # 实体类
│   ├── repository/          # Repository接口
│   ├── service/             # Service接口
│   ├── service/impl/        # Service实现
│   ├── controller/          # Controller
│   ├── dto/request/         # 请求DTO
│   ├── dto/response/        # 响应DTO
│   └── util/                # 工具类
```

## 主要功能

1. **模拟账户管理**
   - 创建模拟账户
   - 账户余额管理
   - 账户重置功能

2. **模拟交易功能**
   - 现货交易
   - USDT本位永续合约
   - 币本位永续合约
   - 交割合约
   - 杠杆交易
   - 期权合约

3. **交易管理**
   - 开仓、平仓
   - 止损、止盈
   - 限价单、市价单
   - 杠杆设置

4. **统计与分析**
   - 盈亏统计
   - 交易记录查询
   - 账户变更记录

5. **后台管理**
   - 规则配置
   - 活动管理
   - 数据统计

## 核心特性

- 无时间限制，随时进入和退出
- 支持重置账户余额
- 支持所有交易类型
- 完整的交易功能和限制
- 实时盈亏计算














