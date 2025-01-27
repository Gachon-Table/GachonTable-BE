package site.gachontable.presentation.waiting.dto.request;

import site.gachontable.presentation.shared.Table;

public record RemoteWaitingRequest(Integer pubId,
                                   Table tableType) {
}
