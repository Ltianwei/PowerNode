
import request from "@/utils/request";

const sysDept='/sysDept'

export default {

  /*
  * 根据名字以及状态 查询所有部门
  * */
  listDept(params){
    return request({
      url:`${sysDept}/listDept/`,
      method:"get",
      params: params
    })
  },
  /*
  * 添加部门
  * */
  save(params){
    return request({
      url:`${sysDept}/save`,
      method:"post",
      data:params
    })
  },
  /*
  * 更改部门参数
  * */
  update(params){
    return request({
      url:`${sysDept}/update`,
      method:"put",
      data:params
    })
  },
  /*
  * 删除部门根据id(本质更改部门的删除状态)
  * */
  delete(params){
    return request({
      url:`${sysDept}/delete/`+params,
      method:"delete",
    })
  },
  /*
  * 根据id查询部门
  * */
  get(params){
    return request({
      url:`${sysDept}/get/`+params,
      method:"get"
    })
  }



}
