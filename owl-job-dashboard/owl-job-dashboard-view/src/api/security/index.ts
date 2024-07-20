import { request } from '../'
import type { LoginParam, LoginResp } from './model'

/**
 * 登录请求
 */
export const login = (data: LoginParam) => {
  return request<LoginResp>({
    uri: '/login',
    data,
    auth: false,
    fail: { 2: '用户名密码错误' }
  })
}
