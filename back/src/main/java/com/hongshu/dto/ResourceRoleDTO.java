package com.hongshu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 资源角色
 *
 * @author HongShu995
 * @create 2022-01-24
 */
@Data
@Builder
public class ResourceRoleDTO
{
    /*
    资源id
     */
    private Long id;

    /*
    路径
     */
    private String url;

    /*
    请求方式
     */
    private String requestMethod;

    /*
    角色名
     */
    private List<String> roleList;
}
