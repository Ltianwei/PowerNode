<template>
<div>
<el-tree :data="menuList"
         show-checkbox
         node-key="menuId"
         ref="tree"
         :props="defaultProps"/>
<!-- show-checkbox 是否默认展开 树形节点的主键 -->
<div align="center">
  <el-button type="primary" icon="el-icon-plus" @click="toAddMenu">权限分配</el-button>
</div>
</div>
</template>

<script>
import sysRoleListApi from "@/api/system/sysRole/sysRoleList";
import {handleTree} from "@/utils/powernode";
import {Message} from "element-ui";
import sysMenuListApi from "@/api/system/sysMenu/sysMenuList";

export default {
  name: "sys-role-select",
  created() {
    this.getAllMenu()
  },props:{
    selectId:{
      type:Number,
      default:undefined
    }
  },watch:{
    selectId:{
      immediate:true,
      handler:function (val, oldVal) {
        this.getMenuByRole(val)
      }
    }
  },
  data(){
    return{
      menuList:[],
      defaultProps:{
        //对树形组件进行标注
        children:'children',
        label:'menuName'
      },
      selectMenu:[]//选中的菜单
    }
  },methods:{
    //根据id查询角色的菜单权限
    getMenuByRole(val){
      sysRoleListApi.getCurrentRoleHasMenuIdsByRoleId(val).then(rs=>{
        //对树形组件进行填充
       this.$refs.tree.setCheckedKeys(rs.data,true)
      })
    },
    //获取所有的菜单
    getAllMenu(){
      sysMenuListApi.listDept().then(rs=>{
        this.menuList=handleTree(rs.data,"menuId")
      })
    },toAddMenu(){
      //获取选中的菜单项 包括全选 半选
      //全选的
      this.selectMenu=this.$refs.tree.getCheckedKeys();
      //半选的 一二级菜单标签
      //...表示将当前数组中的数据一个个添加到数组中 而不是所有元素当做一个元素放入
      this.selectMenu.push(...this.$refs.tree.getHalfCheckedKeys())
      //提交 分配权限 传入 传过来的用户id 以及查到的所有菜单数组
      sysRoleListApi.saveRoleMenu(this.selectId,this.selectMenu).then(rs=>{
        Message.success(rs.msg)
        this.$emit("close")
      })

    }

  }
}
</script>

<style scoped>

</style>
