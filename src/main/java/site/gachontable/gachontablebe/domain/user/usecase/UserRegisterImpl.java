package site.gachontable.gachontablebe.domain.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.dto.response.TestRegisterResponse;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

@Component
@RequiredArgsConstructor
public class UserRegisterImpl implements UserRegister {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TestRegisterResponse execute(String username, String password, String tel) {
        User user = createUser(username, password, tel);
        generateRefreshToken(user);

        return new TestRegisterResponse(true, "유저 가입 성공");
    }

    private User createUser(String username, String password, String tel) {
        User user = User.create(username, passwordEncoder.encode(password), tel, (byte) 0);
        userRepository.save(user);
        return user;
    }

    public void generateRefreshToken(User user) {
        String refreshToken = user.getRefreshToken();

        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getUserName(), Role.ROLE_USER);
            updateUserRefreshToken(user, refreshToken);
        }
    }

    public Boolean isValidToken(String token) {
        try {
            jwtProvider.validateToken(token);
            return true;
        } catch (InvalidTokenException | ExpiredTokenException e) {
            return false;
        }
    }

    private void updateUserRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
