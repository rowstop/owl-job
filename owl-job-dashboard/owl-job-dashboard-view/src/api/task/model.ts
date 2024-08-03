export enum TaskType {
  //一次性的定时任务
  DISPOSABLE = 'DISPOSABLE',
  //固定频率速度轮训
  FIXED_RATE = 'FIXED_RATE',
  //cron
  CRON = 'CRON'
}

export const TaskDesc: Record<TaskType, string> = {
  [TaskType.DISPOSABLE]: 'page.task.type.disposable',
  [TaskType.FIXED_RATE]: 'page.task.type.fixedRate',
  [TaskType.CRON]: 'page.task.type.cron'
}

export interface TaskPageDTO extends PageParam {
  namespace: string
}

export interface TaskVO {
  group: string
  taskId: string
  type: TaskType
  nextTime: string
  param: string
}

export interface TaskKeyDTO extends GroupKey {
  taskId: string
}

export interface TaskAddDTO extends GroupKey {
  type: TaskType
  taskId?: string
  execTime?: string
  param?: string
  cron?: string
  fixedRateSeconds?: number
}
