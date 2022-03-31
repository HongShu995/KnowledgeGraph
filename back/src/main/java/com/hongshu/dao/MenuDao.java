package com.hongshu.dao;

import com.hongshu.entity.Menu;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单DAO
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Repository
public interface MenuDao extends Neo4jRepository<Menu,Long>
{
    /**
     * 根据Id查菜单
     *
     * @param id 菜单id
     * @return 菜单
     */
    @Query("MATCH (n:Menu) WHERE id(n)={id} RETURN n")
    Menu queryById(@Param("id")Long id);

    /**
     * 通过内容查询菜单列表
     *
     * @return 菜单列表
     */
    @Query("MATCH (n:Menu{ name:{name} }) RETURN n")
    List<Menu> listMenusByKeywords(@Param("name") String keywords);

    /**
     * 通过目录名称查询子菜单
     *
     * @param parentName 父菜单名称
     * @return 菜单列表
     */
    @Query("MATCH (n:Menu{ parentName:{parentName} }) RETURN n")
    List<Menu> listChildrenMenu(@Param("parentName")String parentName);

    /**
     * 通过内容查询菜单列表
     *
     * @return 菜单列表
     */
    @Query("MATCH (n:Menu) RETURN n")
    List<Menu> listMenus();

    /**
     * 根据角色查询菜单
     *
     * @param id 角色Id
     * @return 菜单列表
     */
    @Query("MATCH (n:Role)-[r:RoleMenu]->(m:Menu) WHERE id(n)={id} RETURN m")
    List<Menu> listMenusByRoleId(@Param("id")Long id);

    /**
     * 添加菜单
     *
     * @param name 菜单名
     * @param path 路径
     * @param component 组件
     * @param icon 图标
     * @param orderNum 排序
     * @param parentName 父菜单名称
     * @param isHidden 是否隐藏
     * @param createTime 创建时间
     * @param updateTime 修改时间
     */
    @Query("CREATE (n:Menu{ name:{name}, path:{path}, component:{component}, icon:{icon}, orderNum:{orderNum}, parentName:{parentName}, isHidden:{isHidden}, createTime:{createTime}, updateTime:{updateTime} })")
    void addMenu(@Param("name")String name,
                 @Param("path")String path,
                 @Param("component")String component,
                 @Param("icon")String icon,
                 @Param("orderNum")Integer orderNum,
                 @Param("parentName")String parentName,
                 @Param("isHidden")Boolean isHidden,
                 @Param("createTime")String createTime,
                 @Param("updateTime")String updateTime);

    /**
     * 修改菜单是否隐藏
     *
     * @param id 菜单id
     * @param isHidden 是否隐藏
     * @param updateTime 修改时间
     */
    @Query("MATCH (n:Menu) WHERE id(n)={id} SET n.isHidden={isHidden}, n.updateTime={updateTime} RETURN n")
    void changeMenuIsHidden(@Param("id")Long id, @Param("isHidden")Boolean isHidden, @Param("updateTime")String updateTime);

    /**
     * 修改菜单
     *
     * @param id 菜单id
     * @param name 菜单名
     * @param path 路径
     * @param component 组件
     * @param icon 图标
     * @param orderNum 排序
     * @param parentName 父组件名称
     * @param isHidden 是否隐藏
     * @param updateTime 修改时间
     */
    @Query("MATCH (n:Menu) WHERE id(n)={id} SET n.name={name}, n.path={path}, n.component={component}, n.icon={icon}, n.orderNum={orderNum}, n.parentName={parentName}, n.isHidden={isHidden}, n.updateTime={updateTime}")
    void setMenu(@Param("id")Long id,
                 @Param("name")String name,
                 @Param("path")String path,
                 @Param("component")String component,
                 @Param("icon")String icon,
                 @Param("orderNum")Integer orderNum,
                 @Param("parentName")String parentName,
                 @Param("isHidden")Boolean isHidden,
                 @Param("updateTime")String updateTime);

    /**
     * 通过Id删除菜单
     *
     * @param id 菜单Id
     */
    @Query("MATCH (n:Menu) WHERE id(n)={id} DELETE n")
    void deleteMenuById(@Param("id")Long id);

    /**
     * 根据角色Id删除所有角色菜单
     *
     * @param id 角色Id
     */
    @Query("MATCH (n:Role)-[r:RoleMenu]->(m:Menu) WHERE id(n)={id} DELETE r")
    void deleteAllRoleMenuById(@Param("id")Long id);

    /**
     * 设置角色菜单
     *
     * @param roleId 角色Id
     * @param menuId 菜单Id
     */
    @Query("MATCH (n:Role), (m:Menu) WHERE id(n)={roleId} AND id(m)={menuId} CREATE (n)-[r:RoleMenu{ name:'角色菜单' }]->(m)")
    void setRoleMenu(@Param("roleId")Long roleId,@Param("menuId")Long menuId);

    /**
     * 查询角色拥有菜单数量
     *
     * @param id 菜单id
     * @return 菜单数
     */
    @Query("MATCH (n:Role)-[r:RoleMenu]->(m:Menu) WHERE id(m)={id} RETURN COUNT(m)")
    Integer countRoleMenu(@Param("id")Long id);

    /**
     * 查询角色拥有的菜单Id
     *
     * @param id 角色Id
     * @return 菜单Id列表
     */
    @Query("MATCH (n:Role)-[r:RoleMenu]->(m:Menu) WHERE id(n)={id} RETURN id(m)")
    List<Long> listRoleMenuId(@Param("id")Long id);

    /**
     * 修改更新时间
     *
     * @param id 菜单Id
     * @param updateTime 修改时间
     */
    @Query("MATCH (n:Menu) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);

}
