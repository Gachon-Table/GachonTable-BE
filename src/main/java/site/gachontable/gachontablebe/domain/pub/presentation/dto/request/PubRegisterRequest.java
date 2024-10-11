package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

public record PubRegisterRequest(String pubName,
                                 String oneLiner,
                                 String instagramUrl,
                                 Integer hours,
                                 String menuUrl,
                                 Boolean openStatus,
                                 Boolean waitingStatus) {
}
