package com.hongshu.dao;

import com.hongshu.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户DAO
 *
 * @author HongShu995
 * @create 2022-01-10
 */
@Repository
public interface UserDao extends Neo4jRepository<User,Long>
{
    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    @Query("MATCH (n:User) WHERE id(n)={id} RETURN n")
    User queryUserById(@Param("id")Long id);

    /**
     * 通过用户id更新用户
     *
     * @param id 用户id
     * @param lastLoginTime 最后登录时间
     */
    @Query("MATCH (n:User) WHERE id(n)={id} SET n.lastLoginTime = {lastLoginTime}")
    void updateByUsername(@Param("id")Long id,@Param("lastLoginTime")String lastLoginTime);

    /**
     * 添加用户
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param avatar 头像
     * @param isDisable 是否禁用
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @param lastLoginTime 最后登录时间
     */
    @Query("CREATE (n:User{ username:{username},  password:{password},  nickname:{nickname}, avatar:{avatar}, isDisable:{isDisable},  createTime:{createTime},  updateTime:{updateTime},  lastLoginTime:{lastLoginTime} }) RETURN n")
    User addUser(@Param("username")String username,
                 @Param("password")String password,
                 @Param("nickname")String nickname,
                 @Param("avatar")String avatar,
                 @Param("isDisable")Boolean isDisable,
                 @Param("createTime")String createTime,
                 @Param("updateTime")String updateTime,
                 @Param("lastLoginTime")String lastLoginTime);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Query("MATCH (n:User { username:{username} }) RETURN n")
    User queryByUsername(@Param("username")String username);

    /**
     * 用户数量统计
     *
     * @return 用户数量
     */
    @Query("MATCH (n:User) RETURN COUNT(n)")
    Integer countUser();

    /**
     * 获取用户列表
     *
     * @param skip 跳过数量
     * @param limit 每页数量
     * @return 用户列表
     */
    @Query("MATCH (n:User) RETURN n SKIP {skip}  LIMIT {limit}")
    List<User> listUsers(@Param("skip")Integer skip, @Param("limit")Integer limit);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    @Query("MATCH (n:User{ username:{username} }) RETURN n")
    List<User> listUsersWithUsername(@Param("username")String username);

    /**
     * 通过用户昵称查询用户
     *
     * @param nickname 用户昵称
     * @return 用户列表
     */
    @Query("MATCH (n:User{ nickname:{nickname} }) RETURN n")
    List<User> listUsersWithNickname(@Param("nickname")String nickname);

    /**
     * 修改用户是否禁用
     *
     * @param id 用户Id
     * @param isDisable 是否禁用
     */
    @Query("MATCH (n:User) WHERE id(n)={id} SET n.isDisable={isDisable}")
    void setUserDisable(@Param("id")Long id, @Param("isDisable")Boolean isDisable);

    /**
     * 修改用户名称
     *
     * @param id 用户Id
     * @param nickname 用户昵称
     */
    @Query("MATCH (n:User) WHERE id(n)={id} SET n.nickname={nickname}")
    void setUserNickname(@Param("id")Long id, @Param("nickname")String nickname);

    /**
     * 修改用户密码
     *
     * @param id 用户Id
     * @param password 用户密码
     */
    @Query("MATCH (n:User) WHERE id(n)={id} SET n.password={password}")
    void setUserPassword(@Param("id")Long id, @Param("password")String password);

    /**
     * 修改更新时间
     *
     * @param id 用户Id
     * @param updateTime 修改时间
     */
    @Query("MATCH (n:User) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);
}
