package com.node.service;

import com.node.domain.SysDictData;

import java.util.List;

public interface CacheService {
    void refreshCache();

    List<SysDictData> getDictDataByDictType(String dictType);
}
