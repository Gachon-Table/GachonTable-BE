package site.gachontable.domain.auth.domain

import com.google.gson.JsonParser

data class AccessToken(
    val accessToken: String,
) {
    companion object {
        fun from(jsonResponseBody: String): AccessToken {
            val response = JsonParser.parseString(jsonResponseBody).getAsJsonObject()
            val accessToken = response.get("access_token").asString

            return AccessToken(accessToken)
        }
    }
}
