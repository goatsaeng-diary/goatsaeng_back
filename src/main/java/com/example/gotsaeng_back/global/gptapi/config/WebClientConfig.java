package com.example.gotsaeng_back.global.gptapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // application.yml 또는 application.properties 파일에서 openai.api-key 값을 읽어옴
    @Value("${openai.api-key}")
    private String apiKey;

    // WebClient를 설정하는 Bean을 정의
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openai.com/v1")  // OpenAI API 기본 URL 설정
                .defaultHeader("Authorization", "Bearer " + apiKey)  // OpenAI API 인증용 커스텀 헤더 설정
                .defaultHeader("Content-Type", "application/json")  // 요청 본문이 JSON 형식임을 명시
                .build();  // 설정된 WebClient 인스턴스를 반환
    }

    @Bean
    public RestTemplate template() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().set("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
