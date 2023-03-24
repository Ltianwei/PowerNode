package com.node.controller;


import com.node.annotations.AppPermission;
import com.node.common.Page;
import com.node.common.Result;
import com.node.domain.SysDictData;
import com.node.enums.ResultEnums;
import com.node.service.CacheService;
import com.node.service.SysDictDataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysDictData")
public class SysDictDataController extends BaseController{

    @Resource
    private SysDictDataService sysDictDataService;

    @Resource
    private CacheService cacheService;

    /**
     * 查询字典数据 通过分页查询
     * */
    @AppPermission("system:dictData:query")
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){
        Page<List<SysDictData>> list= sysDictDataService.getByPage(page);
       return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",list);
    }

    /*
    * 添加字典数据
    * */
    @AppPermission("system:dictData:add")
    @PostMapping("/save")
    public Result save(@RequestBody SysDictData sysDictData){

        sysDictData.setCreateTime(new Date());
        sysDictData.setCreateBy(getUserName());
        int insert = sysDictDataService.insert(sysDictData);
        if (insert<1){
            return new Result(ResultEnums.ERROR,"添加失败");
        }
        return new Result(ResultEnums.SUCCESS,"添加成功");
    }

    /*
    * 更新字典数据
    * */
    @AppPermission("system:dictData:update")
    @PutMapping("/update")
    public Result update(@RequestBody SysDictData sysDictData){
        sysDictData.setUpdateTime(new Date());
        sysDictData.setUpdateBy(getUserName());
        int i = sysDictDataService.updateByPrimaryKey(sysDictData);
        if (i<1){
            return new Result(ResultEnums.ERROR,"更新失败");
        }
        return new Result(ResultEnums.SUCCESS,"更新成功");
    }


    /*
    * 根据id单独查询字典数据
    * */

    @GetMapping("/get/{dictCode}")
    public Result getById(@PathVariable Long dictCode){

        SysDictData sysDictData = sysDictDataService.selectByPrimaryKey(dictCode);
        return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",sysDictData);
    }

    /*
    * 根据id删除数据
    * */
    @AppPermission("system:dictData:remove")
    @DeleteMapping("/delete/{dictCode}")
    public Result deleteById(@PathVariable Long dictCode){
        int i = sysDictDataService.deleteByPrimaryKey(dictCode);
        if (i<1){
            return new Result(ResultEnums.ERROR,"删除失败");
        }
        return new Result(ResultEnums.SUCCESS,"删除成功");
    }

    /*
    * 根据类型查询数据 若缓存中存在 在缓存中查找 若不存在 在数据库中查找
    * */
    @GetMapping("/getDictDataByDictType/{dictType}")
    public Result getDictDataByDictType(@PathVariable String dictType){
        List<SysDictData> list=cacheService.getDictDataByDictType(dictType);
        return new Result(ResultEnums.SUCCESS.getCode(),"查询成功",list);
    }

}
