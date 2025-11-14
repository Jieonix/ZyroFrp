/**
 * 统一 HTTP 请求工具
 * 基于 axios 封装，提供统一的请求拦截、响应拦截、错误处理等功能
 */

import axios from 'axios'
import { handleError, createErrorHandler } from './errorHandler.js'

// 配置 axios 默认设置
const config = {
  baseURL: 'https://zyroo.cn/backend/', // 发行环境
  // baseURL: 'http://localhost:8085', // 测试环境
  timeout: 30000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
}

// 创建 axios 实例
const httpInstance = axios.create(config)

// 请求拦截器
httpInstance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken')

    if (token) {
      // 根据不同的 token 类型设置不同的 header
      if (localStorage.getItem('AdminToken')) {
        config.headers.Authorization = `Bearer ${token}`
        config.headers.AdminToken = token
      } else {
        config.headers.Token = token
        config.headers.Authorization = `Bearer ${token}`
      }
    }

    // 添加请求时间戳
    config.metadata = { startTime: new Date() }

    // 开发环境下打印请求信息
    if (import.meta.env.DEV) {
      console.log(`🚀 HTTP Request: ${config.method?.toUpperCase()} ${config.url}`, {
        params: config.params,
        data: config.data,
        headers: config.headers
      })
    }

    return config
  },
  (error) => {
    // 对请求错误做些什么
    console.error('HTTP Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
httpInstance.interceptors.response.use(
  (response) => {
    // 对响应数据做点什么
    const endTime = new Date()
    const duration = endTime - response.config.metadata.startTime

    // 开发环境下打印响应信息
    if (import.meta.env.DEV) {
      console.log(`✅ HTTP Response: ${response.config.method?.toUpperCase()} ${response.config.url} (${duration}ms)`, {
        status: response.status,
        data: response.data
      })
    }

    return response
  },
  (error) => {
    // 对响应错误做点什么
    const endTime = new Date()
    const duration = error.config?.metadata ? endTime - error.config.metadata.startTime : 0

    // 开发环境下打印错误信息
    if (import.meta.env.DEV) {
      console.error(`❌ HTTP Error: ${error.config?.method?.toUpperCase()} ${error.config?.url} (${duration}ms)`, {
        status: error.response?.status,
        data: error.response?.data,
        message: error.message
      })
    }

    return Promise.reject(error)
  }
)

/**
 * 基础请求方法
 * @param {Object} options - 请求配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
const baseRequest = (options, errorOptions = {}) => {
  return httpInstance(options).catch(error => {
    // 使用统一错误处理
    const errorHandler = createErrorHandler(errorOptions.context || 'HTTP请求', {
      showMessage: errorOptions.showMessage,
      defaultMessage: errorOptions.defaultMessage,
      onAuthError: errorOptions.onAuthError,
      onNetworkError: errorOptions.onNetworkError
    })

    errorHandler.handle(error)
    throw error // 重新抛出，让调用者可以进一步处理
  })
}

/**
 * GET 请求
 * @param {string} url - 请求地址
 * @param {Object} params - URL 参数
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const get = (url, params = {}, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'GET',
    url,
    params,
    ...options
  }, {
    context: `GET ${url}`,
    ...errorOptions
  })
}

/**
 * POST 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const post = (url, data = {}, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'POST',
    url,
    data,
    ...options
  }, {
    context: `POST ${url}`,
    ...errorOptions
  })
}

/**
 * PUT 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const put = (url, data = {}, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'PUT',
    url,
    data,
    ...options
  }, {
    context: `PUT ${url}`,
    ...errorOptions
  })
}

/**
 * DELETE 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const del = (url, data = {}, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'DELETE',
    url,
    data,
    ...options
  }, {
    context: `DELETE ${url}`,
    ...errorOptions
  })
}

/**
 * PATCH 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const patch = (url, data = {}, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'PATCH',
    url,
    data,
    ...options
  }, {
    context: `PATCH ${url}`,
    ...errorOptions
  })
}

/**
 * 上传文件
 * @param {string} url - 上传地址
 * @param {FormData} formData - 表单数据
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 请求 Promise
 */
export const upload = (url, formData, options = {}, errorOptions = {}) => {
  return baseRequest({
    method: 'POST',
    url,
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
      ...options.headers
    },
    ...options
  }, {
    context: `UPLOAD ${url}`,
    defaultMessage: '文件上传失败',
    ...errorOptions
  })
}

/**
 * 下载文件
 * @param {string} url - 下载地址
 * @param {Object} params - URL 参数
 * @param {string} filename - 保存的文件名
 * @param {Object} options - 额外配置
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 下载 Promise
 */
export const download = async (url, params = {}, filename, options = {}, errorOptions = {}) => {
  try {
    const response = await baseRequest({
      method: 'GET',
      url,
      params,
      responseType: 'blob',
      ...options
    }, {
      context: `DOWNLOAD ${url}`,
      defaultMessage: '文件下载失败',
      ...errorOptions
    })

    // 创建下载链接
    const blob = new Blob([response.data])
    const downloadUrl = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = filename || 'download'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(downloadUrl)

    return response
  } catch (error) {
    throw error
  }
}

/**
 * 并发请求
 * @param {Array} requests - 请求数组
 * @param {Object} errorOptions - 错误处理配置
 * @returns {Promise} 并发请求 Promise
 */
export const all = (requests, errorOptions = {}) => {
  return Promise.all(requests).catch(error => {
    handleError(error, {
      context: '并发请求',
      defaultMessage: '一个或多个请求失败',
      ...errorOptions
    })
    throw error
  })
}

/**
 * 取消请求的 Token
 */
export class CancelToken {
  constructor() {
    this.source = axios.CancelToken.source()
  }

  get token() {
    return this.source.token
  }

  cancel(message) {
    this.source.cancel(message)
  }
}

/**
 * 请求状态管理
 */
export const RequestManager = {
  // 存储活动的请求
  activeRequests: new Map(),

  // 添加请求
  addRequest(key, cancelToken) {
    // 如果已存在相同key的请求，先取消
    if (this.activeRequests.has(key)) {
      this.activeRequests.get(key).cancel('请求被新请求取消')
    }
    this.activeRequests.set(key, cancelToken)
  },

  // 移除请求
  removeRequest(key) {
    this.activeRequests.delete(key)
  },

  // 取消指定请求
  cancelRequest(key) {
    if (this.activeRequests.has(key)) {
      this.activeRequests.get(key).cancel('请求被手动取消')
      this.activeRequests.delete(key)
    }
  },

  // 取消所有请求
  cancelAllRequests() {
    this.activeRequests.forEach((cancelToken, key) => {
      cancelToken.cancel('页面跳转，取消所有请求')
    })
    this.activeRequests.clear()
  }
}

// 页面卸载时取消所有请求
window.addEventListener('beforeunload', () => {
  RequestManager.cancelAllRequests()
})

// 导出 axios 实例，供需要直接使用的场景
export { httpInstance as axios }

// 默认导出对象，包含所有方法
export default {
  get,
  post,
  put,
  del,
  patch,
  upload,
  download,
  all,
  CancelToken,
  RequestManager,
  axios: httpInstance
}