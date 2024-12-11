package com.guan.view;

import com.guan.DAO.JobInfoDAO;
import com.guan.DAO.UserDAO;
import com.guan.VO.JobInfo;
import com.guan.utils.SystemUtil;
import java.util.List;
import java.util.Scanner;

public class AdminView {

    private final JobInfoDAO jobInfoDAO = new JobInfoDAO();
    private final UserDAO userDAO = new UserDAO();
    private final Scanner scanner = new Scanner(System.in);

    // 管理员菜单
    public void showAdminMenu(String username) {
        while (true) {
            System.out.println("====欢迎管理员 '"+ username +"' 登录招聘信息管理系统====");
            System.out.println("            1. 爬取最新招聘信息");
            System.out.println("            2. 浏览招聘信息");
            System.out.println("            3. 搜索招聘信息");
            System.out.println("            4. 修改招聘信息");
            System.out.println("            5. 删除招聘信息");
            System.out.println("            6. 导出招聘信息");
            System.out.println("            7. 修改密码");
            System.out.println("            8. 退出系统");
            System.out.println("===========================================");
            System.out.print("请选择：");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // 消耗掉换行符
                switch(choice) {
                    case 1 -> importJobInfo();
                    case 2 -> viewJobInfo();
                    case 3 -> queryJobInfo();
                    case 4 -> modifyJobInfo();
                    case 5 -> deleteJobInfo();
                    case 6 -> exportJobInfo();

                    case 7 -> modifyPassword(username);

                    case 8 -> {
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

    // 修改密码
    public void modifyPassword(String username) {
        SystemUtil.clearConsole();
        System.out.println("====修改密码====");
        System.out.print("请输入当前账号密码: ");
        String oldPassword = scanner.nextLine();
        System.out.print("请输入新密码: ");
        String newPassword1 = scanner.nextLine();
        System.out.print("请再次输入新密码: ");
        String newPassword2 = scanner.nextLine();
        if (newPassword1.equals(newPassword2)) {
            boolean result = userDAO.modifyPassword(username, oldPassword, newPassword1);
            if (result) {
                System.out.println("密码修改成功！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            } else {
                SystemUtil.clearConsole();
                System.out.println("旧密码输入错误, 修改失败！");
            }
        } else {
            SystemUtil.clearConsole();
            System.out.println("两次输入的新密码不一致，请重新输入！");
        }
    }


    // 导出招聘信息
    public void exportJobInfo() {
        SystemUtil.clearConsole();
        System.out.println("====导出招聘信息====");
        System.out.println("1.导出为文本文件");
        System.out.println("2.导出为Excel文件");
        System.out.print("请选择导出格式：");
        try{
            int choice = scanner.nextInt();
            scanner.nextLine();  // 消耗掉换行符
            if (choice == 1) {
                System.out.print("请输入导出文件名：");
                String fileName = scanner.nextLine();
                jobInfoDAO.exportJobInfoToTxt(fileName);
                System.out.println("导出成功！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            } else if (choice == 2) {
                System.out.print("请输入导出文件名：");
                String fileName = scanner.nextLine();
                jobInfoDAO.exportJobInfoToExcel(fileName);
                System.out.println("导出成功！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            } else {
                System.out.println("无效的选项！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            }
        } catch (Exception e) {
            System.out.println("无效的选项！");
            SystemUtil.pause();
            SystemUtil.clearConsole();
        }
    }

    // 删除招聘信息
    private void deleteJobInfo() {
        SystemUtil.clearConsole();
        System.out.println("====删除招聘信息====");
        System.out.print("请输入要删除的招聘信息ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // 消耗掉换行符
        JobInfo jobInfo = jobInfoDAO.getJobInfoById(id);
        if (jobInfo != null) {
            System.out.println("当前招聘信息如下：");
            SystemUtil.printJobInfo(jobInfo, id);
            System.out.print("确认删除该招聘信息？(y/n): ");
            String choice = scanner.nextLine();
            if ("y".equalsIgnoreCase(choice)) {
                jobInfoDAO.deleteJobInfo(id);
                System.out.println("删除成功！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            } else {
                System.out.println("取消删除！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
            }
        } else {
            System.out.println("未找到该招聘信息！");
            SystemUtil.pause();
            SystemUtil.clearConsole();
        }
    }

    // 修改招聘信息
    private void modifyJobInfo() {
        SystemUtil.clearConsole();
        System.out.println("====修改招聘信息====");
        System.out.print("请输入要修改的招聘信息ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // 消耗掉换行符
        JobInfo jobInfo = jobInfoDAO.getJobInfoById(id);
        if (jobInfo != null) {
            System.out.println("当前招聘信息如下：");
            SystemUtil.printJobInfo(jobInfo, id);
            System.out.println("请输入新的招聘信息(不修改请直接回车)");
            System.out.print("请输入新的职位名称: ");
            String jobTitle = scanner.nextLine();
            System.out.print("请输入新的公司名称: ");
            String companyName = scanner.nextLine();
            System.out.print("请输入新的薪资范围: ");
            String salaryRange = scanner.nextLine();
            System.out.print("请输入新的工作地点: ");
            String workLocation = scanner.nextLine();
            System.out.print("请输入新的工作经验: ");
            String experience = scanner.nextLine();
            System.out.print("请输入新的学历要求: ");
            String education = scanner.nextLine();
            System.out.print("请输入新的招聘人数: ");
            String recruitNum = scanner.nextLine();
            System.out.print("请输入新的发布时间: ");
            String publishDate = scanner.nextLine();
            System.out.print("请输入新的公司性质: ");
            String companyType = scanner.nextLine();

            JobInfo newJobInfo = new JobInfo(
                    id,
                    jobTitle.isEmpty() ? null : jobTitle,             // 空值转为 null
                    companyName.isEmpty() ? null : companyName,
                    salaryRange.isEmpty() ? null : salaryRange,
                    workLocation.isEmpty() ? null : workLocation,
                    experience.isEmpty() ? null : experience,
                    education.isEmpty() ? null : education,
                    recruitNum.isEmpty() ? null : Integer.parseInt(recruitNum),    // 数值字段同样处理
                    publishDate.isEmpty() ? null : publishDate,
                    companyType.isEmpty() ? null : companyType
            );

            if(jobTitle.isEmpty() && companyName.isEmpty() && salaryRange.isEmpty()
                    && workLocation.isEmpty() && experience.isEmpty() && education.isEmpty()
                    && recruitNum.isEmpty() && publishDate.isEmpty() && companyType.isEmpty()){
                System.out.println("未修改任何信息！");
                SystemUtil.pause();
                SystemUtil.clearConsole();
                return;
            }
            jobInfoDAO.modifyJobInfo(newJobInfo);
            System.out.println("修改成功！");
            SystemUtil.pause();
            SystemUtil.clearConsole();
        }else{
            System.out.println("未找到该招聘信息！");
            SystemUtil.pause();
            SystemUtil.clearConsole();
        }
    }

    // 查询招聘信息
    public void queryJobInfo() {
        SystemUtil.clearConsole();
        System.out.println("====查询招聘信息(不填请直接回车)====");
        System.out.print("请输入职位名称(可选): ");
        String jobTitle = scanner.nextLine();
        System.out.print("请输入工作地点(可选): ");
        String workLocation = scanner.nextLine();
        System.out.print("请输入学历要求(可选): ");
        String education = scanner.nextLine();

        List<JobInfo> jobInfos = jobInfoDAO.getJobInfoByConditions(jobTitle, workLocation, education);

        if (jobInfos != null && !jobInfos.isEmpty()) {
            System.out.println("共找到 " + jobInfos.size() + " 条符合条件的招聘信息。");
            SystemUtil.pause();
            SystemUtil.pagePrint(jobInfos, 10);
        } else {
            System.out.println("未找到符合条件的招聘信息。");
            SystemUtil.clearConsole();
        }
    }

    // 浏览招聘信息
    public void viewJobInfo() {
        List<JobInfo> jobInfos = jobInfoDAO.getAllJobInfo();
        int pageSize = 10;
        SystemUtil.pagePrint(jobInfos, pageSize);
    }

    // 爬取招聘信息
    private void importJobInfo() {
        SystemUtil.clearConsole();
        System.out.println("====爬取招聘信息====");
        int count = jobInfoDAO.crawlJobInfo();
        System.out.println("成功新增 " + count + " 条招聘信息。");
        SystemUtil.pause();
        SystemUtil.clearConsole();
    }
}
