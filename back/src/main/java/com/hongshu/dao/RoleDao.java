package com.hongshu.dao;

import com.hongshu.entity.Role;
import com.hongshu.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色DAO
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Repository
public interface RoleDao extends Neo4jRepository<Role,Long>
{
    @Query("MATCH (n:Role) WHERE n.isDisable=false RETURN n")
    List<Role> listRoles();

    /**
     * 根据用户id查询拥有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Query("MATCH (n:User)-[r:UserRole]->(m:Role) WHERE id(n)={id} RETURN m")
    List<Role> listUserRolesById(@Param("id")Long userId);

    /**
     * 根据用户id查询拥有的角色名称
     *
     * @param userId 用户id
     * @return 角色名称列表
     */
    @Query("MATCH (n:User)-[r:UserRole]->(m:Role) WHERE id(n)={id} RETURN m.roleLabel")
    List<String> listUserRoleName(@Param("id")Long userId);

    /**
     * 根据用户id查询拥有的角色
     *
     * @param userId 用户id
     * @return 角色
     */
    @Query("MATCH (n:User)-[r:UserRole]->(m:Role) WHERE id(n)={id} RETURN m")
    Role queryUserRole(@Param("id")Long userId);

    /**
     * 根据Id查询角色
     *
     * @param roleId 角色Id
     * @return 角色
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} RETURN n")
    Role queryById(@Param("id")Long roleId);

    /**
     * 根据角色标签查询Id
     *
     * @param roleLabel 角色标签
     * @return 角色Id
     */
    @Query("MATCH (n:Role{ roleLabel:{roleLabel} }) RETURN id(n)")
    Long queryRoleIdByRoleLabel(@Param("roleLabel")String roleLabel);

    /**
     * 根据角色Id查询用户列表
     *
     * @param roleId 角色Id
     * @return 用户列表
     */
    @Query("MATCH (n:User)-[r:UserRole]->(m:Role) WHERE id(m)={id} RETURN n")
    List<User> listUserByRoleId(@Param("id")Long roleId);

    /**
     * 根据角色Id查询用户
     *
     * @param roleId 角色Id
     * @return 用户
     */
    @Query("MATCH (n:User)-[r:UserRole]->(m:Role) WHERE id(m)={id} RETURN n")
    User queryUserByRoleId(@Param("id")Long roleId);

    /**
     * 添加角色
     *
     * @param roleName 角色名
     * @param roleLabel 角色标签
     * @param isDisable 是否禁用
     * @param createTime 创建时间
     * @param updateTime 修改时间
     */
    @Query("CREATE (n:Role { roleName:{roleName},roleLabel:{roleLabel},isDisable:{isDisable},createTime:{createTime},updateTime:{updateTime} })")
    void addRole(@Param("roleName")String roleName,
                 @Param("roleLabel")String roleLabel,
                 @Param("isDisable")Boolean isDisable,
                 @Param("createTime")String createTime,
                 @Param("updateTime")String updateTime);

    /**
     * 设置用户角色
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    @Query("MATCH (n:User), (m:Role) WHERE id(n)={userId} AND id(m)={roleId} CREATE (n)-[r:UserRole{ name:'用户角色' }]->(m)")
    void setUserRole(@Param("userId")Long userId, @Param("roleId")Long roleId);

    /**
     * 删除用户所有角色
     *
     * @param userId 用户Id
     */
    @Query("MATCH (n:User)-[r:UserRole]->() WHERE id(n)={userId} DELETE r")
    void deleteUserAllRole(@Param("userId")Long userId);

    /**
     * 删除角色
     *
     * @param roleId 角色Id
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} DELETE n")
    void deleteRole(@Param("id")Long roleId);

    /**
     * 角色数量统计
     *
     * @return 角色数量
     */
    @Query("MATCH (n:Role) RETURN COUNT(n)")
    Integer countRole();

    /**
     * 获取角色列表
     *
     * @param skip 跳过数量
     * @param limit 每页数量
     * @return 角色列表
     */
    @Query("MATCH (n:Role) RETURN n SKIP {skip}  LIMIT {limit}")
    List<Role> listRoles(@Param("skip")Integer skip, @Param("limit")Integer limit);

    /**
     * 根据角色名查询角色
     *
     * @param roleName 角色名
     * @return 角色列表
     */
    @Query("MATCH (n:Role { roleName:{roleName} }) RETURN n")
    List<Role> listRolesWithRoleName(@Param("roleName")String roleName);

    /**
     * 设置角色名称
     *
     * @param id 角色Id
     * @param roleName 角色名称
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} SET n.roleName={roleName}")
    void setRoleName(@Param("id")Long id, @Param("roleName")String roleName);

    /**
     * 设置角色标签
     *
     * @param id 角色Id
     * @param roleLabel 角色标签
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} SET n.roleLabel={roleLabel}")
    void setRoleLabel(@Param("id")Long id, @Param("roleLabel")String roleLabel);

    /**
     * 设置角色是否禁用
     *
     * @param id 角色Id
     * @param isDisable 是否禁用
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} SET n.isDisable={isDisable}")
    void setRoleDisable(@Param("id")Long id, @Param("isDisable")Boolean isDisable);

    /**
     * 修改更新时间
     *
     * @param id 角色Id
     * @param updateTime 修改时间
     */
    @Query("MATCH (n:Role) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);
}
