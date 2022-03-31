package com.hongshu.service.impl;

import com.hongshu.dao.KnowledgeDao;
import com.hongshu.dto.CustomDTO;
import com.hongshu.dto.LinkDTO;
import com.hongshu.dto.NodeDTO;
import com.hongshu.dto.OptionDTO;
import com.hongshu.entity.Knowledge;
import com.hongshu.exception.MyException;
import com.hongshu.service.KnowledgeService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.LinkVO;
import com.hongshu.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hongshu.constant.CommonConstant.*;

/**
 * 知识点服务
 *
 * @author HongShu995
 * @create 2022-01-25
 */
@Service
public class KnowledgeServiceImpl implements KnowledgeService
{
    @Autowired
    private KnowledgeDao knowledgeDao;

    @Override
    public List<NodeDTO> listNode(ConditionVO conditionVO)
    {
        List<Knowledge> knowledgeList = new ArrayList<>();
        if (conditionVO.getKeywords() == null || Objects.equals(conditionVO.getKeywords(), ""))
        {
            knowledgeList = knowledgeDao.listKnowledge();
            return packageNode(knowledgeList);
        }
        switch (conditionVO.getDepth())
        {
            case 1:
                knowledgeList = knowledgeDao.listKnowledgeOne(conditionVO.getKeywords());
                break;
            case 2:
                knowledgeList = knowledgeDao.listKnowledgeTwo(conditionVO.getKeywords());
                break;
            case 3:
                knowledgeList = knowledgeDao.listKnowledgeThree(conditionVO.getKeywords());
                break;
            case 4:
                knowledgeList = knowledgeDao.listKnowledgeFour(conditionVO.getKeywords());
                break;
            case 5:
                knowledgeList = knowledgeDao.listKnowledgeFive(conditionVO.getKeywords());
                break;
        }
        return packageNode(clearDuplicateItem(knowledgeList));
    }

    @Override
    public List<LinkDTO> listAllLink()
    {
        List<Long> nodeIds = knowledgeDao.listAllNodeId();
        List<LinkDTO> linkDTOS = new ArrayList<>();
        for (Long id : nodeIds)
        {
            if (knowledgeDao.listLinkIdByNodeId(id) == null)
            {
                continue;
            }
            for (Long linkId : knowledgeDao.listLinkIdByNodeId(id))
            {
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setId(linkId);
                linkDTO.setName(knowledgeDao.getLinkName(linkId));
                linkDTO.setSource(knowledgeDao.getLinkSource(linkId));
                linkDTO.setTarget(knowledgeDao.getLinkTarget(linkId));
                linkDTO.setCreateTime(knowledgeDao.getLinkCreateTime(linkId));
                linkDTOS.add(linkDTO);
            }
        };
        return linkDTOS;
    }

    @Override
    public List<OptionDTO> listOptions()
    {
        List<Knowledge> knowledgeList = knowledgeDao.listKnowledge();
        return BeanCopyUtils.copyList(knowledgeList, OptionDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAndUpdateNode(NodeVO nodeVO)
    {
        if (nodeVO.getId() != null)
        {
            if (nodeVO.getUrl() == null || Objects.equals(nodeVO.getUrl(), ""))
            {
                nodeVO.setUrl(NULL);
            }
            if (nodeVO.getDes() == null || Objects.equals(nodeVO.getDes(), ""))
            {
                nodeVO.setDes(NULL);
            }
            //更新节点信息
            knowledgeDao.updateNode(nodeVO.getId(), nodeVO.getName(),
                    nodeVO.getDes(), nodeVO.getUrl(), nodeVO.getColor(),
                    nodeVO.getSymbolSize(), nodeVO.getLevel());
            knowledgeDao.update(nodeVO.getId(), getCurrentTime());
        } else
        {
            if (nodeVO.getUrl() == null)
            {
                nodeVO.setUrl(NULL);
            }
            //添加节点
            knowledgeDao.addNode(nodeVO.getName(), nodeVO.getDes(),
                    nodeVO.getUrl(), nodeVO.getColor(), nodeVO.getSymbolSize(),
                    nodeVO.getLevel(), getCurrentTime(), NULL);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAndUpdateLink(LinkVO linkVO)
    {
        if (linkVO.getId() == null)
        {
            if (knowledgeDao.countExistRelationship(linkVO.getSource(), linkVO.getTarget()) > 0)
            {
                throw new MyException("关系只能存在一个");
            }
            Long star = Long.valueOf(linkVO.getSource());
            Long end = Long.valueOf(linkVO.getTarget());
            knowledgeDao.addLink(star, end,
                    linkVO.getName(), linkVO.getSource(),
                    linkVO.getTarget(), getCurrentTime(), NULL);
        } else
        {
            knowledgeDao.updateLink(linkVO.getId(), linkVO.getName(), getCurrentTime());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNode(Long id)
    {
        Integer countINCOM = knowledgeDao.countINCOMRelationship(id);
        Integer countOUTCOMR = knowledgeDao.countOUTCOMRelationship(id);
        if (countINCOM > 0 || countOUTCOMR > 0)
        {
            throw new MyException("该节点存在关系，无法删除");
        }
        knowledgeDao.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteLink(Long id)
    {
        knowledgeDao.deleteLinkById(id);
    }

    /**
     * 封装节点信息
     *
     * @param knowledgeList 知识点列表
     * @return 节点列表
     */
    public List<NodeDTO> packageNode(List<Knowledge> knowledgeList)
    {
        List<NodeDTO> nodeList = new ArrayList<>();
        for (Knowledge knowledge : knowledgeList)
        {
            Map<String,String> normal = new HashMap<>();
            normal.put("color",knowledge.getColor());
            CustomDTO customDTO = new CustomDTO(normal);
            NodeDTO nodeDTO = BeanCopyUtils.copyObject(knowledge, NodeDTO.class);
            nodeDTO.setItemStyle(customDTO);
            nodeList.add(nodeDTO);
        }
        return nodeList;
    }

    /**
     * 去除重复项
     *
     * @param knowledgeList 知识点列表
     * @return 知识点列表
     */
    public List<Knowledge> clearDuplicateItem(List<Knowledge> knowledgeList)
    {
        List<Knowledge> newList = new ArrayList<>();
        for (Knowledge knowledge : knowledgeList) {
            if (!newList.contains(knowledge)) {
                newList.add(knowledge);
            }
        }
        return newList;
    }
}
