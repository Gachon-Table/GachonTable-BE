package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.response.SeatingsResponse;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.seating.domain.Seating;
import site.gachontable.gachontablebe.domain.seating.domain.respository.SeatingRepository;

@RequiredArgsConstructor
@Service
public class GetSeatingsImpl implements GetSeatings {
    private final AdminRepository adminRepository;
    private final SeatingRepository seatingRepository;

    // TODO: 시간 지난 자리는 보여지지 않도록 -> 이것도 정책 정해져야 함.
    @Override
    public SeatingsResponse execute(AuthDetails authDetails) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        return new SeatingsResponse(
                seatingRepository.findAllByPubOrderBySeatingNum(pub)
                        .stream()
                        .map(Seating::toSeatingResponse)
                        .toList()
        );
    }
}
