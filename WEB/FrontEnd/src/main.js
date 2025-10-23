import './assets/main.css'
import './assets/L&R.css'
import './assets/api.js'
import './utils/validate.js'
import './utils/token.js'
import './assets/vue_main.js'
import './utils/message.css'
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon.vue'


import Vant from 'vant'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { installMessage } from './utils/message.js'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(Vant)
installMessage(app)
app.mount('#app')
app.component('SvgIcon', SvgIcon)


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