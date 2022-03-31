package com.hongshu.handler;

import com.alibaba.fastjson.JSON;
import com.hongshu.vo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  用户权限处理
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.fail("权限不足")));
    }
}
