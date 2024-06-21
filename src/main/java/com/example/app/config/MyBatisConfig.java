package com.example.app.config;

import java.util.UUID;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.app.handler.UUIDTypeHandler;

/**
 * MyBatis設定
 * @since 2024/06/21
 * @author koji kawazu
 */
@Configuration
public class MyBatisConfig {

	/**
	 * SqlSessionFactoryのBeanを定義
	 * @param dataSource
	 * @return SqlSessionFactory
	 * @throws Exception
	 */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // MyBatisの設定をカスタマイズ
        org.apache.ibatis.session.Configuration myBatisConfiguration = new org.apache.ibatis.session.Configuration();
        // UUID TypeHandlerを登録
        myBatisConfiguration.getTypeHandlerRegistry().register(UUID.class, UUIDTypeHandler.class);
        // キャッシュの変更（デフォルトはtrue、ここでは無効化）
        myBatisConfiguration.setCacheEnabled(false);
        
        // カスタマイズした設定をSqlSessionFactoryBeanに適用
        sessionFactory.setConfiguration(myBatisConfiguration);
        return sessionFactory.getObject();
    }
}
