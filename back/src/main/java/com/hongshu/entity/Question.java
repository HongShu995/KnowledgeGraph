package com.hongshu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

/**
 * 试题
 *
 * @author HongShu995
 * @create 2022-03-23
 */
@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question
{
    /**
     数据库自动生成id
     */
    @Id
    @GeneratedValue
    private Long id;

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
     * 创建时间
     */
    private String createTime;

    /**
     * 最后更新时间
     */
    private String updateTime;

    /**
     * 此题目的发布者
     */
    private String publishUsername;

    /**
     * 主观题的分数每个回答的分数
     */
    private Integer perAnswerScore;

    /**
     * 所属知识点ID
     */
    private Long knowledgeId;
}
