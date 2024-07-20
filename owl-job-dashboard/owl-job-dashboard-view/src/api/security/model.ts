/**
 * 登录参数类型
 */
export interface LoginParam {
  //用户名
  username: string
  //密码
  password: string
}

/**
 * 登录响应参数
 */
export interface LoginResp {
  tokenName: string
  tokenValue: string
}
