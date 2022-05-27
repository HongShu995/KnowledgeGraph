package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 资源
 *
 * @author HongShu995
 * @create 2022-01-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO
{
    /**
    资源Id
     */
    private Long id;

    /**
    资源名
     */
    @NotBlank(message = "资源名不能为空")
    private String resourceName;

    /**
    路径
     */
    private String url;

    /**
    请求方式
     */
    private String requestMethod;

    /**
    父资源Id
     */
    private Long parentId;
}
