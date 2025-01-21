package topg.url_shortener.Jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import topg.url_shortener.models.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserImplDetails implements UserDetails {
    private static final long serialVersionUID = 1L;    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserImplDetails(Collection<? extends GrantedAuthority> grantedAuthorities, String password, String email, String username, Long id) {
        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.email = email;
        this.username = username;
        this.id = id;
    }

    public static UserImplDetails build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new UserImplDetails(
                Collections.singletonList(authority),
                user.getPassword(),
                user.getEmail(),
                user.getUsername(),
                user.getId()

        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
