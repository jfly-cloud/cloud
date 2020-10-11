package com.jfly.oauth.model;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class JwtUser extends User {

    private  Long id;

    public JwtUser( Long id,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }
}
