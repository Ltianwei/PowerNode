<template>
  <div>
    <el-card style="margin-top: 5px">
      <el-form :model="page" :inline="true">
        <el-form-item label="标签">
          <el-input v-model="page.params.dictLabel" placeholder="请输入标签" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="page.params.status" clearable>
            <el-option label="正常" :value="0"></el-option>
            <el-option label="停用" :value="1"></el-option>
            <!--            <el-option  v-for="dictData in statusOptions" :label="dictData.dictLabel" :value="dictData.dictValue"></el-option>-->
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="doSearch"> 搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" @click="doReset"> 重置</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="toAddDialog"> 添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>


    <el-card style="margin-top: 5px">
      <el-table :data="dataPage.list" width="100%" border stripe>
        <el-table-column prop="dictCode" label="字典编码" align="center">
        </el-table-column>
        <el-table-column prop="dictLabel" label="字典标签" align="center">
        </el-table-column>
        <el-table-column prop="dictValue" label="字典键值" align="center">
        </el-table-column>
        <el-table-column prop="dictSort" label="排序" align="center">
        </el-table-column>
        <el-table-column prop="dictType" label="字典类型" align="center">
        </el-table-column>
        <el-table-column prop="dict" label="字典状态" align="center">
          <template slot-scope="{row}">
<!--            <el-tag v-if="row.status==0" type="success">启用</el-tag>
            <el-tag v-else type="error">停用</el-tag>-->
          <el-tag   v-for="dictData in statusOptions"  v-if="dictData.dictValue == row.status"  :type="dictData.dictSkin"  >{{dictData.dictLabel}}</el-tag>
            <!--            <dict-tag :value="row.status" :data-list="statusOptions"></dict-tag>-->
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-dropdown>
                <span class="el-dropdown-link">
                  <el-link type="primary">操作</el-link><i class="el-icon-arrow-down el-icon--right"></i>
                </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-edit" @click="toUpdateDialog(row)">修改</el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" icon="el-icon-delete" @click="toDeleteSysDictData(row)">删除</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        :page-size="page.pageSize"
        :page-sizes="[10,20,30,40]"
        :total="dataPage.totalCount"
        layout="total,sizes,prev,pager,next,jumper"
        @current-change="handlerCurrentChange"
        @size-change="handlerSizeChange"
      >
      </el-pagination>

    </el-card>
    <el-dialog :title="title" :visible.sync="isOpen" center="center" v-if="isOpen">
      <sys-dict-data-add @close="closeDialog" :active-id="activeId"></sys-dict-data-add>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="updateOpen" center="center" v-if="updateOpen">
      <sys-dict-type-update :dict-code="dictCode" @close="closeDialog"></sys-dict-type-update>
    </el-dialog>
  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import SysDictDataAdd from "@/views/system/sysDicData/sys-dict-data-add";
import {Message} from "element-ui";
import SysDictTypeUpdate from "./sys-dict-data-update"


export default {
  name: "sys-dict-data-list",
  components: {SysDictTypeUpdate, SysDictDataAdd},
  created() {
    /*分页查询数据*/
    //根据路由传参获取字典类型
    this.doSearch();
    this.page.params.dictType = this.$route.params.dictType
    this.loadDictDataByDictType();
  },
  data() {
    return {
      page: {
        pageSize: 10,
        pageNumber: 1,
        params: {
          dictType: undefined,
          dictLabel: undefined,
          status: 0
        }
      }, dataPage: {
        list: [],
        totalCount: undefined
      }, title: undefined,
      isOpen: false,
      updateOpen:false,
      activeId: undefined,
      dictCode:undefined,
      statusOptions:[]
    }
  }, methods: {
    //引入根据类型查询id
    loadDictDataByDictType(){
      sysDictDataListApi.getDictDataByDictType("sys_status_type").then(rs=>{
        this.statusOptions=rs.data
      })
    },
    doSearch() {
      sysDictDataListApi.getByPage(this.page).then((rs) => {
        this.dataPage.list = rs.data.list
        this.dataPage.totalCount = rs.data.totalCount
      })

    }, doReset() {
      this.page.params = {
        status: 0
      }
    }, toAddDialog() {
      /*将类型传给子路由*/
      this.activeId = this.page.params.dictType
      this.title = "添加字典信息"
      this.isOpen = true

    }, toUpdateDialog(row) {
      //将id传输 根据id更新 防止出现假修改现象
      this.dictCode=row.dictCode
      this.title="修改字典信息"
      this.updateOpen=true
    }
    , toDeleteSysDictData(row) {
      this.$confirm(`您确定要删除${row.dictLabel}吗？`,{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"danger"
      }).then(flag=>{
        if (flag){
          sysDictDataListApi.delete(row.dictCode).then((rs) => {
            Message.success(rs.msg)
            this.doSearch()
          })
        }
      })
    }, handlerCurrentChange(num) {
      this.page.pageNumber=num
      this.doSearch(this.page)

    }, handlerSizeChange(size) {
      this.page.pageSize=size
      this.page.pageNumber=1
      this.doSearch(this.page)

    }, closeDialog() {
      this.title = undefined
      this.isOpen = false
      this.updateOpen=false
      this.doSearch();
    }
  }
}
</script>

<style scoped>

</style>
