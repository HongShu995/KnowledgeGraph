package com.hongshu.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 资源DTO
 *
 * @author HongShu995
 * @create 2022-01-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO
{
    /**
    权限Id
     */
    private Long id;

    /**
    资源名
     */
    private String resourceName;

    /**
    权限路径
     */
    private String url;

    /**
    请求方式
     */
    private String requestMethod;

    /**
    是否匿名访问
     */
    private Boolean isAnonymous;

    /**
    创建时间
     */
    private String createTime;

    /**
    权限列表
     */
    private List<ResourceDTO> children;
}
