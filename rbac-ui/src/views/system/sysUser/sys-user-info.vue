<template>
<div>
<!--    <div >
      <el-image
        style="width: 100px; height: 100px"
        :src="sysUser.avatar"
        fit="fill"
      />
    </div>-->
    <el-descriptions class="margin-top" :column="3"  border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{ sysUser.userName }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          昵称
        </template>
        {{ sysUser.nickName }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        {{ sysUser.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-message"></i>
          邮箱
        </template>
        {{ sysUser.email }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          性别
        </template>
        <el-tag size="small">
          {{sysUser.sex=='0'?"女":"男"}}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          状态
        </template>
        <el-tag size="small">
          {{sysUser.status=='0'?"正常":"停用"}}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-picture-outline-round"></i>
          最后登录IP
        </template>
        <el-tag size="small">
          {{sysUser.loginIp}}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          最后登录时间
        </template>
        {{sysUser.loginDate}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          创建者
        </template>
        {{sysUser.createBy}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          创建时间
        </template>
        {{sysUser.createTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          更新者
        </template>
        {{sysUser.updateBy}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          更新时间
        </template>
        {{sysUser.updateTime}}
      </el-descriptions-item>
    </el-descriptions>
</div>
</template>

<script>
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
import sysUserListApi from "@/api/system/sysUserList/sysUserList";
export default {
  name: "sys-user-info",
  props:{
    userInfoId:{
      type:Object,
      default:undefined
    }
  },watch:{
    userInfoId:{
      immediate:true,
      handler:function (val, oldVal) {
        this.sysUser=val
      }
    }
  },data(){
    return{
      sysUser:undefined

    }
  },methods:{

  }
}
</script>

<style scoped>

</style>
