# 钱包模块API文档

## 基础信息

- **模块名称**：钱包模块（Wallet）
- **用户端基础路径**：`/wallet`, `/deposit`, `/withdraw`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 钱包余额查询

#### 1.1 查询所有账户余额

**接口地址**：`GET /wallet/balance`

**账户类型说明**：
- **SPOT**：现货账户
- **FUTURES_USDT**：U本位合约账户
- **FUTURES_COIN**：币本位合约账户
- **DELIVERY**：交割合约账户（新增）
- **FINANCE**：理财账户（新增）
- **OPTIONS**：期权账户
- **COPY_TRADING**：跟单账户

**请求头**：
```
X-User-Id: 1
Authorization: Bearer {token}
```

**请求示例**：
```
GET /wallet/balance
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "SPOT": {
      "USDT": "10000.00000000",
      "BTC": "1.50000000",
      "ETH": "10.00000000"
    },
    "FUTURES_USDT": {
      "USDT": "5000.00000000"
    },
    "FUTURES_COIN": {
      "BTC": "0.50000000"
    },
    "DELIVERY": {
      "USDT": "3000.00000000",
      "BTC": "0.30000000"
    },
    "FINANCE": {
      "USDT": "5000.00000000",
      "BTC": "0.20000000"
    },
    "OPTIONS": {
      "USDT": "2000.00000000"
    },
    "COPY_TRADING": {
      "USDT": "1000.00000000"
    }
  }
}
```

#### 1.2 获取资产汇总数据

**接口地址**：`GET /wallet/asset-summary`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 周期：REAL_TIME（实时）, DAILY（每日）, WEEKLY（每周）, MONTHLY（每月），默认REAL_TIME |
| currencyType | String | 否 | 货币类型：CRYPTO（加密货币）, FIAT（法币），默认CRYPTO |

**请求示例**：
```
GET /wallet/asset-summary?period=REAL_TIME&currencyType=CRYPTO
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalValue": "18000.00000000",
    "assetDistribution": [
      {
        "currency": "USDT",
        "amount": "17000.00000000",
        "percentage": 94.44
      },
      {
        "currency": "BTC",
        "amount": "1.00000000",
        "percentage": 5.56
      }
    ],
    "historyData": [
      {
        "date": "2024-01-01",
        "value": "15000.00000000"
      }
    ]
  }
}
```

---

### 2. 资产划转

#### 2.1 跨账户划转资产

**接口地址**：`POST /wallet/transfer`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "fromAccountType": "SPOT",
  "toAccountType": "FUTURES_USDT",
  "currency": "USDT",
  "amount": "1000.00000000",
  "remark": "划转到合约账户"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fromAccountType | String | 是 | 转出账户类型：SPOT（现货）, FUTURES_USDT（U本位合约）, FUTURES_COIN（币本位合约）, DELIVERY（交割合约）, FINANCE（理财）, OPTIONS（期权）, COPY_TRADING（跟单） |
| toAccountType | String | 是 | 转入账户类型（同上） |
| currency | String | 是 | 币种 |
| amount | String | 是 | 划转金额 |
| remark | String | 否 | 备注 |

**响应示例**：
```json
{
  "code": 200,
  "message": "划转成功",
  "data": {
    "transferId": 1234567890,
    "transferNo": "WT-20240101120000-123456",
    "fromAccountType": "SPOT",
    "toAccountType": "FUTURES_USDT",
    "currency": "USDT",
    "amount": "1000.00000000",
    "status": "SUCCESS",
    "createdAt": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 查询资产划转历史

**接口地址**：`GET /wallet/transfer/history`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /wallet/transfer/history
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "transferNo": "WT-20240101120000-123456",
      "userId": 1,
      "fromAccountType": "SPOT",
      "toAccountType": "FUTURES_USDT",
      "currency": "USDT",
      "amount": "1000.00000000",
      "status": "SUCCESS",
      "remark": "划转到合约账户",
      "createdAt": "2024-01-01T12:00:00"
    }
  ]
}
```

#### 2.3 确认资金划转

**接口地址**：`POST /wallet/transfer/confirm`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| transferId | Long | 是 | 划转ID |
| verificationCode | String | 是 | 验证码（邮箱/手机/2FA） |

**请求示例**：
```
POST /wallet/transfer/confirm?transferId=1234567890&verificationCode=123456
```

---

### 3. 充值

#### 3.1 查询充值地址

**接口地址**：`GET /deposit/addresses`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种，如USDT |

**请求示例**：
```
GET /deposit/addresses?currency=USDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "currency": "USDT",
      "chain": "TRC20",
      "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      "qrCode": "data:image/png;base64,...",
      "minAmount": "10.00000000",
      "fee": "0.00000000",
      "confirmations": 12
    },
    {
      "currency": "USDT",
      "chain": "ERC20",
      "address": "0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      "qrCode": "data:image/png;base64,...",
      "minAmount": "20.00000000",
      "fee": "5.00000000",
      "confirmations": 12
    }
  ]
}
```

#### 3.2 第三方充值

**接口地址**：`POST /deposit/third-party`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| amount | String | 否 | 金额（可选） |

**请求示例**：
```
POST /deposit/third-party?currency=USDT&chain=TRC20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "currency": "USDT",
    "chain": "TRC20",
    "qrCode": "data:image/png;base64,...",
    "paymentUrl": "https://..."
  }
}
```

#### 3.3 查询充值历史

**接口地址**：`GET /deposit/history`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |
| status | String | 否 | 状态筛选：PENDING, SUCCESS, FAILED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /deposit/history?page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1,
        "depositNo": "DEP202401010001",
        "userId": 1,
        "currency": "USDT",
        "chain": "TRC20",
        "amount": "1000.00000000",
        "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "txHash": "0x...",
        "status": "SUCCESS",
        "confirmations": 12,
        "createdAt": "2024-01-01T10:00:00",
        "completedAt": "2024-01-01T10:05:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 3.4 查询充值记录详情

**接口地址**：`GET /deposit/record/{depositNo}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| depositNo | String | 是 | 充值单号 |

---

### 4. 提现

#### 4.1 提交提现请求

**接口地址**：`POST /withdraw/submit`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "currency": "USDT",
  "chain": "TRC20",
  "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  "amount": "100.00000000",
  "addressId": 1
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| address | String | 是 | 提现地址 |
| amount | String | 是 | 提现金额 |
| addressId | Long | 否 | 地址簿ID（如使用地址簿） |

**响应示例**：
```json
{
  "code": 200,
  "message": "提现申请已提交，请完成验证",
  "data": {
    "withdrawId": 1234567890,
    "withdrawNo": "WD202401010001",
    "status": "PENDING_VERIFY",
    "currency": "USDT",
    "amount": "100.00000000",
    "fee": "1.00000000",
    "actualAmount": "99.00000000"
  }
}
```

#### 4.2 提现验证

**接口地址**：`POST /withdraw/verify`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "withdrawId": 1234567890,
  "emailCode": "123456",
  "phoneCode": "123456",
  "googleCode": "123456"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| withdrawId | Long | 是 | 提现ID |
| emailCode | String | 条件 | 邮箱验证码 |
| phoneCode | String | 条件 | 手机验证码 |
| googleCode | String | 条件 | 谷歌验证码 |

**响应示例**：
```json
{
  "code": 200,
  "message": "验证成功，提现已提交审核",
  "data": null
}
```

#### 4.3 查询提现状态

**接口地址**：`GET /withdraw/status`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| withdrawNo | String | 是 | 提现单号 |

**请求示例**：
```
GET /withdraw/status?withdrawNo=WD202401010001
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "withdrawNo": "WD202401010001",
    "userId": 1,
    "currency": "USDT",
    "chain": "TRC20",
    "amount": "100.00000000",
    "fee": "1.00000000",
    "actualAmount": "99.00000000",
    "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    "txHash": null,
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

#### 4.4 查询提现历史

**接口地址**：`GET /withdraw/history`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种筛选 |
| status | String | 否 | 状态筛选：PENDING, SUCCESS, FAILED, REJECTED |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /withdraw/history?page=0&size=20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1,
        "withdrawNo": "WD202401010001",
        "currency": "USDT",
        "chain": "TRC20",
        "amount": "100.00000000",
        "fee": "1.00000000",
        "actualAmount": "99.00000000",
        "status": "SUCCESS",
        "txHash": "0x...",
        "createdAt": "2024-01-01T10:00:00",
        "completedAt": "2024-01-01T11:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 4.5 查询提现限制

**接口地址**：`GET /withdraw/limit`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |

**请求示例**：
```
GET /withdraw/limit?currency=USDT&chain=TRC20
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "minAmount": "10.00000000",
    "maxAmount": "50000.00000000",
    "dailyLimit": "100000.00000000",
    "dailyUsed": "5000.00000000",
    "fee": "1.00000000",
    "feeType": "FIXED",
    "estimatedArrivalTime": "1-5分钟"
  }
}
```

#### 4.6 查询提现地址簿

**接口地址**：`GET /withdraw/addresses`

**请求头**：
```
X-User-Id: 1
```

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种（可选） |
| chain | String | 否 | 链类型（可选） |

**请求示例**：
```
GET /withdraw/addresses?currency=USDT
```

**响应示例**：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "currency": "USDT",
      "chain": "TRC20",
      "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      "addressLabel": "我的钱包",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

#### 4.7 添加提现地址

**接口地址**：`POST /withdraw/address`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "currency": "USDT",
  "chain": "TRC20",
  "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  "addressLabel": "我的钱包"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| address | String | 是 | 提现地址 |
| addressLabel | String | 否 | 地址标签 |

**响应示例**：
```json
{
  "code": 200,
  "message": "地址添加成功",
  "data": {
    "id": 1,
    "currency": "USDT",
    "chain": "TRC20",
    "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    "addressLabel": "我的钱包",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

#### 4.8 删除提现地址

**接口地址**：`DELETE /withdraw/address/{addressId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| addressId | Long | 是 | 地址ID |

---

## 账户类型说明

- **SPOT**：现货账户
- **FUTURES_USDT**：U本位合约账户
- **FUTURES_COIN**：币本位合约账户
- **OPTIONS**：期权账户
- **COPY_TRADING**：跟单账户

## 充值/提现状态说明

- **PENDING**：待处理
- **PENDING_VERIFY**：待验证
- **SUCCESS**：成功
- **FAILED**：失败
- **REJECTED**：已拒绝
- **CANCELLED**：已取消

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

## 注意事项

1. 所有金额相关字段支持小数点后8位精度
2. 提现需要完成邮箱、手机或2FA验证
3. 充值地址会定期更新，建议每次充值前查询最新地址
4. 资产划转是实时到账的
5. 提现审核时间一般为1-3个工作日
6. 不同链的提现手续费不同，请查询后再提交
7. 地址簿最多保存20个地址

---

**文档版本**：v1.0  
**最后更新**：2024-01-01

