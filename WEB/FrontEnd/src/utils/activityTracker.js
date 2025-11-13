// 活跃时间跟踪器
import axios from 'axios';

class ActivityTracker {
  constructor() {
    this.updateInterval = 30000; // 30秒
    this.timer = null;
    this.isTracking = false;
    this.lastUpdateTime = 0;
    this.throttleDelay = 5000; // 5秒防抖
    this.pendingUpdate = false;
  }

  // 开始跟踪
  start() {
    if (this.isTracking) return;

    this.isTracking = true;
    this.setupPageVisibilityHandlers();
    this.setupBeforeUnloadHandler();

    // 立即更新一次
    this.updateLastActive();

    // 设置定时更新
    this.timer = setInterval(() => {
      this.updateLastActive();
    }, this.updateInterval);

    console.log('活跃时间跟踪器已启动');
  }

  // 停止跟踪
  stop() {
    if (!this.isTracking) return;

    this.isTracking = false;
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }

    this.removePageVisibilityHandlers();
    this.removeBeforeUnloadHandler();

    console.log('活跃时间跟踪器已停止');
  }

  // 更新最后活跃时间（带防抖）
  updateLastActive() {
    const now = Date.now();

    // 防抖处理
    if (now - this.lastUpdateTime < this.throttleDelay) {
      if (!this.pendingUpdate) {
        this.pendingUpdate = true;
        setTimeout(() => {
          this.pendingUpdate = false;
          this.performUpdate();
        }, this.throttleDelay);
      }
      return;
    }

    this.performUpdate();
  }

  // 执行实际的更新请求
  async performUpdate() {
    try {
      // 尝试获取普通用户token或管理员token
      const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken');
      if (!token) {
        console.warn('未找到token，停止活跃时间更新');
        this.stop();
        return;
      }

      await axios.post('/users/update-last-active', {}, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      this.lastUpdateTime = Date.now();
      console.log('活跃时间更新成功');
    } catch (error) {
      console.error('活跃时间更新失败:', error.message);

      // 如果是认证错误，停止跟踪
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        console.warn('认证失败，停止活跃时间跟踪');
        this.stop();
      }
    }
  }

  // 用户操作时手动触发更新
  onUserAction() {
    if (this.isTracking) {
      this.updateLastActive();
    }
  }

  // 设置页面可见性处理
  setupPageVisibilityHandlers() {
    this.visibilityHandler = () => {
      if (document.visibilityState === 'visible') {
        // 页面重新可见时立即更新
        this.updateLastActive();
      }
    };

    document.addEventListener('visibilitychange', this.visibilityHandler);
  }

  // 移除页面可见性处理
  removePageVisibilityHandlers() {
    if (this.visibilityHandler) {
      document.removeEventListener('visibilitychange', this.visibilityHandler);
      this.visibilityHandler = null;
    }
  }

  // 设置页面卸载前的处理
  setupBeforeUnloadHandler() {
    this.beforeUnloadHandler = () => {
      // 使用sendBeacon发送最后的更新请求
      const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken');
      if (token) {
        const data = new Blob([JSON.stringify({})], { type: 'application/json' });
        navigator.sendBeacon('/users/update-last-active', data);
      }
    };

    window.addEventListener('beforeunload', this.beforeUnloadHandler);
  }

  // 移除页面卸载前的处理
  removeBeforeUnloadHandler() {
    if (this.beforeUnloadHandler) {
      window.removeEventListener('beforeunload', this.beforeUnloadHandler);
      this.beforeUnloadHandler = null;
    }
  }
}

// 创建单例实例
const activityTracker = new ActivityTracker();

export default activityTracker;

// Vue指令，用于自动跟踪用户操作
export const vActivityTrack = {
  mounted(el, binding) {
    const events = binding.value || ['click', 'touchstart', 'keydown'];

    const handler = () => {
      activityTracker.onUserAction();
    };

    el._activityTrackerHandler = handler;

    events.forEach(event => {
      el.addEventListener(event, handler);
    });
  },

  unmounted(el) {
    if (el._activityTrackerHandler) {
      const events = ['click', 'touchstart', 'keydown'];
      events.forEach(event => {
        el.removeEventListener(event, el._activityTrackerHandler);
      });
      delete el._activityTrackerHandler;
    }
  }
};