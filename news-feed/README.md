# 快讯模块

## 模块说明

快讯模块用于自动采集和展示区块链行业的最新资讯，支持多个新闻源、用户定制化订阅、新闻互动等功能。

## 目录结构

```
news-feed/
├── src/main/java/com/cryptotrade/newsfeed/
│   ├── entity/              # 实体类
│   ├── repository/          # Repository接口
│   ├── service/             # Service接口
│   ├── service/impl/        # Service实现
│   ├── controller/          # Controller
│   ├── dto/request/         # 请求DTO
│   ├── dto/response/        # 响应DTO
│   ├── config/              # 配置类
│   └── util/                # 工具类
```

## 主要功能

1. **新闻采集**
   - 多新闻源API集成
   - 定时自动采集
   - 新闻内容存储

2. **新闻展示**
   - 快讯列表展示
   - 新闻详情
   - 分类和标签筛选
   - 热门新闻推荐

3. **用户功能**
   - 新闻收藏
   - 新闻互动（点赞、分享）
   - 新闻订阅
   - 定制化通知

4. **后台管理**
   - 新闻源管理
   - 新闻审核
   - 敏感词过滤
   - 数据统计

## 核心特性

- 多新闻源集成
- 自动分类和标签
- 敏感词过滤
- 用户订阅和推送
- 新闻互动功能














