package com.manageyourtools.toolroom.config;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    Authentication getAuthentication();

    String getUsernameOfCurrentLoggedUser();

    boolean isUserWithAdminAuthority();

    boolean isUserWithWarehouseManAuthority();

}
