package com.hongshu.service;

import com.hongshu.dto.LabelOptionDTO;
import com.hongshu.dto.LinkDTO;
import com.hongshu.dto.NodeDTO;
import com.hongshu.dto.OptionDTO;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.LinkVO;
import com.hongshu.vo.NodeVO;

import java.util.List;

/**
 * 知识点服务
 *
 * @author HongShu995
 * @create 2022-01-25
 */
public interface KnowledgeService
{
    /**
     * 获取节点
     *
     * @param conditionVO 查询条件
     * @return 节点列表
     */
    List<NodeDTO> listNode(ConditionVO conditionVO);
    /**
     * 获取所有连接关系
     *
     * @return 连线列表
     */
    List<LinkDTO> listAllLink();

    /**
     * 获取节点选项
     *
     * @return 选项列表
     */
    List<OptionDTO> listOptions();

    /**
     * 保存或更新节点
     *
     * @param nodeVO 节点信息
     */
    void saveAndUpdateNode(NodeVO nodeVO);

    /**
     * 保存或更新连线
     *
     * @param linkVO 连线信息
     */
    void saveAndUpdateLink(LinkVO linkVO);

    /**
     * 删除节点
     *
     * @param id 节点Id
     */
    void deleteNode(Long id);

    /**
     * 删除关系
     *
     * @param id 关系Id
     */
    void deleteLink(Long id);
}
