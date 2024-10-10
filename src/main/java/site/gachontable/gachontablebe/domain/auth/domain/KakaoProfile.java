package site.gachontable.gachontablebe.domain.auth.domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public record KakaoProfile(String username, String tel) {

    public static KakaoProfile from(String jsonResponseBody) {
        JsonObject object = JsonParser.parseString(jsonResponseBody).getAsJsonObject();

        JsonObject kakaoAccount = object.getAsJsonObject("kakao_account");
        String username = kakaoAccount.get("name").getAsString();
        String tel = kakaoAccount.get("phone_number").getAsString();

        return new KakaoProfile(username, tel);
    }
}
