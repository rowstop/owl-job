import { request } from '../'
import type { NamespaceVO } from '@/api/namespace/model'

export const page = (param: PageParam) => {
  return request<Page<NamespaceVO>>({
    uri: '/namespace/page',
    data: param
  })
}
