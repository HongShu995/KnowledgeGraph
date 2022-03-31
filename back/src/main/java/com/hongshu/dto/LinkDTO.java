package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接DTO
 *
 * @author HongShu995
 * @create 2022-01-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO
{
    /*
    Id
     */
    private Long id;

    /*
    连线名称
     */
    private String name;

    /*
    开始节点Id
     */
    private String source;

    /*
    结束节点Id
     */
    private String target;

    /*
    创建时间
     */
    private String createTime;
}
