package site.gachontable.gachontablebe.global.biztalk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReadyUserTemplateParameterRequest(
        @JsonProperty("pubName")
        String pubName,

        @JsonProperty("order")
        String order,

        @JsonProperty("waitingNum")
        String indexOfWaiting,

        @JsonProperty("waitingId")
        String waitingId
) implements TemplateParameter {
}
