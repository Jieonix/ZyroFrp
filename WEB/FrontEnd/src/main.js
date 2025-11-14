import './modules/common/assets/styles/main.css'
import './modules/common/assets/styles/L&R.css'
import './modules/common/utils/api-config.js'
import './modules/auth/utils/validate.js'
import './modules/auth/utils/token.js'
import './modules/common/utils/activityTracker.js'
import './modules/common/utils/validation.js'
import './modules/common/utils/message.css'
import 'virtual:svg-icons-register'
import SvgIcon from '@/modules/common/components/SvgIcon.vue'
import activityTracker from './modules/common/utils/activityTracker.js'
import { vActivityTrack } from './modules/common/utils/activityTracker.js'

import Vant from 'vant'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { installMessage } from './modules/common/utils/message.js'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(Vant)
installMessage(app)
app.mount('#app')
app.component('SvgIcon', SvgIcon)

// 注册活跃时间跟踪指令
app.directive('activity-track', vActivityTrack)

// 启动活跃时间跟踪器
router.afterEach((to, from) => {
  // 检查是否已登录（支持普通用户和管理员token）
  const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken');
  if (token && to.name !== 'Login' && to.name !== 'Register' && to.name !== 'Admin_Login') {
    activityTracker.start();
  } else {
    activityTracker.stop();
  }
});

// 初始化时检查登录状态
const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken');
const currentRoute = router.currentRoute.value;
if (token && currentRoute.name !== 'Login' && currentRoute.name !== 'Register' && currentRoute.name !== 'Admin_Login') {
  activityTracker.start();
}


window.addEventListener('DOMContentLoaded', () => {
  const baseWidth = 1920;
  const baseFontSize = 14;
  const setFontSize = () => {
    const html = document.documentElement;
    const scale = html.clientWidth / baseWidth;
    html.style.fontSize = baseFontSize * scale + 'px';
  }
  setFontSize();
  window.addEventListener('resize', setFontSize);
});