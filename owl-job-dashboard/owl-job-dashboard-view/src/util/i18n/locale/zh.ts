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
      type: {
        disposable: '一次性',
        fixedRate: '固定频率',
        cron: 'CRON'
      },
      form: {
        namespace: '命名空间',
        group: '任务组',
        groupValid: '任务组不能为空',
        type: '任务类型',
        taskId: '任务 id',
        cron: 'cron 表达式',
        cronValid: 'cron 表达式不能为空',
        wrongCronValid: 'cron 表达式不正确',
        execTime: '首次执行时间',
        execTimeValid: '首次执行时间不能为空',
        fixedRate: '固定执行频率',
        fixedRateValid: '固定执行频率不能为空',
        fixedRateNotice: '单位：秒，首次执行之后，以此固定频率循环执行',
        param: '任务参数',
        submitButton: '保存',
        newButton: '新增任务',
        newTitle: '【{namespace}】新增任务',
        operate: '操作',
        edit: '编辑',
        del: '删除'
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
          timeError: '误差'
        }
      }
    }
  }
}

export default zh
