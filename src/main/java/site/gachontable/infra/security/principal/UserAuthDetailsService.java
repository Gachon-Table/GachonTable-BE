package site.gachontable.infra.security.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.gachontable.domain.shared.Role;
import site.gachontable.domain.user.domain.User;
import site.gachontable.domain.user.port.out.UserRepository;
import site.gachontable.domain.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserAuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String tel) throws UsernameNotFoundException {
        User user = userRepository.findByUserTel(tel)
                .orElseThrow(UserNotFoundException::new);
        return new AuthDetails(user.getUserId(), user.getUserTel(), Role.ROLE_USER);
    }
}
