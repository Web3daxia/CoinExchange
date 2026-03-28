# API网关模块API文档

## 基础信息

- **模块名称**：API网关模块（API Gateway）
- **基础路径**：`/api`
- **认证方式**：API Key + Secret

---

## API Key管理

### 1. 创建API Key

**接口地址**：`POST /api/api-key/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "keyName": "My API Key",
  "permissions": ["READ", "TRADE"],
  "ipWhitelist": ["192.168.1.1"],
  "expireTime": "2025-01-01T00:00:00"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyName | String | 是 | API Key名称 |
| permissions | String[] | 是 | 权限列表：READ（只读）, TRADE（交易）, WITHDRAW（提现） |
| ipWhitelist | String[] | 否 | IP白名单（为空则不限制） |
| expireTime | String | 否 | 过期时间（为空则永不过期） |

**响应示例**：
```json
{
  "code": 200,
  "message": "API Key创建成功",
  "data": {
    "apiKey": "your-api-key-here",
    "apiSecret": "your-api-secret-here",
    "keyName": "My API Key",
    "permissions": ["READ", "TRADE"],
    "createTime": "2024-01-01T12:00:00",
    "expireTime": "2025-01-01T00:00:00",
    "warning": "请妥善保管API Secret，仅显示一次"
  }
}
```

**注意**：API Secret仅在创建时返回一次，请妥善保管。

---

### 2. 获取API Key列表

**接口地址**：`GET /api/api-key/list`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /api/api-key/list
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "keyName": "My API Key",
      "apiKey": "your-api-key-here",
      "permissions": ["READ", "TRADE"],
      "status": "ACTIVE",
      "lastUsedTime": "2024-01-01T11:00:00",
      "createTime": "2024-01-01T10:00:00",
      "expireTime": "2025-01-01T00:00:00"
    }
  ]
}
```

---

### 3. 更新API Key

**接口地址**：`PUT /api/api-key/{keyId}`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "keyName": "Updated API Key",
  "permissions": ["READ"],
  "ipWhitelist": ["192.168.1.1", "192.168.1.2"],
  "expireTime": "2025-12-31T00:00:00"
}
```

---

### 4. 删除API Key

**接口地址**：`DELETE /api/api-key/{keyId}`

**请求头**：
```
X-User-Id: 1
```

---

### 5. 启用/禁用API Key

**接口地址**：`POST /api/api-key/{keyId}/toggle`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "enabled": false
}
```

---

## API签名说明

使用API Key进行API调用时，需要在请求头中包含签名信息。

### 签名生成步骤

1. 获取当前时间戳（秒）：`timestamp = System.currentTimeMillis() / 1000`
2. 构建待签名字符串：`signString = timestamp + method + path + queryString + body`
   - `method`: HTTP方法（GET/POST/PUT/DELETE）
   - `path`: 请求路径（不包含域名）
   - `queryString`: 查询字符串（如果有，需要排序）
   - `body`: 请求体（如果有）
3. 使用HMAC-SHA256算法签名：`signature = HMAC-SHA256(signString, apiSecret)`
4. 将签名转换为十六进制字符串

### 请求头示例

```
X-API-KEY: your-api-key-here
X-TIMESTAMP: 1704067200
X-SIGNATURE: abc123def456...
```

---

## API调用限制

### 限流规则

- **Rate Limit**: 1200次/分钟
- **Burst Limit**: 100次/秒

### 限流响应

当超过限流时，API会返回：

```json
{
  "code": 429,
  "message": "Rate limit exceeded",
  "data": null,
  "rateLimit": {
    "limit": 1200,
    "remaining": 0,
    "reset": 1704067260
  }
}
```

---

## WebSocket API

### 1. WebSocket连接

**连接地址**：`wss://api.example.com/ws`

**认证参数**：
```
apiKey: your-api-key
timestamp: 1704067200
signature: abc123def456...
```

### 2. 订阅市场数据

**订阅消息**：
```json
{
  "op": "subscribe",
  "args": ["market:BTC/USDT:ticker"]
}
```

### 3. 取消订阅

**取消订阅消息**：
```json
{
  "op": "unsubscribe",
  "args": ["market:BTC/USDT:ticker"]
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（API Key无效） |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 429 | 请求过于频繁（限流） |
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














