<template>
  <div class="payment-page">
    <Header />
    <div class="container">
      <div class="payment-container">
        <div class="payment-header">
          <h1>选择支付方式</h1>
          <p class="subtitle">专业版 - ¥{{ currentPrice }}/{{ billingText }}，享受更多高级功能</p>
        </div>

        <div class="payment-content">
          <div class="order-summary">
            <h2>订单详情</h2>
            <div class="plan-info">
              <div class="plan-name">专业版</div>
              <div class="plan-price">¥{{ currentPrice }}</div>
            </div>
            <div class="billing-period">{{ billingText === 'year' ? '每年计费' : '每月计费' }}</div>
            <div class="total">
              <span>总计</span>
              <span class="total-amount">¥{{ currentPrice }}</span>
            </div>
          </div>

          <div class="payment-methods">
            <h2>支付方式</h2>
            <div class="payment-options">
              <div
                class="payment-option"
                :class="{ active: selectedPayment === 'wechat' }"
                @click="selectPayment('wechat')"
              >
                <div class="payment-icon wechat-icon">
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 0 1 .213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 0 0 .167-.054l1.903-1.114a.864.864 0 0 1 .717-.098 10.16 10.16 0 0 0 2.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 4.882-1.786 7.262-.736-.847-3.206-4.468-5.49-8.495-5.49z"/>
                    <path d="M17.305 11.309c3.938 0 7.126 2.557 7.126 5.71 0 2.332-1.273 4.236-3.393 5.29l.364 1.361c.018.068.045.135.045.203 0 .15-.12.27-.268.27a.28.28 0 0 1-.153-.047l-1.748-1.023a.794.794 0 0 0-.66-.09 8.5 8.5 0 0 1-2.313.322c-3.938 0-7.126-2.557-7.126-5.71s3.188-5.71 7.126-5.71z"/>
                  </svg>
                </div>
                <div class="payment-info">
                  <div class="payment-name">微信支付</div>
                  <div class="payment-desc">使用微信扫码支付</div>
                </div>
                <div class="payment-radio">
                  <div class="radio-circle" :class="{ checked: selectedPayment === 'wechat' }"></div>
                </div>
              </div>

              <div
                class="payment-option"
                :class="{ active: selectedPayment === 'alipay' }"
                @click="selectPayment('alipay')"
              >
                <div class="payment-icon alipay-icon">
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.94-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/>
                  </svg>
                </div>
                <div class="payment-info">
                  <div class="payment-name">支付宝</div>
                  <div class="payment-desc">使用支付宝扫码支付</div>
                </div>
                <div class="payment-radio">
                  <div class="radio-circle" :class="{ checked: selectedPayment === 'alipay' }"></div>
                </div>
              </div>
            </div>

            <div class="payment-action">
              <button
                class="pay-button"
                :disabled="!selectedPayment || processing"
                @click="processPayment"
              >
                <span v-if="!processing">立即支付 ¥{{ currentPrice }}</span>
                <span v-else>处理中...</span>
              </button>
            </div>

            <!-- 二维码显示区域 -->
            <div v-if="showQRCode" class="qr-code-section">
              <div class="qr-code-container">
                <h3>请扫码支付</h3>
                <div class="qr-code">
                  <img :src="qrCodeUrl" alt="支付二维码" />
                </div>
                <div class="qr-code-info">
                  <p>请使用{{ selectedPayment === 'wechat' ? '微信' : '支付宝' }}扫描上方二维码完成支付</p>
                  <p class="order-no">订单号：{{ orderNo }}</p>
                </div>
                <div class="qr-code-actions">
                  <button class="retry-button" @click="retryPayment">重新生成</button>
                  <button class="cancel-button" @click="cancelPayment">取消支付</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="security-notice">
          <div class="security-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4z"/>
            </svg>
          </div>
          <div class="security-text">
            <div class="security-title">安全保障</div>
            <div class="security-desc">您的支付信息将被加密保护，支付过程安全可靠</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import paymentService from '@/utils/payment.js'

export default {
  name: 'Payment',
  components: {
    Header
  },
  data() {
    return {
      selectedPayment: 'wechat',
      processing: false,
      orderNo: '',
      qrCodeUrl: '',
      showQRCode: false,
      pollingController: null,
      billingPeriod: 'monthly', // monthly 或 yearly
      planData: {
        monthlyPrice: 29,
        yearlyPrice: 23
      }
    }
  },
  computed: {
    currentPrice() {
      return this.billingPeriod === 'yearly' ? this.planData.yearlyPrice : this.planData.monthlyPrice
    },
    billingText() {
      return this.billingPeriod === 'yearly' ? '年' : '月'
    }
  },
  mounted() {
    // 从URL参数获取计费周期
    const urlParams = new URLSearchParams(window.location.search)
    this.billingPeriod = urlParams.get('billing') || 'monthly'

    // 从URL参数获取价格信息
    const monthlyPrice = urlParams.get('monthly_price')
    const yearlyPrice = urlParams.get('yearly_price')

    if (monthlyPrice) this.planData.monthlyPrice = parseFloat(monthlyPrice)
    if (yearlyPrice) this.planData.yearlyPrice = parseFloat(yearlyPrice)
  },
  methods: {
    selectPayment(method) {
      this.selectedPayment = method
      this.showQRCode = false
      this.stopPolling()
    },

    async processPayment() {
      if (!this.selectedPayment || this.processing) return

      this.processing = true
      this.orderNo = paymentService.generateOrderNo()

      try {
        // 模拟处理，不执行真实支付
        await new Promise(resolve => setTimeout(resolve, 2000))

        // 显示处理完成信息
        console.log(`支付功能暂未开放，订单号：${this.orderNo}，价格：¥${this.currentPrice}/${this.billingText}`)

        // 可以在这里添加提示信息
        this.$message && this.$message.info('支付功能暂未开放')

      } catch (error) {
        console.error('支付处理失败:', error)
      } finally {
        this.processing = false
      }
    },

    startPaymentPolling() {
      this.pollingController = paymentService.pollPaymentStatus(
        this.orderNo,
        this.selectedPayment,
        (paymentData) => {
          // 支付成功
          this.stopPolling()
          this.$message.success('支付成功！')
          setTimeout(() => {
            this.$router.push('/Home')
          }, 1500)
        },
        (error) => {
          // 支付失败
          this.stopPolling()
          this.$message.error(`支付失败: ${error.message}`)
        },
        () => {
          // 支付超时
          this.stopPolling()
          this.$message.warning('支付超时，请重新发起支付')
        }
      )
    },

    stopPolling() {
      if (this.pollingController) {
        this.pollingController.stop()
        this.pollingController = null
      }
    },

    // 重新支付
    retryPayment() {
      this.showQRCode = false
      this.processPayment()
    },

    // 取消支付
    cancelPayment() {
      this.stopPolling()
      this.showQRCode = false
    }
  },

  beforeUnmount() {
    // 组件销毁时停止轮询
    this.stopPolling()
  }
}
</script>

<style scoped>
.payment-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
  padding-top: 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 24px;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.payment-container {
  max-width: 600px;
  width: 100%;
  margin: 0;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.payment-header {
  text-align: center;
  padding: 40px 40px 20px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.payment-header h1 {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.payment-content {
  padding: 40px;
}

.order-summary {
  margin-bottom: 40px;
}

.order-summary h2 {
  font-size: 18px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 20px;
}

.plan-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
  margin-bottom: 16px;
}

.plan-name {
  font-size: 16px;
  font-weight: 600;
  color: #0a0a0a;
}

.plan-price {
  font-size: 24px;
  font-weight: 700;
  color: #10b981;
}

.billing-period {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 20px;
}

.total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
  font-size: 16px;
  font-weight: 600;
  color: #0a0a0a;
}

.total-amount {
  font-size: 24px;
  font-weight: 700;
  color: #10b981;
}

.payment-methods h2 {
  font-size: 18px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 20px;
}

.payment-options {
  margin-bottom: 30px;
}

.payment-option {
  display: flex;
  align-items: center;
  padding: 20px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-option:hover {
  border-color: #10b981;
  background: rgba(16, 185, 129, 0.02);
}

.payment-option.active {
  border-color: #10b981;
  background: rgba(16, 185, 129, 0.05);
}

.payment-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.wechat-icon {
  background: #07c160;
  color: white;
}

.alipay-icon {
  background: #1677ff;
  color: white;
}

.payment-info {
  flex: 1;
}

.payment-name {
  font-size: 16px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 4px;
}

.payment-desc {
  font-size: 14px;
  color: #6b7280;
}

.payment-radio {
  margin-left: 16px;
}

.radio-circle {
  width: 20px;
  height: 20px;
  border: 2px solid #d1d5db;
  border-radius: 50%;
  position: relative;
  transition: all 0.3s ease;
}

.radio-circle.checked {
  border-color: #10b981;
}

.radio-circle.checked::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  height: 10px;
  background: #10b981;
  border-radius: 50%;
}

.payment-action {
  text-align: center;
}

.pay-button {
  width: 100%;
  max-width: 400px;
  padding: 16px 32px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pay-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.pay-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.security-notice {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: #f8fafc;
  border-top: 1px solid #e5e7eb;
}

.security-icon {
  color: #10b981;
  margin-right: 12px;
}

.security-title {
  font-size: 14px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 2px;
}

.security-desc {
  font-size: 12px;
  color: #6b7280;
}

/* 二维码区域样式 */
.qr-code-section {
  margin-top: 30px;
  text-align: center;
}

.qr-code-container {
  padding: 30px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.qr-code-container h3 {
  font-size: 18px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 20px;
}

.qr-code {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.qr-code img {
  width: 200px;
  height: 200px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  padding: 10px;
}

.qr-code-info {
  margin-bottom: 20px;
}

.qr-code-info p {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 8px 0;
}

.order-no {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  color: #10b981;
}

.qr-code-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.retry-button,
.cancel-button {
  padding: 8px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.retry-button {
  background: #10b981;
  color: white;
}

.retry-button:hover {
  background: #059669;
}

.cancel-button {
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #e5e7eb;
}

.cancel-button:hover {
  background: #e5e7eb;
  color: #4b5563;
}

@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .payment-container {
    border-radius: 16px;
  }

  .payment-header {
    padding: 30px 20px;
  }

  .payment-header h1 {
    font-size: 1.5rem;
  }

  .payment-content {
    padding: 30px 20px;
  }

  .payment-option {
    padding: 16px;
  }

  .payment-icon {
    width: 40px;
    height: 40px;
    margin-right: 12px;
  }
}
</style>