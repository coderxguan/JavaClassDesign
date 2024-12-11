package com.guan.mapper;


import com.guan.VO.JobInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobInfoMapper {


    // 查询所有职位信息
    List<JobInfo> selectAllJobInfo();

    // 插入职位信息
    void insertJobInfo(JobInfo jobInfo);

    // 根据条件查询职位信息
    List<JobInfo> getJobInfoByConditions(@Param("jobTitle") String jobTitle,
                                         @Param("workLocation") String workLocation,
                                         @Param("education") String education);

    // 根据id查询职位信息
    @Select("select * from job_info where id = #{id}")
    JobInfo getJobInfoById(int id);

    // 修改职位信息
    void updateJobInfo(JobInfo newJobInfo);

    // 删除职位信息
    void deleteJobInfo(int id);

    // 查询职位信息
    JobInfo selectJobInfoByTitleAndName(@Param("jobTitle") String jobTitle,
                                 @Param("companyName") String companyName);
}