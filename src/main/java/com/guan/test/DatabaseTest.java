package com.guan.test;

import com.guan.VO.JobInfo;
import com.guan.VO.User;
import com.guan.mapper.JobInfoMapper;
import com.guan.mapper.UserMapper;
import com.guan.utils.MapperUtil;

import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {

        testJob();

    }


    // 用户信息测试
    private static void testUser(){
        UserMapper userMapper = MapperUtil.getMapper(UserMapper.class);
        List<User> users = userMapper.selectAllUsers();
        users.forEach(System.out::println);

        // 关闭连接
        MapperUtil.close();
    }



    // 职位信息测试
    private static void testJob(){
        // 初始化一个Mapper
        JobInfoMapper jobInfoMapper = MapperUtil.getMapper(JobInfoMapper.class);

        JobInfo jobInfo = JobInfo.builder()
                .jobTitle("项目经理")         // 职位名称
                .companyName("科技公司")    // 公司名称
                .salaryRange("10k-15k")             // 月薪范围
                .workLocation("武汉")              // 工作地点
                .experience("3-5年")           // 工作经验要求
                .education("本科及以上")        // 学历要求
                .recruitNum(5)          // 招聘人数
                .publishDate("2024-11-28")        // 发布日期
                .companyType("科技公司")        // 公司性质
                .build();

        jobInfoMapper.insertJobInfo(jobInfo);
        MapperUtil.commit();

        List<JobInfo> jobInfos = jobInfoMapper.selectAllJobInfo();



        jobInfos.forEach(System.out::println);

        // 关闭连接
        MapperUtil.close();
    }
}
