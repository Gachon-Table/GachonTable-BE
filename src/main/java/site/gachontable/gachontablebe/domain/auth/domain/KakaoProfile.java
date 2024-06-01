package site.gachontable.gachontablebe.domain.auth.domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public record KakaoProfile(String email, String nickname) {

    public static KakaoProfile from(String jsonResponseBody) {
        JsonObject object = JsonParser.parseString(jsonResponseBody).getAsJsonObject();

        JsonObject properties = object.getAsJsonObject("properties");
        String nickname = properties.get("nickname").getAsString();

        JsonObject kakaoAccount = object.getAsJsonObject("kakao_account");
        String email = kakaoAccount.get("email").getAsString();

        return new KakaoProfile(email, nickname);
    }
}
