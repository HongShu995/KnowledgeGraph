package com.hongshu.service;

import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.ResourceDTO;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.ResourceVO;

import java.util.List;

/**
 * 资源服务
 *
 * @author HongShu995
 * @create 2022-01-23
 */
public interface ResourceService
{
    /**
     * 导入swagger权限
     */
    void importSwagger();

    /**
     * 查看资源列表
     *
     * @param conditionVO 条件
     * @return 资源列表
     */
    List<ResourceDTO> listResources(ConditionVO conditionVO);

    /**
     * 查看资源选项
     *
     * @return 资源选项
     */
    List<LabelOptionDTO> listResourceOption();

    /**
     * 添加或修改资源
     *
     * @param resourceVO 资源对象
     */
    void saveOrUpdateResource(ResourceVO resourceVO);

    /***
     * 删除资源
     *
     @param resourceId 资源id
     */
    void deleteResource(Long resourceId);

    /**
     * 修改资源是否匿名访问
     *
     * @param conditionVO 条件VO
     */
    void changeResource(ConditionVO conditionVO);
}
