import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import { useLoadingStore } from '@/modules/common/stores/loading'

export const commonMethods = {
  checkTokenValidity() {
    const router = useRouter();
    const token = localStorage.getItem("Token");
    if (!validateToken(router, token)) {
      return;
    }
  }
}


export function initializeCommonMethods() {
  return {
    ...commonMethods
  }
}