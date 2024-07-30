import { request } from '../../'
import type { JobLog } from './model'

export const page = (param: PageParam) => {
  return request<Page<JobLog>>({
    uri: '/task/log/page',
    data: param
  })
}
