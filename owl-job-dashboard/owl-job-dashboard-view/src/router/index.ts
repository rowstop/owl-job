import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomePage.vue'
import { useSecurityStore } from '@/stores/security'
import { DataAnalysis, Document, Monitor, SetUp } from '@element-plus/icons-vue'
import { Task } from '@/components/icon'

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
      name: 'Overview',
      component: () => import('../views/pages/Overview.vue'),
      meta: {
        icon: Monitor,
        title: 'page.overview'
      }
    },
    {
      path: 'namespace',
      name: 'Namespace',
      component: () => import('../views/pages/Namespace.vue'),
      meta: {
        icon: Document,
        title: 'page.namespace.name'
      }
    },
    {
      path: 'task',
      name: 'task',
      meta: {
        icon: Task,
        title: 'page.task.name'
      },
      children: [
        {
          path: 'manage',
          name: 'TaskManage',
          component: () => import('../views/pages/task/TaskManage.vue'),
          meta: {
            icon: SetUp,
            title: 'page.task.manage.name'
          }
        },
        {
          path: 'log',
          name: 'TaskLog',
          component: () => import('../views/pages/task/TaskLog.vue'),
          meta: {
            icon: DataAnalysis,
            title: 'page.task.log.name'
          }
        }
      ]
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
