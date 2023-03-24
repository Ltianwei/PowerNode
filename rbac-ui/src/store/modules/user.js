import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'



const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login 登录时会找到当前方法
  login({ commit }, userInfo) {
    //获取传入的用户信息 用userInfo接收
    const { username, password,code } = userInfo
    return new Promise((resolve, reject) => {
      //执行axios的操作时 执行login操作 该login方法是api/user/login方法（鼠标移动至login处可以看到）
      //将表单中的参数进行提交
      login({ username: username.trim(), password: password,code:code }).then(response => {
        //用data接收响应值
        const { data } = response
        //将返回参数（响应对象的值）进行接收并设置在表头set_token中 --> 路由守卫 permission
        commit('SET_TOKEN', data)
        setToken(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        /*获取存入的用户信息*/
        const {permissions,user}=data
        commit('SET_ROLES', permissions)
        commit('SET_NAME', user.userName)

       // commit('SET_AVATAR', "https://pic.baike.soso.com/ugc/baikepic2/0/20220503104849-1407144212_jpeg_700_699_37960.jpg/800")
        commit('SET_AVATAR', "https://ts1.cn.mm.bing.net/th/id/R-C.812abb5f1a3e849b780284f5a4f9f0e6?rik=CVfE%2fropyiX6QQ&riu=http%3a%2f%2fb.hiphotos.baidu.com%2fzhidao%2fpic%2fitem%2fa2cc7cd98d1001e9f0ff907eb90e7bec55e79784.jpg&ehk=VoajgSjNlLmAapK8RN28NJZJ637PB8IPOPhDd%2bcs%2bgA%3d&risl=&pid=ImgRaw&r=0")
       /* commit('SET_AVATAR', avatar)*/
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout 登出 将session中的值置空
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
