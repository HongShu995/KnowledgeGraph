package com.hongshu.dao;

import com.hongshu.entity.Resource;
import com.hongshu.entity.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 资源DAO
 *
 * @author HongShu995
 * @create 2022-01-23
 */
public interface ResourceDao extends Neo4jRepository<Resource,Long>
{
    /**
     * 删除所有资源
     */
    @Query("MATCH (n:Resource) WHERE id(n)<>91 DELETE n")
    void removeAllResource();

    /**
     * 移除所有角色资源权限
     */
    @Query("MATCH ()-[r:RoleResource]-() DELETE r")
    void removeAllAuthority();

    /**
     * 给予管理员所有权限
     */
    @Query("MATCH (n:Role),(m:Resource) WHERE id(n)=22 CREATE (n)-[r:RoleResource{ name:'角色资源' }]->(m)")
    void rootAuthority();

    /**
     * 查询所有资源列表
     *
     * @return 资源列表
     */
    @Query("MATCH (n:Resource) WHERE id(n)<>91 RETURN n")
    List<Resource> listResource();

    /**
     * 查询模块
     *
     * @return 模块列表
     */
    @Query("MATCH (n:Resource{ url:'NULL' }) WHERE id(n)<>91 RETURN n")
    List<Resource> listModel();

    /**
     * 查询接口
     *
     * @return 接口列表
     */
    @Query("MATCH (n:Resource) WHERE id(n)<>91 AND n.url<>'NULL' RETURN n")
    List<Resource> listInterface();

    /**
     * 查询资源选项
     *
     * @return 资源列表
     */
    @Query("MATCH (n:Resource{ isAnonymous:FALSE }) WHERE id(n)<>91 RETURN n")
    List<Resource> listResourceOption();

    /**
     * 根据角色Id查询资源
     *
     * @param roleId 角色Id
     * @return 资源列表
     */
    @Query("MATCH (n:Role)-[r:RoleResource]->(m:Resource) WHERE id(n)={id} RETURN m")
    List<Resource> listResourcesByRoleId(@Param("id")Long roleId);

    /**
     * 模糊查询资源
     *
     * @param keywords 搜索内容
     * @return 资源列表
     */
    @Query("MATCH (n:Resource) WHERE id(n)<>91 AND n.resourceName=~ ('.*'+{keywords}+'.*') RETURN n")
    List<Resource> ListResourceLikeKeywords(@Param("keywords")String keywords);

    /**
     * 根据资源Id查询拥有资源角色
     *
     * @param id 资源Id
     * @return 角色列表
     */
    @Query("MATCH (n:Role)-[r:RoleResource]->(m:Resource) WHERE id(m)={id} AND m.url<>'NULL' RETURN n.roleLabel")
    List<String> listResourceRole(@Param("id")Long id);

    /**
     * 查询角色拥有的资源Id
     *
     * @param id 角色Id
     * @return 资源Id列表
     */
    @Query("MATCH (n:Role)-[r:RoleResource]->(m:Resource) WHERE id(n)={id} RETURN id(m)")
    List<Long> listRoleResourceId(@Param("id")Long id);

    /**
     * 查询模块的资源
     *
     * @param id 资源Id
     * @return 资源列表
     */
    @Query("MATCH (n:Resource{ parentId:{id} }) RETURN id(n)")
    List<Long> listModelResourceId(@Param("id")Long id);

    /**
     * 查询资源拥有角色数量
     *
     * @param id 资源Id
     * @return 角色数量
     */
    @Query("MATCH (n:Role)-[r:RoleResource]->(m:Resource) WHERE id(m)={id} RETURN COUNT(n)")
    Integer countRoleResource(@Param("id")Long id);

    /**
     * 修改模块
     *
     * @param id 资源Id
     * @param resourceName 资源名称
     */
    @Query("MATCH (n:Resource) WHERE id(n)={id} SET n.resourceName={resourceName}")
    void setModel(@Param("id")Long id, @Param("resourceName")String resourceName);

    /**
     * 修改资源
     *
     * @param id 资源Id
     * @param resourceName 资源名称
     * @param url 路径
     * @param requestMethod 请求方式
     */
    @Query("MATCH (n:Resource) WHERE id(n)={id} SET n.resourceName={resourceName}, n.url={url}, n.requestMethod={requestMethod}, n.parentId={parentId}")
    void setResource(@Param("id")Long id,
                     @Param("resourceName")String resourceName,
                     @Param("url")String url,
                     @Param("requestMethod")String requestMethod);

    /**
     * 设置资源是否匿名访问
     *
     * @param id 资源Id
     * @param isAnonymous 是否匿名访问
     */
    @Query("MATCH (n:Resource) WHERE id(n)={id} SET n.isAnonymous={isAnonymous}")
    void setResourceAnonymous(@Param("id")Long id, @Param("isAnonymous")Boolean isAnonymous);

    /**
     * 设置角色资源
     *
     * @param roleId 角色Id
     * @param resourceId 资源Id
     */
    @Query("MATCH (n:Role), (m:Resource) WHERE id(n)={roleId} AND id(m)={resourceId} CREATE (n)-[r:RoleResource{ name:'角色资源' }]->(m)")
    void setRoleResource(@Param("roleId")Long roleId, @Param("resourceId")Long resourceId);

    /**
     * 添加资源
     *
     * @param resourceName 资源名
     * @param url 资源路径
     * @param requestMethod 请求方法
     * @param parentId 所属模块Id
     * @param isAnonymous 是否匿名访问
     * @param createTime 创建时间
     * @param updateTime 修改时间
     */
    @Query("CREATE (n:Resource{ resourceName:{resourceName}, url:{url}, requestMethod:{requestMethod}, parentId:{parentId}, isAnonymous:{isAnonymous}, createTime:{createTime}, updateTime:{updateTime} })")
    void addResource(@Param("resourceName")String resourceName,
                  @Param("url")String url,
                  @Param("requestMethod")String requestMethod,
                  @Param("parentId")Long parentId,
                  @Param("isAnonymous")Boolean isAnonymous,
                  @Param("createTime")String createTime,
                  @Param("updateTime")String updateTime);


    /**
     * 删除所有角色资源
     *
     * @param roleId 角色Id
     */
    @Query("MATCH (n:Role)-[r:RoleResource]->(m:Resource) WHERE id(n)={id} DELETE r")
    void deleteAllRoleResourceById(@Param("id")Long roleId);

    /**
     * 根据Id删除资源
     *
     * @param id 资源Id
     */
    @Query("MATCH (n:Resource) WHERE id(n)={id} DELETE n")
    void delete(@Param("id")Long id);

    /**
     * 更新资源
     *
     * @param id 资源Id
     * @param updateTime 更新时间
     */
    @Query("MATCH (n:Resource) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);
}
