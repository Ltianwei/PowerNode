<template>
  <div>

    <el-card>
      <el-form inline>
        <el-form-item>
          <el-input size="small" clearable v-model="page.params.title" placeholder="请输入模块标题"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable placeholder="请选择业务类型" v-model="page.params.businessType">
            <el-option-group>
              <el-option v-for="type in typeStatus" :label="type.dictLabel" :value="type.dictValue"/>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            size="small"
            v-model="dateValue"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch" size="small">查询</el-button>
          <el-button icon="el-icon-refresh" type="warning" @click="doReset" size="small">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 5px">
      <el-button type="danger" size="small" icon="el-icon-delete" @click="batchDelete">删除</el-button>
      <el-button type="danger" size="small" icon="el-icon-delete" @click="cleanAll">清空</el-button>

      <el-table
        ref="multipleTable"
        :data="dataPage.list"
        tooltip-effect="dark"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column prop="operId" align="center" label="编号"></el-table-column>
        <el-table-column label="模块标题" align="center" prop="title"/>
        <el-table-column label="业务类型" align="center">
          <template slot-scope="{row}">
            <el-dialog v-for="data in typeStatus" v-if="row.businessType==data.dictValue" :type="data.dictSkin">
              {{ data.dictLabel }}
<!--              {{row.businessType}}-->
            </el-dialog>
          </template>
        </el-table-column>
        <el-table-column label="请求方式" align="center" prop="requestMethod"/>
        <el-table-column label="操作人员" align="center" prop="operName"/>
        <el-table-column label="主机地址" align="center" prop="operIp"/>
        <el-table-column label="操作地点" align="center" prop="operLocation"/>
        <el-table-column label="操作状态" align="center">
          <template scope="{row}">
            <el-tag v-for="data in status" v-if="data.dictValue==row.status" :type="data.dictSkin">
              {{ data.dictLabel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" prop="operTime" align="center"/>
        <el-table-column label="操作" align="center">
          <template scope="{row}">
            <el-dropdown class="handle-button">
              <span class="el-dropdown-link">
                <el-link icon="el-icon-s-operation" type="primary">操作<i class="el-icon-arrow-down el-icon--right"/>
                </el-link>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-info"
                    @click="toInfo(row)"
                  >查看
                  </el-button>
                  <!--      v-permission="['system:operLog:info']"            -->
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="toDelete(row)"
                  >删除
                    <!--    v-permission="['system:operLog:remove']"         -->
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="this.page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="this.dataPage.totalCount">
      </el-pagination>
    </el-card>

    <el-dialog :visible.sync="isOpenDialog" :v-if="isOpenDialog" title="详细信息" center>
      <sys-oper-log-info :active-row="activeRow" />
    </el-dialog>

  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysOperLogApi from "@/api/sysOperLog/sysOperLogApi";
import {Message} from "element-ui";
import SysOperLogInfo from "@/views/system/operLog/sys-oper-log-info";

export default {
  name: "sysOperLogList",
  components: {SysOperLogInfo},
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
          businessType: undefined,
          title: undefined,
          startTime: undefined,
          endTime: undefined
        }
      },
      dataPage: {
        list: [],
        totalCount: 10
      },
      typeStatus: [],
      status: [],
      dateValue: [],
      selections: [],
      isOpenDialog: false,
      activeRow: undefined
    }
  }, methods: {
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_oper_type").then(rs => {
        this.typeStatus = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_login_status").then(rs => {
        this.status = rs.data
      })
    }, doSearch() {
      //将日期格式转换
      if (this.dateValue != null) {
        this.page.params.startTime = this.dateValue[0]
        this.page.params.endTime = this.dateValue[1]
      }
      sysOperLogApi.getByPage(this.page).then(rs => {
        this.dataPage.list = rs.data.list
        this.dataPage.totalCount = rs.data.totalCount
      })
    }, doReset() {
      this.page = {
        pageSize: 10,
        pageNumber: 1,
        params: {
          businessType: undefined,
          title: undefined,
          startTime: undefined,
          endTime: undefined
        }
      }
    }, toDelete(row) {
      this.$confirm("您确定要删除选中的日志吗").then(flag => {
        if (flag) {
          sysOperLogApi.delete(row.operId).then(rs => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    },
    batchDelete() {
      if (this.selections.length < 1) {
        Message.error("请先选择日志")
      } else {
        this.$confirm("您确定要删除选中的日志吗").then(flag => {
          if (flag) {
            sysOperLogApi.batchDelete(this.selections).then(rs => {
              Message.success("删除成功")
              this.doSearch()
            })
          }
        })
      }
    }, cleanAll() {
      this.$confirm("您确定要清空操作日志吗").then(flag => {
        sysOperLogApi.cleanOperLog().then(rs => {
          Message.success(rs.msg)
          this.doSearch()
        })
      })
    }, handleSelectionChange(select) {
      //将多选的数据存储在selections中
      select.filter(s => {
        let temp = s.operId
        this.selections.push(temp)
      })
    }, handleSizeChange(size) {
      this.page.pageSize = size
      this.page.pageNumber = 1
      this.doSearch()
    }, handleCurrentChange(num) {
      this.page.pageNumber = num
      this.doSearch()
    }, toInfo(row) {
      this.activeRow = row
      this.isOpenDialog = true
    }, closeDialog() {
      this.isOpenDialog = false
    }
  }
}
</script>

<style scoped>

</style>
