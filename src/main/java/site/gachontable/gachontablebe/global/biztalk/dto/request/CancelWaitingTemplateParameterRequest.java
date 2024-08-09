package site.gachontable.gachontablebe.global.biztalk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CancelWaitingTemplateParameterRequest(
        @JsonProperty("pubName")
        String pubName
) implements TemplateParameter {
}
