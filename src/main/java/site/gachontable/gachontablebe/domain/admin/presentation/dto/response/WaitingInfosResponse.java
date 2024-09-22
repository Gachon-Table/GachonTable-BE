package site.gachontable.gachontablebe.domain.admin.presentation.dto.response;

import lombok.Builder;
import site.gachontable.gachontablebe.domain.shared.Table;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;
import site.gachontable.gachontablebe.domain.waiting.type.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WaitingInfosResponse(Integer count, List<WaitingInfosResponse.WaitingInfo> waitingInfos) {

    @Builder
    public record WaitingInfo(String username, LocalDateTime time, Table tableType, String tel, UUID waitingId, Status waitingStatus) {
        public static WaitingInfo of(String userName, Waiting waiting) {
            return WaitingInfo.builder()
                    .username(userName)
                    .time(waiting.getCreatedAt())
                    .tableType(waiting.getTableType())
                    .tel(waiting.getTel())
                    .waitingId(waiting.getWaitingId())
                    .waitingStatus(waiting.getWaitingStatus())
                    .build();
        }
    }
}
