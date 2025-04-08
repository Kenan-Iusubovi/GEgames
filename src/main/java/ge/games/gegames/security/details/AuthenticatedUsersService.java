package ge.games.gegames.security.details;

import ge.games.gegames.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUsersService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return new AuthenticatedUser(
                userRepository.findByMail(mail)
                        .orElseThrow(() -> new UsernameNotFoundException("User With email <" + mail + "> not found ! "))
        );
    }
}
