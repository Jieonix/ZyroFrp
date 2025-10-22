<template>
  <van-overlay :show="loadingStore.isLoading" class="overlay">
    <div class="spinner"></div>
  </van-overlay>
</template>

<script setup>
import { useLoadingStore } from '@/stores/loading'
import { watch } from 'vue'

const loadingStore = useLoadingStore()

watch(() => loadingStore.isLoading, (newValue) => {
  if (newValue) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = '';
  }
})
</script>

<style scoped>
.overlay {
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  margin-left: 18rem;
  width: calc(100vw - 12vw);
  margin-top: 60px;
  height: calc(100vh - 60px);
  height: 100%;
  z-index: 9999;
}

.spinner {
  width: 35px;
  height: 35px;
  border: 3px solid rgba(255, 255, 255, 0.451);
  border-top: 3px solid #2e6a56;
  border-radius: 50%;
  animation: spin 1.5s linear infinite;
}

@media (prefers-color-scheme: dark) {

.overlay {
  background-color: #101014;
}

}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
