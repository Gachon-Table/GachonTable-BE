package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.gachontable.domain.admin.domain.Admin;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.port.in.AdminRegister;
import site.gachontable.presentation.admin.dto.request.AdminRegisterRequest;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.pub.domain.repository.PubRepository;
import site.gachontable.domain.pub.exception.PubNotFoundException;
import site.gachontable.domain.shared.Role;
import site.gachontable.domain.shared.dto.response.RegisterResponse;
import site.gachontable.infra.security.jwt.JwtProvider;

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
        String refreshToken = jwtProvider.generateRefreshToken(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
        updateRefreshToken(admin, refreshToken);
    }

    private void updateRefreshToken(Admin admin, String refreshToken) {
        admin.updateRefreshToken(refreshToken);
        adminRepository.save(admin);
    }
}
