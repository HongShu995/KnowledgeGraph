package com.hongshu.service;

import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.MenuDTO;
import com.hongshu.dto.UserMenuDTO;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.MenuVO;

import java.util.List;

/**
 * 菜单服务
 *
 * @author HongShu995
 * @create 2022-01-14
 */
public interface MenuService
{
    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 修改菜单是否隐藏
     *
     * @param conditionVO 修改条件
     */
    void changeMenuIsHidden(ConditionVO conditionVO);

    /**
     * 新增菜单
     *
     * @param menuVO 菜单信息
     */
    void saveMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Long menuId);

    /**
     * 修改菜单
     *
     * @param menuVO 菜单信息
     */
    void updateMenu(MenuVO menuVO);
}
