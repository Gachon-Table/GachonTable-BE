package site.gachontable.gachontablebe.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import site.gachontable.gachontablebe.domain.shared.Role;
import site.gachontable.gachontablebe.global.filter.ExceptionHandleFilter;
import site.gachontable.gachontablebe.global.jwt.JwtProvider;
import site.gachontable.gachontablebe.global.jwt.TokenAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // API 명세서

                                .requestMatchers("health-check").permitAll() // 로드 밸런서 상태 확인

                                .requestMatchers("/login").permitAll() // 카카오 로그인
                                .requestMatchers("/admin/test-register", "/admin/login").permitAll() //관리자 로그인
                                .requestMatchers("/user/test-register", "/user/test-login").permitAll() // 개발 테스트 로그인
                                .requestMatchers("user/refresh", "admin/refresh").permitAll() // 토큰 재발급
                                .requestMatchers("pub/register").permitAll() // 주점 등록

                                .requestMatchers("/pub/all", "pub/search").permitAll() //랜딩 페이지

                                .requestMatchers("waiting/cancel").permitAll() // 예약 취소

                                .requestMatchers("waiting/biztalk-status/").permitAll() // 알림톡 웨이팅 조회

                                .requestMatchers("/admin/waitings", "/admin/enter", "/admin/call").hasAuthority(Role.ROLE_ADMIN.getRole()) // 주점 웨이팅 관리
                                .requestMatchers("/admin/status").hasAuthority(Role.ROLE_ADMIN.getRole()) // 주점 상태 변경
                                .requestMatchers("pub/manage").hasAuthority(Role.ROLE_ADMIN.getRole()) // 주점 관리(주점 상세정보 변경)
                                .requestMatchers("waiting/onsite").hasAuthority(Role.ROLE_ADMIN.getRole()) // 현장 웨이팅

                                .requestMatchers("waiting/remote").hasAuthority(Role.ROLE_USER.getRole()) // 원격 웨이팅
                                .requestMatchers("waiting/status", "waiting/history").hasAuthority(Role.ROLE_USER.getRole()) // 마이페이지 웨이팅 현황 및 기록 조회
//                                .requestMatchers("pub/{pubId}").hasAuthority(Role.ROLE_USER.getRole()) // 주점 상세 조회

                                .anyRequest().authenticated()
                );

        http
                .exceptionHandling(exceptionHandlingCustomizer ->
                        exceptionHandlingCustomizer
                                .authenticationEntryPoint(new HttpStatusEntryPoint(FORBIDDEN))
                                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                );

        http
                .oauth2Login(Customizer.withDefaults());

        http
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandleFilter(),
                        TokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:3000",
                "https://api.lupg.me",
                "https://lupg.me",
                "https://www.lupg.me"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
