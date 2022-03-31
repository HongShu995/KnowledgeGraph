package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户禁用状态VO
 *
 * @author HongShu995
 * @create 2022-01-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDisableVO
{
    /*
    id
     */
    private Long id;

    /*
    封禁状态
     */
    private Boolean isDisable;
}
