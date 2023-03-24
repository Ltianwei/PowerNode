import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

//设置白名单 白名单目的是当前地址可以放行
const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

//页面跳转之前执行的方法
router.beforeEach(async(to, from, next) => {
  //开始加载进度条
  NProgress.start()

  //设置页面标题
  document.title = getPageTitle(to.meta.title)

  //获取用户登录token 执行登录操作后(modeles/user.js)中会将当前响应对象写进token
  const hasToken = getToken()
  //当前路由是否携带token
  if (hasToken) {
    //如果进入的地址是/login
    if (to.path === '/login') {
      //如果是 进行跳转主页面 防止进行二次登录
      next({ path: '/' })
        //结束进度条
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // 如果不是去登录页面 获取角色信息（因为要根据角色信息分配权限，并不是所有的动态路由每个角色都可访问）
      // 角色信息中包含每个用户的权限 已经在rooter.index的静态路由中将权限通过数组方式确定 roles: ['admin', 'editor']
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      // 如果存在角色信息
      if (hasRoles) {
        //进行跳转 此时会分配相应的动态路由
        next()
      } else {
        try {
          // 如果不存在角色信息 通过user下的getInfo获取用户角色信息
          const { permissions } = await store.dispatch('user/getInfo')

          // 根据当前角色信息 获取含有的动态路由列表  permission/generateRoutes 可以获取所有的动态路由列表
          const accessRoutes = await store.dispatch('permission/generateRoutes', permissions)

          // 添加动态路由 将动态路由添加到路由中进行展示
          router.addRoutes(accessRoutes)

         //执行下一个路由
          next({ ...to, replace: true })
        } catch (error) {
          // 出现异常回到登录界面
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    // 未登录时 不存在token  需要判断是进行哪个页面的访问 如果是白名单中的页面 可以放行
    //页面加载进入登录页面时 也进行了页面的跳转 如果不放行 页面无法显示
    //白名单 某些路由不需要token即可访问
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // 不在白名单直接回到登录界面
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
