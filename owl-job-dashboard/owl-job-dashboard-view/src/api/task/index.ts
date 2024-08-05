import { request } from '../'
import type { TaskAddDTO, TaskKeyDTO, TaskPageDTO, TaskVO } from './model'
//任务分页查询
export const page = (data: TaskPageDTO) => {
  return request<Page<TaskVO>>({
    uri: '/task/page',
    data
  })
}

//删除任务
export const del = (data: TaskKeyDTO) => {
  return request<void>({
    uri: '/task/delete',
    data
  })
}

//新增任务
export const add = (data: TaskAddDTO) => {
  return request<void>({
    uri: '/task/add',
    data
  })
}

export * from './model'
