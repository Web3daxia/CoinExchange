# 社区模块API文档

## 基础信息

- **模块名称**：社区模块（Community）
- **用户端基础路径**：`/api/community`
- **后台管理基础路径**：`/admin/community`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 帖子管理

#### 1.1 发布帖子

**接口地址**：`POST /api/community/post/create`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | String | 是 | 帖子标题 |
| content | String | 是 | 帖子内容 |
| images | File[] | 否 | 图片文件（可多张） |
| categoryId | Long | 否 | 分类ID |

**请求示例**：
```
POST /api/community/post/create
Content-Type: multipart/form-data

title=帖子标题
content=帖子内容
categoryId=1
images=<file1>
images=<file2>
```

**响应示例**：
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1,
    "title": "帖子标题",
    "content": "帖子内容",
    "authorId": 1,
    "authorName": "用户123",
    "images": ["https://example.com/image1.jpg"],
    "likeCount": 0,
    "commentCount": 0,
    "viewCount": 0,
    "status": "PUBLISHED",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.2 获取帖子列表

**接口地址**：`GET /api/community/post/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 否 | 分类ID筛选 |
| keyword | String | 否 | 关键词搜索 |
| sortBy | String | 否 | 排序方式：LATEST（最新）, HOT（热门）, TOP（置顶） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/community/post/list?sortBy=HOT&page=0&size=20
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
        "title": "帖子标题",
        "content": "帖子内容摘要...",
        "authorId": 1,
        "authorName": "用户123",
        "authorAvatar": "https://example.com/avatar.jpg",
        "images": ["https://example.com/image1.jpg"],
        "likeCount": 100,
        "commentCount": 50,
        "viewCount": 1000,
        "isTop": false,
        "isHot": true,
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5
  }
}
```

#### 1.3 获取帖子详情

**接口地址**：`GET /api/community/post/{postId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| postId | Long | 是 | 帖子ID |

**请求示例**：
```
GET /api/community/post/1
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "帖子标题",
    "content": "完整的帖子内容...",
    "authorId": 1,
    "authorName": "用户123",
    "authorAvatar": "https://example.com/avatar.jpg",
    "images": ["https://example.com/image1.jpg"],
    "likeCount": 100,
    "commentCount": 50,
    "viewCount": 1001,
    "isLiked": false,
    "isTop": false,
    "isHot": true,
    "categoryId": 1,
    "categoryName": "交易心得",
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.4 编辑帖子

**接口地址**：`PUT /api/community/post/{postId}`

**请求头**：
```
X-User-Id: 1
Content-Type: multipart/form-data
```

**请求参数**：同发布帖子（所有字段可选）

#### 1.5 删除帖子

**接口地址**：`DELETE /api/community/post/{postId}`

**请求头**：
```
X-User-Id: 1
```

#### 1.6 点赞/取消点赞帖子

**接口地址**：`POST /api/community/post/{postId}/like`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
POST /api/community/post/1/like
```

**响应示例**：
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "postId": 1,
    "isLiked": true,
    "likeCount": 101
  }
}
```

---

### 2. 评论管理

#### 2.1 发表评论

**接口地址**：`POST /api/community/comment/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "postId": 1,
  "parentId": null,
  "content": "评论内容",
  "images": ["https://example.com/image.jpg"]
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| postId | Long | 是 | 帖子ID |
| parentId | Long | 否 | 父评论ID（回复评论时使用） |
| content | String | 是 | 评论内容 |
| images | String[] | 否 | 图片URL列表 |

**响应示例**：
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 1,
    "postId": 1,
    "authorId": 1,
    "authorName": "用户123",
    "content": "评论内容",
    "likeCount": 0,
    "replyCount": 0,
    "createTime": "2024-01-01T13:00:00"
  }
}
```

#### 2.2 获取帖子评论列表

**接口地址**：`GET /api/community/comment/list/{postId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| postId | Long | 是 | 帖子ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 2.3 删除评论

**接口地址**：`DELETE /api/community/comment/{commentId}`

**请求头**：
```
X-User-Id: 1
```

#### 2.4 点赞/取消点赞评论

**接口地址**：`POST /api/community/comment/{commentId}/like`

**请求头**：
```
X-User-Id: 1
```

---

### 3. 分类管理

#### 3.1 获取分类列表

**接口地址**：`GET /api/community/category/list`

**请求示例**：
```
GET /api/community/category/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "categoryName": "交易心得",
      "categoryIcon": "https://example.com/icon.jpg",
      "postCount": 1000,
      "sortOrder": 1
    }
  ]
}
```

---

### 4. 关注管理

#### 4.1 关注用户

**接口地址**：`POST /api/community/follow/{userId}`

**请求头**：
```
X-User-Id: 1
```

#### 4.2 取消关注

**接口地址**：`DELETE /api/community/follow/{userId}`

#### 4.3 获取关注列表

**接口地址**：`GET /api/community/follow/following`

**请求头**：
```
X-User-Id: 1
```

#### 4.4 获取粉丝列表

**接口地址**：`GET /api/community/follow/followers`

**请求头**：
```
X-User-Id: 1
```

---

## 后台管理API

### 1. 帖子管理

#### 1.1 获取所有帖子

**接口地址**：`GET /admin/community/post/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选：PUBLISHED, PENDING, DELETED |
| keyword | String | 否 | 关键词搜索 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

#### 1.2 审核帖子

**接口地址**：`POST /admin/community/post/review/{postId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 审核状态：APPROVED, REJECTED |
| remark | String | 否 | 审核备注 |

#### 1.3 置顶帖子

**接口地址**：`POST /admin/community/post/top/{postId}`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| isTop | Boolean | 是 | 是否置顶 |

#### 1.4 删除帖子（后台）

**接口地址**：`DELETE /admin/community/post/{postId}`

---

### 2. 分类管理

#### 2.1 创建分类

**接口地址**：`POST /admin/community/category/create`

**请求参数**：
```json
{
  "categoryName": "交易心得",
  "categoryIcon": "https://example.com/icon.jpg",
  "sortOrder": 1,
  "status": "ACTIVE"
}
```

#### 2.2 更新分类

**接口地址**：`PUT /admin/community/category/update/{categoryId}`

#### 2.3 删除分类

**接口地址**：`DELETE /admin/community/category/{categoryId}`

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

**文档版本**：v1.0  
**最后更新**：2024-01-01














