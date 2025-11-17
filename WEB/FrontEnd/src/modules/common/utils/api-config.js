import axios from "axios"

const ensureTrailingSlash = (url) => url.endsWith('/') ? url : `${url}/`
const stripTrailingSlash = (url) => url.endsWith('/') ? url.slice(0, -1) : url

const REQUIRED_ENV_VARS = ['ZYRO_ENV', 'ZYRO_BACKEND_PORT', 'ZYRO_API_ORIGIN']

const env = REQUIRED_ENV_VARS.reduce((acc, key) => {
  const value = import.meta.env[key]
  if (!value || !value.toString().trim()) {
    throw new Error(`[config] 环境变量 ${key} 未设置，无法确定后端地址`)
  }
  acc[key] = value.toString().trim()
  return acc
}, {})

const AUTO_KEYWORD = 'auto'

const resolveOrigin = () => {
  const originSetting = env.ZYRO_API_ORIGIN.toLowerCase()

  if (originSetting === AUTO_KEYWORD) {
    if (typeof window === 'undefined') {
      throw new Error('[config] ZYRO_API_ORIGIN=auto 仅在浏览器环境可用')
    }

    const { protocol, hostname } = window.location
    const normalizedProtocol = protocol === 'https:' ? 'https' : 'http'
    const portSegment = env.ZYRO_BACKEND_PORT ? `:${env.ZYRO_BACKEND_PORT}` : ''
    return `${normalizedProtocol}://${hostname}${portSegment}`
  }

  return stripTrailingSlash(env.ZYRO_API_ORIGIN)
}

const getBackendBaseUrl = () => ensureTrailingSlash(resolveOrigin())

function getApiBaseUrl() {
  return getBackendBaseUrl()
}

function resolveApiUrl(path = '') {
  const base = getApiBaseUrl()
  if (!path || path === '/') {
    return base
  }

  const normalizedPath = path.startsWith('/') ? path.slice(1) : path
  return `${base}${normalizedPath}`
}

function getStaticBaseUrl() {
  return resolveApiUrl('static/')
}

axios.defaults.baseURL = getApiBaseUrl()

const getRuntimeEnv = () => env.ZYRO_ENV

export {
  getApiBaseUrl,
  getStaticBaseUrl,
  resolveApiUrl,
  getRuntimeEnv,
}
