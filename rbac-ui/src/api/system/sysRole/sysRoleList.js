import request from "@/utils/request";

const sysRole = "/sysRole"

export default {
  /*
  * 分页查询
  * */
  getByPage(params) {
    return request({
      url: `${sysRole}/getByPage`,
      method: "post",
      data: params
    })
  },/*
  * 添加角色
  * */
  save(params) {
    return request({
      url: `${sysRole}/save`,
      method: "post",
      data: params
    })
  },
  /*
  * 更改角色
  * */
  update(params) {
    return request({
      url: `${sysRole}/update`,
      method: "put",
      data: params
    })
  },
  /*
  * 删除角色
  * */
  delete(params) {
    return request({
      url: `${sysRole}/delete/` + params,
      method: "delete",
    })
  },
  /*
  * 根据id查询角色
  * */
  get(params) {
    return request({
      url: `${sysRole}/get/` + params,
      method: "get"
    })
  },
  /*
  * 根据角色id查询菜单权限
  * */
  getCurrentRoleHasMenuIdsByRoleId(params) {
    return request({
      url: `${sysRole}/getCurrentRoleHasMenuIdsByRoleId/` + params,
      method: "get"
    })
  },
  /*
  * 保存用户与角色间的关系
  * 一个参数与数组
  * 接收时为数组 需要进行拼接
  * */
  saveRoleMenu(roleId, menuIds) {
    let params = "roleId=" + roleId
    menuIds.filter(menu => {
      params += "&menuIds=" + menu
    })
    return request({
      url: `${sysRole}/saveRoleMenu?` + params,
      method: "post"
    })
  },
  /*
  * 查询所有的角色 不分页
  * */
  getAllRoles() {
    return request({
      url: `${sysRole}/getAllRoles`,
      method: "get"
    })
  },

  /*
    * 根据用户id查询对应的角色id
    * */
  getRoleIdsByUserId(params) {
    return request({
      url: `${sysRole}/getRoleIdsByUserId/` + params,
      method: "get"
    })
  },

  /*
  * 添加用户的角色
  * */
  saveUserRoles(userId,roleIds){
    let params="userId="+userId
    roleIds.filter(role=>{
      params+= "&roleIds="+role
    })
    return request({
      url: `${sysRole}/saveUserRoles?` + params,
      method: "post"
    })
  }


}
