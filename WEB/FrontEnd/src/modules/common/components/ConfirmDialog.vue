<template>
  <div v-if="visible" class="overlay_1" @click="handleOverlayClick">
    <div class="confirm-dialog" @click.stop>
      <h1>{{ title }}</h1>
      <div class="confirm-message">{{ message }}</div>
      <div class="confirm-buttons">
        <button class="confirm-btn" @click="handleConfirm" :disabled="loading">
          {{ loading ? loadingText : confirmText }}
        </button>
        <button class="cancel-btn" @click="handleCancel" :disabled="loading">
          {{ cancelText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '确认操作'
    },
    message: {
      type: String,
      default: '确定要执行此操作吗？'
    },
    confirmText: {
      type: String,
      default: '确认'
    },
    cancelText: {
      type: String,
      default: '取消'
    },
    loading: {
      type: Boolean,
      default: false
    },
    loadingText: {
      type: String,
      default: '处理中...'
    },
    closeOnOverlay: {
      type: Boolean,
      default: true
    }
  },
  emits: ['confirm', 'cancel', 'close'],
  methods: {
    handleConfirm() {
      this.$emit('confirm');
    },
    handleCancel() {
      this.$emit('cancel');
      this.$emit('close');
    },
    handleOverlayClick() {
      if (this.closeOnOverlay) {
        this.handleCancel();
      }
    },
    // 按键事件处理
    handleKeydown(e) {
      if (!this.visible) return;

      if (e.key === 'Escape') {
        this.handleCancel();
      } else if (e.key === 'Enter' && !this.loading) {
        this.handleConfirm();
      }
    }
  },
  mounted() {
    // 添加键盘事件监听
    document.addEventListener('keydown', this.handleKeydown);
  },
  beforeUnmount() {
    // 移除键盘事件监听
    document.removeEventListener('keydown', this.handleKeydown);
  }
}
</script>

<style scoped>
.overlay_1 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(5px);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.confirm-dialog {
  width: 90%;
  max-width: 400px;
  background-color: #ffffff;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  padding: 40px 30px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease;
  min-height: 200px;
  justify-content: center;
}

@keyframes slideIn {
  from {
    transform: translateY(-50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.confirm-dialog h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 1.5rem;
}

.confirm-message {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 16px;
  line-height: 1.5;
  white-space: pre-line;
}

.confirm-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-top: 20px;
}

.confirm-btn {
  height: 42px;
  width: 100px;
  background-color: #4caf50;
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
}

.confirm-btn:hover:not(:disabled) {
  background-color: #45a049;
}

.confirm-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.cancel-btn {
  height: 42px;
  width: 100px;
  background-color: #9e9e9e;
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
}

.cancel-btn:hover:not(:disabled) {
  background-color: #757575;
}

.cancel-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .confirm-dialog {
    width: 95%;
    padding: 30px 20px;
    max-height: 80vh;
  }

  .confirm-buttons {
    flex-direction: column;
    gap: 12px;
  }

  .confirm-btn,
  .cancel-btn {
    width: 100%;
    height: 48px;
  }

  .confirm-message {
    font-size: 14px;
  }
}

/* 暗黑模式 */
@media (prefers-color-scheme: dark) {
  .confirm-dialog {
    background-color: #1e1e1e;
    color: #e9ecef;
  }

  .confirm-dialog h1 {
    color: #e9ecef;
  }

  .confirm-message {
    color: #b0b0b0;
  }

  .cancel-btn {
    background-color: #424242;
    color: #e9ecef;
  }

  .cancel-btn:hover:not(:disabled) {
    background-color: #616161;
  }

  .confirm-btn:disabled,
  .cancel-btn:disabled {
    background-color: #555555;
    color: #999999;
  }
}
</style>