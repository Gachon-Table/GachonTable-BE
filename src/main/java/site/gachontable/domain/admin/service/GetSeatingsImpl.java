package site.gachontable.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.domain.admin.port.out.AdminRepository;
import site.gachontable.domain.admin.exception.AdminNotFoundException;
import site.gachontable.domain.admin.port.in.GetSeatings;
import site.gachontable.presentation.admin.dto.response.SeatingsResponse;
import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.domain.pub.domain.Pub;
import site.gachontable.domain.seating.domain.Seating;
import site.gachontable.domain.seating.port.out.SeatingRepository;

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
