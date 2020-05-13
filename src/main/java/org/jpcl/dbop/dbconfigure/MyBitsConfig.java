package org.jpcl.dbop.dbconfigure;


import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author Administrator
 */
@org.springframework.context.annotation.Configuration
public class MyBitsConfig {

    /**
     * 将数据库中的字段 user_id  可以对应成 userId
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
