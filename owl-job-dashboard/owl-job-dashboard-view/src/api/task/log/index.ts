import { request } from '../../'
import type { TaskLog } from './model'

export const page = (param: PageParam) => {
  return request<Page<TaskLog>>({
    uri: '/task/log/page',
    data: param
  })
}
