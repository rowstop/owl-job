import { request } from '../'
import type { OverviewDTO, OverviewVO } from './model'

export * from './model'

export const namespace = () => {
  return request<string[]>({
    uri: '/overview/namespace'
  })
}

export const count = (data: OverviewDTO = {}) => {
  return request<OverviewVO>({
    uri: '/overview/count',
    data
  })
}
