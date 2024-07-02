package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

public record RegisterRequest(String pubName,
                              String oneLiner,
                              String pubTel,
                              Boolean studentCard,
                              String representativeMenu,
                              String pubLoc,
                              String pubThumbnail,
                              Boolean openStatus) {
}
