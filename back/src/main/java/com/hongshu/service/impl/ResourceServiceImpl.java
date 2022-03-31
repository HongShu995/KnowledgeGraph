package com.hongshu.service.impl;

import com.hongshu.dao.ResourceDao;
import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.ResourceDTO;
import com.hongshu.entity.Resource;
import com.hongshu.exception.MyException;
import com.hongshu.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.hongshu.service.ResourceService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hongshu.constant.CommonConstant.*;

/**
 * 资源服务实例
 *
 * @author HongShu995
 * @create 2022-01-23
 */
@Service
public class ResourceServiceImpl implements ResourceService
{
    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public void importSwagger()
    {
        // 移除所有角色拥有的权限
        resourceDao.removeAllAuthority();
        // 删除所有资源
        resourceDao.removeAllResource();
        Map<String, Object> data = restTemplate.getForObject("http://localhost:8080/v2/api-docs", Map.class);
        // 获取所有模块
        List<Map<String, String>> tagList = (List<Map<String, String>>) data.get("tags");
        tagList.forEach(item -> {
            resourceDao.addResource(item.get("name"),NULL,NULL,
                    null,BOOLEAN_FALSE,getCurrentTime(),NULL);
        });
        List<Resource> resourceList = resourceDao.listResource();
        Map<String, Long> permissionMap = resourceList.stream()
                .collect(Collectors.toMap(Resource::getResourceName, Resource::getId));
        resourceList.clear();
        // 获取所有接口
        Map<String, Map<String, Map<String, Object>>> path = (Map<String, Map<String, Map<String, Object>>>) data.get("paths");
        path.forEach((url, value) -> value.forEach((requestMethod, info) -> {
            String permissionName = info.get("summary").toString();
            List<String> tag = (List<String>) info.get("tags");
            Long parentId = permissionMap.get(tag.get(0));
            Resource resource = Resource.builder()
                    .resourceName(permissionName)
                    .url(url.replaceAll("\\{[^}]*\\}", "*"))
                    .parentId(parentId)
                    .requestMethod(requestMethod.toUpperCase())
                    .isAnonymous(BOOLEAN_FALSE)
                    .build();
            resourceList.add(resource);
        }));
        for (Resource resource : resourceList)
        {
            resourceDao.addResource(
                    resource.getResourceName(),
                    resource.getUrl(),
                    resource.getRequestMethod(),
                    resource.getParentId(),
                    resource.getIsAnonymous(),
                    getCurrentTime(),
                    NULL);
        }
        //给管理员添加所有资源权限
        resourceDao.rootAuthority();
    }

    @Override
    public List<ResourceDTO> listResources(ConditionVO conditionVO)
    {
        // 查询资源列表
        List<Resource> resourceList = resourceDao.ListResourceLikeKeywords(conditionVO.getKeywords());
        // 获取所有模块
        List<Resource> parentList = resourceDao.listModel();
        // 根据父id分组获取模块下的资源
        Map<Long,List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 绑定模块下的所有接口
        List<ResourceDTO> resourceDTOList = parentList.stream().map(item -> {
            ResourceDTO resourceDTO = BeanCopyUtils.copyObject(item, ResourceDTO.class);
            List<ResourceDTO> childrenList = BeanCopyUtils.copyList(childrenMap.get(item.getId()), ResourceDTO.class);
            resourceDTO.setChildren(childrenList);
            childrenMap.remove(item.getId());
            return resourceDTO;
        }).collect(Collectors.toList());
        // 若还有资源未取出则拼接
        if (childrenMap.size() > 0) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, ResourceDTO.class))
                    .collect(Collectors.toList());
            resourceDTOList.addAll(childrenDTOList);
        }
        return resourceDTOList;
    }

    @Override
    public List<LabelOptionDTO> listResourceOption()
    {
        // 查询资源列表
        List<Resource> resourceList = resourceDao.listResourceOption();
        // 获取所有模块
        List<Resource> parentList = resourceDao.listModel();
        // 根据父id分组获取模块下的资源
        Map<Long, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Resource> children = childrenMap.get(item.getId());
            if (children.size() > 0) {
                list = children.stream()
                        .map(resource -> LabelOptionDTO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getResourceName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateResource(ResourceVO resourceVO)
    {
        // 更新资源
        if (resourceVO.getId() != null)
        {
            resourceDao.setResource(resourceVO.getId(),
                    resourceVO.getResourceName(),
                    resourceVO.getUrl(),
                    resourceVO.getRequestMethod());
            resourceDao.update(resourceVO.getId(), getCurrentTime());
        } else if (resourceVO.getParentId() != null)
        {
            resourceDao.addResource(resourceVO.getResourceName(),
                    resourceVO.getUrl(),
                    resourceVO.getRequestMethod(),
                    resourceVO.getParentId(),
                    BOOLEAN_FALSE,
                    getCurrentTime(),
                    NULL);
        } else
        {
            resourceDao.addResource(resourceVO.getResourceName(),
                    NULL, NULL, null, BOOLEAN_FALSE,
                    getCurrentTime(), NULL);
        }
        // 重新加载角色资源信息
        filterInvocationSecurityMetadataSource.clearDataSource();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeResource(ConditionVO conditionVO)
    {
        resourceDao.setResourceAnonymous(conditionVO.getId(), conditionVO.getWhether());
        resourceDao.update(conditionVO.getId(), getCurrentTime());
    }

    @Override
    public void deleteResource(Long resourceId)
    {
        // 查询是否有角色关联
        Integer count = resourceDao.countRoleResource(resourceId);
        if (count > 0)
        {
            throw new MyException("该资源下存在角色");
        }
        List<Long> resourceIdList = resourceDao.listModelResourceId(resourceId);
        resourceIdList.add(resourceId);
        for (Long id : resourceIdList)
        {
            resourceDao.delete(id);
        }
    }

    /**
     * 获取模块下的所有资源
     *
     * @param resourceList 资源列表
     * @return 模块资源
     */
    private Map<Long, List<Resource>> listResourceChildren(List<Resource> resourceList)
    {
        return resourceList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }

    /**
     * 获取所有资源模块
     *
     * @param resourceList 资源列表
     * @return 资源模块列表
     */
    private List<Resource> listResourceModule(List<Resource> resourceList)
    {
        return resourceList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
    }
}
