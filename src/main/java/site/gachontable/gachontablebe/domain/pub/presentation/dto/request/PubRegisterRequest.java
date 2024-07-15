package site.gachontable.gachontablebe.domain.pub.presentation.dto.request;

import java.util.List;

public record PubRegisterRequest(String pubName,
                                 String oneLiner,
                                 String instagramUrl,
                                 Boolean studentCard,
                                 String representativeMenu,
                                 String pubLoc,
                                 List<String> thumbnails,
                                 Boolean openStatus) {
}
