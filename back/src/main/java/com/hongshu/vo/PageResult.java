package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  分页对象
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T>
{
    /**
    分页列表
     */
    private List<T> recordList;

    /**
    总数
     */
    private Integer count;
}
