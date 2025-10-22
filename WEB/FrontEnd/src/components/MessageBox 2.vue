<template>
  <div v-if="visible" class="message-box">
    <div class="message-box-content">
      <span class="message-box-message">{{ message }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  message: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['close']);

const visible = ref(true);
</script>

<style scoped>
.message-box {
  position: fixed;
  top: 10px;
  left: 56%;
  transform: translateX(-50%);
  border-radius: 5px;
  color: #fff;
  font-size: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 200px;
  background-color: #222;
  transition: opacity 0.3s ease;
  z-index: 9999;
  padding: 10px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.message-box-content {
  align-items: center;
  width: 100%;
}

@media (prefers-color-scheme: dark) {

  .message-box {
    background-color: #3e3e3e;
    color: white;
  }

}
</style>