/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE_URL: string
}

// 大整形数字
type Long = number | string

//可以为空的对象
type Nullable<T> = undefined | null | T

//统一响应结果
interface Result<T> {
  code: number
  data: T
  msg: string
}
