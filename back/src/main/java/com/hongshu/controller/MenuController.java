package com.hongshu.controller;

import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.MenuDTO;
import com.hongshu.dto.UserMenuDTO;
import com.hongshu.service.MenuService;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.MenuVO;
import com.hongshu.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController
{
    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表
     *
     * @param conditionVO 条件
     * @return {@link Result<MenuDTO>} 菜单列表
     */
    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVO conditionVO)
    {
        return Result.ok(menuService.listMenus(conditionVO));
    }

    /**
     * 新增菜单
     *
     * @param menuVO 菜单
     * @return {@link Result<>}
     */
    @ApiOperation(value = "新增菜单")
    @PostMapping("/admin/menus")
    public Result<?> saveMenu(@Valid @RequestBody MenuVO menuVO)
    {
        menuService.saveMenu(menuVO);
        return Result.ok();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Long menuId){
        menuService.deleteMenu(menuId);
        return Result.ok();
    }

    /**
     * 修改菜单
     *
     * @param menuVO 菜单
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改菜单")
    @PostMapping("/admin/menus/update")
    public Result<?> updateMenu(@Valid @RequestBody MenuVO menuVO)
    {
        menuService.updateMenu(menuVO);
        return Result.ok();
    }

    /**
     * 修改菜单是否隐藏
     *
     * @param conditionVO 条件
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改菜单是否隐藏")
    @PostMapping("/admin/changeMenu")
    public Result<?> changeMenu(@Valid @RequestBody ConditionVO conditionVO)
    {
        menuService.changeMenuIsHidden(conditionVO);
        return Result.ok();
    }

    /**
     * 查看当前用户菜单
     *
     * @return {@link Result<UserMenuDTO>} 菜单列表
     */
    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    public Result<List<UserMenuDTO>> listUserMenus()
    {
        return Result.ok(menuService.listUserMenus());
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result<LabelOptionDTO>} 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public Result<List<LabelOptionDTO>> listMenuOptions()
    {
        return Result.ok(menuService.listMenuOptions());
    }
}
