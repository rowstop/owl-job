import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomePage.vue'
import { useSecurityStore } from '@/stores/security'
import { Document, Menu as IconMenu } from '@element-plus/icons-vue'

let security: ReturnType<typeof useSecurityStore>

//mainRoute
export const mainRoute: RouteRecordRaw = {
  path: '/',
  component: HomeView,
  meta: {
    auth: true
  },
  children: [
    {
      path: '',
      name: 'home',
      component: () => import('../views/pages/Overview.vue'),
      meta: {
        icon: IconMenu,
        title: 'menu.overview'
      }
    },
    {
      path: 'namespace',
      name: 'namespace',
      component: () => import('../views/pages/Namespace.vue'),
      meta: {
        icon: Document,
        title: 'menu.namespace'
      }
    },
    {
      path: 'job/log',
      name: 'jobLog',
      component: () => import('../views/pages/JobLog.vue'),
      meta: {
        icon: Document,
        title: 'menu.jobLog.name'
      }
    }
  ]
}
//createRouter
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginPage.vue'),
      meta: {
        auth: false
      }
    },
    mainRoute
  ]
})
//路由水位
router.beforeEach((to, from, next) => {
  if (!to.meta?.auth || securityLazyLoad()?.authed) {
    next()
    return
  }
  next('/login')
})

//懒加载 security store
function securityLazyLoad() {
  if (security) {
    return security
  }
  return (security = useSecurityStore())
}

export default router
