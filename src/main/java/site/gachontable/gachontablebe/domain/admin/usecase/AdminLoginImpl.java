package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.exception.PasswordNotMatchException;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

@Service
@RequiredArgsConstructor
public class AdminLoginImpl implements AdminLogin {
    private final JwtProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse execute(String adminName, String password) {
        Admin admin = adminRepository.findByAdminName(adminName).orElseThrow(AdminNotFoundException::new);
        validatePassword(password, admin);

        String accessToken = tokenProvider.generateAccessToken(admin.getAdminId(), admin.getAdminName(), Role.ROLE_ADMIN);
        String refreshToken = generateRefreshToken(admin);

        return new JwtResponse(accessToken, refreshToken);
    }

    private void validatePassword(String password, Admin admin) {
        if (!passwordEncoder.matches(password, admin.getAdminPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    private String generateRefreshToken(Admin admin) {
        String refreshToken = admin.getRefreshToken();
        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = tokenProvider.generateRefreshToken(admin.getAdminId(), admin.getAdminName(), Role.ROLE_ADMIN);
            updateAdminRefreshToken(admin, refreshToken);
        }
        return refreshToken;
    }

    private boolean isValidToken(String token) {
        try {
            tokenProvider.validateToken(token);
            return true;
        } catch (InvalidTokenException | ExpiredTokenException e) {
            return false;
        }
    }

    private void updateAdminRefreshToken(Admin admin, String refreshToken) {
        admin.updateRefreshToken(refreshToken);
        adminRepository.save(admin);
    }
}