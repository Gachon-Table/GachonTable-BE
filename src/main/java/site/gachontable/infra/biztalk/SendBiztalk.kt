package site.gachontable.infra.biztalk

import net.nurigo.sdk.NurigoApp.initialize
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import site.gachontable.infra.biztalk.exception.BiztalkSendFailException

@Configuration
class SendBiztalk(
    @Value("\${biztalk.app_key}")
    private val appKey: String,

    @Value("\${biztalk.secret_key}")
    private val secretKey: String,

    @Value("\${biztalk.pfId}")
    private val pfId: String,

    @Value("\${biztalk.sender_phoneNumber}")
    private val senderPhoneNumber: String,
) {
    private val defaultMessageService: DefaultMessageService =
        initialize(appKey, secretKey, "https://api.solapi.com")

    fun execute(templateId: String, phoneNumber: String, variables: HashMap<String, String>) {
        val kakaoOption = KakaoOption().apply {
            disableSms = true
            this.pfId = this@SendBiztalk.pfId
            this.templateId = templateId
            this.variables = variables
        }

        val message = Message().apply {
            from = senderPhoneNumber
            to = phoneNumber
            kakaoOptions = kakaoOption
        }

        val response = defaultMessageService.sendOne(SingleMessageSendingRequest(message))

        if (response!!.statusCode != "2000") {
            log.error("알림톡 전송 실패. 코드: {}, 메시지: {}", response.statusCode, response.statusMessage)
            throw BiztalkSendFailException()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(SendBiztalk::class.java)
    }
}
