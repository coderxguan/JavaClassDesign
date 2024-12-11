package com.guan.VO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    // 用户名
    private String username;
    // 密码
    private String password;
    // 角色
    private String role;
}
