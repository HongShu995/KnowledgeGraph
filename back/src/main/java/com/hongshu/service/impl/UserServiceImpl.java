package com.hongshu.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hongshu.constant.CommonConstant;
import com.hongshu.dao.RoleDao;
import com.hongshu.dao.UserDao;
import com.hongshu.dto.ExcelUserDTO;
import com.hongshu.dto.UserBackDTO;
import com.hongshu.dto.UserRoleDTO;
import com.hongshu.entity.Role;
import com.hongshu.entity.User;
import com.hongshu.exception.MyException;
import com.hongshu.service.UserService;
import com.hongshu.util.BeanCopyUtils;
import com.hongshu.util.LocalUploadUtils;
import com.hongshu.util.UserUtils;
import com.hongshu.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.hongshu.constant.CommonConstant.*;

/**
 * @author HongShu995
 * @create 2022-01-10
 */
@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private LocalUploadUtils localUploadUtils;

    @Override
    public User loginQuery(String username)
    {
        return userDao.queryByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByLogin(User user)
    {
        userDao.updateByUsername(user.getId(), user.getLastLoginTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importUsersByExcel(MultipartFile file)
    {
        String filename = file.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf("."));
        InputStream is = null;
        //??????????????????
        if (FILE_XLS.equalsIgnoreCase(fileType) || FILE_XLSX.equalsIgnoreCase(fileType))
        {
            String type = null;
            try
            {
                type = FileTypeUtil.getType(file.getInputStream());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            // ????????????????????????????????????
            if (FILE_XLSX.contains(type)||FILE_XLS.contains(type))
            {
                try
                {
                    is = file.getInputStream();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else
            {
                throw new MyException("???????????????????????????");
            }
            // ??????Excel????????????
            EasyExcel.read(is, ExcelUserDTO.class, new AnalysisEventListener<ExcelUserDTO>()
            {
                // ?????????????????????????????????
                @Override
                public void invoke(ExcelUserDTO excelUserDTO, AnalysisContext analysisContext)
                {
                    if (excelUserDTO.getUsername() == null || excelUserDTO.getName() == null || excelUserDTO.getPassword()==null)
                    {
                        int temp = analysisContext.getCurrentRowNum()+1;
                        throw new MyException("????????????????????????????????????,?????????????????????????????????"+temp+"???");
                    }
                    // ??????????????????
                    addUser(packageUserInfo(excelUserDTO));
                }

                @Override
                public void onException(Exception exception, AnalysisContext context) throws Exception
                {
                    throw exception;
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext)
                {
                }
            }).sheet().doRead();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file)
    {
        // ????????????
        String avatar = localUploadUtils.uploadFile(file);
        userDao.updateAvatar(UserUtils.getLoginUser().getId(), avatar);
        userDao.update(UserUtils.getLoginUser().getId(), CommonConstant.getCurrentTime());
        // ??????????????????
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserInfoVO userInfoVO)
    {
        if (userDao.queryByUsername(userInfoVO.getUsername()) != null)
        {
            throw new MyException("?????????????????????????????????");
        }
        // ??????????????????
        User user = userDao.addUser(
                userInfoVO.getUsername(),
                BCrypt.hashpw(userInfoVO.getPassword(), BCrypt.gensalt()),
                userInfoVO.getNickname(),
                CommonConstant.avatar,
                CommonConstant.BOOLEAN_FALSE,
                CommonConstant.getCurrentTime(),
                CommonConstant.NULL,
                CommonConstant.NULL);
        // ??????????????????
        if (userInfoVO.getRoleList().size() > 0)
        {
            for (Long id : userInfoVO.getRoleList())
            {
                roleDao.setUserRole(user.getId(), id);
            }
        }
    }

    @Override
    public void deleteUser(Long id)
    {
        if(id == UserUtils.getLoginUser().getId())
        {
            throw new MyException("?????????????????????????????????");
        }
        userDao.deleteById(id);
    }

    @Override
    public PageResult<UserBackDTO> listUserBackDTO(ConditionVO conditionVO)
    {
        Integer count = userDao.countUser();
        if (count == 0)
        {
            return new PageResult<>();
        }
        // ????????????????????????
        List<User> users;
        if(!Objects.equals(conditionVO.getKeywords(), ""))
        {
            users = userDao.listUsersWithUsername(conditionVO.getKeywords());
            if (users.size() <= 0)
            {
                users = userDao.listUsersWithNickname(conditionVO.getKeywords());
            }
        } else
        {
            users = userDao.listUsers(conditionVO.getSkip(), conditionVO.getLimit());
        }
        List<UserBackDTO> userBackDTOS = BeanCopyUtils.copyList(users, UserBackDTO.class);
        for (UserBackDTO userBackDTO : userBackDTOS)
        {
            List<Role> roles = roleDao.listUserRolesById(userBackDTO.getId());
            List<UserRoleDTO> userRoles = BeanCopyUtils.copyList(roles,UserRoleDTO.class);
            userBackDTO.setRoleList(userRoles);
        }
        return new PageResult<>(userBackDTOS,count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(ConditionVO conditionVO)
    {
        userDao.setUserDisable(conditionVO.getId(), conditionVO.getWhether());
        userDao.update(conditionVO.getId(), CommonConstant.getCurrentTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO)
    {
        userDao.setUserNickname(UserUtils.getLoginUser().getId(), userInfoVO.getNickname());
        userDao.update(UserUtils.getLoginUser().getId(), CommonConstant.getCurrentTime());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(PasswordVO passwordVO)
    {
        // ???????????????????????????
        User user = userDao.queryUserById(UserUtils.getLoginUser().getId());
        // ????????????????????????????????????????????????
        if (user != null && BCrypt.checkpw(passwordVO.getOldPassword(), user.getPassword()))
        {
            userDao.setUserPassword(UserUtils.getLoginUser().getId(), BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()));
        } else {
            throw new MyException("??????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param excelUserDTO Excel????????????
     * @return ????????????
     */
    public UserInfoVO packageUserInfo(ExcelUserDTO excelUserDTO)
    {
        Long id = roleDao.queryRoleIdByRoleLabel(excelUserDTO.getRole());
        List<Long> roleIdList =  new ArrayList<Long>();
        roleIdList.add(id);
        return UserInfoVO.builder()
                .username(excelUserDTO.getUsername())
                .nickname(excelUserDTO.getName())
                .password(excelUserDTO.getPassword())
                .roleList(roleIdList)
                .build();
    }
}
