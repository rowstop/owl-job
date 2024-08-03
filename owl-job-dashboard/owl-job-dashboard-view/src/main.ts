// import './assets/css/main.css'
import { createApp } from 'vue'
//pinia
import { createPinia } from 'pinia'
import PiniaPersistedState from 'pinia-plugin-persistedstate'
//svg icon
import 'virtual:svg-icons-register'
//i18n
import i18n from '@/util/i18n'

import App from './App.vue'
import router from './router'
import '@/assets/css/index.scss'

const app = createApp(App)

//如果不是线上环境则直接全量导入
if (import.meta.env.MODE === 'development') {
  app.use(await import('element-plus'))
}
//注册 pinia
app.use(createPinia().use(PiniaPersistedState)).use(i18n)
//other
app.use(router).mount('#app')
