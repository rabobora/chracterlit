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
                // csrf 차단 해체
                .csrf(csrf -> csrf.disable())
                // 특정 경로에 대한 접근 제한 해제
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/sse/subscribe/**").permitAll() // 인증 없이 접근 허용
                        .requestMatchers("/api/sse/disconnect").permitAll() // 인증 없이 접근 허용
                        .requestMatchers("/api/bid/read/**").permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                );

        return http.build();
    }
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