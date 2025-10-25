# 支付接口集成指南

## 微信支付接口获取

### 1. 注册微信支付商户
- 访问 [微信支付商户平台](https://pay.weixin.qq.com/)
- 注册商户账号（需要企业营业执照）
- 完成实名认证和资质审核

### 2. 申请支付产品
在商户平台申请以下支付产品：
- **Native支付**：用于扫码支付（电脑网站）
- **H5支付**：用于手机浏览器支付
- **JSAPI支付**：用于微信内浏览器支付

### 3. 获取API密钥
- 登录商户平台 → 账户中心 → API安全
- 设置API密钥（32位）
- 下载API证书

### 4. 开发配置
- **商户号**：mch_id
- **APPID**：公众号或小程序APPID
- **API密钥**：key
- **证书**：apiclient_cert.pem、apiclient_key.pem

### 5. 微信支付API文档
- [Native支付文档](https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml)
- [APIv3接口文档](https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter1_1_1.shtml)

## 支付宝接口获取

### 1. 注册支付宝开发者账号
- 访问 [支付宝开放平台](https://open.alipay.com/)
- 注册企业开发者账号
- 完成实名认证

### 2. 创建应用
- 开放平台 → 应用管理 → 创建应用
- 选择"网页&移动应用"
- 填写应用信息并上传营业执照

### 3. 申请支付产品
在应用中添加以下产品：
- **当面付**：用于扫码支付
- **手机网站支付**：用于手机浏览器
- **电脑网站支付**：用于PC网站

### 4. 获取密钥和证书
- 应用详情 → 开发设置 → 接口加签方式
- 设置公钥（生成RSA密钥对）
- 获取支付宝公钥
- 下载应用证书

### 5. 开发配置
- **APPID**：应用ID
- **商户私钥**：private_key
- **支付宝公钥**：alipay_public_key
- **应用证书**：appCertPublicKey.crt
- **支付宝根证书**：alipayRootCert.crt

### 6. 支付宝API文档
- [当面付文档](https://opendocs.alipay.com/open/194/106078)
- [支付能力文档](https://opendocs.alipay.com/open/203/105286)

## 第三方支付服务商推荐

如果您觉得直接申请官方接口复杂，可以考虑以下第三方支付服务商：

### 1. 易宝支付 (Yeepay)
- 网址：https://www.yeepay.com/
- 特点：一站式接入微信、支付宝
- 费率：0.38% - 0.6%

### 2. 联动优势 (UMF)
- 网址：https://www.umf.com.cn/
- 特点：聚合支付，支持多种支付方式
- 费率：0.4% - 0.6%

### 3. 汇付天下 (Chinapay)
- 网址：https://www.chinapay.com/
- 特点：上市公司，服务稳定
- 费率：0.38% - 0.55%

## 前端集成示例代码

### 微信支付Native（扫码支付）
```javascript
// 1. 调用后端接口生成支付二维码
async function createWechatOrder(orderData) {
  try {
    const response = await fetch('/api/payment/wechat/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
    });

    const result = await response.json();

    if (result.code === 'SUCCESS') {
      // 显示二维码
      showQRCode(result.data.code_url);
      // 开始轮询支付状态
      pollPaymentStatus(result.data.out_trade_no);
    }
  } catch (error) {
    console.error('创建微信支付订单失败:', error);
  }
}
```

### 支付宝扫码支付
```javascript
// 1. 调用后端接口生成支付二维码
async function createAlipayOrder(orderData) {
  try {
    const response = await fetch('/api/payment/alipay/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
    });

    const result = await response.json();

    if (result.code === 'SUCCESS') {
      // 显示二维码
      showQRCode(result.data.qr_code);
      // 开始轮询支付状态
      pollPaymentStatus(result.data.out_trade_no);
    }
  } catch (error) {
    console.error('创建支付宝订单失败:', error);
  }
}
```

### 轮询支付状态
```javascript
function pollPaymentStatus(orderNo) {
  const interval = setInterval(async () => {
    try {
      const response = await fetch(`/api/payment/status/${orderNo}`);
      const result = await response.json();

      if (result.data.trade_state === 'SUCCESS') {
        clearInterval(interval);
        // 支付成功，跳转到成功页面
        window.location.href = '/payment/success';
      }
    } catch (error) {
      console.error('查询支付状态失败:', error);
    }
  }, 3000); // 每3秒查询一次
}
```

## 安全注意事项

1. **HTTPS必须**：生产环境必须使用HTTPS
2. **签名验证**：所有回调必须验证签名
3. **订单状态**：后端必须维护订单状态，防止重复支付
4. **金额校验**：回调时必须校验订单金额
5. **日志记录**：记录所有支付相关的操作日志

## 费用说明

### 微信支付
- **费率**：0.6%
- **结算周期**：T+1（次日结算）
- **最低手续费**：无

### 支付宝
- **费率**：0.55% - 0.6%
- **结算周期**：T+1（次日结算）
- **最低手续费**：无

### 第三方支付
- **费率**：0.38% - 0.6%
- **结算周期**：T+1到T+7不等
- **服务费**：部分平台收取年服务费

## 推荐实施步骤

1. **调研选择**：根据业务规模选择官方接口或第三方服务商
2. **申请账号**：注册并申请支付权限
3. **开发测试**：在沙箱环境进行开发和测试
4. **签约上线**：完成签约后切换到生产环境
5. **监控运维**：建立支付监控和异常处理机制

## 技术支持

如果在接入过程中遇到问题，可以联系：
- 微信支付客服：95188
- 支付宝客服：95188
- 第三方支付服务商技术支持