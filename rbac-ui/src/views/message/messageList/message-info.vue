<template>
  <div>

    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-warning-outline"></i>
          发送人
        </template>
        {{ this.userName }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          发布时间
        </template>
        {{ activeRow.createTime }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          发送标题
        </template>
        {{ activeRow.title }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          发送内容
        </template>
        {{ activeRow.content }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-tools"></i>
          系统消息
        </template>
        {{ activeRow.issystem == 0 ? "否" : "是" }}
      </el-descriptions-item>
    </el-descriptions>


  </div>
</template>

<script>
import sysUserListApi from "@/api/system/sysUserList/sysUserList";

export default {
  name: "message-info",
  props: {
    activeRow: {
      type: Object,
      default: undefined
    }
  }, created() {
    this.getAllUser()
    console.log(this.activeRow)
  },
  data() {
    return {
      userName: undefined
    }
  }, methods: {
    getAllUser() {
     //根据id查询发布用户名字
      sysUserListApi.get(this.activeRow.senderId).then(rs=>{
        this.userName =rs.data.userName
      })
    }
  }

}
</script>

<style scoped>

</style>
