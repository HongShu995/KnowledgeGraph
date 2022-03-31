package com.hongshu.controller;

import com.hongshu.dto.RoleDTO;
import com.hongshu.dto.UserRoleDTO;
import com.hongshu.service.RoleService;
import com.hongshu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *  角色控制器
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Api(tags = "角色模块")
@RestController
public class RoleController
{
    @Autowired
    private RoleService roleService;

    /**
     * 查询用户角色选项
     *
     * @return {@link Result<UserRoleDTO>} 用户角色选项
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles()
    {
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * 设置用户角色
     *
     * @param userRolesVO 用户角色列表VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "设置用户角色")
    @PostMapping("/admin/users/setRoles")
    public Result<?> setUserRoles(@Valid @RequestBody UserRolesVO userRolesVO)
    {
        roleService.setUserRoles(userRolesVO);
        return Result.ok();
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO)
    {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件信息VO
     * @return {@link Result<RoleDTO>} 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDTO>> listRoles(ConditionVO conditionVO)
    {
        return Result.ok(roleService.listRoles(conditionVO));
    }

    /**
     * 修改角色禁用状态
     *
     * @param conditionVO 条件信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改角色禁用状态")
    @PutMapping("/admin/roles/disable")
    public Result<?> updateUserDisable(@Valid @RequestBody ConditionVO conditionVO)
    {
        roleService.updateRoleDisable(conditionVO);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Long> roleIdList)
    {
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }
}
