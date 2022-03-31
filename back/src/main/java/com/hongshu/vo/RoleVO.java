package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色VO
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO
{
    /*
    id
     */
    private Long id;

    /*
    角色名
     */
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    /*
    标签
     */
    @NotBlank(message = "权限标签不能为空")
    private String roleLabel;

    /*
    资源列表
     */
    private List<Long> resourceIdList;

    /*
    菜单列表
     */
    private List<Long> menuIdList;
}
