import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
/*import componentsRouter from './modules/components'
import chartsRouter from './modules/charts'
import tableRouter from './modules/table'
import nestedRouter from './modules/nested'*/

//默认找到的路由的所有地址 所有地址的跳转组件在这里注册
  //登录组件 以及我们操作的系统组件等

//静态路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  //登录的login的地址，组件会进入 /login/index   view包下
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: {title: 'Dashboard', icon: 'dashboard', affix: true}
      }
    ]
  },

//导入静态路由
  {
    //字典数据管理
    /*隐藏的路由 通过界面点击进入*/
    path: '/system/dictData',
    component: Layout,
    hidden: true,
    children: [
      {
        //路由进入地址
        path: 'index/:dictType',
        //注册路由
        component: () => import('@/views/system/sysDicData/sys-dict-data-list'),
        name: 'Data',
        meta: {title: '字典数据', activeMenu: '/system/dict'}
      }
    ]
  }
]


//动态路由
export const asyncRoutes = [
 /* {
    path: '/system',
    component: Layout,
    redirect: '/system/dictType',
    alwaysShow: true,
    name: 'system',
    meta: {
      title: '系统管理',
      icon: 'lock',
    },
    children: [
      {
        // 字典类型管理
        path: '/dicType',
        component: () => import('@/views/system/sysDictType/sys-dict-type-list'),
        name: 'dictType',
        meta: {
          title: '字典类型管理',
        }
      }, {
        //部门管理
        path: '/sysDept',
        //注册路由
        component: () => import('@/views/system/sysDept/sys-dept-list'),
        name: 'sysDept',
        meta: {title: '部门管理'}
      },{
        //菜单管理
        path: '/sysMenu',
        //注册路由
        component: () => import('@/views/system/sysMenu/sys-menu-list'),
        name: 'sysMenu',
        meta: {title: '菜单管理'}
      },{
        //角色管理
        path: '/sysRole',
        //注册路由
        component: () => import('@/views/system/sysRole/sys-role-list'),
        name: 'sysMenu',
        meta: {title: '角色管理'}
      },{
        //用户管理
        path: '/sysUser',
        //注册路由
        component: () => import('@/views/system/sysUser/sys-user-list'),
        name: 'sysUser',
        meta: {title: '用户管理'}
      }]
  },

*/

  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
