import { ElMessage } from 'element-plus'
import { useSecurityStore } from '@/stores/security'
import router from '@/router'
import type { RequestConfig } from './model'
import i18n from '@/util/i18n'

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
    body: config.data ? JSON.stringify(config.data) : null
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
  //是否成功
  if (result) {
    result.success = result.code === 200
  }
  if (!result || result.success) {
    return result
  }
  const code = result.code
  if (globalError(code)) {
    return result
  }
  const fail = config.fail
  if (!fail) {
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
    toLoginPage()
    return {}
  }
  //如果已登录 则设置 token 头信息
  if (authed) {
    const { name, value } = security.token
    headers[name] = value
  }
  return headers
}

/**
 * 全局异常处理
 * @param code
 */
function globalError(code: number) {
  //需要登录
  if (code === 1) {
    toLoginPage()
    return true
  }
  //已在其他地方登录
  if (code == 3) {
    ElMessage.error(i18n.global.t('login.loggedElsewhere'))
    toLoginPage(false)
    return true
  }
  return false
}

function toLoginPage(throwError: boolean = true) {
  router.push('/login').catch((e) => console.error('路由跳转失败', e))
  if (throwError) {
    throw new Error('redirect to login')
  }
}
