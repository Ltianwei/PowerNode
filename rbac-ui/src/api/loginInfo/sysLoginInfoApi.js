import request from "@/utils/request";

const groupName = '/sysLoginInfo'

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
  * 根据删除日志
  * */
  delete(params) {
    return request({
      url: `${groupName}/delete/` + params,
      method: "delete"
    })
  },
  /*
  * 批量删除
  * */
  batchDelete(params) {
    return request({
      url: `${groupName}/batchDelete/` + params,
      method: "delete"
    })
  },
  /*
  * 删除所有
  * */
  cleanLoginInfo(){
    return request({
      url: `${groupName}/cleanLoginInfo/`,
      method: "delete"
    })
  }
};


