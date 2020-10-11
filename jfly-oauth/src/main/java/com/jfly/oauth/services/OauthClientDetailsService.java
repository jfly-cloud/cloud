package com.jfly.oauth.services;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jfly.api.entity.OauthClientDetails;
import com.jfly.service.IClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OauthClientDetailsService implements ClientDetailsService {

    protected static final Logger logger = LoggerFactory.getLogger(OauthClientDetailsService.class);

    @Autowired
    private IClientDetailsService clientDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return getClientByClientId(clientId);
    }

    /**
     * 根据客户端id查询
     */
    private BaseClientDetails getClientByClientId(String clientId) {
        OauthClientDetails details = clientDetailsService.getByClientId(clientId);
        BaseClientDetails clientDetails = new BaseClientDetails();
        if (null != details) {
            String authorities = details.getAuthorities();
            clientDetails.setAuthorities(mapToGrantedAuthorities(StrUtil.isNotBlank(authorities) ? CollectionUtil.toList(authorities.split(",")) : new ArrayList<>()));
            clientDetails.setClientId(clientId);
            clientDetails.setClientSecret(passwordEncoder.encode(details.getClientSecret()));
            //设置accessToken和refreshToken的时效，如果不设置则使tokenServices的配置的
            clientDetails.setAccessTokenValiditySeconds(details.getAccessTokenValidity());
            clientDetails.setRefreshTokenValiditySeconds(details.getRefreshTokenValidity());
            String scope = details.getScope();
            clientDetails.setScope(StrUtil.isNotBlank(scope) ? CollectionUtil.toList(scope.split(",")) : null);
            String authorizedGrantTypes = details.getAuthorizedGrantTypes();
            clientDetails.setAuthorizedGrantTypes(StrUtil.isNotBlank(authorizedGrantTypes) ? CollectionUtil.toList(authorizedGrantTypes.split(",")) : null);
            String resourceIds = details.getResourceIds();
            clientDetails.setResourceIds(StrUtil.isNotBlank(resourceIds) ? CollectionUtil.toList(resourceIds.split(",")) : null);
            clientDetails.setRegisteredRedirectUri(CollectionUtil.newHashSet(details.getWebServerRedirectUri()));
            String additionalInformation = details.getAdditionalInformation();
            clientDetails.setAdditionalInformation(StrUtil.isNotBlank(additionalInformation) ? JSON.parseObject(additionalInformation) : new HashMap<>());
            //自动批准作用于，授权码模式时使用，登录验证后直接返回code，不再需要下一步点击授权
            clientDetails.setAutoApproveScopes(CollectionUtil.toList("all"));
        }
        return clientDetails;
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
