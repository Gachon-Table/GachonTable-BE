package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.domain.admin.port.in.ExitUser;
import site.gachontable.presentation.admin.dto.request.ExitUserRequest;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.pub.exception.PubMismatchException;
import site.gachontable.domain.seating.domain.Seating;
import site.gachontable.domain.seating.port.out.SeatingRepository;
import site.gachontable.domain.seating.exception.SeatingNotFoundException;
import site.gachontable.independent.type.SuccessCode;

@Service
@RequiredArgsConstructor
public class ExitUserImpl implements ExitUser {

    private final SeatingRepository seatingRepository;
    private final AdminRepository adminRepository;

    @Transactional
    @Override
    public String execute(AuthDetails authDetails, ExitUserRequest request) {
        Seating seating = seatingRepository.findById(request.seatingId())
                .orElseThrow(SeatingNotFoundException::new);

        checkPubMatches(authDetails, seating);

        seating.updateExitTime();

        return SuccessCode.EXIT_USER_SUCCESS.getMessage();
    }

    private void checkPubMatches(AuthDetails authDetails, Seating seating) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        if (!seating.getPub().equals(pub)) {
            throw new PubMismatchException();
        }
    }
}
