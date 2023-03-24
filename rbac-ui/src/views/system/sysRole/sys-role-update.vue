<template>
<div>
  <el-form :model="form" ref="form" :rules="rules"  size="small" label-width="78px">
    <el-form-item label="角色名称"  prop="roleName">
      <el-input v-model="form.roleName" placeholder="请输入角色名称"></el-input>
    </el-form-item>
    <el-form-item label="排序"  prop="roleSort">
      <el-input-number v-model="form.roleSort" :min="1" ></el-input-number>
    </el-form-item>
    <el-form-item label="备注">
      <el-input type="textarea" placeholder="角色备注" v-model="form.remark"></el-input>
    </el-form-item>
    <el-form-item label="状态">
      <el-radio-group v-model="form.status">
        <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="small" @click="doUpdateSysRole" >更新</el-button>
      <el-button type="warning" size="small" @click="doReset">重置</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysRoleListApi from "@/api/system/sysRole/sysRoleList";
import {Message} from "element-ui";

export default {
  name: "sys-role-update",
  props:{
    updateId:{
      type:Number,
      default:undefined
    }
  },watch:{
    updateId:{
      immediate:true,
      handler:function (val, oldVal) {
        this.doSearch(val);
      }
    }

  },
  created() {
    this.loadDictDataByDictType()
  },
  data(){
    return{
      form:{
        roleName:undefined,
        roleSort:1,
        remark:undefined,
        status:undefined
      },rules:{
        roleName:[
          {
            required:true,trigger:"blur",message:"请指定角色名称!"
          }
        ],
        roleSort:[
          {
            required:true,trigger:"blur",message:"请指定角色排序!"
          }
        ]
      },statusOptions:[]
    }
  },
  methods:{
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then((rs) => {
        this.statusOptions = rs.data
      })
    },doReset(){
      this.form={
        roleName:undefined,
        roleSort:1,
        remark:undefined,
        status:0
      }
    },doUpdateSysRole(){
      this.$refs['form'].validate(flag=>{
        if (flag){
          sysRoleListApi.update(this.form).then(rs=>{
            Message.success(rs.msg)
            this.doReset()
            this.$emit("close")
          })
        }
      })

    },doSearch(val){
      sysRoleListApi.get(val).then(rs=>{
        this.form=rs.data
      })
    }
  }
}
</script>

<style scoped>

</style>
