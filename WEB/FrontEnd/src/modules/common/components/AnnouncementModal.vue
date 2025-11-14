<template>
  <transition name="modal-fade">
    <div v-if="isVisible" class="announcement-overlay" @click.self="handleClose">
      <div class="announcement-modal">
        <div class="announcement-header">
          <div class="announcement-icon">
            <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="1024" height="1024">
              <path
                d="M512 56.878545c251.345455 0 455.121455 203.776 455.121455 455.121455S763.345455 967.121455 512 967.121455 56.878545 763.345455 56.878545 512 260.654545 56.878545 512 56.878545z"
                fill="#22c55e" />
              <g transform="translate(512,512) scale(0.9) translate(-512,-512)">
                <path
                  d="M513.629091 284.020364l-181.061818 181.061818a28.439273 28.439273 0 1 0 40.261818 40.215273l110.312727-110.312728v315.717818a28.439273 28.439273 0 0 0 56.878546 0V390.981818l114.408727 114.362182a28.439273 28.439273 0 1 0 40.215273-40.215273l-181.061819-181.061818z"
                  fill="#ffffff">
                  <animateTransform attributeName="transform" type="translate" values="0,0;0,-16;0,0" dur="1s"
                    repeatCount="indefinite" />
                </path>
              </g>
            </svg>

          </div>
          <h3 class="announcement-title">服务升级公告</h3>
        </div>

        <div class="announcement-content">
          <p>网站正式上线</p>
          <p>服务器品质升级</p>
          <p>我们的服务将长期保持稳定</p>
        </div>

        <div class="announcement-footer">
          <button class="announcement-button" @click="handleClose">
            我知道了
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'

const isVisible = ref(false)

const STORAGE_KEY = 'zyrofrp-announcement-date'

const preventBodyScroll = () => {
  document.body.style.overflow = 'hidden'
  document.body.style.position = 'fixed'
  document.body.style.width = '100%'
  document.body.style.top = `-${window.scrollY}px`
}

const restoreBodyScroll = () => {
  const scrollY = document.body.style.top
  document.body.style.overflow = ''
  document.body.style.position = ''
  document.body.style.width = ''
  document.body.style.top = ''

  if (scrollY) {
    window.scrollTo(0, parseInt(scrollY || '0') * -1)
  }
}

const checkAnnouncementStatus = () => {
  const today = new Date().toDateString()
  const lastShown = localStorage.getItem(STORAGE_KEY)

  if (lastShown !== today) {
    isVisible.value = true
  }
}

const handleClose = () => {
  isVisible.value = false
  const today = new Date().toDateString()
  localStorage.setItem(STORAGE_KEY, today)
}

watch(isVisible, (newValue) => {
  if (newValue) {
    preventBodyScroll()
  } else {
    restoreBodyScroll()
  }
})

onBeforeUnmount(() => {
  restoreBodyScroll()
})

onMounted(() => {
  checkAnnouncementStatus()
})
</script>

<style scoped>
.announcement-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(16, 16, 20, 0.728);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
  padding: 20px;
  box-sizing: border-box;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.announcement-modal {
  background: rgb(255, 255, 255);
  border-radius: 16px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.1);
  overflow: hidden;
  animation: modalSlideIn 0.3s ease-out;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

.announcement-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 24px 20px;
  background: linear-gradient(135deg, rgba(248, 249, 250, 0.8) 0%, rgba(233, 236, 239, 0.6) 100%);
}

.announcement-icon {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
  animation: iconPulse 2s infinite;
}

.announcement-icon svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
}

.announcement-title {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  text-align: center;
  letter-spacing: 0.5px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.announcement-content {
  padding: 25px 28px;
  text-align: center;
  background: rgba(255, 255, 255, 0.4);
}

.announcement-content p {
  margin: 0;
  font-size: 17px;
  line-height: 1.6;
  color: #2c3e50;
  font-weight: 500;
  letter-spacing: 0.3px;
  position: relative;
}

.announcement-footer {
  padding: 20px 24px 30px;
  display: flex;
  justify-content: center;
  background: rgba(255, 255, 255, 0.4);
}

.announcement-button {
  background: linear-gradient(135deg, #49a989 0%, #358360 100%);
  color: white;
  border: none;
  padding: 14px 36px;
  border-radius: 28px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 140px;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 15px rgba(74, 171, 139, 0.3);
}

.announcement-button:hover {
  transform: translateY(-1px);
  transition: all 0.3s ease;
  box-shadow: 0 8px 30px rgba(64, 147, 119, 0.4);
  background: linear-gradient(135deg, #5bbf9c 0%, #54b18a 100%);
}

.announcement-button:active {
  transform: translateY(0);
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

@media (prefers-color-scheme: dark) {

  .announcement-modal {
    background: rgb(30, 30, 35);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(255, 255, 255, 0.1);
  }

  .announcement-header {
    background: linear-gradient(135deg, rgba(25, 25, 30, 0.8) 0%, rgba(15, 15, 20, 0.6) 100%);
  }

  .announcement-icon svg {
    filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.3));
  }

  .announcement-title {
    color: #ffffff;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }

  .announcement-content {
    background: rgba(20, 20, 25, 0.4);
  }

  .announcement-content p {
    color: #e0e0e0;
  }

  .announcement-footer {
    background: rgba(20, 20, 25, 0.4);
  }

  .announcement-button {
    background: linear-gradient(135deg, #32755f 0%, #245840 100%);
    color: white;
    box-shadow: 0 4px 15px rgba(51, 117, 95, 0.3);
  }

  .announcement-button:hover {
    box-shadow: 0 8px 30px rgba(39, 91, 74, 0.4);
    background: linear-gradient(135deg, #4fa788 0%, #489676 100%);
  }
}
</style>