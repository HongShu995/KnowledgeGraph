package com.hongshu.dao;

import com.hongshu.entity.Question;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 试题DAO
 *
 * @author HongShu995
 * @create 2022-04-23
 */
@Repository
public interface QuestionDao extends Neo4jRepository<Question,Long>
{
    @Query("MATCH (n:Question{typeCode:{typeCode}}) RETURN n SKIP {skip} LIMIT {limit}")
    List<Question> listQuestionListByTypeCode(@Param("typeCode")Integer typeCode,@Param("skip")Integer skip, @Param("limit")Integer limit);

    @Query("MATCH (n:Question{typeCode:{typeCode}}) WHERE n.questionName=~ ('.*'+{keywords}+'.*') RETURN n SKIP {skip} LIMIT {limit}")
    List<Question> queryQuestionLikeKeyword(@Param("typeCode")Integer typeCode,@Param("skip")Integer skip, @Param("limit")Integer limit, @Param("keyword")String keyword);

    @Query("MATCH (m:Knowledge) WHERE id(m)={knowledgeId} CREATE (m)-[r:KnowledgeTest{ name:'知识点测试', createTime:{connectTime}}]->(n:Question{ questionName:{questionName}, typeCode:{typeCode}, optionList:{optionList}, correctAnswerList:{correctAnswerList}, keywordList:{keywordList}, analysis:{analysis}, judgmentAnswer:{judgmentAnswer}, createTime:{createTime}, updateTime:{updateTime}, publishUsername:{publishUsername}, perAnswerScore:{perAnswerScore}, knowledgeId:{knowledgeId} })")
    void addQuestion(@Param("questionName")String questionName,
                     @Param("typeCode")Integer typeCode,
                     @Param("optionList")List<String> optionList,
                     @Param("correctAnswerList")List<String> correctAnswerList,
                     @Param("keywordList")List<String> keywordList,
                     @Param("analysis")String analysis,
                     @Param("judgmentAnswer")Boolean judgmentAnswer,
                     @Param("createTime")String createTime,
                     @Param("updateTime")String updateTime,
                     @Param("publishUsername")String publishUsername,
                     @Param("perAnswerScore")Integer perAnswerScore,
                     @Param("knowledgeId")Long knowledgeId,
                     @Param("connectTime")String connectTime);

    @Query("MATCH (n:Question) WHERE id(n)={id} SET n.questionName={questionName}, n.typeCode={typeCode}, n.optionList={optionList}, n.correctAnswerList={correctAnswerList}, n.keywordList={keywordList}, n.analysis={analysis}, n.judgmentAnswer={judgmentAnswer}, n.knowledgeId={knowledgeId}")
    void updateQuestion(@Param("id")Long id,
                        @Param("questionName")String questionName,
                        @Param("typeCode")Integer typeCode,
                        @Param("optionList")List<String> optionList,
                        @Param("correctAnswerList")List<String> correctAnswerList,
                        @Param("keywordList")List<String> keywordList,
                        @Param("analysis")String analysis,
                        @Param("judgmentAnswer")Boolean judgmentAnswer,
                        @Param("perAnswerScore")Integer perAnswerScore,
                        @Param("knowledgeId")Long knowledgeId);

    @Query("MATCH (n:Question),(m:Knowledge) WHERE id(n)={id} AND id(m)={knowledgeId} CREATE (m)-[r:KnowledgeTest{ name:'知识点测试'}]->(n) SET r.updateTime={updateTime}")
    void updateKnowledgeConnect(@Param("id")Long id, @Param("knowledgeId")Long knowledgeId, @Param("updateTime")String updateTime);

    @Query("MATCH (n:Knowledge)-[r:KnowledgeTest]->(m:Question) WHERE id(m)={id} DELETE r")
    void clearRelationshipWithKnowledge(@Param("id")Long id);

    @Query("MATCH (n:Knowledge)-[r:KnowledgeTest]->(m:Question) WHERE id(n)={id} RETURN m")
    List<Question> queryQuestionByKnowledgeId(@Param("id")Long id);

    /**
     * 更新修改时间
     *
     * @param id 知识点Id
     * @param updateTime 更新时间
     */
    @Query("MATCH (n:Question) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);
}
