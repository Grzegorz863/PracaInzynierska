package pl.tcps.principal;

import org.springframework.security.core.GrantedAuthority;

class GrantedAuthorityImpl implements GrantedAuthority {

    private String role = null;

    GrantedAuthorityImpl(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
