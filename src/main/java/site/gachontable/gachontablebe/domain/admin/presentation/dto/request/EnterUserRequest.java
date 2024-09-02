package site.gachontable.gachontablebe.domain.admin.presentation.dto.request;

import java.util.UUID;

public record EnterUserRequest(UUID waitingId,
                               Integer seatingNum) {
}