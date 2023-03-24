<template>
<div>
  <el-descriptions class="margin-top" :column="2"  border>
  <el-descriptions-item>
    <template slot="label">
      <i class="el-icon-warning-outline"></i>
      发布人
    </template>
    {{ activeRow.createBy }}
  </el-descriptions-item>
  <el-descriptions-item>
    <template slot="label">
      <i class="el-icon-date"></i>
      发布时间时间
    </template>
    {{activeRow.createTime}}
  </el-descriptions-item>
  <el-descriptions-item>
    <template slot="label">
      <i class="el-icon-more"></i>
      通知类型
    </template>
<!--    <dict-tag :value="activeRow.noticeType" :data-list="typeOptions"></dict-tag>-->
    <el-tag v-for="dictData in typeOptions" v-if="dictData.dictValue==activeRow.noticeType" :type="dictData.dictSkin">
      {{ dictData.dictLabel }}
    </el-tag>
  </el-descriptions-item>
  <el-descriptions-item>
    <template slot="label">
      <i class="el-icon-more"></i>
      状态类型
    </template>
<!--    <dict-tag :value="activeRow.status" :data-list="statusOptions"></dict-tag>-->
    <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue==activeRow.status" :type="dictData.dictSkin">
      {{ dictData.dictLabel }}
    </el-tag>
  </el-descriptions-item>
  <el-descriptions-item>
    <template slot="label">
      <i class="el-icon-s-tools"></i>
      公告内容
    </template>
    {{ activeRow.noticeContent }}
  </el-descriptions-item>
  </el-descriptions>
</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";

export default {
  name: "sys-notice-info",
  props:{
    activeRow:{
      type:Object,
      default:undefined
    }
  },created() {
    this.loadDictDataByDictType()
  },
  data(){
    return{
      statusOptions:[],
      typeOptions:[]
    }
  },methods:{
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_notice_status").then((rs) => {
        this.statusOptions = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_notice_type").then((rs) => {
        this.typeOptions = rs.data
      })
    }
  }
}
</script>

<style scoped>

</style>
