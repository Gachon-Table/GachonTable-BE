package site.gachontable.gachontablebe.domain.admin.presentation.dto;

import java.util.UUID;

public record EnterUserRequest(
        UUID userId
) {
}