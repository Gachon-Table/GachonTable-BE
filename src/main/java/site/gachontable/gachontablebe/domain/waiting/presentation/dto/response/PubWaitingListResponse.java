package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;


import lombok.Builder;
import site.gachontable.gachontablebe.domain.waiting.domain.Waiting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PubWaitingListResponse(Integer count, List<WaitingInfo> waitingInfoList) {

    @Builder
    public record WaitingInfo(String username, LocalDateTime time, Integer headCount, String tel, UUID waitingId){
        public static WaitingInfo of(String userName, Waiting waiting) {
            return WaitingInfo.builder()
                    .username(userName)
                    .time(waiting.getCreatedAt())
                    .headCount(waiting.getHeadCount())
                    .tel(waiting.getTel())
                    .waitingId(waiting.getWaitingId())
                    .build();
        }
    }
}


