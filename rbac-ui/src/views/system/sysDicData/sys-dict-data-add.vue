<template>
<div>
<!--
  添加执行的路由 子路由 不需要传参 但需要调用父组件的方法 关闭弹出框
-->
  <el-form :model="form" ref="form" :rules="rules"  size="small" label-width="78px">
    <el-form-item label="字典类型" >
      <el-input v-model="form.dictType" placeholder="请输入字典类型" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item label="数据标签"  prop="dictLabel">
      <el-input v-model="form.dictLabel" placeholder="请输入字典标签"></el-input>
    </el-form-item>
    <el-form-item label="键值">
      <el-input-number placeholder="请输入键值" v-model="form.dictValue" :min="1"></el-input-number>
    </el-form-item>
    <el-form-item label="排序">
      <el-input-number v-model="form.dictSort" placeholder="排序" :min="1"></el-input-number>
    </el-form-item>
    <el-form-item label="皮肤属性">
      <el-select v-model="form.dictSkin" placeholder="请选择皮肤">
<!--        <el-option v-for="skin in skinOptions"   :label="skin.label" :value="skin.value" ></el-option>-->
        <el-option v-for="skin in statusOptions" :label="skin.dictLabel" :value="skin.dictValue"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="备注">
      <el-input type="textarea" placeholder="请输入备注" v-model="form.remark"></el-input>
    </el-form-item>
    <el-form-item label="状态">
      <el-radio-group v-model="form.status">
<!--                  <el-radio :label="0" >正常</el-radio>
                  <el-radio :label="1" >停用</el-radio>-->
        <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click="doAddSysDictData" >添加</el-button>
      <el-button type="warning" size="small" @click="doReset">重置</el-button>
    </el-form-item>
  </el-form>


</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import {Message} from "element-ui";
export default {
  name: "sys-dict-data-add",
  created() {
    this.loadDictDataByDictType();
  },
  props:{
    activeId:{
      default:undefined,
      type:String
    }
  },watch:{
    //监视路由传过来的数据发生改变 赋值给dictType
    activeId:{
      immediate:true,
      handler:function (newVal,oldVal){
        this.form.dictType=newVal
      }
    }
  },
  data(){
    return{
      form:{
        dictType:undefined,
        dictLabel:undefined,
        dictValue:1,
        dictSort:1,
        dictSkin:undefined,
        remark:undefined,
        status:0
      },rules: {
        dictLabel:[{
          required:true,trigger:"blur",message:"字典标签不能为空"
        }]
      },
      statusOptions:[]
    }
  },methods:{
    //引入根据类型查询id
    loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },
    doAddSysDictData(){
      this.$refs["form"].validate((flag)=>{
        if (flag){
          sysDictDataListApi.save(this.form).then((rs)=>{
            //关闭页面 刷新页面
            Message.success(rs.msg)
            this.form={
              dictValue:1,
              dictSort:1,
              status:0
            }
            this.$emit("close");
          })
        }
      })

    },
    doReset(){
      this.form={
        dictValue:1,
        dictSort:1,
        status:0
      }
    }
}
}
</script>

<style scoped>

</style>
