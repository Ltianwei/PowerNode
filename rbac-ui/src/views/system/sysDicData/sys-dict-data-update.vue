<template>
  <div>
    <el-form :model="form" ref="form" :rules="rules" size="small" label-width="78px">
      <el-form-item label="字典类型">
        <el-input v-model="form.dictType" placeholder="请输入字典类型" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="数据标签" prop="dictLabel">
        <el-input v-model="form.dictLabel" placeholder="请输入字典标签"></el-input>
      </el-form-item>
      <el-form-item label="键值">
        <el-input-number placeholder="请输入键值" v-model="form.dictValue" :min="1"></el-input-number>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="form.dictSort" placeholder="排序" :min="1"></el-input-number>
      </el-form-item>
      <el-form-item label="皮肤属性">
        <el-select v-model="form.dictSkin" placeholder="请选择皮肤">
          <el-option v-for="skin in skinOptions" :label="skin.label" :value="skin.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" placeholder="请输入备注" v-model="form.remark"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
<!--          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">停用</el-radio>-->
          <el-radio v-for="dictData in skinOptions" :label="dictData.dictValue">{{dictData.dictLabel}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="doUpdateSysDictData">更新</el-button>
        <el-button type="warning" size="small" @click="doReset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import sysDictDataListApi from "@/api/system/dictData/sysDictDataList";
import sysDictTypeList from "@/api/system/dictType/sysDictTypeList";
import {Message} from "element-ui";
import sysDictDataList from "@/api/system/dictData/sysDictDataList";

export default {
  name: "sys-dict-data-update",
  created() {
    this.doSearch(this.form.dictCode)
  },
  props: {
    dictCode: {
      type: Number,
      default: undefined
    }
  }, watch: {
    dictCode: {
      immediate: true,
      handler: function (newVal, oldVal) {
        this.form.dictCode = newVal
        //初始化 根据传过来的id查询当前信息
        this.doSearch(newVal)
      }
    }
  },
  data() {
    return {
      form: {
        dictCode: undefined,
        dictType: undefined,
        dictLabel: undefined,
        dictValue: 1,
        dictSort: 1,
        dictSkin: undefined,
        remark: undefined,
        status: 0
      }, rules: {
        dictLabel: [{
          required: true, trigger: "blur", message: "请指定数据标签!"
        }]
      }, skinOptions: []
    }
  }, methods: {
    created() {
      this.loadDictDataByDictType()
    },
    loadDictDataByDictType(){
      sysDictDataList.getDictDataByDictType("sys_status_type").then((rs)=>{
        this.skinOptions=rs.data
      })
    },
    doSearch(value) {
      //根据id查询信息并填充
      sysDictDataListApi.get(value).then((rs) => {
        this.form = rs.data
      })
    }, doUpdateSysDictData() {
      //更新数据
      this.$refs['form'].validate((flag) => {
        if (flag) {
          sysDictDataListApi.update(this.form).then(rs => {
            //调用父组件的方法
            this.$emit("close")
            Message.success(rs.msg)
            this.form={
              dictValue: 1,
              dictSort: 1,
              status: 0
            }
          })
        }
      })
    }, doReset() {
      this.doSearch()
    }
  }
}
</script>

<style scoped>

</style>
