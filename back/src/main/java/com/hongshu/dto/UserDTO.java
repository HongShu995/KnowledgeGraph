package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户DTO
 *
 * @author HongShu995
 * @create 2022-01-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO
{
    /**
    用户id
     */
    private Long id;

    /**
    用户名
     */
    private String username;

    /**
    用户昵称
     */
    private String nickname;

    /**
    头像
     */
    private String avatar;

    /**
    最近登录时间
     */
    private Date lastLoginTime;
}
