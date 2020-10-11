package com.jfly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jfly.api.entity.JflyUser;

public interface IJflyUserService extends IService<JflyUser> {

    JflyUser getUserByEmail(String email);

    JflyUser getUserByMobile(String mobile);

    JflyUser getUserByUserName(String username);
}
