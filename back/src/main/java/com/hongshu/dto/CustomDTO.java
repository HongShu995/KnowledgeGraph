package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 节点颜色DTO
 *
 * @author HongShu995
 * @create 2022-01-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomDTO
{
    private Map<String,String> normal;
}
