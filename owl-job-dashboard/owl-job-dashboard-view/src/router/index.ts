import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomePage.vue'
import { useSecurityStore } from '@/stores/security'

let security: ReturnType<typeof useSecurityStore>
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '主页',
      component: HomeView,
      meta: {
        auth: true
      }
    },
    {
      path: '/login',
      name: '登录',
      component: () => import('../views/LoginPage.vue'),
      meta: {
        auth: false
      }
    }
  ]
})
//路由水位
router.beforeEach((to, from, next) => {
  if (to.meta?.auth || securityLazyLoad()?.authed) {
    next()
    return
  }
  next('/login')
})

//懒加载 security store
function securityLazyLoad() {
  if (security != null) {
    return security
  }
  security = useSecurityStore()
}

export default router
