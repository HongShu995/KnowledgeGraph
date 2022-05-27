package com.hongshu.service.impl;

import com.hongshu.constant.CommonConstant;
import com.hongshu.dao.RoleDao;
import com.hongshu.dto.UserDetailsDTO;
import com.hongshu.entity.User;
import com.hongshu.exception.MyException;
import com.hongshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用户详细信息服务
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Resource
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if (username == null || username.length() <= 0) {
            throw new MyException("用户名不能为空！");
        }
        //获取用户名和密码
        User loginUser = userService.loginQuery(username);
        if (loginUser == null)
        {
            throw new UsernameNotFoundException("没有该用户");
        }
        return convertUserDetail(loginUser,request);
    }

    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserDetailsDTO convertUserDetail(User user, HttpServletRequest request)
    {
        // 查询账号角色
        List<String> roleList = roleDao.listUserRoleName(user.getId());
        // 封装权限集合
        return UserDetailsDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .roleList(roleList)
                .isDisable(user.getIsDisable())
                .lastLoginTime(CommonConstant.getCurrentTime())
                .build();
    }
}
