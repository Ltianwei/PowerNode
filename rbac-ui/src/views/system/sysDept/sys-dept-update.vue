<template>
<div>

  <el-form :model="form" ref="form" :rules="rules" size="small" label-width="78px">
    <el-form-item label="上级部门" style="width: 80%">
      <!--             <el-input v-model="form.parentId"></el-input>-->
      <treeselect v-model="form.parentId" :options="deptList" :normalizer="normalizer" :show-count="true"
                  placeholder="上级部门"/>
    </el-form-item>
    <el-row>
      <el-col :span="12">
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" style="width: 60%"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="1" :max="100"></el-input-number>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" style="width: 60%"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" style="width: 50%"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="邮箱">
          <el-input v-model="form.email" style="width: 60%"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="部门状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{ dictData.dictLabel }}
            </el-radio>
          </el-radio-group>

        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item>
      <el-button type="primary" @click="toUpdateSysDept"> 更新</el-button>
    </el-form-item>
  </el-form>


</div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
import {Message} from "element-ui";
import {handleTree} from "@/utils/powernode";
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
export default {
  name: "sys-dept-update",
  created() {
    this.loadDictDataByDictType()
    this.loadDeptList()
  },
  props:{
    updateId:{
      type:Number,
      default:undefined
    }
  },
  watch:{
    updateId:{
      immediate:true,
      handler:function (newVal,oldVal ) {
        this.form.deptId=newVal
        this.doSearch(newVal)
      }
    }
  },
  data(){
    return{
      statusOptions:[],
      form:{
        parentId:undefined,
        deptName:undefined,
        orderNum:undefined,
        leader:undefined,
        phone:undefined,
        email:undefined,
        status:0
      },
      deptList:[],
      rules:{
        deptName: [
          {
            required: true, trigger: "blur", message: "部门名称不能为空!"
          }
        ],
        orderNum: [
          {
            required: true, trigger: "blur", message: "请指定排序值!"
          }
        ],
        leader: [
          {
            required: true, trigger: "blur", message: "请指定部门负责人!"
          }
        ],
        phone: [
          {
            required: true, trigger: "blur", message: "请指定联系电话!"
          }
        ]
      }
    }
  },methods:{
    loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },
    doSearch(params){
      sysDeptListApi.get(params).then(rs=>{
        this.form=rs.data
        //当前不存在父级的部门 将父级部门设置为自己
        if (this.form.parentId==0){
          this.form.parentId=this.form.deptId
        }
      })
    },toUpdateSysDept(){
      this.$refs['form'].validate((flag)=>{
        if (flag){
          sysDeptListApi.update(this.form).then(rs=>{
            Message.success(rs.msg)
            this.$emit("close")
          })
        }
      })
    },loadDeptList() {
      sysDeptListApi.listDept().then((rs)=>{
        //传入当前的对象 以及对应的主键值
        this.deptList=handleTree(rs.data,deptId)
      })
    },
    normalizer(node){
      return {
        //id 主键 label 显示的值
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    }
  }
}
</script>

<style scoped>

</style>
