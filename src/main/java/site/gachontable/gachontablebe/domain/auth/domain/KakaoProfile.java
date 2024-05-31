package site.gachontable.gachontablebe.domain.auth.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;

@Getter
public record KakaoProfile(String email, String nickname) {

    public static KakaoProfile from(String jsonResponseBody) {
        JsonElement element = JsonParser.parseString(jsonResponseBody);

        JsonObject properties = element.getAsJsonObject().getAsJsonObject("properties");
        String nickname = properties.get("nickname").getAsString();

        JsonObject kakaoAccount = element.getAsJsonObject().getAsJsonObject("kakao_account");
        String email = kakaoAccount.get("email").getAsString();

        return new KakaoProfile(email, nickname);
    }
}
