package com.vamos.characterlit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 특정 경로에 대한 접근 제한 해제
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/v1/sse/subscribe").permitAll() // 인증 없이 접근 허용
                        .requestMatchers("/v1/sse/broadcast").permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                );

//            http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
//CSRF해제 필요합니다 작성중!!!
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) -> {
            web
                    .ignoring()
                    .requestMatchers(
                            PathRequest.toStaticResources().atCommonLocations()
                    );
        };
    }
}