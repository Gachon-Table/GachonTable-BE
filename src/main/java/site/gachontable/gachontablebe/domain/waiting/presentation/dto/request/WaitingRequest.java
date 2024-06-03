package site.gachontable.gachontablebe.domain.waiting.presentation.dto.request;

import site.gachontable.gachontablebe.domain.waiting.type.Position;

public record WaitingRequest(Position position,
                             Integer headCount) {
}
