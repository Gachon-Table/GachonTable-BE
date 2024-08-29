package site.gachontable.gachontablebe.global.biztalk;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import site.gachontable.gachontablebe.global.biztalk.exception.BiztalkSendFailException;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Configuration
public class sendBiztalk {

    private final DefaultMessageService defaultMessageService;
    private final String pfId;
    private final String senderPhoneNumber;

    public sendBiztalk(
            @Value("${biztalk.app_key}") String appkey,
            @Value("${biztalk.secret_key}") String secretKey,
            @Value("${biztalk.pfId}") String pfId,
            @Value("${biztalk.sender_phoneNumber}") String senderPhoneNumber
    ) {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(appkey, secretKey, "https://api.solapi.com");
        this.pfId = pfId;
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public void execute(String templateId, String phoneNumber, HashMap<String, String> variables) {
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setDisableSms(true);

        kakaoOption.setPfId(pfId);
        kakaoOption.setTemplateId(templateId);
        kakaoOption.setVariables(variables);

        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phoneNumber);
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = defaultMessageService.sendOne(new SingleMessageSendingRequest(message));

        if (!Objects.equals(response.getStatusCode(), String.valueOf(HttpURLConnection.HTTP_OK))) {
            log.error("알림톡 전송 실패. 코드: {}, 메시지: {}", response.getStatusCode(), response.getStatusMessage());
            throw new BiztalkSendFailException();
        }
    }
}
