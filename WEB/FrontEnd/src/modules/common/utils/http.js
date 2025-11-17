import axios from 'axios'
import { getApiBaseUrl } from '@/modules/common/utils/api-config.js'

const config = {
  baseURL: getApiBaseUrl(),
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
}

const httpInstance = axios.create(config)

httpInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('Token') || localStorage.getItem('AdminToken')
    if (token) {
      if (localStorage.getItem('AdminToken')) {
        config.headers.Authorization = `Bearer ${token}`
        config.headers.AdminToken = token
      } else {
        config.headers.Token = token
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    config.metadata = { startTime: new Date() }
    if (import.meta.env.DEV) {
      console.log(`🚀 HTTP Request: ${config.method?.toUpperCase()} ${config.url}`)
    }
    return config
  },
  (error) => {
    console.error('HTTP Request Error:', error)
    return Promise.reject(error)
  }
)

httpInstance.interceptors.response.use(
  (response) => {
    const endTime = new Date()
    const duration = endTime - response.config.metadata.startTime
    if (import.meta.env.DEV) {
      console.log(`✅ HTTP Response: ${response.config.method?.toUpperCase()} ${response.config.url} (${duration}ms)`)
    }
    return response
  },
  (error) => {
    const endTime = new Date()
    const duration = error.config?.metadata ? endTime - error.config.metadata.startTime : 0
    if (import.meta.env.DEV) {
      console.error(`❌ HTTP Error: ${error.config?.method?.toUpperCase()} ${error.config?.url} (${duration}ms)`)
    }
    return Promise.reject(error)
  }
)

const baseRequest = (options, errorOptions = {}) => {
  return httpInstance(options)
}

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

export { httpInstance as axios }
export default { get, post, put, del, axios: httpInstance }
