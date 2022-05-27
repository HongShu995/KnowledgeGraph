package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单DTO
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO
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

    /*
    是否隐藏
     */
    private Boolean isHidden;

    /*
    子菜单列表
     */
    private List<MenuDTO> children;

    /*
    创建时间
     */
    private String createTime;
}
