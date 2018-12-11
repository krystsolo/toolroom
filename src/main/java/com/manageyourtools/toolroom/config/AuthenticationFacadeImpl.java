package com.manageyourtools.toolroom.config;

import com.manageyourtools.toolroom.domains.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean isUserWithAdminAuthority() {

        SimpleGrantedAuthority admin = new SimpleGrantedAuthority(RoleEnum.ADMIN.name());
        Collection<? extends GrantedAuthority> userAuthorities = getAuthentication().getAuthorities();

        return userAuthorities.contains(admin);
    }

    @Override
    public boolean isUserWithWarehouseManAuthority() {

        SimpleGrantedAuthority warehouseman = new SimpleGrantedAuthority(RoleEnum.WAREHOUSEMAN.name());
        Collection<? extends GrantedAuthority> userAuthorities = getAuthentication().getAuthorities();

        return userAuthorities.contains(warehouseman);
    }

    @Override
    public String getUsernameOfCurrentLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
