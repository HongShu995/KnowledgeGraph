package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户角色VO
 *
 * @author HongShu995
 * @create 2022-01-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRolesVO
{
    /*
    用户Id
     */
    private Long userId;

    /*
    用户昵称
     */
    private String nickname;

    /*
    角色Id
     */
    private List<Long> roleList;
}
