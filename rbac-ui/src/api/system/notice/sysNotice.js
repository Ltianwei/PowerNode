import request from "@/utils/request";

const groupName='sysNotice'

export default {

  /*
  * 分页
  * */
  getByPage(params) {
    return request({
      url: `${groupName}/getByPage`,
      method: "post",
      data: params
    })
  },
  /*
  * 增加用户
  * */
  save(params) {
    return request({
      url: `${groupName}/save`,
      method: "post",
      data: params
    })
  },
  /*
  * 修改用户
  * */
  update(params) {
    return request({
      url: `${groupName}/update`,
      method: "put",
      data: params
    })
  },
  /*
  * 删除用户
  * */
  delete(params) {
    return request({
      url: `${groupName}/delete/` + params,
      method: "delete"
    })
  },
  /*
  * 根据id查询
  * */
  get(params){
    return request({
      url: `${groupName}/get/` + params,
      method: "get"
    })
  }
}
