package site.gachontable.gachontablebe.global.biztalk;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.global.biztalk.dto.request.TemplateParameter;
import site.gachontable.gachontablebe.global.biztalk.dto.response.BiztalkResponse;
import site.gachontable.gachontablebe.global.biztalk.exception.BiztalkSendFailException;
import site.gachontable.gachontablebe.global.biztalk.exception.InvalidKeyException;
import site.gachontable.gachontablebe.global.biztalk.dto.request.BiztalkRequest;
import site.gachontable.gachontablebe.global.biztalk.dto.request.BiztalkRequest.Recipient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
@Slf4j
public class sendBiztalk {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${biztalk.app_key}")
    String appkey;

    @Value("${biztalk.secret_key}")
    String secretKey;

    @Value("${biztalk.sender_key}")
    String senderKey;

    public void execute(String templateCode, String phoneNumber, TemplateParameter templateParameter) {

        Recipient recipient = new Recipient(phoneNumber, templateParameter);

        BiztalkRequest biztalkRequest = new BiztalkRequest(senderKey, templateCode, List.of(recipient));

        try {
            BiztalkResponse biztalkResponse;
            try (InputStream inputStream = getConnection(biztalkRequest).getInputStream()) {
                biztalkResponse = objectMapper.readValue(inputStream, BiztalkResponse.class);
            }

            if (!biztalkResponse.header().isSuccessful()){
                log.error("errorCode: {}, errorMsg: {}", biztalkResponse.header().resultCode(), biztalkResponse.header().resultMessage());
                throw new BiztalkSendFailException();
            }

        } catch (IOException e) {
            throw new BiztalkSendFailException();
        }
    }

    private HttpURLConnection getConnection(BiztalkRequest biztalkRequest) throws IOException {
        URL url = new URL("https://api-alimtalk.cloud.toast.com/alimtalk/v2.3/appkeys/" + appkey + "/messages");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-Secret-Key", secretKey);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setDoOutput(true);

        String jsonRequest = objectMapper.writeValueAsString(biztalkRequest);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new InvalidKeyException();
        }

        return connection;
    }
}
