package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连线VO
 *
 * @author HongShu995
 * @create 2022-02-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkVO
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
}
