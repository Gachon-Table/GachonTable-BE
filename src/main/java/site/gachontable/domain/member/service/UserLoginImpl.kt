package site.gachontable.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.presentation.shared.Role;
import site.gachontable.presentation.shared.exception.PasswordNotMatchException;
import site.gachontable.domain.member.domain.User;
import site.gachontable.domain.member.port.out.UserRepository;
import site.gachontable.domain.member.exception.UserNotFoundException;
import site.gachontable.domain.member.port.in.UserLogin;
import site.gachontable.infra.security.jwt.JwtProvider;
import site.gachontable.infra.security.jwt.dto.JwtResponse;

@Service
@RequiredArgsConstructor
public class UserLoginImpl implements UserLogin {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse execute(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        validatePassword(password, user);

        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getUserTel(), Role.ROLE_USER);
        String refreshToken = generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    private void validatePassword(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    private String generateRefreshToken(User user) {
        String refreshToken = user.getRefreshToken();
        if (refreshToken == null || jwtProvider.isInvalidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
            updateRefreshToken(user, refreshToken);
        }
        return refreshToken;
    }

    private void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
