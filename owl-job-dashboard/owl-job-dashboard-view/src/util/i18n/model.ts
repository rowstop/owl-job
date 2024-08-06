interface I18NMSg extends Record<string, string | I18NMSg> {}

export interface ILocal extends I18NMSg {
  common: {
    namespace: string
    all: string
  }
  login: {
    username: string
    password: string
    remember: string
    loginButton: string
    loggedElsewhere: string
  }
  page: {
    overview: {
      name: string
      error: string
      success: string
      pieTitle: string
      lineTitle: string
    }
    namespace: {
      name: string
      tableColumns: {
        namespace: string
        regTime: string
        groupCount: string
        curTaskCount: string
      }
      group: {
        title: string
        name: string
      }
    }
    task: {
      name: string
      manage: {
        name: string
      }
      type: {
        disposable: string
        fixedRate: string
        cron: string
      }
      form: {
        namespace: string
        group: string
        groupValid: string
        type: string
        taskId: string
        execTime: string
        execTimeValid: string
        fixedRate: string
        fixedRateValid: string
        cron: string
        cronValid: string
        wrongCronValid: string
        fixedRateNotice: string
        param: string
        submitButton: string
        newButton: string
        newTitle: string
        operate: string
        edit: string
        del: string
        delConfirm: string
      }
      log: {
        name: string
        tools: {
          failTitle: string
          success: string
          fail: string
        }
        tableColumns: {
          resultOfExecution: string
          namespace: string
          group: string
          taskId: string
          execTime: string
          settingTime: string
          timeError: string
        }
      }
    }
  }
}

//支持的语言枚举
export enum SupportLang {
  zh = 'zh',
  en = 'en'
}
