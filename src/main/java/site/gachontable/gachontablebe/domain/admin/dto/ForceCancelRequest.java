package site.gachontable.gachontablebe.domain.admin.dto;

import java.util.UUID;

public record ForceCancelRequest(
        UUID userId
) {
}