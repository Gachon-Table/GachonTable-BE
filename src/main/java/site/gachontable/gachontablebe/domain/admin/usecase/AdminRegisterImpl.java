package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.AdminRegisterRequest;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

@Service
@RequiredArgsConstructor
public class AdminRegisterImpl implements AdminRegister {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final PubRepository pubRepository;
    private final JwtProvider jwtProvider;

    @Override
    public RegisterResponse execute(AdminRegisterRequest request) {
        Pub pub = pubRepository.findById(request.pubId()).orElseThrow(PubNotFoundException::new);

        Admin admin = Admin.create(request.username(), passwordEncoder.encode(request.password()), request.tel(), pub);
        adminRepository.save(admin);

        generateRefreshToken(admin);

        return new RegisterResponse(true, "어드민 가입 성공");
    }

    public void generateRefreshToken(Admin admin) {
        String refreshToken = admin.getRefreshToken();
        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
            updateRefreshToken(admin, refreshToken);
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

    private void updateRefreshToken(Admin admin, String refreshToken) {
        admin.updateRefreshToken(refreshToken);
        adminRepository.save(admin);
    }
}
