import { createApp, h, ref } from 'vue'

const messageQueue = ref([])

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

class MessageManager {
  constructor() {
    this.nextId = 0
  }

  show(content, type = 'info', duration = 3000) {
    const id = this.nextId++
    const message = {
      id,
      content,
      type,
      duration
    }

    messageQueue.value.push(message)

    if (duration > 0) {
      setTimeout(() => {
        this.close(id)
      }, duration)
    }

    return id
  }

  close(id) {
    const index = messageQueue.value.findIndex(msg => msg.id === id)
    if (index !== -1) {
      messageQueue.value.splice(index, 1)
    }
  }

  closeAll() {
    messageQueue.value = []
  }

  success(content, duration = 3000) {
    return this.show(content, 'success', duration)
  }

  error(content, duration = 3000) {
    return this.show(content, 'error', duration)
  }

  warning(content, duration = 3000) {
    return this.show(content, 'warning', duration)
  }

  info(content, duration = 3000) {
    return this.show(content, 'info', duration)
  }
}

const messageManager = new MessageManager()

export function installMessage(app) {
  app.config.globalProperties.$message = messageManager

  const messageContainer = document.createElement('div')
  messageContainer.id = 'global-message-container'
  document.body.appendChild(messageContainer)

  createApp(MessageComponent).mount('#global-message-container')
}

export default messageManager