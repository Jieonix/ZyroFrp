import axios from 'axios'

export const request = (options, loadingStore, showLoading = true) => {
  if (showLoading) {
    loadingStore.showLoading()
  }

  return axios(options)
    .then((response) => {
      if (showLoading) {
        loadingStore.hideLoading()
      }
      return response
    })
    .catch((error) => {
      if (showLoading) {
        loadingStore.hideLoading()
      }
      return Promise.reject(error)
    })
}
