<template>
  <div>
    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item>
        <template slot="label" >
          <i class="el-icon-warning-outline"></i>
          日志主键
        </template>
        {{ activeRow.operId }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label" >
          <i class="el-icon-s-help"></i>
          模块标题
        </template>
        {{ activeRow.title }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-more"></i>
          业务类型
        </template>
        <template >
          <el-tag v-for="data in typeStatus" v-if="data.dictValue==activeRow.businessType" >
            {{ data.dictLabel }}
          </el-tag>
        </template>
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-flag"></i>
          请求方式
        </template>
        <el-tag size="small">
          {{ activeRow.requestMethod }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          操作人员
        </template>
        <el-tag size="small">
          {{ activeRow.operName }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-order"></i>
          请求URL
        </template>
        <el-tag size="small">
          {{ activeRow.operUrl }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-data"></i>
          主机地址
        </template>
        {{ activeRow.operIp }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-position"></i>
          操作地点
        </template>
        {{ activeRow.operLocation }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-chat-dot-square"></i>
          请求参数
        </template>
        {{ activeRow.operParam }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-chat-line-square"></i>
          返回参数
        </template>
<!--        {{ activeRow.jsonResult }}-->
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-cherry"></i>
          操作状态
        </template>
        <template scope="{row}">
          <el-tag v-for="data in status" v-if="data.dictValue==activeRow.status" :type="data.dictSkin">
            {{ data.dictLabel }}
          </el-tag>
        </template>

      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-error"></i>
          错误消息
        </template>
        {{ activeRow.errorMsg }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          操作时间
        </template>
        {{ activeRow.operTime }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-tools"></i>
          方法名称
        </template>
        {{ activeRow.method }}
      </el-descriptions-item>
    </el-descriptions>

  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";

export default {
  name: "sys-oper-log-info",
  created() {
    this.loadDictDataByDictType()

  },
  props: {
    activeRow: {
      type: Object,
      default: undefined
    }
  },watch:{
    activeRow: {
      immediate:true,
      handler:function (val) {
        console.log(val)


      }
    }
  }, data() {
    return {
      status:[],
      typeStatus:[]
    }
  }, methods: {
    loadDictDataByDictType() {
      sysDictDataListApi.getDictDataByDictType("sys_oper_type").then(rs => {
        this.typeStatus = rs.data
        console.log(this.typeStatus)
      })
      sysDictDataListApi.getDictDataByDictType("sys_login_status").then(rs => {
        this.status = rs.data
      })
    }
  }
}
</script>

<style scoped>

</style>
