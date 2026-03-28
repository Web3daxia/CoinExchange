# Web3钱包模块API文档

## 基础信息

- **模块名称**：Web3钱包模块（Web3 Wallet）
- **用户端基础路径**：`/api/web3-wallet`
- **后台管理基础路径**：`/admin/web3-wallet`
- **认证方式**：Bearer Token (JWT)

---

## 用户端API

### 1. 钱包管理

#### 1.1 创建Web3钱包

**接口地址**：`POST /api/web3-wallet/create`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "walletType": "ETH",
  "password": "WalletPassword123!"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| walletType | String | 是 | 钱包类型：ETH（以太坊）, BTC（比特币）, TRX（波场） |
| password | String | 是 | 钱包密码 |

**响应示例**：
```json
{
  "code": 200,
  "message": "钱包创建成功，请妥善保管私钥",
  "data": {
    "walletId": 1,
    "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "walletType": "ETH",
    "privateKey": "0x...",
    "mnemonic": "word1 word2 word3 ...",
    "createTime": "2024-01-01T12:00:00",
    "warning": "请妥善保管私钥和助记词，丢失后无法找回"
  }
}
```

**注意**：私钥和助记词仅在创建时返回一次，请用户妥善保管。

#### 1.2 导入Web3钱包

**接口地址**：`POST /api/web3-wallet/import`

**请求参数**：
```json
{
  "walletType": "ETH",
  "privateKey": "0x...",
  "password": "WalletPassword123!"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| walletType | String | 是 | 钱包类型 |
| privateKey | String | 条件 | 私钥（私钥导入时必填） |
| mnemonic | String | 条件 | 助记词（助记词导入时必填） |
| password | String | 是 | 钱包密码 |

#### 1.3 获取我的Web3钱包列表

**接口地址**：`GET /api/web3-wallet/my-wallets`

**请求头**：
```
X-User-Id: 1
```

**请求示例**：
```
GET /api/web3-wallet/my-wallets
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "walletId": 1,
      "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
      "walletType": "ETH",
      "balance": "1.50000000",
      "balanceUsd": "3750.00000000",
      "createTime": "2024-01-01T12:00:00"
    }
  ]
}
```

#### 1.4 获取钱包余额

**接口地址**：`GET /api/web3-wallet/{walletId}/balance`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| walletId | Long | 是 | 钱包ID |

**请求示例**：
```
GET /api/web3-wallet/1/balance
```

**响应示例**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "walletId": 1,
    "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "walletType": "ETH",
    "balance": "1.50000000",
    "balanceUsd": "3750.00000000",
    "tokens": [
      {
        "tokenAddress": "0x...",
        "tokenSymbol": "USDT",
        "tokenName": "Tether USD",
        "balance": "1000.00000000",
        "balanceUsd": "1000.00000000",
        "decimals": 6
      }
    ],
    "updateTime": "2024-01-01T12:00:00"
  }
}
```

---

### 2. 转账管理

#### 2.1 发送转账

**接口地址**：`POST /api/web3-wallet/{walletId}/transfer`

**请求头**：
```
X-User-Id: 1
Content-Type: application/json
```

**请求参数**：
```json
{
  "toAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
  "amount": "0.10000000",
  "tokenAddress": null,
  "gasPrice": "20000000000",
  "gasLimit": "21000",
  "password": "WalletPassword123!"
}
```

**参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| toAddress | String | 是 | 接收地址 |
| amount | String | 是 | 转账金额 |
| tokenAddress | String | 否 | 代币合约地址（转账代币时必填，转账主币时为空） |
| gasPrice | String | 否 | Gas价格（可选，系统自动估算） |
| gasLimit | String | 否 | Gas限制（可选，系统自动估算） |
| password | String | 是 | 钱包密码 |

**响应示例**：
```json
{
  "code": 200,
  "message": "转账成功",
  "data": {
    "txHash": "0x...",
    "fromAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "toAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "amount": "0.10000000",
    "gasUsed": "21000",
    "gasPrice": "20000000000",
    "status": "PENDING",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

#### 2.2 查询转账记录

**接口地址**：`GET /api/web3-wallet/{walletId}/transactions`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| walletId | Long | 是 | 钱包ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

**请求示例**：
```
GET /api/web3-wallet/1/transactions?page=0&size=20
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
        "txHash": "0x...",
        "fromAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
        "toAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
        "amount": "0.10000000",
        "tokenAddress": null,
        "tokenSymbol": "ETH",
        "status": "SUCCESS",
        "blockNumber": 12345678,
        "confirmations": 12,
        "gasUsed": "21000",
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

#### 2.3 查询转账状态

**接口地址**：`GET /api/web3-wallet/transaction/{txHash}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| txHash | String | 是 | 交易哈希 |

---

### 3. 代币管理

#### 3.1 添加代币

**接口地址**：`POST /api/web3-wallet/{walletId}/token/add`

**请求参数**：
```json
{
  "tokenAddress": "0x...",
  "tokenSymbol": "USDT",
  "tokenName": "Tether USD",
  "decimals": 6
}
```

#### 3.2 移除代币

**接口地址**：`DELETE /api/web3-wallet/{walletId}/token/{tokenAddress}`

#### 3.3 获取代币列表

**接口地址**：`GET /api/web3-wallet/{walletId}/tokens`

---

## 后台管理API

### 1. 钱包管理

#### 1.1 获取所有Web3钱包

**接口地址**：`GET /admin/web3-wallet/list`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 否 | 用户ID筛选 |
| walletType | String | 否 | 钱包类型筛选 |
| page | Integer | 否 | 页码（默认0） |
| size | Integer | 否 | 每页大小（默认20） |

---

## 钱包类型说明

- **ETH**：以太坊钱包（支持ERC20代币）
- **BTC**：比特币钱包
- **TRX**：波场钱包（支持TRC20代币）

## 转账状态说明

- **PENDING**：待确认
- **SUCCESS**：成功
- **FAILED**：失败

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














