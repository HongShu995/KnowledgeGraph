package com.hongshu.handler;

import com.alibaba.fastjson.JSON;
import com.hongshu.constant.CommonConstant;
import com.hongshu.dto.UserDTO;
import com.hongshu.entity.User;
import com.hongshu.service.UserService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.util.UserUtils;
import com.hongshu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理
 *
 * @author HongShu995
 * @create 2022-01-12
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler
{
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException
    {
        //返回登录信息
        UserDTO userDTO = BeanCopyUtils.copyObject(UserUtils.getLoginUser(),UserDTO.class);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.ok(userDTO)));
        //更新用户信息
        updateUserInfo();
    }

    /**
     * 更新用户信息
     */
    @Async
    public void updateUserInfo() {
        User user = User.builder()
                .id(UserUtils.getLoginUser().getId())
                .username(UserUtils.getLoginUser().getUsername())
                .lastLoginTime(CommonConstant.getCurrentTime())
                .build();
        userService.updateByLogin(user);
    }
}
