package com.jfly.oauth.configs;
import com.jfly.oauth.model.JwtUser;
import com.jfly.oauth.services.RedisAuthorizationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.DigestUtils;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class OauthConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * token存储模式为redis
     */
    @Bean
    public TokenStore tokenStore() {
        //return new InMemoryTokenStore();
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置授权码模式授权码服务
     * 数据存储模式为redis
     */
    @Primary
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new RedisAuthorizationCodeService(redisConnectionFactory);
    }

    /**
     * 定义jwttoken的某些属性
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        KeyPair keyPair = new KeyStoreKeyFactory(
                //指定密钥为jfly-cloud
                new ClassPathResource("keystore.jks"), "jfly-cloud".toCharArray())
                //指定密钥对的别名 jfly-cloud
                .getKeyPair("jfly-cloud");
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /**
             * 重写增强token的方法
             * 自定义返回相应的信息
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                //查看UserDetail实现类
                if(null!=authentication.getUserAuthentication()){
                    JwtUser user = (JwtUser) authentication.getUserAuthentication().getPrincipal();
                    //自定义一些token属性
                    final Map<String, Object> additionalInformation = new HashMap<>();
                    additionalInformation.put("userId", user.getId());
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                }
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
                return enhancedToken;
            }
        };
        accessTokenConverter.setKeyPair(keyPair);
        return accessTokenConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            //对密码进行加密
            @Override
            public String encode(CharSequence charSequence) {
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }
            //对密码进行判断匹配
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                boolean res = s.equals( encode );
                return res;
            }
        };
    }
}
