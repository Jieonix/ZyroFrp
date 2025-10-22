import './assets/main.css'
import './assets/L&R.css'
import './assets/api.js'
import './utils/validate.js'
import './utils/token.js'
import './assets/vue_main.js'

import Vant from 'vant'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(Vant)
app.mount('#app')

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