package com.hongshu.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 登录用户信息DTO
 *
 * @author HongShu995
 * @create 2022-01-12
 */
@Data
@Builder
public class UserDetailsDTO implements UserDetails
{

    /**
    用户id
     */
    private Long id;

    /**
    用户名
     */
    private String username;

    /**
    密码
     */
    private String password;

    /**
    用户角色
     */
    private List<String> roleList;

    /**
    用户头像
     */
    private String avatar;

    /**
    用户昵称
     */
    private String nickname;

    /**
    是否禁用
     */
    private Boolean isDisable;

    /**
    最近登录时间
     */
    private String lastLoginTime;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !this.isDisable;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
