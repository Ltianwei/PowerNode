<template>
  <div>
    <el-card style="margin-top: 5px">
      <el-form :model="queryParams" :inline="true">
        <el-form-item>
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryParams.status" clearable placeholder="请选中菜单状态">
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


    <el-card style="margin-top: 5px">
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="toAddDialog(null)">添加</el-button>
        <el-button type="warning" icon="el-icon-sort" @click="doExpandAll">折叠/展开</el-button>
      </div>
      <el-table
        :data="menuList"
        row-key="menuId"
        border
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}" style="margin-top: 5px"
        v-if="refreshTable"
      >
        <el-table-column
          prop="menuName"
          label="菜单名称"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="图标"
          align="center"
        >
          <template slot-scope="{row}">
            <svgicon :icon-class="row.icon"></svgicon>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderNum"
          label="排序"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="perms"
          label="权限标识"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          prop="component"
          label="组件路径"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          prop="path"
          label="路由地址"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
        >
          <template slot-scope="{row}">
<!--            <dict-tag :value="row.status" :data-list="statusOptions">{{}}</dict-tag>-->
            <el-tag  v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="menuType"
          label="类型"
          align="center"
        >
          <template slot-scope="{row}">
            <el-tag :type=" row.menuType==0?'success':row.menuType==1?'info':'warning'   ">
              {{ row.menuType == 0 ? '目录' : row.menuType == 1 ? '菜单' : '权限' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-dropdown>
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
              <el-dropdown-menu slot="dropdown">

                <el-dropdown-item v-if="row.menuType !=2">
                  <el-button type="text" icon="el-icon-plus" @click="toAddDialog(row)">新增</el-button>
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

    <el-dialog :title="title" center :visible.sync="isOpenAdd" v-if="isOpenAdd" >
      <sys-menu-add @close="closeDialog" :active-id="activeId"></sys-menu-add>
    </el-dialog>
    <el-dialog :title="title" center :visible.sync="isOpenUpdate" v-if="isOpenUpdate">
      <sys-menu-update @close="closeDialog" :update-obj="UpdateObj"></sys-menu-update>
    </el-dialog>
  </div>
</template>

<script>
import sysMenuListApi from "@/api/system/sysMenu/sysMenuList";
import {handleTree} from "@/utils/powernode";
import Svgicon from "@/components/SvgIcon/index"
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import {Message} from "element-ui";
import SysMenuAdd from "@/views/system/sysMenu/sys-menu-add";
import SysMenuUpdate from "@/views/system/sysMenu/sys-menu-update";



export default {
  name: "sys-menu-list",
  components: {
    SysMenuUpdate,
    SysMenuAdd,
    Svgicon,
  }, created() {
    this.loadDictDataByDictType()
    this.doSearch()
  }, data() {
    return {
      statusOptions:[],
      queryParams: {
        menuName: undefined,
        status:0
      },menuList:[],
      isExpandAll:false,
      refreshTable:true,
      title:undefined,
      isOpenUpdate:false,
      isOpenAdd:false,
      activeId:undefined,
      UpdateObj:undefined

    }
  }, methods: {
    loadDictDataByDictType() {
      //获取获取所有的状态值 所有状态的类型是sys_status_type
      //查询到所有类型可选值 放入数组statusOptions
      //调用
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then((rs) => {
        this.statusOptions = rs.data
      })
    },
    doSearch(){
      sysMenuListApi.listDept(this.queryParams).then((rs)=>{
        this.menuList=handleTree(rs.data,"menuId")
      })
    },doReset(){
      this.queryParams={
        status:0
      }
    },toAddDialog(row){
      this.isOpenAdd=true
      this.title="添加菜单"
      this.activeId=row

    },doExpandAll(){
      this.isExpandAll=!this.isExpandAll
      this.refreshTable=false;
      this.$nextTick(()=>{
        this.refreshTable=true
      })

    },doUpdateDialog(row){
      this.isOpenUpdate=true
      this.title="更新菜单"
      this.UpdateObj=row
    },doDeleteSysDept(row){
      this.$confirm(`您确定要删除${row.menuName}吗?`,{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"error"
      }).then((flag)=>{
        if (flag){
          sysMenuListApi.delete(row.menuId).then((rs)=>{
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })

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
