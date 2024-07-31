import { TaskType } from '../model'

/**
 * 任务日志模型
 */
export interface TaskLog {
  //命名空间
  namespace: string
  //任务分组
  taskGroup: string
  //任务 id
  taskId: string
  //任务类型
  type: TaskType
  //任务执行时间
  execTime: string
  //任务设定时间
  settingTime: string
  //任务参数
  param?: string
  //异常原因
  error?: string
  //运行时间误差 单位毫秒
  timeError: number
}
