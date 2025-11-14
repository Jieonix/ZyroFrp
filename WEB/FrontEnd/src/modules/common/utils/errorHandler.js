/**
 * 统一错误处理工具
 * 提供统一的错误处理、日志记录和用户提示功能
 */

// 错误类型枚举
export const ErrorTypes = {
  NETWORK: 'NETWORK',
  VALIDATION: 'VALIDATION',
  AUTHENTICATION: 'AUTHENTICATION',
  AUTHORIZATION: 'AUTHORIZATION',
  SERVER: 'SERVER',
  BUSINESS: 'BUSINESS',
  UNKNOWN: 'UNKNOWN'
};

// 错误级别枚举
export const ErrorLevels = {
  INFO: 'INFO',
  WARN: 'WARN',
  ERROR: 'ERROR',
  FATAL: 'FATAL'
};

/**
 * 错误处理器类
 */
class ErrorHandler {
  constructor() {
    this.errorMessages = {
      [ErrorTypes.NETWORK]: '网络连接失败，请检查网络设置',
      [ErrorTypes.VALIDATION]: '输入数据验证失败',
      [ErrorTypes.AUTHENTICATION]: '身份验证失败，请重新登录',
      [ErrorTypes.AUTHORIZATION]: '权限不足，无法执行此操作',
      [ErrorTypes.SERVER]: '服务器内部错误，请稍后重试',
      [ErrorTypes.BUSINESS]: '业务逻辑错误',
      [ErrorTypes.UNKNOWN]: '未知错误，请联系技术支持'
    };
  }

  /**
   * 分析错误类型
   * @param {Error} error - 错误对象
   * @returns {string} 错误类型
   */
  analyzeErrorType(error) {
    if (!error.response) {
      // 网络错误或请求未发送
      return ErrorTypes.NETWORK;
    }

    const status = error.response.status;

    if (status === 401) {
      return ErrorTypes.AUTHENTICATION;
    } else if (status === 403) {
      return ErrorTypes.AUTHORIZATION;
    } else if (status >= 400 && status < 500) {
      return ErrorTypes.BUSINESS;
    } else if (status >= 500) {
      return ErrorTypes.SERVER;
    }

    return ErrorTypes.UNKNOWN;
  }

  /**
   * 分析错误级别
   * @param {Error} error - 错误对象
   * @returns {string} 错误级别
   */
  analyzeErrorLevel(error) {
    const errorType = this.analyzeErrorType(error);

    switch (errorType) {
      case ErrorTypes.NETWORK:
      case ErrorTypes.SERVER:
        return ErrorLevels.ERROR;
      case ErrorTypes.AUTHENTICATION:
      case ErrorTypes.AUTHORIZATION:
        return ErrorLevels.WARN;
      case ErrorTypes.BUSINESS:
        return ErrorLevels.INFO;
      default:
        return ErrorLevels.ERROR;
    }
  }

  /**
   * 获取用户友好的错误消息
   * @param {Error} error - 错误对象
   * @param {string} defaultMessage - 默认消息
   * @returns {string} 用户友好的错误消息
   */
  getUserFriendlyMessage(error, defaultMessage = null) {
    // 如果服务器返回了错误消息，优先使用
    if (error.response?.data?.message) {
      return error.response.data.message;
    }

    // 如果服务器返回了错误描述
    if (error.response?.data?.error) {
      return error.response.data.error;
    }

    // 使用默认消息
    if (defaultMessage) {
      return defaultMessage;
    }

    // 根据错误类型返回默认消息
    const errorType = this.analyzeErrorType(error);
    return this.errorMessages[errorType];
  }

  /**
   * 记录错误日志
   * @param {Error} error - 错误对象
   * @param {string} context - 错误上下文信息
   * @param {string} level - 错误级别
   */
  logError(error, context = '', level = null) {
    const errorLevel = level || this.analyzeErrorLevel(error);
    const errorType = this.analyzeErrorType(error);
    const timestamp = new Date().toISOString();

    const logData = {
      timestamp,
      level: errorLevel,
      type: errorType,
      context,
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      stack: error.stack
    };

    // 根据级别选择不同的日志输出方式
    switch (errorLevel) {
      case ErrorLevels.FATAL:
      case ErrorLevels.ERROR:
        console.error(`[${errorLevel}] ${context}:`, logData);
        break;
      case ErrorLevels.WARN:
        console.warn(`[${errorLevel}] ${context}:`, logData);
        break;
      case ErrorLevels.INFO:
        console.info(`[${errorLevel}] ${context}:`, logData);
        break;
      default:
        console.log(`[${errorLevel}] ${context}:`, logData);
    }
  }

  /**
   * 显示错误消息给用户
   * @param {Error} error - 错误对象
   * @param {Function} messageFunction - 消息显示函数（如 proxy.$message.error）
   * @param {string} defaultMessage - 默认消息
   */
  showErrorToUser(error, messageFunction = null, defaultMessage = null) {
    const userMessage = this.getUserFriendlyMessage(error, defaultMessage);

    if (messageFunction && typeof messageFunction === 'function') {
      messageFunction(userMessage);
    } else {
      // 如果没有提供消息显示函数，使用 alert
      alert(userMessage);
    }
  }

  /**
   * 统一错误处理函数
   * @param {Error} error - 错误对象
   * @param {Object} options - 配置选项
   * @param {string} options.context - 错误上下文
   * @param {Function} options.showMessage - 消息显示函数
   * @param {string} options.defaultMessage - 默认用户消息
   * @param {boolean} options.log - 是否记录日志
   * @param {string} options.logLevel - 日志级别
   * @param {Function} options.onAuthError - 认证错误回调
   * @param {Function} options.onNetworkError - 网络错误回调
   */
  handle(error, options = {}) {
    const {
      context = '',
      showMessage = null,
      defaultMessage = null,
      log = true,
      logLevel = null,
      onAuthError = null,
      onNetworkError = null
    } = options;

    const errorType = this.analyzeErrorType(error);

    // 记录日志
    if (log) {
      this.logError(error, context, logLevel);
    }

    // 显示用户消息
    if (showMessage) {
      this.showErrorToUser(error, showMessage, defaultMessage);
    }

    // 特殊错误处理
    if (errorType === ErrorTypes.AUTHENTICATION && onAuthError) {
      onAuthError(error);
    } else if (errorType === ErrorTypes.NETWORK && onNetworkError) {
      onNetworkError(error);
    }

    // 返回处理结果
    return {
      handled: true,
      type: errorType,
      level: this.analyzeErrorLevel(error),
      message: this.getUserFriendlyMessage(error, defaultMessage)
    };
  }
}

// 创建单例实例
const errorHandler = new ErrorHandler();

/**
 * 便捷的错误处理函数
 * @param {Error} error - 错误对象
 * @param {Object} options - 配置选项
 * @returns {Object} 处理结果
 */
export const handleError = (error, options = {}) => {
  return errorHandler.handle(error, options);
};

/**
 * 创建带有上下文的错误处理函数
 * @param {string} context - 错误上下文
 * @param {Object} defaultOptions - 默认配置选项
 * @returns {Function} 错误处理函数
 */
export const createErrorHandler = (context, defaultOptions = {}) => {
  return (error, options = {}) => {
    return handleError(error, {
      ...defaultOptions,
      ...options,
      context: context || (options.context || '')
    });
  };
};

/**
 * 异步操作的错误处理装饰器
 * @param {Function} asyncFunction - 异步函数
 * @param {Object} options - 错误处理选项
 * @returns {Function} 装饰后的函数
 */
export const withErrorHandling = (asyncFunction, options = {}) => {
  return async (...args) => {
    try {
      return await asyncFunction(...args);
    } catch (error) {
      handleError(error, options);
      throw error; // 重新抛出错误，让调用者决定是否继续处理
    }
  };
};

export default errorHandler;