# 钱包模块 API 文档

## 模块说明
钱包模块提供资产查询、充值、提现、资产划转等功能。

## Base URL
- 钱包接口: `/wallet`
- 充值接口: `/deposit`
- 提现接口: `/withdraw`

## 认证
所有接口需要认证，请在请求头中携带：
```
X-User-Id: {userId}
Authorization: Bearer {token}
```

---

## 1. 钱包余额查询

### 1.1 查询钱包余额

**接口地址**: `GET /wallet/balance`

**接口描述**: 查询用户各账户的资产余额

**请求头**:
```
X-User-Id: 1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "spot": {
      "USDT": 10000.00,
      "BTC": 1.5,
      "ETH": 10.0
    },
    "futures": {
      "USDT": 5000.00
    },
    "totalValue": 15000.00
  }
}
```

### 1.2 查询所有账户余额

**接口地址**: `GET /wallet/balance`

**接口描述**: 查询用户钱包中各账户的资产余额（包括现货、合约、期权等）

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "SPOT": {
      "USDT": 10000.00,
      "BTC": 1.5
    },
    "FUTURES_USDT": {
      "USDT": 5000.00
    },
    "FUTURES_COIN": {
      "BTC": 0.5
    },
    "OPTIONS": {
      "USDT": 2000.00
    },
    "COPY_TRADING": {
      "USDT": 1000.00
    }
  }
}
```

### 1.3 获取资产汇总数据

**接口地址**: `GET /wallet/asset-summary`

**接口描述**: 获取资产汇总数据（用于图表展示）

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| period | String | 否 | 周期: REAL_TIME, DAILY, WEEKLY, MONTHLY（默认REAL_TIME） |
| currencyType | String | 否 | 货币类型: CRYPTO, FIAT（默认CRYPTO） |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalValue": 18000.00,
    "assetDistribution": [
      {
        "currency": "USDT",
        "amount": 17000.00,
        "percentage": 94.44
      },
      {
        "currency": "BTC",
        "amount": 1.0,
        "percentage": 5.56
      }
    ],
    "historyData": [
      {
        "date": "2024-01-01",
        "value": 15000.00
      }
    ]
  }
}
```

---

## 2. 资产划转

### 2.1 跨账户划转资产

**接口地址**: `POST /wallet/transfer`

**接口描述**: 在不同账户之间划转资产

**请求参数**:
```json
{
  "fromAccountType": "SPOT",
  "toAccountType": "FUTURES_USDT",
  "currency": "USDT",
  "amount": 1000.00,
  "remark": "划转到合约账户"
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| fromAccountType | String | 是 | 转出账户类型: SPOT, FUTURES_USDT, FUTURES_COIN, OPTIONS, COPY_TRADING |
| toAccountType | String | 是 | 转入账户类型 |
| currency | String | 是 | 币种 |
| amount | BigDecimal | 是 | 划转金额 |
| remark | String | 否 | 备注 |

**响应示例**:
```json
{
  "code": 200,
  "message": "划转成功",
  "data": {
    "transferId": 1234567890,
    "fromAccountType": "SPOT",
    "toAccountType": "FUTURES_USDT",
    "currency": "USDT",
    "amount": 1000.00,
    "status": "SUCCESS",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 2.2 查询资产划转历史

**接口地址**: `GET /wallet/transfer/history`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "fromAccountType": "SPOT",
      "toAccountType": "FUTURES_USDT",
      "currency": "USDT",
      "amount": 1000.00,
      "status": "SUCCESS",
      "remark": "划转到合约账户",
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### 2.3 确认资金划转

**接口地址**: `POST /wallet/transfer/confirm`

**接口描述**: 确认资金划转（需要安全验证）

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| transferId | Long | 是 | 划转ID |
| verificationCode | String | 是 | 验证码（邮箱/手机/2FA） |

---

## 3. 充值相关接口

### 3.1 查询充值地址

**接口地址**: `GET /deposit/addresses`

**接口描述**: 获取所有支持的充值地址和二维码

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种，如USDT |

**响应示例**:
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
      "minAmount": 10.0,
      "fee": 0.0
    },
    {
      "currency": "USDT",
      "chain": "ERC20",
      "address": "0xXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      "qrCode": "data:image/png;base64,...",
      "minAmount": 20.0,
      "fee": 5.0
    }
  ]
}
```

### 3.2 第三方充值

**接口地址**: `POST /deposit/third-party`

**接口描述**: 支持第三方充值，获取相关二维码（如优盾钱包）

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| amount | BigDecimal | 否 | 金额（可选） |

**响应示例**:
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

### 3.3 查询充值历史

**接口地址**: `GET /deposit/history`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "depositNo": "DEP202401010001",
      "userId": 1,
      "currency": "USDT",
      "chain": "TRC20",
      "amount": 1000.00,
      "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
      "txHash": "0x...",
      "status": "SUCCESS",
      "confirmations": 12,
      "createdAt": "2024-01-01T10:00:00",
      "completedAt": "2024-01-01T10:05:00"
    }
  ]
}
```

### 3.4 查询充值记录详情

**接口地址**: `GET /deposit/record/{depositNo}`

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| depositNo | String | 是 | 充值单号 |

---

## 4. 提现相关接口

### 4.1 提交提现请求

**接口地址**: `POST /withdraw/submit`

**接口描述**: 提交加密货币提现申请

**请求参数**:
```json
{
  "currency": "USDT",
  "chain": "TRC20",
  "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  "amount": 100.00,
  "addressId": 1
}
```

**字段说明**:
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| address | String | 是 | 提现地址 |
| amount | BigDecimal | 是 | 提现金额 |
| addressId | Long | 否 | 地址簿ID（如使用地址簿） |

**响应示例**:
```json
{
  "code": 200,
  "message": "提现申请已提交，请完成验证",
  "data": {
    "withdrawId": 1234567890,
    "withdrawNo": "WD202401010001",
    "status": "PENDING_VERIFY",
    "currency": "USDT",
    "amount": 100.00,
    "fee": 1.00,
    "actualAmount": 99.00
  }
}
```

### 4.2 提现验证

**接口地址**: `POST /withdraw/verify`

**接口描述**: 提交邮箱/手机验证码、谷歌验证码等进行提现验证

**请求参数**:
```json
{
  "withdrawId": 1234567890,
  "emailCode": "123456",
  "phoneCode": "123456",
  "googleCode": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证成功，提现已提交审核",
  "data": null
}
```

### 4.3 查询提现状态

**接口地址**: `GET /withdraw/status`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| withdrawNo | String | 是 | 提现单号 |

**响应示例**:
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
    "amount": 100.00,
    "fee": 1.00,
    "actualAmount": 99.00,
    "address": "TXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    "txHash": null,
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### 4.4 查询提现历史

**接口地址**: `GET /withdraw/history`

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "withdrawNo": "WD202401010001",
      "currency": "USDT",
      "chain": "TRC20",
      "amount": 100.00,
      "fee": 1.00,
      "actualAmount": 99.00,
      "status": "SUCCESS",
      "txHash": "0x...",
      "createdAt": "2024-01-01T10:00:00",
      "completedAt": "2024-01-01T11:00:00"
    }
  ]
}
```

### 4.5 查询提现限制

**接口地址**: `GET /withdraw/limit`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| amount | BigDecimal | 是 | 提现金额 |

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "minAmount": 10.0,
    "maxAmount": 50000.0,
    "dailyLimit": 100000.0,
    "dailyUsed": 5000.0,
    "fee": 1.0,
    "feeType": "FIXED",
    "estimatedArrivalTime": "1-5分钟"
  }
}
```

### 4.6 查询提现地址簿

**接口地址**: `GET /withdraw/addresses`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 否 | 币种（可选） |
| chain | String | 否 | 链类型（可选） |

**响应示例**:
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

### 4.7 添加提现地址

**接口地址**: `POST /withdraw/address`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| currency | String | 是 | 币种 |
| chain | String | 是 | 链类型 |
| address | String | 是 | 提现地址 |
| addressLabel | String | 否 | 地址标签 |

**响应示例**:
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

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 500 | 服务器内部错误 |

## 注意事项

1. 所有金额相关字段支持小数点后8位精度
2. 提现需要完成邮箱、手机或2FA验证
3. 充值地址会定期更新，建议每次充值前查询最新地址
4. 资产划转是实时到账的
5. 提现审核时间一般为1-3个工作日
6. 不同链的提现手续费不同，请查询后再提交
7. 地址簿最多保存20个地址














