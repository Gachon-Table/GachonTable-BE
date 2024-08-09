package site.gachontable.gachontablebe.global.biztalk.dto.request;

import java.util.List;

public record BiztalkRequest(

        String senderKey,

        String templateCode,

        List<Recipient> recipientList) {

    public record Recipient(

            String recipientNo,

            TemplateParameter templateParameter) {
    }
}
