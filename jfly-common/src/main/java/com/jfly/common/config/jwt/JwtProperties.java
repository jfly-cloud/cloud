package com.jfly.common.config.jwt;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String header;

    private String secret;

    private Integer expiration;

    private Integer dataPeriod;

    private String authPath;

    private String md5Key;
}