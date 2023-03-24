import request from "@/utils/request";

const groupName='/sysOperLog'

export default {
  //分页
  getByPage(params){
    return request({
      url:`${groupName}/getByPage`,
      method:"post",
      data:params
    })
  },
  //删除单个
  delete(params){
    return request({
      url:`${groupName}/delete/`+params,
      method:"delete"
    })
  },
  //批量删除
  batchDelete(params){
    return request({
      url:`${groupName}/delete/`+params,
      method:"delete"
    })
  },
  //删除全部
  cleanOperLog(){
    return request({
      url:`${groupName}/cleanOperLog`,
      method:"delete"
    })
  },
  //查询单个
  get(params){
    return request({
      url:`${groupName}/get/`+params,
      method:"get"
    })
  }


}
