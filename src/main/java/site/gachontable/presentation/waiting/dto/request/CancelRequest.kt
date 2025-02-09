package site.gachontable.presentation.waiting.dto.request;

import java.util.UUID;

public record CancelRequest(UUID waitingId) {
}
