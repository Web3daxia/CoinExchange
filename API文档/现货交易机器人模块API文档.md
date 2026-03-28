# 现货交易机器人模块API文档

## 基础信息

- **模块名称**：现货交易机器人模块（Spot Trading Bot）
- **用户端基础路径**：`/api/spot-bot`
- **后台管理基础路径**：`/admin/spot-bot`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 机器人管理

#### 1.1 获取机器人列表

**接口地址**：`GET /api/spot-bot/bots`

**请求示例**：
```
GET /api/spot-bot/bots
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "pairName": "BTC/USDT",
      "botType": "NORMAL",
      "status": "RUNNING",
      "createTime": "2024-01-01T12:00:00"
    }
  ]
}
```

---

## 后台管理API

### 1. 机器人配置

#### 1.1 创建机器人配置

**接口地址**：`POST /admin/spot-bot/config/create`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "botType": "NORMAL",
  "orderInterval": 5,
  "initialOrderQuantity": "0.10000000",
  "pricePrecision": 2,
  "quantityPrecision": 8,
  "spreadType": "PERCENTAGE",
  "maxSpread": "0.00100000",
  "priceChangeStep": "0.00010000",
  "minTradeVolume": "10.00000000",
  "status": "ACTIVE"
}
```

#### 1.2 更新机器人配置

**接口地址**：`PUT /admin/spot-bot/config/update/{configId}`

#### 1.3 启动/停止机器人

**接口地址**：`POST /admin/spot-bot/bot/{botId}/toggle`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enabled | Boolean | 是 | 是否启用 |

---

### 2. 控制面板机器人

#### 2.1 创建控制面板机器人

**接口地址**：`POST /admin/spot-bot/control-panel/create`

**请求参数**：
```json
{
  "pairName": "BTC/USDT",
  "botType": "CONTROL_PANEL",
  "initialPrice": "50000.00000000",
  "high24h": "51000.00000000",
  "low24h": "49000.00000000",
  "volume24h": "1000000.00000000",
  "amount24h": "50000000.00000000"
}
```

#### 2.2 手动输入K线数据

**接口地址**：`POST /admin/spot-bot/control-panel/{botId}/kline`

**请求参数**：
```json
{
  "interval": "5m",
  "open": "50000.00000000",
  "high": "50500.00000000",
  "low": "49800.00000000",
  "close": "50200.00000000",
  "volume": "1000.00000000",
  "timestamp": "2024-01-01T12:00:00"
}
```

---

**文档版本**：v1.0  
**最后更新**：2024-01-01














