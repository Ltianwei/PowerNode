<template>
  <div>

<el-card  >
    <el-form :model="form" ref="form" :rules="rules" size="small" label-width="100px" style="margin-top: 30px">
      <el-form-item label="标题"  prop="title">
        <el-input v-model="form.title" placeholder="请输入标题" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="form.content" placeholder="请输入内容" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="接收人" prop="recipientId">
      <el-input v-model="form.recipientId" placeholder="请输接收人id" style="width: 300px"></el-input>
      </el-form-item>

      <el-form-item label="是否为公告" prop="issystem" style="width: 200px">
        <el-select v-model="form.issystem">
          <el-option  :value="0" label="否">否</el-option>
          <el-option  :value="1" label="是">是</el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" size="small" @click="doAddSysDictData">添加</el-button>
        <el-button type="warning" size="small" @click="doReset">重置</el-button>
      </el-form-item>
    </el-form>
</el-card>

  </div>
</template>

<script>
import outboxApi from "@/api/message/outbox";
import {Message} from "element-ui";

export default {
  name: "message-add",
  data() {
    return {
      isDisable: true,
      form: {
        title: undefined,
        content: undefined,
        issystem: 0,
        recipientId:1


      },
      rules: {
        title: [{required: true, trigger: "blur", message: "标题不能为空"}],
        content: [{required: true, trigger: "blur", message: "内容不能为空"}],
        issystem: [{required: true, trigger: "blur", message: "内容不能为空"}]
      }
    }
  }, methods: {
    doAddSysDictData() {
      this.$refs["form"].validate((flag) => {
        if (flag) {
          outboxApi.send(this.form).then((rs) => {
            //关闭页面 刷新页面
            Message.success(rs.msg)
            this.form = {
              title: undefined,
              content: undefined,
              issystem: 0
            }
            this.$emit("close")
          })
        }
      })
      outboxApi.inUserMsg()
    },
    doReset() {
      this.form = {
        title: undefined,
        content: undefined,
        issystem: 0
      }
    }
  }
}
</script>

<style scoped>

</style>
