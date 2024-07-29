interface I18NMSg extends Record<string, string | I18NMSg> {}

export interface ILocal extends I18NMSg {
  login: {
    username: string
    password: string
    remember: string
    loginButton: string
    loggedElsewhere: string
  }
  menu: {
    overview: string
    namespace: string
    jobLog: {
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
      }
    }
  }
}

//支持的语言枚举
export enum SupportLang {
  zh = 'zh',
  en = 'en'
}
