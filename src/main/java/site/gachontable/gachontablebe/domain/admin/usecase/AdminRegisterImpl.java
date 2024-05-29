package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.dto.response.TestRegisterResponse;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

@Component
@RequiredArgsConstructor
public class AdminRegisterImpl implements AdminRegister {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;

    @Override
    public TestRegisterResponse execute(String username, String password, String tel) {
        Admin admin = createAdmin(username, password, tel);

        String refreshToken = generateRefreshToken(admin);
        admin.updateRefreshToken(refreshToken);

        adminRepository.save(admin);
        return new TestRegisterResponse(true, "어드민 가입 성공");
    }

    private Admin createAdmin(String username, String password, String tel) {
        Admin admin = Admin.create(username, passwordEncoder.encode(password), tel);
        adminRepository.save(admin);
        return admin;
    }

    public String generateRefreshToken(Admin admin) {
        String refreshToken = admin.getRefreshToken();
        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(admin.getAdminId(), admin.getAdminName(), Role.ROLE_ADMIN);
            updateRefreshToken(admin, refreshToken);
        }
        return refreshToken;
    }

    public Boolean isValidToken(String token) {
        try {
            jwtProvider.validateToken(token);
            return true;
        } catch (InvalidTokenException | ExpiredTokenException e) {
            return false;
        }
    }

    private void updateRefreshToken(Admin admin, String refreshToken) {
        admin.updateRefreshToken(refreshToken);
        adminRepository.save(admin);
    }
}
