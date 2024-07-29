//系统默认语言（浏览器）
import { createI18n } from 'vue-i18n'
import { type ILocal, SupportLang } from '@/util/i18n/model'

const modules = import.meta.glob<true, string, ILocal>('./locale/*.ts', {
  import: 'default',
  eager: true
})
const messages: Record<string, ILocal> = {}
for (const key in modules) {
  const locale = key.substring(key.lastIndexOf('/') + 1).split('.')[0]
  messages[locale] = modules[key]
}

const i18n = createI18n({
  legacy: false,
  fallbackLocale: SupportLang.en, // 设置备用语言
  messages
})

export default i18n
