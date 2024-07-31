import type { ILocal } from '../model'

const zh: ILocal = {
  login: {
    username: '请输入用户名',
    password: '请输入密码',
    remember: '记住登录状态',
    loginButton: '登录',
    loggedElsewhere: '已在其他地方登录'
  },
  page: {
    overview: '概况',
    namespace: {
      name: '命名空间',
      tableColumns: {
        namespace: '命名空间',
        regTime: '注册时间',
        groupCount: '分组数量',
        curTaskCount: '任务数量'
      },
      group: {
        title: '任务分组（{namespace}）',
        name: '组名'
      }
    },
    task: {
      name: '任务',
      manage: {
        name: '任务管理'
      },
      log: {
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
          settingTime: '设定时间',
          timeError: '时间误差'
        }
      }
    }
  }
}

export default zh
