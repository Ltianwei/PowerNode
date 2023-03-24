<template>
  <div>
    <!-- 搜索表单开始 -->
    <el-card class="search-form">
      <el-form :inline="true" size="small" :model="page.params">
        <el-form-item>
          <el-input v-model="page.params.noticeTitle" placeholder="通知公告标题" clearable/>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="page.params.noticeType" placeholder="请选择类型" clearable>
            <el-option v-for="dict in typeOptions" :key="dict.dictValue" :value="dict.dictValue"
                       :label="dict.dictLabel"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="page.params.status" placeholder="请选择状态" clearable>
            <el-option v-for="dict in statusOptions" :key="dict.dictValue" :value="dict.dictValue"
                       :label="dict.dictLabel"/>
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
<!--      v-permission="['system:notice:query']"    -->
          <el-button type="primary" icon="el-icon-search" size="small"
                     @click="doSearch">查询
          </el-button>
          <el-button type="warning" icon="el-icon-refresh" size="small" @click="page.params = {}">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 搜索表单结束 -->
    <el-card style="margin-top: 3px">
      <!-- 菜单组开始 -->
      <!--   v-permission="['system:notice:add']"    -->
      <el-button size="small" type="primary" icon="el-icon-plus" @click="toAdd">
        添加
      </el-button>
      <!-- 菜单组结束 -->
      <!-- 数据表格开始 -->
      <div style="margin-top: 3px">
        <el-table
          v-loading="loading"
          :data="dataPage.list"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column
            prop="noticeId"
            label="编号"
            align="center"
          />
          <el-table-column
            prop="noticeTitle"
            label="通知公告标题"
            width="300px"
            :show-tooltip-when-overflow="true"
            align="center"
          />
          <el-table-column
            prop="createBy"
            align="center"
            label="创建者"
          />
          <el-table-column
            prop="createTime"
            align="center"
            :show-tooltip-when-overflow="true"
            label="创建时间"
          />
          <el-table-column prop="status" align="center" label="类型">
            <template slot-scope="{row}">
<!--              <dict-tag :value="row.noticeType" :data-list="typeOptions"></dict-tag>-->
              <el-tag v-for="dictData in typeOptions" v-if="dictData.dictValue==row.noticeType"
                      :type="dictData.dictSkin">{{ dictData.dictLabel }}

              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" align="center" label="状态">
            <template slot-scope="{row}">
<!--              <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
              <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==row.status" :type="dictData.dictSkin">
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
                  <!--  v-permission="['system:notice:info']"   -->
                  <el-dropdown-item>
                    <el-button type="text" icon="el-icon-info" @click="toInfo(row)">查看</el-button>
                  </el-dropdown-item>
                  <!--  v-permission="['system:notice:update']"   -->
                  <el-dropdown-item>
                    <el-button type="text" icon="el-icon-edit" @click="toUpdate(row)">修改</el-button>
                  </el-dropdown-item>
                  <!--  v-permission="['system:notice:add']"   -->
                  <el-dropdown-item>
                    <el-button type="text" @click="toDelete(row)" icon="el-icon-delete">删除</el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
    <el-dialog center title="添加通知信息" :visible.sync="isOpenAdd" v-if="isOpenAdd">
      <sys-notice-add @close="closeDialog"/>
    </el-dialog>

    <el-dialog center title="修改信息" :visible.sync="isOpenUpdate" v-if="isOpenUpdate">
      <sys-notice-update @close="closeDialog" :active-id="activeId"/>
    </el-dialog>

    <el-dialog center title="通知详情" :visible.sync="isOpenInfo" v-if="isOpenInfo">
      <sys-notice-info @close="closeDialog" :active-row="activeRow"/>
    </el-dialog>

  </div>
</template>

<script>
import sysNoticeApi from "@/api/system/notice/sysNotice";
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import {Message} from "element-ui";
import SysNoticeAdd from "@/views/system/notice/sys-notice-add";
import SysNoticeUpdate from "@/views/system/notice/sys-notice-update";
import SysNoticeInfo from "@/views/system/notice/sys-notice-info";

export default {
  name: "sys-notice-list",
  components: {SysNoticeInfo, SysNoticeUpdate, SysNoticeAdd},
  created() {
    this.loadDictDataByDictType()
    this.doSearch()
  },
  data() {
    return {
      page: {
        pageSize: 10,
        pageNumber: 1,
        params: {
          dateRange: [],
          noticeTitle: undefined,
          noticeType: undefined,
          status: undefined,
          startTime:undefined,
          endTime:undefined,
          /*dateRange:undefined*/

        }
      },
      statusOptions: [],
      typeOptions: [],
      dataPage: {
        totalCount: 10,
        list: []
      },loading:false,
      isOpenAdd:false,
      activeId:undefined,
      isOpenUpdate:false,
      isOpenInfo:false,
      activeRow:undefined
    }
  }, methods: {
    doSearch() {

      //对日期进行提取
      this.loading = true
      if (this.page.params.dateRange){
        this.page.params['startTime']=this.page.params.dateRange[0];
        this.page.params['endTime']=this.page.params.dateRange[1];
      }
      this.loading=false
      sysNoticeApi.getByPage(this.page).then(rs => {
        this.dataPage.totalCount = rs.data.totalCount
        this.dataPage.list = rs.data.list
      })
    },
    toAdd() {
      this.isOpenAdd=true

    }, toInfo(row) {
      this.isOpenInfo=true
      this.activeRow=row

    }, toUpdate(row) {
      this.isOpenUpdate=true
      this.activeId=row

    }, toDelete(row) {
      this.$confirm("您确定要删除吗").then(flag=>{
        if (flag){
          sysNoticeApi.delete(row.noticeId).then(rs=>{
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
    }, loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_notice_status").then((rs) => {
        this.statusOptions = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_notice_type").then((rs) => {
        this.typeOptions = rs.data
      })
    },closeDialog(){
      this.isOpenAdd=false
      this.isOpenUpdate=false
      this.isOpenInfo=false
      this.doSearch()
    }
  }
}
</script>

<style scoped>

</style>
