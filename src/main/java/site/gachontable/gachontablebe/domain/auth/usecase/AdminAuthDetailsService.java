package site.gachontable.gachontablebe.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminAuthDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.ADMIN_NOT_FOUND.getMessage()));
        return new AuthDetails(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
    }
}
