import { ElMessage } from 'element-plus'
import { useSecurityStore } from '@/stores/security'
import router from '@/router'
import type { RequestConfig } from './model'

const baseUrl = import.meta.env.VITE_BASE_URL
const security = useSecurityStore()

/**
 * 发送请求
 * @param config
 */
export const request = async <T>(config: RequestConfig) => {
  const url = baseUrl + config.uri
  const init: RequestInit = {
    method: 'POST',
    headers: getHeaders(config),
    body: JSON.stringify(config.data)
  }
  const response = await fetch(url, init)
  const resp = await response.json()
  return handleResult(config, resp as Result<T>)
}

/**
 * 处理认证结果
 * @param config 请求配置
 * @param result 响应结果
 */
function handleResult<T>(config: RequestConfig, result: Result<T>) {
  const fail = config.fail
  let code
  if (!result || 200 === (code = result.code) || !fail) {
    return result
  }
  //if function
  if (typeof fail === 'function') {
    fail(code)
    throw new Error(result.msg || '')
  }
  //else record
  const handle = fail[code]
  if (typeof handle === 'function') {
    handle(code)
    throw new Error(result.msg || '')
  }
  ElMessage.error({
    message: handle,
    grouping: true
  })
  return result
}

/**
 * 获取请求头
 * @param config 请求配置信息
 */
function getHeaders(config: RequestConfig) {
  const headers = config.headers ?? {}
  //如果 'Content-Type' 不为空 则把'Content-Type'设置为 application/json
  if (!headers['Content-Type']) {
    headers['Content-Type'] = 'application/json'
  }
  //判断是否登录
  const authed = security.authed
  //如果未登录但是接口需要登录 则直接调转到登录页面
  if (!authed && (undefined == config.auth || config.auth)) {
    router.push('/login').catch((e) => console.log('路由跳转失败', e))
    throw new Error('need login,redirect to login')
  }
  //如果已登录 则设置 token 头信息
  if (authed) {
    const { name, value } = security.token
    headers[name] = value
  }
  return headers
}
