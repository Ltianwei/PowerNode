<template>
  <div>
    <el-form :model="form" :rules="rules" ref="form" size="small" label-width="100px">
      <el-form-item label="上级菜单" style="width:80% ">
        <!-- 上级菜单 这里是下拉框 使用树形组件 表单格式-->
        <treeselect v-model="form.parentId" :options="menuList" :normalizer="normalizer" :show-count="true"
                    placeholder="上级菜单"/>
      </el-form-item>

      <el-form-item label="菜单类型">
        <el-radio-group v-model="form.menuType">
          <el-radio :label="0">目录</el-radio>
          <el-radio :label="1">菜单</el-radio>
          <el-radio :label="2">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="菜单图标" v-if="form.menuType !=2">
        <el-input v-model="form.icon" placeholder="请选择图标"></el-input>
        </el-form-item>-->

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
        <el-button type="primary" @click="toAddSysMenu">添加</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="toReset">重置</el-button>
      </el-form-item>
    </el-form>


  </div>

</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysMenuListApi from "@/api/system/sysMenu/sysMenuList";
import {Message} from "element-ui";
import {handleTree} from "@/utils/powernode";
import IconSelect from "@/components/IconSelect";

export default {
  name: "sys-menu-add",
  props: {
    activeId: {
      type: Object,
      default: undefined
    }
  }, watch: {
    activeId: {
      immediate: true,
      handler: function (val, oldVal) {
        //console.log(val)
        if (val != null) {
          this.form.parentId = val.menuId
          if (val.menuType == 0) {
            this.form.menuType = 1
            this.form.icon=''
          } else {
            this.form.menuType = 2
            this.form.icon=''
          }
        } else {
          this.form.parentId = 0
          this.form.menuType = 0
          this.form.icon=''
        }
      }
    }
  },
  created() {
    this.loadDictDataByDictType()
    this.getMenuMc()
  },
  components: {
    Treeselect,
    IconSelect
  }, data() {
    return {
      form: {
        parentId: undefined,
        menuType: 0,
        component: undefined,
        query: undefined,
        path: undefined,
        visible: 0,
        status: 0,
        icon: '404'

      },
      rules: {
        menuName: [
          {
            required: true, trigger: "blur", message: "请指定菜单名称!"
          }
        ],
        orderNum: [
          {
            required: true, trigger: "blur", message: "请指定排序!"
          }
        ],
        path: [
          {
            required: true, trigger: "blur", message: "请指定路由地址!"
          }
        ]
      },
      menuList: [],
      statusOptions: [],
      visiableOptions: []
    }
  }, methods: {
    normalizer(node) {
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children,
        //perms: undefined
      }
    }, getMenuMc() {
      sysMenuListApi.listMenuMC().then(rs => {
          let dataList = handleTree(rs.data, "menuId")
          //因为树形组件接收的是数组 所以需要用数组来展示 给根目录附一个父菜单的值
          //用一个新的数组将原来的数据进行包裹
          let topMenu = [
            {
              menuId: 0,
              menuName: "主类目",
              children: []
            }
          ]
          topMenu[0].children = dataList
          this.menuList = topMenu
        }
      )
    }
    , toAddSysMenu() {
      this.$refs['form'].validate(flag => {
        if (flag) {
          sysMenuListApi.save(this.form).then(rs => {
            Message.success(rs.msg)
            this.$emit("close")
            this.toReset()
          })
        }
      })

    }, toReset() {
      this.form = {
        status: 0,
        visible: 0,
        menuType: 0
      }
      this.test1()

    }, loadDictDataByDictType() {
      //获取获取所有的状态值 所有状态的类型是sys_status_type
      //查询到所有类型可选值 放入数组statusOptions
      //调用
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then((rs) => {
        this.statusOptions = rs.data
      })
      sysDictDataListApi.getDictDataByDictType("sys_show_hide").then((rs) => {
        this.visiableOptions = rs.data
      })
    },// 选择图标
    selected(name) {
      this.form.icon = name;
    },test1(){
      sysMenuListApi.getRouters().then(rs=>{
        console.log(rs)
      })
    }
  }

}
</script>

<style scoped>

</style>
