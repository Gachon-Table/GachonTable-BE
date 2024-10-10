package site.gachontable.gachontablebe.domain.auth.domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public record AccessToken(String accessToken) {

    public static AccessToken from(String jsonResponseBody) {
        JsonObject response = JsonParser.parseString(jsonResponseBody).getAsJsonObject();
        String accessToken = response.get("access_token").getAsString();

        return new AccessToken(accessToken);
    }
}
