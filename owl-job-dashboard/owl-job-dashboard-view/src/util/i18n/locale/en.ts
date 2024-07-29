import type { ILocal } from '../model'

const en: ILocal = {
  login: {
    username: 'please input a username',
    password: 'please input a password',
    remember: 'remember login status',
    loginButton: 'Login',
    loggedElsewhere: 'Already logged in elsewhere'
  },
  menu: {
    overview: 'Overview',
    namespace: 'Namespace',
    jobLog: {
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
        settingTime: 'time of setting'
      }
    }
  }
}

export default en
