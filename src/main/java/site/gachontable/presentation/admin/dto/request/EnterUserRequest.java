package site.gachontable.presentation.admin.dto.request;

import java.util.UUID;

public record EnterUserRequest(UUID waitingId,
                               Integer seatingNum) {
}