<template>
<div>

  <el-input
    placeholder="输入关键字进行过滤"
    v-model="deptName">
  </el-input>

  <el-tree
    class="filter-tree"
    :data="data"
    :props="defaultProps"
    default-expand-all
    :filter-node-method="filterNode"
    ref="tree"
    @node-click="handleNodeClick"
  >
  </el-tree>


</div>
</template>

<script>
import {handleTree} from "@/utils/powernode";
import sysDeptListApi from "@/api/system/sysDept/sysDeptList";
export default {
  name: "sys-user-left",
  created() {
    this.doSearchDept()
  },watch:{
    //监视器
    deptName(val){
      this.$refs.tree.filter(val)
    }
  },
  data(){
    return{
      defaultProps:{
        children:'children',
        label:'deptName'
      },
      deptName:'',
      data:[]
    }
  },methods:{
    filterNode(value,data){
      if (!value)return true;
      //判断 是否有值
      return data.deptName.indexOf(value)!=-1

    },handleNodeClick(data){
      /*调用父类的方法 同时传参*/
      this.$emit("reloadData",data.deptId)
    },doSearchDept(){
      sysDeptListApi.listDept().then(rs=>{
        this.data=handleTree(rs.data,"deptId")
      })
    }
  }
}
</script>

<style scoped>

</style>
