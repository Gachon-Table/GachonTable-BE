package site.gachontable.gachontablebe.global.biztalk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateWaitingTemplateParameterRequest(
        @JsonProperty("pubName")
        String pubName,

        @JsonProperty("headCount")
        String headCount,

        @JsonProperty("order")
        String order,

        @JsonProperty("waitingNum")
        String indexOfWaiting,

        @JsonProperty("waitingId")
        String waitingId
) implements TemplateParameter {
}
