package site.gachontable.gachontablebe.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.gachontable.gachontablebe.domain.auth.domain.KakaoProfile;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final static String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;

    public User getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(USER_INFO_URI, profileRequest, String.class);

        return getUser(KakaoProfile.from(response.getBody()));
    }

    private User getUser(KakaoProfile kakaoProfile) {
        String userName = kakaoProfile.email();
        String tel = kakaoProfile.nickname();

        Optional<User> byUserName = userRepository.findByUsername(userName);

        if (byUserName.isPresent()) {
            return byUserName.get();
        }

        User user = User.create(userName, tel);

        return userRepository.save(user);
    }
}
