package com.guan.DAO;

import com.guan.VO.User;
import com.guan.mapper.UserMapper;
import com.guan.utils.MapperUtil;

public class UserDAO {

    private final UserMapper userMapper = MapperUtil.getMapper(UserMapper.class);

    // 用户登录
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    // 用户注册
    public boolean register(String username, String password, String role) {
        User user = userMapper.findUserByUsername(username);
        if (user != null) {
            return false;
        } else {
            userMapper.insertUser(username, password, role);
            MapperUtil.commit();
            return true;
        }
    }

    // 修改密码
    public boolean modifyPassword(String username, String oldPassword, String newPassword1) {
        User user = userMapper.login(username, oldPassword);
        if (user == null) {
            return false;
        } else {
            userMapper.updatePassword(username, newPassword1);
            MapperUtil.commit();
            return true;
        }
    }
}
