/**
 * 倒计时工具
 * 提供统一的倒计时功能，主要用于验证码发送等场景
 */

/**
 * 倒计时管理器类
 */
class CountdownManager {
  constructor() {
    this.timers = new Map(); // 存储所有活动的倒计时器
  }

  /**
   * 创建倒计时
   * @param {string} id - 倒计时唯一标识
   * @param {Object} reactiveData - Vue 响应式数据对象
   * @param {Object} options - 配置选项
   * @param {number} options.duration - 倒计时时长（秒），默认60
   * @param {string} options.countdownKey - 倒计时值的属性名，默认 'countdown'
   * @param {string} options.isCountingKey - 是否正在倒计时的属性名，默认 'isCounting'
   * @param {Function} options.onStart - 开始倒计时回调
   * @param {Function} options.onTick - 每秒回调
   * @param {Function} options.onComplete - 完成倒计时回调
   * @param {Function} options.onCancel - 取消倒计时回调
   * @returns {Object} 倒计时控制对象
   */
  create(id, reactiveData, options = {}) {
    const {
      duration = 60,
      countdownKey = 'countdown',
      isCountingKey = 'isCounting',
      onStart = null,
      onTick = null,
      onComplete = null,
      onCancel = null
    } = options;

    // 如果已存在相同ID的倒计时，先清除
    if (this.timers.has(id)) {
      this.cancel(id);
    }

    // 初始化倒计时数据
    reactiveData[countdownKey] = duration;
    reactiveData[isCountingKey] = true;

    const timerId = setInterval(() => {
      const currentCountdown = reactiveData[countdownKey];

      if (currentCountdown > 0) {
        // 倒计时递减
        reactiveData[countdownKey] = currentCountdown - 1;

        // 执行每秒回调
        if (onTick) {
          onTick(reactiveData[countdownKey]);
        }
      } else {
        // 倒计时完成
        this.complete(id, reactiveData, options);
      }
    }, 1000);

    // 存储倒计时信息
    this.timers.set(id, {
      timerId,
      reactiveData,
      options
    });

    // 执行开始回调
    if (onStart) {
      onStart(duration);
    }

    // 返回控制对象
    return {
      id,
      cancel: () => this.cancel(id),
      pause: () => this.pause(id),
      resume: () => this.resume(id),
      isRunning: () => this.isRunning(id),
      getRemaining: () => reactiveData[countdownKey]
    };
  }

  /**
   * 完成倒计时
   * @param {string} id - 倒计时ID
   * @param {Object} reactiveData - 响应式数据
   * @param {Object} options - 配置选项
   */
  complete(id, reactiveData, options) {
    const { isCountingKey, countdownKey, onComplete } = options;

    // 清除定时器
    const timerInfo = this.timers.get(id);
    if (timerInfo) {
      clearInterval(timerInfo.timerId);
      this.timers.delete(id);
    }

    // 重置状态
    reactiveData[isCountingKey] = false;
    reactiveData[countdownKey] = options.duration || 60;

    // 执行完成回调
    if (onComplete) {
      onComplete();
    }
  }

  /**
   * 取消倒计时
   * @param {string} id - 倒计时ID
   * @returns {boolean} 是否成功取消
   */
  cancel(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return false;
    }

    clearInterval(timerInfo.timerId);
    this.timers.delete(id);

    // 重置状态
    const { reactiveData, options } = timerInfo;
    reactiveData[options.isCountingKey || 'isCounting'] = false;
    reactiveData[options.countdownKey || 'countdown'] = options.duration || 60;

    // 执行取消回调
    if (options.onCancel) {
      options.onCancel();
    }

    return true;
  }

  /**
   * 暂停倒计时
   * @param {string} id - 倒计时ID
   * @returns {boolean} 是否成功暂停
   */
  pause(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return false;
    }

    clearInterval(timerInfo.timerId);

    // 标记为暂停状态
    timerInfo.paused = true;

    return true;
  }

  /**
   * 恢复倒计时
   * @param {string} id - 倒计时ID
   * @returns {boolean} 是否成功恢复
   */
  resume(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo || !timerInfo.paused) {
      return false;
    }

    const { reactiveData, options } = timerInfo;
    const { countdownKey = 'countdown', onTick = null } = options;

    timerInfo.paused = false;
    timerInfo.timerId = setInterval(() => {
      const currentCountdown = reactiveData[countdownKey];

      if (currentCountdown > 0) {
        reactiveData[countdownKey] = currentCountdown - 1;

        if (onTick) {
          onTick(reactiveData[countdownKey]);
        }
      } else {
        this.complete(id, reactiveData, options);
      }
    }, 1000);

    return true;
  }

  /**
   * 检查倒计时是否正在运行
   * @param {string} id - 倒计时ID
   * @returns {boolean} 是否正在运行
   */
  isRunning(id) {
    const timerInfo = this.timers.get(id);
    return timerInfo && !timerInfo.paused;
  }

  /**
   * 获取倒计时剩余时间
   * @param {string} id - 倒计时ID
   * @returns {number} 剩余时间（秒）
   */
  getRemaining(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return 0;
    }

    const { reactiveData, options } = timerInfo;
    return reactiveData[options.countdownKey || 'countdown'] || 0;
  }

  /**
   * 取消所有倒计时
   */
  cancelAll() {
    for (const [id] of this.timers) {
      this.cancel(id);
    }
  }

  /**
   * 获取活动倒计时数量
   * @returns {number} 活动倒计时数量
   */
  getActiveCount() {
    return this.timers.size;
  }
}

// 创建全局倒计时管理器实例
const countdownManager = new CountdownManager();

/**
 * 便捷的倒计时函数
 * @param {string} id - 倒计时ID
 * @param {Object} reactiveData - Vue 响应式数据
 * @param {Object} options - 配置选项
 * @returns {Object} 倒计时控制对象
 */
export const createCountdown = (id, reactiveData, options = {}) => {
  return countdownManager.create(id, reactiveData, options);
};

/**
 * 创建验证码倒计时
 * @param {string} id - 倒计时ID
 * @param {Object} reactiveData - Vue 响应式数据
 * @param {Object} options - 配置选项
 * @returns {Object} 倒计时控制对象
 */
export const createVerificationCodeCountdown = (id, reactiveData, options = {}) => {
  const defaultOptions = {
    duration: 60,
    countdownKey: 'countdown',
    isCountingKey: 'isCounting',
    onStart: () => {
      console.log(`验证码倒计时开始 (${id})`);
    },
    onTick: (remaining) => {
      // 可以在这里添加每秒的逻辑
    },
    onComplete: () => {
      console.log(`验证码倒计时结束 (${id})`);
    }
  };

  return createCountdown(id, reactiveData, { ...defaultOptions, ...options });
};

/**
 * 取消倒计时
 * @param {string} id - 倒计时ID
 * @returns {boolean} 是否成功取消
 */
export const cancelCountdown = (id) => {
  return countdownManager.cancel(id);
};

/**
 * 检查倒计时是否正在运行
 * @param {string} id - 倒计时ID
 * @returns {boolean} 是否正在运行
 */
export const isCountdownRunning = (id) => {
  return countdownManager.isRunning(id);
};

/**
 * 获取倒计时剩余时间
 * @param {string} id - 倒计时ID
 * @returns {number} 剩余时间（秒）
 */
export const getCountdownRemaining = (id) => {
  return countdownManager.getRemaining(id);
};

/**
 * 取消所有倒计时
 */
export const cancelAllCountdowns = () => {
  countdownManager.cancelAll();
};

export default countdownManager;