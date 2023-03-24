<template>
<div>
  <el-form :model="form" :rules="rules" ref="form" label-width="78px">
    <el-form-item label="字典名称" prop="dictName">
      <el-input v-model="form.dictName" placeholder="请输入字典名称"></el-input>
    </el-form-item>
    <el-form-item label="字典类型" prop="dictType">
      <el-input v-model="form.dictType" placeholder="请输入字典类型"></el-input>
    </el-form-item>
    <el-form-item label="备注">
      <el-input type="textarea" placeholder="备注" v-model="form.remark"></el-input>
    </el-form-item>
    <el-form-item label="状态">
      <el-radio-group v-model="form.status">
        <el-radio :label="0">正常</el-radio>
        <el-radio :label="1">停用</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="doUpdateSysDictType">更新</el-button>
      <el-button type="warning" @click="doReset">重置</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
import sysDictTypeListApi from "@/api/system/dictType/sysDictTypeList";
import {Message} from "element-ui";
export default {
  name: "sys-dict-type-update.vue",
  props:{
    /*接受父类传输的数据 声明默认值 类型*/
    activeId:{
      default:undefined,
      type:Number
    }
  },watch:{
    /*监视器 当传过来的值发生变化时 会实时更新*/
    activeId:{
      immediate:true,
      handler:function (newVal,oldVal){
        /*这里的newVal就是传过来的值*/
        sysDictTypeListApi.get(this.activeId).then(rs=>{
          this.form=rs.data
        })
      }
    }
  },
  created() {
    this.loadDictDataByDictType();
  },
  data(){
    return{
      form:{
        dictId:undefined,
        dictName:undefined,
        dictType:undefined,
        remark:undefined,
        status:0,
      },rules:{
        dictName:[{required:true,trigger:"blur",message:"名字不能为空"}],
        dictType:[{required:true,trigger:"blur",message:"类型不能为空"}],
      }

    }
  },methods:{
    doUpdateSysDictType(){
      //更新操作
      this.$refs["form"].validate(flag=>{
        if (flag){
          sysDictTypeListApi.update(this.form).then((rs)=>{
            Message.success(rs.msg)
            this.doReset();
            //更新完毕 调用父类的方法
            this.$emit("close")
          })
        }
      })

    },doReset(){
      //重置
      this.form={
        status:0
      }
    }

  }
}
</script>

<style scoped>

</style>
