export enum TaskType {
  //一次性的定时任务
  DISPOSABLE = 'DISPOSABLE',
  //固定频率速度轮训
  FIXED_RATE = 'FIXED_RATE',
  //cron
  CRON = 'CRON'
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

export interface TaskKeyDTO {
  namespace: string
  group: string
  taskId: string
}
