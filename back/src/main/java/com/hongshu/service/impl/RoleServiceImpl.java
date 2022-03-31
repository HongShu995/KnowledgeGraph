package com.hongshu.service.impl;

import com.hongshu.dao.MenuDao;
import com.hongshu.dao.ResourceDao;
import com.hongshu.dao.RoleDao;
import com.hongshu.dao.UserDao;
import com.hongshu.dto.ResourceRoleDTO;
import com.hongshu.dto.RoleDTO;
import com.hongshu.dto.UserRoleDTO;
import com.hongshu.entity.Menu;
import com.hongshu.entity.Resource;
import com.hongshu.entity.Role;
import com.hongshu.exception.MyException;
import com.hongshu.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.hongshu.service.RoleService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.PageResult;
import com.hongshu.vo.RoleVO;
import com.hongshu.vo.UserRolesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hongshu.constant.CommonConstant.BOOLEAN_FALSE;
import static com.hongshu.constant.CommonConstant.getCurrentTime;
import static com.hongshu.constant.CommonConstant.NULL;
/**
 * 角色服务
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Service
public class RoleServiceImpl implements RoleService
{
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDTO> listUserRoles()
    {
        List<Role> roleList = roleDao.listRoles();
        return BeanCopyUtils.copyList(roleList,UserRoleDTO.class);
    }

    @Override
    public PageResult<RoleDTO> listRoles(ConditionVO conditionVO)
    {
        Integer count = roleDao.countRole();
        if (count == 0)
        {
            return new PageResult<>();
        }
        // 获取后台用户列表
        List<Role> roles;
        if(!Objects.equals(conditionVO.getKeywords(), ""))
        {
            roles = roleDao.listRolesWithRoleName(conditionVO.getKeywords());
        } else
        {
            roles = roleDao.listRoles(conditionVO.getSkip(), conditionVO.getLimit());
        }
        List<RoleDTO> roleDTOS = BeanCopyUtils.copyList(roles, RoleDTO.class);
        roleDTOS.forEach(roleDTO -> {
            roleDTO.setMenuIdList(menuDao.listRoleMenuId(roleDTO.getId()));
            roleDTO.setResourceIdList(resourceDao.listRoleResourceId(roleDTO.getId()));
        });
        return new PageResult<>(roleDTOS,count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO)
    {
        // 判断角色名重复
        List<Role> roleList = roleDao.listRolesWithRoleName(roleVO.getRoleName());
        if (roleList.size() > 0)
        {
            for (Role role : roleList)
            {
                if (!role.getId().equals(roleVO.getId()))
                {
                    throw new MyException("角色名已存在");
                }
            }
            // 更新角色信息
            roleDao.setRoleName(roleVO.getId(), roleVO.getRoleName());
            roleDao.setRoleLabel(roleVO.getId(), roleVO.getRoleLabel());
            roleDao.update(roleVO.getId(), getCurrentTime());
            // 更新角色菜单
            if (roleVO.getMenuIdList() != null)
            {
                List<Menu> menuList = menuDao.listMenusByRoleId(roleVO.getId());
                if (menuList.size() > 0)
                {
                    menuDao.deleteAllRoleMenuById(roleVO.getId());
                }
                for (Long menuId : roleVO.getMenuIdList())
                {
                    menuDao.setRoleMenu(roleVO.getId(), menuId);
                }
            }
            // 更新角色资源
            if (roleVO.getResourceIdList() != null)
            {
                List<Resource> resourceList = resourceDao.listResourcesByRoleId(roleVO.getId());
                if (resourceList.size() > 0)
                {
                    resourceDao.deleteAllRoleResourceById(roleVO.getId());
                }
                for (Long resourceId : roleVO.getResourceIdList())
                {
                    resourceDao.setRoleResource(roleVO.getId(), resourceId);
                }
                // 重新加载角色资源信息
                filterInvocationSecurityMetadataSource.clearDataSource();
            }
        } else
        {
            // 添加角色
            roleDao.addRole(
                    roleVO.getRoleName(),
                    roleVO.getRoleLabel(),
                    BOOLEAN_FALSE,
                    getCurrentTime(),
                    NULL);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setUserRoles(UserRolesVO userRolesVO)
    {
        // 删除用户的所有角色并修改名称
        roleDao.deleteUserAllRole(userRolesVO.getUserId());
        userDao.setUserNickname(userRolesVO.getUserId(), userRolesVO.getNickname());
        // 重新添加用户角色
        if (userRolesVO.getRoleList().size() > 0)
        {
            for (Long roleId : userRolesVO.getRoleList())
            {
                roleDao.setUserRole(userRolesVO.getUserId(),roleId);
            }
        }
        userDao.update(userRolesVO.getUserId(), getCurrentTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleDisable(ConditionVO conditionVO)
    {
        roleDao.setRoleDisable(conditionVO.getId(), conditionVO.getWhether());
        roleDao.update(conditionVO.getId(), getCurrentTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoles(List<Long> roleIdList)
    {
        // 判断角色下是否有用户
        for (Long roleId : roleIdList)
        {
            if (roleDao.queryUserByRoleId(roleId) != null)
            {
                throw new MyException("该角色下存在用户");
            }
        }

        for (Long roleId : roleIdList)
        {
            menuDao.deleteAllRoleMenuById(roleId);
            roleDao.deleteRole(roleId);
        }
    }

    @Override
    public List<ResourceRoleDTO> listResourceRoles()
    {
        List<ResourceRoleDTO> resourceRoleDTOList = new ArrayList<>();
        List<Resource> resourceList = resourceDao.listInterface();
        for (Resource resource : resourceList)
        {
            List<String> roleList = resourceDao.listResourceRole(resource.getId());
            resourceRoleDTOList.add(
                    ResourceRoleDTO.builder()
                    .id(resource.getId())
                    .url(resource.getUrl())
                    .requestMethod(resource.getRequestMethod())
                    .roleList(roleList)
                    .build());
        }
        return resourceRoleDTOList;
    }
}
