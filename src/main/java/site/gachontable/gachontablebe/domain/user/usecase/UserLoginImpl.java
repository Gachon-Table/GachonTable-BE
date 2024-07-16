package site.gachontable.gachontablebe.domain.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.exception.PasswordNotMatchException;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.domain.user.exception.UserNotFoundException;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

@Service
@RequiredArgsConstructor
public class UserLoginImpl implements UserLogin{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse execute(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        validatePassword(password, user);

        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
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
        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
            updateRefreshToken(user, refreshToken);
        }
        return refreshToken;
    }

    private boolean isValidToken(String token) {
        try {
            jwtProvider.validateToken(token);
            return true;
        } catch (InvalidTokenException | ExpiredTokenException e) {
            return false;
        }
    }

    private void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
