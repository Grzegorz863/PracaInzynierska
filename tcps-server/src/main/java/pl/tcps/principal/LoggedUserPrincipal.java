package pl.tcps.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.tcps.dbEntities.UsersEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LoggedUserPrincipal implements UserDetails {

    private UsersEntity usersEntity;

    public LoggedUserPrincipal(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        String role = null;

        if (usersEntity!=null)
            role = usersEntity.getUserRole();

        if (role!=null)
            grantedAuthorities.add(new GrantedAuthorityImpl(role));

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return usersEntity.getUserPassword();
    }

    @Override
    public String getUsername() {
        return usersEntity.getUserName();
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
        return usersEntity.getIsEnabled();
    }
}
