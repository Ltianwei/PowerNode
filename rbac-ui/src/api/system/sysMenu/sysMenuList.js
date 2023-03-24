import request from "@/utils/request";

const sysMenu = '/sysMenu'

export default {

  /*
  * 根据名字以及状态 查询所有菜单
  * */
  listDept(params) {
    return request({
      url: `${sysMenu}/listMenu/`,
      method: "get",
      params: params
    })
  },
  /*
  * 添加菜单
  * */
  save(params) {
    return request({
      url: `${sysMenu}/save`,
      method: "post",
      data: params
    })
  },
  /*
  * 更改菜单
  * */
  update(params) {
    return request({
      url: `${sysMenu}/update`,
      method: "put",
      data: params
    })
  },
  /*
  * 删除菜单根据id(本质更改部门的删除状态)
  * */
  delete(params) {
    return request({
      url: `${sysMenu}/delete/` + params,
      method: "delete",
    })
  },
  /*
  * 根据id查询菜单
  * */
  get(params) {
    return request({
      url: `${sysMenu}/get/` + params,
      method: "get"
    })
  },
  /*
  * 只查询菜单和目录
  * */
  listMenuMC() {
    return request({
      url: `${sysMenu}/listMenuMC`,
      method: "get"
    })
  },
  /**
   * 获取动态路由
   */
  getRouters() {
    return request({
      url: `${sysMenu}/getRouters`,
      method: "get"
    })
  }


}
