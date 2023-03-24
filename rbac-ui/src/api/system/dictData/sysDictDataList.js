
/*导入工具类*/
import request from "@/utils/request";

const dictData=`/sysDictData`

export default {

  /*分页查询所有数据*/
  getByPage(params){
    return request({
      url:`${dictData}/getByPage`,
      method:"post",
      data:params
    })
  },

  /*添加数据*/
  save(params){
    return request({
      url:`${dictData}/save`,
      method:"post",
      data:params
    })
  },

  /*修改数据*/
  update(params){
    return request({
      url:`${dictData}/update`,
      method:"put",
      data:params
    })
  },
  /*根据id查询字典数据*/
  get(params){
    return request({
      url:`${dictData}/get/`+params,
      method:"get"
    })
  },
  /*删除字典数据*/
  delete(params){
    return request({
      url:`${dictData}/delete/`+params,
      method:"delete"
    })
  },
  /*根据字典类型获取字典数据*/
  getDictDataByDictType(params){
    return request({
      url:`${dictData}/getDictDataByDictType/`+params,
      method:"get"
    })

  }

}
