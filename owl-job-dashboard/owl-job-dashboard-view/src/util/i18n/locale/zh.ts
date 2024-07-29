import type { ILocal } from '../model'

const zh: ILocal = {
  login: {
    username: '请输入用户名',
    password: '请输入密码',
    remember: '记住登录状态',
    loginButton: '登录',
    loggedElsewhere: '已在其他地方登录'
  },
  menu: {
    overview: '概况',
    namespace: '命名空间',
    jobLog: {
      name: '调度日志',
      tools: {
        failTitle: '错误信息',
        success: '成功',
        fail: '失败'
      },
      tableColumns: {
        resultOfExecution: '执行结果',
        namespace: '命名空间',
        group: '任务分组',
        taskId: '任务 ID',
        execTime: '执行时间',
        settingTime: '设定时间'
      }
    }
  }
}

export default zh
