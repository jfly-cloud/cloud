package com.jfly.oauth;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.jfly")
@EnableMethodCache(basePackages = "com.jfly")
@EnableCreateCacheAnnotation
public class OauthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ OauthServer Start Successfully (♥◠‿◠)ﾉﾞ");
    }
}