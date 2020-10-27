package com.jfly.oauth.configs;

import com.jfly.common.config.jwt.JwtProperties;
import com.jfly.oauth.interceptor.AuthInterceptor;
import com.jfly.oauth.services.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private OauthClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthInterceptor interceptor;

    /**
     * 用来配置令牌端点（Token Endpoint）的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //配置oauth2服务跨域
        CorsConfigurationSource source = new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.addAllowedOrigin(request.getHeader( HttpHeaders.ORIGIN));
                corsConfiguration.addAllowedMethod("*");
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        };
        security
                //允许所有资源服务器访问公钥端点（/oauth/token_key）
                //只允许验证用户访问令牌解析端点（/oauth/check_token）
                .tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder)
                //允许客户端发送表单来进行权限认证来获取令牌
                .allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(new CorsFilter(source));
    }

    /**
     * 将ClientDetailsServiceConfigurer（从您的回调AuthorizationServerConfigurer）可以用来在内存或JDBC实现客户的细节服务来定义的。
     * 客户端的重要属性是:
     * clientId：（必填）客户端ID
     * secret:(可信客户端需要）客户机密码（如果有）,没有可不填
     * scope：客户受限的范围。如果范围未定义或为空（默认值），客户端不受范围限制,read write all
     * authorizedGrantTypes：授予客户端使用授权的类型,默认值为空：
     * 1、授权码模式（authorization_code）
     * 2、简化模式（implicit）
     * 3、密码模式（password）
     * 4、客户端模式（client_credentials）
     * 5、刷新token（refresh_token）
     * authorities授予客户的授权机构（普通的Spring Security权威机构）
     * 客户端的详细信息可以通过直接访问底层商店（例如，在数据库表中JdbcClientDetailsService）或通过ClientDetailsManager接口来更新运行的应用程序。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * authenticationManager：通过注入密码授权被打开AuthenticationManager
     * userDetailsService：如果您注入UserDetailsService或者全局配置，则刷新令牌授权将包含对用户详细信息的检查，以确保该帐户仍然活动
     * authorizationCodeServices：定义AuthorizationCodeServices授权代码授权的授权代码服务（实例）
     * implicitGrantService：在批准期间管理状态
     * tokenGranter：（TokenGranter完全控制授予和忽略上述其他属性）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        //自定义token信息
        defaultTokenServices.setTokenEnhancer(accessTokenConverter);
        //token持久化容器
        defaultTokenServices.setTokenStore(tokenStore);
        //access_token 的有效时长 (秒), 默认 12 小时
        defaultTokenServices.setAccessTokenValiditySeconds(jwtProperties.getExpiration());
        //refresh_token 的有效时长 (秒), 默认 30 天
        defaultTokenServices.setRefreshTokenValiditySeconds(jwtProperties.getExpiration()*2);
        //是否支持refresh_token，默认false
        defaultTokenServices.setSupportRefreshToken(true);
        //是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
        defaultTokenServices.setReuseRefreshToken(false);
        endpoints.authenticationManager(authenticationManager)//通过authenticationManager开启密码授权
                .authorizationCodeServices(authorizationCodeServices)//配置授权码模式授权码服务,不配置默认为内存模式
                .userDetailsService(userDetailsService)//配置redis用户信息
                .tokenServices(defaultTokenServices)//配置token资源服务
                .addInterceptor(interceptor)
                .exceptionTranslator(webResponseExceptionTranslator)//自定义登录或者鉴权失败时的返回信息
                /**
                 pathMapping用来配置端点URL链接，第一个参数是端点URL默认地址，第二个参数是你要替换的URL地址
                 上面的参数都是以“/”开头，框架的URL链接如下：
                 /oauth/authorize：授权端点。----对应的类：AuthorizationEndpoint.java
                 /oauth/token：令牌端点。----对应的类：TokenEndpoint.java
                 /oauth/confirm_access：用户确认授权提交端点。----对应的类：WhitelabelApprovalEndpoint.java
                 /oauth/error：授权服务错误信息端点。
                 /oauth/check_token：用于资源服务访问的令牌解析端点。
                 /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
                 */
                //用户确认授权提交端点配置：最后一个参数为替换之后页面的url
                .pathMapping("/oauth/confirm_access", "/custom/confirm_access");
    }


}
