// 共享函数
import { useRouter } from 'vue-router';
import { validateToken } from '../../utils/token.js';
import { useLoadingStore } from '@/stores/loading'

export const commonMethods = {
  // Token验证函数
  checkTokenValidity() {
    const router = useRouter();
    const token = localStorage.getItem("Token");
    if (!validateToken(router, token)) {
      return;
    }
  }
}

export const commonImports = {
  Header: () => import('@/components/Header.vue'),
  Sidebar: () => import('@/components/Sidebar.vue'),
  Footer: () => import('@/components/Footer.vue'),
  Loading: () => import('@/components/Loading.vue')
}

// 通用的mounted初始化函数
export function initializeCommonMethods() {
  return {
    ...commonMethods
  }
}