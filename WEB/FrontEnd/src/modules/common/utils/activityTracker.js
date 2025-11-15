import axios from 'axios';

class ActivityTracker {
  constructor() {
    this.updateInterval = 30000;
    this.timer = null;
    this.isTracking = false;
    this.lastUpdateTime = 0;
    this.throttleDelay = 5000;
    this.pendingUpdate = false;
  }

  start() {
    if (this.isTracking) return;

    this.isTracking = true;
    this.setupPageVisibilityHandlers();
    this.setupBeforeUnloadHandler();

    this.updateLastActive();

    this.timer = setInterval(() => {
      this.updateLastActive();
    }, this.updateInterval);

  }

  stop() {
    if (!this.isTracking) return;

    this.isTracking = false;
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }

    this.removePageVisibilityHandlers();
    this.removeBeforeUnloadHandler();

  }

  updateLastActive() {
    const now = Date.now();

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

  async performUpdate() {
    try {
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
    } catch (error) {
      console.error('活跃时间更新失败:', error.message);

      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        console.warn('认证失败，停止活跃时间跟踪');
        this.stop();
      }
    }
  }

  onUserAction() {
    if (this.isTracking) {
      this.updateLastActive();
    }
  }

  setupPageVisibilityHandlers() {
    this.visibilityHandler = () => {
      if (document.visibilityState === 'visible') {
        this.updateLastActive();
      }
    };

    document.addEventListener('visibilitychange', this.visibilityHandler);
  }

  removePageVisibilityHandlers() {
    if (this.visibilityHandler) {
      document.removeEventListener('visibilitychange', this.visibilityHandler);
      this.visibilityHandler = null;
    }
  }

  setupBeforeUnloadHandler() {
    this.beforeUnloadHandler = () => {
      const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken');
      if (token) {
        const data = new Blob([JSON.stringify({})], { type: 'application/json' });
        navigator.sendBeacon('/users/update-last-active', data);
      }
    };

    window.addEventListener('beforeunload', this.beforeUnloadHandler);
  }

  removeBeforeUnloadHandler() {
    if (this.beforeUnloadHandler) {
      window.removeEventListener('beforeunload', this.beforeUnloadHandler);
      this.beforeUnloadHandler = null;
    }
  }
}

const activityTracker = new ActivityTracker();

export default activityTracker;

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