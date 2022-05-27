package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


/**
 * 资源
 *
 * @author HongShu995
 * @create 2022-01-23
 */
@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resource
{
    /**
    数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
    权限名
     */
    private String resourceName;

    /**
    权限路径
     */
    private String url;

    /**
    请求方式
     */
    private String requestMethod;

    /**
    父权限id
     */
    private Long parentId;

    /**
    是否匿名访问
     */
    private Boolean isAnonymous;

    /**
    创建时间
     */
    private String createTime;

    /**
    修改时间
     */
    private String updateTime;
}
