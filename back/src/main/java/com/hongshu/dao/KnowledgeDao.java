package com.hongshu.dao;

import com.hongshu.entity.Knowledge;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 知识点DAO
 *
 * @author HongShu995
 * @create 2022-01-10
 */
@Repository
public interface KnowledgeDao extends Neo4jRepository<Knowledge,Long>
{
    /**
     * 获取所有知识点
     *
     * @return 知识点列表
     */
    @Query("MATCH (n:Knowledge) WHERE id(n)<>179 RETURN n")
    List<Knowledge> listKnowledge();

    /**
     * 模糊查询节点 (5级深度)
     *
     * @param keywords 搜索内容
     * @return 知识点列表
     */
    @Query("MATCH (n:Knowledge)-[r*0..1]-(result) WHERE id(n)<>179 AND n.name=~ ('.*'+{keywords}+'.*') RETURN result")
    List<Knowledge> listKnowledgeOne(@Param("keywords")String keywords);
    @Query("MATCH (n:Knowledge)-[r*0..2]-(result) WHERE id(n)<>179 AND n.name=~ ('.*'+{keywords}+'.*') RETURN result")
    List<Knowledge> listKnowledgeTwo(@Param("keywords")String keywords);
    @Query("MATCH (n:Knowledge)-[r*0..3]-(result) WHERE id(n)<>179 AND n.name=~ ('.*'+{keywords}+'.*') RETURN result")
    List<Knowledge> listKnowledgeThree(@Param("keywords")String keywords);
    @Query("MATCH (n:Knowledge)-[r*0..4]-(result) WHERE id(n)<>179 AND n.name=~ ('.*'+{keywords}+'.*') RETURN result")
    List<Knowledge> listKnowledgeFour(@Param("keywords")String keywords);
    @Query("MATCH (n:Knowledge)-[r*0..5]-(result) WHERE id(n)<>179 AND n.name=~ ('.*'+{keywords}+'.*') RETURN result")
    List<Knowledge> listKnowledgeFive(@Param("keywords")String keywords);


    /**
     * 获取所有节点Id
     *
     * @return Id列表
     */
    @Query("MATCH (n:Knowledge) WHERE id(n)<>179 RETURN id(n)")
    List<Long> listAllNodeId();

    /**
     * 根据节点Id返回拥有的关系Id
     *
     * @return 关系Id列表
     */
    @Query("MATCH (n:Knowledge)-[r:Relationship]->() WHERE id(n)={id} RETURN id(r)")
    List<Long> listLinkIdByNodeId(@Param("id")Long id);

    /**
     * 返回关系信息
     *
     * @param id 关系Id
     * @return 关系信息
     */
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} RETURN id(r)")
    List<Long> getLinkId(@Param("id")Long id);
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} RETURN r.name")
    String getLinkName(@Param("id")Long id);
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} RETURN r.source")
    String getLinkSource(@Param("id")Long id);
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} RETURN r.target")
    String getLinkTarget(@Param("id")Long id);
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} RETURN r.createTime")
    String getLinkCreateTime(@Param("id")Long id);

    /**
     * 获取节点向外连线数量
     *
     * @param id 知识点Id
     * @return 连线数量
     */
    @Query("MATCH (n:Knowledge)-[r:Relationship]->() WHERE id(n)={id} RETURN COUNT(r)")
    Integer countOUTCOMRelationship(@Param("id")Long id);

    /**
     * 获取节点向内连线数量
     *
     * @param id 知识点Id
     * @return 连线数量
     */
    @Query("MATCH ()-[r:Relationship]->(n:Knowledge) WHERE id(n)={id} RETURN COUNT(r)")
    Integer countINCOMRelationship(@Param("id")Long id);

    /**
     * 获取节点存在关系数
     *
     * @param source 起始节点Id
     * @param target 结束节点Id
     * @return 关系数
     */
    @Query("MATCH ()-[r:Relationship{ source:{source}, target:{target} }]-() RETURN COUNT(r)")
    Integer countExistRelationship(@Param("source")String source, @Param("target")String target);

    /**
     * 更新知识点信息
     *
     * @param id 知识点Id
     * @param name 名称
     * @param des 内容
     * @param url 链接
     * @param color 颜色
     * @param symbolSize 大小
     * @param level 等级
     */
    @Query("MATCH (n:Knowledge) WHERE id(n)={id} SET n.name={name}, n.des={des}, n.url={url}, n.color={color}, n.symbolSize={symbolSize}, n.level={level}")
    void updateNode(@Param("id")Long id, @Param("name")String name,
                    @Param("des")String des, @Param("url")String url,
                    @Param("color")String color, @Param("symbolSize")Integer symbolSize,
                    @Param("level")Integer level);

    /**
     * 更新连线信息
     *
     * @param id 连线Id
     * @param name 连线名称
     * @param updateTime 更新时间
     */
    @Query("MATCH ()-[r:Relationship]->() WHERE id(r)={id} SET r.name={name}, r.updateTime={updateTime}")
    void updateLink(@Param("id")Long id, @Param("name")String name,
                    @Param("updateTime")String updateTime);

    /**
     * 添加知识点
     *
     * @param name 名称
     * @param des 内容
     * @param url 链接
     * @param color 颜色
     * @param symbolSize 大小
     * @param level 等级
     * @param createTime 创建时间
     * @param updateTime 更新时间
     */
    @Query("CREATE (n:Knowledge{ name:{name}, des:{des}, url:{url}, color:{color}, symbolSize:{symbolSize}, level:{level}, createTime:{createTime}, updateTime:{updateTime} })")
    void addNode(@Param("name")String name, @Param("des")String des,
                 @Param("url")String url, @Param("color")String color,
                 @Param("symbolSize")Integer symbolSize, @Param("level")Integer level,
                 @Param("createTime")String createTime, @Param("updateTime")String updateTime);

    /**
     * 创建知识点关系
     *
     * @param star 起始节点Id
     * @param end 结束节点Id
     * @param name 连线名称
     * @param source 起始节点Id
     * @param target 结束节点Id
     * @param createTime 创建时间
     * @param updateTime 更新时间
     */
    @Query("MATCH (n:Knowledge),(m:Knowledge) WHERE id(n)={starId} AND id(m)={endId} CREATE (n)-[r:Relationship{ name:{name}, source:{source}, target:{target}, createTime:{createTime}, updateTime:{updateTime} }]->(m)")
    void addLink(@Param("starId")Long star, @Param("endId")Long end,
                 @Param("name")String name, @Param("source")String source,
                 @Param("target")String target, @Param("createTime")String createTime,
                 @Param("updateTime")String updateTime);

    /**
     * 删除节点
     *
     * @param id 节点Id
     */
    @Query("MATCH (n:Knowledge) WHERE id(n)={id} DELETE n")
    void deleteById(@Param("id")Long id);

    /**
     * 删除关系
     *
     * @param id 关系Id
     */
    @Query("MATCH ()-[r:Relationship]-() WHERE id(r)={id} DELETE r")
    void deleteLinkById(@Param("id")Long id);

    /**
     * 更新修改时间
     *
     * @param id 知识点Id
     * @param updateTime 更新时间
     */
    @Query("MATCH (n:Knowledge) WHERE id(n)={id} SET n.updateTime={updateTime}")
    void update(@Param("id")Long id, @Param("updateTime")String updateTime);
}
