package com.guan.view;

import com.guan.DAO.UserDAO;
import com.guan.VO.User;
import com.guan.utils.SystemUtil;
import java.util.Scanner;

public class LoginView {

    private final UserDAO userDAO = new UserDAO();
    private final Scanner scanner = new Scanner(System.in);

    // 登录菜单
    public void showLoginMenu(){
        while (true) {
            System.out.println("========欢迎使用招聘信息管理系统=========");
            System.out.println("          请选择您的操作：               ");
            System.out.println("            1. 登录                   ");
            System.out.println("            2. 注册                   ");
            System.out.println("            3. 退出系统               ");
            System.out.println("=====================================");
            System.out.print("请输入选项: ");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine();  // 消耗掉换行符

                switch (choice) {
                    case 1 -> {
                        int ret = login(); // 进入登录流程
                        if(ret == 0){
                            System.out.println("最多只能尝试3次，退出系统！");
                            return;
                        }
                    }
                    case 2 -> register(); // 进入注册流程
                    case 3 -> {
                        System.out.print("您确认退出系统吗（y/n）");
                        String confirm = scanner.nextLine();
                        if ("y".equals(confirm)) {
                            System.out.println("感谢使用系统，退出中...");
                            return; // 退出程序
                        }else{
                            SystemUtil.clearConsole();
                        }
                    }
                    default -> {
                        SystemUtil.clearConsole();
                        System.out.println("无效的选项，请重新输入!");
                    }
                }
            }catch (Exception e) {
                SystemUtil.clearConsole();
                System.out.println("无效的选项，请重新输入!");
                scanner.nextLine();
            }
        }
    }

    private int login() {
        int i = 0;
        SystemUtil.clearConsole();
        while(true) {
            i++;
            if (i <= 3) {
                System.out.println("========欢迎登录招聘信息管理系统=========");
                System.out.print("请输入用户名：");
                String username = scanner.nextLine(); // 获取用户名

                System.out.print("请输入密码：");
                String password = scanner.nextLine(); // 获取密码

                // 调用登录验证
                User user = userDAO.login(username, password);
                if (user != null) {
                    if ("admin".equals(user.getRole())) {
                        SystemUtil.clearConsole();
                        AdminView adminView = new AdminView();
                        adminView.showAdminMenu(user.getUsername());
                        return 1;
                    } else {
                        SystemUtil.clearConsole();
                        UserView userView = new UserView();
                        userView.showUserMenu(user.getUsername());
                        return 1;
                    }
                } else {
                    SystemUtil.clearConsole();
                    System.out.println("登录失败，用户名或密码错误！请重试!!");
                }
            }else{
                return 0;
            }
        }
    }

    // 注册
    private void register() {
        SystemUtil.clearConsole();
        while (true) {
            System.out.println("========欢迎注册招聘信息管理系统=========");
            System.out.println("1. 普通用户");
            System.out.println("2. 管理员");
            System.out.print("请选择你要注册的用户的类型：");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character
            System.out.print("请输入用户名：");
            String username = scanner.nextLine(); // 获取用户名
            System.out.print("请输入密码：");
            String password1 = scanner.nextLine(); // 获取密码
            System.out.print("请再次输入密码：");
            String password2 = scanner.nextLine(); // 获取重复密码
            String role;
            if(choice == 1)
                role = "user";
            else
                role = "admin";

            if (password1.equals(password2)) {
                boolean result = userDAO.register(username, password1, role);
                if(!result){
                    SystemUtil.clearConsole();
                    System.out.println("用户名已存在，请重新注册！");
                }else{
                    System.out.println("注册成功！");
                    SystemUtil.pause();
                    SystemUtil.clearConsole();
                    return;
                }
            } else {
                SystemUtil.clearConsole();
                System.out.println("两次输入的密码不一致，请重新注册！");
            }
        }
    }
}
