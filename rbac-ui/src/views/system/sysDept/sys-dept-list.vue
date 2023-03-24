<template>
<div>
  <!--
      treeSelect 树形组件

      el-col 表示分栅格 一行分为24格 用el-row包裹
   -->
  <el-card style="margin-top: 5px">
    <el-form :model="queryParams" :inline="true">
      <el-form-item label="部门名称">
        <el-input v-model="queryParams.deptName" placeholder="请输入部门名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" clearable>
          <el-option v-for="dictData in statusOptions" :label="dictData.dictLabel"
                     :value="dictData.dictValue"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="doSearch"> 搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="doReset"> 重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>


  <!--
     row-key  每条记录唯一标识 deptId
     default-expand-all: 表格是否展开
     tree-props: 描述表数据格式
         children:描述子记录
         v-if:实时更新表格
  -->
  <el-card style="margin-top: 5px">
    <div>
      <el-button type="primary" icon="el-icon-plus" @click="doAddDialog">添加</el-button>
      <el-button type="warning" icon="el-icon-sort" @click="doExpandAll">折叠/展开</el-button>
    </div>
<!--    <el-table
    :data="deptList"
    row-key="deptId"
    border
    :default-expand-all="isExpandAll"
    :tree-props="{children: 'childern',hasChildren: 'hasChildren'}"
    v-if="refreshTable"
    ></el-table>-->
    <el-table
      :data="deptList"
      row-key="deptId"
      border
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      style="margin-top: 5px"
      v-if="refreshTable"
    >
      <el-table-column
        prop="deptName"
        label="部门名称"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="orderNum"
        label="排序"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="leader"
        label="负责人"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="phone"
        label="电话"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="email"
        label="邮箱"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        align="center"
      >
        <template slot-scope="{row}">
<!--          <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
          <el-tag  v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="{row}">
          <el-dropdown>
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" icon="el-icon-plus" @click="doAddDialog(row)">新增</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" icon="el-icon-edit" @click="doUpdateDialog(row)">修改</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" icon="el-icon-delete" @click="doDeleteSysDept(row)">删除</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>

    </el-table>
  </el-card>

  <el-dialog align="center" :title="title"  :visible.sync="isOpenAdd" v-if="isOpenAdd" >
    <sys-dept-add @close="closeDialog" :active-id="activeId"></sys-dept-add>
  </el-dialog>
  <el-dialog align="center" :title="title" :visible.sync="isOpenUpdate" >
    <sys-dept-update :update-id="updateId" @close="closeDialog"></sys-dept-update>
  </el-dialog>

</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
//导入第三方插件
import {handleTree} from "@/utils/powernode";
import SysDeptAdd from "@/views/system/sysDept/sys-dept-add";
import {Message} from "element-ui";
import SysDeptUpdate from "@/views/system/sysDept/sys-dept-update";

export default {
  name: "sys-dept-list",
  components: {
    SysDeptUpdate,
    SysDeptAdd
  },
  created() {
    this.loadDictDataByDictType()
    this.doSearch()
  },data(){
    return{
      statusOptions:[],
      //是否折叠的属性
      isExpandAll:false,
      //实时刷新表格
      refreshTable:true,
      deptList:[],
      queryParams:{
        deptName:undefined,
        status:undefined
      },title:undefined,
      isOpenAdd:false,
      isOpenUpdate:false,
      activeId:undefined,
      updateId:undefined

    }
  },methods:{
    loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },doAddDialog(row){
      //将id值传入
      this.activeId=row.deptId
      this.isOpenAdd=true
      this.title="添加部门数据"

    },doExpandAll(){
      //是否折叠
      this.refreshTable=false
      this.isExpandAll=!this.isExpandAll
      //重新渲染表格
     this.$nextTick(()=>{
       this.refreshTable=true
     })

    },doUpdateDialog(row){
      this.isOpenUpdate=true
      this.updateId=row.deptId
      this.title="更新部门数据"

    },doDeleteSysDept(row){
      this.$confirm(`您确定要删除${row.deptName}吗`,{
        confirmButtonText:"确认",
        cancelButtonText:"取消",
        type:"error"
      }).then((flag)=>{
       if (flag){
         sysDeptListApi.delete(row.deptId).then(rs=>{
           Message.success(rs.msg)
           this.doSearch()
         })

       }
      })

    },doSearch(){
      sysDeptListApi.listDept(this.queryParams).then((rs)=>{
        //使用第三方软件进行下拉渲染 将值 主键放入
        this.deptList=handleTree(rs.data,"deptId")
      })

    },doReset(){
      this.queryParams={}
    },closeDialog(){
      this.title=undefined
      this.isOpenAdd=false
      this.isOpenUpdate=false
      this.doSearch()

    }
  }
}
</script>

<style scoped>

</style>
