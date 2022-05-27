package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单VO
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuVO
{
    /**
    Id
     */
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
}
