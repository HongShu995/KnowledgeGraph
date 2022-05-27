package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 试题VO
 *
 * @author HongShu995
 * @create 2022-04-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionVO
{
    /**
     * 自动生成的id
     */
    private Long id;

    /**
     * 试题类型的代号
     */
    @NotNull(message = "试题类型不能为空")
    private Integer typeCode;

    /**
     * 试题的题目
     */
    @NotBlank(message = "试题题目不能为空")
    private String questionName;

    /**
     * 选择题选项
     */
    private List<String> optionList;

    /**
     * 选择、填空、简答答案集合
     */
    private List<String> correctAnswerList;

    /**
     * 判断题答案
     */
    private Boolean judgmentAnswer;

    /**
     * 如果是答题，那么这个就是关键词集合
     */
    private List<String> keywordList;

    /**
     * 试题的解析，可以为空，null
     */
    private String analysis;

    /**
     * 此试题对应的知识点的id
     */
    private Long knowledgeId;

    /**
     * 试题分值
     */
    private Integer perAnswerScore;
}
