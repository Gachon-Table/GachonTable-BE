package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetSeatingsImpl implements GetSeatings {

    private final AdminRepository adminRepository;
    private final SeatingRepository seatingRepository;

    @Transactional(readOnly = true)
    @Override
    public SeatingsResponse execute(AuthDetails authDetails) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        return new SeatingsResponse(
                seatingRepository.findAllByPubAndExitTimeAfterOrderByExitTime(pub, LocalDateTime.now())
                        .stream()
                        .map(Seating::toSeatingResponse)
                        .toList()
        );
    }
}
