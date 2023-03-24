<template>
  <div>

    <el-row :gutter="5">

      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <el-card>
          <!--  接受子类调用方法的同时 接受子类传的参数  -->
          <sys-user-left @reloadData="doSearch"/>
        </el-card>
      </el-col>

      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <!-- 搜索栏开始 -->
        <el-card class="search-form">
          <el-form :inline="true" :model="page.params" size="small">
            <el-form-item>
              <el-input v-model="page.params.userName" clearable placeholder="请输入用户名"/>
            </el-form-item>
            <el-form-item>
              <el-input v-model="page.params.nickName" clearable placeholder="请输入昵称"/>
            </el-form-item>
            <el-form-item>
              <el-select v-model="page.params.status" clearable placeholder="请选择状态">
                <el-option v-for="dict in statusOptions" :key="dict.dictValue" :value="dict.dictValue"
                           :label="dict.dictLabel"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="doSearch(null)">查询</el-button>
              <el-button type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>

            </el-form-item>
          </el-form>
        </el-card>

        <!-- 搜索栏结束 -->
        <el-card style="margin-top: 3px">
          <!-- 操作按钮组 -->
          <el-button type="primary" size="small" icon="el-icon-plus" @click="toAddDialog">添加</el-button>
          <!-- 操作按钮组结束 -->
          <!-- 数据表格开始 -->
          <el-table border :data="dataPage.list" stripe style="width: 100%;margin-top: 3px">
            <!--            <el-table-column align="center" prop="userId" label="编号"/>-->
            <el-table-column align="center" prop="userName" label="账号"/>
            <el-table-column align="center" prop="nickName" label="昵称"/>
            <el-table-column align="center" prop="email" label="邮箱" width="180px"/>
            <el-table-column align="center" prop="phone" label="电话" width="150px"/>
            <el-table-column align="center" prop="sex" label="性别">
              <template slot-scope="{row}">
                <!--                <dict-tag :value="row.sex" :data-list="sexOptions"></dict-tag>-->
                <el-tag v-for="dictData in sexOptions" v-if="dictData.dictValue==row.sex" :type="dictData.dictSkin">
                  {{ dictData.dictLabel }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="header" label="所在部门" width="150px">
              <template slot-scope="{row}">
                <div v-for="dept in deptList" v-if="row.deptId==dept.deptId">{{ dept.deptName }}</div>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="status" label="状态">
              <template slot-scope="{row}">
                <!--                <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
                <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status">
                  {{ dictData.dictLabel }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" fixed="right">
              <template slot-scope="{row}">
                <el-dropdown class="handle-button">
              <span class="el-dropdown-link">
                <el-link icon="el-icon-s-operation" type="primary">操作<i class="el-icon-arrow-down el-icon--right"/>
                </el-link>
              </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-document" @click="userInfo(row)">详情</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-edit" @click="updateUser(row)">修改</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item v-if="row.userId!=1">
                      <el-button type="text" icon="el-icon-delete" @click.native="deleteById(row)">删除</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" icon="el-icon-edit-outline" @click="selectRole(row)">分配角色</el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页组件开始 -->
          <div style="margin-top: 3px">
            <el-pagination
              :current-page="dataPage.pageNumber"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="dataPage.pageSize"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="dataPage.totalCount"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
          <!-- 分页组件结束 -->
        </el-card>
      </el-col>
    </el-row>

    <!--
       添加对话框
     -->
    <el-dialog :title="title" center :visible.sync="isOpenAdd" v-if="isOpenAdd">
      <sys-user-add @close="closeAdd"></sys-user-add>
    </el-dialog>

    <el-dialog :title="title" center :visible.sync="isOpenUpdate" v-if="isOpenUpdate">
      <sys-user-update @close="closeAdd" :active-id="activeId"/>
    </el-dialog>

    <el-dialog :title="title" center :visible.sync="isOpenUserInfo" v-if="isOpenUserInfo">
      <sys-user-info @close="closeAdd" :user-info-id="userInfoId"/>
    </el-dialog>

    <el-dialog :title="title" center :visible.sync="isSelectRole" v-if="isSelectRole">
      <sys-user-select-role @close="closeAdd" :user-select-role="userSelectRole"/>
    </el-dialog>

  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysUserListApi from "@/api/system/sysUserList/sysUserList";
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
import SysUserLeft from "@/views/system/sysUser/sys-user-left"
import SysUserAdd from "@/views/system/sysUser/sys-user-add"
import {Message} from "element-ui";
import SysUserUpdate from "@/views/system/sysUser/sys-user-update"
import SysUserInfo from "@/views/system/sysUser/sys-user-info";
import SysUserSelectRole from "@/views/system/sysUser/sys-user-select-role"


export default {
  name: "sys-user-list",
  components: {
    SysUserInfo,
    SysUserLeft,
    SysUserAdd,
    SysUserUpdate,
    SysUserSelectRole
  },
  created() {
    this.doSearchDept()
    this.loadDictDataByDictType()
    this.doSearch()
  },
  data() {
    return {
      page: {
        pageSize: 10,
        pageNumber: 1,
        deptId:undefined,
        params: {
          userName: undefined,
          nickName: undefined,
          status: undefined
        }
      },
      statusOptions: [],
      sexOptions: [],
      dataPage: {
        list: [],
        sex: 1,
        totalCount: 10,
        pageNumber: 1,
        pageSize: 10
      },
      deptList: [],
      title: undefined,
      isOpenAdd: false,
      isOpenUpdate:false,
      activeId:undefined,
      isOpenUserInfo:false,
      isSelectRole:false,
      userInfoId:undefined,
      userSelectRole:undefined
    }
  }, methods: {
    doSearch(deptId) {
      //判断是否左侧部门筛选组件传值
      if (deptId !=null && deptId != undefined ){
        this.page.params.deptId=deptId
      }
      sysUserListApi.getByPage(this.page).then(rs => {
        this.dataPage.list = rs.data.list
        this.dataPage.totalCount = rs.data.totalCount
      })
    }, doReset() {
      this.page = {
        pageSize: 10,
        pageNumber: 1,
        params: {
          userName: undefined,
          nickName: undefined,
          status: undefined
        }
      }
    }, loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then((rs) => {
        this.statusOptions = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_user_sex").then((rs) => {
        this.sexOptions = rs.data
      })
    }, toAddDialog() {
      this.isOpenAdd=true
      this.title="添加用户"

    }, deleteById(row) {
      this.$confirm(`您确定要删除【${row.userName}】吗`).then(flag => {
        if (flag) {
          sysUserListApi.delete(row.userId).then(rs => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    }, handleSizeChange(size) {
      this.page.pageSize = size
      this.page.pageNumber = 1
      this.doSearch()
    }, handleCurrentChange(num) {
      this.page.pageNumber = num
      this.doSearch()
    }, doSearchDept() {
      sysDeptListApi.listDept().then(rs => {
        this.deptList = rs.data
      })
    }, closeAdd() {
      this.title = undefined
      this.isOpenAdd = false
      this.isOpenUpdate=false
      this.isOpenUserInfo=false
      this.isSelectRole=false
      this.doSearch();
    },updateUser(row){
      //更新
      this.isOpenUpdate=true
      this.title="用户更新"
      this.activeId=row.userId
    },userInfo(row){
      //查看用户信息
      this.title="用户信息查看"
      this.isOpenUserInfo=true
      this.userInfoId=row
    },selectRole(row){
      //分配用户角色
      this.isSelectRole=true
      this.title="用户【"+row.userName+"】分配角色"
      this.userSelectRole=row.userId
    }
  }
}
</script>

<style scoped>

</style>
