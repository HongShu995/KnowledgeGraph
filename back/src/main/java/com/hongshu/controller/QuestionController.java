package com.hongshu.controller;

import com.hongshu.dto.NodeQuestionDTO;
import com.hongshu.dto.QuestionDTO;
import com.hongshu.service.QuestionService;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.QuestionVO;
import com.hongshu.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 试题模块
 *
 * @author HongShu995
 * @create 2022-03-23
 */
@Api(tags = "试题模块")
@RestController
public class QuestionController
{
    @Autowired
    private QuestionService questionService;

    /**
     * 查询后台选择题
     *
     * @param condition 查询条件
     * @return 选择题列表
     */
    @ApiOperation(value = "查询后台选择题")
    @GetMapping("/admin/question/select")
    Result<List<QuestionDTO>> querySelectQuestion(ConditionVO condition)
    {
        List<QuestionDTO> questionDTOS = questionService.queryBackQuestion(1, condition);
        return Result.ok(questionDTOS);
    }

    /**
     * 查询后台判断题
     *
     * @param condition 查询条件
     * @return 判断题列表
     */
    @ApiOperation(value = "查询后台判断题")
    @GetMapping("/admin/question/judgment")
    Result<List<QuestionDTO>> queryJudgmentQuestion(ConditionVO condition)
    {
        List<QuestionDTO> questionDTOS = questionService.queryBackQuestion(2, condition);
        return Result.ok(questionDTOS);
    }

    /**
     * 查询后台填空题
     *
     * @param condition 查询条件
     * @return 填空题列表
     */
    @ApiOperation(value = "查询后台填空题")
    @GetMapping("/admin/question/completion")
    Result<List<QuestionDTO>> queryCompletionQuestion(ConditionVO condition)
    {
        List<QuestionDTO> questionDTOS = questionService.queryBackQuestion(3, condition);
        return Result.ok(questionDTOS);
    }

    /**
     * 查询后台简答题
     *
     * @param condition 查询条件
     * @return 简答题列表
     */
    @ApiOperation(value = "查询后台简答题")
    @GetMapping("/admin/question/answer")
    Result<List<QuestionDTO>> queryAnswerQuestion(ConditionVO condition)
    {
        List<QuestionDTO> questionDTOS = questionService.queryBackQuestion(4, condition);
        return Result.ok(questionDTOS);
    }

    /**
     * 保存或更新试题
     *
     * @param questionVO 试题VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "保存或更新试题")
    @PostMapping("/admin/question/saveOrUpdate")
    Result<?> saveOrUpdateQuestion(@Valid @RequestBody QuestionVO questionVO)
    {
        questionService.saveOrUpdateQuestion(questionVO);
        return Result.ok();
    }

    /**
     * 通过Id删除试题
     *
     * @param id 试题Id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "通过Id删除试题")
    @DeleteMapping("/admin/question/deleteById/{id}")
    Result<?> deleteById(@PathVariable("id") Long id)
    {
        questionService.deleteById(id);
        return Result.ok();
    }

    /**
     * 通过知识点Id查询相关的试题
     *
     * @param id 知识点Id
     * @return 节点试题列表
     */
    @ApiOperation(value = "通过知识点Id查询相关的试题")
    @GetMapping("/question/query/{knowledgeId}")
    Result<List<NodeQuestionDTO>> queryTest(@PathVariable("knowledgeId")Long id)
    {
        List<NodeQuestionDTO> nodeQuestionDTOS = questionService.queryNodeQuestionByKnowledgeId(id);
        return Result.ok(nodeQuestionDTOS);
    }
}
