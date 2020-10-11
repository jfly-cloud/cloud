package com.jfly.oauth.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jfly.api.entity.OauthClientDetails;
import com.jfly.api.mapper.ClientDetailsMapper;
import com.jfly.service.IClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class ClientDetailsServiceImpl extends ServiceImpl<ClientDetailsMapper, OauthClientDetails> implements IClientDetailsService {

    protected static final Logger logger = LoggerFactory.getLogger(ClientDetailsServiceImpl.class);


    @Override
    @Cached(name = "clientDetails.",key="#clientId",cacheType = CacheType.BOTH,localExpire = 30, localLimit =1000,expire = 60,timeUnit = TimeUnit.MINUTES)
    public OauthClientDetails getByClientId(String clientId) {
        return baseMapper.selectOne(new QueryWrapper<OauthClientDetails>().eq("client_id", clientId));
    }
}
