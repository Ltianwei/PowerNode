package com.node.service.serviceImpl;

import com.node.domain.SysDictData;
import com.node.domain.SysDictType;
import com.node.service.CacheService;
import com.node.service.SysDictDataService;
import com.node.service.SysDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private SysDictTypeService sysDictTypeService;

    @Resource
    private SysDictDataService sysDictDataService;

    private Map<String, List<SysDictData>> map=new HashMap<>();

    /*
    * 刷新缓存 需要查出所有类型对应的值
    * */
    @Override
    public void refreshCache() {
        List<SysDictType> allDicType = sysDictTypeService.getAllDicType(null);

        for (SysDictType type : allDicType) {
            //根据类型查询所有的数据 并将其放入集合
           List<SysDictData> list= sysDictDataService.selectByType(type.getDictType());
           map.put(type.getDictType(),list);
        }
    }

    @Override
    public List<SysDictData> getDictDataByDictType(String dictType) {

        List<SysDictData> list = map.get(dictType);
        //如果缓存中不存在 在数据库中查找
        if (list == null){
            list = sysDictDataService.selectByType(dictType);

            //将查找到的数据添加到缓存中
            addRefreshCache(dictType,list);
        }

        return list;
    }

    private void addRefreshCache(String type,List<SysDictData> list){
        if (!list.isEmpty()){
            map.put(type,list);
        }
    }
}
