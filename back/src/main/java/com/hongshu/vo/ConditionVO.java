package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 条件
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionVO
{
    /*
    搜索Id
     */
    private Long id;

    /*
    判断条件
     */
    private Boolean whether;

    /*
    搜索内容
     */
    private String keywords;

    /*
    条数
     */
    private Integer limit;

    /*
    跳过条数
     */
    private Integer skip;

    /*
    深度
     */
    private Integer depth;
}
