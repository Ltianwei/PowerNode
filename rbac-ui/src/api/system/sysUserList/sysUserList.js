import request from "@/utils/request";

const sysUser = '/sysUser'

export default {
  /*
  * 分页
  * */
  getByPage(params) {
    return request({
      url: `${sysUser}/getByPage`,
      method: "post",
      data: params
    })
  },
  /*
  * 增加用户
  * */
  save(params) {
    return request({
      url: `${sysUser}/save`,
      method: "post",
      data: params
    })
  },
  /*
  * 修改用户
  * */
  update(params) {
    return request({
      url: `${sysUser}/update`,
      method: "put",
      data: params
    })
  },
  /*
  * 删除用户
  * */
  delete(params) {
    return request({
      url: `${sysUser}/delete/` + params,
      method: "delete"
    })
  },
  /*
  * 重置密码
  * */
  resetPwd(params) {
    return request({
      url: `${sysUser}/resetPwd/` + params,
      method: "get"
    })
  },
  /*
  * 根据用户id查询
  * */
  get(params) {
    return request({
      url: `${sysUser}/get/` + params,
      method: "get"
    })
  },
  /*
  * 查询所有的用户
  * */
  getAllUser() {
    return request({
      url: `${sysUser}/getAllUser`,
      method: "get"
    })
  }
};


