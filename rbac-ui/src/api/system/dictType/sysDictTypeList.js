
/*导入工具类*/
import request from "@/utils/request";

const dictType='/sysDictType'

export default {

  /*定义分页查询的组件地址*/
  getByPage(params){
    return request(
      {
        url:`${dictType}/getByPage`,
        method:"post",
        data:params
      }
    )
  },
  /*删除选项*/
  delete(params){
    return request({
      url:`${dictType}/delete/${params}`,
      method:"delete"
    })
  },
  /*添加*/
  save(params){
    return request({
      url:`${dictType}/save`,
      method:"post",
      data:params
    })
  },
  /*更新*/
  update(params){
    return request({
      url:`${dictType}/update`,
      method:"put",
      data:params
    })
  },
  /*根据id查询字典类型*/
  get(params){
    return request({
      url:`${dictType}/get/${params}`,
      method:"get"
    })
  },
  /*查询所有的字典类型 不分页 */
  getAllDictType(){
    return request({
      url:`${dictType}/getAllDictType`,
      method:"get",
    })
  },
  /*刷新缓存*/
  refreshCache(){
    return request({
      url:`${dictType}/refreshCache`,
      method:`get`
    })
  }



}


