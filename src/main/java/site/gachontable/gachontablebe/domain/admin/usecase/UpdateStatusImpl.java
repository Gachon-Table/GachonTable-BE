package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.UpdateStatusRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.shared.dto.response.RegisterResponse;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class UpdateStatusImpl implements UpdateStatus{
    private final AdminRepository adminRepository;
    private final PubRepository pubRepository;

    @Override
    public RegisterResponse execute(AuthDetails authDetails, UpdateStatusRequest request) {
        Pub pub = adminRepository.findByUsername(authDetails.getUsername())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        pub.updateOpenStatus(request.openStatus());
        pubRepository.save(pub);

        return new RegisterResponse(true, SuccessCode.MANAGE_PUB_SUCCESS.getMessage());
    }
}
