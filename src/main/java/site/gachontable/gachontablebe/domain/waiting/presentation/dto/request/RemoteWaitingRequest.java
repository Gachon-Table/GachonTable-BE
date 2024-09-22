package site.gachontable.gachontablebe.domain.waiting.presentation.dto.request;

import site.gachontable.gachontablebe.domain.shared.Table;

public record RemoteWaitingRequest(Integer pubId,
                                   Table tableType) {
}
