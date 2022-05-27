package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点选项DTO
 *
 * @author HongShu995
 * @create 2022-02-09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO
{
    /**
    Id
     */
    private Long id;

    /**
    节点名
     */
    private String name;
}
