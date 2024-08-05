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
      type: {
        disposable: 'disposable',
        fixedRate: 'fixed rate',
        cron: 'CRON'
      },
      form: {
        namespace: 'namespace',
        group: 'task group',
        groupValid: 'task group cannot be null',
        type: 'task type',
        taskId: 'task id',
        cron: 'cron',
        cronValid: 'cron expression cannot be null',
        wrongCronValid: 'wrong cron expression',
        execTime: 'first execution time',
        execTimeValid: 'first execution time cannot be null',
        fixedRate: 'fixed rate',
        fixedRateValid: 'fixed rate cannot be null',
        fixedRateNotice:
          'Unit: second. After the first execution, it will be executed cyclically at this fixed frequency.',
        param: 'task param',
        submitButton: 'submit',
        operate: ' operate',
        newButton: 'add a new Task',
        newTitle: '[{namespace}]Add a new task',
        edit: 'edit',
        del: 'delete',
        delConfirm: 'Are you sure to delete this data?'
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
