<template>
  <div>

    <el-card style="margin-top: 5px">
      <el-table :data="dataPage.list" width="100%" border stripe>
        <el-table-column prop="title" label="标题" align="center">
        </el-table-column>
        <el-table-column prop="senderId" label="发送人" align="center">
          <template scope="{row}">
            <span v-for="r in nameOptions" v-if="r.userId===row.senderId"> {{r.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发送时间" align="center">
        </el-table-column>
        <el-table-column prop="readStatus" label="已读" align="center">
          <template scope="{row}">
            <el-tag :type="row.readStatus==0?'danger':'success'">{{ row.readStatus == 0 ? "未读" : "已读" }}</el-tag>
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
                  <el-button type="text" icon="el-icon-edit" @click="toInfo(row)">查看信息</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit" @click="toDelete(row)">删除信息</el-button>
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
      >
      </el-pagination>
    </el-card>

    <el-dialog center :visible.sync="isOpen" v-if="isOpen" :title="title">
      <message-info :active-row="activeRow" @close="closeDialog"/>
    </el-dialog>


  </div>
</template>

<script>

import inboxApi from "@/api/message/inbox";
import MessageInfo from "./message-info"
import {Message} from "element-ui";
import sysUserListApi from "@/api/system/sysUserList/sysUserList";

export default {
  name: "message-list",
  components: {
    MessageInfo
  },
  created() {
    /*分页查询数据*/
    this.doSearch()

  },
  data() {
    return {
      page: {
        pageSize: 10,
        pageNumber: 1,
        params: {}
      }, dataPage: {
        list: [],
        totalCount: undefined
      },
      title: undefined,
      isOpen: false,
      updateOpen: false,
      activeRow: undefined,
      nameOptions: []
    }
  }, methods: {
    doSearch() {
      inboxApi.getByPage(this.page).then((rs) => {
        this.dataPage.list = rs.data.list
        this.dataPage.totalCount = rs.data.totalCount
      })
      this.getAllUser()
    }, toDelete(row) {
      this.$confirm(`您确定要将当前信息放入回收站吗？`, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "danger"
      }).then(flag => {
        if (flag) {
          inboxApi.delete(row.id).then((rs) => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    },
    handlerCurrentChange(num) {
      this.page.pageNumber = num
      this.doSearch(this.page)

    }, handlerSizeChange(size) {
      this.page.pageSize = size
      this.page.pageNumber = 1
      this.doSearch(this.page)

    }, closeDialog() {
      this.title = undefined
      this.isOpen = false
      this.updateOpen = false
      this.doSearch();
    }, toInfo(row) {
      inboxApi.getInfo(row.id)
      this.doSearch()
      this.title = "详细信息"
      this.activeRow = row
      this.isOpen = true
    }, getAllUser() {
      //根据id查询发布用户名字
     /* console.log(this.dataPage.list[0])
      let userName=null
      this.dataPage.list.filter(li => {
          sysUserListApi.get(li.senderId).then(rs => {
            console.log(rs)
            userName = rs.data.userName
            this.nameOptions.push({
              senderId:li.senderId,
              name:userName
            })
          })
        }
      )*/

      sysUserListApi.getAllUser().then(rs=>{
        this.nameOptions=rs.data
      })

    }
  }
}
</script>

<style scoped>

</style>
