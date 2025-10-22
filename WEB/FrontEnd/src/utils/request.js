import axios from 'axios'

// 封装的请求函数，接受 options 和 loadingStore 作为参数
export const request = (options, loadingStore, showLoading = true) => {
  // 如果需要显示加载效果，则调用 showLoading
  if (showLoading) {
    loadingStore.showLoading()
  }

  return axios(options)
    .then((response) => {
      // 请求成功时隐藏 loading
      if (showLoading) {
        loadingStore.hideLoading()
      }
      return response
    })
    .catch((error) => {
      // 请求失败时隐藏 loading
      if (showLoading) {
        loadingStore.hideLoading()
      }
      return Promise.reject(error)
    })
}
