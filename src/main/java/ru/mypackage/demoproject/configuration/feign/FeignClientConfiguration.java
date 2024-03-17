package ru.mypackage.demoproject.configuration.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FeignClientConfiguration {

    @Value("${myToken}")
    private String myToken;

    @Value("${secretToken}")
    private String secretToken;
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", MediaType.APPLICATION_JSON.toString());
            requestTemplate.header("Authorization", "Token " + myToken);
            requestTemplate.header("X-Secret", secretToken);
        };
    }
}
