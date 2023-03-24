package com.node.controller;

import com.node.annotations.AppPermission;
import com.node.common.Page;
import com.node.common.Result;
import com.node.constants.CoreConstant;
import com.node.domain.SysDictType;
import com.node.enums.ResultEnums;
import com.node.service.SysDictTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysDictType")
public class SysDictTypeController extends BaseController{

    @Resource
    private SysDictTypeService typeService;

    /*
    * sysDictType
    * 字典信息管理  分页查询字典类型
     */
    @AppPermission("system:dictType:query")
    @PostMapping("/getByPage")
    public Result sysDictType(@RequestBody Page page){
        page = typeService.sysDictType(page);
        return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",page);
    }

    /**
     * 添加字典类型
     * @param sysDictType
     * @return
     */
    @AppPermission("system:dictType:add")
    @PostMapping("/save")
    public Result saveDicType(@RequestBody SysDictType sysDictType){
        sysDictType.setCreateTime(new Date());
        sysDictType.setCreateBy(getUserName());
        int insert = typeService.insert(sysDictType);
        if (insert<1){
            return new Result(ResultEnums.ERROR,"添加失败");
        }
        return new Result(ResultEnums.SUCCESS,"添加成功");
    }

    /**
     * 根据id查询字典类型
     */
    @GetMapping("/get/{dictId}")
    public Result getDicTypeById(@PathVariable Long dictId){
        SysDictType sysDictType = typeService.selectByPrimaryKey(dictId);
        return new Result(ResultEnums.SUCCESS,sysDictType);
    }

    /**
     * 更新字典数据
     * @param sysDictType
     * @return
     */
    @AppPermission("system:dictType:update")
    @PutMapping("/update")
    public Result updateDicType(@RequestBody SysDictType sysDictType){
        sysDictType.setUpdateTime(new Date());
        sysDictType.setUpdateBy(getUserName());
        int i = typeService.updateByPrimaryKey(sysDictType);

        if (i<1){
            return new Result(ResultEnums.ERROR,"更新失败");
        }
        return new Result(ResultEnums.SUCCESS,"更新成功");
    }

    /**
     * 根据id删除字典类型
     * */
    @AppPermission("system:dictType:remove")
    @DeleteMapping("/delete/{dictId}")
    public Result deleteDicType(@PathVariable Long dictId){
        int i = typeService.deleteByPrimaryKey(dictId);
        if (i<1){
            return new Result(ResultEnums.ERROR,"删除失败");
        }
        return new Result(ResultEnums.SUCCESS,"删除成功");
    }

    /*
    * 获取所有的字典类型 不分页
    * 可以传入字典状态来查询/或者不传 status
    * */
    @GetMapping("/getAllDictType")
    public Result getAllDicType(Integer status){
      List<SysDictType> list= typeService.getAllDicType(status);
      return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",list);
    }

    /*
    * 同步字典数据到缓存中 无返回值
    * */
    @AppPermission("system:dictType:cache")
    @GetMapping("/refreshCache")
    public Result refreshCache(){
        typeService.refreshCache();
        return new Result(ResultEnums.SUCCESS.getCode(),"缓存已刷新");
    }


}
