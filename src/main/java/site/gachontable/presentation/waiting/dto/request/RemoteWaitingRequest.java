package site.gachontable.presentation.waiting.dto.request;

import site.gachontable.domain.shared.Table;

public record RemoteWaitingRequest(Integer pubId,
                                   Table tableType) {
}
