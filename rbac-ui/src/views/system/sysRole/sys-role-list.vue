<template>
  <div>

    <el-card style="margin-top: 5px">
      <el-form :model="page" :inline="true">
        <el-form-item label="角色名称">
          <el-input v-model="page.params.roleName" placeholder="请输入角色名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="page.params.status" clearable>
            <el-option v-for="dictData in statusOptions" :label="dictData.dictLabel"
                       :value="dictData.dictValue"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch"> 搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="doReset"> 重置</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="doAddDialog"> 添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>


    <el-card style="margin-top: 5px">
      <el-table :data="dataPage.list" width="100%" border stripe>
        <el-table-column prop="roleId" label="角色编码" align="center">
        </el-table-column>
        <el-table-column prop="roleName" label="角色名称" align="center">
        </el-table-column>
        <el-table-column prop="roleSort" label="角色顺序" align="center">
        </el-table-column>
        <el-table-column prop="remark" label="备注" align="center">
        </el-table-column>

        <el-table-column prop="status" label="角色状态" align="center">
          <template slot-scope="{row}">
<!--            <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
            <el-tag  v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-dropdown>
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit" @click="toUpdateDialog(row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="toDeleteSysRole(row)">删除</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit-outline" @click="toRoleSelectMenus(row)">角色菜单分配</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        :page-size="page.pageSize"
        :page-sizes="[10,20,30,40]"
        :total="dataPage.totalCount"
        layout="total,sizes,prev,pager,next,jumper"
        @current-change="handlerCurrentChange"
        @size-change="handlerSizeChange"
      />
    </el-card>

    <el-dialog center :visible.sync="isOpenAdd" v-if="isOpenAdd" :title="title">
      <sys-role-add @close="closeDialog" ></sys-role-add>
    </el-dialog>
    <el-dialog center :visible.sync="isOpenUpdate" v-if="isOpenUpdate" :title="title">
      <sys-role-update @close="closeDialog" :update-id="updateId"/>
    </el-dialog>
    <el-dialog center :visible.sync="isOpenSelect" v-if="isOpenSelect" :title="title">
      <sys-role-select @close="closeDialog" :select-id="selectId"/>
    </el-dialog>


  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysRoleListApi from "@/api/system/sysRole/sysRoleList";
import SysRoleAdd from "@/views/system/sysRole/sys-role-add";
import SysRoleUpdate from "@/views/system/sysRole/sys-role-update";
import {Message} from "element-ui";
import SysRoleSelect from "@/views/system/sysRole/sys-role-select";

export default {
  name: "sys-role-list",
  components: {SysRoleSelect, SysRoleUpdate, SysRoleAdd},
  created() {
    this.loadDictDataByDictType()
    this.doSearch()
  },
  data(){
    return{
          page:{
            pageSize:10,
            pageNumber:1,
            params:{
              roleName:undefined,
              status:undefined,
            }
          },
          statusOptions:[],
          dataPage:{
            pageSize:10,
            pageNumber:1,
            list:[]
      },isOpenAdd:false,
      isOpenUpdate:false,
      updateId:undefined,
      title:undefined,
      isOpenSelect:undefined,
      selectId:undefined
    }
  },methods:{
    loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then((rs)=>{
        this.statusOptions=rs.data
      })
    },doSearch(){
      sysRoleListApi.getByPage(this.page).then(rs=>{
       this.dataPage.list=rs.data.list
        this.dataPage.totalCount=rs.data.totalCount
      })
    },doReset(){
     this.page={
        pageSize:10,
          totalCount:10,
          pageNumber:1,
          params:{
          roleName:undefined,
            status:undefined
        }
      }
    },doAddDialog(){
      this.title="添加角色"
      this.isOpenAdd=true
    },toUpdateDialog(row){
      this.title="更新角色"
      this.isOpenUpdate=true
      this.updateId=row.roleId
    },toDeleteSysRole(row){
      this.$confirm("您确定要删除【"+row.roleName+"】吗?").then(flag=>{
        if (flag){
          sysRoleListApi.delete(row.roleId).then(rs=>{
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })

    },toRoleSelectMenus(row){
      //给用户分配权限
      this.title="用户菜单分配"
      this.isOpenSelect=true
      this.selectId=row.roleId

    },handlerCurrentChange(num){
      this.page.pageNumber=num
      this.doSearch()
    },handlerSizeChange(size){
      this.page.pageSize=size
      this.page.pageNumber=1
      this.doSearch()

    },closeDialog(){
      this.title=undefined
      this.isOpenAdd=false
      this.isOpenUpdate=false
      this.isOpenSelect=false
      this.doSearch()
    }
  }
}
</script>

<style scoped>

</style>
