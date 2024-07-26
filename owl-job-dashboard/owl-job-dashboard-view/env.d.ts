/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE_URL: string
}

//分页查询条件
interface PageParam {
  current: number
  size: number
}

//分页查询结果
interface Page<T> {
  //总数据条数
  total: number
  //当前页数据列表
  records: T[]
}

//分页数据详情 用于分页工具
interface PageInfo {
  current: number
  size: number
  total: number
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
