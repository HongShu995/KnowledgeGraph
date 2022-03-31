package com.hongshu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码VO
 *
 * @author HongShu995
 * @create 2022-01-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVO
{
    /*
    旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /*
    新密码
     */
    @Size(min = 6, message = "新密码不能少于6位")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
