package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户
 *
 * @author HongShu995
 * @create 2022-01-10
 */
@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    /**
    数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
    用户名
     */
    private String username;

    /**
    密码
     */
    private String password;

    /**
    头像
     */
    private String avatar;

    /**
    用户昵称
     */
    private String nickname;

    /**
    是否被禁用
     */
    private Boolean isDisable;

    /**
    创建时间
     */
    private String createTime;

    /**
    修改时间
     */
    private String updateTime;

    /**
    最后登录时间
     */
    private String lastLoginTime;


    public User(String username, String password, String avatar, String nickname, Boolean isDisable, String createTime, String updateTime, String lastLoginTime)
    {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.nickname = nickname;
        this.isDisable = isDisable;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.lastLoginTime = lastLoginTime;
    }
}
