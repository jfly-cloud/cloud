package com.jfly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jfly.api.entity.OauthClientDetails;

public interface IClientDetailsService extends IService<OauthClientDetails> {
    OauthClientDetails getByClientId(String clientId);
}
