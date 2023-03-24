<template>
  <div>
    <!--
        treeSelect 树形组件

        el-col 表示分栅格 一行分为24格 用el-row包裹
     -->
    <el-form :model="form" ref="form" :rules="rules" size="small" label-width="78px">
      <el-form-item label="上级部门" style="width: 80%">
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
        <el-button type="primary" @click="toAddSysDept"> 添加</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
import {Message} from "element-ui";
//导入第三方插件
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {handleTree} from "@/utils/powernode";


export default {
  name: "sys-dept-add",
  components:{
    Treeselect
  },
  props:{
    activeId:{
      type:Number,
      default:undefined
    }
  },watch:{
    activeId:{
      immediate:true,
      handler:function (newVal, oldVal) {
        this.form.parentId=newVal
      }
    }
  },
  created() {
    this.loadDictDataByDictType()
    this.loadDeptList()
  },data(){
    return{
      statusOptions:[],
      deptList:[],
      form:{
        parentId:undefined,
        deptName:undefined,
        orderNum:1,
        leader:undefined,
        phone:undefined,
        email:undefined,
        status:undefined
      },
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
    toAddSysDept(){
      this.$refs['form'].validate(flag=>{
        if (flag){
          sysDeptListApi.save(this.form).then(rs=>{
            Message.success(rs.msg)
            this.$emit("close")
            this.form={orderNum:1}
          })
        }
      })

    },
    loadDeptList() {
      //加载组件
      sysDeptListApi.listDept().then(res => {
        this.deptList = handleTree(res.data, "deptId")
        console.log(this.deptList)
      })
    },
    normalizer(node) {
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
