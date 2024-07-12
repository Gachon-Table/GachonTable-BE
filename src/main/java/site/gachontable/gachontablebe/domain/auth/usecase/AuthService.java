package site.gachontable.gachontablebe.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.gachontable.gachontablebe.domain.auth.domain.KakaoProfile;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.user.domain.repository.UserRepository;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.dto.JwtResponse;
import site.gachontable.gachontablebe.global.jwt.exception.ExpiredTokenException;
import site.gachontable.gachontablebe.global.jwt.exception.InvalidTokenException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final static String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public JwtResponse getUserInfo(String token) {
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
        String tel = kakaoProfile.email();
        String username = kakaoProfile.nickname();

        Optional<User> byUserName = userRepository.findByUsername(username);

        if (byUserName.isPresent()) {
            return byUserName.get();
        }

        User user = User.create(username, tel);

        return userRepository.save(user);
    }

    private JwtResponse createToken(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getUsername(), Role.ROLE_USER);
        String refreshToken = generateRefreshToken(user);

        userRepository.save(user);

        return new JwtResponse(accessToken, refreshToken);
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
