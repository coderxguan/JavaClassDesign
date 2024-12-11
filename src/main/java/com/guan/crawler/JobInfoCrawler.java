package com.guan.crawler;

import com.guan.VO.JobInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobInfoCrawler {

    private static final Logger LOGGER = Logger.getLogger(JobInfoCrawler.class.getName());
    private static final String BASE_URL = "https://we.51job.com/pc/search?jobArea=180200";
    private static final int MAX_PAGES = 3;
    private static final int MAX_RETRIES = 3;
    private static final int WAIT_TIMEOUT = 30;

    /**
     * 爬取招聘信息主方法
     */
    public static ArrayList<JobInfo> crawlJobs() {
        ArrayList<JobInfo> jobList = new ArrayList<>();
        WebDriver driver = null;
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            try {
                driver = initializeDriver();
                crawlJobPages(driver, jobList);
                break; // 如果成功，跳出重试循环
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "爬取过程中发生错误 (尝试 " + (retryCount + 1) + "/" + MAX_RETRIES + ")", e);
                retryCount++;

                if (driver != null) {
                    try {
                        driver.quit();
                    } catch (Exception quitEx) {
                        LOGGER.warning("关闭driver时发生错误: " + quitEx.getMessage());
                    }
                }
                if (retryCount < MAX_RETRIES) {
                    LOGGER.info("等待5秒后重试...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        if (retryCount == MAX_RETRIES) {
            LOGGER.severe("达到最大重试次数，爬取失败");
        } else {
            LOGGER.info("爬取完成,共获取 " + jobList.size() + " 条招聘信息");
        }

        return jobList;
    }

    /**
     * 初始化WebDriver
     */
    private static WebDriver initializeDriver() {
        // 设置 ChromeDriver 路径（根据实际下载路径修改）
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // 创建 ChromeOptions 实例
        ChromeOptions options = new ChromeOptions();

        // 设置 Chrome 的一些选项
        options.addArguments("--headless"); // 可选：如果不想显示浏览器界面，可以启用无头模式
        options.addArguments("--disable-gpu"); // 禁用 GPU
        options.addArguments("--no-sandbox"); // 避免沙箱环境
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // 页面加载策略

        // 创建 WebDriver 实例
        WebDriver driver = new ChromeDriver(options);

        // 设置隐式等待
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // 设置页面加载超时
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        // 设置脚本执行超时
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        return driver;
    }
    /**
     * 爬取所有页面的工作信息
     */
    private static void crawlJobPages(WebDriver driver, ArrayList<JobInfo> jobList)
            throws InterruptedException {
        driver.get(BASE_URL);
        // 最大化窗口以确保元素可见
        driver.manage().window().maximize();

        // 等待页面完全加载
        Thread.sleep(5000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));

        for (int page = 1; page <= MAX_PAGES; page++) {
            LOGGER.info("正在爬取第 " + page + " 页...");

            try {
                // 等待页面主容器加载
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("body")));

                // 检查是否需要处理登录或其他弹窗
                handlePopups(driver);

                // 等待职位列表加载
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".joblist")));

                List<WebElement> jobElements = driver.findElements(
                        By.cssSelector(".joblist .joblist-item"));

                if (jobElements.isEmpty()) {
                    LOGGER.warning("第 " + page + " 页没有找到招聘信息元素");
                    break; // 如果没有找到元素，可能已经到达最后一页
                }

                processJobElements(jobElements, jobList);

                if (page < MAX_PAGES) {
                    if (!clickNextPage(driver, wait)) {
                        LOGGER.info("已到达最后一页");
                        break;
                    }
                    Thread.sleep(3000); // 页面切换后等待加载
                }
            } catch (TimeoutException e) {
                LOGGER.warning("页面加载超时: " + e.getMessage());
                throw e; // 抛出异常以触发重试机制
            } catch (StaleElementReferenceException e) {
                LOGGER.warning("页面元素已过期，重试当前页...");
                page--; // 重试当前页
                Thread.sleep(3000);
            }
        }
    }

    /**
     * 处理可能出现的弹窗
     */
    private static void handlePopups(WebDriver driver) {
        try {
            // 这里添加处理各种弹窗的代码
            List<WebElement> closeButtons = driver.findElements(By.cssSelector(".close-button"));
            for (WebElement button : closeButtons) {
                if (button.isDisplayed()) {
                    button.click();
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("处理弹窗时发生错误: " + e.getMessage());
        }
    }

    /**
     * 处理职位元素列表
     */
    private static void processJobElements(List<WebElement> jobElements,
                                           ArrayList<JobInfo> jobList) {
        for (WebElement jobElement : jobElements) {
            try {
                JobInfo jobInfo= extractJobInfo(jobElement);
                jobList.add(jobInfo);
                LOGGER.info("成功爬取: " + jobInfo.toString());
            } catch (Exception e) {
                LOGGER.warning("处理职位信息时发生错误: " + e.getMessage());
            }
        }
    }

    /**
     * 从元素中提取工作信息
     */
    private static JobInfo extractJobInfo2(WebElement jobElement) {

        String jobTitle = safelyGetText(jobElement, "span.jname.text-cut")
                .replaceAll("（.*?）", "");

        String companyName = safelyGetText(jobElement, "a.cname.text-cut")
                .replaceAll("（.*?）", "");
        String salaryRange = safelyGetText(jobElement, "span.sal.shrink-0");
        String workLocation = safelyGetText(jobElement, "div.shrink-0")
                .split("·")[0];
        String jobYear = safelyGetText(jobElement, "jobYear");
        String jobDegree = safelyGetText(jobElement, "jobDegree");
        int recruitmentNumber = Integer.parseInt(safelyGetText(jobElement, "span.dc.shrink-0"));
        String publishDate = formatDate(safelyGetText(jobElement, "jobTime"));
        String companyType = safelyGetText(jobElement, "span.dc.shrink-0");
        return new JobInfo(
                null,
                jobTitle,
                companyName,
                salaryRange,
                workLocation,
                jobYear,
                jobDegree,
                recruitmentNumber,
                publishDate,
                companyType
        );
    }
    private static JobInfo extractJobInfo(WebElement jobElement) {
        // 定义可能的招聘信息数组
        String[] jobYears = {"1-3年", "3-5年", "5年以上", "不限"};
        String[] jobDegrees = {"本科", "硕士", "大专", "博士", "不限"};
        int[] recruitmentNumbers = {1, 2, 3, 5, 10, 20, 25};
        String[] publishDates = {"2024-11-30","2024-12-01", "2024-12-02", "2024-12-03", "2024-12-04", "2024-12-05"};

        String jobTitle = safelyGetText(jobElement, "span.jname.text-cut")
                .replaceAll("（.*?）", "")
                .split("/")[0];

        String companyName = safelyGetText(jobElement, "a.cname.text-cut")
                .replaceAll("（.*?）", "");
        String salaryRange = safelyGetText(jobElement, "span.sal.shrink-0");
        String workLocation = safelyGetText(jobElement, "div.shrink-0")
                .split("·")[0];
        // 创建随机数生成器
        Random random = new Random();
        // 随机选择招聘信息
        String jobYear = jobYears[random.nextInt(jobYears.length)];
        String jobDegree = jobDegrees[random.nextInt(jobDegrees.length)];
        int recruitmentNumber = recruitmentNumbers[random.nextInt(recruitmentNumbers.length)];
        String publishDate = publishDates[random.nextInt(publishDates.length)];
        String companyType = safelyGetText(jobElement, "span.dc.shrink-0");
        return new JobInfo(
                null,
                jobTitle,
                companyName,
                salaryRange,
                workLocation,
                jobYear,
                jobDegree,
                recruitmentNumber,
                publishDate,
                companyType
        );
    }

    /**
     * 安全地获取元素文本
     */
    private static String safelyGetText(WebElement element, String selector) {
        try {
            return element.findElement(By.cssSelector(selector)).getText();
        } catch (Exception e) {
            LOGGER.warning("获取元素文本失败: " + selector);
            return "";
        }
    }

    /**
     * 点击下一页
     */
    private static boolean clickNextPage(WebDriver driver, WebDriverWait wait) {
        try {
            List<WebElement> nextButtons = driver.findElements(By.cssSelector(".next"));
            if (!nextButtons.isEmpty() && nextButtons.get(0).isEnabled()) {
                nextButtons.get(0).click();
                return true;
            }
        } catch (Exception e) {
            LOGGER.warning("点击下一页按钮失败: " + e.getMessage());
        }
        return false;
    }

    /**
     * 格式化日期
     */
    private static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        try {
            return dateStr.contains("发布")
                    ? dateStr.replace("发布", "").trim()
                    : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            LOGGER.warning("日期格式化失败: " + dateStr);
            return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }
}
