# 期权交易模块API文档

## 基础信息

- **模块名称**：期权交易模块（Options Trading）
- **用户端基础路径**：`/api/options`
- **后台管理基础路径**：`/admin/options`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 期权产品

#### 1.1 获取期权产品列表

**接口地址**：`GET /api/options/products`

**请求示例**：
```
GET /api/options/products
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "underlyingAsset": "BTC",
      "strikePrice": "50000.00000000",
      "expiryDate": "2024-01-31",
      "optionType": "CALL",
      "premium": "1000.00000000",
      "status": "ACTIVE"
    }
  ]
}
```

#### 1.2 获取期权产品详情

**接口地址**：`GET /api/options/product/{productId}`

---

### 2. 期权交易

#### 2.1 买入期权

**接口地址**：`POST /api/options/order/buy`

**请求参数**：
```json
{
  "productId": 1,
  "quantity": 1,
  "premium": "1000.00000000"
}
```

#### 2.2 卖出期权

**接口地址**：`POST /api/options/order/sell`

#### 2.3 行权

**接口地址**：`POST /api/options/exercise`

**请求参数**：
```json
{
  "orderId": 123456,
  "quantity": 1
}
```

#### 2.4 查询我的期权持仓

**接口地址**：`GET /api/options/positions`

---

**文档版本**：v1.0  
**最后更新**：2024-01-01














