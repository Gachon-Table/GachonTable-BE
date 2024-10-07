package site.gachontable.gachontablebe.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.gachontable.gachontablebe.domain.auth.domain.AccessToken;
import site.gachontable.gachontablebe.domain.auth.domain.KakaoProfile;
import site.gachontable.gachontablebe.domain.auth.presentation.dto.response.AuthResponse;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;


@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Transactional
    public AuthResponse getUserInfo(String code) {
        String token = getToken(code);
        return getUserInfoFromToken(token);
    }

    private String getToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(TOKEN_URI, tokenRequest, String.class);

        return AccessToken.from(response.getBody()).accessToken();
    }

    public AuthResponse getUserInfoFromToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(USER_INFO_URI, profileRequest, String.class);

        User user = getUser(KakaoProfile.from(response.getBody()));

        return createToken(user);
    }

    private User getUser(KakaoProfile kakaoProfile) {
        String kakaoTel = kakaoProfile.tel();
        String tel = kakaoTel.replace("+82 ", "0");
        String username = kakaoProfile.username();

        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.create(username, tel)));
    }

    private AuthResponse createToken(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
        String refreshToken = generateRefreshToken(user);

        userRepository.save(user);

        return new AuthResponse(accessToken, refreshToken, user.getUsername());
    }

    private String generateRefreshToken(User user) {
        String refreshToken = user.getRefreshToken();
        if (refreshToken == null || !isValidToken(refreshToken)) {
            refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
            updateRefreshToken(user, refreshToken);
        }
        return refreshToken;
    }

    private boolean isValidToken(String token) {
        try {
            jwtProvider.validateToken(token);
            return true;
        } catch (InvalidTokenException | ExpiredTokenException e) {
            return false;
        }
    }

    private void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
