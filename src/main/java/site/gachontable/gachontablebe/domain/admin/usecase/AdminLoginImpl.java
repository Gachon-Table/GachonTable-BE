package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.AdminLoginResponse;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.exception.PasswordNotMatchException;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;

@Service
@RequiredArgsConstructor
public class AdminLoginImpl implements AdminLogin {

    private final JwtProvider jwtProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminLoginResponse execute(String username, String password) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(AdminNotFoundException::new);
        validatePassword(password, admin);

        String accessToken = jwtProvider.generateAccessToken(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
        String refreshToken = generateRefreshToken(admin);
        Integer pubId = admin.getPub().getPubId();

        return new AdminLoginResponse(accessToken, refreshToken, pubId);
    }

    private void validatePassword(String password, Admin admin) {
        if (!passwordEncoder.matches(password, admin.getAdminPassword())) {
            throw new PasswordNotMatchException();
        }
    }

    private String generateRefreshToken(Admin admin) {
        String refreshToken = admin.getRefreshToken();
        if (refreshToken == null || jwtProvider.isInvalidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
            updateAdminRefreshToken(admin, refreshToken);
        }
        return refreshToken;
    }

    private void updateAdminRefreshToken(Admin admin, String refreshToken) {
        admin.updateRefreshToken(refreshToken);
        adminRepository.save(admin);
    }
}
