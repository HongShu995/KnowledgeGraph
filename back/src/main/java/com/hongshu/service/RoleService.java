package com.hongshu.service;

import com.hongshu.dto.ResourceRoleDTO;
import com.hongshu.dto.RoleDTO;
import com.hongshu.dto.UserRoleDTO;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.PageResult;
import com.hongshu.vo.RoleVO;
import com.hongshu.vo.UserRolesVO;

import java.util.List;

/**
 * 角色服务
 *
 * @author HongShu995
 * @create 2022-01-18
 */
public interface RoleService
{
    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 获取资源角色列表
     *
     * @return 资源角色列表
     */
    List<ResourceRoleDTO> listResourceRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件信息VO
     * @return 角色列表
     */
    PageResult<RoleDTO> listRoles(ConditionVO conditionVO);

    /**
     * 修改角色禁用状态
     *
     * @param conditionVO 条件信息VO
     */
    void updateRoleDisable(ConditionVO conditionVO);

    /**
     * 设置用户角色
     *
     * @param userRolesVO 用户角色VO
     */
    void setUserRoles(UserRolesVO userRolesVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Long> roleIdList);
}
