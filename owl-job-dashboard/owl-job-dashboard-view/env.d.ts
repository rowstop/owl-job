/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE_URL: string
}

//分页查询条件
interface PageParam {
  page: number
  size: number
}

//分页查询结果
interface Page<T> {
  content: T[]
  totalPages: number
  size: number
  empty: boolean
  first: boolean
  last: boolean
  number: number
  pageable: {
    offset: number
    pageNumber: number
    pageSize: number
    paged: boolean
  }
  numberOfElements: number
  sort: { sorted: boolean; unsorted: boolean; empty: boolean }
  totalElements: number
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
