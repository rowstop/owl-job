import './assets/css/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
//pinia 持久化插件
import PiniaPersistedState from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'

const app = createApp(App)

//注册 pinia
app.use(createPinia().use(PiniaPersistedState))
//other
app.use(router).mount('#app')
