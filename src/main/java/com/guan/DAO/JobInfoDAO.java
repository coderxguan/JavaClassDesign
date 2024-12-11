package com.guan.DAO;

import com.guan.VO.JobInfo;
import com.guan.crawler.JobInfoCrawler;
import com.guan.mapper.JobInfoMapper;
import com.guan.utils.MapperUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JobInfoDAO {

    private final JobInfoMapper jobInfoMapper = MapperUtil.getMapper(JobInfoMapper.class);

    // 查询所有招聘信息
    public List<JobInfo> getAllJobInfo() {
        return jobInfoMapper.selectAllJobInfo();
    }

    // 根据条件查询招聘信息
    public List<JobInfo> getJobInfoByConditions(String jobTitle, String workLocation, String education) {
        if (jobTitle == null || jobTitle.trim().isEmpty())
            jobTitle = null;
        if (workLocation == null || workLocation.trim().isEmpty())
            workLocation = null;
        if (education == null || education.trim().isEmpty())
            education = null;

        return jobInfoMapper.getJobInfoByConditions(jobTitle, workLocation, education);
    }

    // 根据id查询招聘信息
    public JobInfo getJobInfoById(int id) {
        return jobInfoMapper.getJobInfoById(id);
    }

    // 修改招聘信息
    public void modifyJobInfo(JobInfo newJobInfo) {
        jobInfoMapper.updateJobInfo(newJobInfo);
    }

    // 删除招聘信息
    public void deleteJobInfo(int id) {
        jobInfoMapper.deleteJobInfo(id);
        MapperUtil.commit();
    }


    // 导出招聘信息到txt文件
    public void exportJobInfoToTxt(String fileName) {
        List<JobInfo> jobInfos = getAllJobInfo();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // 写入标题
            writer.write("ID,职位名称,公司名称,薪资范围,工作地点,工作经验,学历要求,招聘人数,发布时间,公司性质");
            writer.newLine();
            for (JobInfo jobInfo : jobInfos) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%d,%s,%s",
                        jobInfo.getId(),
                        jobInfo.getJobTitle(),
                        jobInfo.getCompanyName(),
                        jobInfo.getSalaryRange(),
                        jobInfo.getWorkLocation(),
                        jobInfo.getExperience(),
                        jobInfo.getEducation(),
                        jobInfo.getRecruitNum(),
                        jobInfo.getPublishDate(),
                        jobInfo.getCompanyType()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 导出招聘信息到excel文件
    public void exportJobInfoToExcel(String fileName) {
        List<JobInfo> jobInfos = getAllJobInfo();
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new java.io.File(fileName));
            WritableSheet sheet = workbook.createSheet("Job Info", 0);

            // 添加标题
            sheet.addCell(new Label(0, 0, "ID"));
            sheet.addCell(new Label(1, 0, "职位名称"));
            sheet.addCell(new Label(2, 0, "公司名称"));
            sheet.addCell(new Label(3, 0, "薪资范围"));
            sheet.addCell(new Label(4, 0, "工作地点"));
            sheet.addCell(new Label(5, 0, "工作经验"));
            sheet.addCell(new Label(6, 0, "学历要求"));
            sheet.addCell(new Label(7, 0, "招聘人数"));
            sheet.addCell(new Label(8, 0, "发布时间"));
            sheet.addCell(new Label(9, 0, "公司性质"));

            // 添加数据
            int row = 1;
            for (JobInfo jobInfo : jobInfos) {
                sheet.addCell(new Label(0, row, jobInfo.getId().toString()));
                sheet.addCell(new Label(1, row, jobInfo.getJobTitle()));
                sheet.addCell(new Label(2, row, jobInfo.getCompanyName()));
                sheet.addCell(new Label(3, row, jobInfo.getSalaryRange()));
                sheet.addCell(new Label(4, row, jobInfo.getWorkLocation()));
                sheet.addCell(new Label(5, row, jobInfo.getExperience()));
                sheet.addCell(new Label(6, row, jobInfo.getEducation()));
                sheet.addCell(new Label(7, row, jobInfo.getRecruitNum().toString()));
                sheet.addCell(new Label(8, row, jobInfo.getPublishDate()));
                sheet.addCell(new Label(9, row, jobInfo.getCompanyType()));
                row++;
            }

            workbook.write();
            workbook.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }

    // 爬取招聘信息
    public int crawlJobInfo() {
        List<JobInfo> jobInfos = JobInfoCrawler.crawlJobs();
        int row = 0;

        for (JobInfo jobInfo : jobInfos) {
            JobInfo jobInfo1 =  jobInfoMapper.selectJobInfoByTitleAndName(
                    jobInfo.getJobTitle(), jobInfo.getJobTitle());
            if(jobInfo1 != null){
                continue;
            }
            jobInfoMapper.insertJobInfo(jobInfo);
            row++;
        }
        MapperUtil.commit();
        // 返回成功更新的条数
        return row;
    }

}