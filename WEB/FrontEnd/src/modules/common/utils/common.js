// 共享函数
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import { useLoadingStore } from '@/modules/common/stores/loading'

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
  Header: () => import('@/modules/common/components/Header.vue'),
  Sidebar: () => import('@/modules/user/components/Sidebar.vue'),
  Footer: () => import('@/modules/common/components/Footer.vue'),
  Loading: () => import('@/modules/common/components/Loading.vue')
}

// 通用的mounted初始化函数
export function initializeCommonMethods() {
  return {
    ...commonMethods
  }
}