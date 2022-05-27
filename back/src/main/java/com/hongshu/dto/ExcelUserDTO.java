package com.hongshu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel表格学生信息
 *
 * @author HongShu995
 * @create 2022-03-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelUserDTO
{
    /**
    用户名
     */
    private String username;

    /**
    姓名
     */
    private String name;

    /**
    角色
     */
    private String role;

    /**
    密码
     */
    private String password;
}
