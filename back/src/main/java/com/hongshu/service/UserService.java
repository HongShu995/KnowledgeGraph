package com.hongshu.service;

import com.hongshu.dto.UserBackDTO;
import com.hongshu.entity.User;
import com.hongshu.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户服务
 *
 * @author HongShu995
 * @create 2022-01-10
 */
public interface UserService
{

    /**
     * 用户登陆时更新信息
     *
     * @param user 用户类
     * @return 更新数量
     */
    void updateByLogin(User user);

    /**
     * 用户登录检验
     *
     * @param username 用户名
     * @return 用户信息
     */
    User loginQuery(String username);

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackDTO> listUserBackDTO(ConditionVO condition);

    /**
     * 修改用户禁用状态
     *
     * @param conditionVO 条件信息
     */
    void updateUserDisable(ConditionVO conditionVO);

    /**
     * 更新用户
     *
     * @param userInfoVO 用户信息
     */
    void updateUserInfo(UserInfoVO userInfoVO);

    /**
     * 添加用户
     *
     * @param userInfoVO 用户信息
     */
    void addUser(UserInfoVO userInfoVO);

    /**
     * 修改密码
     *
     * @param passwordVO 密码信息
     */
    void updatePassword(PasswordVO passwordVO);

    /**
     * 通过Excel表格导入用户信息
     *
     * @param file Excel表格
     */
    void importUsersByExcel(MultipartFile file);


}
