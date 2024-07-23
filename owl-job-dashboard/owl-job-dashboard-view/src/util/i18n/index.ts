//系统默认语言（浏览器）
import { createI18n } from 'vue-i18n'
import { messages } from './messages'

const i18n = createI18n({
  legacy: false, // 是否使用 composition API 模式
  fallbackLocale: 'en', // 设置备用语言
  messages
})

export default i18n
