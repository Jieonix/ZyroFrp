class CountdownManager {
  constructor() {
    this.timers = new Map();
  }

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

    if (this.timers.has(id)) {
      this.cancel(id);
    }

    reactiveData[countdownKey] = duration;
    reactiveData[isCountingKey] = true;

    const timerId = setInterval(() => {
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

    this.timers.set(id, {
      timerId,
      reactiveData,
      options
    });

    if (onStart) {
      onStart(duration);
    }

    return {
      id,
      cancel: () => this.cancel(id),
      pause: () => this.pause(id),
      resume: () => this.resume(id),
      isRunning: () => this.isRunning(id),
      getRemaining: () => reactiveData[countdownKey]
    };
  }

  complete(id, reactiveData, options) {
    const { isCountingKey, countdownKey, onComplete } = options;

    const timerInfo = this.timers.get(id);
    if (timerInfo) {
      clearInterval(timerInfo.timerId);
      this.timers.delete(id);
    }

    reactiveData[isCountingKey] = false;
    reactiveData[countdownKey] = options.duration || 60;

    if (onComplete) {
      onComplete();
    }
  }

  cancel(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return false;
    }

    clearInterval(timerInfo.timerId);
    this.timers.delete(id);

    const { reactiveData, options } = timerInfo;
    reactiveData[options.isCountingKey || 'isCounting'] = false;
    reactiveData[options.countdownKey || 'countdown'] = options.duration || 60;

    if (options.onCancel) {
      options.onCancel();
    }

    return true;
  }

  pause(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return false;
    }

    clearInterval(timerInfo.timerId);

    timerInfo.paused = true;

    return true;
  }

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

  isRunning(id) {
    const timerInfo = this.timers.get(id);
    return timerInfo && !timerInfo.paused;
  }

  getRemaining(id) {
    const timerInfo = this.timers.get(id);
    if (!timerInfo) {
      return 0;
    }

    const { reactiveData, options } = timerInfo;
    return reactiveData[options.countdownKey || 'countdown'] || 0;
  }

  cancelAll() {
    for (const [id] of this.timers) {
      this.cancel(id);
    }
  }

  getActiveCount() {
    return this.timers.size;
  }
}

const countdownManager = new CountdownManager();


export const createCountdown = (id, reactiveData, options = {}) => {
  return countdownManager.create(id, reactiveData, options);
};

export const createVerificationCodeCountdown = (id, reactiveData, options = {}) => {
  const defaultOptions = {
    duration: 60,
    countdownKey: 'countdown',
    isCountingKey: 'isCounting',
    onStart: () => {
      console.log(`验证码倒计时开始 (${id})`);
    },
    onTick: (remaining) => {
    },
    onComplete: () => {
      console.log(`验证码倒计时结束 (${id})`);
    }
  };

  return createCountdown(id, reactiveData, { ...defaultOptions, ...options });
};

export const cancelCountdown = (id) => {
  return countdownManager.cancel(id);
};

export const isCountdownRunning = (id) => {
  return countdownManager.isRunning(id);
};

export const getCountdownRemaining = (id) => {
  return countdownManager.getRemaining(id);
};

export const cancelAllCountdowns = () => {
  countdownManager.cancelAll();
};

export default countdownManager;