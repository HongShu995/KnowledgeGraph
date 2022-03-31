package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点DTO
 *
 * @author HongShu995
 * @create 2022-01-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO
{
    /*
    Id
     */
    private Long id;

    /*
    名称
     */
    private String name;

    /*
    节点内容
     */
    private String des;

    /*
    知识内容链接
     */
    private String url;

    /*
    节点样式
     */
    private CustomDTO itemStyle;

    /*
    大小
     */
    private Integer symbolSize;

    /*
    知识重要星级（满星5星）
     */
    private Integer level;

    /*
    创建时间
     */
    private String createTime;

    /*
    连线信息
     */
    List<LinkDTO> links;
}
