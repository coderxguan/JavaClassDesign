package com.guan.utils;


import com.guan.VO.JobInfo;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SystemUtil {
    static Scanner scanner = new Scanner(System.in);

    // 清空控制台
    public static void clearConsole() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    // 暂停
    public static void pause() {
        System.out.println("请按任意键继续...");
        scanner.nextLine();
    }

    // 分页打印招聘信息
    public static void pagePrint(List<JobInfo> jobInfos, int pageSize){
        int page = 0;
        int total = jobInfos.size();
        // Print table header
        String header = String.format("%-4s %-15s %-25s %-10s %-10s %-10s %-10s %-10s %-15s %-10s",
                "编号", "职位名称", "公司名称", "薪资范围", "工作地点", "工作经验", "学历要求", "招聘人数", "发布日期", "公司类型");

        for (JobInfo jobInfo : jobInfos) {
            page++;
            if ((page - 1) % pageSize == 0) {
                SystemUtil.clearConsole();
                System.out.println("====招聘信息列表 第" + (page / pageSize + 1) + "页 共" + total + "条====");
                System.out.println(header);
                System.out.println("-".repeat(148));
            }
            String row = String.format("%-5s %-15s %-22s %-14s %-10s %-12s %-12s %-12d %-18s %-10s",
                    jobInfo.getId(),
                    jobInfo.getJobTitle(), jobInfo.getCompanyName(), jobInfo.getSalaryRange(), jobInfo.getWorkLocation(),
                    jobInfo.getExperience(), jobInfo.getEducation(), jobInfo.getRecruitNum(), jobInfo.getPublishDate(), jobInfo.getCompanyType());
            System.out.println(row);
            if (page % pageSize == 0 || page == total) {
                System.out.println("-".repeat(148));
                SystemUtil.pause();
            }
        }
        SystemUtil.clearConsole();
    }

    // 打印招聘信息
    public static void printJobInfo(JobInfo jobInfo, int id){
        String header = String.format("%-4s %-15s %-25s %-10s %-10s %-10s %-10s %-10s %-15s %-10s",
                "编号", "职位名称", "公司名称", "薪资范围", "工作地点", "工作经验", "学历要求", "招聘人数", "发布日期", "公司类型");
        System.out.println("====编号为"+ id +"的招聘信息====");
        System.out.println(header);
        System.out.println("-".repeat(148));
        String row = String.format("%-5s %-15s %-22s %-14s %-10s %-12s %-12s %-12d %-18s %-10s",
                jobInfo.getId(),
                jobInfo.getJobTitle(), jobInfo.getCompanyName(), jobInfo.getSalaryRange(), jobInfo.getWorkLocation(),
                jobInfo.getExperience(), jobInfo.getEducation(), jobInfo.getRecruitNum(), jobInfo.getPublishDate(), jobInfo.getCompanyType());
        System.out.println(row);
        System.out.println("-".repeat(148));
    }
}
