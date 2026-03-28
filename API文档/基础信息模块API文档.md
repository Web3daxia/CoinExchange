# 基础信息模块API文档

## 基础信息

- **模块名称**：基础信息模块（Basic Info）
- **用户端基础路径**：`/api/basic`
- **后台管理基础路径**：`/admin/basic`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 币种信息

#### 1.1 获取所有币种列表

**接口地址**：`GET /api/basic/currencies`

**请求示例**：
```
GET /api/basic/currencies
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "currencyCode": "BTC",
      "currencyName": "Bitcoin",
      "currencyNameZh": "比特币",
      "icon": "https://example.com/btc.png",
      "decimals": 8,
      "status": "ACTIVE",
      "sortOrder": 1
    }
  ]
}
```

#### 1.2 获取币种详情

**接口地址**：`GET /api/basic/currency/{currencyCode}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currencyCode | String | 是 | 币种代码 |

#### 1.3 获取币种价格

**接口地址**：`GET /api/basic/currency/{currencyCode}/price`

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "currencyCode": "BTC",
    "price": "50000.00000000",
    "priceChange24h": "2.50000000",
    "priceChangePercent24h": "2.50",
    "updateTime": "2024-01-01T12:00:00"
  }
}
```

---

### 2. 交易对信息

#### 2.1 获取所有交易对

**接口地址**：`GET /api/basic/trading-pairs`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| marketType | String | 否 | 市场类型：SPOT, FUTURES_USDT, FUTURES_COIN, OPTIONS |

**请求示例**：
```
GET /api/basic/trading-pairs?marketType=SPOT
```

---

### 3. 系统公告

#### 3.1 获取公告列表

**接口地址**：`GET /api/basic/announcements`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| type | String | 否 | 公告类型：SYSTEM, MAINTENANCE, NOTICE |

**请求示例**：
```
GET /api/basic/announcements?page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "系统维护通知",
        "content": "系统将于2024-01-01 00:00进行维护...",
        "type": "MAINTENANCE",
        "priority": "HIGH",
        "publishTime": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 3.2 获取公告详情

**接口地址**：`GET /api/basic/announcement/{announcementId}`

---

### 4. 帮助中心

#### 4.1 获取帮助分类

**接口地址**：`GET /api/basic/help/categories`

#### 4.2 获取帮助文章列表

**接口地址**：`GET /api/basic/help/articles`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 否 | 分类ID |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 4.3 获取帮助文章详情

**接口地址**：`GET /api/basic/help/article/{articleId}`

---

## 后台管理API

### 1. 币种管理

#### 1.1 创建币种

**接口地址**：`POST /admin/basic/currency/create`

#### 1.2 更新币种

**接口地址**：`PUT /admin/basic/currency/update/{currencyId}`

#### 1.3 删除币种

**接口地址**：`DELETE /admin/basic/currency/{currencyId}`

---

### 2. 公告管理

#### 2.1 创建公告

**接口地址**：`POST /admin/basic/announcement/create`

#### 2.2 更新公告

**接口地址**：`PUT /admin/basic/announcement/update/{announcementId}`

#### 2.3 删除公告

**接口地址**：`DELETE /admin/basic/announcement/{announcementId}`

---

**文档版本**：v1.0  
**最后更新**：2024-01-01














