import { request } from '../'
import type { TaskKeyDTO, TaskPageDTO, TaskVO } from './model'

export const page = (data: TaskPageDTO) => {
  return request<Page<TaskVO>>({
    uri: '/task/page',
    data
  })
}

export const del = (data: TaskKeyDTO) => {
  return request<void>({
    uri: '/task/delete',
    data
  })
}

export * from './model'
