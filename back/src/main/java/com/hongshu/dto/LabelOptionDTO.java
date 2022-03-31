package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签选项
 *
 * @author HongShu995
 * @create 2022-01-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelOptionDTO
{

    /*
    选项id
     */
    private Long id;

    /*
    选项名
     */
    private String label;

    /*
    子选项
     */
    private List<LabelOptionDTO> children;
}
