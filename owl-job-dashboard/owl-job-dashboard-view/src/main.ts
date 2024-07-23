import './assets/css/main.css'
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

const app = createApp(App)

//注册 pinia
app.use(createPinia().use(PiniaPersistedState)).use(i18n)
//other
app.use(router).mount('#app')
