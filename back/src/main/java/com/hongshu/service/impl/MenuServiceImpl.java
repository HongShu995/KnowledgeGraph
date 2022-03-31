package com.hongshu.service.impl;

import com.hongshu.constant.CommonConstant;
import com.hongshu.dao.MenuDao;
import com.hongshu.dao.RoleDao;
import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.MenuDTO;
import com.hongshu.dto.UserMenuDTO;
import com.hongshu.entity.Menu;
import com.hongshu.entity.Role;
import com.hongshu.exception.MyException;
import com.hongshu.service.MenuService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.util.UserUtils;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hongshu.constant.CommonConstant.*;

/**
 * 用户菜单实例
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Service
public class MenuServiceImpl implements MenuService
{
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO)
    {
        //查询菜单数据
        List<Menu> menuList;
        if (!Objects.equals(conditionVO.getKeywords(), ""))
        {
            menuList = menuDao.listMenusByKeywords(conditionVO.getKeywords());
        } else
        {
            menuList = menuDao.listMenus();
        }
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<String, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        List<MenuDTO> menuDTOList = catalogList.stream().map(item -> {
            MenuDTO menuDTO = BeanCopyUtils.copyObject(item, MenuDTO.class);
            // 获取目录下的菜单排序
            List<MenuDTO> list = BeanCopyUtils.copyList(childrenMap.get(item.getName()), MenuDTO.class).stream()
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTO.setChildren(list);
            childrenMap.remove(item.getName());
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum)).collect(Collectors.toList());
        // 若还有菜单未取出则拼接
        if (childrenMap != null) {
            List<Menu> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<MenuDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTOList.addAll(childrenDTOList);
        }
        return menuDTOList;
    }

    @Override
    public List<UserMenuDTO> listUserMenus()
    {
        // 查询用户菜单信息
        Role role = roleDao.queryUserRole(UserUtils.getLoginUser().getId());
        List<Menu> menuList = menuDao.listMenusByRoleId(role.getId());
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<String, List<Menu>> childrenMap = getMenuMap(menuList);
        // 转换前端菜单格式
        return convertUserMenuList(catalogList, childrenMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMenu(MenuVO menuVO)
    {
        List<Menu> menuList = menuDao.listMenusByKeywords(menuVO.getName());
        if (menuList.size() > 0 )
        {
            throw new MyException("该菜单已存在");
        }
        Menu menu = BeanCopyUtils.copyObject(menuVO, Menu.class);
        menuDao.addMenu(
                menu.getName(),
                menu.getPath(),
                menu.getComponent(),
                menu.getIcon(),
                menu.getOrderNum(),
                menu.getParentName(),
                menu.getIsHidden(),
                getCurrentTime(),
                NULL);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMenu(Long menuId)
    {
        // 查询是否有角色关联
        Integer count = menuDao.countRoleMenu(menuId);
        if (count > 0) {
            throw new MyException("菜单下有角色关联，请先删除该菜单与角色的关系");
        }
        // 查询子菜单
        Menu menu = menuDao.queryById(menuId);
        List<Long> menuIdList = menuDao.listChildrenMenu(menu.getName())
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        menuIdList.add(menuId);
        menuIdList.forEach(menuDao::deleteMenuById);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(MenuVO menuVO)
    {
        menuDao.setMenu(
                menuVO.getId(),
                menuVO.getName(),
                menuVO.getPath(),
                menuVO.getComponent(),
                menuVO.getIcon(),
                menuVO.getOrderNum(),
                menuVO.getParentName(),
                menuVO.getIsHidden(),
                getCurrentTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeMenuIsHidden(ConditionVO conditionVO)
    {
        menuDao.changeMenuIsHidden(conditionVO.getId(), conditionVO.getWhether(),getCurrentTime());
        menuDao.update(conditionVO.getId(), CommonConstant.getCurrentTime());
    }

    @Override
    public List<LabelOptionDTO> listMenuOptions()
    {
        // 查询菜单数据
        List<Menu> menuList = menuDao.listMenus();
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<String, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        return catalogList.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getName());
            if (children != null) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 获取目录列表
     *
     * @param menuList 菜单列表
     * @return 目录列表
     */
    private List<Menu> listCatalog(List<Menu> menuList)
    {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getParentName()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    /**
     * 获取目录下菜单列表
     *
     * @param menuList 菜单列表
     * @return 目录下的菜单列表
     */
    private Map<String, List<Menu>> getMenuMap(List<Menu> menuList)
    {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentName()))
                .collect(Collectors.groupingBy(Menu::getParentName));
    }

    /**
     * 转换用户菜单格式
     *
     * @param catalogList 目录
     * @param childrenMap 子菜单
     */
    private List<UserMenuDTO> convertUserMenuList(List<Menu> catalogList, Map<String, List<Menu>> childrenMap) {
        return catalogList.stream().map(item -> {
            // 获取目录
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();
            // 获取目录下的子菜单
            List<Menu> children = childrenMap.get(item.getName());
            if (children != null) {
                // 多级菜单处理
                userMenuDTO = BeanCopyUtils.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuDTO dto = BeanCopyUtils.copyObject(menu, UserMenuDTO.class);
                            dto.setHidden(menu.getIsHidden().equals(BOOLEAN_TRUE));
                            return dto;
                        })
                        .collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(COMPONENT);
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(BOOLEAN_TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
    }
}
