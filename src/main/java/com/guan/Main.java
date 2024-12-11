package com.guan;

import com.guan.view.LoginView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args){
        // 启动登录界面
        LoginView loginView = new LoginView();
        loginView.showLoginMenu();
    }
}