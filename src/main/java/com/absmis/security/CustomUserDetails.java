package com.absmis.security;


import com.absmis.domain.authority.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
/*UserDetails代表了Spring Security的用户认证实体，
带有用户名、密码、权限列表、过期特性等性质，
*/
public class CustomUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String role;

    public CustomUserDetails(User user,String role) {
        super(user);
        this.role = role;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getEnterpriseType() {
        return null;
    }


}
