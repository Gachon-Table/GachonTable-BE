package site.gachontable.domain.auth.domain

import com.google.gson.JsonParser

data class KakaoProfile(
    val username: String,
    val tel: String,
) {
    companion object {
        fun from(jsonResponseBody: String): KakaoProfile {
            val `object` = JsonParser.parseString(jsonResponseBody).getAsJsonObject()

            val kakaoAccount = `object`.getAsJsonObject("kakao_account")
            val username = kakaoAccount.get("name").asString
            val tel = kakaoAccount.get("phone_number").asString

            return KakaoProfile(username, tel)
        }
    }
}
