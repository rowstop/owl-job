interface I18NMSg extends Record<string, string | I18NMSg> {}

export interface ILocal extends I18NMSg {
  login: {
    username: string
    password: string
    remember: string
    loginButton: string
  }
  menu: {
    overview: string
    namespace: string
  }
}

//支持的语言枚举
export enum SupportLang {
  zh = 'zh',
  en = 'en'
}

//各语言及对应国际化
export const messages: Record<SupportLang, ILocal> = {
  zh: {
    login: {
      username: '请输入用户名',
      password: '请输入密码',
      remember: '记住登录状态',
      loginButton: '登录'
    },
    menu: {
      overview: '概况',
      namespace: '命名空间'
    }
  },
  en: {
    login: {
      username: 'please input a username',
      password: 'please input a password',
      remember: 'remember login status',
      loginButton: 'Login'
    },
    menu: {
      overview: 'overview',
      namespace: 'namespace'
    }
  }
}
