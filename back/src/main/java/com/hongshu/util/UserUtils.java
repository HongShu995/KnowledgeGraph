package com.hongshu.util;

import com.hongshu.dto.UserDetailsDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author HongShu995
 * @create 2022-01-12
 */
@Component
public class UserUtils
{
    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static UserDetailsDTO getLoginUser() {
        return (UserDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
