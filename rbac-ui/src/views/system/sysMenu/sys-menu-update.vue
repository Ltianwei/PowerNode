<template>
<div>
<el-form :model="form" :rules="rules" ref="form" size="small" label-width="100px">
  <el-form-item label="上级菜单" style="width: 80%">
    <treeselect v-model="form.parentId" :options="menuList"  :normalizer="normalizer" :show-count="true"
                placeholder="上级菜单">
    </treeselect>
  </el-form-item>

  <el-form-item label="菜单类型">
    <el-radio-group v-model="form.menuType">
      <el-radio :label="0">目录</el-radio>
      <el-radio :label="1">菜单</el-radio>
      <el-radio :label="2">按钮</el-radio>
    </el-radio-group>
  </el-form-item>

  <el-row>
    <el-col :span="24" v-if="form.menuType != 2">
      <el-form-item label="菜单图标">
        <el-popover
          placement="bottom-start"
          width="460"
          trigger="click"
          @show="this.$refs['iconSelect'].reset()"
        >
          <IconSelect ref="iconSelect" @selected="selected"/>
          <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
            <svg-icon
              v-if="form.icon"
              slot="prefix"
              :icon-class="form.icon"
              class="el-input__icon"
              style="height: 32px;width: 16px;"
            />
          </el-input>
        </el-popover>
      </el-form-item>
    </el-col>
  </el-row>


  <el-row>
    <el-col :span="16">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="form.menuName"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="显示排序" prop="orderNum">
        <el-input-number v-model="form.orderNum" :min="1" :max="100"></el-input-number>
      </el-form-item>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="12">
      <el-form-item prop="path" v-if="form.menuType !=2">
            <span slot="label">
                <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由地址
              </span>
        <el-input v-model="form.path"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item v-if="form.menuType==1">
            <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
        <el-input v-model="form.component"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <el-form-item v-if="form.menuType==2">

        <el-input v-model="form.perms"></el-input>
        <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限字符
              </span>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="路由参数" v-if="form.menuType==1">
            <span slot="label">
                <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由参数
              </span>
        <el-input v-model="form.query"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <el-form-item label="显示状态" v-if="form.menuType !=2">
        <el-radio-group v-model="form.status">
          <el-radio v-for="dictData in statusOptions" :label="dictData.dictValue">{{ dictData.dictLabel }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-col>
    <el-col :span="12">
      <el-form-item label="菜单状态" v-if="form.menuType !=2">
        <el-radio-group v-model="form.visible">
          <el-radio v-for="dictData in visiableOptions" :label="dictData.dictValue">{{ dictData.dictLabel }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-col>
  </el-row>
  <el-form-item>
    <el-button type="primary" @click="toUpdateSysMenu">更新</el-button>
    <el-button type="warning" icon="el-icon-refresh" @click="toReset">重置</el-button>
  </el-form-item>
</el-form>



</div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {handleTree} from "@/utils/powernode";
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import IconSelect from "@/components/IconSelect";
import sysMenuListApi from "@/api/system/sysMenu/sysMenuList";
import {Message} from "element-ui";

export default {
  name: "sys-menu-update",
  props:{
    UpdateObj:{
      type:Object,
      default:undefined
    }
  },watch:{
    UpdateObj:{
      immediate:true,
      handler:function (val, oldVal) {
        //将接受的值进行json的转换后 在转回对象 防止出现假修改现象
        this.form = JSON.parse(JSON.stringify(val))
      }
    }
  },
  components:{
    Treeselect,
    IconSelect
  },created(){
    this.loadDictDataByDictType()
    this.getMenuMC()
  },data(){
    return{
      value:null,
      form:{
        parentId:0,
        icon:'',
        menuName:undefined,
        orderNum:undefined,
        menuType:undefined,
        query:undefined,
        status:0,
        visible:0
      },
      menuList:[],
      statusOptions:[],
      visiableOptions:[],
      rules:{
        menuName:[{required:true,message:"菜单名称不能为空",trigger:"blur"}],
        orderNum:[{required:true,message:"请指定路由地址",trigger:"blur"}]
      }
    }
  },methods:{
    // 选择图标
    selected(name) {
      this.form.icon = name;
    },
    getMenuMC(){
      //查找一二级菜单 并放入一个数组中
      sysMenuListApi.listMenuMC().then((rs)=>{
        let temp=handleTree(rs.data,"menuId")
        let list=[
          {
            menuId:0,
            menuName:"主根目",
            children:[]
          }
        ]
        list[0].children=temp;
        this.menuList=list
      })
    },
    normalizer(node){
      //指定树形组组件的不同属性
     return{
       id:node.menuId,
       label:node.menuName,
       children:node.children,
     }
    },loadDictDataByDictType(){
      //查询字典数据 添加状态 与是否隐藏
      sysDictDataListApi.getDictDataByDictType("sys_show_hide").then(rs=>{
        this.visiableOptions=rs.data
      });

      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },toUpdateSysMenu(){
      //更新
      this.$refs['form'].validate(flag=>{
        if (flag){
          sysMenuListApi.update(this.form).then(rs=>{
            Message.success(rs.msg)
            this.toReset()
            this.$emit("close")
          })
        }
      })

    },toReset(){
      this.form={
        parentId:undefined,
          icon:'',
          menuName:undefined,
          orderNum:undefined,
          menuType:undefined,
          query:undefined,
          status:0,
          visible:0
      }
    }

  }
}
</script>

<style scoped>

</style>
