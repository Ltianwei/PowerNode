<template>
  <div>
    <!-- 搜索表单开始 -->
    <!-- v-permission="['system:loginInfo:query']"在相应的按钮处添加 可以控制权限 -->
    <el-card class="search-form">
      <el-form :model="page.params" ref="queryForm" size="small" :inline="true">
        <el-form-item prop="userName">
          <el-input
            v-model="page.params.userName"
            placeholder="请输入用户名称"
            clearable
            style="width: 240px;"
          />
<!--  @keyup.enter.native="search"        -->
        </el-form-item>
        <el-form-item prop="status">
          <el-select
            v-model="page.params.status"
            placeholder="登录状态"
            clearable
            style="width: 240px"
          >
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="page.params.dateRange"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini"
                     @click="doSearch">搜索
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="page.params={}">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 搜索表单结束 -->
    <el-card style="margin-top: 3px">
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="small"
        @click="toBatchDelete"
      >删除
        <!--      v-permission="['system:loginInfo:batchRemove']"-->
      </el-button>
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="small"
        @click="handleClean"
      >清空
      </el-button>
<!--      v-permission="['system:loginInfo:clean']"-->
      <!-- 菜单组结束 -->
      <!-- 数据表格开始 -->
      <!-- v-loading="loading" 加载动画
           @selection-change="handleSelectionChange" 当选择框发生变化时 只记录最新一次的记录
       -->
      <div style="margin-top: 3px">
        <el-table ref="tables" v-loading="loading" border
                  stripe :data="dataPage.list" @selection-change="handleSelectionChange">

          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="访问编号" align="center" prop="infoId"/>
          <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
          <el-table-column label="登录地址" align="center" prop="ipAddr" width="130" :show-overflow-tooltip="true"/>
          <el-table-column label="登录地点" align="center" prop="loginLocation" :show-overflow-tooltip="true"/>
          <el-table-column label="浏览器" align="center" prop="browser" :show-overflow-tooltip="true"/>
          <el-table-column label="操作系统" align="center" prop="os" :show-overflow-tooltip="true"/>
          <el-table-column label="登录状态" align="center" prop="status">
            <template slot-scope="{row}">
              <!--            <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
              <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue == row.status"
                      :type="dictData.dictSkin">{{ dictData.dictLabel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作信息" align="center" prop="msg" :show-overflow-tooltip="true"/>
          <el-table-column label="登录日期" align="center" prop="loginTime" sortable="custom" width="180"/>
          <el-table-column align="center" label="操作" fixed="right">
            <template slot-scope="{row}">
              <el-button type="text" @click="toDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          class="pagination"
          :current-page="page.pageNumber"
          :page-sizes="[10,20,30,50]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="dataPage.totalCount"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <!-- 数据表格结束 -->
    </el-card>

  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysLoginInfoApi from "@/api/loginInfo/sysLoginInfoApi";
import {Message} from "element-ui";

export default {
  name: "sys-login-info-list",
  created() {
    this.doSearch()
    this.loadDictDataByDictType()
  },
  data() {
    return {
      page: {
        pageNumber: 1,
        pageSize: 10,
        params: {
          dateRange:[]
        }
      }, dataPage: {
        totalCount: 10,
        list: []
      }, statusOptions: [],
      ids: [],
      loading:false
    }
  }, methods: {
    //引入根据类型查询id
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs => {
        this.statusOptions = rs.data
      })
    }, toDelete(row) {
      //删除
      this.$confirm("您确定要删除吗").then(flag => {
        if (flag) {
          sysLoginInfoApi.delete(row.infoId).then(rs => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    }, doSearch() {
      //对日期进行提取
      this.loading = true
      if (this.page.params.dateRange){
        this.page.params['startTime']=this.page.params.dateRange[0];
        this.page.params['endTime']=this.page.params.dateRange[1];
      }

      sysLoginInfoApi.getByPage(this.page).then(rs => {
        this.dataPage.totalCount = rs.data.totalCount
        this.dataPage.list = rs.data.list
      })
      this.loading = false
    }, handleSizeChange(size) {
      this.page.pageSize = size
      this.page.pageNumber = 1
      this.doSearch()

    }, handleCurrentChange(num) {
      this.page.pageNumber = num
      this.doSearch()
    }, handleClean() {
      //清空
      this.$confirm("您确定要清空吗").then(flag => {
        if (flag) {
          sysLoginInfoApi.cleanLoginInfo().then(rs => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    },toBatchDelete(){
      //批量删除
      this.$confirm("您确定要清空吗").then(flag => {
        if (flag) {
          sysLoginInfoApi.batchDelete(this.ids).then(rs => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    },handleSelectionChange(selected){
      let params=selected
      params.filter(rs=>{
        this.ids.push(rs.infoId)
      })
    }
  }
}
</script>

<style scoped>

</style>
