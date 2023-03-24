package com.node.mapper;

import com.node.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectByMenuNameAndStatus(SysMenu sysMenu);

    List<SysMenu> selectUserStatus(@Param("sysMenu") SysMenu sysMenu,@Param("userId") Long userId);

    Long getMax(@Param("parentId") Long parentId,@Param("type") Integer type);

    int hasChildren(@Param("menuId") Long menuId);

    int delete(Long menuId);

    List<SysMenu> selectMenu();


    List<SysMenu> selectMenuById(Long userId);
}