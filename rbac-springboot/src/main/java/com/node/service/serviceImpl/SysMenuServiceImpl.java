package com.node.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import com.node.constants.CoreConstant;
import com.node.domain.SysUser;
import com.node.enums.StateEnums;
import com.node.vo.RouterVo;
import com.node.vo.SysMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.node.mapper.SysMenuMapper;
import com.node.domain.SysMenu;
import com.node.service.SysMenuService;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public int deleteByPrimaryKey(Long menuId) {
        return sysMenuMapper.deleteByPrimaryKey(menuId);
    }

    @Override
    public int insert(SysMenu record) {
        return sysMenuMapper.insert(record);
    }

    @Override
    public int insertSelective(SysMenu record) {
        //改造添加方法 为其添加编号
        //获取当前对象的父id 以及类型 类型分为等级
        Long parentId = record.getParentId();
        Integer type = record.getMenuType();
        int insert = 0;
        //使用自旋锁思想 添加成功后 调出循环 防止出现多条数据同时添加 id重复的现象
       /* while (true){
            try {
                //获取当前的类型下父菜单相同的的最大值
                Long max=sysMenuMapper.getMax(parentId,type);
                if (max !=null){
                    record.setMenuId(max+1L);
                }else {
                    //为空时 添加第一个编号
                    record.setMenuId(new Long(parentId*100+1));
                }
                 insert = sysMenuMapper.insert(record);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        while (true) {
            try {
                Long maxId = sysMenuMapper.getMax(parentId, type);
                System.out.println(maxId);
                if (maxId != null) {
                    // 非空  主键值+1 即可
                    record.setMenuId(maxId + 1L);
                    //record.setMenuId(1L);
                } else {
                    if (type.equals(StateEnums.MENU_M.getCode())) {
                        // 如果为一级菜单
                        record.setMenuId(1L);
                    } else if (type.equals(StateEnums.MENU_C.getCode())) {
                        record.setMenuId(Long.parseLong(parentId + "01"));
                    } else if (type.equals(StateEnums.MENU_F.getCode())) {
                        record.setMenuId(Long.parseLong(parentId + "01"));
                    }
                }
                insert = sysMenuMapper.insert(record);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return insert;
    }

    @Override
    public SysMenu selectByPrimaryKey(Long menuId) {
        return sysMenuMapper.selectByPrimaryKey(menuId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysMenu record) {
        return sysMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysMenu record) {
        return sysMenuMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysMenu> selectByMenuNameAndStatus(SysMenu sysMenu, SysUser user) {
        /*
         * 查询用户权限
         * 如果当前用户是超级管理员 查询所有的菜单
         * 如果当前用户是其他用户 根据用户查询所对应的菜单
         * */
        List<SysMenu> list = null;
        if (user.getUserType().equals(StateEnums.USER_TYPE_ADMIN.getCode())) {
            list = sysMenuMapper.selectByMenuNameAndStatus(sysMenu);
        } else {
            //根据用户查询对应的菜单选项 需要查询用户的角色 获取对应的菜单权限
            list = sysMenuMapper.selectUserStatus(sysMenu, user.getUserId());

        }
        return list;
    }

    @Override
    public int hasChildren(Long menuId) {
        //判断当前菜单是否包含子菜单
        return sysMenuMapper.hasChildren(menuId);
    }

    @Override
    public int delete(Long menuId) {
        //改变状态为删除
        return sysMenuMapper.delete(menuId);
    }

    @Override
    public List<SysMenu> listMenuMC(SysUser user) {
        //根据当前用户信息判断查询一级二级菜单
        List<SysMenu> sysMenu = new ArrayList<>();
        if (user.getUserType().equals(StateEnums.USER_TYPE_ADMIN.getCode())) {
            sysMenu = sysMenuMapper.selectMenu();
        } else {
            sysMenu = sysMenuMapper.selectMenuById(user.getUserId());
        }
        return sysMenu;
    }

    /*
     * 查询角色下的路由
     * 格式需要与前端需要的路由格式一致 通过中间类 menuVo -->RouterVo
     * */
    @Override
    public List<RouterVo> getRouters(SysUser sysUser) {
        /*
         * 判断用户类型 如果用户类型是管理员 获取所有的菜单权限
         * 如果是普通角色 根据角色类型 获取相应的菜单权限(1,2级 不包括按钮)
         * */


        //获取一二级菜单
        List<SysMenu> list = this.listMenuMC(sysUser);
        //将sysMenu --> sysMenuVo
        ArrayList<SysMenuVo> sysMenuVoList = new ArrayList<>();
        list.forEach(sysMenu -> {
            //创建一个新的 sysMenuVo的对象 通过工具类将类中相同属性名的属性进行转移
            SysMenuVo sysMenuVo = new SysMenuVo();
            BeanUtils.copyProperties(sysMenu, sysMenuVo);
            //转移后将所有菜单加入到新的集合
            sysMenuVoList.add(sysMenuVo);
        });
        //获取顶级菜单 （一级菜单，根据类型判断）
        ArrayList<SysMenuVo> topMenu = new ArrayList<>();
        sysMenuVoList.forEach(sysMenuVo -> {
            if (sysMenuVo.getMenuType().equals(StateEnums.MENU_M.getCode())) {
                topMenu.add(sysMenuVo);
            }
        });
        //根据顶级菜单获取对应的二级菜单 放入SysMenuVo内部的集合中
        topMenu.forEach(sysMenuVo -> {
            sysMenuVoList.forEach(sysMenuVo1 -> {
                if (sysMenuVo1.getParentId().equals(sysMenuVo.getMenuId())) {
                    //将子菜单添加到顶级菜单的children中
                    sysMenuVo.getChildren().add(sysMenuVo1);
                }
            });
        });
        //定义方法将当前的SysMenuVo集合转换为 RouterVo 集合
        return transToRouterVo(topMenu);
    }

    /**
     * {
     * path: '/system',
     * component: Layout,
     * redirect: '/system/dictType',
     * alwaysShow: true,
     * name: 'system',
     * meta: {
     * title: '系统管理',
     * icon: 'lock',
     * },
     * children: [
     * {
     * // 字典类型管理
     * path: '/dicType',
     * component: () => import('@/views/system/sysDictType/sys-dict-type-list'),
     * name: 'dictType',
     * meta: {
     * title: '字典类型管理',
     * }
     * }}
     */
    private List<RouterVo> transToRouterVo(List<SysMenuVo> topMenu) {
        //方法中将属性赋值 转换 放入新的RouterVo
        ArrayList<RouterVo> routerVoList = new ArrayList<>();
        topMenu.forEach(top -> {
            RouterVo vo = new RouterVo();
            //菜单的名字
            vo.setName(top.getMenuName());
            //路由的源信息 （内部类）
            vo.setMeta(new RouterVo.MetaVo(top.getMenuName(), top.getIcon()));
            //设置是否总是显示 判断是否是最顶级的菜单目录
            vo.setAlwaysShow(top.getMenuType().equals(StateEnums.MENU_M.getCode()));
            //组件的地址 如果是以及菜单 是LAYOUT 否则就是组件的地址
            vo.setComponent(top.getMenuType().equals(StateEnums.MENU_M.getCode()) ? CoreConstant.DEFAULT_COMPONENT : top.getComponent());
            //设置是否隐藏
            vo.setHidden(top.getVisible() == 1);
            //设置路由地址 如果是顶级路由需要加/ 普通的不需要
            vo.setPath(top.getMenuType().equals(StateEnums.MENU_M.getCode()) ? CoreConstant.URL_SPLIT + top.getPath() : top.getPath());
            //设置重定向
            vo.setRedirect(top.getMenuType().equals(StateEnums.MENU_M.getCode()) ? CoreConstant.NO_REDIRECT : null);
            //设置子路由
            //vo.setChildren(top.getChildren());
            List<SysMenuVo> children = top.getChildren();
            //因为子路由也要进行相同的操作 所以可以用递归
            //判断子路由是否为空
            if (CollectionUtil.isNotEmpty(children)){
                vo.setChildren(transToRouterVo(children));
            }
            //每次循环获得的值加入list
            routerVoList.add(vo);

        });
        routerVoList.forEach(routerVo -> {
            System.out.println(routerVo+"----------------------");
        });
        return routerVoList;
    }

}
