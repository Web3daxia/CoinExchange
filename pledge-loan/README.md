# 质押借币模块

## 模块说明

质押借币模块允许用户将特定加密货币作为抵押物，借取其他加密货币。系统会根据质押资产价值、借款比例、利率等计算借款额度和还款期限，并实施风险管理和自动平仓机制。

## 目录结构

```
pledge-loan/
├── src/main/java/com/cryptotrade/pledgeloan/
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

1. **用户功能**
   - 质押借币申请
   - 查看质押借币记录
   - 还款（全额/部分）
   - 补仓
   - 风险监控

2. **后台管理**
   - 质押币种配置
   - 借款币种配置
   - 订单审批管理
   - 风险监控与平仓
   - 利率调整
   - 数据统计

## 核心特性

- 灵活的质押和借款币种配置
- 自动计算借款额度
- 风险管理和平仓机制
- 利息计算和还款管理
- 健康度监控和风险预警














