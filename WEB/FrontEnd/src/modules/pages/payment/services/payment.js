class PaymentService {
  constructor() {
    this.config = {
      baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8085/api',
      pollInterval: 3000,
      timeout: 300000
    }
  }

  async createWechatOrder(orderData) {
    try {
      const response = await this.request('/payment/wechat/create', {
        method: 'POST',
        body: JSON.stringify({
          ...orderData,
          trade_type: 'NATIVE',
          spbill_create_ip: '127.0.0.1'
        })
      })

      return this.handleResponse(response)
    } catch (error) {
      console.error('创建微信支付订单失败:', error)
      throw error
    }
  }

  async createAlipayOrder(orderData) {
    try {
      const response = await this.request('/payment/alipay/create', {
        method: 'POST',
        body: JSON.stringify({
          ...orderData,
          product_code: 'FAST_INSTANT_TRADE_PAY'
        })
      })

      return this.handleResponse(response)
    } catch (error) {
      console.error('创建支付宝订单失败:', error)
      throw error
    }
  }

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
              break
          }
        }

        this.pollingTimer = setTimeout(poll, this.config.pollInterval)

      } catch (error) {
        onError && onError(error)
      }
    }

    this.pollingTimer = setTimeout(poll, this.config.pollInterval)

    return {
      stop: () => {
        if (this.pollingTimer) {
          clearTimeout(this.pollingTimer)
          this.pollingTimer = null
        }
      }
    }
  }

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

  async handleResponse(response) {
    if (!response.ok) {
      const error = new Error(`HTTP ${response.status}: ${response.statusText}`)
      error.response = response
      throw error
    }

    const data = await response.json()

    if (data.code !== 'SUCCESS' && data.code !== 200) {
      throw new Error(data.message || '请求失败')
    }

    return {
      success: true,
      data: data.data || data,
      message: data.message
    }
  }

  getToken() {
    return localStorage.getItem('Token') || ''
  }

  generateOrderNo() {
    const timestamp = Date.now()
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
    return `PAY${timestamp}${random}`
  }

  formatAmountToFen(amount) {
    return Math.round(amount * 100)
  }

  formatAmountToYuan(amount) {
    return (amount / 100).toFixed(2)
  }
}

const paymentService = new PaymentService()

export default paymentService

export { PaymentService }