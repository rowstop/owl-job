import { request } from '../'
import type { GroupPageDTO, GroupVO, NamespaceVO } from './model'

export const page = (param: PageParam) => {
  return request<Page<NamespaceVO>>({
    uri: '/namespace/page',
    data: param
  })
}

export const groupPage = (param: GroupPageDTO) => {
  return request<Page<GroupVO>>({
    uri: '/namespace/group/page',
    data: param
  })
}
