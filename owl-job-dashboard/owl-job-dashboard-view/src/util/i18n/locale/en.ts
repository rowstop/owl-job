import type { ILocal } from '../model'

const en: ILocal = {
  login: {
    username: 'please input a username',
    password: 'please input a password',
    remember: 'remember login status',
    loginButton: 'Login',
    loggedElsewhere: 'Already logged in elsewhere'
  },
  page: {
    overview: 'Overview',
    namespace: {
      name: 'Namespace',
      tableColumns: {
        namespace: 'namespace',
        regTime: 'time of registration',
        groupCount: 'number of groups',
        curTaskCount: 'number of tasks'
      },
      group: {
        title: 'Task Group({namespace})',
        name: 'group name'
      }
    },
    task: {
      name: 'Tasks',
      manage: {
        name: 'Task Manage'
      },
      log: {
        name: 'Execution Log',
        tools: {
          failTitle: 'Error Info',
          success: ' succeed',
          fail: 'failed'
        },
        tableColumns: {
          resultOfExecution: 'result of execution',
          namespace: 'namespace',
          group: 'task group',
          taskId: 'task id',
          execTime: 'time of execution',
          settingTime: 'time of setting',
          timeError: 'time error'
        }
      }
    }
  }
}

export default en
