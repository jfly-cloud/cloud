package com.jfly.oauth.services;
import com.jfly.api.entity.JflyUser;
import com.jfly.oauth.model.JwtUser;
import com.jfly.service.IJflyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OauthUserDetailsService implements UserDetailsService {

    protected static final Logger logger = LoggerFactory.getLogger(OauthUserDetailsService.class);

    @Resource
    private IJflyUserService jflyUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JflyUser jflyUser = jflyUserService.getUserByUserName(username);
        if (null==jflyUser){
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }else{
            /**
             * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
             * 因此这里授予的是用户的资源权限而非角色（角色是变化的，而系统的资源是固定的）
             */
            JwtUser user = new JwtUser(jflyUser.getId(),jflyUser.getUserName(),jflyUser.getPassword(),
                    mapToGrantedAuthorities(Arrays.asList("admin")));
            return user;
        }
    }
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
