package com.guan.view;

import com.guan.utils.SystemUtil;
import java.util.Scanner;

public class UserView {

    private final Scanner scanner = new Scanner(System.in);
    private final AdminView adminView = new AdminView();

    // 用户菜单
    public void showUserMenu(String username) {
        while (true) {
            System.out.println("====欢迎用户 '"+ username +"' 登录招聘信息管理系统====");
            System.out.println("            1. 浏览招聘信息");
            System.out.println("            2. 搜索招聘信息");
            System.out.println("            3. 导出招聘信息");
            System.out.println("            4. 修改密码");
            System.out.println("            5. 退出系统");
            System.out.println("===========================================");
            System.out.print("请选择：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character
                switch(choice) {
                    case 1 -> viewJobInfo();
                    case 2 -> queryJobInfo();
                    case 3 -> exportJobInfo();
                    case 4 -> modifyPassword(username);
                    case 5 -> {
                        System.out.println("感谢使用系统，退出中...");
                        SystemUtil.pause();
                        SystemUtil.clearConsole();
                        return;
                    }
                    default -> {
                        SystemUtil.clearConsole();
                        System.out.println("无效的选项，请重新输入。");
                    }
                }
            }catch (Exception e) {
                SystemUtil.clearConsole();
                System.out.println("无效的选项，请重新输入。");
                scanner.nextLine();
            }
        }
    }

    // 调用管理员的方法
    // 修改密码
    private void modifyPassword(String username) {
        adminView.modifyPassword(username);
    }

    // 导出招聘信息
    private void exportJobInfo() {
        adminView.exportJobInfo();
    }

    // 查询招聘信息
    private void queryJobInfo() {
        adminView.queryJobInfo();
    }

    // 浏览招聘信息
    private void viewJobInfo() {
        adminView.viewJobInfo();
    }

}
