/**
 * PayJS支付工具类 - 专为个人开发者设计
 * 官网：https://payjs.cn/
 * 费率：0.38% + 0.1%提现费
 */

class PayJSPaymentService {
  constructor() {
    this.config = {
      // PayJS API基础URL
      baseURL: 'https://api.mch.weixin.qq.com',
      // 从PayJS后台获取的配置
      mchid: '',        // 商户号
      key: '',          // API密钥
      notify_url: '',   // 异步通知地址
      // 支付状态轮询间隔（毫秒）
      pollInterval: 3000,
      // 支付超时时间（毫秒）
      timeout: 300000 // 5分钟
    }
  }

  /**
   * 初始化配置
   * @param {Object} config 配置参数
   */
  init(config) {
    this.config = { ...this.config, ...config }
  }

  /**
   * 创建支付订单
   * @param {Object} orderData 订单信息
   * @param {string} orderData.subject 商品描述
   * @param {number} orderData.amount 支付金额（分）
   * @param {string} orderData.orderNo 商户订单号
   * @param {string} orderData.attach 附加数据（可选）
   * @returns {Promise}
   */
  async createOrder(orderData) {
    try {
      const params = {
        mchid: this.config.mchid,
        total_fee: orderData.amount,
        out_trade_no: orderData.orderNo,
        body: orderData.subject,
        attach: orderData.attach || '',
        notify_url: this.config.notify_url,
        type: 'native', // 扫码支付
        auto: '1'      // 自动提交
      }

      // 生成签名
      params.sign = this.generateSign(params)

      // 发送请求
      const response = await fetch(`${this.config.baseURL}/pay`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: this.toQueryString(params)
      })

      const result = await response.text()

      // 解析返回数据
      const data = this.parseUrlParams(result)

      if (data.return_code === 1) {
        return {
          success: true,
          data: {
            code_url: data.code_url,
            out_trade_no: data.out_trade_no,
            payjs_order_id: data.payjs_order_id
          }
        }
      } else {
        throw new Error(data.msg || '创建订单失败')
      }

    } catch (error) {
      console.error('创建PayJS订单失败:', error)
      throw error
    }
  }

  /**
   * 查询订单状态
   * @param {string} orderNo 商户订单号
   * @returns {Promise}
   */
  async queryOrder(orderNo) {
    try {
      const params = {
        mchid: this.config.mchid,
        out_trade_no: orderNo
      }

      // 生成签名
      params.sign = this.generateSign(params)

      // 发送请求
      const response = await fetch(`${this.config.baseURL}/query`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: this.toQueryString(params)
      })

      const result = await response.text()
      const data = this.parseUrlParams(result)

      if (data.return_code === 1) {
        return {
          success: true,
          data: {
            out_trade_no: data.out_trade_no,
            payjs_order_id: data.payjs_order_id,
            transaction_id: data.transaction_id,
            total_fee: parseInt(data.total_fee),
            pay_time: data.pay_time,
            trade_state: data.trade_state, // SUCCESS, REFUND, NOTPAY, CLOSED, USERPAYING
            openid: data.openid
          }
        }
      } else {
        throw new Error(data.msg || '查询订单失败')
      }

    } catch (error) {
      console.error('查询PayJS订单失败:', error)
      throw error
    }
  }

  /**
   * 关闭订单
   * @param {string} orderNo 商户订单号
   * @returns {Promise}
   */
  async closeOrder(orderNo) {
    try {
      const params = {
        mchid: this.config.mchid,
        out_trade_no: orderNo
      }

      params.sign = this.generateSign(params)

      const response = await fetch(`${this.config.baseURL}/close`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: this.toQueryString(params)
      })

      const result = await response.text()
      const data = this.parseUrlParams(result)

      if (data.return_code === 1) {
        return {
          success: true,
          message: data.msg || '订单关闭成功'
        }
      } else {
        throw new Error(data.msg || '关闭订单失败')
      }

    } catch (error) {
      console.error('关闭PayJS订单失败:', error)
      throw error
    }
  }

  /**
   * 轮询支付状态
   * @param {string} orderNo 订单号
   * @param {Function} onSuccess 支付成功回调
   * @param {Function} onError 支付失败回调
   * @param {Function} onTimeout 支付超时回调
   * @returns {Object} 返回控制对象
   */
  pollPaymentStatus(orderNo, onSuccess, onError, onTimeout) {
    let pollCount = 0
    const maxPolls = Math.floor(this.config.timeout / this.config.pollInterval)

    const poll = async () => {
      try {
        pollCount++

        if (pollCount > maxPolls) {
          onTimeout && onTimeout()
          return
        }

        const result = await this.queryOrder(orderNo)

        if (result.success) {
          switch (result.data.trade_state) {
            case 'SUCCESS':
              onSuccess && onSuccess(result.data)
              return
            case 'CLOSED':
              onError && onError(new Error('订单已关闭'))
              return
            case 'REFUND':
              onError && onError(new Error('订单已退款'))
              return
            case 'NOTPAY':
            case 'USERPAYING':
              // 继续轮询
              break
          }
        }

        // 继续轮询
        this.pollingTimer = setTimeout(poll, this.config.pollInterval)

      } catch (error) {
        onError && onError(error)
      }
    }

    // 开始轮询
    this.pollingTimer = setTimeout(poll, this.config.pollInterval)

    // 返回控制对象
    return {
      stop: () => {
        if (this.pollingTimer) {
          clearTimeout(this.pollingTimer)
          this.pollingTimer = null
        }
      }
    }
  }

  /**
   * 生成签名
   * @param {Object} params 待签名参数
   * @returns {string} 签名结果
   */
  generateSign(params) {
    // 过滤空值和sign字段
    const filteredParams = {}
    Object.keys(params).sort().forEach(key => {
      if (params[key] && key !== 'sign') {
        filteredParams[key] = params[key]
      }
    })

    // 构建签名字符串
    const stringA = Object.keys(filteredParams)
      .map(key => `${key}=${filteredParams[key]}`)
      .join('&')

    // 添加key
    const stringSignTemp = `${stringA}&key=${this.config.key}`

    // MD5加密并转大写
    return this.md5(stringSignTemp).toUpperCase()
  }

  /**
   * MD5加密（需要引入MD5库）
   * @param {string} str 待加密字符串
   * @returns {string} MD5结果
   */
  md5(str) {
    // 这里需要引入MD5库，如crypto-js
    // import md5 from 'crypto-js/md5'
    // return md5(str).toString()

    // 简单的MD5实现（仅用于演示，建议使用专业库）
    return this.simpleMD5(str)
  }

  /**
   * 简单MD5实现（不推荐用于生产环境）
   */
  simpleMD5(str) {
    // 生产环境请使用专业的MD5库
    // 这里只是一个占位实现
    function rotateLeft(lValue, iShiftBits) {
      return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits))
    }

    function addUnsigned(lX, lY) {
      const lX4 = (lX & 0x40000000)
      const lY4 = (lY & 0x40000000)
      const lX8 = (lX & 0x80000000)
      const lY8 = (lY & 0x80000000)

      const lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF)
      if (lX4 & lY4) {
        return (lResult ^ 0x80000000 ^ lX8 ^ lY8)
      }
      if (lX4 | lY4) {
        if (lResult & 0x40000000) {
          return (lResult ^ 0xC0000000 ^ lX8 ^ lY8)
        } else {
          return (lResult ^ 0x40000000 ^ lX8 ^ lY8)
        }
      } else {
        return (lResult ^ lX8 ^ lY8)
      }
    }

    // 省略完整MD5实现...
    return str
  }

  /**
   * 对象转查询字符串
   * @param {Object} params 参数对象
   * @returns {string} 查询字符串
   */
  toQueryString(params) {
    return Object.keys(params)
      .map(key => `${key}=${encodeURIComponent(params[key])}`)
      .join('&')
  }

  /**
   * 解析URL参数
   * @param {string} urlParams URL参数字符串
   * @returns {Object} 参数对象
   */
  parseUrlParams(urlParams) {
    const params = {}
    urlParams.split('&').forEach(param => {
      const [key, value] = param.split('=')
      if (key && value) {
        params[key] = decodeURIComponent(value)
      }
    })
    return params
  }

  /**
   * 生成订单号
   * @returns {string}
   */
  generateOrderNo() {
    const timestamp = Date.now()
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
    return `PAYJS${timestamp}${random}`
  }

  /**
   * 格式化金额（元转分）
   * @param {number} amount 元为单位的金额
   * @returns {number} 分为单位的金额
   */
  formatAmountToFen(amount) {
    return Math.round(amount * 100)
  }

  /**
   * 格式化金额（分转元）
   * @param {number} amount 分为单位的金额
   * @returns {number} 元为单位的金额
   */
  formatAmountToYuan(amount) {
    return (amount / 100).toFixed(2)
  }

  /**
   * 验证回调签名
   * @param {Object} params 回调参数
   * @returns {boolean} 验证结果
   */
  verifySign(params) {
    const receivedSign = params.sign
    const calculatedSign = this.generateSign(params)
    return receivedSign === calculatedSign
  }
}

// 创建单例
const payJSPaymentService = new PayJSPaymentService()

export default payJSPaymentService

// 导出类，如果需要创建多个实例
export { PayJSPaymentService }