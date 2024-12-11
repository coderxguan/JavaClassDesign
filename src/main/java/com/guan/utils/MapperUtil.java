package com.guan.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MapperUtil {

    private static final SqlSessionFactory sqlSessionFactory;
    private static SqlSession sqlSession;  // 维护一个 SqlSession 实例

    static {
        try {
            String resource = "mybatis-config.xml"; // MyBatis 配置文件路径
            InputStream inputStream = MapperUtil.class.getClassLoader().getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("MyBatis initialization failed", e);
        }
    }

    // 获取 Mapper 实例
    public static <T> T getMapper(Class<T> mapperClass) {
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession();  // 创建新的 SqlSession
        }
        return sqlSession.getMapper(mapperClass);
    }

    // 提交事务
    public static void commit() {
        if (sqlSession != null) {
            sqlSession.commit();  // 提交事务
        }
    }

    // 关闭 SqlSession
    public static void close() {
        if (sqlSession != null) {
            sqlSession.close();  // 关闭 SqlSession
            sqlSession = null;  // 清除引用，避免内存泄漏
        }
    }

}
