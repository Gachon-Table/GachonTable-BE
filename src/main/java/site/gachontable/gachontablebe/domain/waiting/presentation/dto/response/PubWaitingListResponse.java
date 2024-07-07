package site.gachontable.gachontablebe.domain.waiting.presentation.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public record PubWaitingListResponse(Integer count, List<WaitingInfo> waitingInfoList) {
    public record WaitingInfo(String username, LocalDateTime time, Integer headCount, String tel){}
}


