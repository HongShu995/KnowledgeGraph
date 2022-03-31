package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色DTO
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO
{
    /*
    角色id
     */
    private Long id;

    /*
    角色名
     */
    private String roleName;
}
