package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点试题DTO
 *
 * @author HongShu995
 * @create 2022-04-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeQuestionDTO
{
    /**
     * 试题题目
     */
    private String questionName;

    /**
     * 试题类型的代号
     */
    private Integer typeCode;

    /**
     * 选择题选项集合
     */
    private List<String> optionList;

    /**
     * 正确答案集合
     */
    private List<String> correctAnswerList;

    /**
     * 简答题关键词集合
     */
    private List<String> keywordList;

    /**
     * 试题的解析
     */
    private String analysis;

    /**
     * 判断题答案
     */
    private Boolean judgmentAnswer;

    /**
     * 主观题的分数每个回答的分数
     */
    private Integer perAnswerScore;
}
