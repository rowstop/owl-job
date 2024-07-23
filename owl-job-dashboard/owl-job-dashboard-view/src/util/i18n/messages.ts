//支持的语言枚举
export enum SupportLang {
  zh = 'zh',
  en = 'en'
}

//各语言及对应国际化
export const messages: Record<SupportLang, ILocal> = {
  zh: {
    menu: {
      overview: '概况',
      namespace: '命名空间'
    }
  },
  en: {
    menu: {
      overview: '1overview',
      namespace: '2namespace'
    }
  }
}

interface I18NMSg extends Record<string, string | I18NMSg> {}

export interface ILocal extends I18NMSg {
  menu: {
    overview: string
    namespace: string
  }
}
