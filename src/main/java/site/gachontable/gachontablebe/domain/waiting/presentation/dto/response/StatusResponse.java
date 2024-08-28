package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.util.UUID;

@Builder
public record StatusResponse(UUID waitingId,
                             String pubName,
                             String orderStatus,
                             Integer order,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
                             String createdAt,
                             Integer headCount) {

    public static StatusResponse of(UUID waitingId,
                                    String pubName,
                                    String orderStatus,
                                    Integer order,
                                    String createdAt,
                                    Integer headCount) {
        return new StatusResponse(
                waitingId,
                pubName,
                orderStatus,
                order,
                createdAt,
                headCount
        );
    }

}