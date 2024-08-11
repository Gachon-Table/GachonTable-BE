package site.gachontable.gachontablebe.global.biztalk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CallUserTemplateParameterRequest(
        @JsonProperty("pubName")
        String pubName,

        @JsonProperty("waitingId")
        String waitingId
) implements TemplateParameter {
}
