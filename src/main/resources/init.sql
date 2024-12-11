CREATE TABLE user (
                      username VARCHAR(50) NOT NULL PRIMARY KEY, -- 用户名，唯一标识
                      password VARCHAR(255) NOT NULL,           -- 密码，建议存储加密后的值
                      role VARCHAR(50) NOT NULL                -- 角色，例如 admin, user
);

CREATE TABLE job_info (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          job_title VARCHAR(255) NOT NULL,
                          company_name VARCHAR(255) NOT NULL,
                          salary_range VARCHAR(50),
                          work_location VARCHAR(255),
                          experience VARCHAR(100),
                          education VARCHAR(100),
                          recruit_num INT,
                          publish_date DATE,
                          company_type VARCHAR(100)
);