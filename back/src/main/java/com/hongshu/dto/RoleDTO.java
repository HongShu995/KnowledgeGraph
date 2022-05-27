package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.List;

/**
 * 角色DTO
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO
{
    /**
    id
     */
    private Long id;

    /**
    角色名
     */
    private String roleName;

    /**
    角色标签
     */
    private String roleLabel;

    /**
    是否禁用
     */
    private Boolean isDisable;

    /**
    创建时间
     */
    private String createTime;

    /**
    资源id列表
     */
    private List<Long> resourceIdList;

    /**
    菜单id列表
     */
    private List<Long> menuIdList;
}
