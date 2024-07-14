package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.UpdateStatusRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;

@Service
@RequiredArgsConstructor
public class UpdateStatusImpl implements UpdateStatus{
    private final AdminRepository adminRepository;

    @Override
    public RegisterResponse execute(AuthDetails authDetails, UpdateStatusRequest request) {
        return null;
    }
}
