package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息VO
 *
 * @author HongShu995
 * @create 2022-01-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO
{
    /*
    用户名
     */
    private String username;

    /*
    密码
     */
    private String password;

    /*
    昵称
     */
    private String nickname;

    /*
    角色名
     */
    private List<Long> roleList;
}
