package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * 角色
 *
 * @author HongShu995
 * @create 2022-01-12
 */
@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role
{
    /*
    数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

    /*
    角色名
     */
    private String roleName;

    /*
    角色标签
     */
    private String roleLabel;

    /*
    是否禁用
     */
    private Boolean isDisable;

    /*
    创建时间
     */
    private String createTime;

    /*
    修改时间
     */
    private String updateTime;
}
