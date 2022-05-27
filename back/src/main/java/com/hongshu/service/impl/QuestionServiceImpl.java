package com.hongshu.service.impl;

import com.hongshu.constant.CommonConstant;
import com.hongshu.dao.QuestionDao;
import com.hongshu.dto.NodeQuestionDTO;
import com.hongshu.dto.QuestionDTO;
import com.hongshu.entity.Question;
import com.hongshu.service.QuestionService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.util.UserUtils;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hongshu.constant.CommonConstant.*;

/**
 * 试题服务实例
 *
 * @author HongShu995
 * @create 2022-01-23
 */
@Service
public class QuestionServiceImpl implements QuestionService
{
    @Autowired
    private QuestionDao questionDao;


    @Override
    public List<QuestionDTO> queryBackQuestion(Integer typeCode, ConditionVO conditionVO)
    {
        List<Question> questionList;
        if (conditionVO.getKeywords().equals("") || conditionVO.getKeywords() == null)
        {
            questionList = questionDao.listQuestionListByTypeCode(typeCode, conditionVO.getSkip(), conditionVO.getLimit());
        } else
        {
            questionList = questionDao.queryQuestionLikeKeyword(typeCode, conditionVO.getSkip(), conditionVO.getLimit(), conditionVO.getKeywords());
        }
        return BeanCopyUtils.copyList(questionList, QuestionDTO.class);
    }

    @Override
    public void saveOrUpdateQuestion(QuestionVO questionVO)
    {
        if (questionVO.getId() == null)
        {
            questionDao.addQuestion(
                    questionVO.getQuestionName(),
                    questionVO.getTypeCode(),
                    questionVO.getOptionList(),
                    questionVO.getCorrectAnswerList(),
                    questionVO.getKeywordList(),
                    questionVO.getAnalysis(),
                    questionVO.getJudgmentAnswer(),
                    CommonConstant.getCurrentTime(),
                    NULL,
                    UserUtils.getLoginUser().getUsername(),
                    questionVO.getPerAnswerScore(),
                    questionVO.getKnowledgeId(),
                    CommonConstant.getCurrentTime());
        } else
        {
            questionDao.updateQuestion(
                    questionVO.getId(),
                    questionVO.getQuestionName(),
                    questionVO.getTypeCode(),
                    questionVO.getOptionList(),
                    questionVO.getCorrectAnswerList(),
                    questionVO.getKeywordList(),
                    questionVO.getAnalysis(),
                    questionVO.getJudgmentAnswer(),
                    questionVO.getPerAnswerScore(),
                    questionVO.getKnowledgeId());
            questionDao.clearRelationshipWithKnowledge(questionVO.getId());
            questionDao.updateKnowledgeConnect(questionVO.getId(), questionVO.getKnowledgeId(), CommonConstant.getCurrentTime());
            questionDao.update(questionVO.getId(), CommonConstant.getCurrentTime());
        }
    }

    @Override
    public void deleteById(Long id)
    {
        questionDao.deleteById(id);
    }

    @Override
    public List<NodeQuestionDTO> queryNodeQuestionByKnowledgeId(Long id)
    {
        List<Question> questionList = questionDao.queryQuestionByKnowledgeId(id);
        return BeanCopyUtils.copyList(questionList, NodeQuestionDTO.class);
    }
}
