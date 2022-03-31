package com.hongshu.controller;

import com.hongshu.dto.LinkDTO;
import com.hongshu.dto.NodeDTO;
import com.hongshu.dto.OptionDTO;
import com.hongshu.service.KnowledgeService;
import com.hongshu.vo.ConditionVO;
import com.hongshu.vo.LinkVO;
import com.hongshu.vo.NodeVO;
import com.hongshu.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 知识点控制器
 *
 * @author HongShu995
 * @create 2022-01-25
 */
@Api(tags = "知识点模块")
@RestController
public class KnowledgeController
{
    @Autowired
    private KnowledgeService knowledgeService;

    /**
     * 根据条件查询节点
     *
     * @param conditionVO 查询条件
     * @return 节点列表
     */
    @ApiOperation("根据条件查询节点")
    @GetMapping("/knowledge/node")
    public Result<List<NodeDTO>> listNode(ConditionVO conditionVO)
    {
        return Result.ok(knowledgeService.listNode(conditionVO));
    }

    /**
     * 获取所有知识点关系
     *
     * @return 节点选项
     */
    @ApiOperation("获取所有知识点关系")
    @GetMapping("/knowledge/link")
    public Result<List<LinkDTO>> listALlLink()
    {
        return Result.ok(knowledgeService.listAllLink());
    }

    /**
     * 获取所有节点选项
     *
     * @return 节点选项
     */
    @ApiOperation("获取所有节点选项")
    @GetMapping("/knowledge/option")
    public Result<List<OptionDTO>> listNodeOptions()
    {
        return Result.ok(knowledgeService.listOptions());
    }

    /**
     * 更新节点信息
     *
     * @param nodeVO 节点信息
     * @return {@link Result<>}
     */
    @ApiOperation("更新节点信息")
    @PostMapping("/admin/knowledge/update")
    public Result<?> saveAndUpdateNode(@Valid @RequestBody NodeVO nodeVO)
    {
        knowledgeService.saveAndUpdateNode(nodeVO);
        return Result.ok();
    }

    /**
     * 删除节点
     *
     * @param id 节点Id
     * @return {@link Result<>}
     */
    @ApiOperation("删除节点")
    @DeleteMapping("/admin/knowledge/{id}")
    public Result<?> deleteNode(@PathVariable("id")Long id)
    {
        knowledgeService.deleteNode(id);
        return Result.ok();
    }

    /**
     * 更新节点连线信息
     *
     * @param linkVO 连线信息
     * @return {@link Result<>}
     */
    @ApiOperation("更新节点连线信息")
    @PostMapping("/admin/link/update")
    public Result<?> saveAndUpdateLink(@Valid @RequestBody LinkVO linkVO)
    {
        knowledgeService.saveAndUpdateLink(linkVO);
        return Result.ok();
    }

    /**
     * 删除关系
     *
     * @param id 关系Id
     * @return {@link Result<>}
     */
    @ApiOperation("删除关系")
    @DeleteMapping("/admin/link/{id}")
    public Result<?> deleteLink(@PathVariable("id")Long id)
    {
        knowledgeService.deleteLink(id);
        return Result.ok();
    }

//    @GetMapping("/admin/knowledge/test")
//    public Result<?> test()
//    {
//        List<OptionDTO> optionDTOS = knowledgeService.listOptions();
//        optionDTOS.forEach(System.out::println);
//        return Result.ok();
//    }

}
