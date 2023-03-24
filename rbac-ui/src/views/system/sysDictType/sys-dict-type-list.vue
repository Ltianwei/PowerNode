<template>
  <div>
    <el-card style="margin-top: 20px">
      <el-form inline>
        <el-form-item>
          <el-input size="small" placeholder="字典名称" v-model="page.params.dictName" style="width: 200px"
                    clearable></el-input>
        </el-form-item>
        <el-form-item  label="状态" align="center">
            <!--<el-select v-model="page.params.status" placeholder="请选择状态">
             <el-option :value="0" label="启用"></el-option>
            <el-option :value="1" label="停用"></el-option></el-select>
            -->
            <!--   用缓存中的方法 -->
          <el-select v-model="page.params.status" placeholder="请选择状态" clearable>
             <el-option v-for="dicData in statusOptions" :value="dicData.dictValue" :label="dicData.dictLabel"></el-option>
            </el-select>

        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="el-icon-search" @click="doGetPage">查询</el-button>
          <el-button size="small" type="warning" icon="el-icon-refresh" @click="doReset">重置</el-button>
          <el-button type="primary" @click="refreshCache" icon="el-icon-refresh">同步缓存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 5px">
      <el-button icon="el-icon-plus" type="primary" size="small" @click="toAddDialog">添加</el-button>
      <el-table :data="pageData.list" border
                stripe
                style="width: 100%">
        <el-table-column prop="dictId" label="类型编号" align="center"/>
        <el-table-column prop="dictName" label="类型名称" align="center"/>
        <el-table-column prop="dictType" label="字典类型" align="center">
          <!--  将字典类型进行改造 可以点击进入字典数据类型的路由界面     -->
          <template slot-scope="{row}">
            <!--  会转到相应的路由  路由传参 在路由定义地址时定义接受参数的路由地址 router.index -->
            <router-link :to="'/system/dictData/index/'+row.dictType" style="color: cornflowerblue">{{ row.dictType }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="字典备注" align="center"/>
        <el-table-column prop="status" label="状态" align="center">
        <template scope="{row}">
          <!-- 根据状态类型查询所有的状态值 如果状态值与当前行一致 显示状态的Label 类型是dictskin   -->
          <el-tag v-for="dictData in statusOptions" v-if="dictData.dictValue == row.status"
                  :type="dictData.dictSkin">{{dictData.dictLabel}}</el-tag>
        </template>
        </el-table-column>
        <!-- 全局组件使用的值 -->
        <!-- <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
        <el-table-column prop="createTime" label="创建时间" align="center"/>
        <el-table-column prop="updateTime" label="更新时间" align="center"/>
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-dropdown>
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit" @click="toUpdate(row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="toDelete(row)">删除</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        :current-page="page.pageNumber"
        :page-sizes="[10,20,30,50]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.totalCount"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    <!--  定义弹出对话框 引入子组件-->
    <el-dialog :title="title" :visible.sync="addDialog" center="center" width="80%">
      <!--  引入子组件  通过冒泡让子组件执行父组件的方法-->
      <sys-dict-type-add @close="closeDialog"></sys-dict-type-add>
    </el-dialog>

    <!--  定义弹出对话框 引入子组件 更新-->
    <!--  close 关闭对话框时的回调方法  -->
    <el-dialog :title="title" :visible.sync="updateDialog" center="center" width="80%" @close="reflush">
      <!--  引入子组件  通过冒泡让子组件执行父组件的方法-->
      <!--  向子类传入参数  :active-id="activeId"  左边用- 分割 参数用驼峰（规范）-->
      <sys-dict-type-update @close="closeDialog" :active-id="activeId"></sys-dict-type-update>
    </el-dialog>

  </div>
</template>

<script>
import sysDictTypeListApi from "@/api/system/dictType/sysDictTypeList";
import SysDictTypeAdd from "@/views/system/sysDictType/sys-dict-type-add";
import SysDictTypeUpdate from "@/views/system/sysDictType/sys-dict-type-update";
import {Message} from "element-ui";
import sysDictDataList from "@/api/system/dictData/sysDictDataList";


export default {
  name: "sys-dict-type-list",
  components: {SysDictTypeUpdate, SysDictTypeAdd},
  data() {
    return {
      page: {
        params: {
          dictName: undefined,
          status: 0
        },
        pageNumber: 1,
        pageSize: 10,
      }, pageData: {
        totalCount: undefined,
        pageNumber: undefined,
        list: []
      }, addDialog: false,
      updateDialog: false,
      title: undefined,
      activeId: undefined,
      statusOptions:[]

    }
  }, methods: {
    //根据字典类型获取相应所有的数据信息
    loadDictDataByDictType() {
      //获取获取所有的状态值 所有状态的类型是sys_status_type
      //查询到所有类型可选值 放入数组statusOptions
      //调用
      sysDictDataList.getDictDataByDictType("sys_status_type").then((rs)=>{
        this.statusOptions=rs.data
      })
    },
    toUpdate(row) {
      /*更新操作 将当前行的id传给子组件 需要使用父传子数据 子的冒泡调用父类的方法*/
      this.activeId = row.dictId;
      this.title = "更新数据";
      this.updateDialog = true

    }, toDelete(row) {
      this.$confirm(`确定要删除【${row.dictName}】吗`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "error"
      }).then(() => {
        sysDictTypeListApi.delete(row.dictId).then(() => {
          this.page.pageNumber = 1
          this.doGetPage()
        })
      })

    }, handleSizeChange(size) {
      this.page.pageSize = size
      this.page.pageNumber = 1
      this.doGetPage();

    }, handleCurrentChange(num) {
      this.page.pageNumber = num;
      this.doGetPage();

    }, doReset() {
      this.page = {
        params: {
          dictName: undefined,
          status: 0
        },
        pageNumber: 1,
        pageSize: 10,
      }
    }, doGetPage() {
      sysDictTypeListApi.getByPage(this.page).then(rs => {
        this.pageData.totalCount = rs.data.totalCount;
        this.pageData.pageNumber = rs.data.pageNumber;
        this.pageData.list = rs.data.list;
      })
    }, toAddDialog() {
      //添加的方法 此时弹出对话框 通过父子组件的方式进行展示
      this.addDialog = true;
      this.title = "添加字典类型"
    }, closeDialog() {
      //让子组件冒泡执行父组件的方法
      //关闭弹出层 清空表中数据
      this.addDialog = false;
      this.updateDialog = false;
      this.title = undefined
      this.doGetPage();
    }, refreshCache() {
      //刷新缓存
      sysDictTypeListApi.refreshCache().then(rs => {
        Message.success(rs.msg)
      })
    },
    reflush(){
      //重新刷新表单 保证对话框数据不发生变化
      this.doGetPage()
    }
  }, created() {
    this.doGetPage();
    this.loadDictDataByDictType()
  }
}
</script>

<style scoped>

</style>
