<template>
<div>
  <el-form ref="form" :rules="rules" :model="sysNotice" label-width="80px" size="small">
    <el-form-item label="标题" prop="noticeTitle">
      <el-input v-model="sysNotice.noticeTitle" placeholder="请输入标题" clearable/>
    </el-form-item>
    <el-form-item label="内容" prop="noticeContent">
      <el-input v-model="sysNotice.noticeContent" placeholder="请输入内容" type="textarea" clearable/>
    </el-form-item>
    <el-form-item label="类型" prop="noticeType">
      <el-radio-group v-model="sysNotice.noticeType">
        <el-radio v-for="status in typeOptions" :key="status.dictValue" :label="status.dictValue">{{ status.dictLabel }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="状态" prop="status">
      <el-radio-group v-model="sysNotice.status">
        <el-radio v-for="status in statusOptions" :key="status.dictValue" :label="status.dictValue">{{ status.dictLabel }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="toAdd">添加</el-button>
      <el-button type="warning" @click="sysNotice = {}">重置</el-button>
    </el-form-item>
  </el-form>

</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysNoticeApi from "@/api/system/notice/sysNotice";
import {Message} from "element-ui";

export default {
  name: "sys-notice-add",
  created() {
    this.loadDictDataByDictType()
  },data(){
    return{
      rules:{
        noticeTitle: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        noticeContent: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      },
      sysNotice:{
        noticeTitle:undefined,
        noticeContent:undefined,
      },typeOptions:[],
      statusOptions:[]
    }
  },methods:{
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_notice_status").then((rs) => {
        this.statusOptions = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_notice_type").then((rs) => {
        this.typeOptions = rs.data
      })
    },toAdd(){
      this.$refs['form'].validate(flag=>{
        if (flag){
          sysNoticeApi.save(this.sysNotice).then(rs=>{
            Message.success(rs.msg)
            this.$emit("close")
          })
        }
      })

    }

  }
}
</script>

<style scoped>

</style>
