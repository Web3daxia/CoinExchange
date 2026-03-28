# 矿池矿机模块

## 模块说明

矿池矿机模块用于管理挖矿矿池、算力租赁、挖矿任务和收益分配等功能。

## 目录结构

```
mining-pool/
├── src/main/java/com/cryptotrade/miningpool/
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

1. **矿池管理**
   - 创建、更新、删除矿池
   - 配置矿池参数（币种、算法、分配方式等）
   - 矿池状态管理

2. **算力管理**
   - 算力类型定义
   - 算力价格管理
   - 算力租赁

3. **矿工管理**
   - 用户加入矿池
   - 算力贡献记录
   - 矿工排名

4. **收益分配**
   - 按算力贡献分配收益
   - 定期结算收益
   - 收益结算记录

5. **风控管理**
   - 风险等级设置
   - 参与限制
   - 风险提示














