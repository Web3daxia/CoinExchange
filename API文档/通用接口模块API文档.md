# 通用接口模块API文档

## 基础信息

- **模块名称**：通用接口模块（Common）
- **基础路径**：`/common`
- **认证方式**：无需认证（公开接口）

---

## 接口列表

### 1. 获取国家/地区列表

**接口地址**：`GET /common/countries`

**接口描述**：返回支持的国家/地区及对应的电话区号

**请求参数**：无

**请求示例**：
```
GET /common/countries
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "code": "CN",
      "name": "中国",
      "nameEn": "China",
      "dialCode": "+86",
      "flag": "🇨🇳"
    },
    {
      "code": "US",
      "name": "美国",
      "nameEn": "United States",
      "dialCode": "+1",
      "flag": "🇺🇸"
    },
    {
      "code": "GB",
      "name": "英国",
      "nameEn": "United Kingdom",
      "dialCode": "+44",
      "flag": "🇬🇧"
    },
    {
      "code": "JP",
      "name": "日本",
      "nameEn": "Japan",
      "dialCode": "+81",
      "flag": "🇯🇵"
    },
    {
      "code": "KR",
      "name": "韩国",
      "nameEn": "South Korea",
      "dialCode": "+82",
      "flag": "🇰🇷"
    }
  ]
}
```

**响应字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| code | String | 国家代码（ISO 3166-1 alpha-2） |
| name | String | 国家/地区名称（中文） |
| nameEn | String | 国家/地区名称（英文） |
| dialCode | String | 电话区号（包含+号） |
| flag | String | 国旗emoji |

---

### 2. 获取时区列表

**接口地址**：`GET /common/timezones`

**接口描述**：返回支持的时区列表

**请求参数**：无

**请求示例**：
```
GET /common/timezones
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "timezone": "Asia/Shanghai",
      "name": "中国标准时间",
      "offset": "+08:00"
    },
    {
      "timezone": "America/New_York",
      "name": "美国东部时间",
      "offset": "-05:00"
    },
    {
      "timezone": "Europe/London",
      "name": "格林威治标准时间",
      "offset": "+00:00"
    }
  ]
}
```

---

### 3. 获取语言列表

**接口地址**：`GET /common/languages`

**接口描述**：返回支持的语言列表

**请求参数**：无

**请求示例**：
```
GET /common/languages
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "code": "zh-CN",
      "name": "简体中文",
      "nameEn": "Simplified Chinese",
      "nativeName": "简体中文"
    },
    {
      "code": "en-US",
      "name": "英语（美国）",
      "nameEn": "English (United States)",
      "nativeName": "English"
    },
    {
      "code": "ja-JP",
      "name": "日语",
      "nameEn": "Japanese",
      "nativeName": "日本語"
    },
    {
      "code": "ko-KR",
      "name": "韩语",
      "nameEn": "Korean",
      "nativeName": "한국어"
    }
  ]
}
```

---

## 注意事项

- 所有接口均为公开接口，无需认证
- 数据为静态数据，可根据实际需求扩展
- 国家代码遵循ISO 3166-1 alpha-2标准
- 时区遵循IANA时区数据库标准
- 语言代码遵循RFC 5646标准

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
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














