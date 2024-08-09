package site.gachontable.gachontablebe.global.biztalk.dto.response;

import java.util.List;

public record BiztalkResponse(
        Header header,
        Message message
) {
    public record Header(
            Integer resultCode,
            String resultMessage,
            Boolean isSuccessful
    ) {
    }

    public record Message(
            String requestId,
            String senderGroupingKey,
            List<SendResult> sendResults
    ) {
        public record SendResult(
                Integer recipientSeq,
                String recipientNo,
                Integer resultCode,
                String resultMessage,
                String recipientGroupingKey
        ) {
        }
    }
}
