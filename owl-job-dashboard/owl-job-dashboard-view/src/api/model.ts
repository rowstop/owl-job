export type FailCallback = (code: number) => void

export interface RequestConfig {
  //请求 uri
  uri: string
  //请求提数据
  data?: object
  //是否需要登录 默认为 true
  auth?: boolean
  //额外的请求头
  headers?: Record<string, string>
  //失败时的回调
  fail?: FailCallback | Record<number, string | FailCallback>
}
