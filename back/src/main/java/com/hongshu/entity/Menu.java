package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * 菜单
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu
{
    /**
    数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
    菜单名
     */
    private String name;

    /**
    路径
     */
    private String path;

    /**
    组件
     */
    private String component;

    /**
    图标
     */
    private String icon;

    /**
    排序
     */
    private Integer orderNum;

    /**
    父组件名称
     */
    private String parentName;

    /**
    是否隐藏
     */
    private Boolean isHidden;

    /**
    创建时间
     */
    private String createTime;

    /**
    修改时间
     */
    private String updateTime;
}
