package com.jfly.api.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class HikariProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private String poolName;

    private int minIdle = 10;

    private int MaximumPoolSize = 100;

    private int idleTimeout = 60000;

    private int connectionTimeout = 30000;

    public HikariConfig config() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setPoolName(poolName);
        //从池返回的连接的默认自动提交行为,默认值：true
        config.setMinimumIdle(minIdle);             //最小空闲
        config.setMaximumPoolSize(MaximumPoolSize); //连接池最大连接数，默认是10
        // 空闲连接存活最大时间，默认600000（10分钟）
        config.setIdleTimeout(idleTimeout);
        //数据库连接超时时间,默认30秒，即30000
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery("SELECT 1;");
        //https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        return config;
    }
}
