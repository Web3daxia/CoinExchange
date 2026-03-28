# 学院模块API文档

## 基础信息

- **模块名称**：学院模块（Academy）
- **用户端基础路径**：`/api/academy`
- **后台管理基础路径**：`/admin/academy`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 课程管理

#### 1.1 获取课程列表

**接口地址**：`GET /api/academy/courses`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 否 | 分类ID筛选 |
| keyword | String | 否 | 关键词搜索 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/academy/courses?page=0&size=20
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
        "courseTitle": "区块链基础知识",
        "courseDescription": "学习区块链的基础概念和原理",
        "instructor": "讲师A",
        "thumbnail": "https://example.com/thumbnail.jpg",
        "duration": 120,
        "studentCount": 1000,
        "rating": "4.8",
        "price": "0.00000000",
        "isFree": true,
        "categoryId": 1,
        "categoryName": "入门课程",
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 1.2 获取课程详情

**接口地址**：`GET /api/academy/course/{courseId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | Long | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "courseTitle": "区块链基础知识",
    "courseDescription": "学习区块链的基础概念和原理",
    "content": "完整的课程内容...",
    "instructor": "讲师A",
    "instructorAvatar": "https://example.com/avatar.jpg",
    "thumbnail": "https://example.com/thumbnail.jpg",
    "duration": 120,
    "studentCount": 1000,
    "rating": "4.8",
    "reviewCount": 200,
    "price": "0.00000000",
    "isFree": true,
    "chapters": [
      {
        "id": 1,
        "chapterTitle": "第一章：区块链概述",
        "chapterOrder": 1,
        "lessons": [
          {
            "id": 1,
            "lessonTitle": "什么是区块链",
            "lessonOrder": 1,
            "duration": 10,
            "isFree": true
          }
        ]
      }
    ],
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.3 报名课程

**接口地址**：`POST /api/academy/course/{courseId}/enroll`

**请求头**：
```
X-User-Id: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | Long | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "报名成功",
  "data": {
    "enrollmentId": 1,
    "courseId": 1,
    "userId": 1,
    "enrollTime": "2024-01-01T12:00:00"
  }
}
```

#### 1.4 获取我的课程

**接口地址**：`GET /api/academy/my-courses`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 学习状态：STUDYING（学习中）, COMPLETED（已完成） |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

### 2. 学习进度

#### 2.1 获取学习进度

**接口地址**：`GET /api/academy/course/{courseId}/progress`

**请求头**：
```
X-User-Id: 1
```

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | Long | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "courseId": 1,
    "userId": 1,
    "progressPercent": "50.00",
    "completedLessons": 5,
    "totalLessons": 10,
    "lastStudyTime": "2024-01-01T13:00:00",
    "currentLessonId": 6
  }
}
```

#### 2.2 更新学习进度

**接口地址**：`POST /api/academy/course/{courseId}/progress`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "lessonId": 6,
  "progressPercent": "60.00"
}
```

---

### 3. 课程分类

#### 3.1 获取分类列表

**接口地址**：`GET /api/academy/categories`

**请求示例**：
```
GET /api/academy/categories
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "categoryName": "入门课程",
      "categoryIcon": "https://example.com/icon.jpg",
      "courseCount": 50,
      "sortOrder": 1
    }
  ]
}
```

---

## 后台管理API

### 1. 课程管理

#### 1.1 创建课程

**接口地址**：`POST /admin/academy/course/create`

**请求参数**：
```json
{
  "courseTitle": "区块链基础知识",
  "courseDescription": "学习区块链的基础概念和原理",
  "content": "完整的课程内容...",
  "instructor": "讲师A",
  "thumbnail": "https://example.com/thumbnail.jpg",
  "duration": 120,
  "price": "0.00000000",
  "isFree": true,
  "categoryId": 1,
  "status": "PUBLISHED"
}
```

#### 1.2 更新课程

**接口地址**：`PUT /admin/academy/course/update/{courseId}`

#### 1.3 删除课程

**接口地址**：`DELETE /admin/academy/course/{courseId}`

---

### 2. 分类管理

#### 2.1 创建分类

**接口地址**：`POST /admin/academy/category/create`

**请求参数**：
```json
{
  "categoryName": "入门课程",
  "categoryIcon": "https://example.com/icon.jpg",
  "sortOrder": 1,
  "status": "ACTIVE"
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

**文档版本**：v1.0  
**最后更新**：2024-01-01














