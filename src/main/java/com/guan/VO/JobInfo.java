package com.guan.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobInfo {
    // ID
    private Integer id;
    // 职位名称
    private String jobTitle;
    // 公司名称
    private String companyName;
    // 薪资范围
    private String salaryRange;
    // 工作地点
    private String workLocation;
    // 工作经验
    private String experience;
    // 学历要求
    private String education;
    // 招聘人数
    private Integer recruitNum;
    // 发布时间
    private String publishDate;
    // 公司性质
    private String companyType;
}
