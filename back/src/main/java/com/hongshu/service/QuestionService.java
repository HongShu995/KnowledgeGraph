package com.hongshu.service;

import com.hongshu.dto.NodeQuestionDTO;
import com.hongshu.dto.QuestionDTO;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.QuestionVO;

import java.util.List;

/**
 * 试题服务
 *
 * @author HongShu995
 * @create 2022-01-23
 */
public interface QuestionService
{
    /**
     * 通过题型查询所有试题
     *
     * @param conditionVO 查询条件
     * @return 后台试题DTO
     */
    List<QuestionDTO> queryBackQuestion(Integer typeCode, ConditionVO conditionVO);

    /**
     * 通过知识点Id查询相关的试题
     *
     * @param id 知识点Id
     * @return 节点试题列表
     */
    List<NodeQuestionDTO> queryNodeQuestionByKnowledgeId(Long id);

    /**
     * 保存或更新试题
     *
     * @param questionVO 后台试题VO
     */
    void saveOrUpdateQuestion(QuestionVO questionVO);

    /**
     * 根据Id删除试题
     *
     * @param id 试题Id
     */
    void deleteById(Long id);
}
