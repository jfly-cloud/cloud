package com.jfly.oauth.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jfly.api.entity.JflyUser;
import com.jfly.api.mapper.JflyUserMapper;
import com.jfly.service.IJflyUserService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class JflyUserServiceImpl extends ServiceImpl<JflyUserMapper, JflyUser> implements IJflyUserService {

    @Override
    @Cached(name = "jflyUser.",key="#email",cacheType = CacheType.BOTH,localExpire = 1, localLimit =10000,expire = 2,timeUnit = TimeUnit.HOURS)
    public JflyUser getUserByEmail(String email) {
        return baseMapper.selectOne(new QueryWrapper<JflyUser>().eq("email",email));
    }

    @Override
    @Cached(name = "jflyUser.",key="#mobile",cacheType = CacheType.BOTH,localExpire = 1, localLimit =10000,expire = 2,timeUnit = TimeUnit.HOURS)
    public JflyUser getUserByMobile(String mobile) {
        return baseMapper.selectOne(new QueryWrapper<JflyUser>().eq("mobile",mobile));
    }

    @Override
    @Cached(name = "jflyUser.",key="#username",cacheType = CacheType.BOTH,localExpire = 1, localLimit =10000,expire = 2,timeUnit = TimeUnit.HOURS)
    public JflyUser getUserByUserName(String username) {
        return baseMapper.selectOne(new QueryWrapper<JflyUser>().eq("user_name",username));
    }
}
