package com.hongshu.controller;

import com.hongshu.dto.UserBackDTO;
import com.hongshu.exception.MyException;
import com.hongshu.service.UserService;
import com.hongshu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import static com.hongshu.constant.CommonConstant.*;

/**
 * 用户控制器
 *
 * @author HongShu995
 * @create 2022-01-18
 */
@Api(tags = "用户模块")
@RestController
public class UserController
{
    @Autowired
    private UserService userService;



    /**
     * 查询后台用户列表
     *
     * @param condition 条件信息VO
     * @return {@link Result<UserBackDTO>} 用户列表
     */
    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDTO>> listUsers(ConditionVO condition)
    {
        return Result.ok(userService.listUserBackDTO(condition));
    }

    /**
     * 添加用户
     *
     * @param userInfoVO 用户信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/admin/users/add")
    public Result<?> addUser(@Valid @RequestBody UserInfoVO userInfoVO)
    {
        userService.addUser(userInfoVO);
        return Result.ok();
    }

    /**
     * 修改用户禁用状态
     *
     * @param conditionVO 条件信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    public Result<?> updateUserDisable(@Valid @RequestBody ConditionVO conditionVO)
    {
        userService.updateUserDisable(conditionVO);
        return Result.ok();
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVO 用户信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO)
    {
        userService.updateUserInfo(userInfoVO);
        return Result.ok();
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/admin/users/{id}")
    public Result<?> deleteUser(@PathVariable("id") Long id)
    {
        userService.deleteUser(id);
        return Result.ok();
    }

    /**
     * 修改密码
     *
     * @param passwordVO 密码信息VO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/updatePassword")
    public Result updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO)
    {
        userService.updatePassword(passwordVO);
        return Result.ok();
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link Result<String>} 头像地址
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public Result<String> updateUserAvatar(MultipartFile file)
    {
        String path = userService.updateUserAvatar(file);
        return Result.ok(path);
    }

    /**
     * 通过Excel表格导入学生数据
     *
     * @param file Excel表格文件
     * @return {@link Result<>}
     */
    @ApiOperation(value = "通过Excel导入学生数据")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/users/excel")
    public Result<?> importExcel(MultipartFile file)
    {
        userService.importUsersByExcel(file);
        return Result.ok();
    }

}
