# 快讯模块API文档

## 基础信息

- **模块名称**：快讯模块（News Feed）
- **用户端基础路径**：`/api/news`
- **后台管理基础路径**：`/api/admin/news`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 新闻列表和详情

#### 1.1 获取新闻列表

**接口地址**：`GET /api/news/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态（默认：PUBLISHED） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/news/list?page=0&size=20&status=PUBLISHED
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
        "title": "比特币价格突破50000美元",
        "summary": "比特币价格在今日突破50000美元大关...",
        "source": "CoinDesk",
        "publishTime": "2024-01-01T10:00:00",
        "viewCount": 1000,
        "category": "MARKET",
        "tags": ["BTC", "PRICE"],
        "isHot": true,
        "isRecommended": false
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  }
}
```

#### 1.2 获取热门新闻

**接口地址**：`GET /api/news/hot`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/news/hot?page=0&size=20
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
        "title": "比特币价格突破50000美元",
        "viewCount": 5000,
        "isHot": true
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 1.3 获取推荐新闻

**接口地址**：`GET /api/news/recommended`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/news/recommended?page=0&size=20
```

#### 1.4 搜索新闻

**接口地址**：`GET /api/news/search`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 是 | 搜索关键词 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/news/search?keyword=比特币&page=0&size=20
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
        "title": "比特币价格突破50000美元",
        "summary": "比特币价格在今日突破50000美元大关..."
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

#### 1.5 获取新闻详情

**接口地址**：`GET /api/news/{newsId}`

**请求头**：
```
Accept-Language: zh-CN
userId: 1 (可选)
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
GET /api/news/1
Accept-Language: zh-CN
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "比特币价格突破50000美元",
    "content": "完整的新闻内容...",
    "summary": "比特币价格在今日突破50000美元大关...",
    "source": "CoinDesk",
    "sourceUrl": "https://www.coindesk.com/...",
    "author": "John Doe",
    "publishTime": "2024-01-01T10:00:00",
    "viewCount": 1001,
    "category": "MARKET",
    "tags": ["BTC", "PRICE"],
    "isHot": true,
    "isRecommended": false,
    "images": ["https://example.com/image1.jpg"],
    "languageCode": "zh-CN"
  }
}
```

**说明**：
- 支持多语言，通过`Accept-Language`请求头指定语言代码（如：zh-CN, en-US）
- 如果指定语言的翻译不存在，返回原始语言内容
- 自动增加阅读量
- 如果用户已登录，会记录浏览记录

---

### 2. 新闻收藏

#### 2.1 收藏新闻

**接口地址**：`POST /api/news/favorite/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
POST /api/news/favorite/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "收藏成功",
  "data": {
    "id": 1,
    "userId": 1,
    "newsId": 1,
    "favoriteTime": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 取消收藏

**接口地址**：`DELETE /api/news/favorite/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
DELETE /api/news/favorite/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "取消收藏成功",
  "data": null
}
```

#### 2.3 获取我的收藏

**接口地址**：`GET /api/news/favorite/my-favorites`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/news/favorite/my-favorites?page=0&size=20
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
        "userId": 1,
        "newsId": 1,
        "news": {
          "id": 1,
          "title": "比特币价格突破50000美元",
          "summary": "..."
        },
        "favoriteTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

#### 2.4 检查是否已收藏

**接口地址**：`GET /api/news/favorite/check/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
GET /api/news/favorite/check/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

### 3. 新闻互动

#### 3.1 点赞新闻

**接口地址**：`POST /api/news/interaction/like/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
POST /api/news/interaction/like/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "id": 1,
    "userId": 1,
    "newsId": 1,
    "interactionType": "LIKE",
    "interactionTime": "2024-01-01T12:00:00"
  }
}
```

#### 3.2 取消点赞

**接口地址**：`DELETE /api/news/interaction/like/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

#### 3.3 分享新闻

**接口地址**：`POST /api/news/interaction/share/{newsId}`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sharePlatform | String | 否 | 分享平台：WECHAT, TELEGRAM, TWITTER等 |

**请求示例**：
```
POST /api/news/interaction/share/1?sharePlatform=TWITTER
```

---

### 4. 新闻订阅

#### 4.1 订阅新闻

**接口地址**：`POST /api/news/subscription/subscribe`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：
```json
{
  "categoryIds": [1, 2, 3],
  "tagIds": [1, 2],
  "pushFrequency": "DAILY",
  "pushEnabled": true
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryIds | Array<Long> | 否 | 分类ID列表 |
| tagIds | Array<Long> | 否 | 标签ID列表 |
| pushFrequency | String | 是 | 推送频率：DAILY, WEEKLY, MONTHLY |
| pushEnabled | Boolean | 是 | 是否启用推送 |

**响应示例**：
```json
{
  "code": 200,
  "message": "订阅成功",
  "data": {
    "id": 1,
    "userId": 1,
    "pushFrequency": "DAILY",
    "pushEnabled": true,
    "subscribeTime": "2024-01-01T12:00:00"
  }
}
```

#### 4.2 更新订阅设置

**接口地址**：`PUT /api/news/subscription/update`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
userId: 1
```

**请求参数**：同订阅新闻（所有字段可选）

#### 4.3 取消订阅

**接口地址**：`DELETE /api/news/subscription/unsubscribe`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
DELETE /api/news/subscription/unsubscribe
```

#### 4.4 获取我的订阅设置

**接口地址**：`GET /api/news/subscription/my-subscription`

**请求头**：
```
Authorization: Bearer {token}
userId: 1
```

**请求示例**：
```
GET /api/news/subscription/my-subscription
```

---

## 后台管理API

### 1. 新闻管理

#### 1.1 获取新闻列表

**接口地址**：`GET /api/admin/news/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：PENDING, APPROVED, REJECTED, PUBLISHED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/admin/news/list?status=PENDING&page=0&size=20
```

#### 1.2 获取新闻详情

**接口地址**：`GET /api/admin/news/{newsId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

#### 1.3 审核新闻

**接口地址**：`POST /api/admin/news/review/{newsId}`

**请求头**：
```
adminId: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 审核状态：APPROVED, REJECTED, PUBLISHED |
| remark | String | 否 | 审核备注 |

**请求示例**：
```
POST /api/admin/news/review/1?status=APPROVED&remark=审核通过
```

**响应示例**：
```json
{
  "code": 200,
  "message": "审核成功",
  "data": {
    "id": 1,
    "title": "比特币价格突破50000美元",
    "status": "APPROVED",
    "reviewerId": 1,
    "reviewTime": "2024-01-01T13:00:00"
  }
}
```

#### 1.4 设置热门新闻

**接口地址**：`PUT /api/admin/news/hot/{newsId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| isHot | Boolean | 是 | 是否热门 |

**请求示例**：
```
PUT /api/admin/news/hot/1?isHot=true
```

#### 1.5 设置推荐新闻

**接口地址**：`PUT /api/admin/news/recommended/{newsId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| isRecommended | Boolean | 是 | 是否推荐 |

**请求示例**：
```
PUT /api/admin/news/recommended/1?isRecommended=true
```

#### 1.6 设置新闻分类

**接口地址**：`PUT /api/admin/news/category/{newsId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 是 | 分类ID |

---

### 2. 新闻源管理

#### 2.1 获取新闻源列表

**接口地址**：`GET /api/admin/news-source/list`

**请求示例**：
```
GET /api/admin/news-source/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "sourceName": "CoinDesk",
      "sourceCode": "COINDESK",
      "apiUrl": "https://api.coindesk.com/v1/news",
      "fetchFrequency": "HOURLY",
      "isEnabled": true,
      "status": "ACTIVE"
    }
  ]
}
```

#### 2.2 创建新闻源

**接口地址**：`POST /api/admin/news-source/create`

**请求参数**：
```json
{
  "sourceName": "CoinTelegraph",
  "sourceCode": "COINTELEGRAPH",
  "apiUrl": "https://api.cointelegraph.com/v1/news",
  "apiKey": "your-api-key",
  "fetchFrequency": "HOURLY",
  "isEnabled": true,
  "description": "CoinTelegraph新闻源"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sourceName | String | 是 | 新闻源名称 |
| sourceCode | String | 是 | 新闻源代码（唯一） |
| apiUrl | String | 是 | API地址 |
| apiKey | String | 否 | API密钥 |
| fetchFrequency | String | 是 | 采集频率：HOURLY, DAILY, WEEKLY |
| isEnabled | Boolean | 是 | 是否启用 |
| description | String | 否 | 描述 |

#### 2.3 更新新闻源

**接口地址**：`PUT /api/admin/news-source/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 新闻源ID |

**请求参数**：同创建新闻源（所有字段可选）

#### 2.4 启用/禁用新闻源

**接口地址**：`PUT /api/admin/news-source/status/{id}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| isEnabled | Boolean | 是 | 是否启用 |

#### 2.5 删除新闻源

**接口地址**：`DELETE /api/admin/news-source/{id}`

---

### 3. 新闻分类管理

#### 3.1 获取分类列表

**接口地址**：`GET /api/admin/news-category/list`

**请求示例**：
```
GET /api/admin/news-category/list
```

#### 3.2 创建分类

**接口地址**：`POST /api/admin/news-category/create`

**请求参数**：
```json
{
  "categoryName": "市场动态",
  "categoryCode": "MARKET",
  "sortOrder": 1,
  "status": "ACTIVE",
  "description": "市场相关新闻"
}
```

#### 3.3 更新分类

**接口地址**：`PUT /api/admin/news-category/update/{id}`

#### 3.4 删除分类

**接口地址**：`DELETE /api/admin/news-category/{id}`

---

### 4. 新闻标签管理

#### 4.1 获取标签列表

**接口地址**：`GET /api/admin/news-tag/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：ACTIVE, INACTIVE |

**请求示例**：
```
GET /api/admin/news-tag/list?status=ACTIVE
```

#### 4.2 创建标签

**接口地址**：`POST /api/admin/news-tag/create`

**请求参数**：
```json
{
  "tagName": "BTC",
  "tagCode": "BTC",
  "sortOrder": 1,
  "status": "ACTIVE",
  "description": "比特币相关标签"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tagName | String | 是 | 标签名称 |
| tagCode | String | 是 | 标签代码（唯一） |
| sortOrder | Integer | 否 | 排序顺序 |
| status | String | 否 | 状态：ACTIVE, INACTIVE |
| description | String | 否 | 描述 |

#### 4.3 更新标签

**接口地址**：`PUT /api/admin/news-tag/update/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 标签ID |

#### 4.4 更新标签状态

**接口地址**：`PUT /api/admin/news-tag/status/{id}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 状态：ACTIVE, INACTIVE |

#### 4.5 删除标签

**接口地址**：`DELETE /api/admin/news-tag/{id}`

---

### 5. 敏感词管理

#### 5.1 获取敏感词列表

**接口地址**：`GET /api/admin/sensitive-word/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |
| keyword | String | 否 | 关键词搜索 |

**请求示例**：
```
GET /api/admin/sensitive-word/list?page=0&size=20
```

#### 5.2 创建敏感词

**接口地址**：`POST /api/admin/sensitive-word/create`

**请求参数**：
```json
{
  "word": "敏感词示例",
  "level": "MEDIUM",
  "action": "FILTER",
  "description": "需要过滤的敏感词"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| word | String | 是 | 敏感词 |
| level | String | 是 | 敏感级别：LOW, MEDIUM, HIGH |
| action | String | 是 | 处理动作：FILTER（过滤）, REJECT（拒绝）, WARN（警告） |
| description | String | 否 | 描述 |

#### 5.3 批量导入敏感词

**接口地址**：`POST /api/admin/sensitive-word/batch-import`

**请求参数**：
```json
{
  "words": ["敏感词1", "敏感词2", "敏感词3"],
  "level": "MEDIUM",
  "action": "FILTER"
}
```

#### 5.4 删除敏感词

**接口地址**：`DELETE /api/admin/sensitive-word/{id}`

---

### 6. 新闻翻译管理

#### 6.1 翻译新闻到指定语言

**接口地址**：`POST /api/admin/news-translation/translate/{newsId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| targetLanguage | String | 是 | 目标语言代码，如：zh-CN, en-US |

**请求示例**：
```
POST /api/admin/news-translation/translate/1?targetLanguage=zh-CN
```

**响应示例**：
```json
{
  "code": 200,
  "message": "翻译成功",
  "data": {
    "id": 1,
    "newsId": 1,
    "languageCode": "zh-CN",
    "title": "比特币价格突破50000美元",
    "content": "翻译后的完整内容...",
    "translatedAt": "2024-01-01T14:00:00"
  }
}
```

#### 6.2 翻译新闻到所有语言

**接口地址**：`POST /api/admin/news-translation/translate-all/{newsId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
POST /api/admin/news-translation/translate-all/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "翻译任务已启动",
  "data": null
}
```

#### 6.3 获取新闻的所有翻译

**接口地址**：`GET /api/admin/news-translation/{newsId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |

**请求示例**：
```
GET /api/admin/news-translation/1
```

#### 6.4 获取指定语言的翻译

**接口地址**：`GET /api/admin/news-translation/{newsId}/{languageCode}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newsId | Long | 是 | 新闻ID |
| languageCode | String | 是 | 语言代码 |

#### 6.5 重新翻译新闻

**接口地址**：`POST /api/admin/news-translation/retranslate/{newsId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| languageCode | String | 是 | 语言代码 |

#### 6.6 获取待翻译的新闻列表

**接口地址**：`GET /api/admin/news-translation/pending`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| languageCode | String | 是 | 语言代码 |

---

### 7. 新闻采集日志

#### 7.1 获取采集日志列表

**接口地址**：`GET /api/admin/news-fetch-log/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sourceId | Long | 否 | 新闻源ID |
| fetchStatus | String | 否 | 采集状态：SUCCESS, FAILED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/admin/news-fetch-log/list?sourceId=1&fetchStatus=SUCCESS&page=0&size=20
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
        "sourceId": 1,
        "sourceName": "CoinDesk",
        "fetchStatus": "SUCCESS",
        "fetchedCount": 10,
        "failedCount": 0,
        "fetchTime": "2024-01-01T10:00:00",
        "errorMessage": null
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 通用响应格式

所有API响应统一使用以下格式：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

- `code`: HTTP状态码或业务状态码
- `message`: 响应消息
- `data`: 响应数据（可能为对象、数组或null）

---

## 多语言支持说明

1. **新闻内容多语言**：
   - 用户端通过`Accept-Language`请求头指定语言
   - 系统自动返回对应语言的翻译版本
   - 如果翻译不存在，返回原始语言内容

2. **支持的语言代码**：
   - zh-CN: 简体中文
   - en-US: 英语
   - ja-JP: 日语
   - ko-KR: 韩语
   - 等16种语言（根据系统配置）

3. **自动翻译**：
   - 后台管理可以触发自动翻译
   - 支持翻译到所有支持的语言
   - 使用配置的翻译服务（Google、Azure、Baidu等）

---

**文档版本**：v1.0  
**最后更新**：2024-01-01














