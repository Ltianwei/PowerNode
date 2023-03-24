<template>
<div>


  <el-form ref="form" :model="sysUser" :rules="rules" label-width="80px" size="small">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item label="所在部门">
          <treeselect
            v-model="sysUser.deptId"
            :options="deptOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="选择所在部门"
          />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="账号" prop="userName">
          <el-input v-model="sysUser.userName" placeholder="请输入账号" clearable />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="sysUser.nickName" placeholder="请输入昵称" clearable />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="sysUser.email" placeholder="请输入邮箱" clearable />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="sysUser.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="性别" >
          <el-radio-group v-model="sysUser.sex">
            <el-radio v-for="status in sexOptions" :key="status.dictValue" :label="status.dictValue">{{ status.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="状态" >
          <el-radio-group v-model="sysUser.status">
            <el-radio v-for="status in statusOptions" :key="status.dictValue" :label="status.dictValue">{{ status.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item>
      <el-button type="primary" @click="doAddSysUser">更新</el-button>
      <el-button type="warning" @click="doReset">重置</el-button>
    </el-form-item>
  </el-form>

</div>
</template>

<script>

import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import {handleTree} from "@/utils/powernode";
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysUserListApi from "@/api/system/sysUserList/sysUserList";
import {Message} from "element-ui";

export default {
  name: "sys-user-update",
  props:{
    activeId:{
      type:Number,
      default:undefined
    }
  },watch:{
    activeId:{
      immediate:true,
      handler:function (val, oldVal) {
        sysUserListApi.get(val).then(rs=>{
          this.sysUser=rs.data
        })
      }
    }
  },
  components:{
    Treeselect
  },created() {
    this.doSearchDept()
    this.loadDictDataByDictType()
  },data(){
    return{
      sysUser:{
        deptId:undefined,
        userName:undefined,
        nickName:undefined,
        email:undefined,
        phone:undefined,
        sex:1,
        status:0
      },
      rules: {
        userName:[{required:true,message:"用户名不能为空",trigger:"blur"}],
        nickName:[{required:true,message:"昵称不能为空",trigger:"blur"}],
        email:[{required:true,message:"邮箱不能为空",trigger:"blur"}],
        phone:[{required:true,message:"手机号不能为空",trigger:"blur"}]
      },
      deptOptions:[],
      sexOptions:[],
      statusOptions:[]
    }
  },methods:{
    doSearchDept(){
      sysDeptListApi.listDept().then(rs=>{
        this.deptOptions=handleTree(rs.data,"deptId")
      })
    },normalizer(node){
      return{
        id:node.deptId,
        label:node.deptName,
        children:node.children
      }
    },loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_user_sex").then(rs=>{
        this.sexOptions=rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },doAddSysUser(){
      this.$refs['form'].validate(flag=>{
        if (flag){
          sysUserListApi.update(this.sysUser).then(rs=>{
            Message.success(rs.msg)
            this.$emit("close")
          })
        }
      })

    },doReset(){
      this.sysUser={
        deptId:undefined,
        userName:undefined,
        nickName:undefined,
        email:undefined,
        phone:undefined,
        sex:1,
        status:0
      }
    }
  }
}
</script>

<style scoped>

</style>
