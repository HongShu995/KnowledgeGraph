package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * 知识节点
 *
 * @author
 * @create 2022-01-10
 */
@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Knowledge
{
    /**
    数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
    节点名称
     */
    private String name;

    /**
    节点内容
     */
    private String des;

    /**
    知识内容链接
     */
    private String url;

    /**
    颜色
     */
    private String color;

    /**
    大小
     */
    private Integer symbolSize;

    /**
    知识重要星级（满星5星）
     */
    private Integer level;

    /**
    创建时间
     */
    private String createTime;

    /**
    修改时间
     */
    private String updateTime;

}
