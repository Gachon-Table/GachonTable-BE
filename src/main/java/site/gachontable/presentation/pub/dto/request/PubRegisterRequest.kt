package site.gachontable.presentation.pub.dto.request;

public record PubRegisterRequest(String pubName,
                                 String oneLiner,
                                 String instagramUrl,
                                 Integer minutes,
                                 String menuUrl,
                                 Boolean openStatus,
                                 Boolean waitingStatus) {
}
