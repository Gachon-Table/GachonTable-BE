package site.gachontable.gachontablebe.domain.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
public class UserRegisterImpl implements UserRegister {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse execute(String username, String password, String tel) {
        User user = createUser(username, password, tel);
        generateRefreshToken(user);

        return new RegisterResponse(true, "유저 가입 성공");
    }

    private User createUser(String username, String password, String tel) {
        User user = User.createForTest(username, passwordEncoder.encode(password), tel);
        userRepository.save(user);
        return user;
    }

    public void generateRefreshToken(User user) {
        String refreshToken =
                jwtProvider.generateRefreshToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
        updateUserRefreshToken(user, refreshToken);
    }

    private void updateUserRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
