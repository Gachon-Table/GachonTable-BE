package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.ExitUserRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.exception.PubMismatchException;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;
import site.gachontable.gachontablebe.domain.seating.exception.SeatingNotFoundException;
import site.gachontable.gachontablebe.global.success.SuccessCode;

@Service
@RequiredArgsConstructor
public class ExitUserImpl implements ExitUser{
    private final SeatingRepository seatingRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
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
