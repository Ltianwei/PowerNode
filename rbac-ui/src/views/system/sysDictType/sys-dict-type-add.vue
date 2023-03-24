<template>
<div>
  <el-form :model="form" ref="form"  :rules="rules"  size="small" label-width="78px">
    <el-form-item label="字典名称" prop="dictName">
      <el-input v-model="form.dictName" placeholder="请输入字典名称"></el-input>
    </el-form-item>
    <el-form-item label="字典类型" prop="dictType">
      <el-input v-model="form.dictType" placeholder="请输入字典类型"></el-input>
    </el-form-item>
    <el-form-item label="备注">
      <el-input type="textarea" placeholder="请输入备注" v-model="form.remark"></el-input>
    </el-form-item>
    <el-form-item label="状态">
      <el-radio-group v-model="form.status">
<!--        <el-radio :label="0" >正常</el-radio>
        <el-radio :label="1" >停用</el-radio>-->
        <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue" >{{dictData.dictLabel}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click="doAddDictType">添加</el-button>
      <el-button type="warning" size="small" @click="doReset">重置</el-button>
    </el-form-item>
  </el-form>


</div>
</template>

<script>
import sysDictTypeListApi from "@/api/system/dictType/sysDictTypeList";
import sysDictDataList from "@/api/system/dictData/sysDictDataList";
import {Message} from "element-ui";

export default {
  name: "sys-dict-type-add",
  created() {
    this.loadDictDataByDictType()
  },
  data(){
    return{
      form:{
        dictName:undefined,
        dictType:undefined,
        remark:undefined,
        status:0
      },rules:{
        dictName:[{required:true,trigger:"blur",message:"名字不能为空"}],
        dictType:[{required:true,trigger:"blur",message:"类型不能为空"}],
      },
      statusOptions:[]
    }
  },methods:{
    loadDictDataByDictType(){
      sysDictDataList.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },
    doAddDictType(){
      this.$refs["form"].validate(flag=>{
        if(flag){
          sysDictTypeListApi.save(this.form).then((rs)=>{
            /*关闭窗口 并且将成功信息打印 清除数据*/
            Message.success(rs.msg)
            this.doReset();
            //调用父类的方法 进行关闭 这里调用事件的名字
            this.$emit("close")
          })
        }
      })
    },
    doReset(){
      this.form={
          status:0
      }
    }
  }
}
</script>

<style scoped>

</style>
