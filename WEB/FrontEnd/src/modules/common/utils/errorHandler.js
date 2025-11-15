export const ErrorTypes = {
  NETWORK: 'NETWORK',
  VALIDATION: 'VALIDATION',
  AUTHENTICATION: 'AUTHENTICATION',
  AUTHORIZATION: 'AUTHORIZATION',
  SERVER: 'SERVER',
  BUSINESS: 'BUSINESS',
  UNKNOWN: 'UNKNOWN'
};

export const ErrorLevels = {
  INFO: 'INFO',
  WARN: 'WARN',
  ERROR: 'ERROR',
  FATAL: 'FATAL'
};

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

  analyzeErrorType(error) {
    if (!error.response) {
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

  getUserFriendlyMessage(error, defaultMessage = null) {
    if (error.response?.data?.message) {
      return error.response.data.message;
    }

    if (error.response?.data?.error) {
      return error.response.data.error;
    }

    if (defaultMessage) {
      return defaultMessage;
    }

    const errorType = this.analyzeErrorType(error);
    return this.errorMessages[errorType];
  }


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

  showErrorToUser(error, messageFunction = null, defaultMessage = null) {
    const userMessage = this.getUserFriendlyMessage(error, defaultMessage);

    if (messageFunction && typeof messageFunction === 'function') {
      messageFunction(userMessage);
    } else {
      alert(userMessage);
    }
  }

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

    if (log) {
      this.logError(error, context, logLevel);
    }

    if (showMessage) {
      this.showErrorToUser(error, showMessage, defaultMessage);
    }

    if (errorType === ErrorTypes.AUTHENTICATION && onAuthError) {
      onAuthError(error);
    } else if (errorType === ErrorTypes.NETWORK && onNetworkError) {
      onNetworkError(error);
    }

    return {
      handled: true,
      type: errorType,
      level: this.analyzeErrorLevel(error),
      message: this.getUserFriendlyMessage(error, defaultMessage)
    };
  }
}

const errorHandler = new ErrorHandler();

export const handleError = (error, options = {}) => {
  return errorHandler.handle(error, options);
};

export const createErrorHandler = (context, defaultOptions = {}) => {
  return (error, options = {}) => {
    return handleError(error, {
      ...defaultOptions,
      ...options,
      context: context || (options.context || '')
    });
  };
};

export const withErrorHandling = (asyncFunction, options = {}) => {
  return async (...args) => {
    try {
      return await asyncFunction(...args);
    } catch (error) {
      handleError(error, options);
      throw error;
    }
  };
};

export default errorHandler;