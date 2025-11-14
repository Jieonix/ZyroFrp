/**
 * 支付工具类
 * 支持微信支付和支付宝的统一接口
 */

class PaymentService {
  constructor() {
    this.config = {
      // 后端API基础URL
      baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8085/api',
      // 支付状态轮询间隔（毫秒）
      pollInterval: 3000,
      // 支付超时时间（毫秒）
      timeout: 300000 // 5分钟
    }
  }

  /**
   * 创建微信支付订单
   * @param {Object} orderData 订单信息
   * @param {string} orderData.subject 商品描述
   * @param {number} orderData.amount 支付金额（分）
   * @param {string} orderData.orderNo 商户订单号
   * @param {string} orderData.notifyUrl 异步通知地址
   * @returns {Promise}
   */
  async createWechatOrder(orderData) {
    try {
      const response = await this.request('/payment/wechat/create', {
        method: 'POST',
        body: JSON.stringify({
          ...orderData,
          trade_type: 'NATIVE', // 扫码支付
          spbill_create_ip: '127.0.0.1'
        })
      })

      return this.handleResponse(response)
    } catch (error) {
      console.error('创建微信支付订单失败:', error)
      throw error
    }
  }

  /**
   * 创建支付宝订单
   * @param {Object} orderData 订单信息
   * @param {string} orderData.subject 商品描述
   * @param {number} orderData.amount 支付金额（元）
   * @param {string} orderData.orderNo 商户订单号
   * @param {string} orderData.notifyUrl 异步通知地址
   * @returns {Promise}
   */
  async createAlipayOrder(orderData) {
    try {
      const response = await this.request('/payment/alipay/create', {
        method: 'POST',
        body: JSON.stringify({
          ...orderData,
          product_code: 'FAST_INSTANT_TRADE_PAY' // 即时到账
        })
      })

      return this.handleResponse(response)
    } catch (error) {
      console.error('创建支付宝订单失败:', error)
      throw error
    }
  }

  /**
   * 查询支付状态
   * @param {string} orderNo 订单号
   * @param {string} type 支付类型 'wechat' | 'alipay'
   * @returns {Promise}
   */
  async queryPaymentStatus(orderNo, type) {
    try {
      const response = await this.request(`/payment/${type}/query/${orderNo}`, {
        method: 'GET'
      })

      return this.handleResponse(response)
    } catch (error) {
      console.error('查询支付状态失败:', error)
      throw error
    }
  }

  /**
   * 轮询支付状态
   * @param {string} orderNo 订单号
   * @param {string} type 支付类型
   * @param {Function} onSuccess 支付成功回调
   * @param {Function} onError 支付失败回调
   * @param {Function} onTimeout 支付超时回调
   * @returns {Object} 返回控制对象，包含停止轮询的方法
   */
  pollPaymentStatus(orderNo, type, onSuccess, onError, onTimeout) {
    let pollCount = 0
    const maxPolls = Math.floor(this.config.timeout / this.config.pollInterval)

    const poll = async () => {
      try {
        pollCount++

        if (pollCount > maxPolls) {
          onTimeout && onTimeout()
          return
        }

        const result = await this.queryPaymentStatus(orderNo, type)

        if (result.success) {
          switch (result.data.trade_state) {
            case 'SUCCESS':
            case 'TRADE_SUCCESS':
              onSuccess && onSuccess(result.data)
              return
            case 'CLOSED':
            case 'TRADE_CLOSED':
              onError && onError(new Error('订单已关闭'))
              return
            case 'REFUND':
              onError && onError(new Error('订单已退款'))
              return
            case 'NOTPAY':
            case 'WAIT_BUYER_PAY':
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
   * 发起网络请求
   * @param {string} url 请求地址
   * @param {Object} options 请求选项
   * @returns {Promise}
   */
  async request(url, options = {}) {
    const fullUrl = `${this.config.baseURL}${url}`

    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.getToken()}`
      }
    }

    const finalOptions = {
      ...defaultOptions,
      ...options,
      headers: {
        ...defaultOptions.headers,
        ...options.headers
      }
    }

    return fetch(fullUrl, finalOptions)
  }

  /**
   * 处理响应
   * @param {Response} response 响应对象
   * @returns {Promise}
   */
  async handleResponse(response) {
    if (!response.ok) {
      const error = new Error(`HTTP ${response.status}: ${response.statusText}`)
      error.response = response
      throw error
    }

    const data = await response.json()

    // 根据您的后端API响应格式进行调整
    if (data.code !== 'SUCCESS' && data.code !== 200) {
      throw new Error(data.message || '请求失败')
    }

    return {
      success: true,
      data: data.data || data,
      message: data.message
    }
  }

  /**
   * 获取认证token
   * @returns {string}
   */
  getToken() {
    return localStorage.getItem('Token') || ''
  }

  /**
   * 生成订单号
   * @returns {string}
   */
  generateOrderNo() {
    const timestamp = Date.now()
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
    return `PAY${timestamp}${random}`
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
}

// 创建单例
const paymentService = new PaymentService()

export default paymentService

// 导出类，如果需要创建多个实例
export { PaymentService }