package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

import java.util.List;

public record PubRegisterRequest(String pubName,
                                 String oneLiner,
                                 String instagramUrl,
                                 Integer hours,
                                 String menuUrl,
                                 List<String> thumbnails,
                                 Boolean openStatus,
                                 Boolean waitingStatus) {
}
