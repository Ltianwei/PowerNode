<template>
<div>
  <el-table
    v-loading="loading"
    :data="allRoleList"
    ref="roleTable"
    border
    stripe
    style="width: 100%"
    @selection-change="handlerSelectionChange"
  >
    <el-table-column
      type="selection"
      align="center"
    />
    <el-table-column
      prop="roleId"
      label="角色编号"
      align="center"
    />
    <el-table-column
      prop="roleName"
      label="角色名"
      align="center"
    />
    <el-table-column
      prop="roleSort"
      align="center"
      label="排序"
    />
    <el-table-column
      prop="createTime"
      align="center"
      label="创建时间"
    />
  </el-table>
  <div style="text-align: center;margin-top: 10px">
    <el-button type="primary" icon="el-icon-plus" @click="saveUserRole">保存用户和角色关系</el-button>
  </div>


</div>
</template>

<script>
import sysRoleListApi from "@/api/system/sysRole/sysRoleList";
import {Message} from "element-ui";
export default {
  name: "sys-user-select-role",
  props:{
    //接受传来的值
    userSelectRole:{
      type:Number,
      default:undefined
    }
  },
  created() {
    //进入页面时将已选择的角色刷新 因为传入的是id值 所以需要判断
    this.loading=true
    this.selectAllRole();
    sysRoleListApi.getRoleIdsByUserId(this.userSelectRole).then(rs=>{
      this.temp=rs.data
      //遍历选择
      this.temp.forEach(u=>{
        this.allRoleList.forEach(f=>{
          if (u==f.roleId){
            //选中当前行
            this.$refs["roleTable"].toggleRowSelection(f,true);
          }
        })
      })
      this.loading=false
    })
  },
  data(){
    return{
      allRoleList:[],
      selectRoles:[],
      loading:false,
      temp:[]
    }
  },methods:{
    handlerSelectionChange(selection){
      //当选则框发生变化时接受值 永远都是接受最新的值 存储的是对象
      this.selectRoles=selection
    },saveUserRole(){
      let temp=[]
      this.selectRoles.forEach(rs=>{
        temp.push(rs.roleId)
      })
      sysRoleListApi.saveUserRoles(this.userSelectRole,temp).then(rs=>{
        Message.success(rs.msg)
        this.$emit("close")
      })
    },selectAllRole(){
      //查询所有的角色
      sysRoleListApi.getAllRoles().then(rs=>{
        this.allRoleList=rs.data
      })
    }
  }
}
</script>

<style scoped>

</style>
