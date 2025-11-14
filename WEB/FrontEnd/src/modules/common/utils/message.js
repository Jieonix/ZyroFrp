import { createApp, h, ref } from 'vue'

// 消息队列
const messageQueue = ref([])

// 消息组件
const MessageComponent = {
  name: 'GlobalMessage',
  setup() {
    return {
      messages: messageQueue
    }
  },
  render() {
    return h('div', { class: 'global-message-container' },
      this.messages.map((message, index) =>
        h('div', {
          key: message.id,
          class: ['global-message', message.type],
          style: {
            top: `${index * 70 + 20}px`
          }
        }, [
          h('div', { class: 'global-message-content' }, [
            h('div', { class: 'global-message-icon' },
              message.type === 'success' ? '✓' :
              message.type === 'error' ? '✕' :
              message.type === 'warning' ? '⚠' : 'ℹ'
            ),
            h('span', { class: 'global-message-text' }, message.content),
            h('button', {
              class: 'global-message-close',
              onClick: () => this.closeMessage(message.id)
            }, '×')
          ])
        ])
      )
    )
  },
  methods: {
    closeMessage(id) {
      const index = messageQueue.value.findIndex(msg => msg.id === id)
      if (index !== -1) {
        messageQueue.value.splice(index, 1)
      }
    }
  }
}

// 消息管理器
class MessageManager {
  constructor() {
    this.nextId = 0
  }

  // 显示消息
  show(content, type = 'info', duration = 3000) {
    const id = this.nextId++
    const message = {
      id,
      content,
      type,
      duration
    }

    messageQueue.value.push(message)

    // 自动关闭
    if (duration > 0) {
      setTimeout(() => {
        this.close(id)
      }, duration)
    }

    return id
  }

  // 关闭消息
  close(id) {
    const index = messageQueue.value.findIndex(msg => msg.id === id)
    if (index !== -1) {
      messageQueue.value.splice(index, 1)
    }
  }

  // 关闭所有消息
  closeAll() {
    messageQueue.value = []
  }

  // 成功消息
  success(content, duration = 3000) {
    return this.show(content, 'success', duration)
  }

  // 错误消息
  error(content, duration = 3000) {
    return this.show(content, 'error', duration)
  }

  // 警告消息
  warning(content, duration = 3000) {
    return this.show(content, 'warning', duration)
  }

  // 信息消息
  info(content, duration = 3000) {
    return this.show(content, 'info', duration)
  }
}

// 创建消息管理器实例
const messageManager = new MessageManager()

// 安装函数
export function installMessage(app) {
  // 注册全局属性
  app.config.globalProperties.$message = messageManager

  // 创建并挂载消息容器
  const messageContainer = document.createElement('div')
  messageContainer.id = 'global-message-container'
  document.body.appendChild(messageContainer)

  // 渲染消息组件
  createApp(MessageComponent).mount('#global-message-container')
}

// 导出消息管理器
export default messageManager