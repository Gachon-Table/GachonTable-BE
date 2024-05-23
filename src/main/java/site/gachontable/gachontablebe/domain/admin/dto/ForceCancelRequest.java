package site.gachontable.gachontablebe.domain.admin.dto;

import jakarta.persistence.Column;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ForceCancelRequest(
        @Column(columnDefinition = "BINARY(16)")
        UUID userId
) {
}