package ge.games.gegames.security.details;

import ge.games.gegames.entity.user.User;
import ge.games.gegames.entity.user.enums.UserStatusE;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthenticatedUser implements UserDetails {

    private final User user;

    public AuthenticatedUser(User user){
        this.user = user;
    }

    public Long id(){
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getMail();
    }

    @Override
    public boolean isAccountNonLocked(){
        return !user.getUserStatusE().equals(UserStatusE.BANNED);
    }
}
