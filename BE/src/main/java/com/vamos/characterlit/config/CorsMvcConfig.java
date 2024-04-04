package com.vamos.characterlit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("Set-Cookie", "access_token")
                .allowedOrigins("http://j10b105.p.ssafy.io:5173", "http://j10b105.p.ssafy.io:5173", "http://j10b105.p.ssafy.io:3000", "https://nid.naver.com", "http://j10b105.p.ssafy.io:8080", "http://j10b105.p.ssafy.io:8081");
    }
}
