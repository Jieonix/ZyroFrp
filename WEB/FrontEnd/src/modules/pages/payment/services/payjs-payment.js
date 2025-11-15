class PayJSPaymentService {
  constructor() {
    this.config = {
      baseURL: 'https://api.mch.weixin.qq.com',
      mchid: '',
      key: '',
      notify_url: '',
      pollInterval: 3000,
      timeout: 300000
    }
  }

  init(config) {
    this.config = { ...this.config, ...config }
  }

  async createOrder(orderData) {
    try {
      const params = {
        mchid: this.config.mchid,
        total_fee: orderData.amount,
        out_trade_no: orderData.orderNo,
        body: orderData.subject,
        attach: orderData.attach || '',
        notify_url: this.config.notify_url,
        type: 'native',
        auto: '1'
      }

      params.sign = this.generateSign(params)

      const response = await fetch(`${this.config.baseURL}/pay`, {
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

  async queryOrder(orderNo) {
    try {
      const params = {
        mchid: this.config.mchid,
        out_trade_no: orderNo
      }

      params.sign = this.generateSign(params)

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
            trade_state: data.trade_state,
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

  generateSign(params) {
    const filteredParams = {}
    Object.keys(params).sort().forEach(key => {
      if (params[key] && key !== 'sign') {
        filteredParams[key] = params[key]
      }
    })

    const stringA = Object.keys(filteredParams)
      .map(key => `${key}=${filteredParams[key]}`)
      .join('&')

    const stringSignTemp = `${stringA}&key=${this.config.key}`

    return this.md5(stringSignTemp).toUpperCase()
  }

  md5(str) {
    return this.simpleMD5(str)
  }

  simpleMD5(str) {
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

    return str
  }

  toQueryString(params) {
    return Object.keys(params)
      .map(key => `${key}=${encodeURIComponent(params[key])}`)
      .join('&')
  }

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

  generateOrderNo() {
    const timestamp = Date.now()
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
    return `PAYJS${timestamp}${random}`
  }

  formatAmountToFen(amount) {
    return Math.round(amount * 100)
  }

  formatAmountToYuan(amount) {
    return (amount / 100).toFixed(2)
  }

  verifySign(params) {
    const receivedSign = params.sign
    const calculatedSign = this.generateSign(params)
    return receivedSign === calculatedSign
  }
}

const payJSPaymentService = new PayJSPaymentService()

export default payJSPaymentService

export { PayJSPaymentService }