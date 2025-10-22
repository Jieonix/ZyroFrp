<template>
  <div v-if="visible" class="message-box" :class="type">
    <div class="message-box-content">
      <div class="message-box-icon">
        <span v-if="type === 'success'">✓</span>
        <span v-else-if="type === 'error'">✕</span>
        <span v-else>ℹ</span>
      </div>
      <span class="message-box-message">{{ message }}</span>
    </div>
    <button class="message-box-close" @click="closeMessage">×</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
  message: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
  },
  duration: {
    type: Number,
    default: 3000
  }
});

const emit = defineEmits(['close']);

const visible = ref(true);
let timer = null;

const closeMessage = () => {
  visible.value = false;
  emit('close');
};

onMounted(() => {
  if (props.duration > 0) {
    timer = setTimeout(() => {
      closeMessage();
    }, props.duration);
  }
});
</script>

<style scoped>
.message-box {
  position: fixed;
  top: 20px;
  right: 20px;
  border-radius: 8px;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 300px;
  max-width: 400px;
  transition: all 0.3s ease;
  z-index: 9999;
  padding: 12px 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease-out;
}

.message-box.success {
  background-color: #f0f9ff;
  color: #0369a1;
  border-left: 4px solid #0ea5e9;
}

.message-box.error {
  background-color: #fef2f2;
  color: #dc2626;
  border-left: 4px solid #ef4444;
}

.message-box.warning {
  background-color: #fffbeb;
  color: #d97706;
  border-left: 4px solid #f59e0b;
}

.message-box.info {
  background-color: #f0f9ff;
  color: #0369a1;
  border-left: 4px solid #0ea5e9;
}

.message-box-content {
  display: flex;
  align-items: center;
  flex: 1;
}

.message-box-icon {
  margin-right: 8px;
  font-weight: bold;
  font-size: 16px;
}

.message-box-message {
  flex: 1;
  line-height: 1.5;
}

.message-box-close {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  padding: 0;
  margin-left: 8px;
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.message-box-close:hover {
  opacity: 1;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@media (prefers-color-scheme: dark) {
  .message-box.success {
    background-color: #0c4a6e;
    color: #e0f2fe;
  }

  .message-box.error {
    background-color: #7f1d1d;
    color: #fecaca;
  }

  .message-box.warning {
    background-color: #78350f;
    color: #fef3c7;
  }

  .message-box.info {
    background-color: #0c4a6e;
    color: #e0f2fe;
  }
}
</style>