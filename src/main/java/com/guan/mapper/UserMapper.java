package com.guan.mapper;

import com.guan.VO.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public interface UserMapper {

    // 用户登录
    User login(@Param("username")String username,
               @Param("password")String password);

    // 查询所有用户
    List<User> selectAllUsers();

    // 根据用户名查询用户
    User findUserByUsername(@Param("username") String username);

    // 注册用户
    void insertUser(@Param("username") String username,
                    @Param("password") String password,
                    @Param("role") String role);

    // 修改密码
    void updatePassword(@Param("username") String username,
                        @Param("password") String newPassword);
}
