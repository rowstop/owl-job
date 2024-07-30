export enum OwlJobType {
  //一次性的定时任务
  DISPOSABLE = 'DISPOSABLE',
  //固定频率速度轮训
  FIXED_RATE = 'FIXED_RATE',
  //cron
  CRON = 'CRON'
}

/**
 * 任务日志模型
 */
export interface JobLog {
  //命名空间
  namespace: string
  //任务分组
  taskGroup: string
  //任务 id
  taskId: string
  //任务类型
  type: OwlJobType
  //任务执行时间
  execTime: string
  //任务设定时间
  settingTime: string
  //任务参数
  param?: string
  //异常原因
  error?: string
}
