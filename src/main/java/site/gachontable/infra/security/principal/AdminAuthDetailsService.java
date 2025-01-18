package site.gachontable.infra.security.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.gachontable.domain.admin.domain.Admin;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.presentation.shared.Role;

@Service
@RequiredArgsConstructor
public class AdminAuthDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(AdminNotFoundException::new);
        return new AuthDetails(admin.getAdminId(), admin.getUsername(), Role.ROLE_ADMIN);
    }
}
