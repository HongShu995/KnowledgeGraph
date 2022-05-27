package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户后台DTO
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBackDTO
{
    /**
    id
     */
    private Long id;

    /**
    头像
     */
    private String avatar;

    /**
    昵称
     */
    private String nickname;

    /**
    用户名
     */
    private String username;

    /**
    是否禁用
     */
    private Boolean isDisable;

    /**
    用户角色
     */
    private List<UserRoleDTO> roleList;

    /**
    创建时间
     */
    private String createTime;

    /**
    最近登录时间
     */
    private String lastLoginTime;
}
